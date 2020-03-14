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

/*
Rotates a Vector with a Quaternion
returns a vector
*/
exports.rotateVecQuat =  function(theVectorA, theQuaternion){
	exports.vec = new Array(3);
	var quatVec = new Array(theQuaternion[0], theQuaternion[1], theQuaternion[2]);
	var quatScalar = theQuaternion[3];

	exports.vec = exports.add(exports.add(exports.scaleVecScal(quatVec, 2.0 * exports.dot(quatVec, theVectorA)),
					exports.scaleVecScal(theVectorA, quatScalar * quatScalar - exports.dot(quatVec, quatVec))),
					exports.scaleVecScal(exports.cross(quatVec, theVectorA), 2.0 * quatScalar));
//	post("revec = " + 2.0 * quatScalar * cross(quatVec, theVectorA) + "\n");
	return exports.vec;
}

/*
Cross Product of two vectors
returns a vector
*/
exports.cross = function(theVectorA, theVectorB) {
	exports.vec = new Array(3);
	exports.vec[0] = theVectorA[1] * theVectorB[2] - theVectorA[2] * theVectorB[1];
	exports.vec[1] = theVectorA[2] * theVectorB[0] - theVectorA[0] * theVectorB[2];
	exports.vec[2] = theVectorA[0] * theVectorB[1] - theVectorA[1] * theVectorB[0];
	return exports.vec;
}

/*
Dot Product of two vectors
returns a scalar
*/
exports.dot = function(vec1, vec2){
	return vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];
}

/*
length of a vector
returns a scalar
*/
exports.length = function(vec1){
	return Math.sqrt(vec1[0] * vec1[0] + vec1[1] * vec1[1] + vec1[2] * vec1[2]);
}

/*
Adds two vectors
returns a vector
*/
exports.add = function(theVectorA, theVectorB){
	exports.vec = new Array(3);
	exports.vec[0] = theVectorA[0] + theVectorB[0];
	exports.vec[1] = theVectorA[1] + theVectorB[1];
	exports.vec[2] = theVectorA[2] + theVectorB[2];
	return exports.vec;
}

/*
subtracts two vectors
returns a vector
*/
exports.sub = function(theVectorA, theVectorB){
	exports.vec = new Array(3);
	exports.vec[0] = theVectorA[0] - theVectorB[0];
	exports.vec[1] = theVectorA[1] - theVectorB[1];
	exports.vec[2] = theVectorA[2] - theVectorB[2];
	return exports.vec;
}

/*
Scales a vector with a scalar
returns a vector
*/
exports.scaleVecScal = function(theVectorA, scalar){
	exports.vec = new Array(3);
	exports.vec[0] = theVectorA[0] * scalar;
	exports.vec[1] = theVectorA[1] * scalar;
	exports.vec[2] = theVectorA[2] * scalar;
	return exports.vec;
}

/*
Multiplies (transforms) a vector with a 4x4 Matrix
returns a vector
*/
exports.multiplyVec3ByMat4 = function(theVector, theMatrix){
	var d = 1.0 / (theVector[0] * theMatrix[3] + theVector[1] * theMatrix[7] + theVector[2] * theMatrix[11] + theMatrix[15]);
	exports.vec = new Array(3);
	exports.vec[0] = (theVector[0] * theMatrix[0]  +
				theVector[1] * theMatrix[4]  +
				theVector[2] * theMatrix[8]  +  theMatrix[12]) * d;

	exports.vec[1] = (theVector[0] * theMatrix[1]  +
				theVector[1] * theMatrix[5]  +
				theVector[2] * theMatrix[9]  +  theMatrix[13]) * d;

	exports.vec[2] = (theVector[0] * theMatrix[2]   +
				theVector[1] * theMatrix[6]   +
				theVector[2] * theMatrix[10]  + theMatrix[14]) * d;

	return exports.vec;
}
