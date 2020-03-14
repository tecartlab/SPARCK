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

inlets = 1;
outlets = 3;

var DEBOUNCE_TIME = 500;

var UNDO_MAX_STEPS = 100;

var OUTLET_STORAGE = 0;
var OUTLET_GUI = 1;
var OUTLET_MARKER = 2;

var UNDO_TYPE_PROPS = 0;
var UNDO_TYPE_NODENAME = 1;

var nodeList = null;
var store = null;
var nodeAttributes = null;

var undoStore = null;
var redoStore = null;

var debounceKey = null;
var debounceVal = null;

var lastAttribute = "";

var nodeNameChange = null;

var debounceTimer = new Task(debounceToStore, this);

var recallTimer = new Task(recall, this);

var appGlobal = new Global("bs::app::global");

// set the debugger : 0=off, 1=all, 2=app, 3=io, 4=db, 5=node, 6=select
var debug = (appGlobal.debug == 1 || appGlobal.debug == 2)? true:false;

// set the debugger
function debugger(_val){
	debug = (_val == 1 || _val == 2)? true: false;
}

function dpost(_message){
    if(debug){
        post("reflect: " + _message + " \n");
    }
}

// called on startup
function loadbang(){
	if(nodeAttributes == null){
		nodeAttributes = new Dict("pattrmirror");
		nodeAttributes.clear();
	}
	windowLock();
	store = new Array();
	undoStore = new Array();
	redoStore = new Array();
}

function resetundo(){
	undoStore = new Array();
	redoStore = new Array();
}

function recall(){
	if(debug)
		post("get pattrstore dump...\n");
	store = new Array();
	outlet(OUTLET_STORAGE, "dump");
}


// function of store the snapshot of storage
function snapshot(){
	var list = arrayfromargs(arguments);
	if(debug)
		post("dump: " + list + "\n");
    var attribute = list[0];
	list.shift();
	var value = list.toString().replace(/,/g, " ");
	if(attribute == "dump" && value == "done"){
		outlet(OUTLET_STORAGE, "done");
	} else {
		store.push(new Array(attribute, value));
	}
}

function nodename(_oldName, _newName){
	if(debug)
		post("got nodename change: " + _oldName + " -> " + _newName + "\n");
	nodeNameChange = new Array(_oldName, _newName);
}

// called when a property was changed.
// it debounces the incomming messages by 1 s
function next(){
	var newList = arrayfromargs(arguments);
    var newAttribute = newList[0];
	newList.shift();
	var newValue = newList.toString().replace(/,/g, " ");

	// we only want attributes that are not ::vpl_
	if(newAttribute.indexOf("::vpl_") == -1 && newValue.indexOf("SaveAs...") == -1){
		if(debounceKey == null){
			debounceKey = new Array();
			debounceVal = new Array();
		}
		var indx = debounceKey.indexOf(newAttribute);
		if(indx != -1){
			debounceVal[indx] = newValue;
		} else {
			debounceKey.push(newAttribute);
			debounceVal.push(newValue);
		}
		debounceTimer.schedule(DEBOUNCE_TIME);
	} else if(newAttribute.indexOf("::vpl_nodelogic::vpl_titleEdit") != -1 ){//&& newValue.indexOf("<empty>") == -1){
		// we are only interested in attribute changes where
		// the value is part of the atttribute
		if(newAttribute.indexOf("::" + newValue + "::") != -1){
			if(nodeNameChange != null){
				// if the attribute has the new node name inside and
				// the old name is NOT "ignore"
				if(nodeNameChange[0].indexOf("ignore") == -1 && newAttribute.indexOf(nodeNameChange[1]) != -1){
					// creates a new task
					var task = new Array(newAttribute.substring(0, newAttribute.indexOf(nodeNameChange[1])), nodeNameChange[0], nodeNameChange[1]);
					// and stores it
					undoStore.push(new Array(UNDO_TYPE_NODENAME, task));
					redoStore = new Array();
					if(debug)
						post("got nodename change: " + task[0] + " " + task[1] + " -> " + task[2] + "\n");
					nodeNameChange = null;
				}
				if(debug)
					post("::vpl_titleEdit: " + newAttribute + " -> " + newValue + " snapshot\n");
				// this message is an indication that a node name has changed,
				// by whichever means (user edit or undo/redo), so we have to
				// refresh the store
				recall();
			}
		}
	}
}

// called by the debounce timer.
// it sends the debounced values to be processed
function debounceToStore(){
	while(debounceKey.length > 0){
		var atr = debounceKey.pop();
		var val = debounceVal.pop();
		//post("attempt to store: " + atr + " " + val + "\n");
		setStore(atr, val, true);
	}
}

//store the key-value pairs if the value has changed and create
// undo steps if needed.
// the method is called by debounceToStore(), undo() and redo()
function setStore(_attribute, _value, _undoable){
	for(var i = 0; i < store.length; i++){
    	var oldAttribute = store[i][0];
		var oldValue = store[i][1];
		if(_attribute.indexOf(oldAttribute) == 0 && _attribute.length == oldAttribute.length){
			if(_value != oldValue){
				if(_undoable){
					// creates a property task
					var task = new Array(_attribute, oldValue, _value);
					// and stores it
					undoStore.push(new Array(UNDO_TYPE_PROPS, task));
					// drop the first undoes from the list if the list
					// exceeds the maximum steps
					while(undoStore.length > UNDO_MAX_STEPS){
						undoStore.shift();
					}
					redoStore = new Array();
					if(debug)
						post("property change: " + _attribute + " " + oldValue + " -> " + _value + "\n");
				}
				store[i] = new Array(_attribute, _value);
			}
			// if it found a match exit this function
			return;
		}
	}
	// in this case the attribute doesnt exist in the store, most likely
	// because its properties from a new node
	if(_undoable){
		store.push(new Array(_attribute, _value));
		if(debug)
			post("added new attribute: " + _attribute + " " + _value + "\n");
	}
}

// step back into history
function undo(){
	// first make sure no more debounced messages are waiting
	debounceToStore();
	if(undoStore.length > 0){
		var undoStep = undoStore.pop();
		if(undoStep[0] == UNDO_TYPE_PROPS){
			// get the property task (an array with 3 items)
			var task = undoStep[1];
			setStore(task[0], task[1], false);
			if(debug)
				post("undo: " + task[0] + " " + task[1] + "\n");
			outlet(OUTLET_STORAGE, "undo", task[0], task[1].split(" "));
		} else 	if(undoStep[0] == UNDO_TYPE_NODENAME){
			// get the nodename task (an array with 3 items: attributepath, oldname, newname)
			var task = undoStep[1];
			outlet(OUTLET_STORAGE, "undo", task[0] + task[2] + "::vpl_nodelogic::vpl_titleEdit", task[1]);
			if(debug)
				post("undo nodename: " + task[0] + task[2] + "::vpl_nodelogic::vpl_titleEdit" + " " + task[1] + "\n");
		}
		redoStore.push(undoStep);
	}
}

// step forward to the present
function redo(){
	// first make sure no more debounced messages are waiting
	debounceToStore();
	if(redoStore.length > 0){
		var redoStep = redoStore.pop();
		if(redoStep[0] == UNDO_TYPE_PROPS){
			// get the property task (an array with 3 items)
			var task = redoStep[1];
			setStore(task[0], task[2], false);
			if(debug)
				post("redo: " + task[0] + " " + task[2] + "\n");
			outlet(OUTLET_STORAGE, "redo", task[0], task[2].split(" "));
		}else if(redoStep[0] == UNDO_TYPE_NODENAME){
			// get the nodename task (an array with 3 items: attributepath, oldname, newname)
			var task = redoStep[1];
			outlet(OUTLET_STORAGE, "redo", task[0] + task[1] + "::vpl_nodelogic::vpl_titleEdit", task[2]);
			if(debug)
				post("undo nodename: " + task[0] + task[1] + "::vpl_nodelogic::vpl_titleEdit" + " " + task[2] + "\n");
		}
		undoStore.push(redoStep);
	}
}

/*******************************************

            REFLECTION

********************************************/

// when opening the gui the marker list and the store are updated
function reflect(){
	outlet(OUTLET_GUI, "nodes", "clear");
	outlet(OUTLET_GUI, "text", "clear");
	outlet(OUTLET_GUI, "attributes", "clear");

	outlet(OUTLET_GUI, "window","front");

	outlet(OUTLET_MARKER, "getmarkerlist");
	recall();
}

// stores the data of all the available and relevant nodes
function markerlist(){
	var markerlist = arrayfromargs(arguments);
	markerlist.sort();
	markerlist.reverse();
	nodeList = new Array();
	if(typeof markerlist == "string"){
		outlet(OUTLET_GUI, "nodes", "insert", "0", list);
	} else {
		for(var i = 0; i < markerlist.length; i++){
			if(markerlist[i] != "_app"){
				outlet(OUTLET_GUI, "nodes", "insert", "0", markerlist[i]);
				nodeList.push(markerlist[i]);
			}
		}
	}
}

// sent by the GUI when a node is selected
function getNode(_node){
	nodeAttributes.clear();

//	post("node " + _node + "\n");
	// search through the store and look for
	// attributes that belong to this node
	// and add the found properties to the
	// gAttribute dictionary.

	for(var i = 0; i < store.length; i++){
    	var attribute = store[i][0];
		var value = store[i][1];
		if(attribute.indexOf(_node+"::") != -1 && attribute.indexOf("::_") == -1){
			var complete = attribute.substring(attribute.lastIndexOf(_node), attribute.length)
			var att;
			if(attribute.indexOf("property::") != -1){
				att = attribute.substring(attribute.lastIndexOf("property::") + 10, attribute.length);
			} else {
				att = attribute.substring(attribute.lastIndexOf(_node) + _node.length + 2, attribute.length);
			}
			// we ignore attributes that start with an '_' or 'vpl_'
			if(att.indexOf("vpl_") != 0 && att.indexOf("_") != 0){
				//post(att + " = /sparck/node ::" + complete + " " + value + "\n");
				att = att.replace("::", "/");
				nodeAttributes.set(att, "/sparck/node ::" + complete + " " + value);
			}
		}else if(!(attribute.indexOf("read") == 0) && attribute.indexOf("::") == -1){
			// only sparck_application settings should get in here
			// AND we ignore attributes that start with an '_' or 'vpl_'
			if((_node != null && _node.indexOf("Application_sparck") == 0) && attribute.indexOf("vpl_") != 0 && attribute.indexOf("_") != 0){
    			//post(" root appl settings = " + attribute + " " + value + "\n");
				nodeAttributes.set(attribute, "/sparck/node " + attribute + " " + value);
			}
		}
	}

	// clear the GUI
	outlet(OUTLET_GUI, "attributes", "clear");
	outlet(OUTLET_STORAGE, "done");
	var attrList = nodeAttributes.getkeys();
	// fill up the GUI's attributes list
	if(attrList != null && attrList.length > 0){
 		attrList.sort();
		attrList.reverse();
		for(var i = 0; i < attrList.length; i++){
			outlet(OUTLET_GUI, "attributes", "insert", "0", attrList[i]);
		}
	}else {
		post("no properties accessible for the node = "+ _node +" \n")
	}

	// clear the text
	outlet(OUTLET_GUI, "text", "clear");
}

// sent by the GUI when an attribute is selected
function getAttribute(attribute){
	outlet(OUTLET_GUI, "text", "clear");
	outlet(OUTLET_GUI, "text", "set", nodeAttributes.get(attribute));
}

function windowUnlock(){
	outlet(OUTLET_GUI, "window", "window", "flags", "zoom");
	outlet(OUTLET_GUI, "window", "window", "flags", "minimize");
	outlet(OUTLET_GUI, "window", "window", "flags", "grow");
	outlet(OUTLET_GUI, "window", "window", "flags", "nofloat");
	outlet(OUTLET_GUI, "window", "window", "exec");
	outlet(OUTLET_GUI, "window", "openinpresentation", "0");
	outlet(OUTLET_GUI, "window", "enablehscroll", "1");
	outlet(OUTLET_GUI, "window", "toolbarvisible", "1");
	outlet(OUTLET_GUI, "window", "enablevscroll", "1");
}

function windowLock(){
	outlet(OUTLET_GUI, "window", "window", "flags", "nozoom");
	outlet(OUTLET_GUI, "window", "window", "flags", "nominimize");
	outlet(OUTLET_GUI, "window", "window", "flags", "nogrow");
	outlet(OUTLET_GUI, "window", "window", "flags", "float");
	outlet(OUTLET_GUI, "window", "window", "exec");
	outlet(OUTLET_GUI, "window", "openinpresentation", "1");
	outlet(OUTLET_GUI, "window", "enablehscroll", "0");
	outlet(OUTLET_GUI, "window", "toolbarvisible", "0");
	outlet(OUTLET_GUI, "window", "enablevscroll", "0");
}

function dump(){
	post("undo ------------- \n");
	for(var i = 0; i < undoStore.length; i++){
		var undoStep = undoStore[i];
		var task = undoStep[1];
		if(undoStep[0] == UNDO_TYPE_PROPS )
			post("undo [" + i + "]: " + task[0] + " " + task[2] + " -> " + task[1] + "\n");
		if(undoStep[0] == UNDO_TYPE_NODENAME)
			post("undo [" + i + "]: node name change: " + task[2] + " -> " + task[1] + "\n");
	}
	post("redo ------------- \n");
	for(var i = 0; i < redoStore.length; i++){
		var redoStep = redoStore[i];
		var task = redoStep[1];
		if(redoStep[0] == UNDO_TYPE_PROPS)
			post("redo [" + i + "]: " + task[0] + " " + task[1] + " -> " + task[2] + "\n");
		if(redoStep[0] == UNDO_TYPE_NODENAME)
			post("redo [" + i + "]: node name change: " + task[1] + " -> " + task[2] + "\n");
	}

}

function relax(){
	var parentlevel = 0;
}
