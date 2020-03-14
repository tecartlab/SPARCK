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
setinletassist(0,"selection");
setoutletassist(0,"selection message");
setoutletassist(1,"populate menues");

var blockItems = true;
// patcher arguments
if (jsarguments.length > 1){
    blockItems = (jsarguments[1] == "false")?false:true;
}

var LEVEL_DEV = 9;

var isPopulated = false;

var	nodeAccess;
var accessLevel = LEVEL_DEV;

var LICENSE_LEVEL = ["Unreg", "Free", "nan", "Basic", "nan", "Master", "nan", "Pro", "nan", "Dev"];
var LICENSE_LEVEL_SHOW = [false, false, false, true, true, true, true, true, true, false];

var EmptyString = "               ";

var appGlobal = new Global("bs::app::global");
// set the debugger : 0=off, 1=all, 2=app, 3=io, 4=db, 5=node, 6=select
var debug = (appGlobal.debug == 1 || appGlobal.debug == 6)? true:false;

// set the debugger
function debugger(_val){
	debug = (_val == 1 || _val == 6)? true: false;
}

function dpost(_message){
    if(debug){
        post("bs.vpl.select: " + _message + " \n");
    }
}

function loadbang(){
    nodeAccess = new Dict("bs::app::nodes");
    //post("was here \n");
}

function anything()
{
	if (inlet==0){
        var msgName = messagename.split(" ")[0];
        if(nodeAccess.contains(msgName)){
            //post("try to create " + messagename + " node \n");
            outlet(0, "create", msgName);
        } else if (msgName.indexOf("Nodes..") != 0){
             error("ERROR: Node creation Error: An unknown node was attempted to be created!");
        }
	}

}

function populate(){
	var aLevel = nodeAccess.get("_level");

    pupUils();
    pup3DSpace();
    pupMapping();
    pupTransforming();
    pupInput();
    pupOutput();
    pupEffects();
    pupMisc();
    pupExperimental();
}

var menuCounter = 0;

function menu_Start(_menu, _title){
	outlet(1, _menu, "clear");
	outlet(1, _menu, "append", _title);
    menuCounter = 1;
}

function checkLevel(_level) {
    return _level == accessLevel;
}

function getLevelsText(_levels){
    var result = null;
    var indx = 0;
    for (var i = 0 ; i < _levels.length ; i++ ){
        if(LICENSE_LEVEL_SHOW[_levels[i]]){
            // we dont want to see the developer text.
            if(indx == 0){
                result = "[" + LICENSE_LEVEL[_levels[i]];
            } else {
                result = result + ","+LICENSE_LEVEL[_levels[i]];
                indx++;
            }
            indx++;
        } else {
            result = "[" + LICENSE_LEVEL[_levels[i]];
            break;
        }
    }
    if(result == null){
        // if no level is inside the brackets (i.e. its a node only for developer)
        return "[" + LICENSE_LEVEL[LEVEL_DEV]+ "]";
    } else {
        result = result + "]";
    }
    return result;
}

function menuAppend(_menu, _item){
	var maxNodes = (nodeAccess.get(_item + "::nodeCreation::_maxNodes"));
	var levels = (nodeAccess.get(_item + "::nodeCreation::_level"));
    if(levels != null){
        var levArray = levels.split("|");
        var isMatch = levArray.some(checkLevel);
        var displayName = getLevelsText(levArray);
        var displayMaxNodes = (maxNodes == -1)? "": " (max: " + maxNodes + ")";
        //post("got the level :" +levels+" for " + _item + " displayName:" + displayName + " isMatch:" + isMatch + "\n");
	   //outlet(1, _menu, "append", _item + EmptyString.substr(0,EmptyString.length - _item.length) + displayName + displayMaxNodes);
        outlet(1, _menu, "append", _item + EmptyString.substr(0,EmptyString.length - _item.length) + displayMaxNodes);
        if(blockItems){
            outlet(1, _menu, "enableitem", menuCounter, (isMatch > 0)?1:0);
        }
        menuCounter++;
    }
}

function menu______(_menu){
	outlet(1, _menu, "append", "<separator>");
    menuCounter++;
}

function pupUils(){
    var _menu = "menu1";
    menu_Start(_menu, "Nodes...");
    menuAppend(_menu, "Window");
    menuAppend(_menu, "ViewPort");
    menuAppend(_menu, "Monitor");
    menu______(_menu);
    menuAppend(_menu, "subnode");
    menuAppend(_menu, "sub-inlet");
    menuAppend(_menu, "sub-outlet");
}

function pup3DSpace(){
    var _menu = "menu2";
    menu_Start(_menu, "Nodes...");
    menuAppend(_menu, "Model");
    menuAppend(_menu, "Canvas");
    menuAppend(_menu, "Shape");
    menuAppend(_menu, "Light");
    menuAppend(_menu, "SkyBox");
    menu______(_menu);
    menuAppend(_menu, "Grid");
    menuAppend(_menu, "MeshWarp");
  	menu______(_menu);
    menuAppend(_menu, "Projector");
  	menu______(_menu);
    menuAppend(_menu, "Beamer");
    menuAppend(_menu, "Calibrator");
  	menu______(_menu);
    menuAppend(_menu, "LookAtCamera");
    menuAppend(_menu, "SceneCamera");
    menuAppend(_menu, "SceneCapture");
  	menu______(_menu);
    menuAppend(_menu, "BoxMapCamera");
    menuAppend(_menu, "BoxMapCapture");
  	menu______(_menu);
    menuAppend(_menu, "LedStrip");
    menuAppend(_menu, "LedStripOne");
    menuAppend(_menu, "PixelPusher");
  	menu______(_menu);
    menuAppend(_menu, "HeadRoom");
    menuAppend(_menu, "HeadRoom.RS");
}

function pupMapping(){
    var _menu = "menu3";
    menu_Start(_menu, "Nodes...");
    menuAppend(_menu, "CornerPin");
    menuAppend(_menu, "MeshWarp");
    menu______(_menu);
    menuAppend(_menu, "Projector");
  	menu______(_menu);
    menuAppend(_menu, "Beamer");
    menuAppend(_menu, "Calibrator");
    menu______(_menu);
    menuAppend(_menu, "BoxMapCamera");
    menuAppend(_menu, "BoxMapCapture");
    menu______(_menu);
    menuAppend(_menu, "BoxMapProjection");
    menuAppend(_menu, "TextureProjection");
    menu______(_menu);
    menuAppend(_menu, "SpatialSoftedge");
    menuAppend(_menu, "BlendSoftedge");
	if(isOSX()){
        menuAppend(_menu, "SyphonReceive");
	}
	if(isWin()){
        menuAppend(_menu, "SpoutReceive");
	}
}

function pupTransforming(){
    var _menu = "menu4";
    menu_Start(_menu, "Nodes...");
    menuAppend(_menu, "TfmNode");
    menuAppend(_menu, "TfmNodeInfo");
    menuAppend(_menu, "TfmNodeMerge");
    menuAppend(_menu, "TfmNodePath");
    menu______(_menu);
    menuAppend(_menu, "TfmLookAt");
    menuAppend(_menu, "TfmMirror");
    menu______(_menu);
    menuAppend(_menu, "TurnTable");
    menuAppend(_menu, "HeadRoom");
    menuAppend(_menu, "HeadRoom.RS");
    menuAppend(_menu, "Optitrack");
	if(isWin()){
        menu______(_menu);
        menuAppend(_menu, "VrHMD");
        menuAppend(_menu, "OculusRift");
        menuAppend(_menu, "HtcVive");
    }
}


function pupInput(){
    var _menu = "menu5";
    menu_Start(_menu, "Nodes...");
    menuAppend(_menu, "Video");
    menuAppend(_menu, "Texture");
    menuAppend(_menu, "CubeMap");
    if(isOSX()){
        menuAppend(_menu, "SyphonReceive");
	}
	if(isWin()){
        menuAppend(_menu, "SpoutReceive");
	}
    menu______(_menu);
    menuAppend(_menu, "QueScript");
    menu______(_menu);
    menuAppend(_menu, "LedStrip");
    menuAppend(_menu, "LedStripOne");
    menu______(_menu);
    menuAppend(_menu, "MeshWarp");
    menuAppend(_menu, "TextureMask");
    menu______(_menu);
    menuAppend(_menu, "TurnTable");
    menuAppend(_menu, "HeadRoom");
    menuAppend(_menu, "HeadRoom.RS");
    menuAppend(_menu, "Optitrack");
    menu______(_menu);
    menuAppend(_menu, "Material");
}

function pupOutput(){
    var _menu = "menu6";
    menu_Start(_menu, "Nodes...");
    menuAppend(_menu, "Window");
    menuAppend(_menu, "ViewPort");
	if(isOSX()){
        menuAppend(_menu, "SyphonSend");
	}
	if(isWin()){
        menuAppend(_menu, "SpoutSend");
        menuAppend(_menu, "VrHMD");
        menuAppend(_menu, "OculusRift");
        menuAppend(_menu, "HtcVive");
	}
    menu______(_menu);
    menuAppend(_menu, "QueScript");
    menu______(_menu);
    menuAppend(_menu, "OscSend");
    menuAppend(_menu, "OscMessage");
    menu______(_menu);
    menuAppend(_menu, "PixelPusher");
}

function pupEffects(){
    var _menu = "menu7";
    menu_Start(_menu, "Nodes...");
    menuAppend(_menu, "Material");
    menu______(_menu);
    menuAppend(_menu, "TextureOP");
    menuAppend(_menu, "TextureBlur");
    menuAppend(_menu, "TextureColorMap");
    menuAppend(_menu, "TextureZoom");
    menuAppend(_menu, "TextureBrCoSa");
    menuAppend(_menu, "TextureAnaglyph");
    menu______(_menu);
    menuAppend(_menu, "TextureProjection");
    menuAppend(_menu, "BoxMapProjection");
    menuAppend(_menu, "RayMarcher");
    menu______(_menu);
    menuAppend(_menu, "SpatialSoftedge");
    menuAppend(_menu, "BlendSoftedge");
    menu______(_menu);
    menuAppend(_menu, "TextureStitcher");
}

function pupMisc(){
    var _menu = "menu8";
    menu_Start(_menu, "Nodes...");
    menuAppend(_menu, "QueScript");
    menuAppend(_menu, "SSAO");
    menu______(_menu);
    menuAppend(_menu, "OscSend");
    menuAppend(_menu, "OscMessage");
    menu______(_menu);
    menuAppend(_menu, "LedStrip");
    menuAppend(_menu, "LedStripOne");
    menuAppend(_menu, "PixelPusher");
    menu______(_menu);
    menuAppend(_menu, "TurnTable");
    menuAppend(_menu, "HeadRoom");
    menuAppend(_menu, "HeadRoom.RS");
    menuAppend(_menu, "Optitrack");
}

function pupExperimental(){
    var _menu = "menu9";
    menu_Start(_menu, "Nodes...");
    menuAppend(_menu, "ShaderSelection");
    menu______(_menu);
    menuAppend(_menu, "Volume");
    menuAppend(_menu, "VolumeShader");
    menuAppend(_menu, "VolumeLoader");
    menu______(_menu);
    menuAppend(_menu, "Patcher");
    menuAppend(_menu, "Physics");
}

function isOSX(){
	return (max.os == "macintosh")? true: false;
}

function isWin(){
	return (max.os == "windows")? true: false;
}


function select(_name, _level, _max, _maxsub, _patchername, _title, _args){
	outlet(0, _name, _level, _max, _maxsub, _patchername, _title, _args);
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
