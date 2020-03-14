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

var undefined = "undefined";

var targetSketch = new JitterObject("jit.gl.gridshape", "sparck");;
var sensorBoxSketch = new JitterObject("jit.gl.sketch", "sparck");;

var myColor = new Array(1., 1., 1., 1.);

var isSetup = false;
var targetInit, beamerInit = false;

var stereoMode = 0;

var updateDraw = false;

// setup all the instances needed in here
function loadbang(){
//	post("setup kinect drawing...\n");
	targetSketch.enable = 0;
	targetSketch.shape = 'cube';
	targetSketch.poly_mode = new Array(2, 2);
	targetSketch.dim = new Array(8, 8);
	targetSketch.blend_enable = 1;
	targetSketch.color = new Array(1, 1, 1, 0);

    sensorBoxSketch.enable = 0;

	isSetup = true;
	//post("jit.anim.node: " + animnode.name+ "..\n");
}

function title(_title){
    targetSketch.name = _title+'.model';
}

function enable(_enable){
    targetSketch.enable = _enable;
    sensorBoxSketch.enable = _enable;
}

function drawto(_context){
    targetSketch.drawto = _context;
    sensorBoxSketch.drawto = _context;
}

function stereomode(s){
	stereoMode = s;
}

function color(){
    vars = arrayfromargs(arguments);
    myColor = new Array(vars[0], vars[1], vars[2], 0.5);
	updateDraw = true;
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
    targetSketch.freepeer();
    sensorBoxSketch.freepeer();
}

function anything(){
    //ignore
}

// Used by the capture properties
function worldpos(){
    targetSketch.position = arrayfromargs(arguments);
    sensorBoxSketch.position = arrayfromargs(arguments);
	//post("update worldtransform: " + arrayfromargs(arguments) +" have set " + beamerNodeName +" \n");
}

function worldquat(){
    targetSketch.quat = arrayfromargs(arguments);
    sensorBoxSketch.quat = arrayfromargs(arguments);
	//post("update worldtransform: " + arrayfromargs(arguments) +" have set " + beamerNodeName +" \n");
}

function bang(){
	if(updateDraw){
		updateDraw = false;
		draw();
	}
}

function frustum(left, right, bottom, top, near, far){
	f_left = left;
	f_top = top;
	f_right = right;
	f_bottom = bottom;
	f_near = near;
	f_far = far;

	updateDraw = true;
}

function draw(){
	if(isSetup){
        sensorBoxSketch.reset();
        sensorBoxSketch.poly_mode = [1, 1];
        sensorBoxSketch.lighting_enable = 0;
        targetSketch.lighting_enable = 0;

        drawFrustum();
		drawBeamerBox();
	}
}

function drawFrustum(){
	var coneRatio = f_far / f_near;
    sensorBoxSketch.lighting_enable = 0;
    sensorBoxSketch.glcolor(new Array(myColor[0], myColor[1], myColor[2], myColor[3]));
    sensorBoxSketch.shapeprim("line");
    sensorBoxSketch.gllinewidth = 1;
    sensorBoxSketch.gllinestipple = new Array(100, 127);
	sensorBoxSketch.linesegment(f_left, f_top, -f_near, f_left * coneRatio, f_top * coneRatio, -f_far);
	sensorBoxSketch.linesegment(f_left * coneRatio, f_top * coneRatio, -f_far, f_left * coneRatio * 0.7, f_top * coneRatio, -f_far);
	sensorBoxSketch.linesegment(f_left * coneRatio, f_top * coneRatio, -f_far, f_left * coneRatio, f_top * coneRatio * 0.7, -f_far);
	sensorBoxSketch.linesegment(f_right, f_top, -f_near, f_right * coneRatio, f_top * coneRatio, -f_far);
	sensorBoxSketch.linesegment(f_right * coneRatio, f_top * coneRatio, -f_far, f_right * coneRatio * 0.7, f_top * coneRatio, -f_far);
	sensorBoxSketch.linesegment(f_right * coneRatio, f_top * coneRatio, -f_far, f_right * coneRatio, f_top * coneRatio * 0.7, -f_far);
	sensorBoxSketch.linesegment(f_right, f_bottom, -f_near, f_right * coneRatio, f_bottom * coneRatio, -f_far);
	sensorBoxSketch.linesegment(f_right * coneRatio, f_bottom * coneRatio, -f_far, f_right * coneRatio* 0.7, f_bottom * coneRatio, -f_far);
	sensorBoxSketch.linesegment(f_right * coneRatio, f_bottom * coneRatio, -f_far, f_right * coneRatio, f_bottom * coneRatio * 0.7, -f_far);
	sensorBoxSketch.linesegment(f_left, f_bottom, -f_near, f_left * coneRatio, f_bottom * coneRatio, -f_far);
	sensorBoxSketch.linesegment(f_left * coneRatio, f_bottom * coneRatio, -f_far, f_left * coneRatio * 0.7, f_bottom * coneRatio, -f_far);
	sensorBoxSketch.linesegment(f_left * coneRatio, f_bottom * coneRatio, -f_far, f_left * coneRatio, f_bottom * coneRatio * 0.7, -f_far);
}

function drawBeamerBox(){
	sensorBoxSketch.glcolor(new Array(myColor[0], myColor[1], myColor[2], myColor[3]));
 	sensorBoxSketch.tri(.18, .1, .0, .0, .2, .0, -.18, .1, .0);
	sensorBoxSketch.framequad(.18, .1, 0., .18, .1, 0.2, -.18, .1, 0.2, -.18, .1, 0.);
	sensorBoxSketch.framequad(.18, -.1, 0., .18, -.1, 0.2, -.18, -.1, 0.2, -.18, -.1, 0.);
	sensorBoxSketch.framequad();
	sensorBoxSketch.moveto(0., 0., -0.03);
    sensorBoxSketch.shapeslice(8, 1);
	sensorBoxSketch.cylinder(.08, .09, 0.03);
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
