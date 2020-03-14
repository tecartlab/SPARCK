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
	Rotates a matrix around a pivot point
*/

// set up inlets/outlets/assist strings
inlets = 4;
outlets = 1;
setinletassist(0,"execute");
setinletassist(1,"matrixin");
setinletassist(2,"matrixrot");
setinletassist(3,"pivotpoint");
setoutletassist(0,"new matrix = (matrixin - pivotpont) x matrix2rot + pivotpoint");

var mymessagename = null;

// patcher arguments
if (jsarguments.length > 1){
    mymessagename = jsarguments[1];
}

var matrix_in = null;
var matrix_rot = null;
var pivot_point = null;

var matrixAB = new Array();

var hasChanged = 0;

function bang(){
	if (hasChanged == 1 && matrix_in != null && matrix_rot != null && pivot_point != null){
		var matrix_out = translate(matrix_in, invert(pivot_point));
		matrix_out = mutliAB(matrix_out, matrix_rot);
		matrix_out = translate(matrix_out, pivot_point);
		if(mymessagename != null)
			outlet(0, mymessagename, matrix_out);
		else
			outlet(0, matrix_out);
	}
	hasChanged = 0;
}

function anything()
{
	if (inlet==1){
		if(arguments.length < 16)
			matrix_in = arrayfromargs(messagename,arguments);
		else
			matrix_in = arrayfromargs(arguments);
	}else if (inlet==2){
		if(arguments.length < 16)
			matrix_rot = arrayfromargs(messagename,arguments);
		else
			matrix_rot = arrayfromargs(arguments);
	}else if (inlet==3){
		if(arguments.length < 3)
			pivot_point = arrayfromargs(messagename,arguments);
		else
			pivot_point = arrayfromargs(arguments);
	}
	hasChanged = 1;
}

function invert(vector){
	var vectorA = new Array(3);
	vectorA[0] = - vector[0];
	vectorA[1] = - vector[1];
	vectorA[2] = - vector[2];
	return vectorA;
}


function translate(matrix, vector){
	var matrixA = matrix.slice();
	matrixA[12] += vector[0];
	matrixA[13] += vector[1];
	matrixA[14] += vector[2];
	return matrixA;
}

function mutliAB(matrixA, matrixB){
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
