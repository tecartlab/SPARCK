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

// This script makes sure each instance that uses it will get its own index
// and is informed how many instances are currently using it
// a [receive bs::bake::uniquebang] object has to be attached to it work

autowatch = 1;

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 2;

setoutletassist(0,"length");
setoutletassist(1,"index");


var myNodeID=null;

if (jsarguments.length>1)
	myNodeID = jsarguments[1];

var uniqueBang = new Global("bs.bake.uniquebang");

function loadbang(){
	bang();
}

function bang(){
	if(uniqueBang.clients == null){
		uniqueBang.clients = new Array();
	}
	//post("created " + myNodeID + "\n");
}

function msg_int(v)
{
	enable(v);
}


function enable(_enable){
	if(_enable){	
		uniqueBang.clients.push(myNodeID);
	} else {
		removeEntry();
	}
	messnamed("bs::bake::uniquebang", "update");
}


function update(){
	outlet(0, uniqueBang.clients.length);
	if(myNodeID != null){
		outlet(1, uniqueBang.clients.indexOf(myNodeID));
	}
}

function removeEntry(){
	if(myNodeID != null){
		if(uniqueBang.clients.indexOf(myNodeID) != -1){
			//post("removeEntry " + myNodeID + " "  + uniqueBang.clients.indexOf(myNodeID) + "\n");
			uniqueBang.clients.splice(uniqueBang.clients.indexOf(myNodeID), 1);
		}
	}
}
			
//Called when the parent node was deleted
function notifydeleted(){
	if(uniqueBang != null){
		removeEntry();
	}
	//post("removed " + myNodeID + "\n");
}
