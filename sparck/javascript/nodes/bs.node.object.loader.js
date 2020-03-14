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

autowatch = 1
;
// set up inlets/outlets/assist strings
inlets = 2;
outlets = 2;
setinletassist(0,"load");
setinletassist(1,"add");
setoutletassist(0,"createbox");
setoutletassist(1,"dump");

var scriptObject = null;

var inlet_props;
var inlet_one;
var inlet_two;
var inlet_three;
var inlet_four;
var inlet_five;
var inlet_six;

var outlet_props;
var outlet_one;
var outlet_two;
var outlet_three;
var outlet_four;
var outlet_five;
var outlet_six;

var attributes = "noname";

if (jsarguments.length == 2){
    attributes = jsarguments[1];
}

var openscript;
// space::nodes::node::space::
var outlet_dump = 1;
var myPath;

function loadjs(path){
	if(scriptObject != null)
		this.patcher.remove(scriptObject);
	scriptObject = this.patcher.newdefault(290, 216, "js", path, attributes);
	defconn();
	createconn();
	this.patcher.connect(this.patcher.getnamed("_jsCreator"), 1, scriptObject, 0);
	outlet(1, "loadbang");
	this.patcher.connect(openscript, 1, scriptObject, 0);
}

function loadlua(path){
	if(scriptObject != null)
		this.patcher.remove(scriptObject);
	scriptObject = this.patcher.newdefault(290, 216, "lua", path);
	defconn();
	createconn();
	this.patcher.connect(openscript, 1, scriptObject, 0);
	outlet(1, "bang");
}


function loadsketch(path){
	if(scriptObject != null)
		this.patcher.remove(scriptObject);
	scriptObject = this.patcher.newdefault(290, 216, path, attributes);
	defconn();
	createconn();
	this.patcher.connect(openscript, 0, scriptObject, 0);
	outlet(1, "bang");
}

function loadpatcher(path){
	if(myPath == null || myPath.indexOf(path) == -1){
		if(this.patcher.getnamed("opener") != null)
			this.patcher.remove(this.patcher.getnamed("opener"));
		myPath = path;
		outlet(0, "script", "newobject", "newobj", "@patching_rect", 290, 216, "@text", path, "@varname", "opener");
		this.patcher.connect(this.patcher.getnamed("inlet"), 0, this.patcher.getnamed("opener"), 0);
		outlet(1, "bang");
	}
}

function openpatcher(){
	if(this.patcher.getnamed("opener") != null){
		this.patcher.getnamed("opener").message("front");
	}
}

// is called if <none> file is selected
function reset(){
}

function setattributes(_attr){
	attributes = arrayfromargs(_attr);
}

function createconn(){
	this.patcher.connect(inlet_props, 0, scriptObject, 0);
	this.patcher.connect(inlet_one, 0, scriptObject, 1);
	this.patcher.connect(inlet_two, 0, scriptObject, 2);
	this.patcher.connect(inlet_three, 0, scriptObject, 3);
	this.patcher.connect(inlet_four, 0, scriptObject, 4);
	this.patcher.connect(inlet_five, 0, scriptObject, 5);
	this.patcher.connect(inlet_six, 0, scriptObject, 6);

	this.patcher.connect(scriptObject, 0, outlet_props, 0);
	this.patcher.connect(scriptObject, 1, outlet_one, 0);
	this.patcher.connect(scriptObject, 2, outlet_two, 0);
	this.patcher.connect(scriptObject, 3, outlet_three, 0);
	this.patcher.connect(scriptObject, 4, outlet_four, 0);
	this.patcher.connect(scriptObject, 5, outlet_five, 0);
	this.patcher.connect(scriptObject, 6, outlet_six, 0);

}

function defconn(){
	inlet_props = this.patcher.getnamed("inlet_props");
	inlet_one = this.patcher.getnamed("inlet_one");
	inlet_two = this.patcher.getnamed("inlet_two");
	inlet_three = this.patcher.getnamed("inlet_three");
	inlet_four = this.patcher.getnamed("inlet_four");
	inlet_five = this.patcher.getnamed("inlet_five");
	inlet_six = this.patcher.getnamed("inlet_six");

	outlet_props = this.patcher.getnamed("outlet_props");
	outlet_one = this.patcher.getnamed("outlet_one");
	outlet_two = this.patcher.getnamed("outlet_two");
	outlet_three = this.patcher.getnamed("outlet_three");
	outlet_four = this.patcher.getnamed("outlet_four");
	outlet_five = this.patcher.getnamed("outlet_five");
	outlet_six = this.patcher.getnamed("outlet_six");

	openscript = this.patcher.getnamed("openscript");
}

function pausecomp(ms) {
	ms += new Date().getTime();
	while (new Date() < ms){}
}
