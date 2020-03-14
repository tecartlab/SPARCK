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

 DRAW.Warp2d_EditorHelper = function ( ) {
    this.helpLine = new Array(20);
    this.currentColor = 0.4;

    for(var i = 0; i < this.helpLine.length; i++){
        this.helpLine[i] = new JitterObject("jit.gl.text");
        this.set(this.helpLine[i], [-.95, 0.9 - 0.095 * i, 0.]);
    }

    this.enableDraw = 0;
};

DRAW.Warp2d_EditorHelper.prototype = {

	constructor: DRAW.Warp2d_EditorHelper,

    set: function( _set, _pos){
  		_set.layer = 20;
		_set.font("Courier New");
        _set.transform_reset = 2;
		_set.face("bold");
		_set.scale = [1, 1, 1];
		_set.size(16);
		_set.position = _pos;
		_set.transform_reset = 2;
		_set.color = [this.currentColor, this.currentColor, this.currentColor, 1.];
 	},

    toggleEnable: function(){
        this.enableDraw = 1 - this.enableDraw;
        this.enable(this.enableDraw);
    },

	enable: function(_enable){
        for(var i = 0; i < this.helpLine.length; i++){
            this.helpLine[i].enable = _enable;
        }
	},

    changeColor: function(){
		this.currentColor = (this.currentColor < 0)? 1.0: this.currentColor - 0.1;
 		for(var i = 0; i < this.helpLine.length; i++){
			this.helpLine[i].color = [this.currentColor, this.currentColor, this.currentColor, 1.0];
		}
	},

    drawto: function(_drawto){
        for(var i = 0; i < this.helpLine.length; i++){
            this.helpLine[i].drawto = _drawto;
        }
	},

	freepeer: function(){
        for(var i = 0; i < this.helpLine.length; i++){
            this.helpLine[i].reepeer();
        }
	},

    printLATTICE_SELECT: function(){
		this.helpLine[0].text  ( "Mode: [ LATTICE - SELECT ]" );
		this.helpLine[1].text  ( "tab   : switch Mode " );
		this.helpLine[2].text  ( "a     : select all control points" );
		this.helpLine[3].text  ( "mouse : select single control point" );
		this.helpLine[4].text  ( "shift + mouse: select multiple control points" );
		this.helpLine[5].text  ( "g     : grab control points" );
		this.helpLine[6].text  ( "i     : reset control points" );
		this.helpLine[7].text  ( "" );
		this.helpLine[8].text  ( "" );
		this.helpLine[9].text  ( "" );
		this.helpLine[10].text ( "" );
		this.helpLine[11].text ( "" );
		this.helpLine[12].text ( "" );
		this.helpLine[12].text ( "" );
		this.helpLine[13].text ( "" );
		this.helpLine[14].text ( "z/Z   : undo / redo" );
		this.helpLine[15].text ( "f     : save file" );
		this.helpLine[16].text ( "<alt> + drag mouse: shift canvas" );
		this.helpLine[17].text ( "<ctrl>+ drag mouse: zoom canvas" );
		this.helpLine[18].text ( "H     : change help text color" );
		this.helpLine[19].text ( "h     : toggle this help" );
	},

    printLATTICE_GRAB: function(){
		this.helpLine[0].text  ( "Mode: [ LATTICE - GRAB ]" );
		this.helpLine[1].text  ( "" );
		this.helpLine[2].text  ( "mouse : set points" );
		this.helpLine[3].text  ( "" );
		this.helpLine[4].text  ( "" );
		this.helpLine[5].text  ( "" );
		this.helpLine[6].text  ( "" );
		this.helpLine[7].text  ( "" );
		this.helpLine[8].text  ( "" );
		this.helpLine[9].text  ( "" );
		this.helpLine[10].text ( "" );
		this.helpLine[11].text ( "" );
		this.helpLine[12].text ( "" );
		this.helpLine[12].text ( "" );
		this.helpLine[13].text ( "" );
		this.helpLine[14].text ( "" );
		this.helpLine[15].text ( "f     : save file" );
		this.helpLine[16].text ( "<alt> + drag mouse: shift canvas" );
		this.helpLine[17].text ( "<ctrl>+ drag mouse: zoom canvas" );
		this.helpLine[18].text ( "H     : change help text color" );
		this.helpLine[19].text ( "h     : toggle this help" );
	},

    printMESH_SELECT: function(_){
		this.helpLine[0].text  ( "Mode:  [ MESH - SELECT ]");
		this.helpLine[1].text  ( "tab   : switch Mode " );
		this.helpLine[2].text  ( "a     : select all control points" );
		this.helpLine[3].text  ( "mouse : select single control point" );
		this.helpLine[4].text  ( "shift + mouse: select multiple control points" );
		this.helpLine[5].text  ( "g/s/r : grab/scale/rotate control points" );
		this.helpLine[6].text  ( "i     : reset control points" );
		this.helpLine[7].text  ( "p     : store selection" );
		this.helpLine[8].text  ( "0..9  : recall store selection" );
		this.helpLine[9].text  ( "q     : set transformation cursor" );
		this.helpLine[10].text ( "" );
		this.helpLine[11].text ( "" );
		this.helpLine[12].text ( "" );
		this.helpLine[12].text ( "" );
		this.helpLine[13].text ( "" );
		this.helpLine[14].text ( "z/Z   : undo / redo" );
		this.helpLine[15].text ( "f     : save file" );
		this.helpLine[16].text ( "<alt> + drag mouse: shift canvas" );
		this.helpLine[17].text ( "<ctrl>+ drag mouse: zoom canvas" );
		this.helpLine[18].text ( "H     : change help text color" );
		this.helpLine[19].text ( "h     : toggle this help" );
	},

    printMESH_SELECT_STORE: function(_){
		this.helpLine[0].text  ( "Mode:  [ MESH - SELECT - STORE]");
		this.helpLine[1].text  ( "tab   : exit " );
		this.helpLine[2].text  ( "0..9  : store selection" );
		this.helpLine[3].text  ( "" );
		this.helpLine[4].text  ( "" );
		this.helpLine[5].text  ( "" );
		this.helpLine[6].text  ( "" );
		this.helpLine[7].text  ( "" );
		this.helpLine[8].text  ( "" );
		this.helpLine[9].text  ( "" );
		this.helpLine[10].text ( "" );
		this.helpLine[11].text ( "" );
		this.helpLine[12].text ( "" );
		this.helpLine[13].text ( "" );
		this.helpLine[14].text ( "" );
		this.helpLine[15].text ( "" );
		this.helpLine[16].text ( "<alt> + mouse: shift canvas" );
		this.helpLine[17].text ( "<ctrl>+ drag mouse: zoom canvas" );
		this.helpLine[18].text ( "H     : change help text color" );
		this.helpLine[19].text ( "h     : toggle this help" );
	},

    printMESH_MODIFY: function(){
		this.helpLine[0].text  ( "Mode: [ MESH - MODIFY ]" );
		this.helpLine[1].text  ( "" );
		this.helpLine[2].text  ( "mouse : set points" );
		this.helpLine[3].text  ( "" );
		this.helpLine[4].text  ( "" );
		this.helpLine[5].text  ( "" );
		this.helpLine[6].text  ( "" );
		this.helpLine[7].text  ( "" );
		this.helpLine[8].text  ( "" );
		this.helpLine[9].text  ( "" );
		this.helpLine[10].text ( "" );
		this.helpLine[11].text ( "" );
		this.helpLine[12].text ( "" );
		this.helpLine[12].text ( "" );
		this.helpLine[13].text ( "" );
		this.helpLine[14].text ( "" );
		this.helpLine[15].text ( "f     : save file" );
		this.helpLine[16].text ( "<alt> + drag mouse: shift canvas" );
		this.helpLine[17].text ( "<ctrl>+ drag mouse: zoom canvas" );
		this.helpLine[18].text ( "H     : change help text color" );
		this.helpLine[19].text ( "h     : toggle this help" );
	},

    freepeer: function(){
        for(var i = 0; i < this.helpLine.length; i++){
            this.helpLine[i].freepeer();
        }
    }
};
