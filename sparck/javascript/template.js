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

// autowatch = 1;

var owner = this.patcher;

// set up inlets/outlets/assist strings
inlets = 3;
outlets = 4;
setinletassist(0,"settings");
setinletassist(1,"refresh");
setinletassist(0,"selection");
setoutletassist(0,"umenu");
setoutletassist(1,"receive");
setoutletassist(2,"set");
setoutletassist(3,"store");

var outKeysCount = 6;
if (jsarguments.length > 1)
    outKeysCount = jsarguments[1];

var IN_0 = 0;
var IN_1 = 1;
var IN_2 = 2;

var OUT_UMENU = 0;
var OUT_MESSAGES = 2;
var OUT_SETUP = 3;
var OUT_STORAGE = 1;

var undefined = "off";

var isInitialized = false;

// global varables and code

var	myDict;

var appGlobal = new Global("bs::app::global");
var debug = (appGlobal.debug == 1 || appGlobal.debug == 2)? true:false;

// set the debugger : 0=off, 1=all, 2=app, 3=io, 4=db, 5=node, 6=select, 7=receive
function debugger(_val){
	debug = (_val == 1 || _val == 2)? true: false;
}

function dpost(_message){
    if(debug){
        post("bs.rendergroup: " + _message + " \n");
    }
}

function loadbang(){
}

function anything()
{
    //post("receive selector for inlet="+inlet + ": " + messagename + " = " + arguments[0] + "\n");
 	if (inlet==IN_1){
		if(messagename.indexOf("types") == 0){
       }
	} else if (inlet==IN_2){
		if(messagename.indexOf("bang") != -1){
		}
	} else if (inlet==IN_0){
		if(messagename.indexOf("item") != -1){
		}
	}
}

function debugpost(_poststring){
    if(debug){
        if(ignore != null){
            post("selector(" + ignore + "): " + _poststring + "\n");
        } else {
            post("selector<" + types + ">: " + _poststring + "\n");
        }
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
//    outlet(OUT_UMENU,a);
//  }
