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
setinletassist(0,"load");
setinletassist(1,"add");
setoutletassist(0,"createbox");
setoutletassist(1,"dump");

var LEVEL_OFF = -1;
var LEVEL_UNREG = 0;
var LEVEL_FREE = 1;
var LEVEL_BASIC = 3;
var LEVEL_MASTER = 3;
var LEVEL_PRO = 7;
var LEVEL_DEV = 9;

var vpl_subNode = "bs.vpl.node.util.subnode.1";
var vpl_subNodeType = "subnode";

// space::nodes::node::space::
var outlet_dump = 1;

var baseDB = null;

var filename;
var counter = 0;

var vpl_nodeBox;
var vpl_nodeBoxRect = new Array(300, 300, 500, 500);
var vpl_NodeSpacePatcher;
var vpl_NodeSpaceName = "vpl_NodeSpace_undefined";
var vpl_NodeSpaceLevel = 0;

var	nodeAccess;
var accessLevel;

var callBackParentBuilder = null;

var updateDeprecatedNodes = false;

var _globalBuilder = new Global("_globalBuilder");

var appGlobal = new Global("bs::app::global");
// set the debugger : 0=off, 1=all, 2=app, 3=io, 4=db, 5=node, 6=select
var debug = (appGlobal.debug == 1 || appGlobal.debug == 4)? true:false;

// set the debugger
function debugger(_val){
	debug = (_val == 1 || _val == 4)? true: false;
}

function dpost(_message){
    if(debug){
        post("bs.vpl.db: " + _message + " \n");
    }
}

function clearGlobalReport(){
    appGlobal.globalReport = [];
}

// Carefull: only special characters - ! + ? ( ) are allowed
function addGlobalReport_Info(_title, _msg, _fix, _node){
    appGlobal.globalReport.push(_title + "|" + _msg + "|" + _fix + "|" + _node + "|info");
    dpost(_title + "|" + _msg + "|" + _fix + "|info")
}

// Carefull: only special characters - ! + ? ( ) are allowed
function addGlobalReport_Warning(_title, _msg, _fix, _node){
    appGlobal.globalReport.push(_title + "|" + _msg + "|" + _fix + "|" + _node + "|warning");
    dpost(_title + "|" + _msg + "|" + _fix + "|warning")
}

// Carefull: only special characters - ! + ? ( ) are allowed
function addGlobalReport_Error(_title, _msg, _fix, _node){
    appGlobal.globalReport.push(_title + "|" + _msg + "|" + _fix + "|" + _node + "|error");
    dpost(_title + "|" + _msg + "|" + _fix + "|error")
}

// Carefull: only special characters - ! + ? ( ) are allowed
function addGlobalReport_Fatal(_title, _msg, _fix, _node){
    appGlobal.globalReport.push(_title + "|" + _msg + "|" + _fix + "|" + _node + "|fatal");
    dpost(_title + "|" + _msg + "|" + _fix + "|fatal")
}

function sendGlobalReport(){
    if(appGlobal.globalReport.length > 0){
        for(var i = 0; i < appGlobal.globalReport.length; i++){
            outlet(1, "nodeCreation", appGlobal.globalReport[i]);
        }
        outlet(1, "nodeCreation", "done");
        dpost("send global report");
    }
}

function loadbang(){
    nodeAccess = new Dict("bs::app::nodes");
}

// this method is called when a parent nodespace initializes its children nodes
function init(task){
    dpost("init new nodespace: " + vpl_NodeSpaceName);

	accessLevel = nodeAccess.get("_level");
    updateDeprecatedNodes = (appGlobal.updateDeprecatedNodes == 1)? true: false;
    callBackParentBuilder = _globalBuilder.callBackObject;

    initNodeSpace();
	initDB();
	if(task == "reset"){
		clear();
		clearDB();
	}

	return true;
}

// this message is called by the main patcher
function read(path){
	accessLevel = nodeAccess.get("_level");
    updateDeprecatedNodes = (appGlobal.updateDeprecatedNodes == 1)? true: false;
    _globalBuilder.callBackObject = null;

    messnamed("bs::app::gui::progress", "text", "setting up ...");
 	initDB();
	clear();
	clearDB();
    clearGlobalReport();

	baseDB.readany(path);
	if(!build()){
        // an error during the build occured. better clean up things...
        clear(); // clear the nodespace
        clearDB(); //clear the database
    }
	return true;
}

// this message is called by the main patcher
function saveas(path){
    baseDB.quiet = true;
    messnamed("bs::app::gui::progress", "text", "... in progress ...");
    clearGlobalReport();

	refresh();
	//baseDB.export_json(path);
	if(!writefile(path, baseDB.stringify())){
        sendGlobalReport();//writeProject -> used inside bs.info.node.creation.warning
    } else {
        outlet(0, "done", "saveas");
    }
    post("...saved project\n");
    baseDB.quiet = false;
}

// this method is called by saveas()
function writefile(_path, _string){
	var f = new File(_path,"write","TEXT");

	if (f.isopen) {
		post("saving project: " + _path + "\n");
        f.eof = 0;
		f.writestring(_string); //writes a string
//		post("ended at position: " + f.position + "\n");
		f.close();
	} else {
        addGlobalReport_Error("Saving project", "could not create file: " + _path, "check if you have write access inside this folder")
        return false;
	}
    return true;
}

function initDB(){
	baseDB = new Dict("vpl::db");
	baseDB.set("_enable", 1);
}

function initNodeSpace(){
	var owner = this.patcher.box;
	while (owner) {
		vpl_nodeBox = owner;
		if(vpl_nodeBox.patcher.getnamed("vpl_ThisNodeSpacePatcher")){
			vpl_NodeSpacePatcher = vpl_nodeBox.patcher;
			break;
		} else {
			owner = owner.patcher.box;
		}
	}
}

// called by the subnode that contains this sup-nodespace upon the init message sent by its parent nodespace
function nodespace(wrkspc, level){
	dpost("nodespace received: " + wrkspc + " / " + level + ". make nodespace\n");
	vpl_NodeSpaceName = wrkspc;
	vpl_NodeSpaceLevel = level;
}


// helper vars for duplicating subnodes
var duplArray;
var connDict;
var nodeIDDict;

// called by shortcut on cmd-d
// will duplicate the selected node inside the same workspace
function duplicate(posX, posY){
    if(appGlobal.selectedNode != null){
        if(appGlobal.selectedNode[0] == vpl_NodeSpaceName){
            //dpost(vpl_NodeSpaceName + " / " + vpl_NodeSpaceLevel + " - duplicate");
            dpost("attempting to duplicate: " + appGlobal.selectedNode)
            if(vpl_subNodeType == appGlobal.selectedNode[3]){
                duplArray = new Array();
                connDict = new Dict();
                nodeIDDict = new Dict();
                
                // get all the nodes that need to be duplicated
                duplicateSubNode(appGlobal.selectedNode[1], null, vpl_NodeSpaceName);

                // add them to the db
                
                // but first clean up the connections
                for(var i = 0; i < duplArray.length; i++){
                    // first correct the connections
                    var keys = duplArray[i].getkeys();
                    if(keys != null){
                        for(var k = 0; k < keys.length; k++){
                            if(keys[k].indexOf("_connection") != -1){
                                var con = duplArray[i].get(keys[k]);
                                var newNode = connDict.get(con[0]);
                                if(newNode == null){ 
                                    // this means its a connection of the node to be duplicated
                                    // resp. a node that is connected with a node that is not duplicated
                                    duplArray[i].remove(keys[k]); // remove the connection
                                    dpost("removed connection:"  + con[0]);
                                } else {
                                    con[0] = newNode;
                                    duplArray[i].set(keys[k], con);
                                    dpost("changed connection:"  + newNode);
                                }
                            }
                        }
                    }

                    // add it to the db
                    var nodeType = duplArray[i].get("_type");
                    var nodeSpaceName = duplArray[i].get("_nodespace");
                    var nodeLevel = duplArray[i].get("_level");
                    var nodeTypeDB = getNodeTypeDB(nodeType);

                    dpost("try add to db: "  + nodeType);
                    dpost("  - nodeSpaceName: "  + nodeSpaceName);
                    dpost("  - nodeLevel: "  + nodeLevel);
                    if(creationCheck(nodeTypeDB, nodeType, -1, undefined, nodeSpaceName, nodeLevel, 0)){
                        dpost("Can add to db: "  + nodeType);
                        //... and add it to the type-dict...
                        addNodeBox2NodeType(duplArray[i], nodeTypeDB);
                        //... and add it to the db.
                        addNodeType2NodeDB(nodeType, nodeTypeDB);
                    } else {
                        sendGlobalReport(); //nodeCreation -> used inside bs.info.node.creation.warning
                    }

                    //dpost("save to file: "  + duplArray[i].get("_name"));
                    //duplArray[i].export_json("/Users/maybites/Desktop/test/" + duplArray[i].get("_name"));
                }

                // and last but not least
                // we take the rootSubNode and..
                var rootSubNode = duplArray[duplArray.length - 1];
                // set it to the mousepointer...
                rootSubNode.set("_rect", new Array(posX, posY, posX + 200, posY + 200));
                //send the creation message to the patcher-object...
                vpl_NodeSpacePatcher.message(makeCreationMessage(rootSubNode));
                //...wait 30 ms...
                pausecomp(30);
                //...and send an init message to the new object.
                //dpost("send to " + nodeBoxDB.get("_name")+"::vpl::connection: init()\n");

                // prepare the globalBuilder vars
                _globalBuilder.counterBuild = 0;
                _globalBuilder.counterInit = 0;
                _globalBuilder.numberOfNodeBoxes = 0;
                _globalBuilder.nodeSpaceLevel = rootSubNode.get("_level") + 1;
                
                // initialize the subnode. this will utlimately call the build() function and 
                // build all the nodes that are contained by this subnode.
                messnamed(rootSubNode.get("_name")+"::vpl::connection", "init");

            } else {
                // all other nodes are much easier
                create(appGlobal.selectedNode[3], posX, posY)
            }
        }
    }
}

// this function is iteratively called to get all the nodes that need to be duplicated inside a subnode
// it fills up
//   duplArray, which contains the dictionaries with the new node information
//   connDict, which contains the old and new node names to help update the connections
function duplicateSubNode(subnode, orig_nsName, new_nsName){
    dpost("duplicateSubNode: " + orig_nsName + " to " + new_nsName);

    var nodeTypes = baseDB.getkeys();
    // Creating the list (buildarray) of nodes to be build inside this nodespace

	//dpost("building nodespace: " +vpl_NodeSpaceName + "\n");
	if(nodeTypes != null){
		//dpost("types: " +nodeTypes + "\n");
        // Find all the nodes inside this workspace
		for(var i = 0; i < nodeTypes.length; i++){
			//dpost("build() node types: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				var typeDB = baseDB.get(nodeTypes[i]);
				dpost("build() type: " + nodeTypes[i] + "\n");
				var nodeBoxes = typeDB.getkeys();
				dpost("build() nodeBox keys: " + nodeBoxes + "\n");
				if(nodeBoxes != null){
					for(var j = 0; j < nodeBoxes.length; j++){
						if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
							var nodeBoxDB = typeDB.get(nodeBoxes[j]);
                            // then we look only at the nodes inside this nodespace, check them and add them to the buildArray.
                            if(nodeBoxDB.get("_name") == subnode || nodeBoxDB.get("_nodespace") == orig_nsName){
                                var thisNodeType = nodeBoxDB.get("_type");
                                var thisNodeID = nodeBoxDB.get("_id");
                                var thisNodeName = nodeBoxDB.get("_name");
                                var thisNodeSpace = nodeBoxDB.get("_nodespace");

                                var newNodeID;

                                // since we don't immediately store the new node, getUniqueNodeIndex() will
                                // always return the next index of the currently existing nodes. but we might
                                // have already had a node of this type, so we have to make sure we don't use
                                // the same index twice
                                
                                if(nodeIDDict.contains(thisNodeType)){
                                    newNodeID = nodeIDDict.get(thisNodeType) + 1;
                                } else {
                                    newNodeID = getUniqueNodeIndex(typeDB, thisNodeType);
                                }
                                nodeIDDict.set(thisNodeType, newNodeID);

                                var newNodeName = makeNodeName(thisNodeType, newNodeID);

                                connDict.set(thisNodeName, newNodeName);

                                nodeBoxDB.set("_id", newNodeID);
                                nodeBoxDB.set("_nodespace.idx", newNodeID);
                                nodeBoxDB.set("_name", newNodeName);
                                
                                //nodeBoxDB.set("_title", newNodeName); 
                                // we better don't set the title, sparck will attempt to 
                                // rename the node on initialization, since identical titles are
                                // not allowed in SPARCK - 
                                //   because the title also works as the varname of the node abstraction
                                
                                if(thisNodeType == vpl_subNodeType){
                                    dpost("duplicateSubNode: " + thisNodeName);

                                    var o_nsSpace = thisNodeSpace+"::"+thisNodeID;
                                    var n_nsSpace = new_nsName+"::"+newNodeID;

                                    duplicateSubNode(null, o_nsSpace, n_nsSpace);
                                }

                                if(new_nsName != null)
                                    nodeBoxDB.set("_nodespace", new_nsName);

                                duplArray.push(nodeBoxDB);

                            }
						}
					}
				}
			}
		}
 	}
}

// called when a new node is user created
function create(_nodeType, posX, posY){
 	accessLevel = nodeAccess.get("_level");
    dpost(vpl_NodeSpaceName + " - create node");
    clearGlobalReport();

    //tries to find a canvas object with the name "vpl_createfield"
	//and sets the new creation rectangle at that position
	var canvas = vpl_NodeSpacePatcher.getnamed("vpl_createfield");
	if(canvas != null && posX == null)
		vpl_nodeBoxRect = canvas.rect;
	else if(posX != null && posY != null)
		vpl_nodeBoxRect = new Array(posX, posY, 220, 150);

	if (inlet==0){ // if the message comes into the second inlet...

        // check first if this node exists
        if(hasNode_access(_nodeType)){
            var nodeTypeDB = getNodeTypeDB(_nodeType);

            //check if all creation tests are passed
            if(creationCheck(nodeTypeDB, _nodeType, -1, undefined, vpl_NodeSpaceName, vpl_NodeSpaceLevel, 0)){
                //get a unique node indec

                var nameindex = getUniqueNodeIndex(nodeTypeDB, _nodeType);
                //post("nameindex: " + nameindex + "\n");

                //create a new nodeBox dictionary...
                var nodeBoxDB = createNodeBox(_nodeType, nameindex);
                //... and add it to the type-dict...
                addNodeBox2NodeType(nodeBoxDB, nodeTypeDB);
                //... and add it to the db.
                addNodeType2NodeDB(_nodeType, nodeTypeDB);

                //send the creation message to the patcher-object...
                vpl_NodeSpacePatcher.message(makeCreationMessage(nodeBoxDB));
                //...wait 30 ms...
                pausecomp(30);
                //...and send an init message to the new object.
                //post("send to " + nodeBoxDB.get("_name")+"::vpl::connection: init()\n");

                messnamed(nodeBoxDB.get("_name")+"::vpl::connection", "init");

                outlet(0, "done", "createnode");
            } else {
                sendGlobalReport(); //nodeCreation -> used inside bs.info.node.creation.warning
            }
        } else {
            error("ERROR: Unable to create '" + _nodeType + "' - node. Please contact developer if this error appears.")
        }

    }

}

/**
function import(path){
    clearGlobalReport();

	var nodeTypeDB = getNodeTypeDB("importnode");

	var nameindex = getUniqueNodeIndex(nodeTypeDB, "importnode");
	//create a new nodeBox dictionary...
	var nodeBoxDB = createNodeBox("importnode", nameindex);
	//... and add it to the type-dict...
	addNodeBox2NodeType(nodeBoxDB, nodeTypeDB);
	//... and add it to the db.
	addNodeType2NodeDB("importnode", nodeTypeDB);

	var creationmessage = makeCreationMessage(nodeBoxDB);
	var uniqueNodeSpaceIndex = nodeBoxDB.get("_nodespace.idx");

//	post("import node creationmessage: " + creationmessage + "\n");
//	post("import node nodespace.idx: " + uniqueNodeSpaceIndex + "\n");

	importDB = new Dict("vpl::export");
	importDB.clear();
	importDB.import_json(path);
	var keys = importDB.getkeys();
	for(var i = 0; i < keys.length; i++){
		var imptnode = importDB.get(keys[i]);

		var nodeType = keys[i];
//		post("import node args: " + args + "\n");

		nodeTypeDB = getNodeTypeDB(nodeType);
		var newNodeSpaceName =  vpl_NodeSpaceName + "::" + uniqueNodeSpaceIndex + imptnode.get("_nodespace");
		var newNodeSpaceLevel = vpl_NodeSpaceLevel + imptnode.get("_level") + 1;
		if(creationCheck(nodeTypeDB, nodeType, -1, nodeName, newNodeSpaceName, newNodeSpaceLevel, 0)){
            //check if all creation tests are passed
//			post("creation check passed. newNodeSpaceName=" + newNodeSpaceName + " | newNodeSpaceLevel="+newNodeSpaceLevel+"\n");
			//get a unique node indec
			nameindex = getUniqueNodeIndex(nodeTypeDB, nodeType);
			//adapt the imported dictionary...
			imptnode.set("_id", nameindex);
			imptnode.set("_name", makeNodeName(nodeType,nameindex));
			imptnode.set("_nodespace", newNodeSpaceName);
			imptnode.set("_level", newNodeSpaceLevel);
			//... and add it to the type-dict...
			addNodeBox2NodeType(imptnode, nodeTypeDB);
			//... and add it to the db.
			addNodeType2NodeDB(nodeType, nodeTypeDB);
		} else {
             sendGlobalReport();//importNodes -> used inside bs.info.node.creation.warning
        }
	}

	//send the creation message to the patcher-object...
	vpl_NodeSpacePatcher.message(creationmessage);
	//...wait 30 ms...
	pausecomp(30);
	//...and send an init message to the new object.
	messnamed(nodeBoxDB.get("_name")+"::vpl::connection", "init");
}


function export(path){
	exportDB = new Dict("vpl::export");
	exportDB.clear();
	exportlevel("", 0);
	exportDB.export_json(path);
}
**/

function makeCreationMessage(nodeBox){
	var msp = new Array();
	msp.push("script");
	msp.push("newobject");
	msp.push("bpatcher");
    msp.push(getNode_fileName(nodeBox.get("_type")) + "." + nodeBox.get("_version"));
    msp.push("@patching_rect");
	var rectangle = nodeBox.get("_rect");
	msp.push(rectangle[0]);
	msp.push(rectangle[1]);
	msp.push(rectangle[2]);
	msp.push(rectangle[3]);
	msp.push("@args");
	msp.push("nodename");
	msp.push(nodeBox.get("_name"));
	msp.push("nodespace");
	msp.push(nodeBox.get("_nodespace"));
	msp.push("@varname");
	msp.push(nodeBox.get("_title"));
    //dpost("makeCreationMessage() " + msp + "\n");
	return msp;
}

// creates the entry of the node inside the db
function createNodeBox(_nodeType, _nodeidx){
	var nodeBoxDB = new Dict("vlp::nodebox::" + makeNodeName(_nodeType,_nodeidx));
	nodeBoxDB.set("_type", _nodeType);
	nodeBoxDB.set("_version", getNode_version(_nodeType));
	nodeBoxDB.set("_id", _nodeidx);
	nodeBoxDB.set("_name", makeNodeName(_nodeType,_nodeidx));
	nodeBoxDB.set("_title", makeNodeName(_nodeType, _nodeidx));
	nodeBoxDB.set("_nodespace", vpl_NodeSpaceName);
	nodeBoxDB.set("_level", vpl_NodeSpaceLevel);
	nodeBoxDB.set("_rect", vpl_nodeBoxRect);
	nodeBoxDB.set("_nodespace.idx", _nodeidx);
	//nodeBoxDB.set("_arguments", args);
	return nodeBoxDB;
}

// creates the new node name
function makeNodeName(_nodeType, _nodeidx){
	return _nodeType + "_" + _nodeidx;
}


function addNodeType2NodeDB(_nodeType, _nodeTypeDB){
	baseDB.set(_nodeType, _nodeTypeDB);
}

function addNodeBox2NodeType(_nodeBoxDB, _nodeTypeDB){
	_nodeTypeDB.set(_nodeBoxDB.get("_name"), _nodeBoxDB);
	_nodeTypeDB.set("_type", _nodeBoxDB.get("_type"));
}

function getNodeTypeDB(_nodetype){
	var types = baseDB.getkeys();

	if(types == null || types.indexOf(_nodetype) == -1)
	 	return new Dict("vlp::nodetype::" + _nodetype);
	else
		return baseDB.get(_nodetype);
}

function getUniqueNodeIndex(_nodeTypeDB, _nodeType){
	var nodes = _nodeTypeDB.getkeys();
	var nameindex = 1;

	//post("getUniqueNodeIndex. nodes: " +nodes + "\n");

	if(nodes != null){
		while(nodes.indexOf(makeNodeName(_nodeType,nameindex)) != -1){
			nameindex++;
		}
	}
	return nameindex;
}

// creation check is looking at the node database and validates if
//  - the requested node is created in the correct level
//  - the requested node is not created more than the maximal global requiremens
//  - the requested node is not created more than the maximal subnode requirements
// with validation = 1 it checks if the node db is valid for this node
function creationCheck(nodeTypeDB, _nodeType, _nodeVersion, _nodeName, _spacename, _spacelevel, _validation){
 //	post("enter creation check for nodeType : " + _nodeType + " | _spacename : " + _spacename + " | _spacelevel : "+_spacelevel+" | _validation : "+_validation+"\n");
    if(hasNode_access(_nodeType)){
//		post("...hasNode_access...\n");
        if(_nodeVersion != -1){
            if(getNode_version(_nodeType) != 0 && getNode_version(_nodeType) < _nodeVersion){
                addGlobalReport_Error("["+ _nodeType + "]: version unavailable","You try to load a project that was created with a newer version of SPARCK","Update SPARCK and try again");
                return false;
            } else if(getNode_version(_nodeType) > _nodeVersion || getNode_version(_nodeType) == 0){
                if(updateDeprecatedNodes){
                    addGlobalReport_Info("["+ _nodeType + "]: updated","Your project was updated from a deprecated version of the ["+ _nodeType + "]-node","To make sure your project is working flawlessly, check this node and see if the node is setup properly", _nodeName);
                } else {
                    addGlobalReport_Warning("["+ _nodeType + "]: deprecated","Your project is using a deprecated version of the ["+ _nodeType + "]-node","To make sure your project is working flawlessly, create a new node of the same type and delete this node (indicated by a thick red frame)", _nodeName);
               }
            }
        }
        if(getNode_branchLevel(_nodeType) == -1 ||
            getNode_branchLevel(_nodeType) == _spacelevel ||
            (getNode_branchLevel(_nodeType) == 1 && _spacelevel > 1)){

//			post("...hasBranchLevel_access..\n");

            var nodes = nodeTypeDB.getkeys();
            if(nodes != null){
 //             post("...got Keys..\n");
				var counterMax = 0;
                var counterSub = 0;
				//counting the already existing nodes in the global- and the sub-nodespaces
                for(var i = 0; i < nodes.length; i++){
                    if(nodes[i].indexOf("_") != 0 && nodeTypeDB.contains(nodes[i])){
                        nodeBox = nodeTypeDB.get(nodes[i]);
                        counterMax++;
                        if(nodeBox.get("_nodespace") == _spacename)
                            counterSub++;
                    }
                }
//				post("...counterMax = " +  counterMax +"..\n");
//              post("...counterSub = " +  counterSub +"..\n");
                if(getNode_maxNodes(_nodeType) == 0){
                    addGlobalReport_Fatal("["+ _nodeType + "]: nodes access denied","You try to load a project that contains a node which is not covered by your license","Try again in Demo Mode");
                    return false;
                } else {
                    if(getNode_maxNodes(_nodeType) > 0 && counterMax >= getNode_maxNodes(_nodeType) + _validation){
                        if(accessLevel <= LEVEL_FREE){
                            // if it is a UNREG or FREE license there are some limitations on use for some nodes so we let the user
                            // know
                            addGlobalReport_Warning("["+ _nodeType + "]: maximum nodes reached","You can only create up to " + getNode_maxNodes(_nodeType) + " [" + _nodeType + "]-nodes","Try again in Demo Mode");
                            // post("<"+ _nodeType + ">: maximum nodes reached (max: " + counterMax + ")\n");
                        } else {
                            addGlobalReport_Warning("["+ _nodeType + "]: maximum nodes reached","You can only create up to " + getNode_maxNodes(_nodeType) + " [" + _nodeType + "]-nodes","Not much you can do about.");
                        }
                        return false;
                    } else if(getNode_branchMaxNodes(_nodeType) >= 0 && counterSub >= getNode_branchMaxNodes(_nodeType) + _validation){
//                      post("<"+ _nodeType + ">: maximum nodes in this nodespace level reached (max: " + counterSub + ")\n");
                        addGlobalReport_Warning("["+ _nodeType + "]: maximum nodes reached","You can only create up to " + getNode_branchMaxNodes(_nodeType) + " [" + _nodeType + "]-nodes in one workspace-level","Try again in another workspace-level");
                        return false;
                    }
                }
            }
        }else{
            addGlobalReport_Warning("["+ _nodeType + "]: cannot be created","You tried to create a [" + _nodeType + "]-node in a workspace-level that is unsuitable for this node","Try to create this node in a different workspace-level.");
            return false;
        }
// 		post("... valid creation check for nodeType : " + _nodeType + " | _spacename : " + _spacename + " | _spacelevel : "+_spacelevel+" | _validation : "+_validation+"\n");
        return true;
    }
    addGlobalReport_Fatal("["+ _nodeType + "]: cannot be created","Your try to load a project with an unknown/invalid node"," Update SPARCK and try again");
    return false;
}

function hasNode_access(_nodeType){
    return nodeAccess.contains(_nodeType);
}

function getNode_branchLevel(_nodeType){
    return nodeAccess.get(_nodeType + "::nodeCreation::_branchLevel")
}

function getNode_maxNodes(_nodeType){
//	post("accesslevel = "+accessLevel + "\n");
//	post("_level (from dict)= "+nodeAccess.get(_nodeType + "::nodeCreation::_level") + "\n");
//	post("_maxNodes (from dict)= "+nodeAccess.get(_nodeType + "::nodeCreation::_maxNodes") + "\n");
	dpost("getNode_maxNodes: " + _nodeType);
	dpost("current access level: " + accessLevel);
	dpost("node level: " + nodeAccess.get(_nodeType + "::nodeCreation::_level"));
	if(nodeAccess.get(_nodeType + "::nodeCreation::_level").indexOf(""+accessLevel) != -1){
    	return nodeAccess.get(_nodeType + "::nodeCreation::_maxNodes");
    } else {
		return 0;
    }
}

function getNode_branchMaxNodes(_nodeType){
    return nodeAccess.get(_nodeType + "::nodeCreation::_branchMaxNodes")
}

function getNode_fileName(_nodeType){
    return nodeAccess.get(_nodeType + "::nodeCreation::_fileName")
}

function getNode_version(_nodeType){
    return nodeAccess.get(_nodeType + "::nodeCreation::_version")
}

function getNode_titlePattrn(_nodeType){
    return nodeAccess.get(_nodeType + "::nodeCreation::_titlePattrn")
}

function exportlevel(exportspace, level){
	var nodeTypes = baseDB.getkeys();
	//post("building nodespace: " +vpl_NodeSpaceName + "\n");
	if(nodeTypes != null){
		//post("types: " +nodeTypes + "\n");
		for(var i = 0; i < nodeTypes.length; i++){
			//post("type keys: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				var typeDB = baseDB.get(nodeTypes[i]);
				var nodeBoxes = typeDB.getkeys();
				//post("nodeBox keys: " + nodeBoxes + "\n");
				if(nodeBoxes != null){
					for(var j = 0; j < nodeBoxes.length; j++){
						if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
							var nodeBoxDB = typeDB.get(nodeBoxes[j]);
							if(nodeBoxDB.get("_nodespace") == vpl_NodeSpaceName){
								exportDB.set(nodeBoxDB.get("_name"), nodeBoxDB);
								exportDB.set(nodeBoxDB.get("_name") + "::" + "_nodespace", exportspace);
								exportDB.set(nodeBoxDB.get("_name") + "::" + "_level", level);
								exportDB.remove(nodeBoxDB.get("_name") + "::" + "_name", level);
								exportDB.remove(nodeBoxDB.get("_name") + "::" + "_id", level);
								messnamed(nodeBoxDB.get("_name")+"::vpl::connection", "exportlevel", exportspace, level);
							}
						}
					}
				}
			}
		}
	}
}

function deletenode(nodename){
	var nodeTypes = baseDB.getkeys();
	if(nodeTypes != null){
		dpost("types: " +nodeTypes + "\n");
		for(var i = 0; i < nodeTypes.length; i++){
			dpost("type keys: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				typeDB = baseDB.get(nodeTypes[i]);
				dpost("nodes: " +typeDB.getkeys() + "\n");
				if(typeDB.contains(nodename)){
					dpost("found: " +nodename + "\n");
					var node = typeDB.get(nodename);
					node.clear();
					typeDB.remove(nodename);
				}
				baseDB.set(nodeTypes[i],typeDB);
			}
		}
	}
}

// searches the database for all nodes in this workspace to identify
// the lowest and still available nodeSpaceIndex - this index is used
// to get the _nodespace.idx
function getUniqueNodeSpaceIndex(){
	var indices = new Array();
	var nodeTypes = baseDB.getkeys();
	//post("building nodespace: " +vpl_NodeSpaceName + "\n");
	if(nodeTypes != null){
		//post("types: " +nodeTypes + "\n");
		if(typeof nodeTypes == "string"){
			nodeTypes = new Array(nodeTypes);
		}
		for(var i = 0; i < nodeTypes.length; i++){
			//post("type keys: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				var typeDB = baseDB.get(nodeTypes[i]);
				var nodeBoxes = typeDB.getkeys();
				//post("nodeBox keys: " + nodeBoxes + "\n");
				if(nodeBoxes != null){
					if(typeof nodeBoxes == "string"){
						nodeBoxes = new Array(nodeBoxes);
					}
					for(var j = 0; j < nodeBoxes.length; j++){
						if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
							var nodeBoxDB = typeDB.get(nodeBoxes[j]);
							if(nodeBoxDB.get("_nodespace") == vpl_NodeSpaceName){
								indices.push(nodeBoxDB.get("_nodespace.idx"));
							}
						}
					}
				}
			}
		}
	}
	var spaceid = 0;
	if(indices != null){
		while(indices.indexOf("node." + spaceid) != -1){
			spaceid++;
		}
	}
	return "node."+spaceid;
}

var buildArray;
var initArray;
// builds this nodespace and all its subspaces
// should only be called after a clear().
function build(){
    dpost(vpl_NodeSpaceName + " - prepare building level...");

    var nodeTypes = baseDB.getkeys();
    buildArray = new Array();
    initArray = new Array();
    if(vpl_NodeSpaceLevel == 0){
        _globalBuilder.counterBuild = 0;
        _globalBuilder.counterInit = 0;
        _globalBuilder.numberOfNodeBoxes = 0;
    }

    // Creating the list (buildarray) of nodes to be build inside this nodespace

	//dpost("building nodespace: " +vpl_NodeSpaceName + "\n");
	if(nodeTypes != null){
		//dpost("types: " +nodeTypes + "\n");
        // Create all the nodes inside this workspace
		for(var i = 0; i < nodeTypes.length; i++){
			//dpost("build() node types: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				var typeDB = baseDB.get(nodeTypes[i]);
				//dpost("build() type: " + nodeTypes[i] + "\n");
				var nodeBoxes = typeDB.getkeys();
				//dpost("build() nodeBox keys: " + nodeBoxes + "\n");
				if(nodeBoxes != null){
					for(var j = 0; j < nodeBoxes.length; j++){
						if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
							var nodeBoxDB = typeDB.get(nodeBoxes[j]);
							//dpost("build() node found: " + nodeBoxes[j] + " in nodespace :" + nodeBoxDB.get("_nodespace") + "\n");
                            // if we are in the root nodespace, we count all the nodes
                            if(vpl_NodeSpaceLevel == 0 && nodeBoxDB.get("_nodespace") != null){
                                _globalBuilder.numberOfNodeBoxes = _globalBuilder.numberOfNodeBoxes + 1;
                            }
                            // then we look only at the nodes inside this nodespace, check them and add them to the buildArray.
				            if(nodeBoxDB.get("_nodespace") == vpl_NodeSpaceName){
                                var thisNodeName = nodeBoxDB.get("_name");
                                var thisNodeType = nodeBoxDB.get("_type");
                                // the following checks if the name and type are the same. if not it will correct
                                if(thisNodeName.substring(0, thisNodeName.indexOf("_")) != thisNodeType){
                                    var newNodeName = makeNodeName(thisNodeType, nodeBoxDB.get("_id"));
                                    dpost("build() found currupted data: " + thisNodeName + " should be " + newNodeName + " because of its type: '" + thisNodeType + "'. fixing it...\n");
                                    nodeBoxDB.replace("_name", newNodeName);
                                    typeDB.remove(nodeBoxes[j]);
                                    nodeBoxes[j] = newNodeName;
                                    typeDB.set(nodeBoxes[j], nodeBoxDB);
                                    baseDB.replace(nodeTypes[i], typeDB);
                                }

                                 //check if all creation tests are passed:
 								//dpost("build() node '"+nodeBoxes[j]+"' of type " + nodeTypes[i] + " is in nodespace '" + vpl_NodeSpaceName + "', level '"+vpl_NodeSpaceLevel+"'\n");
                                //dpost("build() node '"+nodeBoxes[j]+"' has type "+ nodeBoxDB.get("_type") +"\b")
                                if(creationCheck(typeDB, nodeTypes[i], nodeBoxDB.get("_version"), thisNodeName, vpl_NodeSpaceName, vpl_NodeSpaceLevel, 1)){
                                    if(updateDeprecatedNodes){

                                        var thisNodeVersion = nodeBoxDB.get("_version");
                                        if(getNode_version(nodeTypes[i]) == 0){
                                            // if this nodes version is 0, it means it is deprecated and we stick to this version max
                                            dpost(vpl_NodeSpaceName + " - keeping deprecated node: " + nodeBoxDB.get("_title"));
                                        } else if(thisNodeVersion < getNode_version(nodeTypes[i])){
                                            // we have now to check if this node is the most current version, if not we have to update it.
                                            dpost(vpl_NodeSpaceName + " - updated deprecated node: " + nodeBoxDB.get("_title"));
                                            nodeBoxDB.replace("_version", getNode_version(nodeTypes[i]));
                                        }
                                    }
                                    buildArray.push(makeCreationMessage(nodeBoxDB));
                                } else { // creation check failed
                                    if(!vpl_NodeSpaceLevel == 0){
                                        // if we are in a subworkspace we need to call back the parent, otherwise the
                                        // process hangs...
                                        dpost(vpl_NodeSpaceName + " - creation check failed ");
                                        callBackParentBuilder.initTask.execute();
                                    } else {
                                        // we are the root workspace..
                                        return false;
                                    }
                                }
 							}
						}
					}
				}
			} else if(nodeTypes[i] == ""){
                dpost("found a \"\":type that needs to be removed. but have no idea how to do this now.");
            }
		}
 	}
    //Sorting the buildarray: make sure the subodes come last.
    for(var i = buildArray.length - 1; i >= 0; i--){
        if(buildArray[i][3] == vpl_subNode){
            var msglist = buildArray.splice(i,1);
            buildArray.push(msglist[0]);
        }
    }
    // if it is the root space level, setup the gui-progressbar
    if(vpl_NodeSpaceLevel == 0){
        messnamed("bs::app::gui::progress", "color", 0.16, 0.73, 0.98, 1.0);
        messnamed("bs::app::gui::progress", "size", _globalBuilder.numberOfNodeBoxes + 1);
        _globalBuilder.nodeSpaceLevel = 0;
    }
    dpost(vpl_NodeSpaceName + " - building level...");

    builderTask.execute();
    return true;
}


var builderTask = new Task(buildSubTask, this);

// TASK function to build the nodes
function buildSubTask(msg){
    if(buildArray.length > 0){
        //get the next node
        var msg = buildArray.shift();
        // add it to the initialisation array
        initArray.push(msg);

        // update the progress bar
        messnamed("bs::app::gui::progress", "text", msg[15]);
        dpost("building " + msg[11] + "/" + msg[13] + "/" + msg[15] + "...");

        // send the build message to the node patcher
        vpl_NodeSpacePatcher.message(msg);

        _globalBuilder.counterBuild++;
        messnamed("bs::app::gui::progress", "pos", _globalBuilder.counterBuild);

        //recall this task in 1 ms
        builderTask.schedule(1);
    } else {
        // once all the nodes inside this space are built, execute initialization
        dpost(vpl_NodeSpaceName + " - initialize level.");
        if(initTask.running == false){
            initTask.execute();
        } else {
            dpost("Houston, we...");
        }
    }
}

var initTask = new Task(initSubTask, this);

//TASK function to initialize the nodes
function initSubTask(msg){
    dpost("initSubTask- building " + _globalBuilder.nodeSpaceLevel + " / " + vpl_NodeSpaceLevel);
    if(_globalBuilder.nodeSpaceLevel == vpl_NodeSpaceLevel){
        // if there are still some nodes to initilize
        if(initArray.length > 0){
            // get the next node
            var msg = initArray.shift();
            dpost("initialize >"+msg[11]+"< at level: " + vpl_NodeSpaceLevel + "\n");

            //_globalBuilder.counterInit++;

            // if it is a subnode, change the nodespacelevel...
            if(msg[3] == vpl_subNode){
                _globalBuilder.nodeSpaceLevel++;
                _globalBuilder.callBackObject = this;
                dpost(vpl_NodeSpaceName + " - initialize sub level '" + msg[11] + "'");
            } else {
                // if it is a subnode, the subnode will call back, otherwise
                // the next node needs to be initialized...
                initTask.schedule(1);
            }

            //.. initialize the node
            dpost("initializing " + msg[11] + "...");
            messnamed(msg[11]+"::vpl::connection", "init");
        } else {
            // if there are no more nodes to initilized, finalize this nodeSpaceLevel...
            dpost(vpl_NodeSpaceName + " - finalize initialization");
            _globalBuilder.nodeSpaceLevel = (_globalBuilder.nodeSpaceLevel > 0)? _globalBuilder.nodeSpaceLevel - 1: 0;
            if(vpl_NodeSpaceLevel == 0){
                //messnamed("bs::app::gui::progress", "color", 0.47, 0.25, 0.61, 1.0);
                messnamed("bs::app::gui::progress", "text", "... initializing ...");
                // if it is the root level, finalize the building process..
                outlet(0, "done", "build");
                outlet(0, "done", "read");
                //builderEnding.schedule(10);
                // finally send the reports that have been created so far:
                sendGlobalReport();
            } else {
//                post("callback...\n");
                dpost(vpl_NodeSpaceName + " - calling back parent nodespace");
                callBackParentBuilder.initTask.execute();
            }
        }
    }
}

//refreshes the this nodespace and its subspaces (subnodes)
function refresh(){
	var nodeTypes = baseDB.getkeys();
	if(nodeTypes != null){
		//post("types: " +nodeTypes + "\n");
		for(var i = 0; i < nodeTypes.length; i++){
			//post("type keys: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				var typeDB = baseDB.get(nodeTypes[i]);
				var nodeBoxes = typeDB.getkeys();
				//post("nodeBox keys: " + nodeBoxes + "\n");
				if(nodeBoxes != null){
					for(var j = 0; j < nodeBoxes.length; j++){
						if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
							var nodeBoxDB = typeDB.get(nodeBoxes[j]);
							if(nodeBoxDB.get("_nodespace") == vpl_NodeSpaceName){
								//post("nodeBox: " + nodeBoxDB.get("_name") + " refreshed\n")
								messnamed(nodeBoxDB.get("_name")+"::vpl::connection", "refresh");
							}
						}
					}
				}
			}
		}
	}
}

// receives this message from a subnode on removal
function dispose(){
    clear();
}

//Clears this nodespace and all its subspaces.
function clear(verbose){
	var nodeTypes = baseDB.getkeys();
	if(nodeTypes != null){
		//post("types: " +nodeTypes + "\n");
		for(var i = 0; i < nodeTypes.length; i++){
			//post("type keys: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				var typeDB = baseDB.get(nodeTypes[i]);
				var nodeBoxes = typeDB.getkeys();
				//post("nodeBox keys: " + nodeBoxes + "\n");
				if(nodeBoxes != null){
					for(var j = 0; j < nodeBoxes.length; j++){
						var wasCleared = false;
						//post("nodeBox: " + nodeBoxes[j] + "\n");
						if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
							var nodeBoxDB = typeDB.get(nodeBoxes[j]);
							if(nodeBoxDB.get("_nodespace") == vpl_NodeSpaceName){
								dpost("dispose " + nodeBoxDB.get("_name") + "\n");
                                // first sends a dispose message to the node
                                messnamed(nodeBoxDB.get("_name") + "::vpl::connection", "dispose");
				                // then calls the nodespace to delete the node -> even though
                                // the dispose message would eventually remove the node, too; for some
                                // unknown reason this doesnt work properly...
								vpl_NodeSpacePatcher.message("script", "delete", nodeBoxDB.get("_title"));
								//outlet(0, "script", "delete", nodeBoxDB.get("_name"));
                                //messnamed(vpl_NodeSpaceName + "::vpl::nodespace", "dispose");
								pausecomp(20);
                                nodeBoxDB.clear();
								wasCleared = true;
							}
						}
						if(wasCleared)
							if(typeDB.contains(nodeBoxes[j]))
								typeDB.remove(nodeBoxes[j]);
					}
				}
			}
		}
	}
	if(verbose == 1)
		outlet(0, "done", "clear");
}

// This method only clears the db, and not the nodespace!
function clearDB(){
	var nodeTypes = baseDB.getkeys();
	if(nodeTypes != null){
		//post("types: " +nodeTypes + "\n");
		for(var i = 0; i < nodeTypes.length; i++){
			//post("type keys: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				var typeDB = baseDB.get(nodeTypes[i]);
				var nodeBoxes = typeDB.getkeys();
				if(nodeBoxes != null){
					for(var j = 0; j < nodeBoxes.length; j++){
						if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
							var nodeBoxDB = typeDB.get(nodeBoxes[j]);
							//post("clear nodebox: " + nodeBoxes[j] + "\n");
							nodeBoxDB.clear();
						}
					}
				}
				//post("clear nodetype: " + nodeTypes[i] + "\n");
				typeDB.clear();
			}
		}
	}
	baseDB.clear();
}

function pausecomp(ms) {
	ms += new Date().getTime();
	while (new Date() < ms){}
}
