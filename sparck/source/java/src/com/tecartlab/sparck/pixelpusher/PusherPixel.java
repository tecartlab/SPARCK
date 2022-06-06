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

public class PusherPixel {

	public float posX;
	public float posY;

	public int stripID;
	public int pos;

	/**
	 * Set the PusherPixel with a reference to a Pixel instance and the normalized coordinates of the pixel position
	 * @param _pixel
	 * @param _x
	 * @param _y
	 */
	public PusherPixel(float _x, float _y, int _stripID, int _pos){
		posX = _x;
		posY = _y;

		stripID = _stripID;
		pos = _pos;
	}

	/**
	 * get the pixels X position providing the width of the image
	 * @param _width
	 * @return
	 */
	public int getPosX(int _width){
		return (int) (posX * _width);
	}

	/**
	 * get the pixels Y position providing the height of the image
	 *
	 * @param _height
	 * @return
	 */
	public int getPosY(int _height){
		return (int) (posY * _height);
	}
}
