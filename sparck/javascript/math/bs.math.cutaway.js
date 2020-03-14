/* MIT License
 *
 * Copyright (c) 2012-2020 tecartlab.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author maybites
 *
 */

autowatch = 1;

// set up inlets/outlets/assist strings
inlets = 3;
outlets = 2;
setinletassist(0,"data");
setinletassist(1,"camera transformations");
setinletassist(2,"cutaway transformations");
setoutletassist(0,"transform");
setoutletassist(1,"refresh");

var vectors = require("bs.math.package.vector");
var matrices = require("bs.math.package.matrix");
var quaternions = require("bs.math.package.quat");

var undefined = "undefined";

var cameraPos = new Array(0, 0, 1);

var cutawayPos = new Array(0, 0, 0);
var cutawayQuat = new Array(0, 0, 0, 1);

var cutawayNormal = new Array(0, 0, 1);;

var cutawayPlaneMatrix = undefined;

var cameraMirrorPos = new Array(0, 0, 1);
var cameraLookAtPos = new Array(0, 0, 0);
var cameraUp;

var fovAngleTangens;

var frustumScale;
var frustumLookAt;
var frustumLookAtLength;
var frustum2NormalDot;
var frustumLookAtTangens;

// create y-z plane normal
var	flipNormal = vectors.rotateVecQuat(new Array(1, 0, 0), new Array(0, 0, 0, 1));
// create y-z plane reflection matrix
var	matFlip = matrices.reflectionPlaneToMat4(flipNormal, 0);

var upX = 0.;
var upY = 0.;
var upZ = 1.;

var zaxisX;
var zaxisY;
var zaxisZ;

var xaxisX;
var xaxisY;
var xaxisZ;

var yaxisX;
var yaxisY;
var yaxisZ;

var zAxisLength;
var xAxisLength;
var yAxisLength;

var hasChanged = false;

var mat = new Array(16);

var fovAngle;

var near;
var far;
var right = .05;
var left = -.05;
var top = .05;
var bottom = -.05;

// ***********************

var vecNormal = new Array(0, 0, 1);

var ratio_orig = 1.0;
var fovAngle_orig = 50.0;
var near_orig = 0.1;
var far_orig = 10.;

var adapt_state = 0;
var adapt_reference = 1;

function loadbang(){
	mirrorplane(0);
}

function bang(){
	if(hasChanged)
		calc();
}

function worldpos(){
    if(inlet == 1){
        cameraPos = arrayfromargs(arguments);
        hasChanged = true;
    } else if(inlet == 2){
        cutawayPos = arrayfromargs(arguments);
    }
}

function worldquat(){
    if(inlet == 2){
        cutawayQuat = arrayfromargs(arguments);
        evaluateCutawayRotationals();
    }
}

function evaluateCutawayRotationals(){
	// calculate mirror plane normal and mirror-plane-d
	//		from quat-rotation and position
	cutawayNormal = vectors.rotateVecQuat(vecNormal, cutawayQuat);
	var d = - vectors.dot(cutawayNormal, cutawayPos);
	// calculate reflection matrix
	cutawayPlaneMatrix = matrices.reflectionPlaneToMat4(cutawayNormal, d);
	hasChanged = true;
}

function fov(){
	var args = arrayfromargs(arguments);
	ratio_orig = args[0];
	fovAngle_orig = args[1];
	near_orig = args[2];
	far_orig = (args[3] > args[2])?args[3]: args[2] + 0.1;
	hasChanged = true;
}

function mirrorplane(dir){
	if(dir == 0){
		vecNormal = new Array(0, 0, 1);
	} else if(dir == 1){
		vecNormal = new Array(0, 1, 0);
	} else if(dir == 2){
		vecNormal = new Array(1, 0, 0);
	}
	evaluateCutawayRotationals()
//	hasChanged == true;
//	outlet(1, "bang");
}

function adapt(state){
	adapt_state = state;
    hasChanged = true;
}

function adaptReference(val){
	adapt_reference = val;
    hasChanged = true;
}

function calc(){

    // **************************************
    //
    //         CALCULATE TRANSFORMATION
    //
    // **************************************

    // calculate object mirrored position
    cameraMirrorPos = vectors.multiplyVec3ByMat4(cameraPos, cutawayPlaneMatrix);

    // calculate the closest point to the camera on the cutaway plane
    cameraLookAtPos = vectors.scaleVecScal(vectors.add(cameraPos, cameraMirrorPos), 0.5);

    //calculate the camera up vector
    cameraUp = vectors.sub(cutawayPos, cameraLookAtPos);

    // if the vector looks straight at the camera, we have a special case and need to change the direction
    // of the up vector:
    if(vectors.length(cameraUp) < 0.000001){
        cameraUp[0] = cutawayNormal[1];
        cameraUp[1] = cutawayNormal[2];
        cameraUp[2] = cutawayNormal[0];
    }

    upX = cameraUp[0];
    upY = cameraUp[1];
    upZ = cameraUp[2];

    zaxisX = cameraPos[0] - cameraLookAtPos[0];
    zaxisY = cameraPos[1] - cameraLookAtPos[1];
    zaxisZ = cameraPos[2] - cameraLookAtPos[2];

	zAxisLength = Math.sqrt(zaxisX * zaxisX + zaxisY * zaxisY + zaxisZ * zaxisZ);

	zaxisX = zaxisX / zAxisLength;
	zaxisY = zaxisY / zAxisLength;
	zaxisZ = zaxisZ / zAxisLength;

	//xaxis = normal(cross(Up, zaxis))

	xaxisX = upY * zaxisZ - upZ * zaxisY;
	xaxisY = upZ * zaxisX - upX * zaxisZ;
	xaxisZ = upX * zaxisY - upY * zaxisX;

	xAxisLength = Math.sqrt(xaxisX * xaxisX + xaxisY * xaxisY + xaxisZ * xaxisZ);

	xaxisX /= xAxisLength;
	xaxisY /= xAxisLength;
	xaxisZ /= xAxisLength;

 	//post(" " + xaxisX + " " + xaxisY + " " + xaxisZ + " \n");

	//yaxis = cross(zaxis, xaxis)

	yaxisX =  zaxisY * xaxisZ - zaxisZ * xaxisY;
	yaxisY =  zaxisZ * xaxisX - zaxisX * xaxisZ;
	yaxisZ =  zaxisX * xaxisY - zaxisY * xaxisX;

	yAxisLength = Math.sqrt(yaxisX * yaxisX + yaxisY * yaxisY + yaxisZ * yaxisZ);

	yaxisX /= yAxisLength;
	yaxisY /= yAxisLength;
	yaxisZ /= yAxisLength;

	//var dotX = xaxisX * eyeX + xaxisY * eyeY + xaxisZ * eyeZ;
	//var dotY = yaxisX * eyeX + yaxisY * eyeY + yaxisZ * eyeZ;
	//var dotZ = zaxisX * eyeX + zaxisY * eyeY + zaxisZ * eyeZ;

	/*
 	xaxis.x           yaxis.x           zaxis.x          0
 	xaxis.y           yaxis.y           zaxis.y          0
 	xaxis.z           yaxis.z           zaxis.z          0
	-dot(xaxis, eye)  -dot(yaxis, eye)  -dot(zaxis, eye)  l
	*/

	mat[0] = xaxisX;
	mat[1] = xaxisY;
	mat[2] = xaxisZ;
	mat[3] = 0;
	mat[4] = yaxisX;
	mat[5] = yaxisY;
	mat[6] = yaxisZ;
	mat[7] = 0;
	mat[8] = zaxisX;
	mat[9] = zaxisY;
	mat[10] = zaxisZ;
	mat[11] = 0;
	//mat[12] = -dotX;
	//mat[13] = -dotY;
	//mat[14] = -dotZ;
	mat[12] = cameraPos[0];
	mat[13] = cameraPos[1];
	mat[14] = cameraPos[2];
	mat[15] = 1;

    outlet(0, "transform", mat);

    // **************************************
    //
    //         CALCULATE FRUSTUM
    //
    // **************************************


    // frustum scale factor
    frustumScale = (zAxisLength + near_orig) / zAxisLength;

    // vector that looks from the camera position to the cutaway position
    frustumLookAt = vectors.sub(cameraPos, cutawayPos);

    // length of frustum look at vector
    frustumLookAtLength = vectors.length(frustumLookAt);

    // normalize the frustum look at
    frustumLookAt = vectors.scaleVecScal(frustumLookAt, 1.0 / frustumLookAtLength);

    // Angle between frustum lookat vector and cutaway normal
    frustum2NormalDot = vectors.dot(frustumLookAt, cutawayNormal);
    // correction if the cutaway vector looks the other way
    frustum2NormalDot = frustum2NormalDot * sign(frustum2NormalDot);

    frustumLookAtTangens = Math.tan(Math.acos(frustum2NormalDot));

	if(adapt_state == 1 && adapt_reference > 0){
		fovAngle = fovAngle_orig / (frustumLookAtLength / adapt_reference);
	} else {
		fovAngle = fovAngle_orig;
	}

    fovAngleTangens = Math.tan(fovAngle / 360. * 3.14159);

    near = zAxisLength;

	top = (frustumLookAtTangens * near + fovAngleTangens * frustumLookAtLength) * frustumScale;
	bottom = (frustumLookAtTangens * near - fovAngleTangens * frustumLookAtLength) * frustumScale;

	right = (ratio_orig * fovAngleTangens * frustumLookAtLength) * frustumScale;
	left = right * -1.;

    near = near * frustumScale;
    near = (near > 0)? near: 0.1;
    far = near + far_orig;

	// output(){
	outlet(1, "frustum", left, right, bottom, top, near, far);

	hasChanged = false;
}

function sign(x){
    if( +x === x ) { // check if a number was given
        return (x === 0) ? x : (x > 0) ? 1 : -1;
    }
    return NaN;
}

// this message can be ignored
function name(){
}
