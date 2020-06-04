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
    //post("loadbang(): " + "sparck:/node/" + myNodeTitle + "/" + myParamAddress + " \n")
//    outlet(0, "address", "sparck:/node/" + myNodeTitle + "/" + myParamAddress);
    outlet(0, "address", myParamAddress);
}

function done(){
    //post("done(): " + "sparck:/node/" + myNodeTitle + "/" + myParamAddress + " \n")
    // called when patcherargs is done
    outlet(0, "get", "type");
    outlet(0, "get", "unit");
    outlet(0, "get", "description");
    outlet(0, "get", "clip");
}

function list(_list){
	//post("list function: " + _list + "\n");
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

function priority(_clip){
}

function default(_val){
    post("default: " + _val + "\n");
}

function rate(_val){
    post("rate : " + _val + "\n");
}

function clip(_clip){
    //post("clip("+_clip+"): " + "sparck:/node/" + myNodeTitle + "/" + myParamAddress + " \n")
    myClip = _clip;
    outlet(0, "get", "range");
}

function min(_min){
    if(myClip == "free"){
        // do nothing
    } else if(myClip == "low"){
        // only set min
        outlet(1, "minimum", _min);
        outlet(1, "usemin", 1);        
    } else if(myClip == "hight"){
        // only set max
    } else {
        outlet(1, "minimum", _min);
        outlet(1, "usemin", 1);        
    }
}

function max(_max){
    if(myClip == "free"){
        // do nothing
    } else if(myClip == "low"){
        // only set min
    } else if(myClip == "hight"){
        // only set max
        outlet(1, "maximum", _max);
        outlet(1, "usemax", 1);        
    } else {
        outlet(1, "maximum", _max);
        outlet(1, "usemax", 1);        
    }
}

function address(){
    //ignore
}

function range(_rangeMin, _rangeMax){
    //post("range("+_rangeMin+","+_rangeMax+"): " + "sparck:/node/" + myNodeTitle + "/" + myParamAddress + " \n")
    // ignore
}
