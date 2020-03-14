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

var myWindowName;

/*
if (jsarguments.length > 1)
    myWindowName = jsarguments[1];
*/

// set up inlets/outlets/assist strings
inlets = 2;
outlets = 2;
setinletassist(0,"matrixA");
setinletassist(1,"matrixB");
setoutletassist(0,"textfield");
setoutletassist(1,"umenu");

var estateDBName = "bs::gui::windows::db";
var updatePath = "bs::gui::windows::updates";

var myEstateDB = new Dict(estateDBName);
var myCurrentStore = null;
var myStoreNames = null;
var empty = "<empty>"

function init(){
	//post("mngr: init() - dbclear\n");
	myEstateDB.clear();
	//post("mngr: init() - sendreset\n");
	myEstateDB.set("_slotspot", 0);

	messnamed(updatePath, "refresh");
	//post("mngr: init() - call refreshmenu()\n");
	myCurrentStore = refreshmenu();
}

var debug = false;

// set the debugger
function debugger(_val){
	debug = (_val == 1)? true: false;
}

function clear(){
	init();
}

function import(path){
	myEstateDB.clear();
	myEstateDB.import_json(path);
	messnamed(updatePath, "refresh");
	myCurrentStore = refreshmenu();
	load(myCurrentStore);
}

function export(path){
	save("Main"); // a hack to have everything currently set stored
	myEstateDB.export_json(path);
}

function refreshmenu(){
	outlet(1, "clear");
	//post("mngr: refreshmenu():\n");
	var select = empty;
	if(myEstateDB.contains("_storenames")){
		myStoreNames = myEstateDB.get("_storenames");
		if(typeof myStoreNames == "string"){
			outlet(1, "append", myStoreNames);
			select = myStoreNames;
			myStoreNames = new Array(myStoreNames);
		} else if (myStoreNames != null){
			for(var i = 0; i < myStoreNames.length; i++){
				outlet(1, "append", myStoreNames[i]);
			}
			select = myStoreNames[0];
		}
	} else {
		//post("mngr: no storenames\n");
		outlet(1, "append", empty);
		outlet(0, "set", empty);
	}

	//post("mngr: select:"+select+"\n");
	return select;
}

function clearstore(){
	messnamed(updatePath, "clear", myCurrentStore);

	if(myStoreNames != null && myStoreNames.indexOf(myCurrentStore) != -1){
		var index = myStoreNames.indexOf(myCurrentStore);
		myStoreNames[index] = "";
		myStoreNames.sort();
		myStoreNames.shift();
		if(myStoreNames.length > 0)
			myEstateDB.set("_storenames", myStoreNames);
		else
			myEstateDB.remove("_storenames");
	}

	myCurrentStore = refreshmenu();
	outlet(0, "set", myCurrentStore);

}

function notifydeleted(){
	myEstateDB.clear();
}

function save(storename){
	if(storename != empty && storename != null){
		if(myStoreNames == null){
			myStoreNames = new Array(storename);
		}else if(myStoreNames.indexOf(storename) == -1){
			myStoreNames.push(storename);
		}
		myEstateDB.set("_storenames", myStoreNames);

		refreshmenu();
		myCurrentStore = storename;
		outlet(0, "set", myCurrentStore);

		messnamed(updatePath, "save", storename);
	}
}

function load(storename){
	if(storename != empty){
		if(myStoreNames.indexOf(storename) == -1){
			myStoreNames.push(storename);
			myEstateDB.set("_storenames", myStoreNames);
		}

		myCurrentStore = storename;
		outlet(0, "set", myCurrentStore);

		myEstateDB.replace("_slotspot", 0);

		messnamed(updatePath, "load", storename);
	}
}

function listtype(_type){
	myEstateDB.replace("_slotspot", 0);

	messnamed(updatePath, "cleanupslotspots");

	messnamed(updatePath, "listtype", "<none>");
	messnamed(updatePath, "listtype", _type);
}

function enable_flags(i){
	messnamed(updatePath, "enable_flags", i);
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
