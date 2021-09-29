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

// set up inlets/outlets/assist strings
inlets = 9;
outlets = 9;
setinletassist(0,"matrixA");
setinletassist(1,"matrixB");
setoutletassist(0,"display feedback");
setoutletassist(1,"menu display 0");
setoutletassist(2,"menu display 1");
setoutletassist(3,"menu display 2");
setoutletassist(4,"menu display 3");
setoutletassist(5,"menu display 4");
setoutletassist(6,"menu display 5");
setoutletassist(7,"menu display 6");
setoutletassist(8,"menu display 7");

var myWindow = null;
var myWidth = 0;
var myHeight = 0;

var myDispWidth = 0;
var myDispHeight = 200;
var myDispBorder = 20;

var maxDisplays = 8;
var displayCount = 1;

var coordinates = createArray(maxDisplays, 4);
var myButtons = new Array(maxDisplays);
var myMenus = new Array(maxDisplays);
var mySelection = new Array(maxDisplays);

var appGlobal = new Global("bs::app::global");

function loadbang(){
	Scan();
}

function msg_int(_val){
	if(inlet > 0){
		mySelection[inlet - 1] = _val;
		updateSelection();
	}
}

function updateSelection(){
	var min_xUpL_sel = 100000;
	var min_yUpL_sel = 100000;
	var max_xLwR_sel = -100000;
	var max_yLwR_sel = -100000;

	for(var i = 0; i < displayCount; i++){
		if(mySelection[i] == 1){
 			min_xUpL_sel = (coordinates[i][0] < min_xUpL_sel)? coordinates[i][0]: min_xUpL_sel;
			min_yUpL_sel = (coordinates[i][1] < min_yUpL_sel)? coordinates[i][1]: min_yUpL_sel;
			max_xLwR_sel = (coordinates[i][2] > max_xLwR_sel)? coordinates[i][2]: max_xLwR_sel;
			max_yLwR_sel = (coordinates[i][3] > max_yLwR_sel)? coordinates[i][3]: max_yLwR_sel;
		}
	}
	min_xUpL_sel = (min_xUpL_sel == 100000)? 0: min_xUpL_sel;
	min_yUpL_sel = (min_yUpL_sel == 100000)? 0: min_yUpL_sel;
	max_xLwR_sel = (max_xLwR_sel == -100000)? 0: max_xLwR_sel;
	max_yLwR_sel = (max_yLwR_sel == -100000)? 0: max_yLwR_sel;

	outlet(0, "selection", min_xUpL_sel, min_yUpL_sel, max_xLwR_sel, max_yLwR_sel);
}

function Scan(){
	myWindow = this.patcher.wind;
	myWidth = myWindow.location[2] - myWindow.location[0];
	myHeight = myWindow.location[3] - myWindow.location[1];
	
	myCanvas  =  this.patcher.getnamed("vpl_canvas");
	myWidth = myCanvas.rect[2] - myCanvas.rect[0];
	myHeight = myCanvas.rect[3] - myCanvas.rect[1];
	
	myDispWidth = myWidth - myDispBorder * 2;
	for(var i = 0; i < maxDisplays; i++){
		myButtons[i] = this.patcher.getnamed("display_" + i);
		myMenus[i] = this.patcher.getnamed("displayModes_" + i);
	}
	//post("got window with width=" + myWidth + " hight=" + myHeight + " myDispWidth=" + myDispWidth + "\n");
	outlet(0, "count");
}

function setmode(_disp, _val1, val2){
	outlet(_disp + 1, "setmode", _val1, val2);
	outlet(0, "coords", _disp);
	updateGUI();
	//updateSelection();
}

function mode(){
	var args = arrayfromargs(arguments);
	outlet(args[0] + 1, "mode", args[1], args[2], args[3], args[4], args[5], args[6]);
}

function currentstate(){
	var args = arrayfromargs(arguments);
	outlet(args[0] + 1, "currentstate", args[1], args[2], args[3], args[4], args[5], args[6]);
}

function coords(_id, _xUpL, _yUpL, _xLwR, _yLwR){
	coordinates[_id][0] = _xUpL;
	coordinates[_id][1] = _yUpL;
	coordinates[_id][2] = _xLwR;
	coordinates[_id][3] = _yLwR;
	//post("got coords: _id=" + _id + " _xUpL=" + _xUpL + " _yUpL=" + _yUpL + " _xLwR=" + _xLwR + " _yLwR=" + _yLwR + "\n");
	if(_xUpL == 0 && _yUpL == 0 && appGlobal.desktop_rect == null)
		appGlobal.desktop_rect = new Array( _xUpL, _yUpL, _xLwR, _yLwR);
}

function count(_val){
	displayCount = _val;
	for(var i = 0; i < maxDisplays; i++){
		if(i < _val){
			outlet(0, "currentstate", i);
			outlet(0, "coords", i);
			outlet(i + 1, "clear");
			outlet(i + 1, "hidden", 0);
			outlet(0, "getmode", i);
		}else
			outlet(i + 1, "hidden", 1);

	}
	updateGUI();
}

function updateGUI(){
	var min_xUpL = 0;
	var min_yUpL = 0;
	var max_xLwR = 0;
	var max_yLwR = 0;
	for(var i = 0; i < displayCount; i++){
 		min_xUpL = (coordinates[i][0] < min_xUpL)? coordinates[i][0]: min_xUpL;
		min_yUpL = (coordinates[i][1] < min_yUpL)? coordinates[i][1]: min_yUpL;
		max_xLwR = (coordinates[i][2] > max_xLwR)? coordinates[i][2]: max_xLwR;
		max_yLwR = (coordinates[i][3] > max_yLwR)? coordinates[i][3]: max_yLwR;
	}
	//post("got minmax: min_xUpL=" + min_xUpL + " min_yUpL=" + min_yUpL + " max_xLwR=" + max_xLwR + " max_yLwR=" + max_yLwR + "\n");
	var maxWidth = max_xLwR - min_xUpL;
	var maxHeight = max_yLwR - min_yUpL;
	//post("got maxes: maxWidth=" + maxWidth + " maxHeight=" + maxHeight + "\n");
	var ratio = myDispWidth / maxWidth;
	if(myDispWidth / myDispHeight > maxWidth / maxHeight){
		ratio = myDispHeight / maxHeight;
	}
	//post("got ratio=" + ratio + "\n");
	//post("got myDispWidth=" + myDispWidth + "\n");
	//post("got ratio: ratio=" + ratio + "\n");
	for(var i = 0; i < maxDisplays; i++){
		//post("got button:" + i + "\n");
		if(myButtons[i] != null){
			//post("displayCount:" + displayCount + "\n");
			if(i < displayCount){
				var abs_locx = coordinates[i][0] - min_xUpL;
				var disp_locx = myDispWidth / 2 + myDispBorder + (abs_locx -  maxWidth/2) * ratio;
				//var locx = (myDispWidth / 2 + myDispBorder + (coordinates[i][0] - min_xUpL - maxWidth/2) * ratio);
				var abs_locy = coordinates[i][1] - min_yUpL;
				var disp_locy = myDispBorder + abs_locy * ratio;
				//var locy = (myDispBorder + (coordinates[i][1] - min_yUpL) * ratio);
				var abs_width  = coordinates[i][2]  - min_xUpL - abs_locx;
				var disp_width  = abs_width * ratio;
				//var wid  = (maxWidth - (max_xLwR - coordinates[i][2])) * ratio - abs_locx;
				var abs_height  = coordinates[i][3]  - min_yUpL - abs_locy;
				var disp_height  = abs_height * ratio;
				//var hei  = (maxHeight - (max_yLwR - coordinates[i][3])) * ratio - abs_locy;
	//post("disprect: i " + i + " | disp_locx=" + disp_locx + " disp_locy=" + disp_locy + " disp_width=" + disp_width + " disp_height=" + disp_height + "\n");
				myButtons[i].message("hidden", 0);
				myButtons[i].message("presentation", 1);
				myButtons[i].message("presentation_rect", disp_locx, disp_locy, disp_width, disp_height);
				myMenus[i].message("hidden", 0);
				myMenus[i].message("presentation", 1);
				myMenus[i].message("presentation_rect", disp_locx + 5, disp_locy + 5, 130, 17);
			}else{
				myButtons[i].message("hidden", 1);
				myButtons[i].message("presentation", 0);
				myMenus[i].message("hidden", 1);
				myMenus[i].message("presentation", 0);
			}
		}
	}
}

function createArray(length) {
    var arr = new Array(length || 0),
        i = length;

    if (arguments.length > 1) {
        var args = Array.prototype.slice.call(arguments, 1);
        while(i--) arr[length-1 - i] = createArray.apply(this, args);
    }

    return arr;
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
