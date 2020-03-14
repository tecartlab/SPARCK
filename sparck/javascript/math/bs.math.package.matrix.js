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

exports.multiplyMat4BYMat4 = function(matrixA, matrixB){
	var matrixAB = new Array(16);
	matrixAB[0] = matrixA[0] * matrixB[0] + matrixA[1] * matrixB[4] + matrixA[2] * matrixB[8] + matrixA[3] * matrixB[12];
	matrixAB[1] = matrixA[0] * matrixB[1] + matrixA[1] * matrixB[5] + matrixA[2] * matrixB[9] + matrixA[3] * matrixB[13];
	matrixAB[2] = matrixA[0] * matrixB[2] + matrixA[1] * matrixB[6] + matrixA[2] * matrixB[10] + matrixA[3] * matrixB[14];
	matrixAB[3] = matrixA[0] * matrixB[3] + matrixA[1] * matrixB[7] + matrixA[2] * matrixB[11] + matrixA[3] * matrixB[15];

	matrixAB[4] = matrixA[4] * matrixB[0] + matrixA[5] * matrixB[4] + matrixA[6] * matrixB[8] + matrixA[7] * matrixB[12];
	matrixAB[5] = matrixA[4] * matrixB[1] + matrixA[5] * matrixB[5] + matrixA[6] * matrixB[9] + matrixA[7] * matrixB[13];
	matrixAB[6] = matrixA[4] * matrixB[2] + matrixA[5] * matrixB[6] + matrixA[6] * matrixB[10] + matrixA[7] * matrixB[14];
	matrixAB[7] = matrixA[4] * matrixB[3] + matrixA[5] * matrixB[7] + matrixA[6] * matrixB[11] + matrixA[7] * matrixB[15];

	matrixAB[8] = matrixA[8] * matrixB[0] + matrixA[9] * matrixB[4] + matrixA[10] * matrixB[8] + matrixA[11] * matrixB[12];
	matrixAB[9] = matrixA[8] * matrixB[1] + matrixA[9] * matrixB[5] + matrixA[10] * matrixB[9] + matrixA[11] * matrixB[13];
	matrixAB[10] = matrixA[8] * matrixB[2] + matrixA[9] * matrixB[6] + matrixA[10] * matrixB[10] + matrixA[11] * matrixB[14];
	matrixAB[11] = matrixA[8] * matrixB[3] + matrixA[9] * matrixB[7] + matrixA[10] * matrixB[11] + matrixA[11] * matrixB[15];

	matrixAB[12] = matrixA[12] * matrixB[0] + matrixA[13] * matrixB[4] + matrixA[14] * matrixB[8] + matrixA[15] * matrixB[12];
	matrixAB[13] = matrixA[12] * matrixB[1] + matrixA[13] * matrixB[5] + matrixA[14] * matrixB[9] + matrixA[15] * matrixB[13];
	matrixAB[14] = matrixA[12] * matrixB[2] + matrixA[13] * matrixB[6] + matrixA[14] * matrixB[10] + matrixA[15] * matrixB[14];
	matrixAB[15] = matrixA[12] * matrixB[3] + matrixA[13] * matrixB[7] + matrixA[14] * matrixB[11] + matrixA[15] * matrixB[15];
	return matrixAB;
}

/*
Rotates a 4x4 Matrix from a Quaternion
returns a new Matrix
*/
exports.quat2Mat4 = function(quaternion){
	var xx      = quaternion[0] * quaternion[0];
    var xy      = quaternion[0] * quaternion[1];
    var xz      = quaternion[0] * quaternion[2];
    var xw      = quaternion[0] * quaternion[3];

    var yy      = quaternion[1] * quaternion[1];
    var yz      = quaternion[1] * quaternion[2];
    var yw      = quaternion[1] * quaternion[3];

    var zz      = quaternion[2] * quaternion[2];
    var zw      = quaternion[2] * quaternion[3];

	var matrix = new Array(16);

	matrix[0] = 1 - 2 * ( yy + zz );
	matrix[4] = 2 * ( xy - zw );
	matrix[8] = 2 * ( xz + yw );
	matrix[12] = 0;

	matrix[1] = 2 * ( xy + zw );
	matrix[5] = 1 - 2 * ( xx + zz );
	matrix[9] = 2 * ( yz - xw );
	matrix[13] = 0;

	matrix[2] = 2 * ( xz - yw );
	matrix[6] = 2 * ( yz + xw );
	matrix[10] = 1 - 2 * ( xx + yy );
	matrix[14] = 0;

	matrix[3] = 0;
	matrix[7] = 0;
	matrix[11] = 0;
	matrix[15] = 1;

	return matrix;
}

/*
Create a 4x4 translation Matrix from a vector
returns a new Matrix
*/
exports.translationVecToMat4 = function(theVector){
	var matrix = new Array(16);

	matrix[0] = 1;
	matrix[4] = 0;
	matrix[8] = 0;
	matrix[12] = theVector[0];

	matrix[1] = 0;
	matrix[5] = 1;
	matrix[9] = 0;
	matrix[13] = theVector[1];

	matrix[2] = 0;
	matrix[6] = 0;
	matrix[10] = 1;
	matrix[14] = theVector[2];

	matrix[3] = 0;
	matrix[7] = 0;
	matrix[11] = 0;
	matrix[15] = 1;

	return matrix;
}

/*
Create a 4x4 scale Matrix from a vector
returns a new Matrix
*/
exports.scaleVecToMat4 = function(theVector){
	var matrix = new Array(16);

	matrix[0] = theVector[0];
	matrix[4] = 0;
	matrix[8] = 0;
	matrix[12] = 0;

	matrix[1] = 0;
	matrix[5] = theVector[1];
	matrix[9] = 0;
	matrix[13] = 0;

	matrix[2] = 0;
	matrix[6] = 0;
	matrix[10] = theVector[2];
	matrix[14] = 0;

	matrix[3] = 0;
	matrix[7] = 0;
	matrix[11] = 0;
	matrix[15] = 1;

	return matrix;
}

/*
Create a 4x4 reflection Matrix from a plane definition
returns a new Matrix
*/
exports.reflectionPlaneToMat4 = function(pNormal, d){
	var matrix = new Array(16);

	matrix[0]  = 1 - 2 * pNormal[0] * pNormal[0];
	matrix[1]  =   - 2 * pNormal[0] * pNormal[1];
	matrix[2]  =   - 2 * pNormal[0] * pNormal[2];
	matrix[3]  =   0;

	matrix[4]  =   matrix[1];
	matrix[5]  = 1 - 2 * pNormal[1] * pNormal[1];
	matrix[6]  =   - 2 * pNormal[1] * pNormal[2];
	matrix[7]  =   0;

	matrix[8]  =   matrix[2];
	matrix[9]  =   matrix[6];
	matrix[10] = 1 - 2 * pNormal[2] * pNormal[2];
	matrix[11] =   0;

	matrix[12] = - 2 * pNormal[0] * d;
	matrix[13] = - 2 * pNormal[1] * d;
	matrix[14] = - 2 * pNormal[2] * d;
	matrix[15] = 1;

	return matrix;
}
