autowatch = 1;

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 2;

var undefined = "undefined";

var myTitle = undefined;
var myNodeId = undefined;
var myNodeName = undefined;
var myWorkspaceObject = undefined;

var uniqueTable = new Dict("bs.vpl.unique.title");

var vpl_edit;
var vpl_titleEdit;

var setupdone = false;

var isInEditMode = -1; // this value has to be -1 at the beginning

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
        post("Node " + myNodeId + " / title: " + _message + " \n");
    }
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


function nodeid(_nodeid){
	myNodeId = _nodeid;
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

function setup(){
	dpost("myNodeId: " + myNodeId + "\n");
		
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
	
	setTitle(varName);
	setupdone = true;
}

// creating a unique nodename. if the node was created by the app, it uses the
// already unique nodename from the app.
// called when a new title was entered in the text box or the node is created
function setTitle(_newTitle){
	dpost("setTitle = " + _newTitle + "\n");
	if(_newTitle != null){
		var oldTitle = myTitle;
        _newTitle = escapeRegExp(_newTitle); // correct for special characters
  		if(_newTitle.indexOf(myTitle) != 0 || _newTitle.length != myTitle.length){		
            if((_newTitle.indexOf("_") + 2) == _newTitle.lastIndexOf("_")){
                _newTitle = _newTitle.substring(0, _newTitle.lastIndexOf("_")); 
                dpost("_newTitle: " + _newTitle + "....\n");
            }
			myTitle = _newTitle;
			if(!isUniqueValue(myTitle)){
                dpost("no unique title: " + myTitle + "....\n");
                var counter = 1;
                if(myTitle.indexOf("_") != -1){
                    counter = parseInt(myTitle.substring(myTitle.indexOf("_") + 1, myTitle.length), 10);
                    myTitle = myTitle.substring(0, myTitle.indexOf("_")); 
                }
                var basetitle = myTitle;
  		        while(!isUniqueValue(myTitle)){
                    myTitle = basetitle + "_" + counter++;
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
			uniqueTable.set(myNodeId, myTitle);
			
			//change the scripting name of the node
			if(myWorkspaceObject != null)
				myWorkspaceObject.varname = myTitle;
			 
			// tell the node script about the change
			outlet(0, "title", myTitle);
						
			//set the node title
			outlet(1, "set", myTitle)
		}
  		edit(0);
	}
}

// search for special characters and replace [ with _ and all others with nothing.
// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions#Using_simple_patterns
function escapeRegExp(_text) {
    while(_text.indexOf("]") != -1){
        _text = _text.substring(0, _text.indexOf("]")) + _text.substring(_text.indexOf("]") + 1, _text.length);
    }
    while(_text.indexOf("[") != -1){
        _text = _text.substring(0, _text.indexOf("[")) + "_" + _text.substring(_text.indexOf("[") + 1, _text.length);
    }
    return _text;
//    var res = _text.replace(/[.*+?^${}()|\]\\]/g, ''); // everything else special with nothing
//    return res.replace(/[[]/g, '_'); // replace [ with _
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
			if(ids[i].indexOf(myNodeId) == -1 || (ids[i].indexOf(myNodeId) != -1 && ids[i].length != myNodeId.length)){
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
	dpost("Unique key ("+myNodeId+") to got deleted....\n");
	if(myNodeId != null && uniqueTable.contains(myNodeId)){
		dpost("Unique keys " +uniqueTable.getkeys() + "\n");
		dpost("get value " +uniqueTable.get(myNodeId) + "\n");
		uniqueTable.remove(myNodeId);
		dpost(myNodeId + "...Unique deleted\n");
    }
}
