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

import com.tecartlab.utils.Debug;

public class CMsgFade implements CMsgInterface{
	String rampName = null;
	CMsgTime time = null;

	boolean tagForDeletion = false;

	public CMsgFade(String _rampName, CMsgTime _timeString){
		rampName = _rampName;
		if(_timeString != null){
			try {
				time = _timeString;
				// a fade message of 0 time is causing troubles:
				// better set it to very short:
				if(time.getTotalMillis() == 0){
					time = new CMsgTime("1ms");
				}
			} catch (ScriptMsgException e) {
				Debug.error("Script Parsing: Command Fade", e.getMessage());
			}
		}
	}

	public void tagForDeletion(){
		tagForDeletion = true;
	}

	public boolean isToBeDeleted(){
		return tagForDeletion;
	}

	public boolean isTrigger(String _name) {
		return false;
	}

	public boolean isFade(String _name) {
		return (rampName == null || rampName.equals(_name)?true:false);
	}

	public boolean isFade() {
		return true;
	}

	public boolean isFadedOut(String _name) {
		return false;
	}

	public boolean isAnim(String _name) {
		return false;
	}

	public boolean hasFadeTime(){
		return (time != null)?true: false;
	}

	public CMsgTime getFadeTime(){
		return time;
	}

	public boolean isStop() {
		return false;
	}
}
