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

Javascript Subnode connector for VPL (Visual Programming Language)

*/

autowatch = 1;

var undefined = "undefined";
var myNodeName = undefined;
var myNodeType = undefined;
var myNodeConn = undefined;
var myVarName = undefined;

// patcher arguments
if (jsarguments.length > 2){
	myNodeName = jsarguments[1];
	myNodeType = myNodeName.substring(0, myNodeName.indexOf("."));
    myNodeConn = jsarguments[2];
}

var vpl_nodeBox;
var vpl_workspacePatcher;
var vpl_workspaceThisPatcher;
var vpl_clientpatcher;
var vpl_clientThisPatch;

var myBaseDB 	 = new Dict("vpl::db");

var vpl_subPatcherWorkSpaceName = undefined;

initWorkspace();

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 1;

function initWorkspace(){
	var owner = this.patcher.box;
	while (owner) {
		vpl_nodeBox = owner;
		vpl_workspaceThisPatcher = vpl_nodeBox.patcher.getnamed("vpl_ThisNodeSpacePatcher");
		if(vpl_workspaceThisPatcher != null){
			vpl_workspacePatcher = vpl_nodeBox.patcher;
    		myVarName = owner.varname;
			break;
		} else {
			owner = owner.patcher.box;
		}
	}
}

function init(){
	initWorkspace();

	// find all bpatcher with the script name inlet and outlet and set
	// their postion according to their ids
	if(vpl_workspaceThisPatcher != null){
		if(myNodeConn == "inlets"){
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", "vpl_inlet_2", 0, myVarName, 1);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", "vpl_inlet_3", 0, myVarName, 2);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", "vpl_inlet_4", 0, myVarName, 3);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", "vpl_inlet_5", 0, myVarName, 4);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", "vpl_inlet_6", 0, myVarName, 5);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", "vpl_inlet_7", 0, myVarName, 6);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", "vpl_inlet_8", 0, myVarName, 7);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", "vpl_inlet_9", 0, myVarName, 8);

			vpl_workspaceThisPatcher.message("script", "hidden", "connect", myVarName, 9, "vpl_outlet_9", 0);
		}

		if(myNodeConn == "outlets"){
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", "vpl_inlet_2", 0, myVarName, 0);

			vpl_workspaceThisPatcher.message("script", "hidden", "connect", myVarName, 1, "vpl_outlet_2", 0);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", myVarName, 2, "vpl_outlet_3", 0);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", myVarName, 3, "vpl_outlet_4", 0);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", myVarName, 4, "vpl_outlet_5", 0);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", myVarName, 5, "vpl_outlet_6", 0);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", myVarName, 6, "vpl_outlet_7", 0);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", myVarName, 7, "vpl_outlet_8", 0);
			vpl_workspaceThisPatcher.message("script", "hidden", "connect", myVarName, 8, "vpl_outlet_9", 0);
		}
	}

	outlet(0, "iolets", getKeyValuefromDB("_iolets"));
	return true;
}

function setiolets(number){
	storeKeyValueInDB("_iolets", number);
}

/********************
DATABASE Functions
*********************/

function storeKeyValueInDB(key, value){
 	if(myBaseDB.contains(getNodeKeyPath(key)))
		myBaseDB.set(getNodeKeyPath(key), value);
	else
		myBaseDB.replace(getNodeKeyPath(key), value);
}

function getKeyValuefromDB(key){
 	if(myBaseDB.contains(getNodeKeyPath(key)))
		return myBaseDB.get(getNodeKeyPath(key));
	return 0;
}


function getNodeKeyPath(key){
	return myNodeType + "::" + myNodeName + "::" + key;
}
