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
 
NODE.IOlets = function ( _offset, _size, _shift, _myType2Color, _myColorTable ) {
    this.myButtonOffset = _offset;
    this.myButtonSize = _size;
    this.myButtonShift = _shift;
    
    this.myType2Color = _myType2Color;
    this.myColorTable = _myColorTable;
    
	this.Inlets = [];
	this.InletsPos = [];
	this.Outlets = [];
    
    this.posCollapsed = 0;
    this.posExpanded = 0;
    this.myDefaultSize = [200, 30];
    this.myNodePatcher = null;
};

NODE.IOlets.prototype = {

	constructor: NODE.IOlets,

    /* _vpl_nodePatcher: instance to the patcher that contains the IOlets
     * myDefaultSize: size of the nodebox
     */
	init: function ( _vpl_nodePatcher, _myDefaultSize) {
        this.myDefaultSize = _myDefaultSize;
        this.myNodePatcher = _vpl_nodePatcher;
        this.posCollapsed = this.myDefaultSize[1] - this.myButtonSize/2 + this.myButtonShift;
        this.posExpanded = this.myDefaultSize[1] - this.myButtonSize/2 + this.myButtonShift;

        //post("init iolets..\n")
        var objects = this.myNodePatcher.firstobject;
		while(objects != null){
			// find all bpatcher with the script name inlet and outlet and set
			// their postion according to their ids
 			if(objects.varname.indexOf("vpl_inlet") == 0){
                //post("_myDefaultSize: " + _myDefaultSize + "\n");
				var xpos = this.getIOLetXPos(objects.varname, this.myDefaultSize[0]);
                this.Inlets.push(objects.varname);
                this.InletsPos.push(xpos);
                //post("xpos: " + xpos + "\n");
				this.myNodePatcher.message("script", "sendbox", objects.varname, "presentation_rect", xpos + this.myButtonOffset, 0., this.myButtonSize, this.myButtonSize);
				var subpat = objects.subpatcher();
				if(subpat != null){
					subpat.apply(this.setColor);
					//post(" found subpatcher: " + subpat + "\n");
				}
			}else if(objects.varname.indexOf("vpl_outlet") == 0){
				var xpos = this.getIOLetXPos(objects.varname, this.myDefaultSize[0]);
                this.Outlets.push(objects.varname);
 				this.myNodePatcher.message("script", "sendbox", objects.varname, "presentation_rect", xpos + this.myButtonOffset, this.posCollapsed, this.myButtonSize, this.myButtonSize);
				var subpat = objects.subpatcher();
				if(subpat != null){
					subpat.apply(this.setColor);
					//post(" found subpatcher: " + subpat.box.varname + "\n");
				}
			}
            objects = objects.nextobject;
        }
	},

    expand: function ( _expanded , _myExpandedSize, _myDefaultSize) {
        this.posExpanded = _myExpandedSize[1] - this.myButtonSize/2 + this.myButtonShift;
        for(var i = 0; i < this.Outlets.length; i++){
            var xpos = this.getIOLetXPos(this.Outlets[i], (_expanded)?_myExpandedSize[0]: _myDefaultSize[0]);
            this.myNodePatcher.message("script", "sendbox", this.Outlets[i], "presentation_rect", xpos + this.myButtonOffset, this.getPosition(_expanded), this.myButtonSize, this.myButtonSize);
        }
	},

    getPosition: function ( _expanded ) {
        return (_expanded == 1)?this.posExpanded:this.posCollapsed;
	},

    getIOLetXPos: function(_a, _width){
        var indexes = this.bracketEnclosure(_a).split("/");
        var index = indexes[0];
        var total = indexes[1]; //9.5
        //post("inlet position index: " + index + "/total: " + total + "\n");
        var dividable = _width - 18; //3.5 = xpos for index 1
        //post("dividable: " + dividable + "\n");
        var steps = dividable / (total - 1);
        return 10 + (index - 1) * steps - this.myButtonSize/2;
    },

    bracketEnclosure: function (a){
	   return a.substring(a.indexOf("[") + 1, a.indexOf("]"));
    },

    setColor: function (a) {
        //find all buttons with the script name inlet and outlet and set
        // their colors according to their type
        if(a.maxclass.indexOf("led") != -1){
            var localThisPatch = a.patcher;
    		//post("found button: ->|" + a.varname + "|<- \n");
            var types = a.varname;
            var color = this.getColor(types);
            //post(" setting colors for buttons: " + a.varname + " for types " + types + "\n");
            if(color != null){
                //post("found color: " + color + "\n");
                var colormsg = myColorTable.get(color);
                //post("found message: " + colormsg + "\n");
                a.message("oncolor", colormsg[2] * 0.5, colormsg[3] * 0.5, colormsg[4] * 0.5, colormsg[5]);
                a.message("offcolor", colormsg[2], colormsg[3], colormsg[4], colormsg[5]);
                //a.message("bgcolor", colormsg[2], colormsg[3], colormsg[4], colormsg[5]);
    //			a.message(colormsg[1], colormsg[2], colormsg[3], colormsg[4], colormsg[5]);
            }else{
                a.message("oncolor", .25, .25, .25, 1.);
                a.message("offcolor", .5, .5, .5, 1.);
                //a.message("bgcolor", .5, .5, .5, 1.);
    //			a.message("fgcolor", .5, .5, .5, 1.);
            }

            localThisPatch.message("script", "sendbox", a.varname, "size", myIOLetButtonSize, myIOLetButtonSize);
        }

        return true;
    },

    getColorID: function (types){
        var color = this.getColor(types);
        var colorid = 0;
        if(color != null){
            var colormsg = this.myColorTable.get(color);
            colorid = colormsg[0];
        }
        return colorid;
    },

    getColor: function (types){
        var typeArray = types.split("_");
        if(typeArray.length > 0){
                //post("use type: >" + typeArray[0] + "< \n");
            var color = this.myType2Color.get(typeArray[0]);
            return color;
        }
        return null;
    },

    freepeer: function ( ) {
    }
    
};
