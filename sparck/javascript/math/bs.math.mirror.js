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
inlets = 1;
outlets = 2;
setinletassist(0,"data");
setoutletassist(0,"transform");
setoutletassist(1,"refresh");

var vectors = require("bs.math.package.vector");
var matrices = require("bs.math.package.matrix");
var quaternions = require("bs.math.package.quat");

var undefined = "undefined";

var objectPos = undefined;
var objectQuat = undefined;

var mirrorPos = undefined;
var mirrorQuat = undefined;

var pNormal = undefined;

var matReflection = undefined;
var matRotationReflection = undefined;
var matRotation = undefined;
var matObjectMirror = undefined;

// create y-z plane normal
var	flipNormal = vectors.rotateVecQuat(new Array(1, 0, 0), new Array(0, 0, 0, 1));
// create y-z plane reflection matrix
var	matFlip = matrices.reflectionPlaneToMat4(flipNormal, 0);

var mirrorMode = 0;
var hasChanged = false;

var vecNormal = new Array(0, 0, 1);

function loadbang(){
	mirrorplane(0);
}

function bang(){
	if(hasChanged == true &&
		mirrorQuat != undefined &&
		objectQuat != undefined){
		calc();
	}
}

function objpos(){
	objectPos = arrayfromargs(arguments);
}

function objquat(){
	objectQuat = arrayfromargs(arguments);;
	matRotation = matrices.quat2Mat4(objectQuat);
	hasChanged = true;
}

function worldpos(){
	mirrorPos = arrayfromargs(arguments);;
}

function worldquat(){
	mirrorQuat = arrayfromargs(arguments);
	evaluateWorldRotationals();
}

function evaluateWorldRotationals(){
	// calculate mirror plane normal and mirror-plane-d
	//		from quat-rotation and position
	pNormal = vectors.rotateVecQuat(vecNormal, mirrorQuat);
	var d = - vectors.dot(pNormal, mirrorPos);
	// calculate reflection matrix
	matReflection = matrices.reflectionPlaneToMat4(pNormal, d);
	// calculate rotation-reflection matrix (reflection matrix through world origin)
	matRotationReflection = matrices.reflectionPlaneToMat4(pNormal, 0);
	hasChanged = true;
}

function mode(mirrormode){
	mirrorMode = mirrormode;
	hasChanged == true;
//	outlet(1, "bang");
}

function mirrorplane(dir){
	if(dir == 0){
		vecNormal = new Array(0, 0, 1);
	} else if(dir == 1){
		vecNormal = new Array(0, 1, 0);
	} else if(dir == 2){
		vecNormal = new Array(1, 0, 0);
	}
	evaluateWorldRotationals()
//	hasChanged == true;
//	outlet(1, "bang");
}

function calc(){
	if(mirrorMode == 0){ // translation and rotation
		// calculate object mirrored position
    	var objectMirrorPos = vectors.multiplyVec3ByMat4(objectPos, matReflection);
    	// create translation matrix from object mirrored position
		matObjectMirror = matrices.translationVecToMat4(objectMirrorPos);

		// create rotation matrix from object rotation and rotation-reflection
		var mat = matrices.multiplyMat4BYMat4(matRotation, matRotationReflection);
		// flip all through a y-z plane reflection matrix
		var mat1 = matrices.multiplyMat4BYMat4(matFlip, mat);
		// apply mirrored object translation matrix
    	var mat2 = matrices.multiplyMat4BYMat4(mat1, matObjectMirror);

		outlet(0, "transform", mat2);
	} else if(mirrorMode == 1){ // translation only
		// calculate object mirrored position
    	var objectMirrorPos = vectors.multiplyVec3ByMat4(objectPos, matReflection);
    	// create translation matrix from object mirrored position
		matObjectMirror = matrices.translationVecToMat4(objectMirrorPos);

		// create transformation matrix from object rotation and mirrored object translation
		var mat = matrices.multiplyMat4BYMat4(matRotation, matObjectMirror);

		outlet(0, "transform", mat);
	} else if(mirrorMode == 2){ // rotation only
    	// create translation matrix from object position
		matObject = matrices.translationVecToMat4(objectPos);

		// create rotation matrix from object rotation and rotation-reflection
		var mat = matrices.multiplyMat4BYMat4(matRotation, matRotationReflection);
		// flip all through a y-z plane reflection matrix
		var mat1 = matrices.multiplyMat4BYMat4(matFlip, mat);
		// apply mirrored object translation matrix
    	var mat2 = matrices.multiplyMat4BYMat4(mat1, matObject);

		outlet(0, "transform", mat2);
	}

	hasChanged = false;

//	outlet(1, "reset");
// MIRROR PLANE DEBUG
//	outlet(1, "linesegment", 0, 0, 0, 0, 0, 1);
//	outlet(1, "moveto", 0, 0, -d);
//	outlet(1, "plane", 1);
//	outlet(1, "quat", mirrorQuat);
//	outlet(1, "position", mirrorPos);

//MIRROR OBJ DEBUG
//	outlet(1, "moveto", mirrorObj);
//	outlet(1, "glcolor", 1., 0, 0, 1.);
//	outlet(1, "sphere", 0.03);

// MIRROR PLANE DEBUG
//	outlet(2, "reset");
//	outlet(2, "moveto", 0, 0, -2*d);
//	outlet(2, "plane", 1);
//	outlet(2, "quat", mirrorQuat);
//	outlet(2, "position", mirrorPos);

}

// this message can be ignored
function name(){
}
