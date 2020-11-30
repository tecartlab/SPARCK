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

include("node.classes.js");

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 5;

var OUTLET_THISPATCHER = 0;
var OUTLET_OSSIA = 1;
var OUTLET_PCONTROL = 2;
var OUTLET_DUMP = 3;
var OUTLET_MENUL = 4;

var myColorTable = new Dict("vpl::colortable");
var myType2Color = new Dict("vpl::type2color");
var myBaseDB 	 = new Dict("vpl::db");

var uniqueTable = new Dict("bs.vpl.unique.title");

var myIOLetButtonSize = 8;
var myIOLetButtonShift = -2;
var myIOLetButtonOffset = -2;

var myTitleBarHeight = 28;      // size of title bar
var myPBodyOffset = 2;          // distance of pbody bpatcher from top
var myCanvasLowerSpacing = 4;   // additional space for canvas at the bottom of pbody
var myNodeLowerSpacing = 4;     // additional space for node at the bottom of canvas
var myDefaultSize = new Array(180, 0);
var myExpandedMode = 0; // 0 = default, 1 = folded, 2 = unfolded
var myNodeSizes = new Array(myDefaultSize, myDefaultSize, myDefaultSize); // there are 3 sizes, one for each ExpandedModes

var myIOlets = new NODE.IOlets(myIOLetButtonOffset, myIOLetButtonSize, myIOLetButtonShift, myType2Color, myColorTable);

//autowatch = 1;

var undefined = "undefined";

var myNodeDBPath;
var myNodeFileName = undefined;
var myNodeName = undefined; // fixed name, only initialize inside APP
var myNodeID = undefined; // fixed ID, based on a random but unique number
var myNodeTitle = undefined; // user set node title
var myNodeAddress = undefined; // ossia node address, set through the title
var myNodeOssiaModel = undefined; // ossia model address, set through the title
var myNodeVarName = undefined;
var myNodeSpace = undefined;
var myDefaultColor = new Array(1.0, 0.65, 0.0, 1.);
var myNodeColorOn = new Array(1.0, 0.65, 0.0, 1.);
var myNodeColorOff = new Array(0.7, 0.7, 0.7, 0.8);
var myNodeColorSelected = new Array(0.0, 0.0, 0.0, 0.3);
var myNodeColorUnSelected = new Array(0.0, 0.0, 0.0, 0.8);

var myNodeEnable = 0;
var myNodeSelected = 0;

var myNodeEnableProperties = 0;
var myNodePropsFileName = undefined;

var myNodeEnableBody = 0;
var myNodePBodyFileName = undefined;

var myNodeEnableHelp = 0;
var myNodeHelp = undefined;

var myNodeInit = false;
var myNodeTitleYPos = 11;
var myNodeTitleIconSize = 13;

var connections = new Array();

var vpl_nodeBox;
var vpl_nodePatcher;
var vpl_nodeLogicPatcher;
var vpl_nodeSpacePatcher;
var vpl_nodeCanvas;
var vpl_nodeEnable;
var vpl_titleEdit;
var vpl_titleBar;
var vpl_body;
var vpl_linked = true;

var appGlobal = new Global("bs::app::global");

//make it dragable even without init()

// set the debugger : 0=off, 1=all, 2=app, 3=io, 4=db, 5=node, 6=select
var debug = (appGlobal.debug == 1 || appGlobal.debug == 5)? true:false;

// set the debugger
function debugger(_val){
	debug = (_val == 1 || _val == 5)? true: false;
}

function dpost(_message){
    if(debug){
        post("Node " + myNodeTitle + "/" +myNodeID + ": " + _message + " \n");
    }
}

/**********************
  Attribute Functions
 **********************/

function enable_body(_enable){
	myNodeEnableBody = _enable;
	dpost("enable body = " + myNodeEnableBody + "\n");
}

function enable_properties(_enable){
    myNodeEnableProperties = _enable;
	dpost("enable properties = " + myNodeEnableProperties + "\n");
}

function help(val){
    myNodeEnableHelp = 1;
	myNodeHelp = val;
}

function done(){
    dpost("execute done...\n");
	// initialize automatically only if the parent patch wastn created by vpl
	if(myNodeInit == false && myNodeName == undefined && myNodeSpace == undefined){
		vpl_linked = false;
		init();
 	} else {
        dpost("is created via vpl. myNodeInit = "+myNodeInit+" | myNodeName = "+myNodeName+" | myNodeSpace = "+myNodeSpace+" \n");
    }
    dpost("...done\n");
}

/**********************
  Init Functions
 **********************/


function init(){
//	myNodeVarName = getKeyValuefromDB(myNodeName, "_conn.id");
	dpost("init node..("+myNodeName+") \n");
	initMenu();
	initNodeSpace();
	initNode();
	initNodeBox();
    initIOlets();
	initConnections();

 	dpost("nodebox initialized: " + myNodeName + ". sending nextlevel\n");
	outlet(0, "initnextlevel", "nodespace", getKeyValuefromDB(myNodeName, "_nodespace") + "::" + getKeyValuefromDB(myNodeName, "_nodespace.idx"), getKeyValuefromDB(myNodeName, "_level") + 1);
	outlet(0, "done");
	myNodeInit = true;

    if(vpl_nodeSpacePatcher != null){
        vpl_nodeSpacePatcher.message("script", "bringtofront", vpl_nodeBox.varname);
    }

	dpost("...init("+myNodeName+") done\n");
	return true;
}

// repopulates the menu items
function initMenu(){
	outlet(4, "vpl_menu", "clear");
	outlet(4, "vpl_menu", "append", "properties");
	outlet(4, "vpl_menu", "append", "help");
	outlet(4, "vpl_menu", "append", "rename");
	outlet(4, "vpl_menu", "append", "expand");
	outlet(4, "vpl_menu", "append", "fold");
	outlet(4, "vpl_menu", "append", "---");
	outlet(4, "vpl_menu", "append", "duplicate");
	outlet(4, "vpl_menu", "append", "delete");

	outlet(4, "vpl_menu", "enableitem", 0, myNodeEnableProperties);
	outlet(4, "vpl_menu", "enableitem", 1, myNodeEnableHelp);
    outlet(4, "vpl_menu", "enableitem", 3, myNodeEnableBody);		
    outlet(4, "vpl_menu", "enableitem", 4, myNodeEnableBody);		
}

/* recursively gets the the parents patcher information
*/
function initNodeSpace(){
	var owner = this.patcher.box;
	if(owner != null){
		owner = this.patcher.box;
		if(owner != null){
            // get the node abstraction file names 
            var nodepath = owner.patcher.filepath;
            myNodeFileName = nodepath.substring(nodepath.lastIndexOf("/") + 1, nodepath.lastIndexOf("."));
            
            var nodeName = myNodeFileName.substring(8, myNodeFileName.length); // cutting away 'bs.node.'
            if(myNodeEnableBody == 1){
                myNodePBodyFileName = "bs.props." + nodeName + ".p";
            } else if (myNodeEnableBody == 2){
                myNodePBodyFileName = "bs.props." + nodeName + ".b";
            }
            myNodePropsFileName = "bs.props." + nodeName + ".p";
            
            vpl_nodeBox = owner.patcher.box;
			if(vpl_nodeBox != null){
				vpl_nodeSpacePatcher = vpl_nodeBox.patcher;
			} else {
                post("didnt find nodespacepatcher\n");
            }
		}
	}
}

function initNode(){
	//Find  all the important objects in this patcher
	// and initialize them.
	if(vpl_nodePatcher == null){
        
		// tries to find a bpatcher object with the name "vpl_body"
		if(this.patcher.getnamed("vpl_body") != null){
			vpl_body = this.patcher.getnamed("vpl_body");
			//post(" has vpl_titleEdit...\n");
		}
		// tries to find a text object with the name "vpl_titleEdit"
		if(this.patcher.getnamed("vpl_titleEdit") != null){
			vpl_titleEdit = this.patcher.getnamed("vpl_titleEdit");
			//post(" has vpl_titleEdit...\n");
		}
		if(this.patcher.getnamed("vpl_titleBar") != null){
			vpl_titleBar = this.patcher.getnamed("vpl_titleBar");
			vpl_titleBar.message("hint", "NodeType: " + myNodeHelp);
			//post(" has vpl_titleBar...\n");
		}
        // tries to find a canvas object with the name "vpl_canvas"
        if(this.patcher.getnamed("vpl_canvas") != null){
            vpl_nodeCanvas = this.patcher.getnamed("vpl_canvas");
            dpost(" has vpl_canvas... \n");
        }
        // tries to find a thispather object with the name "vpl_ThisNodeLogicPatcher"
        if(this.patcher.getnamed("vpl_ThisNodeLogicPatcher") != null){
            vpl_nodeLogicPatcher = this.patcher.getnamed("vpl_ThisNodeLogicPatcher");
            dpost(" has vpl_nodeLogicPatcher... \n");
        }
        
		var client = this.patcher.box;
		while (client) {
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
    dpost("initNodeBox()\n");
	//Find  all the important objects in this patcher
	// and initialize them.

	// calculates this vpl_nodeBox size
	if(vpl_nodeCanvas != null){
        setNodeRect(1);
		if(vpl_titleBar != null){
			//post(" set vpl_titleBar panel: " + vpl_titleBar.rect + " ... \n");
			vpl_titleBar.message("presentation_rect", 0, 4., myDefaultSize[0], 28);
			//post(" to vpl_titleBar panel: " + vpl_titleBar.rect + " \n");
		}
	}else{
		post(" found no canvas \n");
	}
}

function initConnections(){
	var keys = getNodeKeysfromDB(myNodeName);
	//post("my keys: " + keys);
	connections = new Array();
	if(keys != null){
		for(var i = 0; i < keys.length; i++){
			if(keys[i].indexOf("_connection") != -1){
				var con = getKeyValuefromDB(myNodeName, keys[i]);
				connection(con[0], con[1], con[2], con[3], con[4], con[5]);
			}
		}
	}
}

function initIOlets() {
    myIOlets.init(vpl_nodePatcher, myDefaultSize, myIOLetButtonSize/2 + myTitleBarHeight);
}

/**********************
  Call Functions
 **********************/

// called by the menu
function menu(_func){
	if(myNodeInit){		
		if(_func == "properties"){
			openproperties();
		} else if(_func == "collapse"){
        	myExpandedMode = 0;
			expand();
        	outlet(4, "vpl_menu", "setitem", 3, "expand");
        	outlet(4, "vpl_menu", "setitem", 4, "fold");
		} else if(_func == "expand" || _func == "unfold"){
        	myExpandedMode = 2;
			expand();
        	outlet(4, "vpl_menu", "setitem", 3, "collapse");
        	outlet(4, "vpl_menu", "setitem", 4, "fold");
        } else if(_func == "fold"){
        	myExpandedMode = 1;
			expand();
        	outlet(4, "vpl_menu", "setitem", 3, "collapse");
        	outlet(4, "vpl_menu", "setitem", 4, "unfold");
		} else if(_func == "duplicate"){
			;
		} else if(_func == "delete"){
			;
		} else if(_func == "help"){
			outlet(2, "load", "bs.help.node." + myNodeHelp + ".maxpat");
		}
	}
}

// called by menu()
function expand(){
    setNodeRect(0);
    if(myExpandedMode > 0){
        //post("sent  " +myNodePBodyFileName + " to '" + myNodeID + "::pbody" +"' \n");
        messnamed(myNodeID + "::pbody", "name", myNodePBodyFileName);
        myIOlets.expand(myNodeSizes[myExpandedMode], myIOLetButtonSize/2 + myTitleBarHeight + myPBodyOffset + myCanvasLowerSpacing);
    } else {
        myIOlets.expand(myNodeSizes[myExpandedMode], myIOLetButtonSize/2 + myTitleBarHeight);
    }
    //vpl_body.message("hidden", !myExpandedMode);
}

/* Sets the node size, the node logic size and the canvas size, depending on beeing expanded or not.
 * 
 */
function setNodeRect(_store){
    var currentSize = myNodeSizes[myExpandedMode];
    if(vpl_nodeBox != null && vpl_nodeBox.varname == myNodeVarName){
        var myBoxRect = vpl_nodeBox.rect;
        myBoxRect[2] = myBoxRect[0] + currentSize[0];
        if (myExpandedMode == 0) {
            myBoxRect[3] = myBoxRect[1] + currentSize[1] + myIOLetButtonSize + myTitleBarHeight;
            vpl_nodeLogicPatcher.message("script", "sendbox", "vpl_canvas", "presentation_size", currentSize[0], currentSize[1] + myTitleBarHeight);
            vpl_nodeLogicPatcher.message("script", "sendbox", "vpl_body", "hidden", 1);
            vpl_nodeLogicPatcher.message("script", "sendbox", "vpl_body", "size", currentSize[0], currentSize[1]);
            vpl_nodePatcher.message("script", "sendbox", "vpl_nodelogic", "size", currentSize[0], currentSize[1] + myIOLetButtonSize/2 + myTitleBarHeight);
        } else {
            myBoxRect[3] = myBoxRect[1] + currentSize[1] + myIOLetButtonSize + myTitleBarHeight + myPBodyOffset + 2 * myCanvasLowerSpacing;
            vpl_nodeLogicPatcher.message("script", "sendbox", "vpl_canvas", "presentation_size", currentSize[0], currentSize[1] + myTitleBarHeight + myPBodyOffset + myCanvasLowerSpacing);
            vpl_nodeLogicPatcher.message("script", "sendbox", "vpl_body", "hidden", 0);
            vpl_nodeLogicPatcher.message("script", "sendbox", "vpl_body", "size", currentSize[0], currentSize[1]);
            vpl_nodePatcher.message("script", "sendbox", "vpl_nodelogic", "size", currentSize[0], currentSize[1] + myIOLetButtonSize/2 + myTitleBarHeight + myPBodyOffset + myCanvasLowerSpacing);
        }
        vpl_nodeBox.rect = myBoxRect;
        if(_store == 1){
            storeKeyValueInDB(myNodeName, "_rect", myBoxRect);
        }
    }
}

// this function is called by the vpl_body once a body is loaded and the size of the body is evaluated
function expanded_size(_foldedSizeX, _foldedSizeY, _unfoldedSizeX, _unfoldedSizeY){
    //post("expanded_size " + _foldedSizeX + " | " + _foldedSizeY + " | " + _unfoldedSizeX + " | " + _unfoldedSizeY + "\n");
    myNodeSizes[1] = new Array(_foldedSizeX, _foldedSizeY);
    myNodeSizes[2] = new Array(_unfoldedSizeX, _unfoldedSizeY);
    expand();
}

// called on creation. it is a unique id, loaded from the project and needed to access the nodeDB
function nodename(_ndname){
    myNodeName = _ndname;
    dpost("my myNodeName is set: '" + myNodeName + "'\n");
}

// called on creation. it is the node unique id, which does not change during the lifetime
// nodeid is currently only used by properties
function nodeid(_nodeid){
    myNodeID = _nodeid;
    myNodeTitle = _nodeid;
    dpost("My nodeid is set: '" + _nodeid + "'\n");
	outlet(OUTLET_DUMP, "nodeid", _nodeid);
}

// called by the menu
function openproperties(){
    //post("openproperties" +  myNodeID + " " + myNodeTitle + " " + myNodeAddress + " " + myNodePropsFileName + "\n");
    if(myNodeEnableProperties){
		//post("color " + colr + " \n");
        outlet(2, "shroud", "bs.vpl.node.props", myNodeID, myNodeTitle, myNodeAddress, myNodePropsFileName, myNodeColorOn);
    }
}

// open property patch (used only during evelopment)
function open_p(){
    if(myNodeEnableProperties){
        outlet(2, "load", myNodePropsFileName);
    }
}

function open_b(){
    if(myNodeEnableBody){
        outlet(2, "load", myNodePBodyFileName);
    }
}

function refresh(){
	//post("refreshing now: " + myNodeName + "\n");
	initNodeBox();
	outlet(0, "refresh");
}

function exportlevel(exportspace, level){
	outlet(0, "exportlevel", exportspace + "::" + myNodeVarName,level + 1);
}

function nodespace(wrksp){
	myNodeSpace = wrksp;
}

// called by the unique script
function title(newtitle){
	dpost("set new title " + newtitle + " -> nodeid =  " + myNodeID + "\n");
	myNodeTitle = newtitle;
	myNodeOssiaModel = "node/" + newtitle;
    myNodeAddress = "sparck:/" + myNodeOssiaModel;
	storeKeyValueInDB(myNodeName, "_title", newtitle);
	myNodeVarName = newtitle;
    // the sequence of the following messages matter:
    //    we first need to set the title
    //         used by: vpl.node.property.logic
    messnamed(myNodeID + "::props", "title", myNodeTitle);
    messnamed(myNodeID + "::props", "address", myNodeAddress);
    messnamed(myNodeID + "::pbody", "address", myNodeTitle, myNodeAddress);
	outlet(OUTLET_DUMP, "setmsgtitle", newtitle);
	outlet(OUTLET_DUMP, "title", newtitle);
	outlet(OUTLET_DUMP, "address", myNodeOssiaModel);
}

function color(){
	args = arrayfromargs(arguments);
	if(args.length > 0){
		myNodeColorOn[0] = args[0];
		myNodeColorOn[1] = args[1];
		myNodeColorOn[2] = args[2];
		myNodeColorOn[3] = args[3];
	} else {
		myNodeColorOn[0] = myDefaultColor[0];
		myNodeColorOn[1] = myDefaultColor[1];
		myNodeColorOn[2] = myDefaultColor[2];
		myNodeColorOn[3] = myDefaultColor[3];
	}
	setGUIColors();
	messnamed(myNodeID + "::props", "color", myNodeColorOn);
}

function msg_int(val){
	if(inlet == 0)
		if(val == 0 || val == 1)
			enable(val);
}

function enable(_enable){
	if(myNodeEnable != _enable){
		myNodeEnable = _enable;
		myNodeSelected = 0;
		setGUIColors();
		outlet(OUTLET_DUMP, "enable", _enable);
		// for good measure, set the toogle
		outlet(OUTLET_OSSIA, "enable", "set", _enable);
	}
}

// this function is called by
// bs.app.output.stageview.window/objectPicker/pickAndSelect
function pickselect(_openProperties){
	select(1);
	if(_openProperties)
		openproperties();
}

// called if this node became selected
// is also called by bs.app.output.stageview.window/objectPicker/pickAndSelect
// if no node is picked and this node was selected last.
function select(_select){
    //post("got selected ("+_select+")... (" + myNodeName + ")\n");
	if(vpl_linked){
//		cpost("... selected is linked... (" + myNodeName + ")\n");
        if(appGlobal.selectedNode == null){
            appGlobal.selectedNode = "undefined";
        }
//		cpost("... selected has appGlobal.selectedNode = "+appGlobal.selectedNode+"... (" + myNodeName + ")\n");
		//post("got selected: " + myNodeTitle + ": " + _select + " \n");
		if(_select){
            if(vpl_nodeSpacePatcher != null){
        		vpl_nodeSpacePatcher.message("script", "bringtofront", vpl_nodeBox.varname);
            }
			if(appGlobal.selectedNode[1] != myNodeName){
//				cpost("... deselect = "+appGlobal.selectedNode[1]+" by (" + myNodeName + ")\n");
				// if anther node was selected, send a deselect message to that node
				messnamed(appGlobal.selectedNode[1] +"::vpl::connection", "select", 0);
				// set the global selected variable
				appGlobal.selectedNode = new Array(myNodeSpace, myNodeName, myNodeTitle, getKeyValuefromDB(myNodeName, "_type"), (vpl_nodeBox != null)?vpl_nodeBox.rect[0]:0, (vpl_nodeBox != null)?vpl_nodeBox.rect[1]:0);
//				cpost("... set global selectedNode = "+appGlobal.selectedNode[1]+" by (" + myNodeName + ")\n");
			}
		} else {
			// set the value to undefined if it was previsouly set to this node
			if(appGlobal.selectedNode[1] == myNodeName)
				appGlobal.selectedNode = "undefined";
		}

		myNodeSelected = _select;
		//if(debug){
		//	post("bs.vpl.node.select(): " + myNodeName + " selected: " + _select + "\n");
		//	post(" appGlobal.selectedNode set to: " + appGlobal.selectedNode + "\n");
        //}
		//post("node: select() " + myNodeSelected + "\n");
		setGUIColors();
	}
	if(vpl_titleEdit != null){
//		cpost("... selected send title bang... (" + myNodeName + ")... done\n");
		vpl_titleEdit.message("bang");
	}
//	cpost("... selected ("+_select+")... (" + myNodeName + ")... done\n");
}

//Called by another selected inside the workspace
/*
function dragselected(_nodename, _diffX, _diffY){
	if(myNodeSelected == 1 && myNodeName != _nodename){
		applydrag(_diffX, _diffY);
	}
}
*/

//Called by a subpacth inside the node-patch
function drag(diffX, diffY){
	if(vpl_nodeBox != null){
		applydrag(diffX, diffY);

		// if this node is part of multiple selected nodes,
		//  then it will send all other selected nodes a drag-message
		/*
		if(myNodeSelected == 1){
			messnamed(myNodeSpace + "::vpl::nodespace", "dragselected", myNodeName, diffX, diffY);
		}
		*/
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
	storeKeyValueInDB(myNodeName, "_rect", myBoxRect);
//	vpl_nodeSpacePatcher.message("front"); <- slows down patch rendering
}

function openworkspace(){
	if(vpl_nodeBox.patcher != null){
		vpl_nodeBox.patcher.message("front");
    }
}

// called when the node is removed by the user or a new project is loaded,
// but NOT when the application is quited -> this is important, because window-node would
// create a message sent to the window.context which causes a crash...
function dispose(){
//	initNode();
	dpost("dispose function called\n");
    enable(0); // sends enable 0 messages...
	outlet(OUTLET_DUMP, "dispose");

    if(vpl_nodePatcher != 0){
        // calls the parent patcher to dispose this node.
        // it will eventually call the notifydeleted function as well.
		vpl_nodePatcher.message("dispose");
    }
}

//Called when the parent node was deleted
function notifydeleted(){
	dpost("notifydeleted function called\n");
// 	cpost("ABOUT to got deleted\n");
	deletenodeFromDB(myNodeName);

	// call all nodes inside the same nodespace and tell them
	// to remove connections to this node from the database
	messnamed(myNodeSpace + "::vpl::nodespace", "removeConnection", myNodeName);
    messnamed(myNodeID + "::props", "close");
// 	cpost(myNodeName + " got deleted\n");
    myIOlets.freepeer();
}


/**********************
  Managing GUI / Colors
 **********************/

function setGUIColors(){
	dpost("setGUIColors()\n");
	if(myNodeInit){
		var workingcolor = myNodeColorOn;
		if(myNodeEnable == 0)
			workingcolor = myNodeColorOff;
   
    	if(vpl_nodeCanvas.understands("bgfillcolor")){
        	//post("bgfillcolor\n");
        	vpl_nodeCanvas.message("bgfillcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
    	}
    	if(vpl_nodeCanvas.understands("bgcolor")){
        	//post("bgcolor\n");
        	vpl_nodeCanvas.message("bgcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
    	}

		if(vpl_nodeEnable != null){
		//	vpl_nodeEnable.message("bordercolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]- 0.05 );
		}

		// setting the title bar
		workingcolor = (myNodeSelected == 1)? myNodeColorSelected:myNodeColorUnSelected;
		if(vpl_titleBar != null){
			vpl_titleBar.message("bgcolor", workingcolor);
		}
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
		removeKeyValuefromDB(myNodeName, "_connection_" + i);
		if(connections[i] != null)
			newConnections.push(connections[i]);
	}
	connections = newConnections;
	for(var i = 0; i < connections.length; i++){
		storeConnectionInDB(i, connections[i]);
	}
}

function storeConnectionInDB(index, conn){
	storeKeyValueInDB(myNodeName, "_connection_" + index, new Array(conn.outaddress, conn.outid, conn.outtype, conn.inid, conn.intype, conn.inmaxcon));
}

function makeBoxConnection(conn){
	//post("makeBoxConnection");
	//post(" : " + getKeyValuefromDB(conn.outaddress, "_title") + " | " + conn.outid + " | " + vpl_nodeBox.varname + " | " + conn.inid + " | " + getColorID(conn.intype) + "\n")
	vpl_nodeSpacePatcher.message("script", "connectcolor", getKeyValuefromDB(conn.outaddress, "_title"), conn.outid-1, vpl_nodeBox.varname, conn.inid-1, getColorID(conn.intype));
}

function removeBoxConnection(conn){
	vpl_nodeSpacePatcher.disconnect(vpl_nodeSpacePatcher.getnamed(getKeyValuefromDB(conn.outaddress, "_title")), conn.outid-1, vpl_nodeBox, conn.inid-1);
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

function removeKeyValuefromDB(_nodeName, _key){
	if(hasDBAccess(_nodeName)){
 		if(myBaseDB.contains(getNodeKeyPath(_nodeName, _key)))
			myBaseDB.remove(getNodeKeyPath(_nodeName, _key));
	}
}

function storeKeyValueInDB(_nodeName, _key, _value){
    //post("storing " + _nodeName + " key: " + _key + " | value: " + _value + "\n");
    if(hasDBAccess(_nodeName)){
        //post("was here: " + getNodeType(_nodeName) + " \n");
		if(myBaseDB.contains(getNodeKeyPath(_nodeName, _key))){
			myBaseDB.replace(getNodeKeyPath(_nodeName, _key), _value);
		} else
			myBaseDB.set(getNodeKeyPath(_nodeName, _key), _value);
	}
}

function getKeyValuefromDB(_nodeName, _key){
	if(hasDBAccess(_nodeName)){
 		if(myBaseDB.contains(getNodeKeyPath(_nodeName, _key)))
			return myBaseDB.get(getNodeKeyPath(_nodeName, _key));
	}
//	dpost("getKeyValuefromDB(" +_nodeName + ", " + _key +") fails to return value \n");
	return null;
}

function getNodeKeysfromDB(_nodeName){
	if(hasDBAccess(_nodeName)){
		var nodeTypeDB = myBaseDB.get(getNodeType(_nodeName));
		//post("nodes: " +typeDB.getkeys() + "\n");
		if(nodeTypeDB != null && nodeTypeDB.contains(_nodeName)){
			return nodeTypeDB.get(_nodeName).getkeys();
		}
	}
//	dpost("getNodeKeysfromDB(" +_nodeName + ") fails to return value \n");
	return null;
}

function getNodeType(_nodeName){
	return _nodeName.substring(0, _nodeName.indexOf("_"));
}

function getNodeKeyPath(_nodeName, _key){
	return getNodeType(_nodeName) + "::" + _nodeName + "::" + _key;
}

/*
function getNodeKeysfromDB(){
	var nodeTypeDB = myBaseDB.get(myNodeType);
	//post("nodes: " +typeDB.getkeys() + "\n");
	if(nodeTypeDB != null && nodeTypeDB.contains(myNodeName)){
		return nodeTypeDB.get(myNodeName).getkeys();
	}
}
*/

function hasDBAccess(_nodeName){
    //post("hasDBAccess: " + _nodeName + "\n");
	if(_nodeName != null && _nodeName != myNodeID){
		var nodeTypeDB = myBaseDB.get(getNodeType(_nodeName));
        //post("nodes: " +nodeTypeDB.getkeys() + "\n");
		if(nodeTypeDB != null){
            //post("nodes: " +nodeTypeDB.getkeys() + "\n");
            if(nodeTypeDB.contains(_nodeName)){
                return true;
            } else {
                //dpost("hasDBAccess(" +_nodeName + ") fails to find '"+getNodeType(_nodeName)+"' inside nodeTypeDB\n");
                //dpost("hasDBAccess(" +_nodeName + ") finds only '"+myBaseDB.getkeys()+"' inside myBaseDB\n");
            }
        } else {
            //dpost("hasDBAccess(" +_nodeName + ") fails to find nodeTypeDB for nodetype: "+getNodeType(_nodeName)+" \n");
        }
	}
	//dpost("hasDBAccess(" +_nodeName + ") fails to access db \n");
	return false;
}

//Called by notifydeleted()
function deletenodeFromDB(){
	if(hasDBAccess(myNodeName)){
		//dpost("delete node: " + myNodeName + " \n");
		var typeDB = myBaseDB.get(getNodeType(myNodeName));
		var node = typeDB.get(myNodeName);
		node.clear();
		typeDB.remove(myNodeName);
		myBaseDB.set(getNodeType(myNodeName),typeDB);
	}
}
