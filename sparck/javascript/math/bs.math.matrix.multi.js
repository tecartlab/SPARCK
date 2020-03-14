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

/*
	View Projection Matrix
*/

// set up inlets/outlets/assist strings
inlets = 3;
outlets = 1;
setinletassist(0,"bang");
setinletassist(1,"view matrix");
setinletassist(2,"projection matrix");
setoutletassist(0,"vp matrix = view x projection");

var mymessagename = "><";

// patcher arguments
if (jsarguments.length > 1){
    mymessagename = jsarguments[1];
}

var matrixA = 0;
var matrixB = 0;

var matrixAB = new Array();

var hasChanged = false;
var isEnabled = 0;
var isAutoUpdate = 0;

function bang(){
	if (inlet == 0 && isEnabled && hasChanged){
		update()
	}
}

function update(){
	if( matrixB != 0 && matrixA != 0){
		mutliAB();
		if(mymessagename == "><")
			outlet(0, matrixAB);
		else
			outlet(0, mymessagename, matrixAB);
	}
	hasChanged = false;
}

function enable(_enabled){
	isEnabled = _enabled;
}

function autoupdate(_enabled){
	isAutoUpdate = _enabled;
}

function anything()
{
	if (inlet==1){
		if(arguments.length < 16)
			matrixA = arrayfromargs(messagename,arguments);
		else
			matrixA = arrayfromargs(arguments);
		hasChanged = true;
	}else if (inlet==2){
		if(arguments.length < 16)
			matrixB = arrayfromargs(messagename,arguments);
		else
			matrixB = arrayfromargs(arguments);
		hasChanged = true;
	}
	if(isAutoUpdate == 1)
		update();
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
