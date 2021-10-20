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
	protected ExpressionEvaluated evaluated;
		
	protected int arrayIndex = 0;

	private Operation operation;
	protected ArrayList<ExpressionVar> params;

	// stores the expression if this instance is the result of an Expression.parse(rt)
	private String expr = null;

	// tells if this instance is used as a variable inside RunTimeEnvironment.
	protected boolean isUsedAsVariable = false;

	/**
	 * Creates an ExpressionVar with a NULL - Value
	 */
	public ExpressionVar(){
		evaluated = new ExpressionEvaluated(0);
	}

	/**
	 * Creates an ExpressionVar with the nummeric value of
	 * @param value
	 */
	public ExpressionVar(double value){
		this();
		setValue(value);
	}

	/**
	 * Creates an ExpressionVar with the String value of
	 * @param value
	 */
	public ExpressionVar(String value){
		this();
		setValue(value);
	}

	/**
	 * Creates an ExpressionVar with an Evaluation Tree
	 * @param varValue
	 */
	public ExpressionVar(Operation op, ArrayList<ExpressionVar> p){
		this();
		// TODO:Check if still working
		operation = op;
		if(op.oper.equals("ARRAY") || op.oper.equals("[]")){
			evaluated.makeArray();
			if(op.oper.equals("[]")){
				isUsedAsVariable = true;
			}
		}
		if(op.oper.equals("LERP") && p.get(1).isArray()){
			evaluated.makeArray();
		}
		params = p;
	}

	/**
	 * Creates an ExpressionVar Array with an Evaluation Tree
	 * @param varValue
	 */
			
	public ExpressionVar(ArrayList<ExpressionVar> p){
		// TODO:Check if still working
		operation = null;
		params = p;
		isUsedAsVariable = true;
	}


	/**
	 * copies the content of the passed ExpressionVar into this ExpressionVar
	 * @param expr
	 * @returns this instance
	 */
	public ExpressionVar copyFrom(ExpressionVar expr){
		this.arrayIndex = expr.arrayIndex;
		this.expr = expr.expr;
		this.evaluated = expr.evaluated.clone();
		this.isUsedAsVariable = expr.isUsedAsVariable;
		this.operation = expr.operation;
		this.params = expr.params;
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
		if(val != null) {
			evaluated = val.evaluated.clone();
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
			double dValue = Double.parseDouble(val);
			this.evaluated.setValue(dValue);
		} catch (NumberFormatException e){
			this.evaluated.setValue(val);
		}
		return this;
	}

	/**
	 * Mutate this instance and set it with a double Value
	 * @param val
	 * @return this instance
	 */
	public ExpressionVar setValue(double val){
		this.evaluated.setValue(val);
		return this;
	}

	/**
	 * Mutate this instance and set it with an atom
	 * @param val
	 * @return
	 */
	public ExpressionVar setValue(ExpressionAtom val){
		this.evaluated.setValue(val);
		return this;
	}


	/**
	 * Get the numeric value of this instance.
	 * @return the numeric value. If it is a String var, it returns 0
	 */
	public double getNumberValue(){
		return this.evaluated.getNumericValue();
	}

	/**
	 * check if evaluated value is an array
	 * @return
	 */
	public boolean isArray() {
		return this.evaluated.isArray();
	}
	
	/**
	 * check if evaluated value is numeric
	 * @return
	 */
	public boolean isNumeric() {
		return this.evaluated.isNumeric();		
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
				boolean dirtied = false;
				for(ExpressionVar var: params) {
					if(var.evaluated.isDirty()) {
						var.eval();
						dirtied = true;
					}
				}
				if(dirtied)
					operation.eval(params, evaluated);
				
				evaluated.cleaned();
			} catch (ExpressionException e) {
				throw new ExpressionException(e.getMessage() + "\n" + expr);
			}
		}
		return this;
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
		return this.evaluated.compareTo(v2.evaluated);
	}

	/**
	 * Returns a String representation of this instance
	 */
	public String toString(){
		return evaluated.toString();
	}

}
