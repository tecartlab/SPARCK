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

package com.tecartlab.mxj.utils;

import com.cycling74.max.Atom;

public class Atomizer {

	private Atom[] myArray;

	public Atomizer(){
		clear();
	}

	public void clear(){
		myArray = new Atom[0];
	}

	public Atom[] getArray(){
		return myArray;
	}

	public Atomizer append(Atom atom){
		Atom[] newArray = new Atom[myArray.length + 1];
		for(int i = 0; i < myArray.length; i++)
			newArray[i] = myArray[i];
		newArray[newArray.length -1] = atom;
		myArray = newArray;
		return this;
	}

	public Atomizer prepend(Atom atom){
		Atom[] newArray = new Atom[myArray.length + 1];
		for(int i = 0; i < myArray.length; i++)
			newArray[i + 1] = myArray[i];
		newArray[0] = atom;
		myArray = newArray;
		return this;
	}

}
