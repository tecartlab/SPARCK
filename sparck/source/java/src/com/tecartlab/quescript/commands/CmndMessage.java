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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;

import com.tecartlab.quescript.expression.Expression;
import com.tecartlab.quescript.expression.RunTimeEnvironment;
import com.tecartlab.quescript.expression.Expression.ExpressionException;
import com.tecartlab.quescript.messages.CMsgShuttle;
import com.tecartlab.quescript.messages.ScriptMsgException;
import com.tecartlab.utils.Debug;

public class CmndMessage extends Cmnd {
	public static String NODE_NAME_SEND 	= "send";
	public static String NODE_NAME_PRINT 	= "print";
	public static String NODE_NAME_OUT 		= "out";
	public static String NODE_NAME_TRIGGER 	= "trigger";
	public static String NODE_NAME_OSC 		= "osc";

	private static String ATTR_SENDTO = "sendto";

	String sendto = "default";

	QueMessageRAW myMessage;

	public CmndMessage(Cmnd _parentNode, String _cmdName){
		super(_parentNode);
		super.setCmndName(_cmdName);
	}

	public void build(Node _xmlNode) throws ScriptMsgException{
		super.build(_xmlNode);

		myMessage = QueMsgFactory.getMsg(this.cmdName);

		if(super.cmdName.equals(NODE_NAME_OSC)){
			// only the OSC message has an sendto attribute
			if(this.hasAttributeValue(ATTR_SENDTO)){
				sendto = getAttributeValue(ATTR_SENDTO);
			}
			myMessage.addSendTo(sendto);
		}
	}

	private void parseContentString(String _content, RunTimeEnvironment rt) throws ExpressionException{
		List<String> segmts = new ArrayList<String>();
		Matcher m = Pattern.compile("([^{]\\S*|.+?[{}])\\s*").matcher(_content);
		while (m.find())
			segmts.add(m.group(1).trim()); // Add .replace("\"", "") to remove surrounding quotes.

		boolean isNotSet = false;
		for(int i = 0; i < segmts.size(); i++){
			isNotSet = true;
			// first try int
			if(isNotSet){
				try{
					myMessage.add(Integer.parseInt(segmts.get(i)));
					isNotSet = !isNotSet;
				} catch (NumberFormatException e){;}
			}
			if(isNotSet){
				try{
					myMessage.add(Double.parseDouble(segmts.get(i)));
					isNotSet = !isNotSet;
				} catch (NumberFormatException e){;}
			}
			if(isNotSet){
				if(segmts.get(i).startsWith("{"))
					myMessage.add(new Expression(segmts.get(i), "{", "}").setInfo(" at line(" + lineNumber + ")").parse(rt));
				else
					myMessage.add(segmts.get(i));
			}
		}
		myMessage.done();
	}

	/**
	 * Parse the Expressions with the RuntimeEnvironement
	 */
	public void setup(RunTimeEnvironment rt)throws ScriptMsgException{
		try {
			parseContentString(super.content, rt);
		} catch (ExpressionException e) {
			throw new ScriptMsgException("Script - Command <" + cmdName +"> " + e.getMessage() + " at line("+lineNumber+")");
		}

		if(debugMode)
			Debug.verbose("QueScript - NodeFactory", "que("+parentNode.getQueName()+") "+new String(new char[getLevel()]).replace('\0', '_')+" created "+cmdName+"-Comnd = '"+super.content+"'");

		// and then do it for all the children
		for(Cmnd child: this.getChildren()){
			child.setup(rt);
		}
	}

	/**
	 * Sends the message after it takes all the interpolated values.
	 */
	public void lockLessBang(CMsgShuttle _msg){
		try {
			myMessage.eval();
			getOutput().outputSendMsg(myMessage);
		} catch (ExpressionException e) {
			Debug.error("Script - Command <" + cmdName +">", "expression evaluation error: " + e.getMessage() + " at line("+lineNumber+")");
		}
	}

	public void clear(){
		myMessage.clear();
		for(Cmnd child: getChildren()){
			child.clear();
		}
	}

	public void bang(CMsgShuttle _msg) {
		if(!_msg.isWaitLocked()){
			lockLessBang(_msg);
			if(cmdName.equals(NODE_NAME_TRIGGER)){
				sendInternalMessage();
			}
		}
	}

	private void sendInternalMessage(){
		getOutput().selfCommand(myMessage.getStringArray(cmdName, parentNode.getQueName()));
	}

	public void resume(long _timePassed) {
	}

}
