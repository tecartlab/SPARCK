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

import java.util.List;

import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;

public class PixelPusherData {

	PixelStripData[] strips;

	int[] ledCounter;

	public PixelPusherData(){
		strips = new PixelStripData[8];
		ledCounter = new int[8];
		for(int i = 0; i < 8; i++){
			strips[i] = new PixelStripData();
			ledCounter[i] = 0;
		}
	}

	public void resetSize(PixelPusher _pusher){
		for(int i = 0; i < 8; i++){
			strips[i].reset(_pusher.getPixelsPerStrip());;
		}
	}

	public void resetStrips(){
		for(int i = 0; i < 8; i++){
			strips[i].reset();
		}
	}

	public void startLedCounter(){
		for(int i = 0; i < 8; i++){
			ledCounter[i] = 0;
		}
	}

	public void setStripLedCount(int _strip, int _ledCount){
		if(_strip < 8){
			strips[_strip].setStripLedCount(_ledCount);
		}
	}

	public void setStripEnable(int _strip, boolean _enable){
		if(_strip < 8){
			strips[_strip].setEnable(_enable);
		}
	}

	public void setStripName(int _strip, String _name){
		if(_strip < 8){
			strips[_strip].setStripName(_name);
		}
	}

	public boolean hasPixel(int _strip, int _pos){
		return (_strip < 8 && strips[_strip].hasPixel(_pos))?true: false;
	}

	public Pixel getPixel(int _strip, int _pos){
		return strips[_strip].getPixel(_pos);
	}

	public void setPixel(int _strip, int _pos, byte _red, byte _green, byte _blue){
		strips[_strip].setPixel(_pos, _red, _green, _blue);
		ledCounter[_strip]++;
	}

	public boolean hasStrip(int _strip){
		return (_strip < 8 && strips !=  null)?true: false;

	}

	public Pixel[] getStrip(int _strip){
		return strips[_strip].getPixels();
	}

	/**
	 * Check if there are strip pixels under this stripname
	 *
	 * @param _stripName
	 * @return true if it is so
	 */
	public boolean hasStrip(String _stripName){
		if(strips != null){
			for(int i = 0; i < strips.length; i++){
				if(strips[i].getStripName().equals(_stripName))
					return true;
			}
		}
		return false;
	}

	/**
	 * Get the strip pixels with the name of the strip. Test first with
	 *
	 * hasStrip(String _stripName)
	 *
	 * to see if there is data available. otherwise null will be returned.
	 *
	 * @param _stripName
	 * @return Pixel[] Array with all the pixels of the strip
	 */
	public Pixel[] getStrip(String _stripName){
		if(strips != null){
			for(int i = 0; i < strips.length; i++){
				if(strips[i].getStripName().equals(_stripName))
					return strips[i].getPixels();
			}
		}
		return null;
	}

}
