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
outlets = 3;

include("_three.js");
include("_gui.js");
include("_draw.js");

var OUTLET_LCD = 0;
var OUTLET_BLUR = 1;
var OUTLET_WINDOW = 2;

var EDITMODE_LINE = 0;
var EDITMODE_POLY = 1;
var EDITMODE_COLOR = 2;

var pickRay = new THREE.Ray(new THREE.Vector3(0, 0, 0), new THREE.Vector3(0, 0, 1));
var uiEvent = new GUI.UIEvent();
var editPlane = new THREE.Plane(new THREE.Vector3(0, 0, 1), 0);
var help = new DRAW.EditorHelper();
var editMode = EDITMODE_LINE;
var editor_enable = 0;
var cameraMatrix = new THREE.Matrix4();

var lcd_Dim = [320, 240];
var lcd_aspectRatio = lcd_Dim[0] / lcd_Dim[1];
var lcd_file;

// create our lattice object
var cameraAnimNode;
var cameraObj;
var nodeObj;
var meshObj;

// global Function Key
var functionKey;
var lastMouse;

var mouseHitCounter = 0;
var MessageArray;
var lineThickness = 1;

var BLACK_NAME = "black";
var WHITE_NAME = "white";
var BLACK_COLOR = [0, 0, 0];
var WHITE_COLOR = [255, 255, 255];

var bgColor = BLACK_COLOR;
var fgColor = WHITE_COLOR;

var bgName = BLACK_NAME;
var fgName = WHITE_NAME;

//post("bs.draw.editor.js newLoad\n");

function loadbang(){
	nodeObj = new JitterObject("jit.gl.node","editor");
    nodeObj.enable = editor_enable;
	cameraAnimNode = new JitterObject("jit.anim.node");
	cameraAnimNode.position = [0, 0, 3.0];
	cameraAnimNode.fsaa = 1;
	cameraObj = new JitterObject("jit.gl.camera");
	cameraObj.drawto = nodeObj.name;
	cameraObj.anim = cameraAnimNode.name;
    cameraObj.enable = editor_enable;
	meshObj = new JitterObject("jit.gl.videoplane");
	meshObj.drawto = nodeObj.name;
	meshObj.layer = 10;
    meshObj.enable = editor_enable;
    meshObj.scale = [lcd_aspectRatio, 1., 1.];
    help.drawto(nodeObj.name);
    help.enable(editor_enable);
    help.printLINE(lineThickness, fgName);
	calcCameraMatrix();
    outlet(OUTLET_LCD, "brgb", bgColor);
    outlet(OUTLET_LCD, "frgb", fgColor);
    redraw();
  }

function enable(_enable){
	nodeObj.enable = _enable;
	cameraObj.enable = _enable;
	meshObj.enable = _enable;
    lcdObj.enable = _enable;
}

function update(){
	// This method needs to be called at the beginning
	if(uiEvent.update_start()){
//       post("uiEvent.update_start() \n");
        if(uiEvent.mouseInWindows && uiEvent.mouseButtonPress == false && uiEvent.keyRelease == true){
            if(uiEvent.keyChar == 'tab'){
                if(editMode == EDITMODE_LINE){
                    editMode = EDITMODE_POLY;
                    help.printPOLY(fgName);
                    mouseHitCounter = 0;
                    MessageArray = ["paintpoly"];
                }else if(editMode == EDITMODE_POLY){
                    editMode = EDITMODE_COLOR;
                    help.printCOLOR(bgName, fgName);
                }else if(editMode == EDITMODE_COLOR){
                    editMode = EDITMODE_LINE;
                    help.printLINE(lineThickness, fgName);
                }
            } else if(uiEvent.keyChar == 'h'){
                help.toggleEnable();
            } else if(uiEvent.keyChar == 's'){
                lcd_write();
            } else if(uiEvent.keyChar == 'space'){
                toggleColors();
                if(editMode == EDITMODE_COLOR)
                    help.printCOLOR(bgName, fgName);
                if(editMode == EDITMODE_LINE)
                    help.printLINE(lineThickness, fgName);
                if(editMode == EDITMODE_POLY)
                    help.printPOLY(fgName);
                outlet(OUTLET_LCD, "frgb", fgColor);
                outlet(OUTLET_LCD, "brgb", bgColor);
				//also make sure in poly mode the poly is finished
                mouseHitCounter = 0;
                MessageArray = ["paintpoly"];
                redraw();
            }
        }

        if(editMode == EDITMODE_LINE){
            if(!isNavigationEvent()){
//                 post("uiEvent.mouseButtonHit = " + uiEvent.mouseButtonHit +" \n");
               if(uiEvent.mouseButtonHit == true){
                    if(uiEvent.mousePlusKey == uiEvent.keyUndef){
                        outlet(OUTLET_LCD, "moveto", getLcdPixelPos());
                        redraw();
                    }
                }
                if(uiEvent.mouseButtonPress == true && uiEvent.mousePlusKey == uiEvent.keyUndef){
//                    post("uiEvent.mousePlusKey " + uiEvent.mousePlusKey + " == " + uiEvent.keyUndef +" \n");
                    outlet(OUTLET_LCD, "lineto", getLcdPixelPos());
                    redraw();
                } else if(uiEvent.keyHit){
                    if(uiEvent.keyChar == 'up'){
                        lineThickness ++;
                        outlet(OUTLET_LCD, "pensize", lineThickness, lineThickness);
                        redraw();
                        help.printLINE(lineThickness, fgName);
                    } else if( uiEvent.keyChar == 'down'){
                        if(lineThickness > 1)
                            lineThickness--;
                        outlet(OUTLET_LCD, "pensize", lineThickness, lineThickness);
                        redraw();
                        help.printLINE(lineThickness, fgName);
                    }
                }
            }
        }

        if(editMode == EDITMODE_POLY){
            if(!isNavigationEvent()){
  //              post("uiEvent.mouseButtonHit = " + uiEvent.mouseButtonHit +" \n");
           	    if(uiEvent.mouseButtonHit){
  //                  post("mouseHitCounter = inside = " + mouseHitCounter +" \n");
               	    var pixelPos = getLcdPixelPos();
               	    MessageArray.push(pixelPos[0]);
                    MessageArray.push(pixelPos[1]);
                    if(mouseHitCounter == 0){
                        outlet(OUTLET_LCD, "pensize", 1, 1);
                        outlet(OUTLET_LCD, "moveto", pixelPos);
                    } else if(mouseHitCounter < 3){
                        outlet(OUTLET_LCD, "lineto", pixelPos);
                    } else {
                        outlet(OUTLET_LCD, MessageArray);
                    }
                    mouseHitCounter = mouseHitCounter + 1;
                    redraw();
                } else if(uiEvent.keyHit){
                    if(uiEvent.keyChar == 'return'){
                        mouseHitCounter = 0;
                        MessageArray = ["paintpoly"];
                    }
                }
            }
        }

        if(editMode == EDITMODE_COLOR){
            if(isNavigationEvent()){
                ; // ignore the other else ifs
            } else if(uiEvent.keyHit) {
                if(uiEvent.keyChar == 'c'){
                    outlet(OUTLET_LCD, "clear");
                    redraw();
               }
            }
        }
    }
	// This method needs to be called at the end
	uiEvent.update_end();
}

function toggleColors(){
    if(bgName == BLACK_NAME){
        bgColor = WHITE_COLOR;
        fgColor = BLACK_COLOR;
        bgName = WHITE_NAME;
        fgName = BLACK_NAME;
    } else {
        fgColor = WHITE_COLOR;
        bgColor = BLACK_COLOR;
        fgName = WHITE_NAME;
        bgName = BLACK_NAME;
    }
}

function isNavigationEvent(){
    if(uiEvent.special_CtrlKey){
        var diff = (uiEvent.lastDragPosY - uiEvent.currentPosY) / 10.;
        if(diff != 0){
//				post("in zoom mode " + diff + " + " + cameraObj.position[2] + " \n");
            if((cameraAnimNode.position[2] + diff) > 1.0 && (cameraAnimNode.position[2] + diff) < 7.){
                cameraAnimNode.position = [
                    cameraAnimNode.position[0],
                    cameraAnimNode.position[1],
                    cameraAnimNode.position[2] + diff / 2.0];
                calcCameraMatrix();
            }
        }
        return true;
    } else if(uiEvent.special_ShiftKey){
        var diffY = (uiEvent.lastDragPosY - uiEvent.currentPosY) / 10.;
        var diffX = (uiEvent.lastDragPosX - uiEvent.currentPosX) / 10.;
        if(diffY != 0 || diffX != 0){
//				post("in zoom mode " + diff + " + " + cameraObj.position[2] + " \n");
            if((cameraAnimNode.position[1] - diffY / 10.0) > -2.0 && (cameraAnimNode.position[1] - diffY / 10.0) < 2.){
                cameraAnimNode.position = [
                    cameraAnimNode.position[0],
                    cameraAnimNode.position[1] - diffY / 10.0,
                    cameraAnimNode.position[2]];
//                            post("cameraAnimNode.position[1] " + cameraAnimNode.position[1] + " diffY " + diffY +" \n");
                calcCameraMatrix();
            }
            if((cameraAnimNode.position[0] + diffX / 10.0) > -2.0 && (cameraAnimNode.position[0] + diffX / 10.0) < 2.){
              cameraAnimNode.position = [
                    cameraAnimNode.position[0] + diffX / 10.0,
                    cameraAnimNode.position[1],
                    cameraAnimNode.position[2]];
                calcCameraMatrix();
            }
        }
        return true;
    }
    return false;
}

function getLcdPixelPos(){
    var lcdWorldPos = uiEvent.getPickRay().intersectPlane(editPlane);
    var posx = (1 + lcdWorldPos.x / lcd_aspectRatio) * lcd_Dim[0] / 2.0;
    var posy = (1 - lcdWorldPos.y ) * lcd_Dim[1] / 2.0;
    return [posx, posy];
}

function lcd_dim(){
    lcd_Dim = arrayfromargs(arguments);
    outlet(OUTLET_LCD, "dim", lcd_Dim[0], lcd_Dim[1]);
    if(lcd_file != null){
        lcd_readpict(lcd_file)
    } else {
        redraw();
    }
    if(meshObj != null){
        lcd_aspectRatio = lcd_Dim[0] / lcd_Dim[1];
        meshObj.scale = [lcd_aspectRatio, 1., 1.];
    }
}

function lcd_write(){
    outlet(OUTLET_LCD, "writepict", lcd_file);
    redraw();
}

function lcd_writepict(_warpPath){
    lcd_file = _warpPath;
    outlet(OUTLET_LCD, "writepict", lcd_file);
    redraw();
}

function lcd_readpict(_warpPath){
    lcd_file = _warpPath;
    outlet(OUTLET_LCD, "readpict", "pic", lcd_file);
    outlet(OUTLET_LCD, "drawpict", "pic", 0, 0, lcd_Dim[0], lcd_Dim[1]);
    redraw();
}

function lcd_clear(){
    cameraAnimNode.position = [0, 0, 3.0];
    outlet(OUTLET_LCD, "clear");
    redraw();
}

function blur_strength(_strength){
    outlet(OUTLET_BLUR, _strength);
    redraw();
}

function redraw(){
    outlet(OUTLET_LCD, "bang");
}

function drawLattice(){
}

function drawMesh( ){
}

function texture(){
    if(meshObj != null)
        meshObj.texture = arrayfromargs(arguments);
}

function editor(_enable){
	var args = arrayfromargs(arguments);
	if(args[0] == 'enable'){
        editor_enable = args[1];
        if(nodeObj != null){
            nodeObj.enable = args[1];
            cameraObj.enable = args[1];
            meshObj.enable = args[1];
       }
    }
}

/*
	Setting uiEvent data for windows interaction
*/
function ui_keys(){
	uiEvent.key(arguments[0], arguments[1]);
}

/*
	Setting uiEvent data for windows interaction
*/
function ui_window(){
	var args = arrayfromargs(arguments);
	if(args[0] == 'mouse'){
		args.splice(0, 1);
		uiEvent.mouse(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7], 0.);
	}else if(args[0] == 'mouseidle'){
		args.splice(0, 1);
		uiEvent.mouse(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7], 0.);
	}else if(args[0] == 'mouseidleout'){
        outlet(OUTLET_WINDOW, "getsize");
	}else if(args[0] == 'mousewheel'){
		args.splice(0, 1);
		uiEvent.mouse(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8]);
	}else if(args[0] == 'pickray'){
		args.splice(0, 1);
        if(cameraObj.enable == 1){
            args = cameraObj.getviewportray(uiEvent.currentPosX, uiEvent.currentPosY);
            uiEvent.pickray(args[0],args[1],args[2],args[3],args[4],args[5]);
        }
	}else if(args[0] == 'size'){
		args.splice(0, 1);
		uiEvent.windowSize(args[0],args[1]);
	}
}

function calcCameraMatrix(){
	var camMat = cameraAnimNode.worldtransform;
	cameraMatrix.set(
		camMat[0], camMat[4], camMat[8], camMat[12],
		camMat[1], camMat[5], camMat[9], camMat[13],
		camMat[2], camMat[6], camMat[10], camMat[14],
		camMat[3], camMat[7], camMat[11], camMat[15]);
}

function notifydeleted(){
    if(nodeObj != null){
        cameraAnimNode.freepeer();
        cameraObj.freepeer();
        nodeObj.freepeer();
        meshObj.freepeer();
        help.freepeer();
    }
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
