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

var undefined = "undefined";
var LEFTBORDERDIST = 1;
var RIGHTBORDERDIST = 1;
var BOTTOMBORDERDIST = 5;
var TOPBORDERDIST = 23;

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 2;
setinletassist(0,"input");
setoutletassist(0,"output to patcher");
setoutletassist(1,"output to ui");

var myEstateDB = new Dict("bs::gui::windows::db");
var myWindowDBName = null;
var myWindowDict = null;
var myStoreDBName = null;
var myStoreDict = null;
//var myStoreName = null;

var myWindow = null;
var myWindowTitle = "P";
var myWindowIsInit = false;
var myWindowPatcherName = "vpl_ThisNodeSpacePatcher";

var myNodeWorkspace = null;

var myRect = null;
var myVisibility = false;

var myPrefix = null;
var	myType = null;

var myStore = true;

//Default Flags:
var myFlagClose = "close";
var myFlagZoom = "zoom";
var myFlagMinimize = "nominimize";
var myFlagGrow = "nogrow";
var myFlagFloat = "float";
var myFlagTitle = "title";
var myFlagMenu = "nomenu";

var myFlagsAreEnabled = false;

var useWindowManagement = true;

var isDone = false;

function done(){
	if(!isDone){
		var owner = this.patcher.box;
		while (owner) {
			var NodeSpaceThisPatcher = owner.patcher.getnamed(myWindowPatcherName);
			if(NodeSpaceThisPatcher != null){
				myWindow = owner.patcher.wind;
				break;
			} else {
				owner = owner.patcher.box;
			}
		}
		isDone = true;
	}
}

var debug = false;

// set the debugger
function debugger(_val){
	debug = (_val == 1)? true: false;
}

// IMPORTANT: This messages has to be sent in order to use it in conjunction
// with the windows manager.
function workspace(_workspacename){
	myNodeWorkspace = _workspacename;

	myWindowDBName = "bs_gui_win_";
	if(myPrefix != null)
 		myWindowDBName += myPrefix + "_";
	if(myType != null)
 		myWindowDBName += myType + "_";
	myWindowDBName += myNodeWorkspace;

	myStoreDBName = myWindowDBName + "_store";

	refresh();
}


/*******************
/ MANAGER FUNCTIONS
/*******************/

// called to collapse all windows of the specified type and list them
// accordingly to their slotid (only those that are pinned are getting a
// slotid > 0 - this is set by the manager;
function listpinned(_type){
	//dont do anything
}

//Resets the window to stored data
function refresh(){
	if(useWindowManagement && isDone){
		//post("wins: loadMyWindowDict()...\n");
		loadMyWindowDict(); //some data is maintained on managment side
		//post("wins: saveMyWindowDict()...\n");
		saveMyWindowDict(); //some data is maintained on window side
	}
	//post("wins: .. reset done\n");
}

function notifydeleted(){
	clearMyWindowDict();
}

/*********************
/ DICT / DB FUNCTIONS
/*********************/

// loads a stored settings
function load(_storename){
//	post(myWindowTitle + "::load = " + _storename + "\n");
	if(useWindowManagement){
		if(myWindowDict != null && myWindowDict.contains(_storename)){
			myStoreDict = myWindowDict.get(_storename);
			// get the attributes from the dict and set the object
			myRect = myStoreDict.get("_rect");
			myVisibility = myStoreDict.get("_visible");

//			post(myWindowTitle + "::load() myRect = " + myRect + "\n");

			updateWindow();
		}
	}
}

// saves the current settings in the store
function save(_storename){
//	post(myWindowTitle + "::save = " + _storename + "\n");
	if(useWindowManagement){
		queryWindow();
//		post(myWindowTitle + "::save() myWindowDict:" + myWindowDict + " myWindowIsInit:" + myWindowIsInit + " myVisibility:" + myVisibility + " \n");
		if(myWindowDict != null && myWindowIsInit){
//			post(myWindowTitle + "::save() " + _storename + "\n");
			if(myStoreDict == null){
//				post(myWindowTitle + "::save() createStoreDB = " + myStoreDBName + "\n");
				myStoreDict = new Dict(myStoreDBName);
			}
			myStoreDict.clear();

			// get the attributes from the object and store them here in the dict
			myStoreDict.set("_rect", myRect);
			myStoreDict.set("_visible", myVisibility);

			if(myWindowDict.contains(_storename))
				myWindowDict.replace(_storename, myStoreDict);
			else
				myWindowDict.set(_storename, myStoreDict);

			saveMyWindowDict();
		}
	}
}

// removes a storename from the window dict
function clear(_storename){
	//post("winpops:  clear = " + _storename + "\n");
	if(useWindowManagement)
		if(myWindowDict != null)
			if(myWindowDict.contains(_storename)){
				myWindowDict.remove(_storename);
				saveMyWindowDict();
			}
}


function loadMyWindowDict(){
	if(useWindowManagement){
		//post("loadMyWindowDict:  myWindowDBName = " + myWindowDBName + " has EstateDict :\n");
		if(myEstateDB.contains(myWindowDBName)){
			myWindowDict = myEstateDB.get(myWindowDBName);
		}//else
			//post("loadMyWindowDict:  myEstateDB  doesnot contain " + myWindowDBName + "\n");
	}
}

function saveMyWindowDict(){
	if(useWindowManagement){
		//post("wins: saveMyWindowDict():\n");
		if(myWindowDict == null){
			myWindowDict = new Dict(myWindowDBName);
			//post("wins: saveMyWindowDict()- create myWindowDict\n");
		}

		//myWindowDict.clear();
		// get the attributes from the object and store them here in the dict
		//post("wins: saveMyWindowDict()- myWindowDict set(_type, myType)\n");
		myWindowDict.set("_type", myType);

		//post("wins: saveMyWindowDict()- storing in db...\n");
		if(myEstateDB.contains(myWindowDBName))
			myEstateDB.replace(myWindowDBName, myWindowDict);
		else
			myEstateDB.set(myWindowDBName, myWindowDict);
		//post("wins: saveMyWindowDict()- ...storing done\n");
	}
}

function clearMyWindowDict(){
	if(useWindowManagement && myEstateDB != null && myWindowDBName != null)
		if(myEstateDB.contains(myWindowDBName))
			myEstateDB.remove(myWindowDBName);
}


/***********************************
/ Window query and update functions
/***********************************/

function queryWindow(){
	if(myWindow != null && myWindowIsInit){
//		post(myWindowTitle + "::queryWindow() myWindow.location=" + myWindow.location + "\n");
		myRect = myWindow.location;
		myVisibility = myWindow.visible;
	}
}

function updateWindow(){
	//post("windowsE: " + myWindowTitle + " loadFromDB 0:\n");
	if(myWindow != null){
		if(myRect != null){
			outlet(0, "window", "size", myRect);
			outlet(0, "window", "exec");
		}
		if(myVisibility){
			outlet(0, "front");
			//post("winpops:updateWindow() myWindowIsInit = " + myWindowIsInit + "\n");
			myWindowIsInit = true;
		}else{
			outlet(0, "wclose");
		}
	}
}

/***********************************
/ Attributes setter and getter
/***********************************/

function msg_int(i){
	if(i == 0)
		winclose();
	if(i == 1)
		winopen();
}

// opens the window at its current location
function winopen(){
	queryWindow();
	myVisibility = true;
	//post("windowsE: " + myWindowTitle + " wopen\n");
	updateWindow();
}

//closes the window
function winclose(){
	//post("windowsE: " + myWindowTitle + " close\n");
	myVisibility = false;
	updateWindow();
}

// if the window is active or not
function active(i){
	if(myFlagsAreEnabled){
		if(i == 1){
			myWindowIsInit = true;
//			post(myWindowTitle + "::active() myWindowIsInit = " + myWindowIsInit + "\n");
		}
	}
}

// sets the title
function title(_title){
	myWindowTitle = "";
	if(myPrefix != null)
		myWindowTitle = myPrefix + "::";
	if(myType != null)
		myWindowTitle += myType + "::";
	myWindowTitle += _title;

	outlet(0, "title", myWindowTitle);
}

// sets the type of information
function prefix(_prefix){
	myPrefix = _prefix;
}

// sets the type of node
function type(_type){
	myType = _type;
}

//set if the window is managed by the windows manager and saves its data
// inside the windows db.
function managed(_val){
	useWindowManagement = (_val == 1)?true: false;
}

//sets the scriptname of the thispatcher which is inside the patcher that
// is used for this window - IMPORTANT: Works only as an attribute!
function patchername(_name){
	myWindowPatcherName = _name;
}

function anything(){
	if (inlet==0){
		if(messagename == "flags")
			setflags(arrayfromargs(arguments));
	}
}

function enable(_enable){
	enable_flags(_enable);
}

// used to manually override the presets
function enable_flags(enable){
	myFlagsAreEnabled = (enable == 1)? true: false;
	if(myFlagsAreEnabled){
		outlet(0, "window", "flags", myFlagClose);
		outlet(0, "window", "flags", myFlagZoom);
		outlet(0, "window", "flags", myFlagMinimize);
		outlet(0, "window", "flags", myFlagGrow);
		outlet(0, "window", "flags", myFlagFloat);
		outlet(0, "window", "flags", myFlagTitle);
		outlet(0, "window", "flags", myFlagMenu);
		outlet(0, "window", "exec");
	}
	if(!myFlagsAreEnabled){
		outlet(0, "window", "flags", "close");
		outlet(0, "window", "flags", "zoom");
		outlet(0, "window", "flags", "minimize");
		outlet(0, "window", "flags", "grow");
		outlet(0, "window", "flags", "nofloat");
		outlet(0, "window", "flags", "title");
		outlet(0, "window", "flags", "menu");
		outlet(0, "window", "exec");
	}
}

setflags.local = 1;
function setflags(flags){
	for(var i = 0; i < flags.length; i++){
		if(flags[i] == "close")
			myFlagClose = "close";
		if(flags[i] == "noclose")
			myFlagClose = "noclose";
		if(flags[i] == "zoom")
			myFlagZoom = "zoom";
		if(flags[i] == "nozoom")
			myFlagZoom = "nozoom";
		if(flags[i] == "minimize")
			myFlagMinimize = "minimize";
		if(flags[i] == "nominimize")
			myFlagMinimize = "nominimize";
		if(flags[i] == "grow")
			myFlagGrow = "grow";
		if(flags[i] == "nogrow")
			myFlagGrow = "nogrow";
		if(flags[i] == "float")
			myFlagFloat = "float";
		if(flags[i] == "nofloat")
			myFlagFloat = "nofloat";
		if(flags[i] == "title")
			myFlagTitle = "title";
		if(flags[i] == "notitle")
			myFlagTitle = "notitle";
		if(flags[i] == "menu")
			myFlagMenu = "menu";
		if(flags[i] == "nomenu")
			myFlagMenu = "nomenu";
	}
}

function pausecomp(ms) {
	ms += new Date().getTime();
	while (new Date().getTime() < ms){}
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
