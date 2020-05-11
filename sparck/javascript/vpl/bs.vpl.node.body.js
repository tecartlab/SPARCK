//autowatch = 1;

var undefined = "undefined";

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 2;
setinletassist(0,"input");
setoutletassist(0,"output to patcher");
setoutletassist(1,"output to ui");

var myNodeId = null;
//var myStoreName = null;

var mySize = null;

//Default Flags:

var desktop_rect;

var isDone = false;

var appGlobal = new Global("bs::app::global");

var debug = (appGlobal.debug == 1 || appGlobal.debug == 5)? true:false;

// set the debugger
function debugger(_val){
	debug = (_val == 1)? true: false;
}

function dpost(_message){
    if(debug){
        post("node.body->" + myID + ": " + _message + " \n");
    }
}

function loadbang(){
}

function done(){
 	if(!isDone){
		if(this.patcher.box != null){
			myWindow = this.patcher.box.patcher.wind;

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
                        outlet(1, "script", "sendbox", "vpl_properties", "presentation_rect", 0, 0, mySize[0], mySize[1]);
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
function addressChange(_newAddress){
	dpost("received address change= " + _newAddress + "\n");
    address(_newAddress);
}

function address(_address){
    outlet(0, "script", "sendbox", "vpl_properties", "args", myID, "@address", _address);    
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