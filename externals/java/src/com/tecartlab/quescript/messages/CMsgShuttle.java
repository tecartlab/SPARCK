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

import java.util.ArrayList;
import java.util.Calendar;

import com.tecartlab.quescript.commands.Cmnd;
import com.tecartlab.quescript.expression.RunTimeEnvironment;

public class CMsgShuttle {
	private final static int EXECUTE_OFF 		= 0;
	private final static int EXECUTE 			= 1;
	private final static int EXECUTE_SHUTDOWN	= 2;
	private final static int EXECUTE_STOP		= 3;

	long _currentMillis = 0;

	CMsgTime executionTime;
	CMsgTime timerTime;
	CMsgTime frameTime;

	boolean hasTimer = false;

	public long deltaFrameTime;

	Cmnd lockingObject = null;

	ArrayList<CMsgInterface> msgs;

	int nodesInShutdown;

	int executionMode = EXECUTE_OFF;

	boolean debugMode = false;

	public CMsgShuttle(){
		msgs = new ArrayList<CMsgInterface>();
	}

	/**
	 * called by que manager
	 */
	public void execute(){
		executionTime = new CMsgTime(Calendar.getInstance());
		frameTime = executionTime.clone();
		msgs.clear();
		nodesInShutdown = 0;
		lockingObject = null;
		executionMode = EXECUTE;
	}

	/**
	 * called by que manager
	 */
	public void shutDown(){
		lockingObject = null;
		executionMode = EXECUTE_SHUTDOWN;
	}

	/**
	 * called by que manager
	 */
	public void stop(){
		lockingObject = null;
		executionMode = EXECUTE_STOP;
	}

	/**
	 * called by que manager
	 */
	public void off(){
		lockingObject = null;
		executionMode = EXECUTE_OFF;
	}

	/**
	 * called by que manager when new frame is started
	 */
	public void frameBang(RunTimeEnvironment rt){
		Calendar md = Calendar.getInstance();
		CMsgTime newFrameTime =  new CMsgTime(md);
		deltaFrameTime = newFrameTime.getTotalMillis() - frameTime.getTotalMillis();
		frameTime = newFrameTime;
		nodesInShutdown = 0;
		if(hasTimer()){
			rt.addLocalVariable("$TIMER", frameTime.getTotalMillis() - timerTime.getTotalMillis());
		}
	}

	/**
	 * set to debugging info
	 */
	public void setDebugInfo(boolean _debug){
		debugMode = _debug;
	}

	public boolean isDebugging(){
		return debugMode;
	}

	/**
	 * If a node received a stop message and is shutting down, it needs to keep on telling this
	 * here.
	 */
	public void addNodesStillRunning(){
		if(executionMode == EXECUTE_SHUTDOWN)
			nodesInShutdown++;
	}

	/**
	 * the function returns something greater than zeor if there are still nodes shutting down
	 * @return
	 */
	public boolean hasNodesInShutDown(){
		return (nodesInShutdown > 0)? true: false;
	}

	/**
	 * returns a copy of the current frame time
	 * @return
	 */
	public CMsgTime getFrameTime(){
		return frameTime.clone();
	}

	/**
	 * returns a copy of the timer time
	 * @return
	 */
	public CMsgTime getTimerTime(){
		return timerTime.clone();
	}

	/**
	 * creates the countdown time. internally it takes the frametime and adds the countdown-timer
	 * @param _countdownTimer
	 * @return
	 */
	public CMsgTime getCountDownTime(CMsgTime _countdownTimer){
		return getFrameTime().add(_countdownTimer);
	}

	/**
	 * returns true if the stepper is locked.
	 *
	 * if the execution mode is other then normal execution,
	 * this method will return true.
	 *
	 * @return
	 */
	public boolean isWaitLocked(){
		if(executionMode == EXECUTE)
			return (lockingObject != null);
		return true;
	}

	/**
	 * checks if there is a lock by this object.
	 *
	 * if the execution mode is other then normal execution,
	 * this method will return false.
	 *
	 * @param _cmnd
	 * @return true if this is the case
	 */
	public boolean isWaitLockedBy(Cmnd _cmnd){
		if(executionMode == EXECUTE)
			return (lockingObject == _cmnd);
		return false;
	}

	/**
	 * locks the waitlock with the passed on object.
	 * @param _cmnd
	 * @return true if it was successfull
	 */
	public boolean lockWaitLock(Cmnd _cmnd){
		if(executionMode == EXECUTE && lockingObject == null){
			lockingObject = _cmnd;
			return true;
		}
		return false;
	}

	/**
	 * frees the wait lock. this can only be done by the object that set the lock
	 * @param _cmnd
	 */
	public void freeWaitLock(Cmnd _cmnd){
		if(lockingObject == _cmnd)
			lockingObject = null;
	}

	public CMsgTime getExecutionTime(){
		return executionTime.clone();
	}

	/**
	 * This message should only be called the que manager
	 * @param _msg
	 */
	public void addMessage(CMsgInterface _msg){
		msgs.add(_msg);
	}

	/**
	 * This message should only be called by the que manager
	 */
	public void clearMessages(){
		ArrayList<CMsgInterface> newMsgs = new ArrayList<CMsgInterface>();

		for(CMsgInterface msg: msgs){
			// clears all messages tagged for deletion
			if(msg.isFade()){
				if(!((CMsgFade)msg).isToBeDeleted()){
					newMsgs.add(msg);
				}
			}
		}
		msgs.clear();
		msgs = newMsgs;
	}

	public CMsgTrigger getTriggerMessage(String _trigger){
		for(CMsgInterface msg: msgs){
			if(msg.isTrigger(_trigger))
				return (CMsgTrigger)msg;
		}
		return null;
	}

	public boolean isOff(){
		return (executionMode == EXECUTE_OFF)?true: false;
	}

	public boolean isInExecution(){
		return (executionMode == EXECUTE)?true: false;
	}

	public boolean isInFullStopMode(){
		return (executionMode == EXECUTE_STOP)?true: false;
	}

	public boolean isInShutDownMode(){
		return (executionMode == EXECUTE_SHUTDOWN)?true: false;
	}

	public boolean isInStopMode(){
		return isInFullStopMode() || isInShutDownMode();
	}

	public boolean hasTriggerMessage(String _trigger){
		for(CMsgInterface msg: msgs){
			if(msg.isTrigger(_trigger))
				return true;
		}
		return false;
	}

	public void removeTriggerMessage(String _trigger){
		for(CMsgInterface msg: msgs){
			if(msg.isTrigger(_trigger)){
				msgs.remove(msg);
				return;
			}
		}
	}

	public CMsgFade getFadeMessage(String _fade){
		for(CMsgInterface msg: msgs){
			if(msg.isFade(_fade))
				return (CMsgFade)msg;
		}
		return null;
	}

	public boolean hasFadeMessage(String _fade){
		for(CMsgInterface msg: msgs){
			if(msg.isFade(_fade))
				return true;
		}
		return false;
	}

	public CMsgAnim getRampMessage(String _ramp){
		for(CMsgInterface msg: msgs){
			if(msg.isAnim(_ramp))
				return (CMsgAnim)msg;
		}
		return null;
	}

	public boolean hasAnimMessage(String _ramp){
		for(CMsgInterface msg: msgs){
			if(msg.isAnim(_ramp))
				return true;
		}
		return false;
	}

	public boolean hasFadedMessage(String _ramp){
		for(CMsgInterface msg: msgs){
			if(msg.isFadedOut(_ramp))
				return true;
		}
		return false;
	}

	/**
	 * Tests if a timer has been set.
	 * @return
	 */
	public boolean hasTimer(){
		return hasTimer;
	}

	/**
	 * Creates a timer. this method should only be called by CmndTimer
	 */
	public void setTimer(){
		timerTime = frameTime.clone();
		hasTimer = true;
	}

	public void addToTimer(long _resumeTime){
		if(hasTimer)
			timerTime.add(_resumeTime);
	}

	/**
	 * Tests if the relative _timer has passed the Timer.
	 * Internally it takes the timerTime and adds the _timer and compares it with the
	 * frameTime.
	 * @param _timer
	 * @return
	 */
	public boolean hasFrameTimePassedTimer(CMsgTime _timer){
		return timerTime.clone().add(_timer).isWatch(frameTime);
	}

	/**
	 * Tests if _hourglass is passed the minute finger of the current frame time
	 * @param _hourglass
	 * @return
	 */
	public boolean hasFrameTimePassedHourglass(CMsgTime _hourglass){
		return _hourglass.isHourglass(frameTime);
	}

	/**
	 * Tests if _countdown is passed the current frame time
	 * @param _watch
	 * @return
	 */
	public boolean hasFrameTimePassedCountdown(CMsgTime _countdown){
		return _countdown.isWatch(frameTime);
	}

	/**
	 * Tests if _watch is passed the current frame time
	 * @param _watch
	 * @return
	 */
	public boolean hasFrameTimePassedWatch(CMsgTime _watch){
		return _watch.isWatch(frameTime);
	}

}
