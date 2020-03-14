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
setinletassist(0,"fov");
setoutletassist(0,"frustum");
setoutletassist(1,"p_matrix");

var ratio_orig = 1.0;
var angle_orig = 50.0;
var near_orig = 0.1;
var far_orig = 10.;

var ratio;
var angle;
var near;
var far;

var right_orig = .05;
var left_orig = -.05;
var top_orig = .05;
var bottom_orig = -.05;

var right = .05;
var left = -.05;
var top = .05;
var bottom = -.05;

var opticsmode = 0;// (0 = fov, 1 = frustum);

var mat = new Array(16);

var ortho_mode = 0;
var adapt_state = 0;
var adapt_reference = 0;
var adapt_distance = 0;

function mode(_mode){
	opticsmode = _mode;
    ortho_mode = (_mode < 2)?0:1;

    update();
}

function adapt(state){
	adapt_state = state;
	update();
}

function adaptReference(val){
	adapt_reference = val;
	update();
}

function adaptDistance(val){
	adapt_distance = val;
	if(adapt_state == 1)
		update();
}

function frustum(_left, _right, _bottom, _top, _near, _far){
	near_orig = _near;
	far_orig = _far;
	right_orig = _right;
	left_orig = _left;
	top_orig = _top;
	bottom_orig = _bottom;

	update();
}

function fov(){
	var args = arrayfromargs(arguments);
	ratio_orig = args[0];
	angle_orig = args[1];
	near_orig = (args[2] > 0)?args[2]: 0.1;
	far_orig = args[3];

	update();
}

function update(){
	// calcFov(){
	if(opticsmode == 0){ // fov perspective
		ratio = ratio_orig;
		if(adapt_state == 1 && adapt_reference > 0 && adapt_distance > 0){
			angle = angle_orig / (adapt_distance / adapt_reference);
			angle = (angle < 150)? angle: 150;
			far = far_orig - adapt_reference + adapt_distance;
			near = near_orig - adapt_reference + adapt_distance;
			near = (near > 0)?near: 0.01;
		} else {
			angle = angle_orig;
			near = near_orig;
			far = far_orig;
		}

		// calcFrustum(){
		var a = Math.tan(angle / 360. * 3.14159) * near;

		right = ratio * a;
		left = right * -1.;
		top = a;
		bottom = a * -1.;
	} if(opticsmode == 2){  // fov orthogonal
		ratio = ratio_orig;
		if(adapt_state == 1 && adapt_reference > 0 && adapt_distance > 0){
			far = far_orig - adapt_reference + adapt_distance;
			near = near_orig - adapt_reference + adapt_distance;
			near = (near > 0)?near: 0.01;
		} else {
			angle = angle_orig;
			near = near_orig;
			far = far_orig;
		}

		// calcFrustum(){
		var a = Math.tan(angle / 720. * 3.14159);

		right = ratio * a;
		left = right * -1.;
		top = a;
		bottom = a * -1.;
	} else if(opticsmode == 1 || opticsmode == 3){ //frustum perspective/othogonal
		right 	= right_orig;
		left 	= left_orig;
		top 	= top_orig;
		bottom 	= bottom_orig;
		near 	= near_orig;
		far 	= far_orig;
	}

	// output(){
	outlet(0, "frustum", left, right, bottom, top, near, far);
	outlet(0, "frustum_left", left, right, bottom, top, near, far);
	outlet(0, "frustum_right", left, right, bottom, top, near, far);

	updateProjectionMatrix(left, right, bottom, top, near, far);

	// outlet(0, "frustum_right", left, right, bottom, top, near, far);
	outlet(1, "p_matrix", mat);
	outlet(1, "p_matrix_left", mat);
	outlet(1, "p_matrix_right", mat);
}

function updateProjectionMatrix(left, right, bottom, top, near, far){
	if(ortho_mode == 0)
		projectionMatrix(left, right, bottom, top, near, far);
	else if (ortho_mode == 1)
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
}
