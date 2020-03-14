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
outlets = 3;
setinletassist(0,"data");
setoutletassist(0,"transform");
setoutletassist(1,"adapt (distance)");
setoutletassist(2,"refresh");

var mat = new Array(16);

var eyeX = 0.;
var eyeY = 0.;
var eyeZ = 1.;

var atX = 0.;
var atY = 0.;
var atZ = 0.;

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

var direction = 0;
var hasChanged = 0;

function bang(){
	if(hasChanged == 1)
		calc();
	hasChanged = 0;
}

function eye(){
	var args = arrayfromargs(arguments);
	eyeX = args[0];
	eyeY = args[1];
	eyeZ = args[2];
	hasChanged = 1;
}

function at(){
	var args = arrayfromargs(arguments);
	atX = args[0];
	atY = args[1];
	atZ = args[2];
	hasChanged = 1;
}

function up(sel){
	if(sel == 0){
		upX = 1.;
		upY = 0.;
		upZ = 0.;
	}else if(sel == 1){
		upX = 0.;
		upY = 1.;
		upZ = 0.;
	} else if(sel == 2){
		upX = 0.;
		upY = 0.;
		upZ = 1.;
	}else if(sel == 3){
		upX = -1.;
		upY = 0.;
		upZ = 0.;
	}else if(sel == 4){
		upX = 0.;
		upY = -1.;
		upZ = 0.;
	} else if(sel == 5){
		upX = 0.;
		upY = 0.;
		upZ = -1.;
	}
	hasChanged = 1;
	outlet(2, "bang");
}

function dir(val){
	direction = val;
	hasChanged = 1;
	outlet(2, "bang");
}

function calc(){
	// zaxis = normal(At - Eye);

	if(direction == 0){
		zaxisX = atX - eyeX;
		zaxisY = atY - eyeY;
		zaxisZ = atZ - eyeZ;
	}else if(direction == 1){
		zaxisX = eyeX - atX;
		zaxisY = eyeY - atY;
		zaxisZ = eyeZ - atZ;
	}

	zAxisLength = Math.sqrt(zaxisX * zaxisX + zaxisY * zaxisY + zaxisZ * zaxisZ);

	outlet(1, "adapt", zAxisLength);

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
	mat[12] = eyeX;
	mat[13] = eyeY;
	mat[14] = eyeZ;
	mat[15] = 1;

	outlet(0, "transform", mat);
}
