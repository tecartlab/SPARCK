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

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Node;

import com.tecartlab.quescript.expression.ExpressionNode;
import com.tecartlab.quescript.expression.RunTimeEnvironment;
import com.tecartlab.quescript.expression.Expression.ExpressionException;
import com.tecartlab.quescript.messages.CMsgAnim;
import com.tecartlab.quescript.messages.CMsgFade;
import com.tecartlab.quescript.messages.CMsgFadedOut;
import com.tecartlab.quescript.messages.CMsgShuttle;
import com.tecartlab.quescript.messages.CMsgTime;
import com.tecartlab.quescript.messages.ScriptMsgException;
import com.tecartlab.utils.Debug;

public class CmndAnim extends Cmnd {
	public static String NODE_NAME = "anim";

	private static String ATTR_LOOP = "loop";
	private static String ATTR_NAME = "name";
	private static String ATTR_DURATION = "duration";
	private static String ATTR_FADEOUT = "fadeout";

	private static String ATTR_LOOP_VAL_NO = "no";
	private static String ATTR_LOOP_VAL_NORMAL = "normal";
	private static String ATTR_LOOP_VAL_PALINDROME = "palindrome";

	private final static int LOOP_MODE_NONE			= 1;
	private final static int LOOP_MODE_NORMAL 		= 2;
	private final static int LOOP_MODE_PALINDROME 	= 3;

	private final static int EXECUTE_OFF 		= 0;
	private final static int EXECUTE 			= LOOP_MODE_NONE;
	private final static int EXECUTE_LOOP 		= LOOP_MODE_NORMAL;
	private final static int EXECUTE_PALINDROME = LOOP_MODE_PALINDROME;
	private final static int EXECUTE_PAUSE 		= 4;
	private final static int EXECUTE_FADEOUT 	= 5;

	private int loop;
	private String name;
	private CMsgTime durationTime;
	private CMsgTime fadeoutTime;
	private CMsgTime fadeoutTime_original;

	private CMsgTime executionTime;

	int runMode = EXECUTE_OFF;

	boolean palindromDirection = false;

	ArrayList<CmndTrack> valueInterolators;

	double[] relKeyTiming = null;

	RunTimeEnvironment prt;

	public CmndAnim(Cmnd _parentNode){
		super(_parentNode);
		super.setCmndName(NODE_NAME);
	}

	public void build(Node _xmlNode) throws ScriptMsgException{
		super.build(_xmlNode);

		if(this.getAttributeValue(ATTR_LOOP).equals(ATTR_LOOP_VAL_NO))
			loop = LOOP_MODE_NONE;
		else if(this.getAttributeValue(ATTR_LOOP).equals(ATTR_LOOP_VAL_NORMAL))
			loop = LOOP_MODE_NORMAL;
		else if(this.getAttributeValue(ATTR_LOOP).equals(ATTR_LOOP_VAL_PALINDROME))
			loop = LOOP_MODE_PALINDROME;

		name = getAttributeValue(ATTR_NAME);
	}

	/**
	 * Parse the Expressions with the RuntimeEnvironement
	 */
	public void setup(RunTimeEnvironment rt)throws ScriptMsgException{
    	prt = new RunTimeEnvironment(rt);
		if(debugMode)
			Debug.verbose("QueScript - NodeFactory", "que("+parentNode.getQueName()+") "+new String(new char[getLevel()]).replace('\0', '_')+" created Anim Comnd: name='" + name +"'");

		// first the attribute values
		String expr = "notset";
		try {
			expr = getAttributeValue(ATTR_DURATION);
			durationTime = getAttributeTime(expr, " at line(" + lineNumber + ")",prt);
			if(hasAttributeValue(ATTR_FADEOUT)){
				expr = getAttributeValue(ATTR_FADEOUT);
				fadeoutTime = getAttributeTime(expr, " at line(" + lineNumber + ")",prt);
				// a fadeout time of zero is causing troubles with the algorithm:
				// better set it to very short.
				if(fadeoutTime.getTotalMillis()==0){
					fadeoutTime = new CMsgTime(new ExpressionNode(1), 0);
				}
				fadeoutTime_original = fadeoutTime.clone();
			}
		} catch (ScriptMsgException e1) {
			throw new ScriptMsgException("<que name=\""+parentNode.getQueName()+"\"> <anim name=\""+name+"\" ("+expr+")>: "+e1.getMessage());
		} catch (ExpressionException e1) {
			throw new ScriptMsgException("<que name=\""+parentNode.getQueName()+"\"> <anim name=\""+name+"\" ("+expr+")>: "+e1.getMessage());
		}


		// now we can get the keyTimes
		for(Cmnd child: this.getChildren()){
			if(child.isCmndName(CmndKeys.NODE_NAME)){
				// if there is a keys command, get the keyTimes.
				CmndKeys snd = (CmndKeys)child;
				relKeyTiming = snd.getKeyTimes(durationTime.getTotalMillis());
			}
		}

		valueInterolators = new ArrayList<CmndTrack>();

		for(Cmnd child: this.getChildren()){
			if(child.isCmndName(CmndTrack.NODE_NAME)){
				CmndTrack flt = (CmndTrack)child;
				prt.setVariable(name+"."+flt.trackName, flt.getValueObject(), 1);
				prt.addLocalVariable(flt.trackName, flt.getValueObject());
				valueInterolators.add(flt);
				if(relKeyTiming != null)
					flt.setKeyTimes(relKeyTiming);
			}
		}


		// Make sure the que- and local- variables are created before the children are parsed
		for(Cmnd child: this.getChildren()){
			child.setup(prt);
		}

	}

	@Override
	public void store(Node _parentElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bang(CMsgShuttle _msg) {
		if(_msg.isInStopMode() &&
				(runMode != EXECUTE_OFF)){
			// if there is a stop message (called by the stop command or the interruption
			// of the que),

			// if a fadeout is set:
			if(fadeoutTime != null){
				// and there is a fadeout time set, then it attempts to reach the fadeout target

				// it is a fullstop message, then it will try to reach
				// the fadeout target immediately
				if(_msg.isInFullStopMode()){
					// make sure the execution is running, but only once so it is set to the
					// defined fadeout values
					runMode = EXECUTE_FADEOUT;
					setFade2Mode(runMode);
					palindromDirection = false;

					executionTime = _msg.getFrameTime().subtract(fadeoutTime);
//					Debugger.info("Script - Command <ramp> fullstop", "set execution time("+executionTime.print()+")");
				} else if(_msg.isInShutDownMode() && runMode != EXECUTE_FADEOUT){
					// or its a shutdown message and it allows to reach with the default fadeout time
					runMode = EXECUTE_FADEOUT;
					setFade2Mode(runMode);
					palindromDirection = false;

					executionTime = _msg.getFrameTime().subtract(_msg.deltaFrameTime);
//					Debugger.info("Script - Command <ramp> shutdown", "set execution time("+executionTime.print()+")");
				}


			} else {
				// otherwise it stops the ramp if it is in LOOP and still executing
				if(runMode == EXECUTE_LOOP || runMode == EXECUTE_PALINDROME || _msg.isInFullStopMode()){
					palindromDirection = false;
					runMode = EXECUTE_OFF;
				}
				// this also means that it will allow to finish the ramp
			}

		} else if(_msg.hasFadeMessage(name) &&
				(runMode != EXECUTE_OFF && runMode != EXECUTE_FADEOUT)){
			// if there is a fade message use the fade message value
//			Debugger.verbose("Script Ramp - Command", "received fade message");
			runMode = EXECUTE_FADEOUT;
			setFade2Mode(runMode);
			palindromDirection = false;

			executionTime = _msg.getFrameTime().subtract(_msg.deltaFrameTime);
//			Debugger.info("Script - Command <ramp> fade", "set execution time("+executionTime.print()+")");

			CMsgFade fade = _msg.getFadeMessage(name);
			fade.tagForDeletion();
			if(fade.hasFadeTime())
				fadeoutTime = fade.getFadeTime();
		} else {
			// here the ramp is actually started.
			if(!_msg.isWaitLocked() &&
					(runMode == EXECUTE_OFF || runMode == EXECUTE_PAUSE)){
				runMode = loop;
				setFade2Mode(runMode);
				palindromDirection = false;

				executionTime = _msg.getFrameTime();
//				Debugger.info("Script - Command <ramp> start", "set execution time("+executionTime.print()+")");
				fadeoutTime = fadeoutTime_original;
			}
		}
		// and in here the calculations are done
		if(runMode != EXECUTE_OFF && runMode != EXECUTE_PAUSE){
			long passedTime = _msg.getFrameTime().subtract(executionTime).getTotalMillis();
			float normalizedTime;
			if(runMode == EXECUTE_FADEOUT){
				normalizedTime = (float)passedTime / (float)fadeoutTime.getTotalMillis();
//				Debugger.info("Script - Command <ramp> execute", "passedTime = "+passedTime+" | normalizedTime = " + normalizedTime);
			}else{
				normalizedTime = (float)passedTime / (float)durationTime.getTotalMillis();
			}
			// now make sure it will be exactly 1 if time passes the duration
			normalizedTime = (normalizedTime > 1.0)? 1.0f: normalizedTime;
			// and then turn it depending of the palindrome direction

			execute(normalizedTime, _msg);

			// in here the decision is made what to do when the target time has been reached
			if(normalizedTime == 1.0f){
				switch(runMode){
				case EXECUTE_FADEOUT:
					runMode = EXECUTE_OFF;
					_msg.addMessage(new CMsgFadedOut(name));
					break;
				case EXECUTE:
					runMode = (fadeoutTime != null)? EXECUTE_PAUSE: EXECUTE_OFF;
					_msg.addMessage(new CMsgAnim(name));
					break;
				case EXECUTE_LOOP:
					executionTime = _msg.getFrameTime();
					break;
				case EXECUTE_PALINDROME:
					executionTime = _msg.getFrameTime();
					palindromDirection = !palindromDirection;
					break;
				}
			}
			// and tell the que command that it is still running
			_msg.addNodesStillRunning();
		}
	}

	public void lockLessBang(CMsgShuttle _msg){;}

	private void execute(float _normalizedTime, CMsgShuttle _msg){
		_normalizedTime = (palindromDirection)? 1.0f - _normalizedTime: _normalizedTime;

//		Debugger.verbose("Script - Command Ramp", "normalized time: " + _normalizedTime);

		Iterator<CmndTrack> e = valueInterolators.iterator();
		while(e.hasNext())
			e.next().calculate(_normalizedTime);

		for(Cmnd snd: getChildren())
			snd.lockLessBang(_msg);
	}

	private void setFade2Mode(int _mode){
		Iterator<CmndTrack> e = valueInterolators.iterator();
		while(e.hasNext())
			e.next().fadeToMode((_mode == EXECUTE_FADEOUT)? true: false);
	}

	public void resume(long _timePassed) {
		if(executionTime != null){
			executionTime.add(_timePassed);
		}
	}

	public void clear(){
		if(valueInterolators != null)
			valueInterolators.clear();
		for(Cmnd child: getChildren()){
			child.clear();
		}
	}

}
