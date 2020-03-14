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

/*

Javascript Node - Workhorse for VPL (Visual Programming Language)

*/

var myIOLetButtonSize = 10;
var myIOLetButtonShift = 2;

autowatch = 1;

var undefined = "undefined";

var myNodeBoxSize;
var myNodeDBPath;
var myNodeName = undefined;
var myNodeVarName = undefined;
var myNodeSpace = undefined;
var myNodeType = undefined;
var myNodeColorOn = new Array(1.0, 0.65, 0.0, 1.);
var myNodeColorOff = new Array(0.7, 0.7, 0.7, 0.8);
var myNodeColorSelected = new Array(0.74, 0.48, 0.69, 0.5);
var myNodeEnable = 0;
var myNodeSelected = 0;

// patcher arguments
if (jsarguments.length > 1){
    myNodeName = jsarguments[1];
	myNodeType = myNodeName.substring(0, myNodeName.indexOf("."));
}
if (jsarguments.length > 2){
    myNodeSpace = jsarguments[2];
}

var myColorTable = new Dict("vpl::colortable");
var myType2Color = new Dict("vpl::type2color");
var myBaseDB 	 = new Dict("vpl::db");

var connections = new Array();

var vpl_nodeBox;
var vpl_nodePatcher;
var vpl_nodeCanvas;
var vpl_canvas = new Array();
var vpl_nodeEnable;
var vpl_title;
var vpl_NodeSpacePatcher;

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 3;

//make it dragable even without init()

function init(){
	myNodeVarName = getKeyValuefromDB("_conn.id");

	initNodeSpace();
	initNode();
	initNodeBox();
	initConnections();

 	//post("nodebox initialized: " + myNodeName + ". sending nextlevel\n");
	outlet(0, "initnextlevel", "nodespace", getKeyValuefromDB("_nodespace") + "::" + myNodeVarName, getKeyValuefromDB("_level") + 1);
	myNodeInit = true;
	return true;
}

function initNodeSpace(){
	var owner = this.patcher.box;
	while (owner) {
		vpl_nodeBox = owner;
		var NodeSpaceThisPatcher = vpl_nodeBox.patcher.getnamed("vpl_ThisNodeSpacePatcher");
		if(NodeSpaceThisPatcher != null){
			vpl_NodeSpacePatcher = vpl_nodeBox.patcher;
			break;
		} else {
			owner = owner.patcher.box;
		}
	}
}

function initNode(){
	//Find  all the important objects in this patcher
	// and initialize them.
	if(vpl_nodePatcher == null){
		// tries to find a text object with the name "vpl_title"
		if(this.patcher.getnamed("vpl_title") != null){
			vpl_title = this.patcher.getnamed("vpl_title");
			//post(" has vpl_title... \n");
		}
		var client = this.patcher.box;
		while (client) {
			// tries to find a node enable object "vpl_enable"
			if(client.patcher.getnamed("vpl_enable") != null)
				vpl_nodeEnable = client.patcher.getnamed("vpl_enable");

			// tries to find a canvas object with the name "vpl_canvas"
			if(client.patcher.getnamed("vpl_canvas") != null){
				vpl_nodeCanvas = client.patcher.getnamed("vpl_canvas");
			 	//post(" has vpl_canvas... \n");
			}

			if(client.patcher.getnamed("vpl_ThisNodePatcher") != null){
				vpl_nodePatcher = client.patcher;
				break;
			} else {
				client = client.patcher.box;
			}
		}
	}
}

function initNodeBox(){
	//Find  all the important objects in this patcher
	// and initialize them.

	// calculates this vpl_nodeBox size
	if(vpl_nodeCanvas != null){
 		//post("canvas found 2: " + a.rect + "\n");
		myNodeBoxSize = vpl_nodeCanvas.rect;
		//post("node size  " + myNodeBoxSize[2] + ", " + myNodeBoxSize[3] + "\n");
		if(vpl_nodeBox != null && vpl_nodeBox.varname == myNodeVarName){
 			var myBoxRect = vpl_nodeBox.rect;
			myBoxRect[2] = myBoxRect[0] + myNodeBoxSize[2];
			myBoxRect[3] = myBoxRect[1] +  myNodeBoxSize[3] + myIOLetButtonSize / 2;
			vpl_nodeBox.rect = myBoxRect;
			storeKeyValueInDB("_rect", myBoxRect);
		}
		//adjust the title field length
		if(vpl_title != null){
			//var displayBox = vpl_title.rect;
			//displayBox[2] = myNodeBoxSize[2] - 60;
			//displayBox[3] = displayBox[3] - displayBox[1];
			//post(" found title textfiled: " + vpl_title.rect + " \n");
			vpl_title.message("presentation_rect", 28, 10,   myNodeBoxSize[2] - 60, 20.);
		}
	}else{
		post(" found no canvas \n");
	}

	if(myNodeBoxSize != null){
		var objects = vpl_nodePatcher.firstobject;
		while(objects != null){
			// find all bpatcher with the script name inlet and outlet and set
			// their postion according to their ids
 			if(objects.varname.indexOf("vpl_inlet") == 0){
				var xpos = getIOLetXPos(objects);
				vpl_nodePatcher.message("script", "sendbox", objects.varname, "presentation_rect", xpos, 0., myIOLetButtonSize, myIOLetButtonSize);
				var subpat = objects.subpatcher();
				if(subpat != null){
					subpat.apply(initIOlets);
					//post(" found subpatcher: " + subpat + "\n");
				}
			}else if(objects.varname.indexOf("vpl_outlet") == 0){
				var xpos = getIOLetXPos(objects);
				vpl_nodePatcher.message("script", "sendbox", objects.varname, "presentation_rect", xpos, myNodeBoxSize[3] + myNodeBoxSize[1] - myIOLetButtonSize + myIOLetButtonShift, myIOLetButtonSize, myIOLetButtonSize);
				var subpat = objects.subpatcher();
				if(subpat != null){
					subpat.apply(initIOlets);
					//post(" found subpatcher: " + subpat.box.varname + "\n");
				}
			}

			// find all vpl_canvas object in this node to set their colors.
 			if(objects.varname.indexOf("vpl_canvas") == 0){
				vpl_canvas.push(objects);
			}
			var subpat = objects.subpatcher();
			if(subpat != null){
				var subcanv = subpat.getnamed("vpl_canvas");
				if(subcanv != null)
					vpl_canvas.push(subcanv);
			}

			objects = objects.nextobject;
		}
	}

}

function initConnections(){
	var keys = getNodeKeysfromDB(myNodeName);
	//post("my keys: " + keys);
	connections = new Array();
	if(keys != null){
		for(var i = 0; i < keys.length; i++){
			if(keys[i].indexOf("_connection") != -1){
				var con = getKeyValuefromDB(keys[i]);
				connection(con[0], con[1], con[2], con[3], con[4], con[5]);
			}
		}
	}
}

function initIOlets(a) {
	//find all buttons with the script name inlet and outlet and set
	// their colors according to their type
	if(a.maxclass.indexOf("button") != -1 &&
		a.varname.indexOf("_with_type[") != -1){
		var localThisPatch = a.patcher;
		//post("found button: " + a.varname + "\n");
		var types = bracketEnclosure(a);
		var color = getColor(types);
		//post(" setting colors for buttons: " + a.varname + " for types " + types + "\n");
		if(color != null){
			//post("found color: " + color + "\n");
			var colormsg = myColorTable.get(color);
			//post("found message: " + colormsg + "\n");
			a.message("outlinecolor", colormsg[2], colormsg[3], colormsg[4], colormsg[5]);
//			a.message(colormsg[1], colormsg[2], colormsg[3], colormsg[4], colormsg[5]);
		}else{
			a.message("outlinecolor", .5, .5, .5, 1.);
//			a.message("fgcolor", .5, .5, .5, 1.);
		}

		localThisPatch.message("script", "sendbox", a.varname, "size", myIOLetButtonSize, myIOLetButtonSize);
	}

	// this is looking for trigger objects inside inlet and outlet
	// to set this nodename = nodeaddress
	if(a.varname.indexOf("vplconnectionaddress") == 0)
		a.message(myNodeVarName, myNodeSpace);
	if(a.varname.indexOf("vplnodeconnection") == 0)
		a.message(myNodeName, myNodeSpace);

	return true;
}

/**********************
  Call Functions
 **********************/


function refresh(){
	//post("refreshing now: " + myNodeName + "\n");
	initNodeBox();
	outlet(0, "refresh");
}

function exportlevel(exportspace, level){
	outlet(0, "exportlevel", exportspace + "::" + myNodeVarName,level + 1);
}

//Has no function at all...
function nodespace(wrksp){
	vpl_subPatcherNodeSpaceName = wrksp;
}


function title(newtitle){
	storeKeyValueInDB("_title", newtitle);
}

function color(red, green, blue, alpha){
	myNodeColorOn[0] = red;
	myNodeColorOn[1] = green;
	myNodeColorOn[2] = blue;
	myNodeColorOn[3] = alpha;
	setGUIColors();
}

// is called after the abstraction is loaded
function done(){
}

function msg_int(val){
	if(inlet == 0)
		if(val == 0 || val == 1)
			enable(val);
}

function enable(_enable){
	myNodeEnable = _enable;
	myNodeSelected = 0;
	setGUIColors();
}

// sets the type of node
function logo(_logo){
	outlet(2, "logo", "read", "bs.node.logo." + _logo + ".png");
}


function select(_select){
	myNodeSelected = (_select == 0 || (myNodeSelected == 1 && _select == 1))? 0: 1;
	//post("node: select() " + myNodeSelected + "\n");
	setGUIColors();
}

//Called by another selected inside the workspace
function dragselected(_nodename, _diffX, _diffY){
	if(myNodeSelected == 1 && myNodeName != _nodename){
		applydrag(_diffX, _diffY);
	}
}

//Called by a subpacth inside the node-patch
function drag(diffX, diffY){
	if(vpl_nodeBox != null){
		applydrag(diffX, diffY);

		if(myNodeSelected == 1){
			messnamed(myNodeSpace + "::vpl::nodespace", "dragselected", myNodeName, diffX, diffY);
		}
	}
}

// called by one of the drag functions
function applydrag(diffX, diffY){
	var myBoxRect = vpl_nodeBox.rect;
	myBoxRect[0] += diffX;
	myBoxRect[2] += diffX;
	myBoxRect[1] += diffY;
	myBoxRect[3] += diffY;
	vpl_nodeBox.rect = myBoxRect;
	storeKeyValueInDB("_rect", myBoxRect);
	vpl_NodeSpacePatcher.message("script", "bringtofront", vpl_nodeBox.varname);
}

function openworkspace(){
	if(vpl_NodeSpacePatcher != null)
		vpl_NodeSpacePatcher.message("front");
}

function dispose(){
//	initNode();
	outlet(1, "dispose");

	if(vpl_nodePatcher != 0)
		vpl_nodePatcher.message("dispose");
}

//Called when the parent node was deleted
function notifydeleted(){
	deletenodeFromDB(myNodeName);
	messnamed(myNodeSpace + "::vpl::nodespace", "removeConnection", myNodeName);
 	//post(myNodeName + " got deleted\n");
}


/**********************
  Managing GUI / Colors
 **********************/

function setGUIColors(){
	initNode();

	var workingcolor = myNodeColorOn;
	if(myNodeEnable == 0)
		workingcolor = myNodeColorOff;
	if(myNodeSelected == 1)
		workingcolor = myNodeColorSelected;

	// sets the color pf all vpl_canvas objects within the node
	if(vpl_canvas instanceof Array){
		for(var i = 0; i < vpl_canvas.length; i++){
//			vpl_canvas[i].message("bordercolor", 0., 0., 0., 1.);
//			vpl_canvas[i].message("borderoncolor", 1., 1., 1., 1.);
//			vpl_canvas[i].message("bgovercolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]- 0.05 );
			vpl_canvas[i].message("bgoncolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
			vpl_canvas[i].message("bgcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
		}
	}else{
//		vpl_canvas.message("bordercolor", 0., 0., 0., 1.);
//		vpl_canvas.message("borderoncolor", 1., 1., 1., 1.);
//		vpl_canvas.message("bgovercolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]- 0.05 );
		vpl_canvas.message("bgoncolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
		vpl_canvas.message("bgcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
	}

	if(vpl_nodeEnable != null){
//		vpl_nodeEnable.message("bordercolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]- 0.05 );
	}

	if(vpl_title != null){
//		vpl_title.message("bordercolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]- 0.05 );
		// setting the hint message
		vpl_title.message("hint", myNodeType + " - node");
	}
}

/**********************
  Managing Connections
 **********************/

function connection(outaddress, outid, outtype, inid, intype, inmaxcon){
 	//post("Connection attmpt: outaddress " + outaddress + " | outid " + outid + " \n");
	var newConn = new Connection(outaddress, outid, outtype, inid, intype, inmaxcon);
	if(compareTypes(outtype, intype)){ // if the types are compatible
		var removedConnection = false;
		// if there are more than one connections allowed...
		if(inmaxcon > 1){
			// ..see if there is already an identical connection..
			for(var i = 0; i < connections.length; i++){
				if(connections[i].inid == newConn.inid &&
					connections[i].outaddress == newConn.outaddress &&
					connections[i].outid == newConn.outid){
					// ..and this connection already exists, then delete
					// this connection.

					removeBoxConnection(connections[i]);
					connections[i] = null;
					removedConnection = true;
					//post("remove connection " + inid + "\n");
				}
			}
		}
		if(removedConnection){
			refreshConnections();
		} else {
			// if there was no identical connection then count the connections
			// to this inlet
			var countConnections = 0;
			for(var i = 0; i < connections.length; i++)
				if(connections[i].inid == newConn.inid)
					countConnections++

			if(countConnections == inmaxcon){ // if they have reached max connections
				// first remove the first connection if the same id
				for(var i = 0; i < connections.length; i++){
					if(connections[i].inid == newConn.inid){
						// remove the old nodebox connections
						removeBoxConnection(connections[i]);
						// new connection replaces the old connection
						connections[i] = newConn;
						// create the nodebox connections
						makeBoxConnection(newConn);
						//store the connection inside the database
						storeConnectionInDB(i, newConn);
						break;
					}
				}
			} else {
				var newIndex = connections.length;
				connections.push(newConn);
				// create the nodebox connections
				makeBoxConnection(newConn);
				// store the connecion inside the database
				storeConnectionInDB(newIndex, newConn);
			}
		}
	}
}

//Called by nodes that might be connected to this node via their outlets. Since
// the connections are in the responsibility of the node with the inlets, it needs
// to know when a node in the same NodeSpace was deleted in case it has some
// connections with it
function removeConnection(outaddress){
	if(outaddress != myNodeName){
		var removedConnection = false;
		for(var i = 0; i < connections.length; i++){
			if(connections[i].outaddress == outaddress){
				removeBoxConnection(connections[i]);
				connections[i] = null;
				removedConnection = true;
			}
		}
		if(removedConnection)
			refreshConnections();
	}
}

function disconnect(inid){
	var removedConnection = false;
	for(var i = 0; i < connections.length; i++){
		if(connections[i].inid == inid){
			removeBoxConnection(connections[i]);
			connections[i] = null;
			removedConnection = true;
		}
	}
	if(removedConnection)
		refreshConnections();
}

function disconnectaddress(outaddress, outid){
	var removedConnection = false;
	for(var i = 0; i < connections.length; i++){
		if(connections[i].outaddress == outaddress &&
			connections[i].outid == outid){
			removeBoxConnection(connections[i]);
			connections[i] = null;
			removedConnection = true;
		}
	}
	if(removedConnection)
		refreshConnections();
}

function refreshConnections(){
	var newConnections = new Array();
	for(var i = 0; i < connections.length; i++){
		removeKeyValuefromDB("_connection_" + i);
		if(connections[i] != null)
			newConnections.push(connections[i]);
	}
	connections = newConnections;
	for(var i = 0; i < connections.length; i++){
		storeConnectionInDB(i, connections[i]);
	}
}

function storeConnectionInDB(index, conn){
	storeKeyValueInDB("_connection_" + index, new Array(conn.outaddress, conn.outid, conn.outtype, conn.inid, conn.intype, conn.inmaxcon));
}

function makeBoxConnection(conn){
	vpl_NodeSpacePatcher.message("script", "connectcolor", conn.outaddress, conn.outid-1, vpl_nodeBox.varname, conn.inid-1, getColorID(conn.intype));
}

function removeBoxConnection(conn){
	vpl_NodeSpacePatcher.disconnect(vpl_NodeSpacePatcher.getnamed(conn.outaddress), conn.outid-1, vpl_nodeBox, conn.inid-1);
}

function Connection(outaddress, outid, outtype, inid, intype, inmaxcon){
	this.outaddress = outaddress;
	this.outid = outid;
	this.outtype = outtype;
	this.inid = inid;
	this.intype = intype;
	this.inmaxcon = inmaxcon;
}

/*******************
 Utility functions
 *******************/

function getIOLetXPos(a){
	var indexes = bracketEnclosure(a).split("/");
	var index = indexes[0];
	var total = indexes[1]; //9.5
	//post("inlet position index: " + index + "/total: " + total + "\n");
	var dividable = myNodeBoxSize[2] - 18; //3.5 = xpos for index 1
	var steps = dividable / (total - 1);
	return 10 + (index - 1) * steps - myIOLetButtonSize/2;
}

function getColor(types){
	var typeArray = types.split("_");
	if(typeArray.length > 0){
			//post("use type: >" + typeArray[0] + "< \n");
		var color = myType2Color.get(typeArray[0]);
		return color;
	}
	return null;
}

function getColorID(types){
	var color = getColor(types);
	var colorid = 0;
	if(color != null){
		var colormsg = myColorTable.get(color);
		colorid = colormsg[0];
	}
	return colorid;
}

function bracketEnclosure(a){
	return a.varname.substring(a.varname.indexOf("[") + 1, a.varname.indexOf("]"));
}

function compareTypes(outtype, intype){
	if(intype == 0 || intype.indexOf("type") != -1)
		return true;
	if(outtype == 0 || outtype.indexOf("type") != -1)
		return true;
	var outtypeArray = outtype.split("_");
	var intypeArray = intype.split("_");
	for(var i = 0; i < outtypeArray.length; i++){
		for(var j = 0; j < intypeArray.length; j++){
			//post("compare type o: " + outtypeArray[i] + " with i:" + intypeArray[j] + "\n");
			if(outtypeArray[i].indexOf(intypeArray[j]) != -1)
				return true;
		}
	}
	return false;
}


/********************
DATABASE Functions
*********************/

function removeKeyValuefromDB(key){
	if(hasDBAccess()){
 		if(myBaseDB.contains(getNodeKeyPath(key)))
			myBaseDB.remove(getNodeKeyPath(key));
	}
}

function storeKeyValueInDB(key, value){
	if(hasDBAccess()){
 		if(myBaseDB.contains(getNodeKeyPath(key)))
			myBaseDB.set(getNodeKeyPath(key), value);
		else
			myBaseDB.replace(getNodeKeyPath(key), value);
	}
}

function getKeyValuefromDB(key){
	if(hasDBAccess()){
 		if(myBaseDB.contains(getNodeKeyPath(key)))
			return myBaseDB.get(getNodeKeyPath(key));
	}
	return null;
}


function getNodeKeyPath(key){
	return myNodeType + "::" + myNodeName + "::" + key;
}

function getNodeKeysfromDB(){
	var nodeTypeDB = myBaseDB.get(myNodeType);
	//post("nodes: " +typeDB.getkeys() + "\n");
	if(nodeTypeDB != null && nodeTypeDB.contains(myNodeName)){
		return nodeTypeDB.get(myNodeName).getkeys();
	}
}

function hasDBAccess(){
	var nodeTypeDB = myBaseDB.get(myNodeType);
	//post("nodes: " +typeDB.getkeys() + "\n");
	if(nodeTypeDB != null && nodeTypeDB.contains(myNodeName))
		return true;
	return false;
}

//Called by notifydeleted()
function deletenodeFromDB(){
	//post("delete note: " +nodename + "\n");
	if(hasDBAccess()){
		var typeDB = myBaseDB.get(myNodeType);
		var node = typeDB.get(myNodeName);
		node.clear();
		typeDB.remove(myNodeName);
		myBaseDB.set(myNodeType,typeDB);
	}
}
