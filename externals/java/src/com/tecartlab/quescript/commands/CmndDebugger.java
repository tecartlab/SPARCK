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

import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Node;

import com.tecartlab.quescript.expression.ExpressionVar;
import com.tecartlab.quescript.expression.RunTimeEnvironment;
import com.tecartlab.quescript.messages.CMsgShuttle;
import com.tecartlab.quescript.messages.ScriptMsgException;
import com.tecartlab.utils.Debug;

public class CmndDebugger extends Cmnd {
	protected static String NODE_NAME = "debugger";

	private static String ATTR_SCOPE = "scope";
	private static String ATTR_NAME = "name";

	private static String ATTR_SCOPE_SCRIPT = "script";
	private static String ATTR_SCOPE_QUE = "que";
	private static String ATTR_SCOPE_LOCAL = "local";

	RunTimeEnvironment prt;

	private int showVarDomain = 0;
	private String name = "";

	public CmndDebugger(Cmnd _parentNode){
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
		prt = rt;
		showVarDomain = 0;

		if(getAttributeValue(ATTR_SCOPE) != null){
			String scope = getAttributeValue(ATTR_SCOPE);
			if(scope.equals(ATTR_SCOPE_SCRIPT)){
				showVarDomain = 0;
			} else if(scope.equals(ATTR_SCOPE_QUE)){
				showVarDomain = 1;
			} else if(scope.equals(ATTR_SCOPE_LOCAL)){
				showVarDomain = 10;
			} else{
				new ScriptMsgException("<que name=\""+parentNode.getQueName()+"\"> <debugger>: attribute '" + ATTR_SCOPE + "' must be either script|que|local, but is " + getAttributeValue(ATTR_SCOPE) + " at line(" + lineNumber +" )");
			}
		}
		if(getAttributeValue(ATTR_NAME) != null){
			name = getAttributeValue(ATTR_NAME);
		} else {
			name = "que(" + this.getQueName()+ ") inside " + parentNode.cmdName + " at line(" + lineNumber +")";
		}

		if(debugMode)
			Debug.verbose("QueScript - NodeFactory", "que("+parentNode.getQueName()+") "+new String(new char[getLevel()]).replace('\0', '_')+" created "+cmdName+"-Comnd");
	}

	@Override
	public void store(Node _parentElement) {
		// TODO Auto-generated method stub

	}

	public void bang(CMsgShuttle _msg) {
		if(!_msg.isWaitLocked()){
			lockLessBang(_msg);
		}
	}

	public void lockLessBang(CMsgShuttle _msg){
		if(debugMode){
			String var;
			ExpressionVar exVar;
			Iterator<String> it;
			getOutput().outputSendMsg(QueMsgFactory.getMsg("print").add("DEBUGGER " + name).done());

			int levels = prt.getScopeLevels();
			for(int i = 0; i < levels; i++){
				if(showVarDomain >= i){
					HashMap<String, ExpressionVar> global = (HashMap<String, ExpressionVar>) prt.getScope(i);
					getOutput().outputSendMsg(QueMsgFactory.getMsg("print").add("-------  ").add("Scope:").add(i).add("  -------").done());
					it = global.keySet().iterator();
					while(it.hasNext()){
						var = it.next();
						exVar = global.get(var);
						getOutput().outputSendMsg(QueMsgFactory.getMsg("print").add(var).add(" = ").add(exVar.toString()).add(" (" + ((exVar.isArray())?"array":(exVar.isNumeric())?"float":"string") + ")").done());
					}
				}
			}
			getOutput().outputSendMsg(QueMsgFactory.getMsg("print").add("------------------").done());
		}
	}

	@Override
	public void resume(long _timePassed) {
		// TODO Auto-generated method stub

	}

}
