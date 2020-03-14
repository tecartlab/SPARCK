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

var left, front, right, back, top, bottom = false;

var undefined = "undefined";

var cameraNodeName = undefined;

var cameraAnimNode = undefined;

var cameraSketch = undefined;

var color = new Array(1., 1., 1., 1.);

var isSetup = false;

var stereoMode = 0;

// setup all the instances needed in here
function loadbang(){
//	post("setup kinect drawing...\n");

	cameraAnimNode = new JitterObject("jit.anim.node");
	cameraAnimNode.automatic = 0;

	cameraSketch = new JitterObject("jit.gl.sketch", "sparck");
	cameraSketch.enable = 0;
	cameraSketch.anim = cameraAnimNode.name;

  	if(cameraNodeName != undefined){
		cameraAnimNode.anim = cameraNodeName;
	}

	isSetup = true;

	//post("jit.anim.node: " + animnode.name+ "..\n");
}

function enable(_enable){
	if(isSetup){
 		cameraSketch.enable = _enable;
	}
}

function enable_cams(){
    var cams = arrayfromargs(arguments);
    left = (cams[0] == 1)?true: false;
    front = (cams[1] == 1)?true: false;
    right = (cams[2] == 1)?true: false;
    back = (cams[3] == 1)?true: false;
    top = (cams[4] == 1)?true: false;
    bottom = (cams[5] == 1)?true: false;
    draw();
}

function color0(){
    color = arrayfromargs(arguments);
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

function drawto(_context){
    if(cameraSketch != null){
        cameraSketch.drawto = _context;
    }
}

function notifydeleted(){
    if(cameraSketch != null){
        cameraSketch.freepeer();
        cameraAnimNode.freepeer();
    }
}

function anim(){
	cameraNodeName = arguments[0];
//	post("draw beamer: anim: " + arguments[0] +" \n");
	if(isSetup){
		cameraAnimNode.anim = arguments[0];
	}
}

function bang(){
	if(isSetup){
		cameraAnimNode.update_node();
		outlet(0, "position", cameraAnimNode.worldpos);
		outlet(0, "quat", cameraAnimNode.worldquat);
	}
}

function frustum(left, right, bottom, top, near, far){
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
        cameraSketch.reset();
        cameraSketch.poly_mode = [1, 1];
        cameraSketch.lighting_enable = 0;

        drawBeamerBox();
        if(left){
            cameraSketch.glrotate(90, 0., 1., 0);
            drawFrustum();
            cameraSketch.glrotate(-90, 0., 1., 0);
        }
        if(front){
            drawFrustum();
        }
        if(right){
            cameraSketch.glrotate(-90, 0., 1., 0);
            drawFrustum();
            cameraSketch.glrotate(90, 0., 1., 0);
        }
        if(back){
            cameraSketch.glrotate(-180, 0., 1., 0);
            drawFrustum();
            cameraSketch.glrotate(180, 0., 1., 0);
        }
        if(top){
            cameraSketch.glrotate(90, 1., 0., 0);
            drawFrustum();
            cameraSketch.glrotate(-90, 1., 0., 0);
        }
        if(bottom){
            cameraSketch.glrotate(-90, 1., 0., 0);
            drawFrustum();
            cameraSketch.glrotate(90, 1., 0., 0);
        }

	}
}

function drawFrustum(){
	var coneRatio = f_far / f_near;
    //cameraSketch.lighting_enable = 0;
    //cameraSketch.glcolor(new Array(color[0], color[1], color[2], color[3]));
    //cameraSketch.shapeprim("line");
    cameraSketch.gllinewidth = 1;
    cameraSketch.gllinestipple = new Array(3, 127);
	cameraSketch.linesegment(f_left, f_top, -f_near, f_left * coneRatio, f_top * coneRatio, -f_far);
	cameraSketch.linesegment(f_left * coneRatio, f_top * coneRatio, -f_far, f_left * coneRatio * 0.7, f_top * coneRatio, -f_far);
	cameraSketch.linesegment(f_left * coneRatio, f_top * coneRatio, -f_far, f_left * coneRatio, f_top * coneRatio * 0.7, -f_far);
	cameraSketch.linesegment(f_right, f_top, -f_near, f_right * coneRatio, f_top * coneRatio, -f_far);
	cameraSketch.linesegment(f_right * coneRatio, f_top * coneRatio, -f_far, f_right * coneRatio * 0.7, f_top * coneRatio, -f_far);
	cameraSketch.linesegment(f_right * coneRatio, f_top * coneRatio, -f_far, f_right * coneRatio, f_top * coneRatio * 0.7, -f_far);
	cameraSketch.linesegment(f_right, f_bottom, -f_near, f_right * coneRatio, f_bottom * coneRatio, -f_far);
	cameraSketch.linesegment(f_right * coneRatio, f_bottom * coneRatio, -f_far, f_right * coneRatio* 0.7, f_bottom * coneRatio, -f_far);
	cameraSketch.linesegment(f_right * coneRatio, f_bottom * coneRatio, -f_far, f_right * coneRatio, f_bottom * coneRatio * 0.7, -f_far);
	cameraSketch.linesegment(f_left, f_bottom, -f_near, f_left * coneRatio, f_bottom * coneRatio, -f_far);
	cameraSketch.linesegment(f_left * coneRatio, f_bottom * coneRatio, -f_far, f_left * coneRatio * 0.7, f_bottom * coneRatio, -f_far);
	cameraSketch.linesegment(f_left * coneRatio, f_bottom * coneRatio, -f_far, f_left * coneRatio, f_bottom * coneRatio * 0.7, -f_far);
    cameraSketch.framequad(f_left * coneRatio * 0.7, f_top * coneRatio * 0.7, -f_far,
                      f_right * coneRatio* 0.7, f_top * coneRatio* 0.7, -f_far,
                      f_right * coneRatio* 0.7, f_bottom * coneRatio* 0.7, -f_far,
                      f_left * coneRatio* 0.7, f_bottom * coneRatio* 0.7, -f_far);
    cameraSketch.framequad(f_left  * 0.7, f_top  * 0.7, -f_near,
                      f_right * 0.7, f_top * 0.7, -f_near,
                      f_right * 0.7, f_bottom * 0.7, -f_near,
                      f_left * 0.7, f_bottom * 0.7, -f_near);
}

function drawBeamerBox(){
	cameraSketch.glcolor(new Array(color[0], color[1], color[2], color[3]));
 	cameraSketch.tri(.13, .13, .0, .0, .25, .0, -.13, .13, .0);
    cameraSketch.moveto(0, 0, 0);
    cameraSketch.shapeslice(2, 2);
	cameraSketch.cube(.13, .13, .13);
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
