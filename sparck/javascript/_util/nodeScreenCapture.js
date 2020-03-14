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

inlets = 1;
outlets = 3;

var myproperties = 0;
var myname = "unknown";

function bang(){
	var owner = this.patcher;
	if(owner != null){
//		outlet(0, owner.getnamed('node_patch').subpatcher.box.getnamed('vpl_canvas').rect);
		outlet(1, 'rect', owner.getnamed('node_patch').subpatcher().getnamed('vpl_canvas').rect);
		outlet(1, 'exportimage', myname + "_n");
        if(myproperties){
            outlet(2, 'rect', owner.getnamed('prop_patch').subpatcher().getnamed('vpl_canvas').rect);
            outlet(2, 'exportimage', myname + "_p");
        }
	}
}

function load(name, filepath, properties){
	var owner = this.patcher;
	if(owner != null){
        myname = name;
        myproperties = properties;
        outlet(0, 'script', 'send', 'node_title', 'set', name);
        outlet(0, 'script', 'sendbox', 'node_patch', 'name', filepath+'.maxpat');
        if(myproperties){
            outlet(0, 'script', 'sendbox', 'prop_patch', 'name', filepath+'.properties.maxpat');
        }
	}
}
