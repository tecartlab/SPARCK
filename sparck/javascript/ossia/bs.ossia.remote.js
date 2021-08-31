// set up inlets/outlets/assist strings
inlets = 3;
outlets = 6;

setinletassist(0,"input from ossia.remote 3rd outlet");
setinletassist(1,"input from interaction inlet");
setinletassist(2,"input from condition inlet");
setoutletassist(0,"output to ossia.remote 1st inlet");
setoutletassist(1,"output to gui object");
setoutletassist(2,"output to text object");
setoutletassist(3,"output to interaction");
setoutletassist(4,"output to visibility override");
setoutletassist(5,"dump");

var IN_ARGS = 0;
var IN_INTER = 1;
var IN_COND = 2;

var OUT_REMOTE = 0;
var OUT_GUI = 1;
var OUT_TEXT = 2;
var OUT_INTER = 3;
var OUT_VISIBL = 4;
var OUT_DUMP = 5;

var VISIBILITY_OVERRIDE = "_visibilityOverride";

var useclip = 1;
declareattribute("useclip", null, "setuseclip", 1);

var myNodeTitle = undefined;
var myParamAddress = undefined;

var myClip = "free";
var myUnit = "";
var myType = "floa	t";
var myText = null;

var myCond_IS = null;
var myCond_ISNOT = null;
var myCond_GT = null;
var myCond_LT = null;
var myCond_EQ = null;
var myCond_LTEQ = null;
var myCond_GTEQ = null;


var myVisibility = true; // can be changed by the evaluation of the condition
var myVisOverride = true; // visibility can also be overwritten by a message
var myInvisibility = false; // the invisibility attribute set via parameter

// patcher arguments
if (jsarguments.length > 2){
    myNodeTitle = jsarguments[1];
    myParamAddress = jsarguments[2];
}

function setuseclip(_clip){
	useclip = _clip;
}

function loadbang(){
    //post("loadbang(): " + "sparck:/node/" + myNodeTitle + "/" + myParamAddress + " \n")
//    outlet(OUT_REMOTE, "address", "sparck:/node/" + myNodeTitle + "/" + myParamAddress);
    outlet(OUT_REMOTE, "address", myParamAddress);
}

function text(_text){
	myText = _text;
}

function hint(_hint){
	outlet(OUT_TEXT, "hint", _hint);
}

function is(){
	myCond_IS = arrayfromargs(arguments);
	myVisibility = false;
}

function isnot(){
	myCond_ISNOT = arrayfromargs(arguments);
	myVisibility = true;
}

function lt(_lt){
	myCond_LT = _lt;
	myVisibility = false;
}

function gt(_gt){
	myCond_GT = _gt;
	myVisibility = false;
}

function eq(_eq){
	myCond_EQ = _eq;
	myVisibility = false;
}

function lteq(_lteq){
	myCond_LTEQ = _lteq;
	myVisibility = false;
}

function gteq(_gteq){
	myCond_GTEQ = _gteq;
	myVisibility = false;
}

function done(){
    //post("done(): " + "sparck:/node/" + myNodeTitle + "/" + myParamAddress + " \n")
    // called when patcherargs is done
    outlet(OUT_REMOTE, "get", "type");
    outlet(OUT_REMOTE, "get", "unit");
    outlet(OUT_REMOTE, "get", "description");
    outlet(OUT_REMOTE, "get", "clip");
    outlet(OUT_REMOTE, "get", "invisible");

	setVisibility();
}

function msg_int(_value){
	if(inlet == IN_COND){
		// it is a condition
		updateCondition(_value);
	}
}

function msg_float(_value){
	if(inlet == IN_COND){
		// it is a condition
		updateCondition(_value);
	}
}

function anything(){
	if(inlet == IN_ARGS){
		// everything unknown is passed on to the GUI object
		outlet(OUT_GUI, arrayfromargs(messagename,arguments));
	} else if(inlet == IN_INTER){
		//post("IN_INTER message " + arrayfromargs(messagename,arguments) + " " + myVisibility + "\n");
		// if it is an interaction message, send it to the GUI if visible
		if(myVisibility && myVisOverride && !myInvisibility){
			outlet(OUT_GUI, arrayfromargs(messagename,arguments));
		} else { // or else pass it on
			outlet(OUT_INTER, arrayfromargs(messagename,arguments));
		}
	} 
}

function uc(){
	//post("uc = " + arrayfromargs(arguments)  + "\n");
	if(inlet == IN_COND){
		// it is a condition
		args = arrayfromargs(arguments);
		if(args[0] == VISIBILITY_OVERRIDE){
			myVisOverride = (args[1] == 1)? true: false;
			setVisibility();
		} else {
			updateCondition(args[0]);
		}
	}
}

function updateCondition(_cond){
	//post("updateCondition " + _cond + "\n");
	if(myCond_IS != null){
		if(Array.isArray(myCond_IS)){
			myVisibility = (myCond_IS.indexOf(_cond) != -1)?true: false;
		} else {
			myVisibility = (myCond_IS == _cond)?true: false;
		}
	} 
	else if(myCond_ISNOT != null){
		//post("myCond_ISNOT = " + myCond_ISNOT + "\n");
		if(Array.isArray(myCond_ISNOT)){
			myVisibility = (myCond_ISNOT.indexOf(_cond) == -1)?true: false;
		} else {
			myVisibility = (myCond_ISNOT != _cond)?true: false;
		}
	} 
	else if(myCond_LT != null){
		myVisibility = (_cond < myCond_LT);
	} 
	else if(myCond_GT != null){
		myVisibility = (_cond > myCond_GT);
	} 
	else if(myCond_EQ != null){
		myVisibility = (_cond == myCond_EQ);
	}
	else if(myCond_LTEQ != null){
		myVisibility = (_cond <= myCond_LTEQ);
	}
	else if(myCond_GTEQ != null){
		myVisibility = (_cond >= myCond_GTEQ);
	}
	setVisibility();
}

function setVisibility(){
	//post("setVisibility " + myVisibility + "\n");
	
	//post("myInvisibility " + myInvisibility + "\n");
	if(myVisibility && myVisOverride && !myInvisibility){	
		outlet(OUT_GUI, "hidden", 0);
		if(myText == null){
			outlet(OUT_TEXT, "hidden", 1);
			outlet(OUT_GUI, "presentation_position", 0, 0);
		} else {
			outlet(OUT_TEXT, "set", myText);
			outlet(OUT_TEXT, "hidden", 0);
			outlet(OUT_GUI, "presentation_position", 60, 0);
		}
		outlet(OUT_VISIBL, VISIBILITY_OVERRIDE, 1);
	} else {
		outlet(OUT_TEXT, "hidden", 1);
		outlet(OUT_GUI, "hidden", 1);
		outlet(OUT_VISIBL, VISIBILITY_OVERRIDE, 0);
	}
}

function list(_list){
	//post("list function: " + _list + "\n");
}


function type(_type){
    myType = _type;
}

function unit(_unit){
    if(_unit == ""){
        myUnit = "";
    } else {
        myUnit = " [" + _unit + "]";
    }
}

function description(_desc){
    outlet(OUT_GUI, "hint", _desc + myUnit + " | {osc: /node/" + myNodeTitle + "/" + myParamAddress + " <"+myType+">}");
}

function priority(_clip){
}


function default(_val){
    //post("default: " + _val + "\n");
}

function rate(_val){
    //post("rate : " + _val + "\n");
}

function tags(_tags){
	//
}

function clip(_clip){
    //post("clip("+_clip+"): " + "sparck:/node/" + myNodeTitle + "/" + myParamAddress + " \n")
    myClip = _clip;
    outlet(OUT_REMOTE, "get", "range");
}

function min(_min){
	if(useclip){
    	if(myClip == "free"){
        	// do nothing
    	} else if(myClip == "low"){
        	// only set min
        	outlet(OUT_GUI, "minimum", _min);
        	outlet(OUT_GUI, "usemin", 1);        
    	} else if(myClip == "hight"){
        	// only set max
    	} else {
        	outlet(OUT_GUI, "minimum", _min);
        	outlet(OUT_GUI, "usemin", 1);        
    	}
	}
}

function max(_max){
	if(useclip){
    	if(myClip == "free"){
        	// do nothing
    	} else if(myClip == "low"){
        	// only set min
    	} else if(myClip == "hight"){
        	// only set max
        	outlet(OUT_GUI, "maximum", _max);
        	outlet(OUT_GUI, "usemax", 1);        
    	} else {
        	outlet(OUT_GUI, "maximum", _max);
        	outlet(OUT_GUI, "usemax", 1);        
    	}
	}
}

function address(){
    //ignore
}

function invisible(_val){
	myInvisibility = (_val == 1)?true:false;
	setVisibility();
//	post("myInvisibility (" + myParamAddress +")= " + myInvisibility + "\n");
}

function range(_rangeMin, _rangeMax){
    //post("range("+_rangeMin+","+_rangeMax+"): " + "sparck:/node/" + myNodeTitle + "/" + myParamAddress + " \n")
    // ignore
}
