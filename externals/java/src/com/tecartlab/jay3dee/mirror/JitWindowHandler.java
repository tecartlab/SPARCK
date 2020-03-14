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

package com.tecartlab.jay3dee.mirror;

import com.tecartlab.mxj.utils.mirror.JitterConnector;

import com.cycling74.max.Atom;

public class JitWindowHandler{

	private JitterConnector window;

	public JitWindowHandler(JitterConnector _window) {
		window = _window;
	}

	public void setPos(int xPos, int yPos){
		window.send("pos", new Atom[] {Atom.newAtom(xPos), Atom.newAtom(yPos)});
	}

	public int[] getPos(){
		return Atom.toInt(window.dropFirstIndex(window.send("getpos")));
	}

	public void setRect(int xPos, int yPos, int width, int height){
		window.send("rect", new Atom[] {Atom.newAtom(xPos), Atom.newAtom(yPos), Atom.newAtom(width), Atom.newAtom(height)});
	}

	public int[] getRect(){
		return Atom.toInt(window.dropFirstIndex(window.send("getrect")));
	}

	public void setSize(int xPos, int yPos){
		window.send("size", new Atom[] {Atom.newAtom(xPos), Atom.newAtom(yPos)});
	}

	public void idlemouse(int flag){
		window.send("idlemouse", new Atom[] {Atom.newAtom(flag)});
	}

	public int[] getSize(){
		return Atom.toInt(window.dropFirstIndex(window.send("getsize")));
	}

	public boolean isOperational(){
		return true;
	}

}
