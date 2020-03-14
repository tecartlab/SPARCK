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

package com.tecartlab.mxj.utils.pattr;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.tecartlab.utils.Debug;

public class LinkCallback {

	private Method getter;
	private Method setter;

	private Object client;

	public LinkCallback(Object _client, String _getter, String _setter) throws PattrException{
		client = _client;
		try{
			if(_getter != null)
				getter = _client.getClass().getDeclaredMethod(_getter, new Class[] {String.class});
			if(_setter != null)
				setter = _client.getClass().getDeclaredMethod(_setter, new Class[] {String.class, Float.TYPE});

			if(!getter.isAccessible())
				getter.setAccessible(true);
			if(!setter.isAccessible())
				setter.setAccessible(true);

		} catch (Exception e){
			throw new PattrException("Check if you listener method has the specifies getter ["+_getter+"] and setter ["+_setter+"] methods.");
		}
	}

	protected void set(String _address, float _value){
		if(setter != null){
			try {
				setter.invoke(client, _address, _value);
			} catch (Exception e) {
				Debug.error(getClass(), "Setter invoke failed. Address:"+_address+" | Client:" + client.getClass().getName() + " | Method:"+setter.getName());
				e.printStackTrace();
			}
		}
	}

	protected float get(String _address){
		float ret = 0.0f;
		if(getter != null){
			try {
				ret = ((Float)getter.invoke(client, _address)).floatValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

}
