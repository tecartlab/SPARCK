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
inlets = 2;
var IN_PARENT = 0;
var IN_LOOKAT = 1;
setinletassist(IN_PARENT,"camera transformations");
setinletassist(IN_LOOKAT,"cutaway transformations");

outlets = 5;
var OUT_TFM_C = 0;
var OUT_TFM_L = 1;
var OUT_TFM_R = 2;
var OUT_FRUSTUM = 3;
var OUT_P_MATRIX = 4;
setoutletassist(OUT_TFM_C,"transform center");
setoutletassist(OUT_TFM_L,"transform left");
setoutletassist(OUT_TFM_R,"transform right");
setoutletassist(OUT_FRUSTUM,"frustum");
setoutletassist(OUT_P_MATRIX,"p_matrix");

var vectors = require("bs.math.package.vector");
var matrices = require("bs.math.package.matrix");
var quaternions = require("bs.math.package.quat");

var undefined = "undefined";

var cameraPos = new Array(0, 0, 1);
var cameraPosLeft = new Array(0, 0, 1);
var cameraPosRight = new Array(0, 0, 1);

var cutawayPos = new Array(0, 0, 0);
var cutawayQuat = new Array(0, 0, 0, 1);
var cutawayTransform = new Array(0, 0, 0, 1, 0, 0, 0, 1 , 0, 0, 0, 1, 0, 0, 0, 1);
var cutawayInvTransform = new Array(0, 0, 0, 1, 0, 0, 0, 1 , 0, 0, 0, 1, 0, 0, 0, 1);

var cutawayNormal = new Array(0, 0, 1);
var cutawayUp = new Array(1, 0, 0);

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
var vecUp = new Array(1, 0, 0);

var upDir = 1;
var xzCorr = 1;

var ratio_orig = 1.0;
var fovAngle_orig = 50.0;
var near_orig = 0.1;
var far_orig = 10.;
var witdh_orig = 1.0;
var height_orig = 1.0;

var adapt_state = 0;
var adapt_reference = 1;

var calculationMode = 0;

var isStereo = 0;

function loadbang(){
	mirrorplane(0);
}

function bang(){
	if(hasChanged){
        if(calculationMode == 0){
            calcParallel();
        }else if(calculationMode == 1){
            calcTurn();
        }
        hasChanged = false;
    }
}

function calcmode(_mode){
    if(_mode == "parallel"){
        calculationMode = 0;
    } else if(_mode == "turn"){
        calculationMode = 1;
    }
    // update stereomode
    stereomode(isStereo);
    hasChanged = true;
}

function stereomode(_mode){
    isStereo = _mode;
    if(calculationMode == 0){
        outlet(OUT_FRUSTUM, "stereomode", _mode);
    } else if(calculationMode == 1){
        outlet(OUT_FRUSTUM, "stereomode", 0);
    }
    hasChanged = true;
}

function leftpos(){
    cameraPosLeft = arrayfromargs(arguments);
    hasChanged = true;
}

function rightpos(){
    cameraPosRight = arrayfromargs(arguments);
    hasChanged = true;
}

function worldpos(){
    if(inlet == IN_PARENT){
        cameraPos = arrayfromargs(arguments);
        hasChanged = true;
    } else if(inlet == IN_LOOKAT){
        cutawayPos = arrayfromargs(arguments);
    }
}

function worldquat(){
    if(inlet == IN_LOOKAT){
        cutawayQuat = arrayfromargs(arguments);
    }
}

function worldtransform(){
    if(inlet == IN_LOOKAT){
        cutawayTransform = arrayfromargs(arguments);
    }
}

function invtransform(){
    if(inlet == IN_LOOKAT){
        cutawayInvTransform = arrayfromargs(arguments);
        evaluateCutawayRotationals();
    }
}

function fov(_ratio, _angle, _near, _far, _width, _height){
	var args = arrayfromargs(arguments);
	ratio_orig = _ratio;
	fovAngle_orig = _angle;
	near_orig = _near;
	far_orig = (_far > _near)?_far: _near + 0.1;
    witdh_orig = _width;
    height_orig = _height;
	hasChanged = true;
}

function mirrorplane(dir, up){
	if(dir == 0){
		vecNormal = new Array(0, 0, 1);
        if(up == 0){
            vecUp = new Array(1, 0, 0);
        }else{
            vecUp = new Array(0, 1, 0);
        }
        xzCorr = 1;
	} else if(dir == 1){
		vecNormal = new Array(0, 1, 0);
        if(up == 0){
            vecUp = new Array(1, 0, 0);
        }else{
            vecUp = new Array(0, 0, 1);
        }
        xzCorr = -1;
	} else if(dir == 2){
		vecNormal = new Array(1, 0, 0);
        if(up == 0){
            vecUp = new Array(0, 1, 0);
        }else{
            vecUp = new Array(0, 0, 1);
        }
        xzCorr = 1;
	}
    upDir = (up - 0.5) * - 2.;
	evaluateCutawayRotationals()
//	hasChanged == true;
//	outlet(1, "bang");
}

function evaluateCutawayRotationals(){
	// calculate mirror plane normal and mirror-plane-d
	//		from quat-rotation and position
	cutawayNormal = vectors.rotateVecQuat(vecNormal, cutawayQuat);
	cutawayUp = vectors.rotateVecQuat(vecUp, cutawayQuat);

    var d = - vectors.dot(cutawayNormal, cutawayPos);
	// calculate reflection matrix
	cutawayPlaneMatrix = matrices.reflectionPlaneToMat4(cutawayNormal, d);
	hasChanged = true;
}

function adapt(state){
	adapt_state = state;
    hasChanged = true;
}

function adaptReference(val){
	adapt_reference = val;
    hasChanged = true;
}

function calcParallelDetail(_camPos){
        // **************************************
    //
    //         CALCULATE TRANSFORMATION
    //
    // **************************************

    // calculate object mirrored position
    cameraMirrorPos = vectors.multiplyVec3ByMat4(_camPos, cutawayPlaneMatrix);

    // calculate the closest point to the camera on the cutaway plane
    cameraLookAtPos = vectors.scaleVecScal(vectors.add(_camPos, cameraMirrorPos), 0.5);

    //calculate the camera up vector
    cameraUp = cutawayUp;

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

    zaxisX = _camPos[0] - cameraLookAtPos[0];
    zaxisY = _camPos[1] - cameraLookAtPos[1];
    zaxisZ = _camPos[2] - cameraLookAtPos[2];

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
	mat[12] = _camPos[0];
	mat[13] = _camPos[1];
	mat[14] = _camPos[2];
	mat[15] = 1;

    // **************************************
    //
    //         CALCULATE FRUSTUM
    //
    // **************************************

    // calculate the camera look at point into the local lookat (cutaway) plane
    var cutawayLocalLookAtPos = vectors.multiplyVec3ByMat4(cameraLookAtPos, cutawayInvTransform);
    var topBottom, leftRight;

    if(vecUp[0] != 0){
        topBottom = cutawayLocalLookAtPos[0];
        leftRight = cutawayLocalLookAtPos[1] + cutawayLocalLookAtPos[2];
    } else if(vecUp[1] != 0){
        topBottom = cutawayLocalLookAtPos[1];
        leftRight = cutawayLocalLookAtPos[0] + cutawayLocalLookAtPos[2];
    }else if(vecUp[2] != 0){
        topBottom = cutawayLocalLookAtPos[2];
        leftRight = cutawayLocalLookAtPos[0] + cutawayLocalLookAtPos[1];
    }

    top = - topBottom + height_orig / 2.0;
	bottom = - topBottom - height_orig / 2.0;

    // these different factor math is black magic and created by try and error...
    var cam2cut = vectors.sub(_camPos, cutawayPos);
    var dir = vectors.dot(cutawayNormal, cam2cut);
    var upFactor = (dir > 0)? 1:(dir < 0)?-1:0;

    right = xzCorr * upDir * upFactor * leftRight + witdh_orig / 2.;
	left = xzCorr * upDir * upFactor * leftRight - witdh_orig / 2.;

    var planeDistance = zAxisLength;

    //near correction
	if(adapt_state == 1){
        near = planeDistance * adapt_reference;
        far = near + far_orig;
        top = top / planeDistance * near;
        bottom = bottom / planeDistance * near;
        right = right / planeDistance * near;
        left = left / planeDistance * near;
 	} else {
        top = top / planeDistance * near_orig;
        bottom = bottom / planeDistance * near_orig;
        right = right / planeDistance * near_orig;
        left = left / planeDistance * near_orig;
        near = near_orig;
        far = far_orig;
	}
}

function calcParallel(){

    if(isStereo == 0){
        calcParallelDetail(cameraPos);
        outlet(OUT_TFM_C, "transform", mat);
        outlet(OUT_TFM_L, "transform", mat);
        outlet(OUT_TFM_R, "transform", mat);

        outlet(OUT_FRUSTUM, "frustum", left, right, bottom, top, near, far);
        outlet(OUT_FRUSTUM, "frustum_left", left, right, bottom, top, near, far);
        outlet(OUT_FRUSTUM, "frustum_right", left, right, bottom, top, near, far);

        projectionMatrix(left, right, bottom, top, near, far);

        outlet(OUT_P_MATRIX, "p_matrix", mat);
        outlet(OUT_P_MATRIX, "p_matrix_left", mat);
        outlet(OUT_P_MATRIX, "p_matrix_right", mat);
    } else {
        calcParallelDetail(cameraPos);
        outlet(OUT_TFM_C, "transform", mat);
        outlet(OUT_FRUSTUM, "frustum", left, right, bottom, top, near, far);
        projectionMatrix(left, right, bottom, top, near, far);
        outlet(OUT_P_MATRIX, "p_matrix", mat);

        calcParallelDetail(cameraPosLeft);
        outlet(OUT_TFM_L, "transform", mat);
        outlet(OUT_FRUSTUM, "frustum_left", left, right, bottom, top, near, far);
        projectionMatrix(left, right, bottom, top, near, far);
        outlet(OUT_P_MATRIX, "p_matrix_left", mat);

        calcParallelDetail(cameraPosRight);
        outlet(OUT_TFM_R, "transform", mat);
        outlet(OUT_FRUSTUM, "frustum_right", left, right, bottom, top, near, far);
        projectionMatrix(left, right, bottom, top, near, far);
        outlet(OUT_P_MATRIX, "p_matrix_right", mat);
    }
}

function calcTurn(){

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

    outlet(OUT_TFM_C, "transform", mat);
    outlet(OUT_TFM_L, "transform", mat);
    outlet(OUT_TFM_R, "transform", mat);

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
	outlet(OUT_FRUSTUM, "frustum", left, right, bottom, top, near, far);
	outlet(OUT_FRUSTUM, "frustum_left", left, right, bottom, top, near, far);
	outlet(OUT_FRUSTUM, "frustum_right", left, right, bottom, top, near, far);

    var p_matrix = projectionMatrix(left, right, bottom, top, near, far);

	outlet(OUT_P_MATRIX, "p_matrix", p_matrix);
	outlet(OUT_P_MATRIX, "p_matrix_left", p_matrix);
	outlet(OUT_P_MATRIX, "p_matrix_right", p_matrix);

}

function projectionMatrix(left, right, bottom, top, near, far){
	mat[0] = 2 * near / (right - left);
	mat[1] = 0;
	mat[2] = 0;
	mat[3] = 0;
	mat[4] = 0;
	mat[5] = 2 * near / (top - bottom);
	mat[6] = 0;
	mat[7] = 0;
	mat[8] = (right + left) / (right - left);
	mat[9] = (top + bottom) / (top - bottom);
	mat[10] = (near + far) / (near - far);
	mat[11] = -1;
	mat[12] = 0;
	mat[13] = 0;
	mat[14] = 2 * far / (near - far);
//	mat[14] = 2 * (near * far) / (near - far);
	mat[15] = 0;

    return mat;
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
