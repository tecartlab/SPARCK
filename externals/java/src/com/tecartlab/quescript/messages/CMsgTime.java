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

import java.util.Calendar;

import com.tecartlab.quescript.expression.ExpressionVar;
import com.tecartlab.quescript.expression.Expression.ExpressionException;
import com.tecartlab.utils.Debug;

public class CMsgTime {
	final static int MILS = 0;
	final static int SECS = 1;
	final static int MINS = 2;
	final static int HOURS = 3;

	private ExpressionVar mills;

	private long diffMills;

	/**
	 * Create a time object with the current time
	 */
	public CMsgTime(Calendar md){
		setMilsTime(getStringTime(md.get(Calendar.HOUR_OF_DAY), md.get(Calendar.MINUTE), md.get(Calendar.SECOND), md.get(Calendar.MILLISECOND)));
	}

	/**
	 * Creating a time with an Expression
	 * @param _ms
	 * @param _ms_hourglass
	 * @throws ScriptMsgException
	 */
	public CMsgTime(ExpressionVar mills, long diffMills) throws ScriptMsgException{
		this.mills = mills;
		this.diffMills = diffMills;
	}

	/**
	 * Creating a time with a .h:.m:.s:..ms string
	 * @param time
	 * @throws ScriptMsgException
	 */
	public CMsgTime(String time) throws ScriptMsgException{
		this.mills = new ExpressionVar(parse(time));
		this.diffMills = 0;
	}

	public long parse(String _time) throws ScriptMsgException{
		float[] time = new float[]{0, 0, 0, 0};
		String[] tim = _time.split("\\:");
		for(int i = 0; i < tim.length; i++){
			if(tim[i].endsWith("h"))
				time[HOURS] = Integer.parseInt(tim[i].substring(0, tim[i].length() - 1));
			else if(tim[i].endsWith("m"))
				time[MINS] = Integer.parseInt(tim[i].substring(0, tim[i].length() - 1));
			else if(tim[i].endsWith("ms"))
				time[MILS] = Integer.parseInt(tim[i].substring(0, tim[i].length() - 2));
			else if(tim[i].endsWith("s"))
				time[SECS] = Integer.parseInt(tim[i].substring(0, tim[i].length() - 1));
			else
				throw new ScriptMsgException("invalid time format: " + tim[i] + " inside " + _time);
		}
		if(time[HOURS] >= 24)
			throw new ScriptMsgException("invalid time format: Hours can't be greater than 23: " + time[HOURS]);
		else if(time[MINS] >=60)
			throw new ScriptMsgException("invalid time format: Minutes can't be greater than 59: " + time[MINS]);
		else if(time[MINS] >= 60)
			throw new ScriptMsgException("invalid time format: Seconds can't be greater than 59: " + time[SECS]);
		else if(time[MILS] >= 1000)
			throw new ScriptMsgException("invalid time format: Milliseconds can't be greater than 1000: " + time[SECS]);
		return getStringTime(time[HOURS], time[MINS], time[SECS], time[MILS]);
	}

	private void setMilsTime(long mils){
		mills = new ExpressionVar((double)mils);
	}

	private long getStringTime(float hours, float mins, float secs, float mils){
		return (long)(((hours * 60 + mins ) * 60 + secs ) * 1000 + mils);
	}

	private long getHourglass(){
		return getTotalMillis() % ( 60 * 60 * 1000 );
	}

	/**
	 * gets the total milliseconds of this time
	 * @return
	 */
	public long getTotalMillis(){
		try {
			mills.eval();
		} catch (ExpressionException e) {
			Debug.error("QueScript", "Expression Runtime Exception :" + e.getMessage());
		}
		if(mills.isNumber){
			return (long)mills.getNumberValue() + diffMills;
		} else {
			try {
				return parse(mills.getStringValue())  + diffMills;
			} catch (ScriptMsgException e) {
				Debug.error("QueScript", "Expression Runtime Exception :" + e.getMessage());
			}
		}
		return 0;
	}

	/**
	 * Tests if the provided _time is passed or equal the minute finger (metaphorically speaking) of this
	 * instance
	 * @param _time
	 * @return
	 */
	public boolean isHourglass(CMsgTime _time){
		return (getHourglass() <= _time.getHourglass())? true: false;
	}

	/**
	 * Tests if the provided _time is passed or equal the time of this instance
	 * @param _time
	 * @return
	 */
	public boolean isWatch(CMsgTime _time){
		return (getTotalMillis() <= _time.getTotalMillis())? true: false;
	}

	/**
	 * Adds the _time to this instance and returns it
	 * @param _time
	 * @return
	 */
	public CMsgTime add(CMsgTime _time){
		diffMills += _time.getTotalMillis();
		return this;
	}

	/**
	 * Adds the _milliseconds to this instance and returns it
	 * @param _milliseconds
	 * @return
	 */
	public CMsgTime add(long _milliseconds){
		diffMills += _milliseconds;
		return this;
	}

	/**
	 * Subtracts the _time from this instance and returns it
	 * @param _time
	 * @return
	 */
	public CMsgTime subtract(CMsgTime _time){
		diffMills -= _time.getTotalMillis();
		return this;
	}

	/**
	 * Subtracts the _milliseconds from this instance and returns it
	 * @param _milliseconds
	 * @return
	 */
	public CMsgTime subtract(long _milliseconds){
		diffMills -= _milliseconds;
		return this;
	}

	public CMsgTime clone(){
		try {
			return new CMsgTime(mills, diffMills);
		} catch (ScriptMsgException e) {
			Debug.fatal("QueScript", "Unable to clone Time: Expression Runtime Exception :" + e.getMessage());
		}
		return null;
	}

	public String print(){
		float[] time = new float[]{0, 0, 0, 0};
		long leftover = getTotalMillis();
		time[HOURS] = (float)(leftover / (60 * 60 * 1000));
		leftover = leftover - (long)(time[HOURS] * 60 * 60 * 1000);
		time[MINS] 	= (int)(leftover / (60 * 1000));
		leftover = leftover - (long)(time[MINS] * 60 * 1000);
		time[SECS] 	= (int)(leftover / 1000);
		leftover = leftover - (long)(time[SECS] * 1000);
		time[MILS] = (int)leftover;
		return "" + time[HOURS] + "h:" + time[MINS] + "m:" + time[SECS] + "s:" + time[MILS] + "ms";
	}
}
