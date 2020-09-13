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

package com.tecartlab.quescript.expression;

import java.util.ArrayList;

import com.tecartlab.quescript.expression.Expression.ExpressionException;
import com.tecartlab.quescript.expression.RunTimeEnvironment.Operation;

public class ExpressionVar {
	private static final String NULL_VALUE = "NULL";

	public static final ExpressionVar ONE = new ExpressionVar(1);
	public static final ExpressionVar ZERO = new ExpressionVar(0);
	public static final ExpressionVar NULL = new ExpressionVar();
	public static final ExpressionVar PI = new ExpressionVar(
			3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679);

	private double dValue;
	private String sValue;

	protected int arrayIndex = 0;

	private Operation operation;
	private ArrayList<ExpressionVar> params;

	public boolean isNumber = false;
	public boolean isArray = false;

	// stores the expression if this instance is the result of an Expression.parse(rt)
	private String expr = null;

	// tells if this instance is used as a variable inside RunTimeEnvironment.
	protected boolean isUsedAsVariable = false;

	/**
	 * Creates an ExpressionVar with a NULL - Value
	 */
	public ExpressionVar(){
		this.sValue = NULL_VALUE;
		isNumber = false;
	}

	/**
	 * Creates an ExpressionVar with the nummeric value of
	 * @param value
	 */
	public ExpressionVar(double value){
		this.dValue = value;
		isNumber = true;
	}

	/**
	 * Creates an ExpressionVar with the String value of
	 * @param value
	 */
	public ExpressionVar(String value){
		setValue(value);
	}

	/**
	 * Creates an ExpressionVar with an Evaluation Tree
	 * @param varValue
	 */
	public ExpressionVar(Operation op, ArrayList<ExpressionVar> p){
		operation = op;
		if(op.oper.equals("ARRAY") || op.oper.equals("[]")){
			isArray = true;
			if(op.oper.equals("[]")){
				isUsedAsVariable = true;
			}
		}
		params = p;
		this.dValue = 0;
		isNumber = true;
	}

	/**
	 * Creates an ExpressionVar Array with an Evaluation Tree
	 * @param varValue
	 */
	public ExpressionVar(ArrayList<ExpressionVar> p){
		operation = null;
		params = p;
		dValue = 0;
		isNumber = true;
		isUsedAsVariable = true;
		if(p.size() > 0){
			set(p.get(0));
		}
		if(p.size() > 1) {
			isArray = true;
		}
	}


	/**
	 * copies the content of the passed ExpressionVar into this ExpressionVar
	 * @param expr
	 * @returns this instance
	 */
	public ExpressionVar copyFrom(ExpressionVar expr){
		this.arrayIndex = expr.arrayIndex;
		this.dValue = expr.dValue;
		this.expr = expr.expr;
		this.isArray = expr.isArray;
		this.isNumber = expr.isNumber;
		this.isUsedAsVariable = expr.isUsedAsVariable;
		this.operation = expr.operation;
		this.params = expr.params;
		this.sValue = expr.sValue;
		return this;
	}

	/**
	 * get the number of parameters inside this ExpressionVar
	 * @return the number of parameters
	 */
	protected int getParamSize(){
		if(params != null){
			return params.size();
		}
		return 0;
	}


	/**
	 * get the ExpressionVar at the parameter index
	 * @param index
	 * @return null if no parameter is set
	 */
	protected ExpressionVar getParam(int index){
		if(params != null && params.size() > index){
			return params.get(index);
		}
		return null;
	}

	/**
	 * Used by RunTimeEnvironment to tell if this instance is a variable
	 * @return
	 */
	public ExpressionVar setUsedAsVariable(){
		isUsedAsVariable = true;
		return this;
	}

	/**
	 * Used by Expression to store the expression if this instance is the result of
	 * a parse() operation
	 * @param expr
	 * @return
	 */
	protected ExpressionVar setExpression(String expr){
		this.expr = expr;
		return this;
	}

	/**
	 * Set this instance with the values of the passed ExpressionVar
	 * @param val
	 * @return this instance
	 */
	public ExpressionVar set(ExpressionVar val){
		if(val.isNumber){
			this.dValue = val.getNumberValue();
			this.isNumber = true;
		} else {
			this.sValue = val.getStringValue();
			this.isNumber = false;
		}
		return this;
	}

	/**
	 * Mutate this instance and set it with a String Value
	 * @param val
	 * @return this instance
	 */
	public ExpressionVar setValue(String val){
		try{
			this.dValue = Double.parseDouble(val);
			isNumber = true;
		} catch (NumberFormatException e){
			this.sValue = val;
			isNumber = false;
		}
		return this;
	}

	/**
	 * Mutate this instance and set it with a double Value
	 * @param val
	 * @return this instance
	 */
	public ExpressionVar setValue(double val){
		this.dValue = val;
		isNumber = true;
		return this;
	}

	/**
	 * Get the numeric value of this instance.
	 * @return the numeric value. If it is a String var, it returns 0
	 */
	public double getNumberValue(){
		if(isNumber)
			return dValue;
		return 0;
	}

	/**
	 * Get the String value of this instance
	 * @return the String. If it is a numeric var, the number returned as a String
	 */
	public String getStringValue(){
		if(!isNumber)
			return sValue;
	    if(dValue == (long) dValue)
	        return String.format("%d",(long)dValue);
	    else
	        return String.format("%s",dValue);
	}

	/**
	 * Evaluates the Expression Tree (if there is one).
	 * The result is stored inside this instance.
	 * If this instance is returned by the parse() function of
	 * Expression, this function should be called before you
	 * attempt do get its value.
	 * @return this instance
	 * @throws ExpressionException
	 */
	public ExpressionVar eval() throws ExpressionException{
		if(operation != null){
			try {
				for(ExpressionVar exp: params)
					exp.eval();
				set(operation.eval(params));
			} catch (ExpressionException e) {
				throw new ExpressionException(e.getMessage() + "\n" + expr);
			}
		}
		return this;
	}

	/**
	 * Adds the provided Var to this Var.
	 * If one of them is a String var, the returned var is a String var too.
	 * @param v2
	 * @return a new instance of an ExpressionVar containing the result
	 */
	protected ExpressionVar add(ExpressionVar v2) {
		if(isNumber && v2.isNumber)
			return new ExpressionVar(this.dValue + v2.getNumberValue());
		else
			return new ExpressionVar(getStringValue() + v2.getStringValue());
	}

	/**
	 * Compares the numeric value of this instance with the argument.
	 * -1 if this instance is < than the argument
	 * 0 if both are the same
	 * +1 if this instance is > than the argument
	 * @param v2
	 * @return a new instance of an ExpressionVar containing the result
	 */
	protected int compareTo(ExpressionVar v2) {
		if(isNumber && v2.isNumber)
			return (getNumberValue() == v2.getNumberValue())? 0: (getNumberValue() > v2.getNumberValue())? 1: -1;
		else
			return (getStringValue().equals(v2.getStringValue()))? 0: 1;
	}

	/**
	 * Returns a String representation of this instance
	 */
	public String toString(){
		return getStringValue();
	}

}
