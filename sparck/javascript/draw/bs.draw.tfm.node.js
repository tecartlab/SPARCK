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
outlets = 1;
setinletassist(0,"size");
setoutletassist(0,"jit.gl.sketch commands");

var undefined = "undefined";

var myType = "point";
var ssize = .2;

var color = new Array(1., 1., 1., 1.);

var isSetup = false;

var worldPos = undefined;
var parentPos = undefined;
var rotateXYZ = undefined;

var drawParentlink = false;
var drawPosition = false;
var drawRotation = false;

var nodeSketch = undefined;

function loadbang(){
	nodeSketch = new JitterObject("jit.gl.sketch", "sparck");
	nodeSketch.enable = 0;
//	post("setup()\n");
	isSetup = true;
}

function enable(_enable){
	if(isSetup){
		nodeSketch.enable = _enable;
	}
}

function worldpos(){
	worldPos = arrayfromargs(arguments);
//	post("worldPos("+worldPos+")\n");
	draw();
}

function parentpos(){
	parentPos = arrayfromargs(arguments);
//	post("parentPos("+parentPos+")\n");
	draw();
}

function rotatexyz(){
	rotateXYZ = arrayfromargs(arguments);
	draw();
}

function drawparentlink(){
	drawParentlink = (arguments[0] == 1)?true: false;
	if(drawParentlink)
		outlet(0, "bang");
	else
		draw();
}

function drawposition(){
	drawPosition = (arguments[0] == 1)?true: false;
//	post("drawposition("+drawPosition+")\n");
	if(drawPosition)
		outlet(0, "bang");
	else
		draw();
}

function drawrotation(){
	drawRotation = (arguments[0] == 1)?true: false;
	if(drawRotation)
		outlet(0, "bang");
	else
		draw();
}

function color0(){
	color = arrayfromargs(arguments);
}

function draw(){
	if(isSetup){
		nodeSketch.reset();
		nodeSketch.lighting_enable = 0;
		if(drawParentlink && parentPos != undefined && worldPos != undefined){
			nodeSketch.glcolor(new Array(color[0], color[1], color[2], color[3]));
			nodeSketch.shapeprim("line");
			nodeSketch.gllinewidth = 1;
			nodeSketch.gllinestipple = new Array(100, 127);
			nodeSketch.linesegment(parentPos[0], parentPos[1], parentPos[2], worldPos[0], worldPos[1], worldPos[2]);
		}
		if(drawRotation){
		}
		if(drawPosition && worldPos != undefined){
			var sa = ssize * 1.1;
			nodeSketch.gllinewidth = 1;
			nodeSketch.glcolor(1, 0, 0, 1);
			nodeSketch.linesegment(worldPos[0] - sa, worldPos[1], worldPos[2], worldPos[0] + sa, worldPos[1], worldPos[2]);
			nodeSketch.glcolor(0, 1, 0, 1);
			nodeSketch.linesegment(worldPos[0], worldPos[1] - sa, worldPos[2], worldPos[0], worldPos[1] + sa, worldPos[2]);
			nodeSketch.glcolor(0, 0, 1, 1);
			nodeSketch.linesegment(worldPos[0], worldPos[1], worldPos[2] - sa, worldPos[0], worldPos[1], worldPos[2] + sa);
		}
	}
}


function notifydeleted(){
	if(nodeSketch != undefined)
		nodeSketch.freepeer();
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
