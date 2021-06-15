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

var owner = this.patcher;

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 3;
setinletassist(0,"set");
setoutletassist(0,"drawto");
setoutletassist(1,"update");

var outKeysCount = 6;
if (jsarguments.length > 1)
    outKeysCount = jsarguments[1];

var IN_0 = 0;

var OUT_MAIN = 0;
var OUT_ENABLED = 1;
var OUT_DUMP = 2;

var undefined = "off";

var isInitialized = false;

var myContexts = Array(undefined);
var myGroup = 0;

// global varables and code

var	myDict;

var rendergroups = new Dict("bs::rendergroup");

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

function notifydeleted(){
    // first reset all my context to NULL
    for(var i = 0; i < myContexts.length; i++){
        rendergroups.set(myContexts[i]);
    }
}

function anything()
{
    var a = arrayfromargs(arguments);
 	if (inlet==IN_0){
		if(messagename.indexOf("group") != -1){
            myGroup = a[0];
    		//post("receive group: " + messagename + " = " + myGroup + "\n");
           // set all myContexts
            for(var i = 0; i < myContexts.length; i++){
                rendergroups.set(myContexts[i], myGroup);
            }

            if(myGroup == 0){
            	outlet(OUT_ENABLED, 0);
            } else {
            	outlet(OUT_ENABLED, 1);
			}

            outlet(OUT_DUMP, "bang");
        }
 		if(messagename.indexOf("drawto") != -1){
            // first reset all my context to NULL
            for(var i = 0; i < myContexts.length; i++){
                rendergroups.set(myContexts[i]);
            }
            // then set all new contexts
            for(var i = 0; i < a.length; i++){
                rendergroups.set(a[i], myGroup);
            }
            // then set myContext with the new contexts
            myContexts = a;
            outlet(OUT_DUMP, "bang");
		}
		if(messagename.indexOf("update") != -1){
            keys = rendergroups.getkeys();
            if(keys != null){
                if(typeof keys == "string"){
                    keys = new Array(keys);
                }
                out = Array("drawto");
                for(var i = 0; i < keys.length; i++){
                    if(rendergroups.get(keys[i]) & a[0]){
                        out.push(keys[i]);
                    }
                }
                if(out.length == 1){
                    out.push("off");
                	outlet(OUT_ENABLED, 0);
                } else {
                	outlet(OUT_ENABLED, 1);
				}
                outlet(OUT_MAIN, out);
            }
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
//    outlet(OUT_MAIN,a);
//  }
