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

var model = null;//; = new JitterObject("jit.gl.mesh", ctx);

var myDict = null;

var stetupDone = false;

function title(_title){
	var modelTitle = _title + ".shady";
//	post("title function." + _title + "\n");
	if(model == null && myDict == null){
		myDict = new Dict("bs.modelstore." + _title);
		myDict.set("name", modelTitle);
//		post("created model dict: bs.modelstore." + _title + "\n");
	} else if(model != null){
		post("set shady model name to: " + modelTitle + "\n");
		model.name = modelTitle;
	}
}

function drawto(_ctx){
	if(model == null){
		model = new JitterObject("jit.gl.mesh", _ctx);
		outlet(1, "bang");
		if(myDict != null && myDict.getkeys() != null){
			var keys = myDict.getkeys();
			for(var i = 0; i < keys.length; i++){
				setmodel(keys[i], myDict.get(keys[i]));
			}
			myDict.clear();
			myDict == null;
		}
	} else {
		model.drawto = _ctx;
//		post("draw shady model to: " + _ctx + "\n");
	}
}

function setmodel(_msg, _vals){
	if(typeof model[_msg] == 'function'){
		//post("message: " + _msg + " to "+ _vals +"\n");
		if(_vals != null)
			model[_msg](_vals);
		else
			model[_msg]();
	} else {
		//post("attribute: " + _msg + " to "+ _vals +"\n");
		model[_msg] = _vals;
	}
}

function anything(){
//	cpost("got message to send: from:" + sendTitle + myAppendix + " message:"+ messagename +" \n");
	if(model != null){
		setmodel(messagename, arrayfromargs(arguments));
	} else if(myDict != null){
		myDict.set(messagename, arrayfromargs(arguments));
//		post("store for shady model:" + arrayfromargs(messagename,arguments) + "\n");
	}
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
