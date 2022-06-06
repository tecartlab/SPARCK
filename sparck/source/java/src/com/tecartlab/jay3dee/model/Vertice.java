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

package com.tecartlab.jay3dee.model;

import com.tecartlab.tools.math.la.*;

public class Vertice extends Vector3f {

	protected boolean isSelected;
	protected boolean isPicked;
	protected boolean isPickable = true;

	public int index;

	public Vertice(float x, float y, float z){
		super(x, y, z);
		isSelected = false;
		isPicked = false;
		index = -1;
	}

	public Vertice(float x, float y){
		this(x, y, 0);
	}

	public Vertice(Vertice _vert){
		this(_vert.v_[0], _vert.v_[1], _vert.v_[2]);
		this.index = _vert.index;
		this.isPicked = _vert.isPicked;
		this.isSelected = _vert.isSelected;
	}

	public Vertice(){
		this(0, 0, 0);
	}

	public int getIndex(){
		return index;
	}

	public boolean isSelected(){
		return isSelected;
	}

	public boolean isPicked(){
		return isPicked;
	}

	public Vector3f worldTransformation(Matrix4x4f matrix){
		//Vector3f result = new Vector3f(x, y, z);
		//matrix.transform(result);
		//return result;
		return null;
	}

	public Vertice clone(){
		Vertice clone = new Vertice();
		clone.set(x(), y(), z());
		clone.isPicked = isPicked;
		clone.isSelected = isSelected;
		clone.index = index;
		return clone;
	}

}
