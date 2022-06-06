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

package com.tecartlab.quescript.commands;

import org.w3c.dom.Node;

import com.tecartlab.quescript.expression.RunTimeEnvironment;
import com.tecartlab.quescript.messages.CMsgShuttle;
import com.tecartlab.quescript.messages.CMsgTime;
import com.tecartlab.quescript.messages.ScriptMsgException;
import com.tecartlab.utils.Debug;

public class CmndKeys extends Cmnd {
	public static String NODE_NAME = "keys";

	private static String ATTR_TIMING = "timing";

	private double[] relKeyTimes;

	private boolean timingIsAbsolute;

	public CmndKeys(Cmnd _parentNode){
		super(_parentNode);
		super.setCmndName(NODE_NAME);
	}

	public void build(Node _xmlNode) throws ScriptMsgException{
		super.build(_xmlNode);
	}

	/**
	 * Parse the Expressions with the RuntimeEnvironement
	 */
	public void setup(RunTimeEnvironment rt)throws ScriptMsgException{
		if(this.hasAttributeValue(ATTR_TIMING))
			timingIsAbsolute = (getAttributeValue(ATTR_TIMING).equals("abs"))? true: false;
		else
			timingIsAbsolute = true;

		parse(super.content);

		if(debugMode)
			Debug.verbose("QueScript - NodeFactory", "que("+parentNode.getQueName()+") "+new String(new char[getLevel()]).replace('\0', '_')+" created keys Comnd");
	}

	/**
	 * get the relative key times. if the key times are set in absolute values, they are
	 * converted into relative key times.
	 *
	 * @param _duration the duration of the ramp in milliseconds
	 * @return the relative key times
	 */
	public double[] getKeyTimes(long _duration){
		if(timingIsAbsolute){
			double[] ret = new double[relKeyTimes.length];
			for(int i = 1; i < (ret.length - 1); i++){
				ret[i] = relKeyTimes[i] / _duration;
			}
			ret[0] = relKeyTimes[0];
			ret[ret.length - 1] = relKeyTimes[ret.length - 1];
			return ret;
		}
		return relKeyTimes;
	}

	/**
	 * parse the values and stores them
	 * @param _content
	 */
	private void parse(String _content){
		String[] segmts = _content.split("\\s+");
		relKeyTimes = new double[segmts.length + 2];
		for(int i = 0; i < segmts.length; i++){
			try{
				if(timingIsAbsolute){
					relKeyTimes[i + 1] = (float)(new CMsgTime(segmts[i])).getTotalMillis();
				} else {
					relKeyTimes[i + 1] = Float.parseFloat(segmts[i]);
				}
			} catch (NumberFormatException e){;
			} catch (ScriptMsgException e){;}

		}
		// make sure the last keyTime is a perfect 1.0:
		relKeyTimes[0] = 0.0f;
		relKeyTimes[relKeyTimes.length - 1] = 1.0f;
	}

	public void bang(CMsgShuttle _msg) {;}

	public void lockLessBang(CMsgShuttle _msg){;}

	public void resume(long _timePassed) {;}

}
