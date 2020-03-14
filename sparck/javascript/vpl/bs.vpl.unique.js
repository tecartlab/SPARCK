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

var myTitle = "undefined";
var myID = null;
var myWorkspaceObject = null;

var uniqueTable = new Dict("bs.vpl.unique.title");

var vpl_edit;
var vpl_titleEdit;

var myID = null;
var setupdone = false;

var isInEditMode = -1; // this value has to be -1 at the beginning

// patcher arguments
if (jsarguments.length > 1){
    myID = "nodeid_" + jsarguments[1];
}

function loadbang(){
	if(this.patcher.getnamed("vpl_edit") != null){
		vpl_edit = this.patcher.getnamed("vpl_edit");
		//post(" has vpl_edit...\n");
	}
	if(this.patcher.getnamed("vpl_titleEdit") != null){
		vpl_titleEdit = this.patcher.getnamed("vpl_titleEdit");
		//post(" has vpl_titleEdit...\n");
	}
}

// is called when the node has finished loading.
// used when node is not embedded in app
function done(){
	if(!setupdone)
		setup();
}

// is called by javascript upon creation / loading / importing app
function init(){
	if(!setupdone)
		setup();
}

// called when node was created within app.
function nodename(_val){
	myID = _val;
}

function setup(){
	//post("myID: " + myID + "\n");

	// searching the node-box in order to be able to set the title as varname
	var owner = this.patcher.box;
	var varName = null;
	if(owner){
		myWorkspaceObject = owner.patcher.box;
		if(myWorkspaceObject)
			varName = myWorkspaceObject.varname;
	}

	// this makes sure that at startup the title attribute will
	// be used if no scriptname is set (i.e. if the node is beeing edited
	varName = (varName == null)? myTitle: varName;
	// change the myTitle to a undef so the setTitle function can do its thing
	myTitle = "undef";

	outlet(0, "nodeid", myID);
	setTitle(varName);
	setupdone = true;
}

// creating a unique nodename. if the node was created by the app, it uses the
// already unique nodename from the app.
// called when a new title was entered in the text box or the node is created
function setTitle(_newTitle){
	//post("setTitle = " + _newTitle + "\n");
	if(_newTitle != null){
		var oldTitle = myTitle;
		if(_newTitle.indexOf(myTitle) != 0 || _newTitle.length != myTitle.length){
			myTitle = _newTitle;
			if(!isUniqueValue(myTitle)){
//           	post("no unique title: " + myTitle + "....\n");
				var counter = 1;
				while(!isUniqueValue(myTitle)){
					myTitle = myTitle + "_" + counter++;
				}
 			}
			// it is used to determine if the title was changed by the
			//    user -> in this case an undo action is created
			//    undo/redo -> in this case no undo action should be created
			if(isInEditMode == 1){
				messnamed("bs::app::reflection", "nodename", oldTitle, myTitle);
			} else if(isInEditMode == 0) {
				messnamed("bs::app::reflection", "nodename", "ignore", myTitle);
				// "ignore" should not be changed unless the code
				// inside bs.app.pattrmirror.js is adjusted
			}
			// at the creation of this node no message should be sent,
			// so no unnecessary dump of the pattrstroge for
			// the reflection is done.


			// store the new name in the global list
			uniqueTable.set(myID, myTitle);

			//change the scripting name of the node
			if(myWorkspaceObject != null)
				myWorkspaceObject.varname = myTitle;

			// set the receive objects
			// they should be called before the node db is informed
			outlet(1, myTitle);

			// tell the node script about the change
			outlet(0, "title", myTitle);

			//set the node title
			outlet(2, "set", myTitle)
			//outlet(2, "name", "logic." + myTitle)
		}
		edit(0);
	}
}

// this method is called when the user attempts to edit the node title.
function edit(_enable){
	//post("set edit to : " + _enable + "\n");
	isInEditMode = _enable;
	//jump out of title editmode
	if(vpl_titleEdit != null){
		vpl_titleEdit.message("border", (_enable == 1)? 1: 0);
		vpl_titleEdit.message("ignoreclick", (_enable == 1)? 0: 1);
		vpl_titleEdit.message("hint", (_enable == 1)? "edit node name": "node name");
	}
	if(vpl_edit != null){
		this.patcher.message("script", "sendbox", "vpl_edit", "hidden", (_enable == 1)? 1: 0);
		//vpl_edit.message("hidden", (_enable == 1)? 1: 0);
	}
}

// sets the default title
function title(_val){
	myTitle = _val;
	//post("set my title as : " + myTitle + "\n");
}

//checks if title is a unique title
function isUniqueValue(_val){
	var ids = uniqueTable.getkeys();
	if(typeof ids == "string")
		ids = new Array(ids);
	if(ids != null){
		for(var i = 0; i < ids.length; i++){
			if(ids[i].indexOf(myID) == -1 || (ids[i].indexOf(myID) != -1 && ids[i].length != myID.length)){
				var _title = uniqueTable.get(ids[i]);
				if(_title.indexOf(_val) != -1 && _title.length == _val.length)
					return false;
			}
		}
	}
	return true;
}

//Called when the parent node was deleted
function notifydeleted(){
	//post("Unique to got deleted....\n");
	if(myID != null && uniqueTable.contains(myID)){
		uniqueTable.remove(myID);
		//post(myID + "...Unique deleted\n");
    }
}
