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

import com.tecartlab.quescript.expression.Expression;
import com.tecartlab.quescript.expression.ExpressionNode;
import com.tecartlab.quescript.expression.RunTimeEnvironment;
import com.tecartlab.quescript.expression.Expression.ExpressionException;
import com.tecartlab.quescript.messages.CMsgAnim;
import com.tecartlab.quescript.messages.CMsgShuttle;
import com.tecartlab.quescript.messages.ScriptMsgException;
import com.tecartlab.utils.Debug;

public class CmndWhile extends Cmnd {
	protected static String NODE_NAME = "while";

	private static String ATTR_CONDITION = "condition";
	private static String ATTR_INIT = "init";
	private static String ATTR_NEXT = "next";
	private static String ATTR_NAME = "name";

	private ExpressionNode whileCondition = null;
	private ExpressionNode initExpression = null;
	private ExpressionNode nextExpression = null;

	private boolean running = false;

	private String name = null;

	public CmndWhile(Cmnd _parentNode){
		super(_parentNode);
		super.setCmndName(NODE_NAME);
	}

	public void build(Node _xmlNode) throws ScriptMsgException{
		super.build(_xmlNode);

		//if there is a nested <anim> node inside this if, it checks if there is another <anim> node
		// further down the tree towards the root
		for(Cmnd child: this.getChildren()){
			if(child instanceof CmndAnim ||child instanceof CmndWhile){
				Cmnd parnt = this.parentNode;
				while(!(parnt instanceof CmndQue)){
					if(parnt instanceof CmndAnim ||parnt instanceof CmndWhile)
						throw new ScriptMsgException("Command <while>: Nesting of <anim> and <while> nodes are prohibited");
					parnt = parnt.parentNode;
				}
			}
		}
	}

	/**
	 * Parse the Expressions with the RuntimeEnvironement
	 */
	public void setup(RunTimeEnvironment rt)throws ScriptMsgException{
		RunTimeEnvironment prt = new RunTimeEnvironment(rt);

		try {
			if(getAttributeValue(ATTR_INIT) != null){
				initExpression = new Expression(getAttributeValue(ATTR_INIT), "{", "}").setInfo(" at line(" + lineNumber + ")").parse(prt);
			}
		} catch (ExpressionException e) {
			throw new ScriptMsgException("Command <while>: Attribute <init>: " + e.getMessage());
		}

		try {
			whileCondition = new Expression(getAttributeValue(ATTR_CONDITION), "{", "}").setInfo(" at line(" + lineNumber + ")").parse(prt);
		} catch (ExpressionException e) {
			throw new ScriptMsgException("Command <while>: Attribute <condition>: " + e.getMessage());
		}

		try {
			if(getAttributeValue(ATTR_NEXT) != null){
				nextExpression = new Expression(getAttributeValue(ATTR_NEXT), "{", "}").setInfo(" at line(" + lineNumber + ")").parse(prt);
			}
		} catch (ExpressionException e) {
			throw new ScriptMsgException("Command <while>: Attribute <next>: " + e.getMessage());
		}

		if(getAttributeValue(ATTR_NAME) != null){
			name = getAttributeValue(ATTR_NAME);
		}

		if(debugMode)
			Debug.verbose("QueScript - NodeFactory", "que("+parentNode.getQueName()+") "+new String(new char[getLevel()]).replace('\0', '_')+" created while Cmnd: " + getAttributeValue(ATTR_CONDITION));

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
		if(_msg.isInStopMode())
			running = false;
		if(_msg.hasFadeMessage(name))
			running = false;
		if(!_msg.isWaitLocked() || running){
			try {
				if(!running && initExpression != null){
					// the <while> loop starts here. should be only called once
					initExpression.eval();
				}
				whileCondition.eval();
				if(whileCondition.getNumberValue() >= 1){
					for(Cmnd child : getChildren()){
						child.lockLessBang(_msg);
					}
					// if there is a step expression,
					// it will be executed after all the <while> children
					if(nextExpression != null)
						nextExpression.eval();
					running = true;
				} else {
					running = false;
					// if a name is set, <while> will send an anim message once its looping has finished
					if(name != null)
						_msg.addMessage(new CMsgAnim(name));
				}
			} catch (ExpressionException e) {
				Debug.error("Script - Command <while>", "while expression: " + e.getMessage());
			}
		}
	}

	public void lockLessBang(CMsgShuttle _msg){;}

	@Override
	public void resume(long _timePassed) {
	}

}
