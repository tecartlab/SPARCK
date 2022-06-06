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

package com.tecartlab.mxj.utils.mirror;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxObject;

/**
 * This class makes the connection between a more sophisticated Jitter Interface Object and JitterMirror.
 *
 * @author maybites
 *
 */
public class JitterConnector {

	private JitterMirror max;
	private Atom[] response;

	public boolean isInitialized = false;

	public JitterConnector(JitterMirror _max){
		max = _max;
		isInitialized = true;
	}

	/**
	 * This method is called as a response from the mirrored jitter object
	 * @param message
	 * @param args
	 */
	public final void maxResponse(String message, Atom[] args){
		response = new Atom[args.length + 1];
		response[0] = Atom.newAtom(message);
		for(int i = 0; i < args.length; i++){
			response[i + 1] = args[i];
		}
	}

	public final void bang(){
		output("bang", null);
	}

	/**
	 * A way to get other kind of messages
	 *
	 * Atom.toInt(send(messagename, Atom.newAtom(value)))
	 *
	 * @param messagename
	 * @param args
	 * @return
	 */
	public final Atom[] send(String messagename, Atom[] args){
		response = null;
		output(messagename, args);
		return response;
	}

	public final boolean setAttr(String attrname, Atom[] args){
		output(attrname, args);
		return true;
	}

	public final Atom[] send(String messagename){
		response = null;
		output(messagename, null);
		return response;
	}

	public final Atom[] getAttr(String attrname){
		response = null;
		output(attrname, null);
		return response;
	}

	public static Atom[] dropFirstIndex(Atom[] args){
		if(args != null && args.length > 1){
			Atom[] ret = new Atom[args.length - 1];
			for(int i = 1; i < args.length; i++){
				ret[i - 1] = args[i];
			}
			return ret;
		}
		return null;
	}

	private final void output(String messagename, Atom[] args){
		if(args != null)
			max.mirrorOutput(messagename, args);
		else
			max.mirrorOutput(messagename);
	}
}
