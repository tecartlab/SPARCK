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

package com.tecartlab.jay3dee;

import java.util.ArrayList;
import java.util.Iterator;

import com.cycling74.max.*;
import com.cycling74.jitter.*;

import com.tecartlab.jay3dee.model.drawer.MaxSketchMessage;

public class SketchCanvas implements GLCanvas{

	public int outletNumber;
	private JitterObject canvas;

	public SketchCanvas(JitterObject _canvas, int layer){
		canvas = _canvas;
		canvas.setAttr("depth_enable", 0);
		canvas.setAttr("layer", layer);
	}

	public SketchCanvas(int layer){
		canvas = new JitterObject("jit.gl.sketch");
		canvas.setAttr("depth_enable", 0);
		canvas.setAttr("layer", layer);
	}

	public void notifyDeleted()
	{
		canvas.call("enable", 0);
		canvas.freePeer();
	}

	public void call(String message, Atom[] params){
		canvas.call(message, params);
	}

	public void executeMessage(MaxSketchMessage command){
		command.execute(canvas);
	}

	public void drawGlCommand(ArrayList<MaxSketchMessage> commands){
		Iterator c = commands.iterator();
		while(c.hasNext()){
			((MaxSketchMessage) c.next()).execute(canvas);
		}
	}


}
