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

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 5;

var myIOLetButtonSize = 8;
var myIOLetButtonShift = -2;
var myIOLetButtonOffset = -2;

//autowatch = 1;

var undefined = "undefined";

var myNodeBoxSize;
var myNodeDBPath;
var myNodeName = undefined; // fixed name, only initialize inside APP
var myNodeID = undefined; // fixed ID, based on a random but unique number
var myNodeTitle = undefined; // user set node title
var myNodeAddress = undefined; // ossia node address, set through the title
var myNodeProperties = undefined;
var myNodeVarName = undefined;
var myNodeSpace = undefined;
var myNodeColorOn = new Array(1.0, 0.65, 0.0, 1.);
var myNodeColorOff = new Array(0.7, 0.7, 0.7, 0.8);
var myNodeColorSelected = new Array(0.0, 0.0, 0.0, 0.3);
var myNodeColorUnSelected = new Array(0.0, 0.0, 0.0, 0.8);
var myNodeEnable = null;
var myNodeSelected = 0;
var myNodeIsCollapsed = 0;
var myNodeInit = false;
var myNodeTitleYPos = 11;
var myNodeTitleIconSize = 13;

var myColorTable = new Dict("vpl::colortable");
var myType2Color = new Dict("vpl::type2color");
var myBaseDB 	 = new Dict("vpl::db");

var uniqueTable = new Dict("bs.vpl.unique.title");

var connections = new Array();

var vpl_nodeBox;
var vpl_nodePatcher;
var vpl_nodeCanvas;
var vpl_canvas = new Array();
var vpl_windowBar = new Array();
var vpl_nodeEnable;
var vpl_edit;
var vpl_titleEdit;
var vpl_titleBar;
var vpl_help;
var vpl_properties;
var vpl_NodeSpacePatcher;
var vpl_linked = true;

var showProperties = 0;
var showHelp = null;

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

function init(){
//	myNodeVarName = getKeyValuefromDB(myNodeName, "_conn.id");
	dpost("init node..("+myNodeName+") \n");
	initMenu();
	initNodeSpace();
	initNode();
	initNodeBox();
	initConnections();

 	dpost("nodebox initialized: " + myNodeName + ". sending nextlevel\n");
	outlet(0, "initnextlevel", "nodespace", getKeyValuefromDB(myNodeName, "_nodespace") + "::" + getKeyValuefromDB(myNodeName, "_nodespace.idx"), getKeyValuefromDB(myNodeName, "_level") + 1);
	outlet(0, "done");
	myNodeInit = true;

    if(vpl_NodeSpacePatcher != null){
        vpl_NodeSpacePatcher.message("script", "bringtofront", vpl_nodeBox.varname);
    }
	return true;
	dpost("...init("+myNodeName+") done\n");
}

// repopulates the menu items
function initMenu(){
	outlet(4, "icons", "vpl_menu", "clear");
	outlet(4, "icons", "vpl_menu", "append", "properties");
	outlet(4, "icons", "vpl_menu", "append", "help");
	outlet(4, "icons", "vpl_menu", "append", "rename");
	outlet(4, "icons", "vpl_menu", "append", "collapse");
	outlet(4, "icons", "vpl_menu", "append", "---");
	outlet(4, "icons", "vpl_menu", "append", "duplicate");
	outlet(4, "icons", "vpl_menu", "append", "delete");
}


function initNodeSpace(){
	var owner = this.patcher.box;
	if(owner != null){
		owner = this.patcher.box;
		if(owner != null){
			vpl_nodeBox = owner.patcher.box;
			if(vpl_nodeBox != null){
				vpl_NodeSpacePatcher = vpl_nodeBox.patcher;
			}
		}
	}
}

function initNode(){
	//Find  all the important objects in this patcher
	// and initialize them.
	if(vpl_nodePatcher == null){
		// tries to find a text object with the name "vpl_titleEdit"
		if(this.patcher.getnamed("vpl_titleEdit") != null){
			vpl_titleEdit = this.patcher.getnamed("vpl_titleEdit");
			//post(" has vpl_titleEdit...\n");
		}
		if(this.patcher.getnamed("vpl_titleBar") != null){
			vpl_titleBar = this.patcher.getnamed("vpl_titleBar");
			//post(" has vpl_titleBar...\n");
		}
		if(this.patcher.getnamed("vpl_edit") != null){
			vpl_edit = this.patcher.getnamed("vpl_edit");
			//post(" has vpl_edit...\n");
		}
		if(this.patcher.getnamed("vpl_help") != null){
			vpl_help = this.patcher.getnamed("vpl_help");
			//post(" has vpl_help... \n");
		}
		if(this.patcher.getnamed("vpl_properties") != null){
			vpl_properties = this.patcher.getnamed("vpl_properties");
			//post(" has vpl_dispose... \n");
		}
		var client = this.patcher.box;
		while (client) {
			// tries to find a node enable object "enable"
			if(client.patcher.getnamed("enable") != null){
				vpl_nodeEnable = client.patcher.getnamed("enable");
			}

			// tries to find a canvas object with the name "vpl_canvas"
			if(client.patcher.getnamed("vpl_canvas") != null){
				vpl_nodeCanvas = client.patcher.getnamed("vpl_canvas");
			 	dpost(" has vpl_canvas... \n");
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
    dpost("initNodeBox()\n");
	//Find  all the important objects in this patcher
	// and initialize them.

	// calculates this vpl_nodeBox size
	if(vpl_nodeCanvas != null){
 		myNodeBoxSize = vpl_nodeCanvas.rect;
		//dpost("node size  " + myNodeBoxSize[2] + ", " + myNodeBoxSize[3] + "\n");
		if(vpl_nodeBox != null && vpl_nodeBox.varname == myNodeVarName){
 			var myBoxRect = vpl_nodeBox.rect;
			myBoxRect[2] = myBoxRect[0] + myNodeBoxSize[2];
			myBoxRect[3] = myBoxRect[1] +  myNodeBoxSize[3] + myIOLetButtonSize / 2;
//			post("node rect  " +myBoxRect + "\n");
			vpl_nodeBox.rect = myBoxRect;
			storeKeyValueInDB(myNodeName, "_rect", myBoxRect);
		}
		if(vpl_titleBar != null){
			//post(" set vpl_titleBar panel: " + vpl_titleBar.rect + " ... \n");
			vpl_titleBar.message("presentation_rect", 0, 4., myNodeBoxSize[2], 31.);
			//post(" to vpl_titleBar panel: " + vpl_titleBar.rect + " \n");
		}
		//adjust the title field length
		if(vpl_titleEdit != null){
			//var displayBox = vpl_titleEdit.rect;
			//displayBox[2] = myNodeBoxSize[2] - 60;
			//displayBox[3] = displayBox[3] - displayBox[1];
			//post(" found title textfiled: " + vpl_titleEdit.rect + " \n");
			vpl_titleEdit.message("presentation_rect", 21, myNodeTitleYPos,   myNodeBoxSize[2] - 47, 16.);
			vpl_titleEdit.message("border", 0.);
			vpl_titleEdit.message("ignoreclick", 1.);
		}
		if(vpl_edit != null){
			this.patcher.message("script", "sendbox", "vpl_edit", "presentation_rect", myNodeBoxSize[2] - 46, myNodeTitleYPos, myNodeTitleIconSize, myNodeTitleIconSize);
			//vpl_edit.message("presentation_rect", myNodeBoxSize[2] - 58, myNodeTitleYPos, myNodeTitleIconSize, myNodeTitleIconSize);
			this.patcher.message("script", "sendbox", "vpl_edit", "hidden", 0);
			//vpl_edit.message("hidden", 0);
		}
		if(vpl_help != null){
			this.patcher.message("script", "sendbox", "vpl_help", "presentation_rect", myNodeBoxSize[2] - 31, myNodeTitleYPos, myNodeTitleIconSize, myNodeTitleIconSize);
			//vpl_help.message("presentation_rect", myNodeBoxSize[2] - 44, myNodeTitleYPos, myNodeTitleIconSize, myNodeTitleIconSize);
//			post(" showHelp = " + showHelp + " \n");
			if(showHelp != null){
				outlet(4, "icons", "vpl_menu", "enableitem", 1, 1);
				outlet(4, "icons", "vpl_help", "link", showHelp);
			} else {
				outlet(4, "icons", "vpl_menu", "enableitem", 1, 0);
				//vpl_help.message("ignoreclick", 1);
			}
		}
		if(vpl_properties != null){
			this.patcher.message("script", "sendbox", "vpl_properties", "presentation_rect", myNodeBoxSize[2] - 16, myNodeTitleYPos, myNodeTitleIconSize, myNodeTitleIconSize);
			//vpl_properties.message("presentation_rect", myNodeBoxSize[2] - 30, myNodeTitleYPos, myNodeTitleIconSize, myNodeTitleIconSize);
			//post("init myNodeProperties" + myNodeProperties + "\n");
			if(myNodeProperties != undefined){
				outlet(4, "icons", "vpl_menu", "enableitem", 0, 1);
			} else {
				outlet(4, "icons", "vpl_menu", "enableitem", 0, 0);
			}
		}
	}else{
		post(" found no canvas \n");
	}

	if(myNodeBoxSize != null){
        dpost("myNodeBoxSize is not null...\n");
		var objects = vpl_nodePatcher.firstobject;
		while(objects != null){
			// find all bpatcher with the script name inlet and outlet and set
			// their postion according to their ids
 			if(objects.varname.indexOf("vpl_inlet") == 0){
				var xpos = getIOLetXPos(objects);
				vpl_nodePatcher.message("script", "sendbox", objects.varname, "presentation_rect", xpos + myIOLetButtonOffset, 0., myIOLetButtonSize, myIOLetButtonSize);
				var subpat = objects.subpatcher();
				if(subpat != null){
					subpat.apply(initIOlets);
					//post(" found subpatcher: " + subpat + "\n");
				}
			}else if(objects.varname.indexOf("vpl_outlet") == 0){
				var xpos = getIOLetXPos(objects);
				vpl_nodePatcher.message("script", "sendbox", objects.varname, "presentation_rect", xpos + myIOLetButtonOffset, myNodeBoxSize[3] + myNodeBoxSize[1] - myIOLetButtonSize + myIOLetButtonShift, myIOLetButtonSize, myIOLetButtonSize);
				var subpat = objects.subpatcher();
				if(subpat != null){
					subpat.apply(initIOlets);
					//post(" found subpatcher: " + subpat.box.varname + "\n");
				}
			}

			// find all vpl_canvas object in this node to set their colors.
 			if(objects.varname.indexOf("vpl_canvas") == 0){
                dpost("found more vpl_canvases...\n");
				vpl_canvas.push(objects);
			}
			// try and find subwindows
			var subpat = objects.subpatcher();
			if(subpat != null){
				var subcanv = subpat.getnamed("vpl_canvas");
				if(subcanv != null){
					// it found a property window (indicated by the existence
					// of a vpl_canvas object
					vpl_canvas.push(subcanv);
					// now try to find the bs.gui.windows.pops
					var sobj = subpat.firstobject;
					while(sobj != null){
						// step through all the objects
						var poppat = sobj.subpatcher();
						if(poppat != null){
							// if there is one with vpl_windowBar
							var windowBar = poppat.getnamed("vpl_windowBar");
							if(windowBar != null){
								// store it
								vpl_windowBar.push(windowBar);
								//post("vpl_windowBar\n")
							}
						}
						sobj = sobj.nextobject;
					}
				}

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
				var con = getKeyValuefromDB(myNodeName, keys[i]);
				connection(con[0], con[1], con[2], con[3], con[4], con[5]);
			}
		}
	}
}

function initIOlets(a) {
	//find all buttons with the script name inlet and outlet and set
	// their colors according to their type
	if(a.maxclass.indexOf("led") != -1){
		var localThisPatch = a.patcher;
//		post("found button: ->|" + a.varname + "|<- \n");
		var types = a.varname;
		var color = getColor(types);
		//post(" setting colors for buttons: " + a.varname + " for types " + types + "\n");
		if(color != null){
			//post("found color: " + color + "\n");
			var colormsg = myColorTable.get(color);
			//post("found message: " + colormsg + "\n");
			a.message("oncolor", colormsg[2] * 0.5, colormsg[3] * 0.5, colormsg[4] * 0.5, colormsg[5]);
			a.message("offcolor", colormsg[2], colormsg[3], colormsg[4], colormsg[5]);
			//a.message("bgcolor", colormsg[2], colormsg[3], colormsg[4], colormsg[5]);
//			a.message(colormsg[1], colormsg[2], colormsg[3], colormsg[4], colormsg[5]);
		}else{
			a.message("oncolor", .25, .25, .25, 1.);
			a.message("offcolor", .5, .5, .5, 1.);
			//a.message("bgcolor", .5, .5, .5, 1.);
//			a.message("fgcolor", .5, .5, .5, 1.);
		}

		localThisPatch.message("script", "sendbox", a.varname, "size", myIOLetButtonSize, myIOLetButtonSize);
	}

	return true;
}

/**********************
  Call Functions
 **********************/

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
	outlet(3, "_control", "nodeid", _nodeid);
	outlet(3, "nodeid", _nodeid);

}

// called by the menu
function openproperties(){
    //post("openproperties: myNodeTitle = " + myNodeTitle + " | myNodeID = " + myNodeID + " \n");
    //appGlobal.currentnodetitle = myNodeTitle;
    //appGlobal.currentnodeid = myNodeID;
    //appGlobal.currentproperties = myNodeProperties;
    if(myNodeProperties != undefined && myNodeProperties != 1){
        outlet(2, "shroud", "bs.vpl.node.property", myNodeID, myNodeTitle, myNodeAddress, myNodeProperties);
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
    myNodeAddress = "sparck:/node/" + newtitle;
	storeKeyValueInDB(myNodeName, "_title", newtitle);
	myNodeVarName = newtitle;
    // the sequence of the following messages matter:
    //    we first need to set the title
    //         used by: vpl.node.property.logic
    messnamed("bs::app::node::property::" + myNodeID, "titleChange", myNodeTitle);
    messnamed("bs::app::node::property::" + myNodeID, "addressChange", myNodeAddress);
	outlet(3, "setmsgtitle", newtitle);
	outlet(3, "title", newtitle);
	outlet(3, "address", myNodeAddress);
	outlet(3, "_control", "title", newtitle);
}

function color(red, green, blue, alpha){
	myNodeColorOn[0] = red;
	myNodeColorOn[1] = green;
	myNodeColorOn[2] = blue;
	myNodeColorOn[3] = alpha;
	setGUIColors();
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
		outlet(3, "_control", "enable", _enable);
		outlet(3, "enable", _enable);
		// for good measure, set the toogle
		outlet(1, "set", _enable);
	}
}

// sets the type of node
function logo(_logo){
//	nrou, "logo", "read", "bs.node.logo." + _logo + ".png");
	//vpl_nodeEnable.message("read", "bs.node.logo." + _logo + ".png");
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
//	cpost("got selected ("+_select+")... (" + myNodeName + ")\n");
	if(vpl_linked){
//		cpost("... selected is linked... (" + myNodeName + ")\n");
        if(appGlobal.selectedNode == null){
            appGlobal.selectedNode = "undefined";
        }
//		cpost("... selected has appGlobal.selectedNode = "+appGlobal.selectedNode+"... (" + myNodeName + ")\n");
		//post("got selected: " + myNodeTitle + ": " + _select + " \n");
		if(_select){
			vpl_NodeSpacePatcher.message("script", "bringtofront", vpl_nodeBox.varname);
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

// called by the menu
function menu(_func){
	if(_func == "properties"){
		openproperties();
	} else if(_func == "collapse"){
		collapse(1);
	} else if(_func == "expand"){
		collapse(0);
	} else if(_func == "duplicate"){
		;
	} else if(_func == "delete"){
		;
	}
}


function collapse(_collapsed){
    myNodeIsCollapsed = _collapsed;
	if(vpl_nodeCanvas != null){
 		myNodeBoxSize = vpl_nodeCanvas.rect;
		//dpost("node size  " + myNodeBoxSize[2] + ", " + myNodeBoxSize[3] + "\n");
		if(vpl_nodeBox != null && vpl_nodeBox.varname == myNodeVarName){
            var myBoxRect = vpl_nodeBox.rect;
			myBoxRect[3] = myBoxRect[1] + ((myNodeIsCollapsed == 1)? 40:myNodeBoxSize[3] + myIOLetButtonSize / 2);
//			post("node rect  " +myBoxRect + "\n");
			vpl_nodeBox.rect = myBoxRect;
			//storeKeyValueInDB(myNodeName, "_rect", myBoxRect);
			outlet(4, "icons", "vpl_menu", "setitem", 3, (myNodeIsCollapsed == 1)?"expand":"collapse");
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
	storeKeyValueInDB(myNodeName, "_rect", myBoxRect);
//	vpl_NodeSpacePatcher.message("front"); <- slows down patch rendering
}

function openworkspace(){
	//outlet(3, "_control", "openproperties");
	if(vpl_nodeBox.patcher != null){
		vpl_nodeBox.patcher.message("front");
    }
}

function help(val){
	showHelp = val;
    vpl_titleBar.message("hint", "NodeType: " + showHelp);
}

function properties(val){
	//post("set myNodeProperties" + myNodeProperties + "\n");
    myNodeProperties = val;
 }

// called when the node is removed by the user or a new project is loaded,
// but NOT when the application is quited -> this is important, because window-node would
// create a message sent to the window.context which causes a crash...
function dispose(){
//	initNode();
	dpost("dispose function called\n");
    enable(0); // sends enable 0 messages...
	outlet(3, "dispose");

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
// 	cpost(myNodeName + " got deleted\n");
}


/**********************
  Managing GUI / Colors
 **********************/

function setGUIColors(){
	dpost("setGUIColors()\n");
	initNode();

	var workingcolor = myNodeColorOn;
	if(myNodeEnable == 0)
		workingcolor = myNodeColorOff;

	// sets the color pf all vpl_canvas objects within the node
	if(vpl_canvas instanceof Array){
        dpost("canvas is array: "+vpl_canvas.length+"\n");
		for(var i = 0; i < vpl_canvas.length; i++){
//			vpl_canvas[i].message("bordercolor", 0., 0., 0., 1.);
//			vpl_canvas[i].message("borderoncolor", 1., 1., 1., 1.);
//			vpl_canvas[i].message("bgovercolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]- 0.05 );
//			vpl_canvas[i].message("bgoncolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
//			vpl_canvas[i].message("bgcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
			if(vpl_canvas[i].understands("bgfillcolor")){
                //post("bgfillcolor\n");
				vpl_canvas[i].message("bgfillcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
            }
			if(vpl_canvas[i].understands("bgcolor")){
                //post("bgcolor\n");
				vpl_canvas[i].message("bgcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
            }
		}
	}else{
//		vpl_canvas.message("bordercolor", 0., 0., 0., 1.);
//		vpl_canvas.message("borderoncolor", 1., 1., 1., 1.);
//		vpl_canvas.message("bgovercolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]- 0.05 );
//		vpl_canvas.message("bgoncolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
//		vpl_canvas.message("bgcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
		if(vpl_canvas[i].understands("bgfillcolor")){
            //post("bgfillcolor\n");
			vpl_canvas.message("bgfillcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
        }
		if(vpl_canvas[i].understands("bgcolor")){
            //post("bgcolor\n");
			vpl_canvas.message("bgcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
        }
	}

	if(vpl_nodeEnable != null){
//		vpl_nodeEnable.message("bordercolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]- 0.05 );
	}

	// setting the title bar
	workingcolor = (myNodeSelected == 1)? myNodeColorSelected:myNodeColorUnSelected;
	if(vpl_titleBar != null)
		vpl_titleBar.message("bgcolor", workingcolor);

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
	vpl_NodeSpacePatcher.message("script", "connectcolor", getKeyValuefromDB(conn.outaddress, "_title"), conn.outid-1, vpl_nodeBox.varname, conn.inid-1, getColorID(conn.intype));
}

function removeBoxConnection(conn){
	vpl_NodeSpacePatcher.disconnect(vpl_NodeSpacePatcher.getnamed(getKeyValuefromDB(conn.outaddress, "_title")), conn.outid-1, vpl_nodeBox, conn.inid-1);
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
