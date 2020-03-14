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

GUI.UIEvent = function ( ) {
    this.keyUndef = 'undef';
	this.currentPosX = 0;
	this.currentPosY = 0;
	this.lastPosX = 0;
	this.lastPosY = 0;
	this.lastDragPosX = 0;
	this.lastDragPosY = 0;
	this.mouseButtonHit = false;
	this.mouseButtonPress = false;
	this.mouseButtonRelease = false;
	this.special_CtrlKey = false;
	this.special_ShiftKey = false;
	this.special_AltKey = false;
	this.mouseWheel = 0;
	this.keyChar = this.keyUndef;
	this.keyHit = false;
	this.keyPress = false;
	this.keyRelease = false;
	this.rayOrigin = new THREE.Vector3(0, 0, 0);
	this.rayDirect = new THREE.Vector3(0, 0, 1);
	this.pickRay = new THREE.Ray(this.rayOrigin, this.rayDirect);
    this.windowSizeX = 320;
    this.windowSizeY = 240;
	this.hasNewPickRay = false;
    this.mouseInWindows = false;
 	this.hasUpdate = false;
};

GUI.UIEvent.prototype = {

	constructor: GUI.UIEvent,

	mouse: function ( _posX, _posY, _pressed, _cmndKey, _shiftKey, _unknown, _altKey, _ctrlKey, _wheel ) {
		this.currentPosX = _posX;
		this.currentPosY = _posY;
        var presd = (_pressed == 1)?true:false;
		if(presd != this.mouseButtonPress){
			this.mouseButtonHit = presd;
  			this.mouseButtonPress = presd;
			this.mouseButtonRelease = !presd;
            this.lastDragPosX = this.currentPosX;
            this.lastDragPosY = this.currentPosY;
//			post("got change = " + presd + " this.mouseButtonHit = " + this.mouseButtonHit +" \n");
		} else if (this.hasUpdate == false){
			// check if this function is called twice before update()
			this.mouseButtonHit = false;
			this.mouseButtonRelease = false;
		}

        this.special_ShiftKey = (_shiftKey == 1)?true: false;
        this.special_CtrlKey = (_ctrlKey == 1)?true: false;
        this.special_AltKey = (_altKey == 1)?true: false;

        this.mouseWheel = _wheel;
        this.mouseInWindows = true;
        this.hasUpdate = true;
	},

	key: function ( _keyChar, _pressed ) {
		if((_pressed == 1)?true:false != this.keyPress){
			if(_pressed == 1){
				this.keyHit = true;
				this.keyPress = true;
				this.keyRelease = false;
			} else {
				this.keyHit = false;
				this.keyPress = false;
				this.keyRelease = true;
			}
		} else {
			this.keyHit = false;
			this.keyRelease = false;
		}
		this.keyChar = _keyChar;
        this.hasUpdate = true;
	},

	pickray: function ( _origX, _origY, _origZ, _targX, _targY, _targZ ) {
		this.rayOrigin.x = _origX;
		this.rayOrigin.y = _origY;
		this.rayOrigin.z = _origZ;
		this.rayDirect.x = _targX - _origX;
		this.rayDirect.y = _targY - _origY;
		this.rayDirect.z = _targZ - _origZ;
		this.rayDirect.normalize();
		this.pickRay.origin = this.rayOrigin;
		this.pickRay.direction = this.rayDirect;
		this.hasNewPickRay = true;
        this.hasUpdate = true;
	},

    windowSize: function ( _sizeX, _sizeY ) {
        this.windowSizeX = _sizeX;
        this.windowSizeY = _sizeY;
        this.mouseInWindows = false;
        this.hasUpdate = true;
    },

	update_start: function () {
        return this.hasUpdate;
	},

	update_end: function () {
		this.hasNewPickRay = false;
		this.mouseButtonHit = false;
		this.mouseButtonRelease = false;
		this.keyHit = false;
		this.keyRelease = false;
        this.keyChar = this.keyUndef;
        this.mousePlusKey = this.keyUndef;
		this.keyChar = this.keyUndef;
		this.lastPosX = this.currentPosX;
		this.lastPosY = this.currentPosY;
        this.lastDragPosX = this.currentPosX;
        this.lastDragPosY = this.currentPosY;
        this.hasUpdate = false;
	},

	getPickRay: function ( ) {
		return this.pickRay;
	}

};
