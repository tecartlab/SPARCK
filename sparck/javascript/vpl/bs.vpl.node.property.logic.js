//autowatch = 1;

var undefined = "undefined";
var LEFTBORDERDIST = 1;
var RIGHTBORDERDIST = 1;
var BOTTOMBORDERDIST = 5;
var TOPBORDERDIST = 23;
var SNAPDISTANCE = BOTTOMBORDERDIST + 5;

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 4;
setinletassist(0,"input");
setoutletassist(0,"output to patcher");
setoutletassist(1,"output to ui");

var myNodeColorOn = new Array(1.0, 0.65, 0.0, 1.);
var myNodeColorOff = new Array(0.7, 0.7, 0.7, 0.8);
var myNodeColorSelected = new Array(0.0, 0.0, 0.0, 0.3);
var myNodeColorUnSelected = new Array(0.0, 0.0, 0.0, 0.8);

var managerPath = "bs::gui::window::manger";
var updatePath = "bs::gui::windows::updates";

var myEstateDB = new Dict("bs::gui::windows::db");
var myWindowDBName = null;
var myWindowDict = null;
var myStoreDBName = null;
var myStoreDict = null;

var myID = null;
var myNodeId = null;
//var myStoreName = null;

var myWindow = null;
var myWindowTitle = "Props";
var myWindowIsInit = false;

var myClientCanvas = null;

var myRect = null;
var mySize = null;
var mySizeCollapsed = null;
var myCollapsedHeight = 45;
var myWinIsCollabsable = true;
var myWinIsCloseable = true;
var myWinIsActive = false;
var myVisibility = false;
var myWinIsSlotted = false;
var mySlot = -1;
var myPin = 0;

var myPrefix = null;
var	myType = null;

var myStore = true;

var mouseX 

//Default Flags:
var myFlagClose = "close";
var myFlagZoom = "zoom";
var myFlagMinimize = "nominimize";
var myFlagGrow = "nogrow";
var myFlagFloat = "float";
var myFlagTitle = "title";
var myFlagMenu = "nomenu";

var myFlagsAreEnabled = false;

var desktop_rect;

var useWindowManagement = true;

var isDone = false;

var appGlobal = new Global("bs::app::global");

var debug = (appGlobal.debug == 1 || appGlobal.debug == 5)? true:false;

// set the debugger
function debugger(_val){
	debug = (_val == 1)? true: false;
}

function dpost(_message){
    if(debug){
        post("node.property->" + myID + ": " + _message + " \n");
    }
}


function loadbang(){
}

function done(){
 	if(!isDone){
		if(this.patcher.box != null){
			myWindow = this.patcher.box.patcher.wind;
            // if it is an oldfashoned
			myClientCanvas = this.patcher.box.patcher.getnamed("vpl_canvas");
			if(myClientCanvas != null){
 				mySize = new Array(myClientCanvas.rect[2], myClientCanvas.rect[3]);	
            }

            var myClientProperties = this.patcher.box.patcher.getnamed("vpl_properties");
            if(myClientProperties != null){
                var myproperty = myClientProperties.subpatcher();
                if(myproperty != null){
                    // first we are looking for the full size of the properties
                    var myPropertyCanvas = myproperty.getnamed("vpl_canvas_full");
                    if(myPropertyCanvas != null){                    
                        mySize = new Array(myPropertyCanvas.rect[2], myPropertyCanvas.rect[3]);
                        //post("myPropertyCanvas.rect: " + myPropertyCanvas.rect + "\n");
                        //post("mySize: " + mySize + "\n");
                        //myClientProperties.message("presentation_rect", 0, 0, theSize[0], theSize[1]);
                        outlet(0, "script", "sendbox", "vpl_properties", "presentation_rect", 0, 0, mySize[0], mySize[1]);
                        //myClientProperties.presentation_rect = new Array(0,0,theSize[0], theSize[1]);
                    }else {
                        post("this shouldnt happen: loaded property patcher doesn't contain a 'vpl_canvas_full'!!\n");
                    }
                    // then we are looking for the collapsed size of the properties
                    myPropertyCanvas = myproperty.getnamed("vpl_canvas_collapsed");
                    if(myPropertyCanvas != null){                    
                        myCollapsedHeight = myPropertyCanvas.rect[3];
                        //post("myCollapsedHeight: " + myCollapsedHeight + "\n");
                    } else {
                        post("this shouldnt happen: loaded property patcher doesn't contain a 'vpl_canvas_collapsed'!!\n");
                    }
                } 
			} 
		}
		//var sysprops = new Dict("msg::bs::system::settings");
		desktop_rect = new Array(0, 0, 1280, 720);
		if(appGlobal.desktop_rect != null){
			//post("pops:done( Display Settings are set: "+appGlobal.desktop_rect+" )\n");
			
			desktop_rect = appGlobal.desktop_rect;
		}
        
		isDone = true;
        //outlet(1, "address", appGlobal.currentnode);
        //window(1);
		//post("pops: size = " + mySize + "\n");
		//post("pops:done( " + desktop_rect + " )\n");

        //title(appGlobal.currentnode);
	}
}

// called by node.logic of this properties parent node
function titleChange(_newtitle){
	dpost("received title change= " + _newtitle + "\n");
    title(_newtitle);
}

// sets the title
function title(_title){
	//post("pops:title() = " + _title + "\n");
    
	if(myID != null){
		clearMyWindowDict();
	}
	myID = _title;
	
	myWindowDBName = "bs_gui_win_" + myID;
	if(myPrefix != null)
 		myWindowDBName += "_" + myPrefix;
	if(myType != null)
 		myWindowDBName += "_" + myType;

	myStoreDBName = myWindowDBName + "_store";

	//post("title() myStoreDBName = " + myStoreDBName + "\n");

	refresh();

	myWindowTitle = "";
	myWindowTitle = _title;
	if(myType != null)
		myWindowTitle = myWindowTitle;
//		myWindowTitle = myWindowTitle + " (" + myType + ")";
	
	outlet(0, "title", myWindowTitle);
	outlet(3, "title", myWindowTitle);
}

// called by node.logic of this properties parent node
function addressChange(_newAddress){
	dpost("received address change= " + _newAddress + "\n");
    address(_newAddress);
}

function address(_address){
    outlet(0, "script", "sendbox", "vpl_properties", "args", myID, "@address", _address);    
}

function nodeid(_nodeId){
    myNodeId = _nodeId;
    outlet(2, "send", "bs::app::node::" + _nodeId);
}

function enable(_enable){
    var workingcolor = myNodeColorOn;
	if(_enable == 0)
		workingcolor = myNodeColorOff;

    if(myClientCanvas.understands("bgfillcolor")){
        //post("bgfillcolor\n");
        myClientCanvas.message("bgfillcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
    } 
    if(myClientCanvas.understands("bgcolor")){
        //post("bgcolor\n");
        myClientCanvas.message("bgcolor", workingcolor[0], workingcolor[1], workingcolor[2], workingcolor[3]);
    } 
}

/*******************
/ MANAGER FUNCTIONS
/*******************/

//Resets the window to stored data
function refresh(){
	if(useWindowManagement && myWindowDBName != null){
		loadMyWindowDict(); //some data is maintained on managment side
		saveMyWindowDict(); //some data is maintained on window side
	}
}

function notifydeleted(){
	clearMyWindowDict();
}

/*********************
/ DICT / DB FUNCTIONS
/*********************/

// loads a stored settings
function load(_storename){
//	post("winpops:  load = " + _storename + "\n");
	if(useWindowManagement){
		if(myWindowDict != null && myWindowDict.contains(_storename)){
			cleanupslotspots();
			
			myStoreDict = myWindowDict.get(_storename);
			// get the attributes from the dict and set the object
			myVisibility = myStoreDict.get("_visible");
			myWinIsSlotted = myStoreDict.get("_slotted");
			if(myWinIsSlotted)
				collapse(1);
		}
	}
}

// saves the current settings in the store
function save(_storename){
	//post("winpops:  save = " + _storename + "\n");
	if(useWindowManagement){
		queryWindow();
		if(myWindowDict != null && myWindowIsInit){
			//post("winpops: saveMyStoreDict() " + _storename + "\n");
			if(myStoreDict == null){
				//post("winpops: createStoreDB = " + myStoreDBName + "\n");
				myStoreDict = new Dict(myStoreDBName);
			}
			myStoreDict.clear();
					
			// get the attributes from the object and store them here in the dict
			myStoreDict.set("_visible", myVisibility);
			myStoreDict.set("_slotted", myWinIsSlotted);
	
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
			// get the attributes from the dict and set the object
			//post("loadMyWindowDict:  myPin = " + myPin + "\n");
			myPin = myWindowDict.get("_pin");
			//post("loadMyWindowDict:  mySlot = " + mySlot + "\n");			
			pin(myPin); 
			
		}//else
			//post("loadMyWindowDict:  myEstateDB  doesnot contain " + myWindowDBName + "\n");
	}
	//post("winpops: loadMyWindowDict(): done \n");
}

function saveMyWindowDict(){
	if(useWindowManagement){
		if(myWindowDict == null)
			myWindowDict = new Dict(myWindowDBName);

		//post("winpops: saveMyWindowDict(): " + myWindowDBName + " \n");
			
		//myWindowDict.clear();
		// get the attributes from the object and store them here in the dict
		myWindowDict.set("_pin", myPin);
		myWindowDict.set("_type", myType);
		myWindowDict.set("_slotsize", myCollapsedHeight);

		if(myEstateDB.contains(myWindowDBName))
			myEstateDB.replace(myWindowDBName, myWindowDict);
		else
			myEstateDB.set(myWindowDBName, myWindowDict);
	}
//	post("winpops: saveMyWindowDict(): done \n");
}

function clearMyWindowDict(){
	if(useWindowManagement && myEstateDB != null && myWindowDBName != null)
		if(myEstateDB.contains(myWindowDBName))
			myEstateDB.remove(myWindowDBName);
}

/*********************
/ SLOT FUNCTIONS
/*********************/


// locally called by the updateRect function if the pops is pinned and if the pops
// is collapsed.
function getSlotSpot(){
	if(useWindowManagement){
//		post(myWindowTitle + "::getSlotSpot() mySlot=" + mySlot + "\n");
		if(mySlot < 0){
			if(myEstateDB.contains("_slotspot_" + myType)){	
				//if there is a slotspot designated for this type			
//				post(myWindowTitle + "::calling slotrefresh: myWindowTitle=" + myWindowTitle + " _seekedSpot=" + myEstateDB.get("_slotspot_" + myType) + " (myCollapsedHeight + 1)=" + (myCollapsedHeight + 1) + "\n");
				
				//tell all other nodes to make space
				messnamed(updatePath, "slotrefresh", myWindowTitle, myType, myEstateDB.get("_slotspot_" + myType), (myCollapsedHeight + 1));
				
				//take the designated slotspot for this type
				mySlot = myEstateDB.get("_slotspot_" + myType);
				
				//shift the last slotspot accordingly 
				myEstateDB.replace("_slotspot", myEstateDB.get("_slotspot") + myCollapsedHeight + 1);
				myEstateDB.replace("_slotspot_" + myType + "_counter", myEstateDB.get("_slotspot_" + myType + "_counter") + 1);
				
			}else if(myEstateDB.contains("_slotspot")){				
				//if there is no slotspot designated for this type, but a last slotspot		
//				post(myWindowTitle + "::calling slotrefresh: myWindowTitle=" + myWindowTitle + " _seekedSpot=" + myEstateDB.get("_slotspot") + " (myCollapsedHeight + 1)=" + (myCollapsedHeight + 1) + "\n");

				//take the last slotspot
				mySlot = myEstateDB.get("_slotspot");
								
				//shift the last slotspot accordingly 
				myEstateDB.replace("_slotspot", myEstateDB.get("_slotspot") + myCollapsedHeight + 1);
				// and create a designate slotspot for this typ at my spot
				myEstateDB.set("_slotspot_" + myType, mySlot);
				myEstateDB.set("_slotspot_" + myType + "_counter", 1);
			} else{
				mySlot = 0;
				
				myEstateDB.set("_slotspot", mySlot + myCollapsedHeight + 1);
				myEstateDB.set("_slotspot_" + myType, mySlot)
				myEstateDB.set("_slotspot_" + myType + "_counter", 1);
			}
		}
	}
	return mySlot;
}

// locally called when the slot spot needs to be release 
// - i.e. when the pops is unpinned
function releaseSlotSpot(){
	//post("releaseSlotSpot(): " + myWindowTitle + " active: " + myWinIsActive + " mySlot: " + mySlot + "\n");
	if(useWindowManagement){
		if(mySlot >= 0){
			if(myEstateDB.contains("_slotspot"))
				myEstateDB.replace("_slotspot", myEstateDB.get("_slotspot") - (myCollapsedHeight + 1));

			if(myEstateDB.contains("_slotspot_" + myType)){
				if(myEstateDB.get("_slotspot_" + myType + "_counter") > 1){
					myEstateDB.set("_slotspot_" + myType + "_counter", myEstateDB.get("_slotspot_" + myType + "_counter") - 1);
				} else {
					myEstateDB.remove("_slotspot_" + myType + "_counter");
					myEstateDB.remove("_slotspot_" + myType);
				}
			}

			var sendslot = mySlot;
			mySlot = -1;
			messnamed(updatePath, "slotrefresh", myWindowTitle, myType, sendslot, - (myCollapsedHeight + 1));
			//post("releaseSlotSpot(): slotrefresh sent\n");
		}
	}
}

// Externally called by another pops (-> _windowTitle), if it 
// releases itself from a slot and all other pops with slots have to reajust
function slotrefresh(_windowTitle, _type, _openSpot, _slotShift){
	if(useWindowManagement && myWindowTitle != _windowTitle){
//		post(myWindowTitle + "::slotrefresh() _windowTitle=" + _windowTitle + " _openSpot=" + _openSpot + " _slotShift=" + _slotShift + " mySlot=" + mySlot + "\n");
		if(mySlot >= _openSpot){				
			if(myEstateDB.contains("_slotspot_" + myType)){
//				post(myWindowTitle + "::slotrefresh() myEstateDB.get(_slotspot_" + myType + "): " +  myEstateDB.get("_slotspot_" + myType) + "\n");
				var nextSlotSpot = mySlot + _slotShift;
				if(myType != _type && myEstateDB.get("_slotspot_" + myType) == mySlot){
					myEstateDB.replace("_slotspot_" + myType, nextSlotSpot);
//					post(myWindowTitle + "::slotrefresh() myEstateDB.replace(_slotspot_" + myType + "): " +  nextSlotSpot + "\n");
				}
				mySlot = nextSlotSpot;
				//post("myEstateDB.get(): " +  mySlot + "\n");
			}
//			post(myWindowTitle + "::slotrefresh() updating window mySlot=" + mySlot + "\n");
			updateRectAbsolute(myRect[0], mySlot);
			updateWindow();
		}
	}
}

// Externally called by another pops (-> _windowTitle), if it 
// uncaollapses itself from a slot and all other pops with slots have to collapse
function slotcollapse(_windowTitle){
	if(useWindowManagement && myWindowTitle != _windowTitle){
		//post(myWindowTitle + "::slotcollapse() _windowTitle=" + _windowTitle + "\n");
		if(myWinIsSlotted && !mySizeIsCollapsed){
			collapse(1);
			//post(myWindowTitle + "::slotcollapse() collapsed my window\n");
		}
	}
}	

// called by the manager to clean up slotspots
function cleanupslotspots(){
	if(useWindowManagement){
		if(myEstateDB.contains("_slotspot_" + myType)){
			myEstateDB.remove("_slotspot_" + myType);
			myEstateDB.remove("_slotspot_" + myType + "_counter");
		}
	}
}

// called by the manager to collapse all pinned pops of the specified type
// and allow them to gather 
function listtype(_type){
	if(useWindowManagement){
		if(myType == _type || (_type == "<pinned>" && myPin == 1)){
			mySlot = -1;
			myVisibility = true;
			mySizeIsCollapsed = true;
			getSlotSpot(); // gets a new slotspot
			updateRectAbsolute(desktop_rect[2] - mySize[0] - RIGHTBORDERDIST, TOPBORDERDIST + mySlot);
			updateWindow();
		}else{
			myVisibility = false;
			mySlot = -1;
			setcollapse(0);
			updateWindow();
		}
	}
}


/***********************************
/ Window query and update functions
/***********************************/

function queryWindow(){
	if(myWindow != null && myWindowIsInit){
		//post("winpops: queryWindow() myWindow.location=" + myWindow.location + "\n");
		updateRectAbsolute(myWindow.location[0], myWindow.location[1]);
		myVisibility = myWindow.visible;
	}
}

function updateRect(){
	//post("winpops:updateRect() title: " + myWindowTitle + "...\n");
	if(myRect != null){
		//post("winpops:updateRect() title: " + myWindowTitle + " | myRect: " + myRect +"\n");
		updateRectAbsolute(myRect[0], myRect[1]);
	}else{
		//post("winpops:updateRect() title: " + myWindowTitle + " | myRect = null\n");
		updateRectAbsolute(0, 0);
	}
}

function updateRectRelative(_diffX, _diffY){
//	post("winpops:updateRectRelative() title: " + myWindowTitle + "...\n");
	if(myRect != null){
//		post("winpops:updateRect() title: " + myWindowTitle + " | myRect: " + myRect +"\n");
		updateRectAbsolute(myRect[0] + _diffX, myRect[1] + _diffY);
	}
}

function updateRectAbsolute(_posX, _posY){
	// make sure to move within the designated desktop area
	_posX = (_posX > LEFTBORDERDIST)? _posX: LEFTBORDERDIST;
	_posY = (_posY > TOPBORDERDIST)? _posY: TOPBORDERDIST;
	//if(debug)
	//post("bs.gui.windows.pops/updateRectAbsolute(_posX, _posY) title: " + myWindowTitle + " | _posX: " +_posX + " | _posY "+_posY+"\n");
		
	//post("props: " + myWindowTitle + " updateRect " + _posX + " | " + _posY + "\n");
//	post("winpops:updateRectAbsolute(_posX, _posY) title: " + myWindowTitle + " | mySize " + mySize[0] + " | " + mySize[1] + "\n");
//	post("winpops:updateRectAbsolute(_posX, _posY) title: " + myWindowTitle + " | desktop_rect " + desktop_rect[0] + " | " + desktop_rect[1] + "\n");

	myRect = new Array();
	if(mySlot >= 0){
		myRect[0] = desktop_rect[2] - (2 - ((mySizeIsCollapsed)?1:0)) * (mySize[0] + RIGHTBORDERDIST);
		// if the slot is to low, the expanded view is set above the slot so the complete window can be seen
		myRect[1] = ((TOPBORDERDIST + mySlot + ((mySizeIsCollapsed)?myCollapsedHeight:mySize[1])) > desktop_rect[3])?desktop_rect[3] - ((mySizeIsCollapsed)?myCollapsedHeight:mySize[1]):TOPBORDERDIST + mySlot;
		myRect[2] = myRect[0] + mySize[0];
		myRect[3] = myRect[1] + ((mySizeIsCollapsed)?myCollapsedHeight:mySize[1]);
	} else {
		myRect[0] = ((_posX + mySize[0] + RIGHTBORDERDIST) < desktop_rect[2])?_posX:(desktop_rect[2] - mySize[0] - RIGHTBORDERDIST);
		myRect[2] = myRect[0] + mySize[0];
		myRect[1] = ((_posY + mySize[1] + BOTTOMBORDERDIST) < desktop_rect[3])?_posY:(desktop_rect[3] - mySize[1] - BOTTOMBORDERDIST);
		myRect[3] = myRect[1] + mySize[1];
	}

    //post("bs.gui.windows.pops/updateRectAbsolute(_posX, _posY) myCollapsedHeight: " + myCollapsedHeight + "\n");
	
    myClientCanvas.message("presentation_rect", 0, 0, mySize[0], ((mySizeIsCollapsed)?myCollapsedHeight:mySize[1]));
	
	//if(debug)
	//post("bs.gui.windows.pops/updateRectAbsolute(_posX, _posY) title: " + myWindowTitle + " | myRect: " + myRect +"\n");
}

function updateWindow(){
	//if(debug)
	//post("bs.gui.windows.pops/updateWindow() title: " + myWindowTitle + " | myRect: " + myRect +"\n");
	if(myWindow != null){
		//post("bs.gui.windows.pops/updateWindow() title: " + myWindowTitle + " | myWindow != null\n");
		if(myRect != null & mySize != null){			
			outlet(0, "window", "size", myRect);
			outlet(0, "window", "exec");
			outlet(0, "window", "size", myRect); // bug fix repetition
			outlet(0, "window", "exec");
		}
		if(myVisibility){
			outlet(0, "front");
            //post("bs.gui.windows.pops/updateWindow() title: " + myWindowTitle + " | myVisibility\n");
//			post("winpops:updateWindow() myWindowIsInit = " + myWindowIsInit + "\n");
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

// opens the window at the specified mouse coordinates
function mouse(_mouseX, _mouseY){
    //if(debug)
    //post("winpops:mouse() title: " + myWindowTitle + " | mouse: x" + _mouseX + "  y" + _mouseY + " \n");
	myVisibility = true;
	setcollapse(0);

	if(mySlot >= 0){
		releaseSlotSpot();
		myWinIsSlotted = false;		
	}

	updateRectAbsolute(_mouseX, _mouseY);
	updateWindow();
	updateGUI();
	//collapse(0);
}

// sets the pin
function pin(_pin){
	//post("winpops:pin() title: " + myWindowTitle + " | pin " + _pin + "\n");
	queryWindow();

	myPin = _pin;
	if(myWinIsCollabsable)
		collapse(0);
	if(mySlot >= 0){
		releaseSlotSpot();
		myWinIsSlotted = false;		
	}
		
	updateGUI();
	//updateRect();
	updateWindow();
}

// opens the window at its current location
function winopen(){
//	post("winpops:winopen() title: " + myWindowTitle + "\n");
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
    if(debug){
        //post("winpops:active() title: " + myWindowTitle + " | acive: " + i + "\n");
        //post("winpops:active() title: " + myWindowTitle + " | myFlagsAreEnabled: " + myFlagsAreEnabled + "\n");
    }
	myWinIsActive = (i == 1)? true: false;
	if(myFlagsAreEnabled){
        if(debug){
            //post("winpops:active() title: " + myWindowTitle + " | myPin: " + myPin + "\n");
		}
		if(i == 1){
			myWindowIsInit = true;
		}else
			if(myPin == 0)
				winclose();
	}
}

// the drag of window
function drag(diffX, diffY){
//	post("winpops:drag() title: " + myWindowTitle + " active: " + myWinIsActive + " diffs: " + diffX + " / "+diffY+"\n");
	if(myWinIsActive){
		if(myRect == null){
			myWindowIsInit = true;
//			post("winpops:drag() myWindowIsInit = " + myWindowIsInit + "\n");
			queryWindow();
		}
		if(myRect != null){
//			post("myRect pre: " + myRect[0] + " : "+myRect[1]+"\n");
			updateRectRelative(diffX, diffY);
			updateWindow();
		}
//	post("myRect post: " + myRect[0] + " : "+myRect[1]+"\n");
	}
}

// sets the type of information
function prefix(_prefix){
	myPrefix = _prefix;
}

// sets the type of node
function type(_type){
	myType = _type;
}

// sets the type of node
function logo(_logo){
	//outlet(3, "logo", "read", "bs.node.logo." + _logo + ".png");
}

//set if the window is managed by the windows manager and saves its data
// inside the windows db.
function managed(_val){
	useWindowManagement = (_val == 1)?true: false;
}

//set if the loacate button is active or not (default is active)
function locating(_val){
	outlet(3, "locating", "active", _val);
}
	
// title was clicket
function locate(){
	// brings the nodespace to the front inside which the node is placed
	//messnamed(myNodeID + "::vpl::connection", "openworkspace");
	outlet(2, "openworkspace");
	outlet(2, "select", 1);
}

// shows the close button (default = 1)
function closeable(_flg){
	myWinIsCloseable = (_flg == 1)?true: false;
	updateGUI();
}

// closes the window
function close(_close){
	winclose();
}

// closes the window
function pressed_esc(){
	winclose();
}

// sets the height of the window in its collapsed state
function collapsedheight(_height){
	myCollapsedHeight = _height;
}

// shows the collapse button (default = 1)
function collapsable(_flg){
	myWinIsCollabsable = (_flg == 1)?true: false;
	//setcollapse(0);
}

// collapses / uncollapses the window
// if it collapses it als o gets slotted
function setcollapse(_collapse){
	mySizeIsCollapsed = (_collapse == 1)?true: false;
	if(mySizeIsCollapsed){
		getSlotSpot();
		myWinIsSlotted = true;		
	}
}

// collapses / uncollapses the window
function collapse(_collapse){
	//post("pops:collapse() " + _collapse + "\n");
	setcollapse(_collapse);
	updateGUI();
	updateRect();
	// a hack to make sure the window is realy at its designated position
	updateWindow();
	updateWindow();
	updateWindow();
	// this collapses all other sloted windows to make way
	if(myWinIsSlotted && !mySizeIsCollapsed){
		messnamed(updatePath, "slotcollapse", myWindowTitle);
	}
}

function updateGUI(){
	//post("pops:updateGUI()\n");
	outlet(3, "collapse", "set", (mySizeIsCollapsed == true)?1: 0);
	outlet(3, "collapse", "active", 1 - (myWinIsCollabsable == true && myPin == 1)?0: 1);
//	outlet(3, "close", "hidden", 1 - (myWinIsCloseable == true && myPin == 1)?1: 0);
	outlet(3, "pin", "set", myPin);
}

function notifydeleted(){
	releaseSlotSpot();
}

function desktop_rect(_left, _top, _right, _bottom){
	desktop_rect = new Array(_left, _top, _right, _bottom);
}

function anything(){
	if (inlet==0){
		if(messagename == "flags")
			setflags(arrayfromargs(arguments));
	}
}

// used to manually override the presets
function window(enable){
	//post("pops:enable_flags() " + enable + "\n");
	if(enable == 1 && !myFlagsAreEnabled){
		outlet(0, "window", "flags", myFlagClose);
		outlet(0, "window", "flags", myFlagZoom);
		outlet(0, "window", "flags", myFlagMinimize);
		outlet(0, "window", "flags", myFlagGrow);
		outlet(0, "window", "flags", myFlagFloat);
		outlet(0, "window", "flags", myFlagTitle);
		outlet(0, "window", "flags", myFlagMenu);
		outlet(0, "window", "exec");
		updateWindow();
	}
	if(enable == 0 && myFlagsAreEnabled){
		outlet(0, "window", "flags", "close");
		outlet(0, "window", "flags", "zoom");
		outlet(0, "window", "flags", "minimize");
		outlet(0, "window", "flags", "grow");
		outlet(0, "window", "flags", "nofloat");
		outlet(0, "window", "flags", "title");
		outlet(0, "window", "flags", "menu");
		outlet(0, "window", "exec");
	}
	myFlagsAreEnabled = (enable == 1)? true: false;
}

setflags.local = 1;
function setflags(flags){
	//post("pops:setflags() " + flags + "\n");
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