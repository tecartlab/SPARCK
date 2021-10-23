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
import java.util.HashMap;

import com.tecartlab.quescript.QueMessage;
import com.tecartlab.quescript.expression.ExpressionNode;
import com.tecartlab.quescript.expression.Expression.ExpressionException;

public abstract class QueMessageRAW implements QueMessage{

	protected String messageName;
	protected String sendTo;

	protected ArrayList<Object> tempList;

	protected HashMap<Integer, ExpressionNode> evalList;

	protected int index;

	protected QueMessageRAW(String messageName){
		index = 0;
		tempList = new ArrayList<Object>();
		tempList.add(messageName);
		this.messageName = messageName;
	}

	protected QueMessageRAW addSendTo(String sendTo){
		index++;
		tempList.add(sendTo);
		this.sendTo = sendTo;
		return this;
	}

	protected QueMessageRAW add(String token){
		index++;
		tempList.add(token);
		return this;
	}

	protected QueMessageRAW add(ExpressionNode token){
		index++;
		if (evalList == null)
			evalList = new HashMap<Integer, ExpressionNode>();
		evalList.put(new Integer(index), token);
		tempList.add(token);
		return this;
	}

	protected QueMessageRAW add(double number){
		index++;
		tempList.add(new Double(number));
		return this;
	}

	protected QueMessageRAW add(long number){
		index++;
		tempList.add(new Long(number));
		return this;
	}

	protected void clear(){
		if(tempList != null)
			tempList.clear();
		if(evalList != null)
			evalList.clear();
	}

	/**
	 * Calling this method requires to call eval() beforehand
	 * @param cmdName
	 * @param queName
	 * @return
	 */
	abstract protected String[] getStringArray(String cmdName, String queName);

	/**
	 * Calling This method indicates the creation of this method is done
	 * and makes the message ready for shipment
	 * @return
	 */
	abstract protected QueMessageRAW done();

	/**
	 * Calling the method will evaluate all stored expressions
	 * and makes the message ready for shipment
	 * @return
	 * @throws ExpressionException
	 */
	abstract protected QueMessageRAW eval() throws ExpressionException;

}
