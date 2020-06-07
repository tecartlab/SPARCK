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

inlets = 1;
outlets = 1;

function load(){
	var parentlevel = 0;
	var owner = this.patcher.box.patcher.box;
	if (owner) {
//		post("owner : " + owner.varname + " level : " + parentlevel+"\n");
		if(owner.patcher.name.indexOf("APP") != 0){
			outlet(0, "loadproject", owner.patcher.filepath.substring(0, owner.patcher.filepath.lastIndexOf(".")));
		}else{
			post("Projectname should not start with 'APP'");
		}
	}
}

function save(){
	var parentlevel = 0;
	var owner = this.patcher.box.patcher.box;
	if (owner) {
//		post("owner : " + owner.varname + " level : " + parentlevel+"\n");
		if(owner.patcher.name.indexOf("APP") != 0){
			outlet(0, "saveas_project", owner.patcher.filepath.substring(0, owner.patcher.filepath.lastIndexOf(".")));
		}else{
			post("Projectname should not start with 'APP'");
		}
	}
}
