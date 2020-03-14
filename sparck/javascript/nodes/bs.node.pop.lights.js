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
inlets = 1;
outlets = 2;
setinletassist(0,"messages");

var undefined = "address";

var verbose = true; // will print out messages when true

var lights = null;//; = new JitterObject("jit.gl.mesh", ctx);
var anims = null;

var myDict = null;

var myTitle = null;

var stetupDone = false;

var	myAnimNode = new JitterObject("jit.anim.node");

function loadbang(){
    //post("[node.lights] myAnimNode created\n");
	myAnimNode.automatic = 1;
}

function anim(_name){
    //post("[node.lights] anim function " + _name + "\n");
    myAnimNode.anim = _name;
}

function title(_title){
	//post("[node.lights] title function." + _title + "\n");
    myTitle = _title;
    var newDict = new Dict("bs.lightsstore." + _title);
	if(myDict == null){
		myDict = newDict;
	} else {
        var keys = myDict.getkeys();
        for(var i = 0; i < keys.length; i++){
            newDict.set(keys[i], myDict.get(keys[i]));
        }
        myDict.clear();
        myDict = newDict;
    }
}

function drawto(){
    var cntxts = arrayfromargs(arguments);
    // first we clean up the current lights.
    freepeer();
    lights = [];
    anims = [];
    for(var i = 0; i < cntxts.length; i++){
        var animNode = new JitterObject("jit.anim.node");
        animNode.automatic = 1;
        animNode.anim = myAnimNode.name;

        var light = new JitterObject("jit.gl.light");
        //post("[node.lights] drawto: create new light @drawto "+ cntxts[i] + "\n");
        light.drawto = cntxts[i];
        light.anim = animNode.name;

        lights.push(light);
        anims.push(animNode);
    }

    if(myDict != null && myDict.getkeys() != null){
        var keys = myDict.getkeys();
        for(var i = 0; i < keys.length; i++){
            //post("[node.lights] set light:"+ keys[i] + " + " + myDict.get(keys[i]) + "\n");
            setlight(keys[i], myDict.get(keys[i]));
        }
    }
}

function enable(_enable){
	if(lights != null){
        for(var i = 0; i < lights.length; i++){
            lights[i].enable = _enable;
        }
    }
}

function setlight(_msg, _vals){
    if(lights != null){
        for(var i = 0; i < lights.length; i++){
            light = lights[i];
            if(typeof light[_msg] == 'function'){
                if(_vals != null)
                    light[_msg](_vals);
                else
                    light[_msg]();
            } else {
                light[_msg] = _vals;
            }
        }
    }
}

function anything(){
	//post("[node.lights] got anything:"+ messagename + " + " + arrayfromargs(arguments) + "\n");
    setlight(messagename, arrayfromargs(arguments));
    if(myDict != null){
		myDict.set(messagename, arrayfromargs(arguments));
		//post("[node.lights] anything store:" + arrayfromargs(messagename,arguments) + "\n");
	}
}


function freepeer(){
    if(lights != null){
        for(var i = 0; i < lights.length; i++){
            lights[i].freepeer();
            anims[i].freepeer();
        }
    }
}

function notifydeleted(){
    freepeer();
    myAnimNode.freepeer();
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
