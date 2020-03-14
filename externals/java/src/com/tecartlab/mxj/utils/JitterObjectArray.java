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

import java.util.*;

import com.cycling74.jitter.JitterObject;
import com.cycling74.max.Atom;

/**
 * Wrapper Class for multiple instances of the same JitterObject that
 * needs to be available in multiple drawing contexts
 *
 * @author maybites
 *
 */
public class JitterObjectArray {

	private String jitterObjectType;
	private Hashtable<String, JitterObject> array;

	public JitterObjectArray(String _jitterObject){
		jitterObjectType = _jitterObject;
		array = new Hashtable<String, JitterObject>();
	}

	/**
	 * Clears the array from all associated JitterObjects and removes them safely from the System
	 */
	public void clear(){
		Iterator<JitterObject> i = array.values().iterator();
		while(i.hasNext()){
			i.next().freePeer();
			i.remove();
		}
	}

	/**
	 * Adds a new JitterObject drawing to the provided context if this context has not been used so far.
	 *
	 * @param _context
	 * @return true if an new object was created and added
	 */
	public boolean add(String _context){
		if(!array.containsKey(_context)){
			JitterObject next = new JitterObject(jitterObjectType);
			next.call("drawto", _context);
			array.put(_context, next);
			return true;
		}
		return false;
	}


	/**
	 * Removes the JitterObject that draws to this context
	 * @param _context
	 * @return true if an object was removed
	 */
	public boolean remove(String _context){
		if(array.containsKey(_context)){
			JitterObject next = array.get(_context);
			next.freePeer();
			array.remove(_context);
			return true;
		}
		return false;
	}

	/**
	 * enables the object with the specified context
	 * @param _context
	 * @param _enable 1 / 0
	 */
	public void enable(String _context, int _enable){
		if(array.containsKey(_context)){
			JitterObject next = array.get(_context);
			next.call("enable", _enable);
		}
	}

	/**
	 * Calls all the JitterObject inside this Array
	 * @param messagename
	 * @param value
	 */
	public void call(String messagename){
		Iterator<JitterObject> i = array.values().iterator();
		while(i.hasNext()){
			i.next().call(messagename);
		}
	}

	/**
	 * Calls all the JitterObject inside this Array
	 * @param messagename
	 * @param value
	 */
	public void call(String messagename, String value){
		Iterator<JitterObject> i = array.values().iterator();
		while(i.hasNext()){
			i.next().call(messagename, value);
		}
	}

	/**
	 * Calls all the JitterObject inside this Array
	 * @param messagename
	 * @param value
	 */
	public void call(String messagename, int value){
		Iterator<JitterObject> i = array.values().iterator();
		while(i.hasNext()){
			i.next().call(messagename, value);
		}
	}

	/**
	 * Calls all the JitterObject inside this Array
	 * @param messagename
	 * @param value
	 */
	public void call(String messagename, float[] values){
		Iterator<JitterObject> i = array.values().iterator();
		while(i.hasNext()){
			i.next().call(messagename, values);
		}
	}

	/**
	 * Calls all the JitterObject inside this Array
	 * @param messagename
	 * @param value
	 */
	public void call(String messagename, Atom[] values){
		Iterator<JitterObject> i = array.values().iterator();
		while(i.hasNext()){
			i.next().call(messagename, values);
		}
	}

	public void call(String messagename, float f) {
		Iterator<JitterObject> i = array.values().iterator();
		while(i.hasNext()){
			i.next().call(messagename, f);
		}
	}

}
