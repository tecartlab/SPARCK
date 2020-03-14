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
inlets = 3;
outlets = 2;
setinletassist(0,"enable");
setinletassist(1,"store messages");
setinletassist(2,"stream messages");
setoutletassist(0,"dump");
setoutletassist(1,"set wormhole address");

var undefined = "undefined";

var myTitle = "" + jsarguments[1];
var myAppendix = "";
var myTypes = "";

var msgTitle = myTitle;
var msgAddress = "msg::undefined";

var purge = 0;
var unique = true;	// can be set via @unique
var verbose = true; // will print out messages when true

var myDict = null;
var setEnable = 1;

var stetupDone = false;

function loadbang(val){
    msgAddress = "msg::" + msgTitle;
    myDict = new Dict(msgAddress);
		//post("created new dict. msgAddress = " + msgAddress + "\n");
        //post("all dicts: = " + myDict.getnames() + "\n");

//		post("...accessing new dict. msgAddress = " + msgAddress + "\n");
    myDict.set("_enable", "_enable", setEnable);
    myDict.set("_types", myTypes);
    myDict.set("_title", msgTitle);

    outlet(1, "send", msgAddress + "::wormhole");
	stetupDone = true;
//		post("created new message send. address = " + sendAdress + " Appendix = " + myAppendix + " for msgAddress = " + msgAddress + " with enable "+ setEnable+"\n");
}

function msg_int(val){
	if(inlet == 0)
		if(val == 0 || val == 1)
			_enable(val);
}

function appendix(_val){
	//in case the program needs to send a message called <this functionname> than this
	// statement makes it possible:
	if(inlet > 0){
        sendMessage("appendix", _val);
	} else {
		if(stetupDone && myAppendix != _val)
			updateAddress(myTitle, _val);
		else
			myAppendix = _val;
	}
}

function title(val){
	//in case the program needs to send a message called <this functionname> than this
	// statement makes it possible:
	if(inlet > 0){
        sendMessage("title", val);
	} else {
		if(myTitle != val)
			updateAddress(val, myAppendix);
	}
}

function setmsgtitle(val){
	//in case the program needs to send a message called <this functionname> than this
	// statement makes it possible:
	if(inlet > 0){
        sendMessage("setmsgtitle", val);
	} else {
		if(myTitle != val)
        	updateAddress(val, myAppendix);
	}
}

updateAddress.local = 1;
function updateAddress(title, appendix){
//    post("update address from <"+msgTitle+"> to <" + title + appendix + "> \n");
    myTitle = title;
    myAppendix = appendix;
    // change firts the msgTitle
    msgTitle = title + appendix;
    var list = new Array("_title", msgTitle);

    // set the new title inside the old dict
    myDict.set("_title", msgTitle);

    // clone the dict with a new name
    var newDict = new Dict("msg::" + msgTitle);
    cloneDict(myDict, newDict);

    myDict.clear();
    myDict = newDict;

//    post("cloned dict: " + myDict.getkeys() + "\n");

    //tells the attached bs.msg.receive.select objects that the
    //title has changed... still using the old address..
    messnamed(msgAddress+"::_enable",list);
    // ... for the undifined the new title
    messnamed("msg_undefined::"+msgTitle+"::_enable",list);

    //  change the address
    msgAddress = "msg::" + msgTitle;

    // and then change the wormhole
    outlet(1, "send", msgAddress + "::wormhole");
}

cloneDict.local = 1;
function cloneDict(oldDict, newDict){
    var listOfOldKeys = oldDict.getkeys();
    for(var i = 0; i < listOfOldKeys.length; i++){
        newDict.set(listOfOldKeys[i], oldDict.get(listOfOldKeys[i]));
    }
}

function anything(){
//post("got message to send: from:" + sendTitle + myAppendix + " message: "+ messagename +" \n");
	if(inlet == 0){
		if(messagename.indexOf("enable") != -1){
			_enable(arguments[0]);
		} else if(messagename.indexOf("types") != -1){
			var type = arrayfromargs(arguments);
			if(stetupDone)
				myDict.set("_types", type);
			else
				myTypes = type;
		} else if(messagename.indexOf("getdict") != -1){
			outlet(0, "dict", msgAddress);
		} else if(messagename.indexOf("unique") != -1){
			unique = (arguments[0] == 0)?false:true;
		}else if(messagename.indexOf("dump") != -1){
			outlet(0, "dump", "address", msgAddress);
			outlet(0, "dump", "title", myTitle);
			outlet(0, "dump", "appendix", myAppendix);
			outlet(0, "dump", "types", myType);
		}
	} else if(inlet == 1 && myDict != null){
        //post("set: values: " + values +"\n");
        myDict.set(messagename, arrayfromargs(messagename, arguments));
        messnamed(msgAddress+"::"+messagename, arrayfromargs(messagename, arguments));
	} else if(inlet == 2){
		messnamed(msgAddress+"::"+messagename, arrayfromargs(messagename, arguments));
	}
}

sendMessage.local = 1;
function sendMessage(msgKey, msgValues){
    var list = new Array(msgKey, msgValues);
    myDict.set(msgKey, list);
    messnamed(msgAddress+"::"+msgKey,list);
//    post("sendto: "+msgAddress+"::"+ msgTitle +" | values: " + msgValues +"\n");
}

function _enable(val){
	if(stetupDone){
        sendMessage("_enable", val);
		//post("send new message address = " + sendAdress + " Appendix = " + myAppendix + " for msgAddress = " + msgAddress + " with enable "+ val+"\n");
	}
	setEnable = val;
}

function _types(val){
	error("message '_types' is reserved");
}

function _unique(val){
	error("message '_unique' is reserved");
}

function _title(val){
	error("message '_title' is reserved");
}

function notifydeleted(){
	if(unique){
		//post("clearing dict with address = " + msgAddress + "\n");
		// this dict will only be cleared if it is unique
		myDict.clear();
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
