package com.tecartlab.sparck.pixelpusher;
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

import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;

public class PixelStripData {

	private Pixel[] myPixels;

	private String stripName;

	private boolean enable;

	// led count is number of pixels the assigned ledstrip has
	private int ledCount;

	public PixelStripData(){
		reset(1);
		enable = false;
	}

	public void reset(int _stripSize){
		myPixels = new Pixel[_stripSize];
		for(int i = 0; i < _stripSize; i++){
			myPixels[i] = new Pixel();
		}
	}

	public void reset(){
		reset(myPixels.length);
	}

	public void setStripName(String _stripName){
		stripName = _stripName;
	}

	public String getStripName(){
		return stripName;
	}

	public void setStripLedCount(int _ledCount){
		ledCount = _ledCount;
	}

	public void setEnable(boolean _enable){
		enable = _enable;
	}

	/**
	 * returns true if there is pixel at this position
	 * @param _pos
	 * @return true / false
	 */
	public boolean hasPixel(int _pos){
		return (myPixels != null && _pos < myPixels.length)?true: false;
	}

	/**
	 * returns the pixel at this position
	 * @param _pos
	 * @return the pixel, null if the request is out of bounds
	 */
	public Pixel getPixel(int _pos){
		return myPixels[_pos];
	}

	public void setPixel(int _pos, byte _red, byte _green, byte _blue){
		myPixels[_pos].red = _red;
		myPixels[_pos].green = _green;
		myPixels[_pos].blue = _blue;
	}

	public boolean hasPixels(){
		return (myPixels != null)?true: false;
	}

	public Pixel[] getPixels(){
		return myPixels;
	}

}
