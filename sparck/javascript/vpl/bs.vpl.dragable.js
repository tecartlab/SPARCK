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

/*

Javascript Node - Workhorse for VPL (Visual Programming Language)

*/

var myIOLetButtonSize = 10;
var myIOLetButtonShift = 2;

autowatch = 1;

var undefined = "undefined";

var myNodeBoxSize;
var myNodeName = undefined;
var myNodeEnable = 0;

// patcher arguments
if (jsarguments.length > 1){
    myNodeName = jsarguments[1];
	myNodeType = myNodeName.substring(0, myNodeName.indexOf("."));
}

var vpl_nodeBox;
var vpl_NodeSpacePatcher;
var vpl_clientpatcher;

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 2;

//make it dragable even without init()

function init(){
	initNodeSpace();
	initNodeBox();

	return true;
}

function initNodeSpace(){
	var owner = this.patcher.box;
	while (owner) {
		vpl_nodeBox = owner;
		var NodeSpaceThisPatcher = vpl_nodeBox.patcher.getnamed("vpl_ThisNodeSpacePatcher");
		if(NodeSpaceThisPatcher != null){
			vpl_NodeSpacePatcher = vpl_nodeBox.patcher;
			break;
		} else {
			owner = owner.patcher.box;
		}
	}
}

function initNodeBox(){
	//Find  all the important objects in this patcher
	// and initialize them.
	var vpl_clientpatcher = this.patcher.box.patcher;

	// tries to find a canvas object with the name "vplcanvas" and
	// calculates this vpl_nodeBox size
	var vpl_clientCanvas = vpl_clientpatcher.getnamed("vpl_canvas");
	if(vpl_clientCanvas != 0){
 		//post("canvas found 2: " + a.rect + "\n");
		myNodeBoxSize = vpl_clientCanvas.rect;
		//post("node size  " + myNodeBoxSize[2] + ", " + myNodeBoxSize[3] + "\n");
		if(vpl_nodeBox != null && vpl_nodeBox.varname == myNodeName){
 			var myBoxRect = vpl_nodeBox.rect;
			myBoxRect[2] = myBoxRect[0] + myNodeBoxSize[2];
			myBoxRect[3] = myBoxRect[1] +  myNodeBoxSize[3] + myIOLetButtonSize / 2;
			vpl_nodeBox.rect = myBoxRect;
		}
	}
}

/**********************
  Call Functions
 **********************/

//Called by a subpacth inside the node-patch
function drag(diffX, diffY){
	if(vpl_nodeBox != null){
		var myBoxRect = vpl_nodeBox.rect;
		myBoxRect[0] += diffX;
		myBoxRect[2] += diffX;
		myBoxRect[1] += diffY;
		myBoxRect[3] += diffY;
		vpl_nodeBox.rect = myBoxRect;
		vpl_NodeSpacePatcher.message("script", "bringtofront", vpl_nodeBox.varname);
	}
}

//Called when the parent node was deleted
function notifydeleted(){
}
