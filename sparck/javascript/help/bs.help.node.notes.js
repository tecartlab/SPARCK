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

this.box.message("border", 0);
this.box.message("ignoreclick", 1);
mgraphics.init();
mgraphics.relative_coords = 0;
mgraphics.autofill = 0;
var bgcolor = this.patcher.getattr("locked_bgcolor");
var alpha = 1.0;
var name = jsarguments[1];
var type = jsarguments[2];
var dict;
var shortDesc = "";
var longDesc = "";

var maxSeeAlsos = 3;
var maxTutorials = 3;

var notes;
var description;

function loadbang()
{
	dict = new Dict();
	dict.import_json(name + ".en.sparckref.json");
    if (typeof(dict) == "object") {
		if(typeof(dict.get(type) == "object")){
			notes = dict.get(type).getkeys();
            //post("notes: " + notes + "\n");
			if(typeof notes == "string"){
				notes = new Array(notes);
			}
  			description = new Array();
			for(var i = 0; i < notes.length; i++){
                description.push(dict.get(type).get(notes[i]));
  			}
		}
		//post(dict.getkeys() + "\n");
		dict.freepeer();
	}
}

function paint()
{
	if(name){
    	with(mgraphics)
    	{
			set_source_rgba(bgcolor);
			paint();
    		select_font_face("Lato");
       	    set_font_size(12);
 			var position = 50;
			for(var i = 0; i < notes.length; i++){
                set_source_rgba(0.501961, 0.501961, 0.501961, 1.);
                position = wordwrap(description[i], position, 10) + 20;
            }
			move_to(4, position);
			fill();
    	}
	}
}

function wordwrap(str, pos, indent, width, brk, cut)
{
 	if(jsarguments[3]==null){jsarguments[3]=95};
    brk = brk || '\\cr';
    width = width || jsarguments[3];
    cut = cut || false;
    if (!str) { return str; }
    str = str.replace(/\t/g,'   ');
    str = str.replace(/<li>/g,'•');
    var regex = '.{1,' +width+ '}(\\s|$)' + (cut ? '|.{' +width+ '}|.+$' : '|\\S+?(\\s|$)');
	var v=str.match( RegExp(regex, 'g') );
	for(i=0;i<=v.length;i++){
        mgraphics.move_to(indent, pos+15*i);
        mgraphics.show_text(v[i], 1);
    }
	return pos+15*(v.length - 1);
}
