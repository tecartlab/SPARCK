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
	Model View Projection Matrix
	
*/

// set up inlets/outlets/assist strings
inlets = 4;
outlets = 1;
setinletassist(0,"bang");
setinletassist(1,"model matrix");
setinletassist(2,"view matrix");
setinletassist(3,"projection matrix");
setoutletassist(0,"mvp matrix = model x view x projection");

var mymessagename = "mvp_matrix";

// patcher arguments
if (jsarguments.length > 1){
    mymessagename = jsarguments[1];
}

var matrixA = 0;
var matrixB = 0;
var matrixC = 0;

var matrixAB = new Array();
var matrixABC = new Array();

var hasChanged = false;
var isEnabled = 0;

function bang(){
	if (inlet == 0 && isEnabled && hasChanged && matrixB != 0 && matrixA != 0 && matrixC != 0){
		mutliAB();
		mutliABC();
		outlet(0, mymessagename, matrixABC);
		hasChanged = false;
	}
}

function enable(_enabled){
	isEnabled = _enabled;
}

function list()
{
	if (inlet==1){
		matrixA = arrayfromargs(messagename,arguments);
		hasChanged = true;
	}else if (inlet==2){
		matrixB = arrayfromargs(messagename,arguments);
		hasChanged = true;
	}else if (inlet==3){
		matrixC = arrayfromargs(messagename,arguments);
		hasChanged = true;
	}
}

function mutliABC(){
	matrixABC[0] = matrixAB[0] * matrixC[0] + matrixAB[1] * matrixC[4] + matrixAB[2] * matrixC[8] + matrixAB[3] * matrixC[12];
	matrixABC[1] = matrixAB[0] * matrixC[1] + matrixAB[1] * matrixC[5] + matrixAB[2] * matrixC[9] + matrixAB[3] * matrixC[13];
	matrixABC[2] = matrixAB[0] * matrixC[2] + matrixAB[1] * matrixC[6] + matrixAB[2] * matrixC[10] + matrixAB[3] * matrixC[14];
	matrixABC[3] = matrixAB[0] * matrixC[3] + matrixAB[1] * matrixC[7] + matrixAB[2] * matrixC[11] + matrixAB[3] * matrixC[15];

	matrixABC[4] = matrixAB[4] * matrixC[0] + matrixAB[5] * matrixC[4] + matrixAB[6] * matrixC[8] + matrixAB[7] * matrixC[12];
	matrixABC[5] = matrixAB[4] * matrixC[1] + matrixAB[5] * matrixC[5] + matrixAB[6] * matrixC[9] + matrixAB[7] * matrixC[13];
	matrixABC[6] = matrixAB[4] * matrixC[2] + matrixAB[5] * matrixC[6] + matrixAB[6] * matrixC[10] + matrixAB[7] * matrixC[14];
	matrixABC[7] = matrixAB[4] * matrixC[3] + matrixAB[5] * matrixC[7] + matrixAB[6] * matrixC[11] + matrixAB[7] * matrixC[15];

	matrixABC[8] = matrixAB[8] * matrixC[0] + matrixAB[9] * matrixC[4] + matrixAB[10] * matrixC[8] + matrixAB[11] * matrixC[12];
	matrixABC[9] = matrixAB[8] * matrixC[1] + matrixAB[9] * matrixC[5] + matrixAB[10] * matrixC[9] + matrixAB[11] * matrixC[13];
	matrixABC[10] = matrixAB[8] * matrixC[2] + matrixAB[9] * matrixC[6] + matrixAB[10] * matrixC[10] + matrixAB[11] * matrixC[14];
	matrixABC[11] = matrixAB[8] * matrixC[3] + matrixAB[9] * matrixC[7] + matrixAB[10] * matrixC[11] + matrixAB[11] * matrixC[15];

	matrixABC[12] = matrixAB[12] * matrixC[0] + matrixAB[13] * matrixC[4] + matrixAB[14] * matrixC[8] + matrixAB[15] * matrixC[12];
	matrixABC[13] = matrixAB[12] * matrixC[1] + matrixAB[13] * matrixC[5] + matrixAB[14] * matrixC[9] + matrixAB[15] * matrixC[13];
	matrixABC[14] = matrixAB[12] * matrixC[2] + matrixAB[13] * matrixC[6] + matrixAB[14] * matrixC[10] + matrixAB[15] * matrixC[14];
	matrixABC[15] = matrixAB[12] * matrixC[3] + matrixAB[13] * matrixC[7] + matrixAB[14] * matrixC[11] + matrixAB[15] * matrixC[15];
}

function mutliAB(){
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
}
