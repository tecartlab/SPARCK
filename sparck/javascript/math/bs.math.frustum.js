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

var optics_mode = 0;// (0 = fov, 1 = frustum);
var ortho_mode = 0;

var mat = new Array(16);

var adapt_state = 0;
var adapt_reference = 0;
var adapt_dist = 0;

var upd_tsk = new Task(update, this);

function refresh(){
    upd_tsk.schedule(10);
}

function notifydeleted(){
    upd_tsk.freepeer();
}

function optics(_mode){
	switch (_mode){
		case 'fov_perspective':
			optics_mode = 0;
			ortho_mode = 0;
			break;
 		case 'fov_orthografic':
			optics_mode = 1;
			ortho_mode = 1;
			break;
 		case 'frustum_perspective':
			optics_mode = 2;
			ortho_mode = 0;
			break;
 		case 'frustum_orthografic':
			optics_mode = 3;
			ortho_mode = 1;
			break;
	}
	
    update();
}

function adapt(state){
	adapt_state = state;
	refresh();
}

function adapt_factor(val){
	adapt_reference = val;
	refresh();
}

function adapt_distance(val){
	adapt_dist = val;
	if(adapt_state == 1)
		update();
}

function frust_left(_left){
	left_orig = _left;
    refresh();
}

function frust_right(_right){
	right_orig = _right;
    refresh();
}

function frust_top(_top){
	top_orig = _top;
    refresh();
}

function frust_bottom(_bottom){
	bottom_orig = _bottom;
    refresh();
}

function cull_near(_near){
	near_orig = _near;
    refresh();
}

function cull_far(_far){
	far_orig = _far;
    refresh();
}

function fov_ratio(_ratio){
    ratio_orig = _ratio;
    refresh();
}

function fov_angle(_angle){
    angle_orig = _angle;
    refresh();
}

function frustum(_left, _right, _bottom, _top, _near, _far){
	near_orig = _near;
	far_orig = _far;
	right_orig = _right;
	left_orig = _left;
	top_orig = _top;
	bottom_orig = _bottom;
    refresh();
}

function fov(){
	var args = arrayfromargs(arguments);
	ratio_orig = args[0];
	angle_orig = args[1];
	near_orig = (args[2] > 0)?args[2]: 0.1;
	far_orig = args[3];
    refresh();
}

function update(){
	// calcFov(){
	//post("update " + optics_mode + " \n");
	if(optics_mode == 0){ // fov perspective
		ratio = ratio_orig;
		if(adapt_state == 1 && adapt_reference > 0 && adapt_dist > 0){
			angle = angle_orig / (adapt_dist / adapt_reference);
			angle = (angle < 150)? angle: 150;
			far = far_orig - adapt_reference + adapt_dist;
			near = near_orig - adapt_reference + adapt_dist;
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
	} if(optics_mode == 1){  // fov orthogonal
		ratio = ratio_orig;
		if(adapt_state == 1 && adapt_reference > 0 && adapt_dist > 0){
			far = far_orig - adapt_reference + adapt_dist;
			near = near_orig - adapt_reference + adapt_dist;
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
	} else if(optics_mode == 2 || optics_mode == 3){ //frustum perspective/othogonal
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

    outlet(0, "ortho", ortho_mode);

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
