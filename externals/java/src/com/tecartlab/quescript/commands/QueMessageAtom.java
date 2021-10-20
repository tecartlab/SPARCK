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

import com.cycling74.max.Atom;

import com.tecartlab.quescript.expression.Expression.ExpressionException;
import com.tecartlab.quescript.expression.ExpressionVar;

public class QueMessageAtom extends QueMessageRAW{

	private Atom[] messageList;

	private int iterationIndex;

	protected QueMessageAtom(String messageName){
		super(messageName);
	}

	public boolean hasAtoms() {
		return (messageList == null)?false: true;
	}

	public Object[] getAtoms() {
		return messageList;
	}

	public void iterate() {
		iterationIndex = 0;
	}

	public boolean hasNext() {
		return (iterationIndex <= index);
	}

	public boolean isNextString() {
		return messageList[iterationIndex].isString();
	}

	public boolean isNextFloat() {
		return messageList[iterationIndex].isFloat();
	}

	public String nextString() {
		return messageList[iterationIndex].toString();
	}

	public float nextFloat() {
		return messageList[iterationIndex].toFloat();
	}

	protected QueMessageRAW done() {
		messageList = new Atom[index + 1];
		for(int i = 0; i <= index; i++){
			if(tempList.get(i) instanceof String){
				messageList[i] = Atom.newAtom((String)tempList.get(i));
			} else if(tempList.get(i) instanceof Double){
				messageList[i] = Atom.newAtom((Double)tempList.get(i));
			} else if(tempList.get(i) instanceof Long){
				messageList[i] = Atom.newAtom((Long)tempList.get(i));
			} else if(tempList.get(i) instanceof ExpressionVar){
				messageList[i] = Atom.newAtom(0);
			} else {
				messageList[i] = Atom.newAtom(0);
			}
		}
		return this;
	}

	protected QueMessageRAW eval() throws ExpressionException {
		if(evalList != null && evalList.size() > 0){
			ExpressionVar ev;
//			System.out.println(" evallist size = " + evalList.size() + " | messageList size = " + messageList.length);
			for(Integer i: evalList.keySet()){
//				System.out.println(" -> evallist int = " + i);
				ev = evalList.get(i).eval();
				if(ev.isNumeric())
					messageList[i] = Atom.newAtom(ev.getNumberValue());
				else
					messageList[i] = Atom.newAtom(ev.toString());
			}
		}
		return this;
	}

	protected String[] getStringArray(String cmdName, String queName){
		String[] cmnd = new String[messageList.length + 2];
		cmnd[0] = cmdName;
		cmnd[1] = queName;
		for(int i = 0; i < messageList.length; i++){
			cmnd[i + 2] = messageList[i].getString();
		}
		return cmnd;
	}

	protected void clear(){
		super.clear();
	}

}
