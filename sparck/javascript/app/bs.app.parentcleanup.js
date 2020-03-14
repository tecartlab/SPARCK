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

function clean(){
	var parentlevel = 0;
	var owner = this.patcher.box;

	if (owner) {
//		post("owner : " + owner.varname + " level : " + parentlevel+"\n");
		if(owner.patcher.name.indexOf("APP") != 0){
			var panel = this.patcher.getnamed("appwindow");
			owner.patcher.message("script", "sendbox", owner.varname, "presentation_rect", panel.rect[0], panel.rect[1], panel.rect[2], panel.rect[3]);

			owner.patcher.message("script", "sendbox", owner.varname, "presentation", 1);

			outlet(0, "window", "flags", "nozoom");
			outlet(0, "window", "flags", "nominimize");
			outlet(0, "window", "flags", "noclose");
			outlet(0, "window", "flags", "nogrow");
			outlet(0, "window", "flags", "nomenu");
			outlet(0, "window", "flags", "float");
			outlet(0, "window", "size", 200, 40, 200 + panel.rect[2], 40 + panel.rect[3]);

			outlet(0, "window", "exec");

			outlet(0, "openinpresentation", "1");
			outlet(0, "enablehscroll", "0");
			outlet(0, "toolbarvisible", "0");
			outlet(0, "enablevscroll", "0");

			//owner.message("presentation_rect", 0, 0, 60, 20);
		}else{
			post("Projectname should not start with 'APP'");
		}
	}
}

function relax(){
	var parentlevel = 0;
	var owner = this.patcher.box;
	if (owner) {
//		post("owner : " + owner.varname + " level : " + parentlevel+"\n");
		if(owner.patcher.name.indexOf("APP") != 0){
			owner.patcher.message("script", "sendbox", owner.varname, "presentation", 0);

			outlet(0, "window", "flags", "zoom");
			outlet(0, "window", "flags", "minimize");
			outlet(0, "window", "flags", "close");
			outlet(0, "window", "flags", "grow");
			outlet(0, "window", "flags", "menu");
			outlet(0, "window", "flags", "nofloat");

			outlet(0, "window", "exec");

			outlet(0, "enablehscroll", "1");
			outlet(0, "toolbarvisible", "1");
			outlet(0, "enablevscroll", "1");
			//owner.message("presentation_rect", 0, 0, 60, 20);
		}else{
			post("Projectname should not start with 'APP'");
		}
	}
}
