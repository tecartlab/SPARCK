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
outlets = 6;
setinletassist(0,"size");
setoutletassist(0,"jit.gl.sketch commands");
setoutletassist(1,"jit.anim.node commands");

var OUT_INFO = 0;
var OUT_EYE = 1;
var OUT_GAZE = 2;
var OUT_HEAD = 3;
var OUT_TRANS = 4;
var OUT_DUMP = 5;

var color = new Array(0.5, 0.5, 0.5, 1.);

var color_sensorBox = new Array(1., 1., 0., 1.);
var color_frustum = new Array(0., 0., 1., 1.);
var color_gaze = new Array(1., .4, .4, 1.);
var color_eye = new Array(0., 0., 1., 1.);
var color_head = new Array(0.4, 0.4, 1., 1.);
var color_body = new Array(1., .4, 1., 1.);

var undefined = "undefined";

var parentNodeName = undefined;

var lastFrameNumber = 0;

var animNode_head;
var animNode_eye;
var animNode_gaze;
var animNode_calibPos;
var animNode_calibRot;

var animNode_drawBlobs;
var animNode_drawBox;

var sketchBlobs;
var sketchFrustum;
var sketchSensorBox;
var sketchGazePoint;

var isSetup = false;

var updateEye = false;
var updateHead = false;
var updateGaze = false;
var updateTrans = false;

var publishEye = 0;
var publishHead = 0;
var publishGaze = 0;
var publishKinect = 0;

var isDrawn = false;

var sendBodyBlob = false;
var sendHeadBlob = false;
var sendHead = false;
var sendEye = false;

var nodeEnabled = 0;

// setup all the instances needed in here
function loadbang(){
	//post("setup kinect tracking...\n");

	animNode_eye = new JitterObject("jit.anim.node");
	animNode_eye.automatic = 0;

	animNode_head = new JitterObject("jit.anim.node");
	animNode_head.automatic = 0;

	animNode_gaze = new JitterObject("jit.anim.node");
	animNode_gaze.automatic = 0;

    animNode_calibPos = new JitterObject("jit.anim.node");
	animNode_calibPos.automatic = 0;
    animNode_calibRot = new JitterObject("jit.anim.node");
	animNode_calibRot.automatic = 0;
	animNode_calibRot.anim = animNode_calibPos.name;

	animNode_drawBlobs = new JitterObject("jit.anim.node");
	animNode_drawBlobs.automatic = 0;

	animNode_drawBox = new JitterObject("jit.anim.node");
	animNode_drawBox.automatic = 0;

	sketchGazePoint = new JitterObject("jit.gl.sketch", "sparck");
	sketchGazePoint.enable = 0;
	sketchGazePoint.glcolor(color_gaze);
    sketchGazePoint.sphere(0.02);
 	sketchGazePoint.anim = animNode_gaze.name;

	sketchBlobs = new JitterObject("jit.gl.sketch", "sparck");
	sketchBlobs.enable = 0;
	sketchBlobs.anim = animNode_drawBlobs.name;

    sketchSensorBox = new JitterObject("jit.gl.sketch", "sparck");
	sketchSensorBox.enable = 0;
	sketchSensorBox.anim = animNode_drawBox.name;

    sketchFrustum = new JitterObject("jit.gl.sketch", "sparck");
	sketchFrustum.enable = 0;
	sketchFrustum.anim = animNode_calibRot.name;

	if(parentNodeName != undefined){
		animNode_calibPos.anim = parentNodeName;
		animNode_head.anim = parentNodeName;
		animNode_eye.anim = parentNodeName;
		animNode_gaze.anim = parentNodeName;
		animNode_drawBlobs.anim = parentNodeName;
        animNode_drawBox.anim = parentNodeName;
	}

	isSetup = true;

	refreshprops();

	//post("jit.anim.node: " + animnode.name+ "..\n");
}

function publish(_publishEye, _publishGaze, _publishHead, _publishKinect){
    publishEye = _publishEye;
    publishHead = _publishHead;
    publishGaze = _publishGaze;
    publishKinect = _publishKinect;
    publishEnable();
}

// called by the node to enable
function enable(_enable){
    nodeEnabled = _enable;
    publishEnable();
}

function publishEnable(){
    outlet(OUT_EYE, "setup", "enable", nodeEnabled * publishEye);
    outlet(OUT_HEAD, "setup", "enable", nodeEnabled * publishHead);
    outlet(OUT_GAZE, "setup", "enable", nodeEnabled * publishGaze);
    outlet(OUT_TRANS, "setup", "enable", nodeEnabled * publishKinect);
}

function dispose(){
    outlet(OUT_EYE, "message", "anim");
    outlet(OUT_HEAD, "message", "anim");
    outlet(OUT_GAZE, "message", "anim");
    outlet(OUT_TRANS, "message", "anim");
    outlet(OUT_EYE, "wh", "bang");
    outlet(OUT_HEAD, "wh", "bang");
    outlet(OUT_GAZE, "wh", "bang");
    outlet(OUT_TRANS, "wh", "bang");

    animNode_calibPos.freepeer();
	animNode_calibRot.freepeer();
    animNode_eye.freepeer();
    animNode_head.freepeer();
    animNode_gaze.freepeer();
	animNode_drawBlobs.freepeer();
	animNode_drawBox.freepeer();

	sketchBlobs.freepeer();
	sketchFrustum.freepeer();
	sketchSensorBox.freepeer();
    sketchGazePoint.freepeer();
}

// next transformation bang, time to update downstream anim nodes
function bang(){
	if(isSetup){
		animNode_calibPos.update_node(); // has to be before animNode_calibRot
		animNode_calibRot.update_node();
		animNode_gaze.update_node();
		animNode_eye.update_node();
		animNode_head.update_node();
		animNode_drawBlobs.update_node();
        animNode_drawBox.update_node();

        outlet(OUT_EYE, "wh", "bang");
        outlet(OUT_HEAD, "wh", "bang");
        outlet(OUT_GAZE, "wh", "bang");
        outlet(OUT_TRANS, "wh", "bang");
	}
}

function color0(_red, _green, _blue, _alpha){
    color = new Array(_red, _green, _blue, _alpha);
}

function setmsgtitle(_title){
    animNode_eye.name = _title + ":eye";
    animNode_head.name = _title + ":head";
    animNode_gaze.name = _title + ":gaze";
    animNode_calibRot.name = _title + ":trans";

    sketchGazePoint.anim = animNode_gaze.name;
	sketchFrustum.anim = animNode_calibRot.name;

    outlet(OUT_EYE, "setup", "setmsgtitle", _title);
    outlet(OUT_HEAD, "setup", "setmsgtitle", _title);
    outlet(OUT_GAZE, "setup", "setmsgtitle", _title);
    outlet(OUT_TRANS, "setup", "setmsgtitle", _title);

    outlet(OUT_EYE, "message", "anim", animNode_eye.name);
    outlet(OUT_HEAD, "message", "anim", animNode_head.name);
    outlet(OUT_GAZE, "message", "anim", animNode_gaze.name);
    outlet(OUT_TRANS, "message", "anim", animNode_calibRot.name);

    outlet(OUT_EYE, "wh", "bang");
    outlet(OUT_HEAD, "wh", "bang");
    outlet(OUT_GAZE, "wh", "bang");
    outlet(OUT_TRANS, "wh", "bang");
}

// sets the parent anim node
function anim(_parentname){
	parentNodeName = _parentname;
	if(isSetup){
		//post("draw kinect blobs anim: " + arguments[0] + "\n");
		animNode_calibPos.anim = _parentname;
		animNode_gaze.anim = _parentname;
		animNode_eye.anim = _parentname;
		animNode_head.anim = _parentname;
		animNode_drawBlobs.anim = _parentname;
		animNode_drawBox.anim = _parentname;
	}
}

// is called if the HeadSpace helper data is drawn to 3dViewer
function draw(_enable){
    isDrawn = (_enable == 1)?true: false;
}

// is called if the 3dViewer is drawn and it needs to be drawn
function scenedraw(){
//	post("anything: " + arguments[1] + "\n");
    arsg = arrayfromargs(arguments)
	if(isSetup){
        if(args[0] === "enable"){
            sketchBlobs.enable = args[1];
            sketchSensorBox.enable = args[1];
            sketchGazePoint.enable = args[1];
            sketchFrustum.enable = args[1];
        } else if(args[0] === "drawto"){
            sketchBlobs.drawto = args[1];
            sketchSensorBox.drawto = args[1];
            sketchGazePoint.drawto = args[1];
            sketchFrustum.drawto = args[1];
        }
	}
}

// the kinect transformation node.
function calibTrans(_serverID, rotX, rotY, posZ){
	if(isSetup){
		animNode_calibPos.position = new Array(0, 0, posZ);
		animNode_calibPos.update_node();
		animNode_calibRot.rotatexyz = new Array(rotX, rotY, 0);
		animNode_calibRot.update_node();
        updateTrans = true;
	}
}

function calibSensorbox(_serverID, _left, _right, _bottom, _top, _near, _far){
	if(isSetup){
        sketchSensorBox.reset();
        sketchSensorBox.glcolor(color_sensorBox);
        sketchSensorBox.framequad(_left, _near, _bottom, _left, _far, _bottom, _right, _far, _bottom, _right, _near, _bottom);
        sketchSensorBox.framequad(_left, _near, _top, _left, _far, _top, _right, _far, _top, _right, _near, _top);
    }
}

function calibFrustum(_serverID, _left, _right, _bottom, _top, _near, _far){
 	if(isSetup){
        sketchFrustum.reset();
        sketchFrustum.lighting_enable = 0;
        sketchFrustum.glcolor(color_frustum);
        sketchFrustum.shapeprim("line");
        sketchFrustum.gllinewidth = 1;
        sketchFrustum.gllinestipple = new Array(100, 127);
        sketchFrustum.cube(.2, .05, .02);

        var coneRatio = _far / _near;
        sketchFrustum.linesegment(_left, _top, -_near, _left * coneRatio, _top * coneRatio, -_far);
        sketchFrustum.linesegment(_right, _top, -_near, _right * coneRatio, _top * coneRatio, -_far);
        sketchFrustum.linesegment(_right, _bottom, -_near, _right * coneRatio, _bottom * coneRatio, -_far);
        sketchFrustum.linesegment(_left, _bottom, -_near, _left * coneRatio, _bottom * coneRatio, -_far);
    }
}

function calibGazepoint(_serverID, gazePosX, gazePosY, gazePosZ){
    animNode_gaze.position = new Array(gazePosX, gazePosY, gazePosZ);
    updateGaze = true;
}

function trackFrame(_serverID, _frameNo, _sendBodyBlob, _sendHeadBlob, _sendHead, _sendEye){
    lastFrameNumber = _frameNo;
    sendBodyBlob = (_sendBodyBlob == 1)?true: false;
    sendHeadBlob = (_sendHeadBlob == 1)?true: false;
    sendHead = (_sendHead == 1)?true: false;
    sendEye = (_sendEye == 1)?true: false;

    if(isDrawn){
        sketchBlobs.reset();
        sketchBlobs.poly_mode = new Array(1, 1);
        sketchBlobs.lighting_enable = 0;
        sketchBlobs.shapeprim("line");
        sketchBlobs.gllinewidth = 1;
    }
}

// nothing is done here, yet...
function trackEnd(){
}

// nothing is done here, yet...
function trackFrameEnd(){
}

function trackEye(_serverID, _frameNo, _blobID, _sortPos, _age, _eyePosX, _eyePosY, _eyePosZ, _eyeGazeX, _eyeGazeY, _eyeGazeZ){
	if(_sortPos == 0){
        if(isDrawn){
            sketchBlobs.glcolor(color_eye);
            sketchBlobs.moveto(_eyePosX, _eyePosY, _eyePosZ);
            sketchBlobs.sphere(0.02);
        }
 		animNode_eye.position = new Array(_eyePosX, _eyePosY, _eyePosZ);
        updateEye = true;
	}
}

function trackHead(_serverID, _frameNo, _blobID, _sortPos, _age, _headTopPosX, _headTopPosY, _headTopPosZ, _headCenterPosX, _headCenterPosY, _headCenterPosZ){
	if(_sortPos == 0){
        if(isDrawn){
            sketchBlobs.glcolor(color_head);
            sketchBlobs.moveto(_headTopPosX, _headTopPosY, _headTopPosZ);
            sketchBlobs.sphere(0.02);
        }
		animNode_head.position = new Array(_headTopPosX, _headTopPosY, _headTopPosZ);
        updateHead = true;
	}
}

function trackHeadBlob(_serverID, _frameNo, _blobID, _sortPos, _age, _headBlobPosX, _headBlobPosY, _headBlobPosZ, _headBlobWidth, _headBlobDepth){
    if(isDrawn){
        if(_sortPos == 0)
            sketchBlobs.glcolor(color_head);
        else
            sketchBlobs.glcolor(new Array(255, 255, 255, 255));

        sketchBlobs.framequad(
            _headBlobPosX - _headBlobWidth / 2., _headBlobPosY - _headBlobDepth / 2., _headBlobPosZ,
            _headBlobPosX - _headBlobWidth / 2., _headBlobPosY + _headBlobDepth / 2., _headBlobPosZ,
            _headBlobPosX + _headBlobWidth / 2., _headBlobPosY + _headBlobDepth / 2., _headBlobPosZ,
            _headBlobPosX + _headBlobWidth / 2., _headBlobPosY - _headBlobDepth / 2., _headBlobPosZ);
    }
}

function trackBodyBlob(_serverID, _frameNo, _blobID, _sortPos, _age, _bodyBlobPosX, _bodyBlobPosY, _bodyBlobWidth, _bodyBlobDepth, _bodyHeight){
    if(isDrawn){
        if(_sortPos == 0)
            sketchBlobs.glcolor(color_body);
        else
            sketchBlobs.glcolor(new Array(255, 255, 255, 255));

        sketchBlobs.framequad(
            _bodyBlobPosX - _bodyBlobWidth / 2., _bodyBlobPosY - _bodyBlobDepth / 2., _bodyHeight,
            _bodyBlobPosX - _bodyBlobWidth / 2., _bodyBlobPosY + _bodyBlobDepth / 2., _bodyHeight,
            _bodyBlobPosX + _bodyBlobWidth / 2., _bodyBlobPosY + _bodyBlobDepth / 2., _bodyHeight,
            _bodyBlobPosX + _bodyBlobWidth / 2., _bodyBlobPosY - _bodyBlobDepth / 2., _bodyHeight);
        sketchBlobs.framequad(
            _bodyBlobPosX - _bodyBlobWidth / 2., _bodyBlobPosY - _bodyBlobDepth / 2., 0,
            _bodyBlobPosX - _bodyBlobWidth / 2., _bodyBlobPosY + _bodyBlobDepth / 2., 0,
            _bodyBlobPosX + _bodyBlobWidth / 2., _bodyBlobPosY + _bodyBlobDepth / 2., 0,
            _bodyBlobPosX + _bodyBlobWidth / 2., _bodyBlobPosY - _bodyBlobDepth / 2., 0);
        sketchBlobs.linesegment(
            _bodyBlobPosX - _bodyBlobWidth / 2., _bodyBlobPosY - _bodyBlobDepth / 2., _bodyHeight,
            _bodyBlobPosX - _bodyBlobWidth / 2., _bodyBlobPosY - _bodyBlobDepth / 2., 0);
        sketchBlobs.linesegment(
            _bodyBlobPosX - _bodyBlobWidth / 2., _bodyBlobPosY + _bodyBlobDepth / 2., _bodyHeight,
            _bodyBlobPosX - _bodyBlobWidth / 2., _bodyBlobPosY + _bodyBlobDepth / 2., 0);
        sketchBlobs.linesegment(
            _bodyBlobPosX + _bodyBlobWidth / 2., _bodyBlobPosY + _bodyBlobDepth / 2., _bodyHeight,
            _bodyBlobPosX + _bodyBlobWidth / 2., _bodyBlobPosY + _bodyBlobDepth / 2., 0);
        sketchBlobs.linesegment(
            _bodyBlobPosX + _bodyBlobWidth / 2., _bodyBlobPosY - _bodyBlobDepth / 2., _bodyHeight,
            _bodyBlobPosX + _bodyBlobWidth / 2., _bodyBlobPosY - _bodyBlobDepth / 2., 0);
    }
}

function refreshprops(){
	outlet(OUT_INFO, "bang");
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
