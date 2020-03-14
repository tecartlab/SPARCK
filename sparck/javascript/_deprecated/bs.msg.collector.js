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
var owner = this.patcher;

// set up inlets/outlets/assist strings
inlets = 2;
outlets = 1;
setinletassist(0,"address");
setinletassist(1,"getter");
setoutletassist(0,"dump");

var msgaddress;
var myAppendix = "";
var myTitle = null;

var outputPosX = 30;
var outputPosY = 100;
if (jsarguments.length >= 3)
    outputPosY = jsarguments[2];
if (jsarguments.length >= 3)
    outputPosX = jsarguments[1];

var addressSource = this.patcher.getnamed("addressSource");
var setupdone = false;

function done(){
	outlet(0, "title", msgaddress);
	setupdone = true;
//	post("send address: " + msgaddress + "\n");
}

function address(_address){
	msgaddress = _address;
	done();
}

function title(dname){
	dtitle = dname + myAppendix;
	if(myTitle == null || !(myTitle.indexOf(dtitle) == 0 && myTitle.length == dtitle.length)){
		var tempDict = new Dict(undefined);
		var names = tempDict.getnames();
//		post("look for title: " + dtitle + "\n");
//		post("have appendix: " + myAppendix + "\n");
		for(var i = 0; i < names.length; i++){
			if(names[i].indexOf("msg::") == 0){
				tempDict = new Dict(names[i]);
				if(tempDict.contains("_title")){
//					post("("+dtitle+") found title: " + tempDict.get("_title") + "\n");
 					var tempTitle = tempDict.get("_title");
					if(tempTitle.indexOf(dtitle) == 0 && tempTitle.length == dtitle.length){
//						post("("+dtitle+") setto title: " + tempDict.get("_title") + "\n");
						myTitle = tempTitle;
						msgaddress = names[i].substring(5);
////						post("("+dtitle+") set to address: " + msgaddress + "\n");
						done();
						return;
					} else {
//						post("("+dtitle+") not my title : " + tempDict.get("_title") + "\n");
					}
				}
			}
		}
	}
//	post("found no title for: " + dname + "\n");
}

function appendix(_val){
	myAppendix = _val;
}

function anything()
{
	if (inlet==1){
		if(setupdone == false && messagename.indexOf("out") != -1){
			if(owner.getnamed(messagename) == null){
				outputPosX = outputPosX + 50;
				outputPosY = outputPosY + 30
				var receiver = owner.newdefault(outputPosX, outputPosY, "bs.msg.util.key.receive", "@out", arrayfromargs(arguments));
				receiver.varname = messagename;

				var outlet = owner.getnamed("outlet");
				owner.connect(this.box, 0, receiver, 0);
				owner.connect(receiver, 0, outlet, 0);

			//owner.connect(substitute, 0, main_outlet, 0);
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
//    outlet(0,a);
//  }
