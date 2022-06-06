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

package com.tecartlab.tools.threedee;

public class Viewport {

	/**
	 * Width of viewport
	 */
	public float width;

	/**
	 * Height of viewport
	 */
	public float height;

	/**
	 * lower Left Corner of viewport
	 */
	public float posX;

	/**
	 * lower Left Corner of viewport
	 */
	public float posY;

	/**
	 * Create Viewport with normalized values
	 */
	public Viewport(){
		width = 1.f;
		height = 1.f;
	}

	/**
	 * Create Viewport
	 *
	 * @param _width
	 * @param _height
	 */
	public Viewport(float _width, float _height){
		width = _width;
		height = _height;
	}

	/**
	 * Create Viewport
	 *
	 * @param _posX lower left corner
	 * @param _posY lower left corner
	 * @param _width
	 * @param _height
	 */
	public Viewport(float _posX, float _posY, float _width, float _height){
		posX = _posX;
		posY = _posY;
		width = _width;
		height = _height;
	}

	/**
	 * Viewport coordinates in form of a float array, which sets the
	 * left(=xPos), bottom(=yPos), width, and height of the viewport
	 * @param _viewport
	 */
	public Viewport(float[] _viewport){
		posX = _viewport[0];
		posY = _viewport[1];
		width = _viewport[2];
		height = _viewport[3];
	}

	public Viewport clone(){
		return new Viewport(posX, posY, width, height);
	}
}
