// set up inlets/outlets/assist strings
inlets = 1;
outlets = 2;
setinletassist(0,"input from ossia.remote 3rd outlet");
setoutletassist(0,"output to ossia.remote 1st inlet");
setoutletassist(1,"output to float");

var myNodeTitle = undefined;
var myParamAddress = undefined;

var myClip = "free";
var myUnit = "";
var myType = "float";
// patcher arguments
if (jsarguments.length > 2){
    myNodeTitle = jsarguments[1];
    myParamAddress = jsarguments[2];
}

function loadbang(){
    outlet(0, "address", "sparck:/node/" + myNodeTitle + "/" + myParamAddress);
    outlet(0, "get", "type");
    outlet(0, "get", "unit");
    outlet(0, "get", "description");
}

function done(){
    // called when patcherargs is done
}

function type(_type){
    myType = _type;
}

function unit(_unit){
    if(_unit == "none"){
        myUnit = "";
    } else {
        myUnit = " [" + _unit + "]";
    }
}

function description(_desc){
    outlet(1, "hint", _desc + myUnit + " | {osc: /node/" + myNodeTitle + "/" + myParamAddress + " <"+myType+">}");
}

function address(){
    //ignore
}

function range(){
    // ignore
}
