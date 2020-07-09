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
setinletassist(0,"messages");

var availableRows = 1;
var availableCols = 1;

var selectedRows = 1;
var selectedCols = 1;



function loadbang(){
	enableColums();
	enableRows();
}

function rows(){
	availableRows = arguments[0] + 1;
	update();
	enableRows();
}

function cols(){
	availableCols = arguments[0] + 1;
	update();
	enableColums();
}

function selRow(){
	selectedRows = arguments[0];
	update();
}

function selCol(){
	selectedCols = arguments[0];
	update();
}

function enableColums(){
	var range = new Array(12);
	var enableItem = new Array(12);
	for(var i = 0; i < range.length; i++){
		range[i] = "column_" + (i + 1);
		enableItem[i] = ((availableCols - 1) >= i)?1:0;
	}
	outlet(1, "range", range);
	outlet(1, "enableitem", enableItem);
}

function enableRows(){
	var range = new Array(2);
	var enableItem = new Array(2);
	for(var i = 0; i < range.length; i++){
		range[i] = "row_" + (i + 1);
		enableItem[i] = ((availableRows - 1) >= i)?1:0;
	}
	outlet(2, "range", range);
	outlet(2, "enableitem", enableItem);
}

function update(){
	var _colScale = 1. / availableCols;
	var _rowScale = 1. / availableRows;

	var _colPos = (_colScale -1.) + ( _colScale * 2 * selectedCols);
	var _rowPos = (1. - _rowScale) - ( _rowScale * 2 * selectedRows);

	var _vpScaleX = 1. / availableCols;
	var _vpScaleY = 1. / availableRows;

	var _vpPosX = _colScale * selectedCols;
	var _vpPosY = (1 - _rowScale) * (1 - selectedRows);

//	outlet(0, "scale" , _colScale, _rowScale, 1);
//	outlet(0, "position" , _colPos, _rowPos, 0);
	outlet(0, "viewport" , _vpPosX, _vpPosY, _vpScaleX, _vpScaleY);
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
