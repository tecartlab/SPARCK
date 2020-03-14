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
outlets = 5;

setoutletassist(0,"dispose");
setoutletassist(1,"setup bs::app::rc::context");
setoutletassist(2,"rendercontext Output");
setoutletassist(3,"window desktop default position");
setoutletassist(4,"window output default position");

var maxContexts = 4;

var myContext = null;
var myContextID = -1;

var outputContext = new Global("bs.node.ouput.unique.context");

function done(){
	if(outputContext.out == null){
		outputContext.out = new Array(0, 0, 0, 0);
	}
	for(var i = 0; i < maxContexts; i++){
		if(myContext == null && outputContext.out[i] == 0){
			outputContext.out[i] = 1;
			myContext = "Output_"+ (i + 1);
			myContextID = i;
//			post("create window context = " + myContext + " myContextID = " + myContextID + "\n");
		}
	}

	if(myContext != null){
		outlet(1, "context", "bs::app::rc::" + myContext);
		//outlet(1, "idlemouse", 1);
		outlet(2, "rendercontext", myContext);
		outlet(3, "rect", 5 + myContextID * 320, 725, 320 + myContextID * 320, 885);
		outlet(4, "rect", 100, 100, 420, 340);
		outlet(1, "call", 1);
	} else {
		error("Only "+maxContexts+" Windows are allowed at the same time\n");
		outlet(0, "dispose");
	}
}

function reset(){
	if(myContext != null){
		outlet(3, "rect", 5 + myContextID * 320, 725, 320 + myContextID * 320, 885);
		outlet(4, "rect", 100, 100, 420, 340);
		outlet(1, "call", 1);
    }
}

//Called when the parent node was deleted
function notifydeleted(){
	if(myContext != null)
		outputContext.out[myContextID] = 0;
}
