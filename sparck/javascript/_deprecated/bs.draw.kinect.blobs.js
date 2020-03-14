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
setoutletassist(1,"jit.anim.node commands");

var color = new Array(1., 1., 1., 1.);

var undefined = "undefined";

var parentNodeName = undefined;

var lastFrameNumber = 0;

var localAnimNode;
var sketchAnimNode;
var blobSketches;

var isSetup = false;

// setup all the instances needed in here
function loadbang(){
	//post("setup kinect tracking...\n");

	localAnimNode = new JitterObject("jit.anim.node");
	localAnimNode.automatic = 0;

	sketchAnimNode = new JitterObject("jit.anim.node");
	sketchAnimNode.automatic = 0;

	blobSketches = new JitterObject("jit.gl.sketch", "sparck");
	blobSketches.enable = 0;
	blobSketches.anim = sketchAnimNode.name;

	outlet(0, "message", "anim", localAnimNode.name);

	if(parentNodeName != undefined){
		localAnimNode.anim = parentNodeName;
		sketchAnimNode.anim = parentNodeName;
	}

	isSetup = true;

	refreshprops();

	//post("jit.anim.node: " + animnode.name+ "..\n");
}

function enable(_enable){
	if(isSetup){
		blobSketches.enable = _enable;
	}
}

function notifydeleted(){
	outlet(0, "message", "anim");
	blobSketches.freepeer();
	localAnimNode.freepeer();
	sketchAnimNode.freepeer();
}

function bang(){
	if(isSetup){
		localAnimNode.update_node();
		sketchAnimNode.update_node();
		outlet(0, "messagestream", "worldpos", localAnimNode.worldpos);
		outlet(0, "messagestream", "worldquat", localAnimNode.worldquat);
		outlet(0, "messagestream", "worldscale", localAnimNode.worldscale);
		outlet(0, "messagestream", "animcascade", "bang");
		outlet(0, "messagewormhole", "bang");
	}
}

function anim(){
	parentNodeName = arguments[0];
	if(isSetup){
		//post("draw kinect blobs anim: " + arguments[0] + "\n");
		localAnimNode.anim = arguments[0];
		sketchAnimNode.anim = arguments[0];
	}
}

function scenedraw(){
//	post("anything: " + arguments[1] + "\n");
	if(isSetup){
		blobSketches.enable = arguments[1];
	}
}

function anything(){
	if(isSetup){
		if(messagename == "/ks/server/track/eye"){
			trackEye(arrayfromargs(arguments));
		} else if(messagename == "/ks/server/track/headblob"){
			trackHeadBlob(arrayfromargs(arguments));
		} else if(messagename == "/ks/server/track/bodyblob"){
			trackBodyBlob(arrayfromargs(arguments));
		}
	}
}

function checkFrameNumber(_args){
	if(_args[1] != lastFrameNumber){
		lastFrameNumber = _args[1];
		blobSketches.reset();
		blobSketches.poly_mode = new Array(1, 1);
		blobSketches.lighting_enable = 0;
		blobSketches.glcolor(new Array(color[0], color[1], color[2], color[3]));
		blobSketches.shapeprim("line");
		blobSketches.gllinewidth = 1;
	}
}

function trackEye(_args){
	checkFrameNumber(_args);
	if(_args[3] == 0){
		localAnimNode.position = new Array(_args[4], _args[5], _args[6]);
		localAnimNode.lookat = new Array(_args[4] + _args[7], _args[5] + _args[8], _args[6] + _args[9]);
	}
}

function trackHeadBlob(_args){
	checkFrameNumber(_args);
	blobSketches.framequad(
		_args[4] - _args[7] / 2., _args[5] - _args[8] / 2., _args[6],
		_args[4] - _args[7] / 2., _args[5] + _args[8] / 2., _args[6],
		_args[4] + _args[7] / 2., _args[5] + _args[8] / 2., _args[6],
		_args[4] + _args[7] / 2., _args[5] - _args[8] / 2., _args[6])
}

function trackBodyBlob(_args){
	checkFrameNumber(_args);
	if(_args[3] == 0)
		blobSketches.glcolor(new Array(255, 0, 0, 255));
	else
		blobSketches.glcolor(new Array(255, 255, 255, 255));

	blobSketches.framequad(
		_args[4] - _args[6] / 2., _args[5] - _args[7] / 2., _args[8],
		_args[4] - _args[6] / 2., _args[5] + _args[7] / 2., _args[8],
		_args[4] + _args[6] / 2., _args[5] + _args[7] / 2., _args[8],
		_args[4] + _args[6] / 2., _args[5] - _args[7] / 2., _args[8]);
	blobSketches.framequad(
		_args[4] - _args[6] / 2., _args[5] - _args[7] / 2., 0,
		_args[4] - _args[6] / 2., _args[5] + _args[7] / 2., 0,
		_args[4] + _args[6] / 2., _args[5] + _args[7] / 2., 0,
		_args[4] + _args[6] / 2., _args[5] - _args[7] / 2., 0);
	blobSketches.linesegment(
		_args[4] - _args[6] / 2., _args[5] - _args[7] / 2., _args[8],
		_args[4] - _args[6] / 2., _args[5] - _args[7] / 2., 0);
	blobSketches.linesegment(
		_args[4] - _args[6] / 2., _args[5] + _args[7] / 2., _args[8],
		_args[4] - _args[6] / 2., _args[5] + _args[7] / 2., 0);
	blobSketches.linesegment(
		_args[4] + _args[6] / 2., _args[5] + _args[7] / 2., _args[8],
		_args[4] + _args[6] / 2., _args[5] + _args[7] / 2., 0);
	blobSketches.linesegment(
		_args[4] + _args[6] / 2., _args[5] - _args[7] / 2., _args[8],
		_args[4] + _args[6] / 2., _args[5] - _args[7] / 2., 0);
	//outlet(0,"cube", _args[6], _args[7], _args[8] / 2.);
}

function refreshprops(){
	outlet(0, "refreshprops", "bang");
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
