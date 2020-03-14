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
outlets = 1;
setinletassist(0,"size");
setoutletassist(0,"jit.gl.sketch commands");

var myType = "point";
var ssize = .2;

var f_left = 0.1;
var f_top = 0.1;
var f_right = -0.1;
var f_bottom = -0.1;
var f_near = 0.1;
var f_far = 100.;

var ac = 1.;
var al = 0.;
var aq = 0.;

var color = new Array(1., 1., 1., 1.);

var falloff = 0;

var sketch = new JitterObject("jit.gl.sketch", "sparck");;

var myAnimNode = new JitterObject("jit.anim.node");

function loadbang(){
	myAnimNode.automatic = 1;

    sketch.anim = myAnimNode.name;

    sketch.lighting_enable = 1;
    sketch.blend_enable = 1;
    sketch.two_sided = 0;
    sketch.layer = 5;
    sketch.poly_mode = new Array(0, 1);
    sketch.enable = 0;
}

function anim(_name){
    //post("[draw.node.lights] anim function " + _name + "\n");
    myAnimNode.anim = _name;
}

function drawto(_context){
    //post("[draw.node.lights] anim function " + _name + "\n");
    sketch.drawto = _context;
}

function spot_falloff(foff){
	falloff = foff;
	draw();
}

function diffuse(r, g, b, a){
	color = new Array(r, g, b, a);
	draw();
}

function type(lighttype){
	myType = lighttype;
	draw();
}

function lightstagesize(s){
	ssize = s;
	draw();
}

function frustum(left, right, bottom, top, near, far){
	f_left = left;
	f_top = top;
	f_right = right;
	f_bottom = bottom;
	f_near = near;
	f_far = far;
	draw();
}

function atten_const(c){
  ac = c;
  draw();
}

function atten_linear(l){
  al = l;
  draw();
}

function atten_quad(q){
  aq = q;
  draw();
}

function anything(){
	sketch[messagename] = arrayfromargs(arguments);
}

function setSketch(_msg, _vals){
	sketch[_msg] = _vals;
}

function draw(){
	var sa = ssize * 1.1;
	var sb = ssize * 1.8;
	var sc = ssize *.5;

	sketch.reset();

	sketch.lighting_enable = 0;
	sketch.glcolor(color[0], color[1], color[2], color[3]);
	sketch.shapeprim("line");
	sketch.gllinewidth(1);
	sketch.framecircle(sc);
	sketch.shapeorient(90, 0, 0);
	sketch.framecircle(sc);
	sketch.shapeorient(0, 90, 0);
	sketch.framecircle( sc);


	if(myType == "point"){
		drawLightBeams(sa, sb, sc);
	} else if (myType == "directional"){
		drawAttenuation();
	} else if (myType == "spot"){
		drawCone();
		drawAttenuation();
	}
}

function drawLightBeams(sa, sb, sc){
	sketch.linesegment(sa, 0, 0, sb, 0, 0);
	sketch.linesegment(-sa, 0, 0, -sb, 0, 0);
	sketch.linesegment(0, sa, 0, 0, sb, 0);
	sketch.linesegment(0, -sa, 0, 0, -sb, 0);
	sketch.linesegment(0, 0, sa, 0, 0, sb);
	sketch.linesegment(0, 0, -sa, 0, 0, -sb);

	/*
	sketch.linesegment", sc, sc, 0, sb, sb, 0);
	sketch.linesegment", sc, -sc, 0, sb, -sb, 0);
	sketch.linesegment", -sc, sc, 0, -sb, sb, 0);
	sketch.linesegment", -sc, -sc, 0, -sb, -sb, 0);
	*/
	/*
	sketch.linesegment", sc, sc, sc, sb, sb, sb);
	sketch.linesegment", sc, -sc, sc, sb, -sb, sb);
	sketch.linesegment", -sc, sc, sc, -sb, sb, sb);
	sketch.linesegment", -sc, -sc, sc, -sb, -sb, sb);

	sketch.linesegment", sc, sc, -sc, sb, sb, -sb);
	sketch.linesegment", sc, -sc, -sc, sb, -sb, -sb);
	sketch.linesegment", -sc, sc, -sc, -sb, sb, -sb);
	sketch.linesegment", -sc, -sc, -sc, -sb, -sb, -sb);
	*/

	sketch.linesegment(sc, sc, sc, sa, sa, sa);
	sketch.linesegment(sc, -sc, sc, sa, -sa, sa);
	sketch.linesegment(-sc, sc, sc, -sa, sa, sa);
	sketch.linesegment(-sc, -sc, sc, -sa, -sa, sa);

	sketch.linesegment(sc, sc, -sc, sa, sa, -sa);
	sketch.linesegment(sc, -sc, -sc, sa, -sa, -sa);
	sketch.linesegment(-sc, sc, -sc, -sa, sa, -sa);
	sketch.linesegment(-sc, -sc, -sc, -sa, -sa, -sa);
}

function drawAttenuation(){
	var a = 1. / (ac + al * f_far + aq * f_far * f_far);
	sketch.glbegin("lines");
	sketch.glcolor(color[0], color[1], color[2], color[3]);
	sketch.glvertex(0., 0., 0.);
	sketch.glcolor(a * color[0], a * color[1], a * color[2], color[3]);
	sketch.glvertex(0., 0., -f_far);
	sketch.glend();
}


function drawCone(){
	var coneRatio = f_far / f_near;
	sketch.linesegment(0, f_left, -f_near, 0, f_left * coneRatio, -f_far);
	sketch.linesegment(0, f_right, -f_near, 0, f_right * coneRatio, -f_far);
	sketch.linesegment(f_top, 0, -f_near, f_top * coneRatio, 0, -f_far);
	sketch.linesegment(f_bottom, 0, -f_near,f_bottom * coneRatio, 0, -f_far);
	sketch.moveto(0, 0, -f_far);
	sketch.shapeorient(0, 0, 0);
	sketch.framecircle(f_bottom * coneRatio);
	sketch.framecircle(f_bottom * coneRatio * Math.max(0, 135. - falloff * 2) / 135.);
}

function notifydeleted(){
    sketch.freepeer();
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
