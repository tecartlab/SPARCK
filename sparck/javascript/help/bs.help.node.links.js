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

inlets=1;
outlets=1;

var bgcolor = this.patcher.getattr("locked_bgcolor");
var alpha = 1.0;
var name = jsarguments[1];
name = (name.indexOf("#") == 0)?null: name;
var dict;

var maxSeeAlsos = 6;
var maxLinks = 5;

function loadbang()
{
	dict = new Dict();
	if(name){
		dict.import_json(name + ".en.sparckref.json");
		if (typeof(dict) == "object") {
        	if(typeof(dict.get("seealso") == "object")){
				var seealso = dict.get("seealso").getkeys();
				if(typeof seealso == "string"){
					seealso = new Array(seealso);
				}
				var position = 0;
				for(var i = 0; i < maxSeeAlsos; i++){
					if(i < seealso.length){
						outlet(0, "seealso", i, "text", seealso[i]);
						outlet(0, "seealso", i, "presentation_rect", position, 0, seealso[i].length * 10, 20);
						outlet(0, "seealso", i, "hidden", 0);
						position = position + seealso[i].length * 10 + 10;
					}else{
						outlet(0, "seealso", i, "hidden", 1);
					}
				}
			}
			if(typeof(dict.get("links") == "object")){
				var links = dict.get("links").getkeys();
				if(typeof links == "string"){
					links = new Array(links);
				}
				var position = 0;
				for(var i = 0; i < maxLinks; i++){
					if(i < links.length){
						outlet(0, "links", i, "button", "text", links[i]);
						outlet(0, "links", i, "button", "presentation_rect", position, 0, links[i].length * 10, 20);
						outlet(0, "links", i, "button", "hidden", 0);
						outlet(0, "links", i, "url", dict.get("links").get(links[i]));
						position = position + links[i].length * 10 + 10;
					}else{
						outlet(0, "links", i, "button", "hidden", 1);
					}
				}
			}
			//post(dict.getkeys() + "\n");
			dict.freepeer();
		}
	}
}
