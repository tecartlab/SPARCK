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

import java.util.ArrayList;
import java.util.ListIterator;

import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;

public class ScanLineStore {

	ArrayList<PusherPixel> store;
	int myScanLine;

	float normalizedScanLine;

	int width;
	int height;

	int iterIndex = 0;

	public ScanLineStore(int _scanLine, int _imgWidth, int _imgHeight){
		myScanLine = _scanLine;
		width = _imgWidth;
		height = _imgHeight;
		store = new ArrayList<PusherPixel>();
		normalizedScanLine = (float)myScanLine / (float)height;
	}

	public int getMyScanline(int _height){
		return (int)(normalizedScanLine * _height);
	}

	public void addPixel(int posX, int posY, int _stripID, int _pos){
		store.add(new PusherPixel((float) posX / (float) width, (float) posY / (float) height, _stripID, _pos));
	}

	public ListIterator<PusherPixel> listIterator(){
		return store.listIterator();
	}

	public void clear(){
		store.clear();
	}

	public boolean isTouched(){
		return (store.size() > 0)? true: false;
	}

}
