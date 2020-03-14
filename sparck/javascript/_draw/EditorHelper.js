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

DRAW.EditorHelper = function ( ) {
    this.helpLine0 = new JitterObject("jit.gl.text");
    this.helpLine1 = new JitterObject("jit.gl.text");
    this.helpLine2 = new JitterObject("jit.gl.text");
    this.helpLine3 = new JitterObject("jit.gl.text");
    this.helpLine4 = new JitterObject("jit.gl.text");
    this.helpLine5 = new JitterObject("jit.gl.text");
    this.helpLine6 = new JitterObject("jit.gl.text");
    this.helpLine7 = new JitterObject("jit.gl.text");
    this.helpLine8 = new JitterObject("jit.gl.text");
    this.helpLine9 = new JitterObject("jit.gl.text");

    this.set(this.helpLine0, [-0.95, 0.90, 0.]);
    this.set(this.helpLine1, [-.95, .80, 0.]);
    this.set(this.helpLine2, [-.95, .70, 0.]);
    this.set(this.helpLine3, [-.95, .60, 0.]);
    this.set(this.helpLine4, [-.95, .50, 0.]);
    this.set(this.helpLine5, [-.95, .40, 0.]);
    this.set(this.helpLine6, [-.95, .30, 0.]);
    this.set(this.helpLine7, [-.95, .20, 0.]);
    this.set(this.helpLine8, [-.95, .10, 0.]);
    this.set(this.helpLine9, [-.95, .00, 0.]);

    this.enableDraw = 0;
};

DRAW.EditorHelper.prototype = {

	constructor: DRAW.EditorHelper,

    set: function( _set, _pos){
  		_set.layer = 20;
		_set.font("Courier New");
        _set.transform_reset = 2;
		_set.face("bold");
		_set.scale = [1, 1, 1];
		_set.size( 20);
		_set.position = _pos;
 	},

    toggleEnable: function(){
        this.enableDraw = 1 - this.enableDraw;
        this.enable(this.enableDraw);
    },

	enable: function(_enable){
		this.helpLine0.enable = _enable;
		this.helpLine1.enable = _enable;
		this.helpLine2.enable = _enable;
		this.helpLine3.enable = _enable;
		this.helpLine4.enable = _enable;
		this.helpLine5.enable = _enable;
		this.helpLine6.enable = _enable;
		this.helpLine7.enable = _enable;
		this.helpLine8.enable = _enable;
		this.helpLine9.enable = _enable;
	},

    drawto: function(_drawto){
		this.helpLine0.drawto = _drawto;
		this.helpLine1.drawto = _drawto;
		this.helpLine2.drawto = _drawto;
		this.helpLine3.drawto = _drawto;
		this.helpLine4.drawto = _drawto;
		this.helpLine5.drawto = _drawto;
		this.helpLine6.drawto = _drawto;
		this.helpLine7.drawto = _drawto;
		this.helpLine8.drawto = _drawto;
		this.helpLine9.drawto = _drawto;
 //       post("this.helpLine0.drawto = " + this.helpLine0.drawto + "\n");
	},

	printLINE: function(_lineThickness, _fgColor){
		this.helpLine0.text ( "Mode:  [ LINE ]" );
		this.helpLine1.text ( "tab   - switch Mode " );
		this.helpLine2.text ( "mouse - draw line" );
		this.helpLine3.text ( "up    - make line thicker (" + _lineThickness + ")" );
		this.helpLine4.text ( "down  - make line thinner" );
		this.helpLine5.text ( "space - toggle pen color (" + _fgColor + ")" );
		this.helpLine6.text ( "s     - save image" );
		this.helpLine7.text ( "shift + mouse: shift canvas" );
		this.helpLine8.text ( "ctrl  + mouse: zoom canvas" );
		this.helpLine9.text ( "h     - toggle this help" );
	},

    printPOLY: function(_fgColor){
		this.helpLine0.text ( "Mode:  [ POLY ]" );
		this.helpLine1.text ( "tab   - switch Mode " );
		this.helpLine2.text ( "mouse - set points" );
		this.helpLine3.text ( "enter - poly end" );
		this.helpLine4.text ( "" );
		this.helpLine5.text ( "space - toggle pen color (" + _fgColor + ")" );
		this.helpLine6.text ( "s     - save image" );
		this.helpLine7.text ( "shift + mouse: shift canvas" );
		this.helpLine8.text ( "ctrl  + mouse: zoom canvas" );
		this.helpLine9.text ( "h     - toggle this help" );
	},

    printCOLOR: function(_bgColor, _fgColor){
		this.helpLine0.text ( "Mode:  [ COLOR ]");
		this.helpLine1.text ( "tab   - switch Mode " );
		this.helpLine2.text ( "c     - clear canvas with " + _bgColor );
		this.helpLine3.text ( "" );
		this.helpLine4.text ( "" );
		this.helpLine5.text ( "space - toggle pen color (" + _fgColor + ")" );
		this.helpLine6.text ( "s     - save image" );
		this.helpLine7.text ( "shift + mouse: shift canvas" );
		this.helpLine8.text ( "ctrl  + mouse: zoom canvas" );
		this.helpLine9.text ( "h     - toggle this help" );
	},

	freepeer: function(){
		this.helpLine0.freepeer();
		this.helpLine1.freepeer();
		this.helpLine2.freepeer();
		this.helpLine3.freepeer();
		this.helpLine4.freepeer();
		this.helpLine5.freepeer();
		this.helpLine6.freepeer();
		this.helpLine7.freepeer();
		this.helpLine8.freepeer();
		this.helpLine9.freepeer();
	}
};
