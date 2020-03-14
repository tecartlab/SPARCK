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

public class JitGlCameraHandler{

	private JitterConnector camera;

	protected boolean hasChanged = false;

	public JitGlCameraHandler(JitterConnector _camera) {
		camera = _camera;
	}

	/**
	 * Calling this message will return true if there was a change to the camera object
	 * @return
	 */
	public boolean hasChanged(){
		boolean change = hasChanged;
		hasChanged = false;
		return change;
	}

	public float[] getViewportRay(int xPos, int yPos){
		Atom[] viewPortRay = camera.send("getviewportray", new Atom[] {Atom.newAtom(xPos), Atom.newAtom(yPos)});
		return (viewPortRay != null && viewPortRay.length == 7)?Atom.toFloat(camera.dropFirstIndex(viewPortRay)) : null;
	}

	public void position(float xPos, float yPos, float zPos){
		camera.send("position", new Atom[] {Atom.newAtom(xPos), Atom.newAtom(yPos), Atom.newAtom(zPos)});
	}

	public void quat(float xReal, float yReal, float zReal, float w){
		camera.send("quat", new Atom[] {Atom.newAtom(xReal), Atom.newAtom(yReal), Atom.newAtom(zReal), Atom.newAtom(w)});
	}

	public void position(float[] position){
		camera.send("position", Atom.newAtom(position));
	}

	public void quat(float[] quat){
		camera.send("quat", Atom.newAtom(quat));
	}

	public void frustum(float[] frustum){
		camera.send("frustum", Atom.newAtom(frustum));
	}

	public void projection_mode(String mode){
		camera.send("projection_mode", new Atom[] {Atom.newAtom(mode)});
	}

	public float[] getViewport(){
		return Atom.toFloat(camera.dropFirstIndex(camera.send("getviewport")));
	}

	public String getContext(){
		return Atom.toOneString(camera.dropFirstIndex(camera.send("getdrawto")));
	}

	public boolean isOperational(){
		return true;
	}
}
