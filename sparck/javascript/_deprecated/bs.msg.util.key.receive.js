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
outlets = 2;
setinletassist(0,"address");
setinletassist(1,"key_in/key_out");
//setinletassist(1,"matrixB");
setoutletassist(0,"set send");
setoutletassist(1,"value");
setoutletassist(2,"substitute");

var sendAdress = "none";
var key = "_none";
var sub = "_none";

var myDict = null;

var subName = "substitute";
var routName = "route";

var current = subName;

function bang(){
	update();
}

function done(){
	update();
}

function title(val){
    sendAdress = val;
//    post("set send address: " + sendAdress + "\n");
    var tempDict = new Dict(undefined);
    var names = tempDict.getnames();
    if(names != null){
        for(var i = 0; i < names.length; i++){
//            post("  looking for names: " + names[i] + "\n");
            if(names[i].indexOf("msg::") == 0){
                tempDict = new Dict(names[i]);
//                post("    checking for title: " +val + " ...\n");
                if(tempDict.contains("_title") && tempDict.get("_title") == val){
                    myDict = new Dict("msg::" +names[i].substring(5));
//                    post("    set dict name: msg::" +names[i].substring(5) + "\n");
                }
            }
        }
    }
    update();
}

function out(val1, val2){
    key = val1;
    sub = val2
    if(sub == null){
        sub = val1;
    }
 //   post("key receive out: " + key + " | " + sub + "\n");
    if(sub == "><"){
        changeObjects(routName);
    } else {
        changeObjects(subName);
    }

    update();
}

function changeObjects(newObjectName){
	var theReceiver = this.patcher.getnamed("receiver");
	var theOutlet = this.patcher.getnamed("outlet");
	var jsOutlet1 = this.patcher.getnamed("jsoutlet1");
	this.patcher.remove(this.patcher.getnamed("substitute"));
	var theObject;
	if(newObjectName == routName)
		theObject =  this.patcher.newdefault(26, 182., newObjectName, key);
	else if(newObjectName == subName)
		theObject =  this.patcher.newdefault(26, 182., newObjectName, key, sub, 1);
	theObject.varname = "substitute";
	this.patcher.connect(jsOutlet1, 0, theObject, 0);
	this.patcher.connect(theReceiver, 0, theObject, 0);
	this.patcher.connect(theObject, 0, theOutlet, 0);
	current = newObjectName;
}

function update(){
	outlet(0, "set", "msg::" + sendAdress + "::" + key);
	if(myDict != null && myDict.contains(key)){
		outlet(1, myDict.get(key));
	}
}

function notifydeleted(){
	myDict = null;
}
