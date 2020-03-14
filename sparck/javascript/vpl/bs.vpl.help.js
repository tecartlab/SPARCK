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

var COL_INSET = 80;
var COL_WIDTH = 100;
var COL_GAP = 10;
var ROW_INSET = 100;
var ROW_GAP = 17;

var LEVEL_OFF = -1;
var LEVEL_UNREG = 0;
var LEVEL_FREE = 1;
var LEVEL_BASIC = 3;
var LEVEL_MASTER = 5;
var LEVEL_PRO = 7;
var LEVEL_DEV = 9;

var isPopulated = false;

var	nodeAccess;
var accessLevel = LEVEL_OFF;

var LICENSE_LEVEL = ["Unreg", "Free", "nan", "Basic", "nan", "Master", "nan", "Pro", "nan", "Dev"];
var LICENSE_LEVEL_SHOW = [false, false, false, true, true, true, true, true, true, false];

var LICENSE_LEVEL_COL = [-1, 0, -1, 1, -1, 2, -1, 3, -1, -1];

var EmptyString = "                    ";

function loadbang(){
    nodeAccess = new Dict("bs::app::nodes");

    var firstObject = this.patcher.firstobject;
    var thisObject = firstObject;

    while(thisObject != null){
        nextObject = thisObject.nextobject;
        if(thisObject.varname.indexOf("_lic_") == 0){
            this.patcher.remove(thisObject);
        }
        thisObject = nextObject;
    }
    //post("was here \n");
}

function populate(){
    menuCounter = ROW_INSET;
    pupOutput();
    pup2Dee();
    pup3Dee();
    pupTransforming();
    pupMapping();
    pupEffects();
    pupScript();
    pupLED();
    pupVolume();
    //pupMisc();
}

var menuCounter = 0;
var menuStart = 0

function menu_Start(_menu){
    menuStart = menuCounter
    menuCounter = menuCounter + 2;
}

function menu_End(_menu){
    menuCounter = menuCounter + 1;
    this.patcher.message(createPanelMsg(_menu, 5, menuStart, 517, menuCounter - menuStart));
    this.patcher.message(createTitleMsg(_menu, 5 + 5, menuStart + 5, 65, 20));
    this.patcher.message("script", "sendbox", "_lic_" + _menu + "_title", "set", _menu);
    menuCounter = menuCounter + 1;
}

function createPanelMsg(_name, _inset, _row, _width, _height){
	var msp = new Array();
	msp.push("script");
	msp.push("newobject");
	msp.push("panel");
	msp.push("@patching_rect");
	msp.push(_inset);
	msp.push(_row);
	msp.push(_width);
	msp.push(_height);
	msp.push("@presentation_rect");
	msp.push(_inset);
	msp.push(_row);
	msp.push(_width);
	msp.push(_height);
	msp.push("@presentation");
	msp.push(1);
	msp.push("@background");
	msp.push(0);
	msp.push("@border");
	msp.push(1);
	msp.push("@bordercolor");
	msp.push(0);
	msp.push(0);
	msp.push(0);
	msp.push(1);
	msp.push("@bgfillcolor");
	msp.push(0.5);
	msp.push(0.5);
	msp.push(0.5);
	msp.push(0.3);
	msp.push("@varname");
	msp.push("_lic_" + _name + "_panel");
    return msp;
}

function createTitleMsg(_name, _inset, _row, _width, _height){
	var msp = new Array();
	msp.push("script");
	msp.push("newobject");
	msp.push("comment");
	msp.push("@patching_rect");
	msp.push(_inset);
	msp.push(_row);
	msp.push(_width);
	msp.push(_height);
	msp.push("@presentation_rect");
	msp.push(_inset);
	msp.push(_row);
	msp.push(_width);
	msp.push(_height);
	msp.push("@presentation");
	msp.push(1);
	msp.push("@background");
	msp.push(0);
	msp.push("@varname");
	msp.push("_lic_" + _name + "_title");
    return msp;
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
        } else if( _levels[i] == LEVEL_UNREG){
            return "";
        } else if( _levels[i] == LEVEL_FREE){
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
    var levArray = "0";
    if(levels.indexOf("|") == -1){
        levArray = new Array(levels);
    } else {
        levArray = levels.split("|");
    }
    var displayName = getLevelsText(levArray);
    var displayMaxNodes = (maxNodes == -1)? "": " (max: " + maxNodes + ")";

    for(var i = 0; i < levArray.length; i++){
        var licenseLevel = parseInt(levArray[i])
        if(LICENSE_LEVEL_COL[licenseLevel] != -1){
            var row = menuCounter;
            var col = COL_INSET + (COL_WIDTH + COL_GAP) * LICENSE_LEVEL_COL[licenseLevel];
            var msg = createMessage(_item, row, col, licenseLevel);
            this.patcher.message(msg);
        }
    }

    menuCounter = menuCounter + ROW_GAP;
}

function createMessage(_nodeName, _row, _col, _level){
	var msp = new Array();
	msp.push("script");
	msp.push("newobject");
	msp.push("bpatcher");
	msp.push("bs.help.util.license.nodelink.maxpat");
	msp.push("@patching_rect");
	msp.push(_col);
	msp.push(_row);
	msp.push(100);
	msp.push(15);
	msp.push("@presentation_rect");
	msp.push(_col);
	msp.push(_row);
	msp.push(100);
	msp.push(15);
	msp.push("@args");
	msp.push(_nodeName);
	msp.push("@presentation");
	msp.push(1);
	msp.push("@varname");
	msp.push("_lic_" + _nodeName + "_" + _level);
    return msp;
}

function menu______(_menu){
	outlet(1, _menu, "append", "<separator>");
    menuCounter++;
}

function pup3Dee(){
    var _menu = "3Dee";
    menu_Start(_menu);
    menuAppend(_menu, "Grid");
    menuAppend(_menu, "Model");
    menuAppend(_menu, "Canvas");
    menuAppend(_menu, "Shape");
    menuAppend(_menu, "Light");
    menuAppend(_menu, "SceneCamera");
    menuAppend(_menu, "SceneCapture");
    menuAppend(_menu, "LookAtCamera");
    menuAppend(_menu, "BoxMapCamera");
    menuAppend(_menu, "BoxMapCapture");
    menu_End(_menu);
}

function pupMapping(){
    var _menu = "Mapping";
    menu_Start(_menu);
    menuAppend(_menu, "CornerPin");
    menuAppend(_menu, "MeshWarp");
    menuAppend(_menu, "Beamer");
    menuAppend(_menu, "Calibrator");
    menu_End(_menu);
}

function pupTransforming(){
    var _menu = "Transform";
    menu_Start(_menu);
    menuAppend(_menu, "TfmNode");
    menuAppend(_menu, "TfmNodeInfo");
    menuAppend(_menu, "TfmNodePath");
    menuAppend(_menu, "TfmNodeMerge");
    menuAppend(_menu, "TfmLookAt");
    menuAppend(_menu, "TfmMirror");
    menuAppend(_menu, "TurnTable");
    menuAppend(_menu, "HeadRoom");
    menuAppend(_menu, "Optitrack");
    menu_End(_menu);
}

function pupLED(){
    var _menu = "Led";
    menu_Start(_menu);
    menuAppend(_menu, "LedStrip");
    menuAppend(_menu, "LedStripOne");
    menuAppend(_menu, "PixelPusher");
    menu_End(_menu);
}

function pup2Dee(){
    var _menu = "2Dee";
    menu_Start(_menu);
    menuAppend(_menu, "Video");
    menuAppend(_menu, "Texture");
    menuAppend(_menu, "SyphonReceive");
    menuAppend(_menu, "SpoutReceive");
    menuAppend(_menu, "SyphonSend");
    menuAppend(_menu, "SpoutSend");
    menuAppend(_menu, "TextureMask");
    menu_End(_menu);
}

function pupOutput(){
    var _menu = "Output";
    menu_Start(_menu);
    menu_Start(_menu);
    menuAppend(_menu, "Window");
    menuAppend(_menu, "ViewPort");
    menuAppend(_menu, "Monitor");
    menuAppend(_menu, "OscSend");
    menuAppend(_menu, "OculusRift");
    menuAppend(_menu, "HtcVive");
    menuAppend(_menu, "OscMessage");
    menu_End(_menu);
}

function pupScript(){
    var _menu = "Script";
    menu_Start(_menu);
    menuAppend(_menu, "QueScript");
    menuAppend(_menu, "Patcher");
    menu_End(_menu);
}

function pupEffects(){
    var _menu = "Effects";
    menu_Start(_menu);
    menuAppend(_menu, "TextureOP");
    menuAppend(_menu, "TextureBlur");
    menuAppend(_menu, "TextureColorMap");
    menuAppend(_menu, "TextureZoom");
    menuAppend(_menu, "TextureBrCoSa");
    menuAppend(_menu, "TextureAnaglyph");
    menuAppend(_menu, "TextureProjection");
    menuAppend(_menu, "RayMarcher");
    menuAppend(_menu, "Material");
    menuAppend(_menu, "TextureStitcher");
    menuAppend(_menu, "BoxMapProjection");
    menuAppend(_menu, "BlendSoftedge");
    menuAppend(_menu, "SpatialSoftedge");
    menuAppend(_menu, "ShaderSelection");
    menuAppend(_menu, "SSAO");
    menu_End(_menu);
}

function pupMisc(){
    var _menu = "Misc";
    menu_Start(_menu);
    menuAppend(_menu, "Physics");
    menu_End(_menu);
}

function pupVolume(){
    var _menu = "Volume";
    menu_Start(_menu);
    menuAppend(_menu, "Volume");
    menuAppend(_menu, "VolumeShader");
    menuAppend(_menu, "VolumeLoader");
    menu_End(_menu);
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
