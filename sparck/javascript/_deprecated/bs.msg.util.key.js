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
inlets = 2;
outlets = 3;
setinletassist(0,"address/value");
setinletassist(1,"key_in/key_out");
//setinletassist(1,"matrixB");
setoutletassist(0,"set send");
setoutletassist(1,"value");
setoutletassist(2,"offvalue");

var key = "_none";
var sub = "_none";

var undefined = "undefined";
var subName = "substitute";
var routName = "route";
var routpassName = "routepass";

var offvaluelist = undefined;

var current = subName;

function anything()
{
//    post("utilKey: " + messagename + " -> " + arguments[0] + "\n");
	if(inlet == 0){
		if(messagename != "off"){
			// if a change happens, the offvalues are first sent,
			//  because if the new address doesnt have this key,
			//  this outlet will react as if the selection is off.
			if(offvaluelist != undefined)
				outlet(2, offvaluelist);
			outlet(0, "set", "msg::" + messagename + "::" + key);
			outlet(1, arrayfromargs(arguments));
		} else {
			outlet(0, "set");
			if(offvaluelist != undefined)
				outlet(2, offvaluelist);
		}
	}
	if(inlet == 1){
		if(messagename.indexOf("outoff") == 0){
			offvaluelist = arrayfromargs(arguments);
		} else if (messagename.indexOf("out") == 0){
			key = arguments[0];
			sub = arguments[0];
			if(arguments.length == 2)
				sub = arguments[1];
			if(sub == "><")
				changeObjects(routName);
			else{
				if(key == sub)
					changeObjects(routpassName);
				else
					changeObjects(subName);
			}
		}
	}
//	cpost("utilKey EXIT: " + messagename + " -> " + arguments[0] + "\n");

}

function changeObjects(newObjectName){
	var theReceiver = this.patcher.getnamed("receiver");
	var theOutlet = this.patcher.getnamed("outlet");
	var jsOutlet1 = this.patcher.getnamed("jsoutlet1");
	this.patcher.remove(this.patcher.getnamed("substitute"));
	var theObject;
	if(newObjectName == routName)
		theObject =  this.patcher.newdefault(26, 135, newObjectName, key);
	if(newObjectName == routpassName)
		theObject =  this.patcher.newdefault(26, 135, newObjectName, key);
	else if(newObjectName == subName)
		theObject =  this.patcher.newdefault(26, 135, newObjectName, key, sub, 1);
	theObject.varname = "substitute";
	this.patcher.connect(jsOutlet1, 0, theObject, 0);
	this.patcher.connect(theReceiver, 0, theObject, 0);
	this.patcher.connect(theObject, 0, theOutlet, 0);
	current = newObjectName;
}
