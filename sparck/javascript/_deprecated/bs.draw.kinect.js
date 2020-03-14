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

var f_left = 0.1;
var f_top = 0.1;
var f_right = -0.1;
var f_bottom = -0.1;
var f_near = 0.1;
var f_far = 100.;

var sb_left = 0.1;
var sb_top = 0.1;
var sb_right = -0.1;
var sb_bottom = -0.1;
var sb_near = 0.1;
var sb_far = 100.;

var	gz_x = 0.;
var	gz_y = 0.;
var	gz_z = 0.;

var undefined = "undefined";

var parentNodeName = undefined;

var positionAnimNode;
var rotationAnimNode;
var sensorAnimNode;
var frustumSketch;
var sensorBoxSketch;

var color = new Array(0.5, 0.5, 0.5, 1.);

var color_blue = new Array(0., 0., 1., 1.);
var color_green = new Array(0., 1., 0., 1.);

var isSetup = false;

// setup all the instances needed in here
function loadbang(){
	//post("setup kinect drawing...\n");

	positionAnimNode = new JitterObject("jit.anim.node");
	positionAnimNode.automatic = 0;

	sensorAnimNode = new JitterObject("jit.anim.node");
	sensorAnimNode.automatic = 0;

	rotationAnimNode = new JitterObject("jit.anim.node");
	rotationAnimNode.automatic = 0;
	rotationAnimNode.anim = positionAnimNode.name;

	frustumSketch = new JitterObject("jit.gl.sketch", "sparck");
	frustumSketch.enable = 0;
	frustumSketch.anim = rotationAnimNode.name;

	sensorBoxSketch = new JitterObject("jit.gl.sketch", "sparck");
	sensorBoxSketch.enable = 0;
	sensorBoxSketch.anim = sensorAnimNode.name;

	if(parentNodeName != undefined){
		positionAnimNode.anim = parentNodeName;
		sensorAnimNode.anim = parentNodeName;
	}

	isSetup = true;

	refreshprops();

	//post("jit.anim.node: " + animnode.name+ "..\n");
}

function enable(_enable){
	if(isSetup){
		frustumSketch.enable = _enable;
		sensorBoxSketch.enable = _enable;
	}
}

function color0(){
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

function anything(){
}

function anim(){
	parentNodeName = arguments[0];
	if(isSetup){
		//post("draw kinect: anim: " + arguments[0] +" \n");
		positionAnimNode.anim = arguments[0];
		sensorAnimNode.anim = arguments[0];
	}
}

function scenedraw(){
	if(isSetup){
		if(arguments[2] == 1){
			frustumSketch.enable = arguments[1];
		}
		sensorBoxSketch.enable = arguments[1];
	}
}

function scenedirect(){
	sensorBoxSketch.enable = arguments[1];
}


function bang(){
	if(isSetup){
		positionAnimNode.update_node();
		rotationAnimNode.update_node();
		sensorAnimNode.update_node();
	}
}

function transform(kinectid, rotX, rotY, posZ){
	if(isSetup){
		positionAnimNode.position = new Array(0, 0, posZ);
		positionAnimNode.update_node();
		rotationAnimNode.rotatexyz = new Array(rotX, rotY, 0);
		rotationAnimNode.update_node();
	}
}

function sensorbox(kinectid, left, right, bottom, top, near, far){
	sb_left = left;
	sb_top = top;
	sb_right = right;
	sb_bottom = bottom;
	sb_near = near;
	sb_far = far;
	draw();
}

function gazepoint(kinectid, x, y, z){
	gz_x = x;
	gz_y = y;
	gz_z = z;
	draw();
}

function frustum(kinectid, left, right, bottom, top, near, far){
	f_left = left;
	f_top = top;
	f_right = right;
	f_bottom = bottom;
	f_near = near;
	f_far = far;
	draw();
}

function draw(){
	if(isSetup){
		drawFrustum();
		drawSensorBox();
	}
}

function drawSensorBox(){
	sensorBoxSketch.reset();
	sensorBoxSketch.glcolor(color_blue);
	sensorBoxSketch.framequad(sb_left, sb_near, sb_bottom, sb_left, sb_far, sb_bottom, sb_right, sb_far, sb_bottom, sb_right, sb_near, sb_bottom);
	sensorBoxSketch.framequad(sb_left, sb_near, sb_top, sb_left, sb_far, sb_top, sb_right, sb_far, sb_top, sb_right, sb_near, sb_top);
    sensorBoxSketch.moveto(gz_x, gz_y, gz_z);
	sensorBoxSketch.glcolor(color_green);
    sensorBoxSketch.sphere(0.05);
}


function drawFrustum(){
    frustumSketch.reset();
    frustumSketch.lighting_enable = 0;
    frustumSketch.glcolor(new Array(color[0], color[1], color[2], color[3]));
    frustumSketch.shapeprim("line");
    frustumSketch.gllinewidth = 1;
    frustumSketch.gllinestipple = new Array(100, 127);
    frustumSketch.cube(.2, .05, .02);

    var coneRatio = f_far / f_near;
	frustumSketch.linesegment(f_left, f_top, -f_near, f_left * coneRatio, f_top * coneRatio, -f_far);
	frustumSketch.linesegment(f_right, f_top, -f_near, f_right * coneRatio, f_top * coneRatio, -f_far);
	frustumSketch.linesegment(f_right, f_bottom, -f_near, f_right * coneRatio, f_bottom * coneRatio, -f_far);
	frustumSketch.linesegment(f_left, f_bottom, -f_near, f_left * coneRatio, f_bottom * coneRatio, -f_far);
}

function refreshprops(){
	outlet(0, "refreshproperties");
}

function notifydeleted(){
	positionAnimNode.freepeer();
	rotationAnimNode.freepeer();
	sensorAnimNode.freepeer();
	frustumSketch.freepeer();
	sensorBoxSketch.freepeer();
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
