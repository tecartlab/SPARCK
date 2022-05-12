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


var OUTLET_THISPATCHER = 0;

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 1;
setinletassist(0,"input");
setoutletassist(0,"output to patcher");
setoutletassist(1,"output to ui");

var myNodeID = null;
// patcher arguments
if (jsarguments.length > 1){
    myNodeID = jsarguments[1];
}

var myNodeTitle = null;
var myBodyName = null;
var myUnfoldedSize = null;
var myFoldedSize = null;

var appGlobal = new Global("bs::app::global");

var debug = (appGlobal.debug == 1 || appGlobal.debug == 5)? true:false;

// set the debugger
function debugger(_val){
	debug = (_val == 1)? true: false;
}

function dpost(_message){
    if(debug){
        post("node.pbody->" + myNodeTitle + ": " + _message + " \n");
    }
}

function loadbang(){
}

function done(){
}

function getSize(){
    var myClientProperties = this.patcher.getnamed("vpl_properties");
    if(myClientProperties != null){
        var myproperty = myClientProperties.subpatcher();
        if(myproperty != null){
            // first we are looking for the full size of the properties
            var myPropertyCanvasUnfolded = myproperty.getnamed("vpl_canvas_unfolded");
            var myPropertyCanvasFolded = myproperty.getnamed("vpl_canvas_folded");
            if(myPropertyCanvasUnfolded != null && myPropertyCanvasFolded != null){                    
                outlet(OUTLET_THISPATCHER, "script", "sendbox", "vpl_properties", "size", myPropertyCanvasUnfolded.rect[2], myPropertyCanvasUnfolded.rect[3]);               
                
                myFoldedSize =  new Array(myPropertyCanvasFolded.rect[2], myPropertyCanvasFolded.rect[3]);
                myUnfoldedSize =  new Array(myPropertyCanvasUnfolded.rect[2], myPropertyCanvasUnfolded.rect[3]);
 
                messnamed(myNodeID + "::nodelogic", "expanded_size", myFoldedSize, myUnfoldedSize);
            } else {
                error("This shouldnt happen, pbody is missing 'vpl_canvas_unfolded' or 'vpl_canvas_folded'")
            }
        }
    }
}

// called by node.logic of this properties parent node
function address(_nodetitle, _address){
    myNodeTitle = _nodetitle;
	dpost("received address = " + _address + "\n");
    outlet(OUTLET_THISPATCHER, "script", "sendbox", "vpl_properties", "args", _nodetitle, "@address", _address);    
}

function name(_name){
    if(myBodyName == null){
        myBodyName = _name;
        outlet(OUTLET_THISPATCHER, "script", "sendbox", "vpl_properties", "name", _name);  
        getSize();
    }
}

function clear(){
    if(myBodyName != null){
        myBodyName = null;
        outlet(OUTLET_THISPATCHER, "script", "sendbox", "vpl_properties", "name", "bs.vpl.node.pbody.empty");  
        getSize();
    }
}

function enable(_enable){
}


function notifydeleted(){
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