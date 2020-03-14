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

import java.io.IOException;
import java.io.PrintWriter;

import com.cycling74.jitter.JitterEvent;
import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

/**
 * This class is a barebone mirror structure and should be extended by classes that whish to have
 * the mirror functionality
 *
 * @author maybites
 *
 */
public class JitterMirror extends MaxObject{

	public final JitterConnector connector;

	private final int JIT_MIRROR_INLET = 2;
	private final int JIT_MIRROR_OUTLET = 2;

	private final int JIT_MSG_INLET = 1;
	private final int JIT_MSG_OUTLET = 1;

	private final int MAIN_INLET = 0;
	private final int MAIN_OUTLET = 0;

	/**
	 * This method needs to be called by a child class
	 * @param args
	 */
	public JitterMirror(Atom args[]){

		declareInlets(new int[]{ DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
		declareOutlets(new int[]{ DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
		setInletAssist(new String[] { "bang to initialize",  "messages to jitter object", "connected to right outlet of jitterobject"});
		setOutletAssist(new String[] { "messages from this object",  "messages from jitter object", "connected to left inlet of jitterobject"});
		createInfoOutlet(false);

		connector = new JitterConnector(this);
	}

	/**
	 * The Child class can overwrite this method and decide what to do with it,
	 * since some Events can be caused by user interactions to the jitter object.
	 *
	 * Just make sure to implement the line:
	 *
	 * super.connector.maxResponse(message, args);
	 *
	 * otherwise queries to the jitter object will be go into that data nirvana.
	 *
	 * @param event
	 */
	public void jitterEvent(String message, Atom[] args){
		connector.maxResponse(message, args);
	}

	/**
	 * The Child class can overwrite this method and decide what to do with it.
	 * This message is called if a change was made to the mirrored jitter object,
	 * no matter if on the maxMSP level or via the java handle.
	 *
	 * @param messagename
	 */
	public void changeEvent(String messagename){
	}

	public void anything(String message, Atom[] args){
		if(getInlet() == JIT_MIRROR_INLET){
			//This is a response message from the mirrored jitter object
			jitterEvent(message, args);
		} else if(getInlet() == JIT_MSG_INLET){
			if(message.startsWith("@") && args.length > 0){
				//This is an attribute message to the mirrored jitter object
				connector.setAttr(message.substring(1), args);
			} else{
				Atom[] ret;
				if(args.length > 0)
					//This is a message to the mirrored jitter object
					ret = connector.send(message, args);
				else
					//This is a message to the mirrored jitter object
					ret = connector.send(message);
				if(ret != null && ret.length > 0)
					messageOutput(ret);
			}
		} else {
			// messages meant for this max object
		}
	}

	/**
	 * Prints a Message to the Jitter Message Outlet
	 * @param msg
	 */
	public void messageOutput(Atom[] msg){
		outlet(JIT_MSG_OUTLET, msg);
	}

	/**
	 * Prints a Message to the Jitter Message Outlet
	 * @param msg
	 */
	public void messageOutput(String message, Atom[] args){
		outlet(JIT_MSG_OUTLET, message, args);
	}

	/**
	 * Prints a Message to the Jitter mirror Outlet
	 * @param msg
	 */
	public void mirrorOutput(String message){
		changeEvent(message);
		outlet(JIT_MIRROR_OUTLET, message);
	}

	/**
	 * Prints a Message to the Jitter mirror Outlet
	 * @param msg
	 */
	public void mirrorOutput(String message, Atom[] args){
		changeEvent(message);
		outlet(JIT_MIRROR_OUTLET, message, args);
	}
}
