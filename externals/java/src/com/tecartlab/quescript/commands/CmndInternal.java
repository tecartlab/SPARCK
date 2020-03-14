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
import com.tecartlab.quescript.messages.ScriptMsgException;
import com.tecartlab.utils.Debug;

public class CmndInternal extends Cmnd {
	public static String NODE_NAME_STOP = "stop";
	public static String NODE_NAME_RESUME = "resume";
	public static String NODE_NAME_PAUSE = "pause";
	public static String NODE_NAME_PLAY = "play";
	public static String NODE_NAME_SHUTDOWN = "shutdown";

	private static String ATTR_NAME = "name";

	String name;

	public CmndInternal(Cmnd _parentNode, String _cmdName){
		super(_parentNode);
		super.setCmndName(_cmdName);
	}

	public void build(Node _xmlNode) throws ScriptMsgException{
		super.build(_xmlNode);

		// use the attribute or the first value of the key
		if(this.hasAttributeValue(ATTR_NAME))
			name = getAttributeValue(ATTR_NAME);

	}

	/**
	 * Parse the Expressions with the RuntimeEnvironement
	 */
	public void setup(RunTimeEnvironment rt)throws ScriptMsgException{
		if(debugMode)
			Debug.verbose("QueScript - NodeFactory", "que("+parentNode.getQueName()+") "+new String(new char[getLevel()]).replace('\0', '_')+" created internal "+ cmdName +"-Comnd for: " + name);
	}

	/**
	 * checks if the attribute name is set
	 * @return
	 */
	public boolean hasName(){
		return (name == null)?false:true;
	}

	/**
	 * get the value of attribute name
	 * @return
	 */
	public String getName(){
		return name;
	}

	@Override
	public void store(Node _parentElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bang(CMsgShuttle _msg) {
		if(!_msg.isWaitLocked()){
			lockLessBang(_msg);
		}
	}

	public void lockLessBang(CMsgShuttle _msg){
		if(!_msg.isInStopMode()){
			this.getOutput().selfCommand(new String[]{cmdName, parentNode.getQueName(), name});
			if(debugMode)
				Debug.verbose("QueScript "+cmdName+"-Command que:(" + parentNode.getQueName() + ")", "sent "+cmdName+"-message to "+cmdName+": " + name);
		}
	}

	@Override
	public void resume(long _timePassed) {
		// TODO Auto-generated method stub

	}

}
