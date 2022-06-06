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

public class CmndElse extends Cmnd {
	protected static String NODE_NAME = "else";

	public CmndElse(Cmnd _parentNode){
		super(_parentNode);
		super.setCmndName(NODE_NAME);
	}

	public void build(Node _xmlNode) throws ScriptMsgException{
		super.build(_xmlNode);

		//if there is a nested <anim> node inside this if, it checks if there is another <anim> node
		// further down the tree towards the root
		for(Cmnd child: this.getChildren()){
			if(child instanceof CmndAnim){
				Cmnd parnt = this.parentNode;
				while(!(parnt instanceof CmndQue)){
					if(parnt instanceof CmndQue)
						throw new ScriptMsgException("Command <else>: Multiple Nesting of <anim> and <if> nodes are prohibited");
					parnt = parnt.parentNode;
				}
			}
		}
	}


	/**
	 * Parse the Expressions with the RuntimeEnvironement
	 */
	public void setup(RunTimeEnvironment rt)throws ScriptMsgException{
		if(debugMode)
			Debug.verbose("QueScript - NodeFactory", "que("+parentNode.getQueName()+") "+new String(new char[getLevel()]).replace('\0', '_')+" created "+cmdName+"-Comnd");
		// Make sure the que- and local- variables are created before the children are parsed
		for(Cmnd child: this.getChildren()){
			child.setup(rt);
		}
	}

	@Override
	public void store(Node _parentElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bang(CMsgShuttle _msg) {
		for(Cmnd child : getChildren()){
			child.bang(_msg);
		}
	}

	public void lockLessBang(CMsgShuttle _msg) {
		for(Cmnd child : getChildren()){
			child.lockLessBang(_msg);
		}
	}

	@Override
	public void resume(long _timePassed) {
	}

}
