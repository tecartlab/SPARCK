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
setinletassist(0,"size");
setoutletassist(0,"jit.gl.sketch commands");

var myType = "point";
var ssize = .2;


var color = new Array(1., 1., 1., 1.);

function insides(_insides){
	drawfrontface(_insides);
}

function draw(){
	drawpreview();
	drawbackface();
}
0.
function drawpreview(){
	outlet(0,"reset");
	outlet(0,"quad", -1., -1., 0., -1., 1., 0., 1., 1., 0., 1., -1., 0.);
	outlet(0,"quad", 0., -1., -1., 0., 1., -1., 0., 1., 1., 0., -1., 1.);
	outlet(0,"quad", 0., 1., -1., 0., -1., -1., 0., -1., 1., 0., 1., 1.);
	outlet(0,"quad", -1., 0., -1., 1., 0., -1., 1., 0., 1., -1., 0., 1.);
	outlet(0,"quad", -1., 0., 1., 1., 0., 1., 1., 0., -1., -1., 0., -1.);
	outlet(0,"quad", 1., -1., 0., 1., 1., 0., -1., 1., 0., -1., -1., 0.);

	outlet(0,"linesegment", -1., -1., -1., -1., 1., -1.);
	outlet(0,"linesegment", -1., 1., -1., 1., 1., -1.);
	outlet(0,"linesegment", 1., 1., -1., 1., -1., -1.);
	outlet(0,"linesegment", 1., -1., -1., -1., -1., -1.);
	outlet(0,"linesegment", -1., -1., 1., -1., 1., 1.);
	outlet(0,"linesegment", -1., 1., 1., 1., 1., 1.);
	outlet(0,"linesegment", 1., 1., 1., 1., -1., 1.);
	outlet(0,"linesegment", 1., -1., 1., -1., -1., 1.);
	outlet(0,"linesegment", -1., -1., -1., -1., -1., 1.);
	outlet(0,"linesegment", -1., 1., -1., -1., 1., 1.);
	outlet(0,"linesegment", 1., 1., -1., 1., 1., 1.);
	outlet(0,"linesegment", 1., -1., -1., 1., -1., 1.);
}

function drawfrontface(_insides){
	var step = 1.0 / (_insides + 0.0);
	var s0 = -1.0;
	var s1 = 1.0;
	outlet(2,"reset");
	for(var i = 0; i < _insides; i++){
		s1 = 1. - step * i;
		s0 = -1. + step * i;
		outlet(2,"quad", s1, -1., -1., s1, 1., -1., s1, 1., 1., s1, -1., 1.);
		outlet(2,"quad", s0, 1., -1., s0, -1., -1., s0, -1., 1., s0, 1., 1.);
		outlet(2,"quad", -1., s1, 1., 1., s1, 1., 1., s1, -1., -1., s1, -1.);
		outlet(2,"quad", -1., s0, -1., 1., s0, -1., 1., s0, 1., -1., s0, 1.);
		outlet(2,"quad", 1., -1., s1, 1., 1., s1, -1., 1., s1, -1., -1., s1);
		outlet(2,"quad", -1., -1., s0, -1., 1., s0, 1., 1., s0, 1., -1., s0);
/*
		outlet(2,"quad", s1, s0, s0, s1, s1, s0, s1, s1, s1, s1, s0, s1);
		outlet(2,"quad", s0, s1, s0, s0, s0, s0, s0, s0, s1, s0, s1, s1);
		outlet(2,"quad", s0, s1, s1, s1, s1, s1, s1, s1, s0, s0, s1, s0);
		outlet(2,"quad", s0, s0, s0, s1, s0, s0, s1, s0, s1, s0, s0, s1);
		outlet(2,"quad", s1, s0, s1, s1, s1, s1, s0, s1, s1, s0, s0, s1);
		outlet(2,"quad", s0, s0, s0, s0, s1, s0, s1, s1, s0, s1, s0, s0);
*/
	}
}
function drawbackface(){
	outlet(1,"reset");
	outlet(1,"quad", -1., -1., -1., -1., 1., -1., 1., 1., -1., 1., -1., -1.);
	outlet(1,"quad", 1., -1., -1., 1., 1., -1., 1., 1., 1., 1., -1., 1.);
	outlet(1,"quad", -1., 1., -1., -1., -1., -1., -1., -1., 1., -1., 1., 1.);
	outlet(1,"quad", -1., -1., -1., 1., -1., -1., 1., -1., 1., -1., -1., 1.);
	outlet(1,"quad", -1., 1., 1., 1., 1., 1., 1., 1., -1., -1., 1., -1.);
	outlet(1,"quad", 1., -1., 1., 1., 1., 1., -1., 1., 1., -1., -1., 1.);
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
