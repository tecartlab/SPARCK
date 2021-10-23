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

public class ExpressionNode {
	
	// result of the evaluation
	protected ExpressionVar eValuated;
	
	// operation of this node
	private Operation operation;
	
	// the local branch of the nodetree
	protected ArrayList<ExpressionNode> nodeTree;

	// stores the expression if this instance is the result of an Expression.parse(rt)
	private String expr = null;

	// tells if this instance is used as a variable inside RunTimeEnvironment.
	protected boolean isUsedAsVariable = false;

	/**
	 * Creates an ExpressionVar with a NULL - Value
	 */
	public ExpressionNode(){
		eValuated = new ExpressionVar(0);
	}

	/**
	 * Creates an ExpressionVar with the nummeric value of
	 * @param value
	 */
	public ExpressionNode(double value){
		this();
		setValue(value);
	}

	/**
	 * Creates an ExpressionVar with the String value of
	 * @param value
	 */
	public ExpressionNode(String value){
		this();
		setValue(value);
	}

	/**
	 * Creates an ExpressionVar with an Evaluation Tree
	 * @param varValue
	 */
	public ExpressionNode(Operation op, ArrayList<ExpressionNode> p){
		this();
		operation = op;
		if(op.oper.equals("ARRAY") || op.oper.equals("[]")){
			eValuated.makeArray();
			if(op.oper.equals("[]")){
				isUsedAsVariable = true;
			}
		}
		if(op.oper.equals("LERP") && p.get(1).isArray()){
			eValuated.makeArray();
		}
		nodeTree = p;
	}

	/**
	 * Creates an ExpressionVar Array with an Evaluation Tree
	 * @param varValue
	 */
			
	public ExpressionNode(ArrayList<ExpressionNode> p){
		operation = null;
		nodeTree = p;
		isUsedAsVariable = true;
	}


	/**
	 * copies the content of the passed ExpressionVar into this ExpressionVar
	 * @param expr
	 * @returns this instance
	 */
	public ExpressionNode copyFrom(ExpressionNode expr){
		this.expr = expr.expr;
		this.eValuated = expr.eValuated.clone();
		this.isUsedAsVariable = expr.isUsedAsVariable;
		this.operation = expr.operation;
		this.nodeTree = expr.nodeTree;
		return this;
	}

	/**
	 * get the number of parameters inside this ExpressionVar
	 * @return the number of parameters
	 */
	protected int getParamSize(){
		if(nodeTree != null){
			return nodeTree.size();
		}
		return 0;
	}


	/**
	 * get the ExpressionVar at the parameter index
	 * @param index
	 * @return null if no parameter is set
	 */
	protected ExpressionNode getParam(int index){
		if(nodeTree != null && nodeTree.size() > index){
			return nodeTree.get(index);
		}
		return null;
	}

	/**
	 * Used by RunTimeEnvironment to tell if this instance is a variable
	 * @return
	 */
	public ExpressionNode setUsedAsVariable(){
		isUsedAsVariable = true;
		return this;
	}

	/**
	 * Used by Expression to store the expression if this instance is the result of
	 * a parse() operation
	 * @param expr
	 * @return
	 */
	protected ExpressionNode setExpression(String expr){
		this.expr = expr;
		return this;
	}

	/**
	 * Set this instance with the values of the passed ExpressionVar
	 * @param val
	 * @return this instance
	 */
	public ExpressionNode set(ExpressionNode val){
		if(val != null) {
			eValuated = val.eValuated.clone();
		}
		return this;
	}

	/**
	 * Mutate this instance and set it with a String Value
	 * @param val
	 * @return this instance
	 */
	public ExpressionNode setValue(String val){
		try{
			double dValue = Double.parseDouble(val);
			this.eValuated.setValue(dValue);
		} catch (NumberFormatException e){
			this.eValuated.setValue(val);
		}
		return this;
	}

	/**
	 * Mutate this instance and set it with a double Value
	 * @param val
	 * @return this instance
	 */
	public ExpressionNode setValue(double val){
		this.eValuated.setValue(val);
		return this;
	}

	/**
	 * Mutate this instance and set it with an atom
	 * @param val
	 * @return
	 */
	public ExpressionNode setValue(ExpressionAtom val){
		this.eValuated.setValue(val);
		return this;
	}


	/**
	 * Get the numeric value of this instance.
	 * @return the numeric value. If it is a String var, it returns 0
	 */
	public double getNumberValue(){
		return this.eValuated.getNumericValue();
	}

	/**
	 * check if evaluated value is an array
	 * @return
	 */
	public boolean isArray() {
		return this.eValuated.isArray();
	}
	
	/**
	 * check if evaluated value is numeric
	 * @return
	 */
	public boolean isNumeric() {
		return this.eValuated.isNumeric();		
	}
	
	/**
	 * Evaluates the Expression Tree (if there is one).
	 * The result is stored inside this instance.
	 * If this instance is returned by the parse() function of
	 * Expression, this function should be called before you
	 * attempt do get its value.
	 * @return true if the inner evaluation tree needed to be reevaluated
	 * @throws ExpressionException
	 */
	public ExpressionNode eval() throws ExpressionException{
		if(operation != null){
			try {
				// reevaluate inner tree
				for(ExpressionNode var: nodeTree) {
					var.eval();
				}
				
				operation.eval(nodeTree, eValuated);
				
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
	protected int compareTo(ExpressionNode v2) {
		return this.eValuated.compareTo(v2.eValuated);
	}

	/**
	 * Returns a String representation of this instance
	 */
	public String toString(){
		return eValuated.toString();
	}

}
