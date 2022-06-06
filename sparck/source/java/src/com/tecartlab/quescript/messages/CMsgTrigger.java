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

package com.tecartlab.quescript.messages;

public class CMsgTrigger implements CMsgInterface{
	String trigger;
	String[] triggerVals;
	String[] triggerVariables;

	boolean isArmed = false;

	public CMsgTrigger(String _trigger, String[] _values){
		trigger = _trigger;
		triggerVals = _values;
	}

	public CMsgTrigger(String _trigger){
		trigger = _trigger;
		triggerVals = null;
	}

	public void setArmed(){
		isArmed = true;
	}

	public boolean isArmed(){
		return isArmed;
	}

	public void setTriggerVariables(String[] _triggerVariables){
		triggerVariables = _triggerVariables;
	}

	/**
	 * test if the passed string matches the trigger
	 */
	public boolean isTrigger(String _name) {
		// first split the string into segments
		String[] segmts = _name.split("\\s+");
		if(segmts.length == 1){
			return (trigger.equals(segmts[0])?true:false);
		} else if(segmts.length > 1 && triggerVals != null){
			int isMatch = (trigger.equals(segmts[0]))? 1: 0;
			for(int i = 1; i < segmts.length; i++){
				if(triggerVals.length >= i){
					isMatch *= (triggerVals[i - 1].equals(segmts[i]))? 1: 0;
				}
			}
			return (isMatch == 1)? true: false;
		}
		return false;
	}

	public boolean isFade(String _name) {
		return false;
	}

	public boolean isFadedOut(String _name) {
		return false;
	}

	public boolean isAnim(String _name) {
		return false;
	}

	public boolean isStop() {
		return false;
	}

	public boolean isFade() {
		return false;
	}
}
