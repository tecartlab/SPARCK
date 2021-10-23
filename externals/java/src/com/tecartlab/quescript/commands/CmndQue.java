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

import org.w3c.dom.Node;

import com.tecartlab.quescript.expression.*;
import com.tecartlab.quescript.messages.*;
import com.tecartlab.utils.Debug;

public class CmndQue extends Cmnd{
	public static String NODE_NAME = "que";

	private static String ATTR_NAME = "name";
	private static String ATTR_LOOP = "loop";

	private static String ATTR_LOOP_VAL_NO = "no";

	CMsgShuttle executionShuttle;

	boolean isLooping = false;

	public boolean isPlaying = false;

	RunTimeEnvironment prt;

	CMsgTime pauseTime;

	protected int scriptLineSize;
	protected int waitLineNumber;
	protected String waitLineMsg = "";

	protected CmndQue(Cmnd _parentNode){
		super(_parentNode);
		super.setCmndName(NODE_NAME);
  	}

	public void build(Node _xmlNode) throws ScriptMsgException{
		super.build(_xmlNode);

		isLooping = (this.getAttributeValue(ATTR_LOOP).equals(ATTR_LOOP_VAL_NO))?false: true;

		queName = getAttributeValue(ATTR_NAME);

		executionShuttle = new CMsgShuttle();

		scriptLineSize = getQueLineSize();
	}

	/**
	 * takes the global Expression-runtime-environment and modifies its own
	 * local Expression-runtime-environment
	 */
	public void setup(RunTimeEnvironment rt)throws ScriptMsgException{
		prt = new RunTimeEnvironment(rt);
		if(debugMode)
			Debug.verbose("QueScript - NodeFactory", "... created Que:" + queName + " at " + lineNumber);

		prt.addLocalVariable("$TIMER", 0);
		for(Cmnd child: this.getChildren()){
			child.setup(prt);
		}
	}

	/**
	 * is called by the scripter to start the que
	 * @param debugMode
	 */
	public void play(boolean debugMode){
//		Debugger.verbose("Script-Command Que", "beeing executed: " + this.queName);
		// if the que is still running, it first has to be stopped
		if(executionShuttle.isInExecution() || executionShuttle.isInShutDownMode()){
//			Debugger.verbose("Script-Command Que", "... stopped: " + this.queName);
			executionShuttle.stop();
			bang(executionShuttle);
		}

		//first we want

		// and now it can be restarted
		executionShuttle.execute();
//		Debugger.verbose("Script-Command Que", "...executed: " + this.queName);

		executionShuttle.setDebugInfo(debugMode);

		// pass the info of how many lines are in this que
		getOutput().outputInfoMsg(QueMsgFactory.getMsg("quename").add(queName).done());

		// pass the info of how many lines are in this que
		getOutput().outputInfoMsg(QueMsgFactory.getMsg("script").add("size").add(scriptLineSize).done());

		isPlaying = true;
	}

	public void resume(){
		if(!isPlaying && !executionShuttle.isOff()){
			executionShuttle.frameBang(prt);

			// calculate the time passed during the pause
			long resumeTime = executionShuttle.getFrameTime().getTotalMillis() - pauseTime.getTotalMillis();
			// tell all child nodes about this
			resume(resumeTime);
			// set the timer accordingly
			executionShuttle.addToTimer(resumeTime);

			// keep on playing
			isPlaying = true;
		}
	}

	public void pause(){
		if(isPlaying && !executionShuttle.isOff()){
			isPlaying = false;
			pauseTime = executionShuttle.getFrameTime();
		}
	}

	/**
	 * is called by the scripter when a new que was selected
	 */
	public void shutDown(){
		if(executionShuttle.isInExecution()){
			if(executionShuttle.isDebugging())
				Debug.verbose("QueScript", "...shuting down: " + this.queName);
			executionShuttle.shutDown();

			//TODO on execution stop the waitlock needs to be removed and should not
			// be able to be set again until it is executed again..
		}
	}

	/**
	 * Fullstop of the execution
	 */
	public void stop(){
		executionShuttle.stop();
		bang(executionShuttle);
		executionShuttle.off();
		isPlaying = false;
		if(executionShuttle.isDebugging())
			Debug.verbose("QueScript", "stop: " + this.queName);
	}

	/**
	 * is called by the scripter on every frame
	 * @param _triggerQueue
	 */
	public void bang(ArrayList<CMsgTrigger> _triggerQueue){
		if(isPlaying){
			if(executionShuttle.isInExecution()){
				// add first all trigger messages into the container
				for(CMsgTrigger trgger: _triggerQueue)
					executionShuttle.addMessage(trgger);

				executionShuttle.frameBang(prt);
				bang(executionShuttle);
				// if the que is over and no looping is set, shutdown this que
				if(!executionShuttle.isWaitLocked() && !isLooping){
					executionShuttle.shutDown();
					if(executionShuttle.isDebugging())
						Debug.verbose("QueScript", "... shuting down: " + this.queName);
				}
			}  else if(executionShuttle.isInShutDownMode()){
				// the create the frame Timer
				executionShuttle.frameBang(prt);
				bang(executionShuttle);
				if(!executionShuttle.hasNodesInShutDown()){
					if(executionShuttle.isDebugging())
						Debug.verbose("QueScript", "stop: " + this.queName);
					executionShuttle.off();
					isPlaying = false;
				}
			}
		}
	}

	public void clear(){
		executionShuttle.clearMessages();

		for(Cmnd child: getChildren()){
			child.clear();
		}
	}

	/**
	 * creates a variable inside the que-scope
	 * @param name
	 * @param values
	 */
	protected void setQueVar(String name, ArrayList<ExpressionNode> values){
		// we actually dont want quevar, because they set the <var>-defined variables,
		// and they are reset to their initial expression every time the que restarts.
		// this means, no variable ever set through this channel will be accessible
	}

	protected void setWait(int waitLineNumber, String waitLineMsg){
		this.waitLineNumber = waitLineNumber;
		this.waitLineMsg = waitLineMsg;
	}

	private int getQueLineSize(){
		Cmnd lastChild = this.getChildren().get(getChildren().size()-1);
		while(lastChild.getChildren().size() > 0){
			lastChild = lastChild.getChildren().get(lastChild.getChildren().size()-1);
		}
		return lastChild.lineNumber - lineNumber;
	}

	/**
	 * sends the bang message through the tree
	 */
	public void bang(CMsgShuttle _msg) {
		for(Cmnd child: super.getChildren()){
			child.bang(_msg);
		}
		_msg.clearMessages();
	}

	public void lockLessBang(CMsgShuttle _msg){;}

	public String getQueName(){
		return this.getAttributeValue(ATTR_NAME);
	}

	@Override
	public void resume(long _timePassed) {
		for(Cmnd child: super.getChildren()){
			child.resume(_timePassed);
		}
	}

}
