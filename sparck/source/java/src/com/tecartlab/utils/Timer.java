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

package com.tecartlab.utils;

public class Timer {
	long time;

	public Timer(){

	}

	public void start(){
		time = System.currentTimeMillis();
	}

	public String estimation(int percent){
		long diff = System.currentTimeMillis() - time;
		return getTimeString(diff / percent * (100 - percent));
	}

	public String stop(){
		long diff = System.currentTimeMillis() - time;
		return getTimeString(diff);
	}

	private String getTimeString(long _time){
		StringBuffer timebandit = new StringBuffer();
		timebandit.insert(0, _time%1000 + "ms");
		_time /= 1000;
		if(_time >= 1)
			timebandit.insert(0, _time%60 + "s:");
		_time /= 60;
		if(_time >= 1)
			timebandit.insert(0, _time%60 + "m:");
		_time /= 60;
		if(_time >= 1)
			timebandit.insert(0, _time%24 + "h:");
		return timebandit.toString();
	}

}
