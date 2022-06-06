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

import com.cycling74.max.Atom;

import com.tecartlab.quescript.expression.Expression.ExpressionException;
import com.tecartlab.quescript.expression.ExpressionAtom;
import com.tecartlab.quescript.expression.ExpressionNode;

public class QueMessageAtom extends QueMessageRAW{

	private Atom[] messageList;
	private ArrayList<Atom> msgList;
	
	protected QueMessageAtom(String messageName){
		super(messageName);
	}

	public boolean hasAtoms() {
		return (msgList.size() > 0)?true: false;
	}

	public Object[] getAtoms() {
		Atom[] arr = new Atom[msgList.size()];
		return msgList.toArray(arr);
	}

	protected QueMessageRAW done() {
		messageList = new Atom[index + 1];
		msgList = new ArrayList<Atom>();
		for(int i = 0; i <= index; i++){
			if(tempList.get(i) instanceof String){
				messageList[i] = Atom.newAtom((String)tempList.get(i));
				msgList.add(messageList[i]);
			} else if(tempList.get(i) instanceof Double){
				messageList[i] = Atom.newAtom((Double)tempList.get(i));
				msgList.add(messageList[i]);
			} else if(tempList.get(i) instanceof Long){
				messageList[i] = Atom.newAtom((Long)tempList.get(i));
				msgList.add(messageList[i]);
			} else if(tempList.get(i) instanceof ExpressionNode){
				messageList[i] = null;
			} else {
				messageList[i] = Atom.newAtom(0);
				msgList.add(messageList[i]);
			}
		}
		return this;
	}

	protected QueMessageRAW eval() throws ExpressionException {
		if(evalList != null && evalList.size() > 0){
			ExpressionNode ev;
//			System.out.println(" evallist size = " + evalList.size() + " | messageList size = " + messageList.length);
			msgList.clear();
			for(int i = 0; i < messageList.length; i++) {
				if(messageList[i] == null) {
					ev = evalList.get(i);
					ev.eval();
					if(ev.isArray()) {
						for(ExpressionAtom atom: ev.getValues().getValues()) {
							if(atom.isNumeric())
								msgList.add(Atom.newAtom(atom.getNumericValue()));
							else
								msgList.add(Atom.newAtom(atom.getStringValue()));							
						}
					} else {
						if(ev.isNumeric())
							msgList.add(Atom.newAtom(ev.getNumberValue()));
						else
							msgList.add(Atom.newAtom(ev.toString()));
					}
				} else {
					msgList.add(messageList[i]);
				}
			}
		}
		return this;
	}

	protected String[] getStringArray(String cmdName, String queName){
		String[] cmnd = new String[msgList.size() + 2];
		cmnd[0] = cmdName;
		cmnd[1] = queName;
		for(int i = 0; i < msgList.size(); i++){
			cmnd[i + 2] = msgList.get(i).getString();
		}
		return cmnd;
	}

	protected void clear(){
		super.clear();
	}

}
