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

autowatch = 0; //should not be set to 1, since the reload does not call notifydeleted()

// set up inlets/outlets/assist strings
inlets = 1; 	//DO NOT CHANGE
outlets = 1;	//DO NOT CHANGE
// Remark: This template inherits some structures from the js-script, like the 7
// inlets and outlets, but only two inlets and outlets are used

var tableanimnode;
var turneranimnode;

var turntablemodel;
var turnermodel;

var isSetup = false;
var hasChanged = false;

var potiNoiseThreshold = 2;
var speedLimitABS = 500;

// setup all the instances needed in here
function loadbang(){
	if(isSetup == false){
		//post("setup turntablescript...\n");
		// add code here
		tableanimnode = new JitterObject("jit.anim.node");
		tableanimnode.automatic = 0;

		turneranimnode = new JitterObject("jit.anim.node");
		turneranimnode.automatic = 0;
		turneranimnode.anim = tableanimnode.name;

		turntablemodel = new JitterObject("jit.gl.sketch", "sparck");
		turnermodel = new JitterObject("jit.gl.sketch", "sparck");

		turntablemodel.anim = tableanimnode.name;
		turntablemodel.enable = 0;
		drawtable();

		turnermodel.anim = turneranimnode.name;
		turnermodel.enable = 0;
		drawturner(0);

		isSetup = true;

		//post("jit.anim.node: tableanimnode = " + tableanimnode.name+ "\n");
		//post("jit.anim.node: turneranimnode = " + turneranimnode.name+ "\n");

		outputAnimNodeName();


		refreshprops(); //do not remove this line
	}
}

// called if this scipt is deleted. it is also called if the script is saved
//  bcause it is causing a removal and reload of it.
//  free all created jitter objects inside this function, otherwise
//  dead jitter object will hang inside the 3d environment
function notifydeleted(){
	tableanimnode.freepeer();
	turneranimnode.freepeer();
	turntablemodel.freepeer();
	turnermodel.freepeer();
	//post("notifydeleted() method has been executed \n");
}

// sets the parent animation node for this script
function anim(){
	tableanimnode.anim = arguments[0];
	tableanimnode.update_node();
	turneranimnode.update_node();
}

// called on each frame at the beginning of all renderpasses
function update(){
//	post("got parent: "+ tableanimnode.anim + " \n");
}

var smoothArray = new Array(10);
var smoothIndex = 0;

// called on each frame right after update()
// here all the necessary calculations are done
function execute(){
	if(isSetup & hasChanged){

		smoothIndex = (smoothIndex < smoothArray.length - 1)?smoothIndex + 1: 0;
		smoothArray[smoothIndex] = tableAngleDiff;

		var smoothAvrg = 0;
		for(var i = 0; i < smoothArray.length; i++)
			smoothAvrg += smoothArray[i];
		smoothAvrg = (smoothArray.length > 0)?smoothAvrg / smoothArray.length:0;
		//post("smothAvrg = " + smoothAvrg + "\n");

		turneranimnode.rotatexyz = new Array(0, 0, tableAngle + smoothAvrg + rotationOffset);
		transformationbang();
		hasChanged = false;
	}
}

//called on a transformation pass
function transformationbang(){
	//post("transformationbang\n");
	tableanimnode.update_node();
	turneranimnode.update_node();
//	post("got worldpos: "+ tableanimnode.worldpos + " \n");
	outlet(0, "messagewormhole", "bang");
}

// is used to enable and disable jitter-
// called if  "tostage" is selected and the sceneview is enabled
function drawToScene(){
	var args = arrayfromargs(arguments);
	//post("got scenedirect: "+ args + " \n");
	if(turntablemodel != null){
		if(args[0] == "enable"){
			turntablemodel.enable = args[1];
			turnermodel.enable = args[1];
		}else if(args[0] == "drawto"){
			turntablemodel.drawto = args[1];
			turnermodel.drawto = args[1];
		}
	}
}

var useScriptSpeed = 0;
var rotationOffset = 0;
var speed = 0;
var turnSpeed = 0;
var animNodeTree = new Array();

//utility function
function anything(){
    var args = arrayfromargs(arguments);
	if(messagename == "useScriptSpeed"){
		useScriptSpeed = args[0];
		if(useScriptSpeed == 0){
			turnSpeed = speed;
			sendSerialSpeed(turnSpeed);
		}
	}else if(messagename == "rotationOffset"){
		rotationOffset = args[0];
	}else if(messagename == "speed"){
		speed = args[0];
		if(useScriptSpeed == 0){
			turnSpeed = speed;
			sendSerialSpeed(turnSpeed);
		}
	}else if(messagename == "name"){
		turneranimnode.name = args[0];
		turnermodel.anim = turneranimnode.name;
		outputAnimNodeName();
		//post("this scipts node-name is: "+ args[0] + "\n");
	}
	hasChanged = true;
}


var tableAngle = 0;
var newTableAngle = 0;
var tableAngleDiff = 0;

var poti0Angle = 0;
var poti0AngleDiff = 0;
var poti1Angle = 0;
var poti1AngleDiff = 0;
var poti2Angle = 0;
var poti2AngleDiff = 0;

function streaming(){
    var args = arrayfromargs(arguments);
	if(args[0] == "tableAngle"){
		newTableAngle = (args[1] != 0.0)?args[1] / 2047.0 * 360.:0.0;
		if(newTableAngle != tableAngle){
			//post("tableAngle: "+tableAngle+" \n");
			tableAngle = newTableAngle;
			hasChanged = true;
		}
	}else if(args[0] == "tableAngleDiff"){
		tableAngleDiff = args[1];
	}else if(args[0] == "poti0Angle"){
		poti0Angle = (args[1] != 0.0)?args[1] / 1023.0 * 360.:0.0;
	}else if(args[0] == "poti0AngleDiff"){
		if(args[1] > potiNoiseThreshold || args[1] < -potiNoiseThreshold){
			poti0AngleDiff = args[1];
			if(useScriptSpeed == 1){
				sendSerialSpeedDiff(poti0AngleDiff)
			}
		}
	}else if(args[0] == "poti1Angle"){
		poti1Angle = (args[1] != 0.0)?args[1] / 1023.0 * 360.:0.0;
	}else if(args[0] == "poti1AngleDiff"){
		if(args[1] > potiNoiseThreshold || args[1] < -potiNoiseThreshold){
			poti1AngleDiff = args[1];
			if(useScriptSpeed == 2){
				sendSerialSpeedDiff(poti1AngleDiff)
			}
		}
	}else if(args[0] == "poti2Angle"){
		poti2Angle = (args[1] != 0.0)?args[1] / 1023.0 * 360.:0.0;
	}else if(args[0] == "poti2AngleDiff"){
		if(args[1] > potiNoiseThreshold || args[1] < -potiNoiseThreshold){
			poti2AngleDiff = args[1];
			if(useScriptSpeed == 3){
				sendSerialSpeedDiff(poti2AngleDiff)
			}
		}
	}
}

function sendSerialSpeedDiff( _diff){
	turnSpeed +=  _diff;

	// test turnspeed versus speed limit
	if(turnSpeed < -speedLimitABS){
		turnSpeed = -speedLimitABS;
	} else if(turnSpeed > speedLimitABS){
		turnSpeed = speedLimitABS;
	}

	// stop turning if speed is between -30 & 30
	if(turnSpeed > -30 && 30 > turnSpeed)
		sendSerialSpeed(0);
	else
		sendSerialSpeed(turnSpeed);
}

// sends a speed message to the turntable firmware:
//		speed <int> (-500 ... 500);
function sendSerialSpeed( _arg){
	//post("sendSerialSpeed: "+ _arg + "\n");
	outlet(0, "sendserial", "speed", _arg);
}


function drawtable(){
	turntablemodel.reset();
	turntablemodel.blend_enable = 1;
	turntablemodel.lighting_enable = 0;
	turntablemodel.moveto(0., 0., -.073);
	turntablemodel.glcolor(new Array(1, 1, 1, .5));
	turntablemodel.cube(.2, .2, .073);
}

function drawturner(offset){
	turnermodel.reset();
	turnermodel.blend_enable = 1;
	turnermodel.lighting_enable = 0;
	turnermodel.glcolor(new Array(1, 1, 1, 1));
	turnermodel.shapeprim("line");
	turnermodel.framecircle(.3, 0, -offset);
	turnermodel.linesegment(0., 0., 0., .3, 0., 0. );
}

function anim_tree_nodes(){
	//post("anim_tree_nodes = " + arrayfromargs(arguments) + "\n");
	outlet(0, "message", "anim_tree_nodes", arrayfromargs(arguments));
}

function outputAnimNodeName(){
	//post("outputAnimNodeName = " + turneranimnode.name+ "\n");
	outlet(0, "message", "anim", turneranimnode.name);
}

// calling this function will cause the node to resend all previously set
//  properties. should be done after each setup call.
function refreshprops(){
	outlet(0, "refreshprops", "bang");
}

/*
var smoothArray = new Array(10);
var smoothIndex = 0;

			smoothIndex = (smoothIndex < smoothArray.length - 1)?smoothIndex + 1: 0;
			smoothArray[smoothIndex] = anglestep;

			var smoothAvrg = 0;
			for(var i = 0; i < smoothArray.length; i++)
				smoothAvrg += smoothArray[i];
			smoothAvrg = (smoothArray.length > 0)?smoothAvrg / smoothArray.length:0;


			if(gotAngleUpdate == 0){
				//post("smoothAvrg"+	smoothAvrg + "\n");
				angle += smoothAvrg;
			}

//		if(gotAngleUpdate == 0)
//			post("transformationbang missed angle: "+ angle +" \n");
//		else
//			post("transformationbang got angle: "+ angle +" \n");


			gotAngleUpdate = 0;
	//	lastturnangle = turnangle;
			turnangle = (angle != 0.0)?angle / 2047. * 360.:0.0;
	//	if((lastturnangle - turnangle) > 2){
			post("lastturnangle: " + lastturnangle + "\n");
			post("turnangle: " + turnangle + "\n");
			post("angle: " + angle + "\n");
	//	}
			turneranimnode.rotatexyz = new Array(0, 0, rotationOffset + turnangle);

*/

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
