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
setinletassist(0,"size");
setoutletassist(0,"jit.gl.sketch commands");
setoutletassist(1,"jit.gl.sketch commands");

var myType = "point";
var ssize = .2;

var f_L_left = 0.1;
var f_L_top = 0.1;
var f_L_right = -0.1;
var f_L_bottom = -0.1;
var f_R_left = 0.1;
var f_R_top = 0.1;
var f_R_right = -0.1;
var f_R_bottom = -0.1;
var f_near = 0.1;
var f_far = 100.;

var undefined = "undefined";

var parentLeftNodeName = undefined;
var parentRightNodeName = undefined;
var parentNodeName = undefined;

var frustumLeftAnimNode = undefined;
var frustumRightAnimNode = undefined;
var sensorAnimNode = undefined;
var frustumLeftSketch = undefined;
var frustumRightSketch = undefined;
var sensorBoxSketch = undefined;

var color = new Array(1., 1., 1., 1.);

var isSetup = false;

var stereoMode = 0;

var isOrthoMode = false;

var updateDraw = false;

// setup all the instances needed in here
function loadbang(){
//	post("setup kinect drawing...\n");

	frustumLeftAnimNode = new JitterObject("jit.anim.node");
	frustumLeftAnimNode.automatic = 1;

	frustumRightAnimNode = new JitterObject("jit.anim.node");
	frustumRightAnimNode.automatic = 1;

	sensorAnimNode = new JitterObject("jit.anim.node");
	sensorAnimNode.automatic = 1;

	frustumLeftSketch = new JitterObject("jit.gl.sketch", "sparck");
	frustumLeftSketch.enable = 0;
	frustumLeftSketch.anim = frustumLeftAnimNode.name;

	frustumRightSketch = new JitterObject("jit.gl.sketch", "sparck");
	frustumRightSketch.enable = 0;
	frustumRightSketch.anim = frustumRightAnimNode.name;

	sensorBoxSketch = new JitterObject("jit.gl.sketch", "sparck");
	sensorBoxSketch.enable = 0;
	sensorBoxSketch.anim = sensorAnimNode.name;

	if(parentLeftNodeName != undefined){
		frustumLeftAnimNode.anim = parentLeftNodeName;
	}
	if(parentRightNodeName != undefined){
		frustumRightAnimNode.anim = parentRightNodeName;
	}
	if(parentNodeName != undefined){
		sensorAnimNode.anim = parentNodeName;
	}

	isSetup = true;

	//post("jit.anim.node: " + animnode.name+ "..\n");
}

function drawto(_context){
    frustumLeftSketch.drawto = _context;
    frustumRightSketch.drawto = _context;
    sensorBoxSketch.drawto = _context;
	bang();
}

function enable(_enable){
	if(isSetup){
		frustumLeftSketch.enable = _enable;
		if(stereoMode == 1){
			frustumRightSketch.enable = _enable;
		}
		sensorBoxSketch.enable = _enable;
	}
	bang();
}

function stereomode(s){
	stereoMode = s;
	if(frustumRightSketch != undefined){
		frustumRightSketch.enable = 0;
	}
}

function dim(){
	// ignore
}

function stereo_spread(sprd){
	sensorBoxSketch.scale = new Array(Math.pow(1 + sprd, 2), 1., 1.);
}

function objcolor(){
    color = arrayfromargs(arguments)
	updateDraw = true;
	//not implemented yet
}

function enablenode(){
	//not implemented yet
}

function verbose(){
	//not implemented yet
}

function normalize(){
	//not implemented yet
}

function axes(){
	//not implemented yet
}

function notifydeleted(){
	frustumLeftAnimNode.freepeer();
	frustumRightAnimNode.freepeer();
	sensorAnimNode.freepeer();
	frustumLeftSketch.freepeer();
	frustumRightSketch.freepeer();
	sensorBoxSketch.freepeer();
}

function anim(){
	parentNodeName = arguments[0];
	if(isSetup){
		sensorAnimNode.anim = arguments[0];
	}
}

function anim_left(){
	parentLeftNodeName = arguments[0];
//	post("draw frustum left: anim: " + arguments[0] +" \n");
	if(isSetup){
		frustumLeftAnimNode.anim = arguments[0];
	}
}

function anim_right(){
	parentRightNodeName = arguments[0];
//	post("draw frustum right: anim: " + arguments[0] +" \n");
	if(isSetup){
		frustumRightAnimNode.anim = arguments[0];
	}
}

function bang(){
	if(isSetup){
		frustumLeftAnimNode.update_node();
		frustumRightAnimNode.update_node();
		sensorAnimNode.update_node();
		outlet(0, "position", sensorAnimNode.worldpos);
		outlet(0, "quat", sensorAnimNode.worldquat);
        if(updateDraw){
		  updateDraw = false;
		  draw();
	   }
	}
}

function ortho(_mode){
	updateDraw = true;
    isOrthoMode = (_mode == 1)?true:false;
}

function frustum(left, right, bottom, top, near, far){
	f_L_left = left;
	f_L_top = top;
	f_L_right = right;
	f_L_bottom = bottom;

    f_R_left = left;
	f_R_top = top;
	f_R_right = right;
	f_R_bottom = bottom;

    f_near = near;
	f_far = far;

	updateDraw = true;
}

function frustum_left(left, right, bottom, top, near, far){
	f_L_left = left;
	f_L_top = top;
	f_L_right = right;
	f_L_bottom = bottom;
	f_near = near;
	f_far = far;

	updateDraw = true;
}

function frustum_right(left, right, bottom, top, near, far){
	f_R_left = left;
	f_R_top = top;
	f_R_right = right;
	f_R_bottom = bottom;
	f_near = near;
	f_far = far;

	updateDraw = true;
}


function draw(){
//	post("Setup = " + isSetup + "\n");
	if(isSetup){
		var sa = ssize * 1.1;
		var sb = ssize * 1.8;
		var sc = ssize *.5;

		frustumLeftSketch.reset();
		frustumLeftSketch.lighting_enable = 0;
		frustumLeftSketch.glcolor(new Array(color[0], color[1], color[2], color[3]));
		frustumLeftSketch.shapeprim("line");
		frustumLeftSketch.gllinewidth = 1;
		frustumLeftSketch.gllinestipple = new Array(100, 127);

		frustumRightSketch.reset();
		frustumRightSketch.lighting_enable = 0;
		frustumRightSketch.glcolor(new Array(color[0], color[1], color[2], color[3]));
		frustumRightSketch.shapeprim("line");
		frustumRightSketch.gllinewidth = 1;
		frustumRightSketch.gllinestipple = new Array(100, 127);

        if(isOrthoMode){
    		drawFrustumOrtho();
        } else {
    		drawFrustumPersp();
        }
		drawSensorBox();
	}
}

function drawSensorBox(){
	sensorBoxSketch.reset();
	sensorBoxSketch.lighting_enable = 0;
	sensorBoxSketch.glcolor(new Array(0, 0, 0, 1));
	sensorBoxSketch.tri(.1, .2, .0, .0, .3, .0, -.1, .2, .0);
	sensorBoxSketch.glcolor(new Array(color[0], color[1], color[2], color[3]));
	sensorBoxSketch.framequad(.2, .2, 0.1, .2, -.2, 0.1, -.2, -.2, 0.1, -0.2, 0.2, 0.1);
	sensorBoxSketch.framequad(.2, .2, .0, .2, -.2, .0, -.2, -.2, .0, -0.2, 0.2, 0.);
	sensorBoxSketch.moveto(0., 0., 0.1);
	//sensorBoxSketch.plane(.1);
}


function drawFrustumOrtho(){
	frustumLeftSketch.linesegment(f_L_left, f_L_top, -f_near, f_L_left, f_L_top, -f_far);
	frustumLeftSketch.linesegment(f_L_left, f_L_top, -f_far, f_L_left * 0.7, f_L_top, -f_far);
	frustumLeftSketch.linesegment(f_L_left, f_L_top, -f_far, f_L_left, f_L_top * 0.7, -f_far);
	frustumLeftSketch.linesegment(f_L_right, f_L_top, -f_near, f_L_right, f_L_top, -f_far);
	frustumLeftSketch.linesegment(f_L_right, f_L_top, -f_far, f_L_right  * 0.7, f_L_top, -f_far);
	frustumLeftSketch.linesegment(f_L_right, f_L_top, -f_far, f_L_right, f_L_top  * 0.7, -f_far);
	frustumLeftSketch.linesegment(f_L_right, f_L_bottom, -f_near, f_L_right, f_L_bottom, -f_far);
	frustumLeftSketch.linesegment(f_L_right, f_L_bottom, -f_far, f_L_right * 0.7, f_L_bottom, -f_far);
	frustumLeftSketch.linesegment(f_L_right, f_L_bottom, -f_far, f_L_right, f_L_bottom  * 0.7, -f_far);
	frustumLeftSketch.linesegment(f_L_left, f_L_bottom, -f_near, f_L_left, f_L_bottom, -f_far);
	frustumLeftSketch.linesegment(f_L_left, f_L_bottom, -f_far, f_L_left  * 0.7, f_L_bottom, -f_far);
	frustumLeftSketch.linesegment(f_L_left, f_L_bottom, -f_far, f_L_left, f_L_bottom  * 0.7, -f_far);

	if(stereoMode == 1){
    	frustumRightSketch.linesegment(f_R_left, f_R_top, -f_near, f_R_left, f_R_top, -f_far);
		frustumRightSketch.linesegment(f_R_left, f_R_top, -f_far, f_R_left  * 0.7, f_R_top, -f_far);
		frustumRightSketch.linesegment(f_R_left, f_R_top, -f_far, f_R_left, f_R_top  * 0.7, -f_far);
		frustumRightSketch.linesegment(f_R_right, f_R_top, -f_near, f_R_right, f_R_top, -f_far);
		frustumRightSketch.linesegment(f_R_right, f_R_top, -f_far, f_R_right  * 0.7, f_R_top, -f_far);
		frustumRightSketch.linesegment(f_R_right, f_R_top, -f_far, f_R_right, f_R_top  * 0.7, -f_far);
		frustumRightSketch.linesegment(f_R_right, f_R_bottom, -f_near, f_R_right, f_R_bottom, -f_far);
		frustumRightSketch.linesegment(f_R_right, f_R_bottom, -f_far, f_R_right * 0.7, f_R_bottom, -f_far);
		frustumRightSketch.linesegment(f_R_right, f_R_bottom, -f_far, f_R_right, f_R_bottom  * 0.7, -f_far);
		frustumRightSketch.linesegment(f_R_left, f_R_bottom, -f_near, f_R_left, f_R_bottom, -f_far);
		frustumRightSketch.linesegment(f_R_left, f_R_bottom, -f_far, f_R_left  * 0.7, f_R_bottom, -f_far);
		frustumRightSketch.linesegment(f_R_left, f_R_bottom, -f_far, f_R_left, f_R_bottom  * 0.7, -f_far);
	}
}

function drawFrustumPersp(){
	var coneRatio = f_far / f_near;
	frustumLeftSketch.linesegment(f_L_left, f_L_top, -f_near, f_L_left * coneRatio, f_L_top * coneRatio, -f_far);
	frustumLeftSketch.linesegment(f_L_left * coneRatio, f_L_top * coneRatio, -f_far, f_L_left * coneRatio * 0.7, f_L_top * coneRatio, -f_far);
	frustumLeftSketch.linesegment(f_L_left * coneRatio, f_L_top * coneRatio, -f_far, f_L_left * coneRatio, f_L_top * coneRatio * 0.7, -f_far);
	frustumLeftSketch.linesegment(f_L_right, f_L_top, -f_near, f_L_right * coneRatio, f_L_top * coneRatio, -f_far);
	frustumLeftSketch.linesegment(f_L_right * coneRatio, f_L_top * coneRatio, -f_far, f_L_right * coneRatio * 0.7, f_L_top * coneRatio, -f_far);
	frustumLeftSketch.linesegment(f_L_right * coneRatio, f_L_top * coneRatio, -f_far, f_L_right * coneRatio, f_L_top * coneRatio * 0.7, -f_far);
	frustumLeftSketch.linesegment(f_L_right, f_L_bottom, -f_near, f_L_right * coneRatio, f_L_bottom * coneRatio, -f_far);
	frustumLeftSketch.linesegment(f_L_right * coneRatio, f_L_bottom * coneRatio, -f_far, f_L_right * coneRatio* 0.7, f_L_bottom * coneRatio, -f_far);
	frustumLeftSketch.linesegment(f_L_right * coneRatio, f_L_bottom * coneRatio, -f_far, f_L_right * coneRatio, f_L_bottom * coneRatio * 0.7, -f_far);
	frustumLeftSketch.linesegment(f_L_left, f_L_bottom, -f_near, f_L_left * coneRatio, f_L_bottom * coneRatio, -f_far);
	frustumLeftSketch.linesegment(f_L_left * coneRatio, f_L_bottom * coneRatio, -f_far, f_L_left * coneRatio * 0.7, f_L_bottom * coneRatio, -f_far);
	frustumLeftSketch.linesegment(f_L_left * coneRatio, f_L_bottom * coneRatio, -f_far, f_L_left * coneRatio, f_L_bottom * coneRatio * 0.7, -f_far);

	if(stereoMode == 1){
    	frustumRightSketch.linesegment(f_R_left, f_R_top, -f_near, f_R_left * coneRatio, f_R_top * coneRatio, -f_far);
		frustumRightSketch.linesegment(f_R_left * coneRatio, f_R_top * coneRatio, -f_far, f_R_left * coneRatio * 0.7, f_R_top * coneRatio, -f_far);
		frustumRightSketch.linesegment(f_R_left * coneRatio, f_R_top * coneRatio, -f_far, f_R_left * coneRatio, f_R_top * coneRatio * 0.7, -f_far);
		frustumRightSketch.linesegment(f_R_right, f_R_top, -f_near, f_R_right * coneRatio, f_R_top * coneRatio, -f_far);
		frustumRightSketch.linesegment(f_R_right * coneRatio, f_R_top * coneRatio, -f_far, f_R_right * coneRatio * 0.7, f_R_top * coneRatio, -f_far);
		frustumRightSketch.linesegment(f_R_right * coneRatio, f_R_top * coneRatio, -f_far, f_R_right * coneRatio, f_R_top * coneRatio * 0.7, -f_far);
		frustumRightSketch.linesegment(f_R_right, f_R_bottom, -f_near, f_R_right * coneRatio, f_R_bottom * coneRatio, -f_far);
		frustumRightSketch.linesegment(f_R_right * coneRatio, f_R_bottom * coneRatio, -f_far, f_R_right * coneRatio* 0.7, f_R_bottom * coneRatio, -f_far);
		frustumRightSketch.linesegment(f_R_right * coneRatio, f_R_bottom * coneRatio, -f_far, f_R_right * coneRatio, f_R_bottom * coneRatio * 0.7, -f_far);
		frustumRightSketch.linesegment(f_R_left, f_R_bottom, -f_near, f_R_left * coneRatio, f_R_bottom * coneRatio, -f_far);
		frustumRightSketch.linesegment(f_R_left * coneRatio, f_R_bottom * coneRatio, -f_far, f_R_left * coneRatio * 0.7, f_R_bottom * coneRatio, -f_far);
		frustumRightSketch.linesegment(f_R_left * coneRatio, f_R_bottom * coneRatio, -f_far, f_R_left * coneRatio, f_R_bottom * coneRatio * 0.7, -f_far);
	}
}

// error("What just happened?\n");
// post(1,2,3,"violet",a);
// cpost (any arguments); -> to system console:
// messnamed (Max object name, message name, any arguments)
// messnamed("flower","bang”);
// a = new Array(900,1000,1100);
// jsthis -> this instance
//  autowatch (1) -> global code -> autoload after js-file was edited and saved

// patcher arguments
//if (jsarguments.length >= 2)
//    outlets = jsarguments[1];

// var stuff;
//  function anything(val)
//  {
//    if (arguments.length) // were there any arguments?
//      stuff[messagename] = val;
//  }

// function anything()
//  {
//    var a = arrayfromargs(messagename,arguments);

//    a.sort();
//    outlet(0,a);
//  }
