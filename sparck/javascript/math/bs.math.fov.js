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

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 2;
setinletassist(0,"fov");
setoutletassist(0,"matrix");
setoutletassist(1,"frustum");

var near = 0.1;
var far = 10.;
var right = .05;
var left = -.05;
var top = .05;
var bottom = -.05;

var mat = new Array(16);

var projection_mode = 0;

function camtype(_mode){
	projection_mode = _mode;
	outputProjectionMatrix(left, right, bottom, top, near, far);
}

function bang(){
	outlet(1, "frustum", left, right, bottom, top, near, far);
	outputProjectionMatrix(left, right, bottom, top, near, far);
}

function frustum(_left, _right, _bottom, _top, _near, _far){
	near = _near;
	far = _far;
	right = _right;
	left = _left;
	top = _top;
	bottom = _bottom;

	outlet(1, "frustum", left, right, bottom, top, near, far);
	outputProjectionMatrix(left, right, bottom, top, near, far);
}

function fov(){
	var args = arrayfromargs(arguments);
	var ratio = args[0];
	var angle = args[1];

	near = (args[2] > 0)?args[2]: 0.1;
	far = args[3];

	var a = Math.tan(angle / 360. * 3.14159) * near;

	right = ratio * a;
	left = right * -1.;
	top = a;
	bottom = a * -1.;

	outlet(1, "frustum", left, right, bottom, top, near, far);

	outputProjectionMatrix(left, right, bottom, top, near, far);
}

function outputProjectionMatrix(left, right, bottom, top, near, far){
	if(projection_mode == 0)
		projectionMatrix(left, right, bottom, top, near, far);
	else if (projection_mode == 1)
		orthographicMatrix(left, right, bottom, top, near, far);
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

	outlet(0, mat);
}

function orthographicMatrix(left, right, bottom, top, near, far){
	mat[0] = 2 / (right - left);
	mat[1] = 0;
	mat[2] = 0;
	mat[3] = 0;

	mat[4] = 0;
	mat[5] = 2 / (top - bottom);
	mat[6] = 0;
	mat[7] = 0;

	mat[8] = 0;
	mat[9] = 0;
	mat[10] = -2 / (far -near);
	mat[11] = 0;

	mat[12] = - (right + left) / (right - left);
	mat[13] = - (top + bottom) / (top - bottom);
	mat[14] = - (far + near) / (far - near);
	mat[15] = 1;

	outlet(0, mat);
}
