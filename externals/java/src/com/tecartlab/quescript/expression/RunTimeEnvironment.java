/*
 * Copyright 2015/2016 Martin Fröhlich
 * Copyright 2012/2015 Udo Klimaschewski
 * 
 * http://maybites.ch
 * http://UdoJava.com/
 * http://about.me/udo.klimaschewski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package com.tecartlab.quescript.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.tecartlab.quescript.expression.Expression.ExpressionException;
import com.tecartlab.utils.Debug;

public class RunTimeEnvironment {
	/**
	 * Super Storage for all Variables. The individual variables are stored inside 
	 * HashMaps, their position inside this storage indicates their domain.
	 */
	private ArrayList<Map<String, ExpressionNode>> allVarScopes;
	
	/**
	 * local variable scope
	 */
	private HashMap<String, ExpressionNode> localVarScope;
	
	/**
	 * All defined operators with name and implementation.
	 */
	protected Map<String, Operator> operators;

	/**
	 * All defined functions with name and implementation.
	 */
	public Map<String, Function> functions;

	/**
	 * All defined variables with name and value.
	 */
	protected Map<String, ExpressionNode> staticVars;

	public RunTimeEnvironment(RunTimeEnvironment rt) {
		operators = rt.operators;
		functions = rt.functions;
		staticVars = rt.staticVars;
		allVarScopes = new ArrayList<Map<String, ExpressionNode>>();
		for(Map<String, ExpressionNode> m: rt.allVarScopes)
			allVarScopes.add(m);
		localVarScope = new HashMap<String, ExpressionNode>();
		allVarScopes.add(localVarScope);		
	}

	public RunTimeEnvironment() {
		allVarScopes = new ArrayList<Map<String, ExpressionNode>>();
		operators = new HashMap<String, Operator>();
		functions = new HashMap<String, Function>();
		staticVars = new HashMap<String, ExpressionNode>();
		localVarScope = new HashMap<String, ExpressionNode>();
		allVarScopes.add(localVarScope);

		addOperator(new Operator("@", 5, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException{
				if(parameters.size() == 1){
					ExpressionVar p0 = parameters.get(0).eValuated;

					if(p0.isArray()){
						if(!result.isArray()) {
							result.makeArray();
						}
						if(p0.getArraySize() != result.getArraySize()) {
							result.setValues(p0.getValues());
						} else {
							for(int i = 0; i < p0.getArraySize(); i++) {
								result.setArrayIndex(i, p0.getArrayIndex(i));
							}
						}
					} else {
						result.setValue(p0.getValue());
					}
				}	
				//throw new ExpressionException("= can only assign to a variable");
			}
		});		
		addOperator(new Operator("=", 5, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException{
				if(parameters.size() == 2){
					ExpressionVar p0 = parameters.get(0).eValuated;
					ExpressionVar p1 = parameters.get(1).eValuated;

					if(p1.isArray()){
						if(!p0.isArray()) {
							p0.makeArray();
						}
						if(p1.getArraySize() != p0.getArraySize()) {
							p0.setValues(p1.getValues());
						} else {
							for(int i = 0; i < p1.getArraySize(); i++) {
								p0.setArrayIndex(i, p1.getArrayIndex(i));
							}
						}
					} else {
						p0.setValue(p1.getValue());
					}
				}	
				//throw new ExpressionException("= can only assign to a variable");
			}
		});		
		addOperator(new Operator("+", 20, true) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				if(p0.isArray()) {
					if(!result.isArray() || result.getArraySize() != p0.getArraySize()) {
						// we first have to adjust the result side
						result.reset();
						result.setValues(p0.getClonedValues());
					}
					// TODO: getArrayIndex(i) is resource hungry and is done multiple times: better store one call to a local var
					ExpressionAtom p0Atom, p1Atom;
					if(p1.isArray()) {
						//p0 is array and p1 is array -> dont need to be same size, 
						// each corresponding element will be added, be it numeric or string
						for(int i = 0; i < p0.getArraySize() && i < p1.getArraySize(); i++) {
							p0Atom = p0.getArrayIndex(i);
							p1Atom = p1.getArrayIndex(i);
							
							if(p0Atom.isNumeric() && p1Atom.isNumeric()) {
								result.getArrayIndex(i).setNumericValue(p0Atom.getNumericValue() + p1Atom.getNumericValue());
							} else {
								result.getArrayIndex(i).setStringValue(p0Atom.getStringValue() + p1Atom.getStringValue());
							}
						}						
					} else {
						//p0 is array and p1 is not -> p1 is added to each element of p0
						for(int i = 0; i < p0.getArraySize(); i++) {
							p0Atom = p0.getArrayIndex(i);
							p1Atom = p1.getArrayIndex(i);
							
							if(p0Atom.isNumeric() && p1Atom.isNumeric()) {
								result.getArrayIndex(i).setNumericValue(p0Atom.getNumericValue() + p1Atom.getNumericValue());
							} else {
								result.getArrayIndex(i).setStringValue(p0Atom.getStringValue() + p1Atom.getStringValue());
							}
						}												
					}
				} else { 
					if(result.isArray()) {
						// we first have to adjust the result side
						result.reset();
					}
					if(p0.isNumeric() && p1.isNumeric()) {
						result.setValue(p0.getNumericValue() + p1.getNumericValue());
					} else {
						result.setValue(p0.getStringValue() + p1.getStringValue());
					}
				}
			}
		});
		addOperator(new Operator("-", 20, true) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException{
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;
				
				if(p0.isNumeric() && p1.isNumeric()) {
					if(!p0.isArray() && !p1.isArray()) {
						result.setValue(p0.getNumericValue() - p1.getNumericValue());
					} else {
						throw new ExpressionException("Array subtraction not supported");						
					}
				} else {
					throw new ExpressionException("String subtraction not supported");												
				}
			}
		});
		addOperator(new Operator("*", 30, true) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				if(p0.isNumeric() && p1.isNumeric()) {
					if(!p0.isArray() && !p1.isArray()) {
						result.setValue(p0.getNumericValue() * p1.getNumericValue());
					} else {
						throw new ExpressionException("Array multiplication not supported");						
					}
				} else {
					throw new ExpressionException("String multiplication not supported");												
				}
			}
		});
		addOperator(new Operator("/", 30, true) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				if(p0.isNumeric() && p1.isNumeric()) {
					if(!p0.isArray() && !p1.isArray()) {
						result.setValue(p0.getNumericValue() / p1.getNumericValue());
					} else {
						throw new ExpressionException("Array division not supported");						
					}
				} else {
					throw new ExpressionException("String division not supported");												
				}
			}
		});
		addOperator(new Operator("%", 30, true) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				if(p0.isNumeric() && p1.isNumeric()) {
					if(!p0.isArray() && !p1.isArray()) {
						result.setValue(p0.getNumericValue() % p1.getNumericValue());
					} else {
						throw new ExpressionException("Array modulo not supported");						
					}
				} else {
					throw new ExpressionException("String modulo not supported");												
				}
			}
		});
		addOperator(new Operator("^", 40, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				if(p0.isNumeric() && p1.isNumeric()) {
					if(!p0.isArray() && !p1.isArray()) {
						result.setValue(Math.pow(p0.getNumericValue(), p1.getNumericValue()));
					} else {
						throw new ExpressionException("Array power not supported");						
					}
				} else {
					throw new ExpressionException("String power not supported");												
				}
			}
		});	
		addOperator(new Operator("[]", 50, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar array = parameters.get(0).eValuated;
				ExpressionVar indice = parameters.get(1).eValuated;

				if(array.isArray()){
					if(indice.isNumeric()){
						int index = (int)indice.getNumericValue();
						if(index < array.getArraySize()){
							result.setValue(array.getArrayIndex(index));
							return;
						} 					
						throw new ExpressionException("Array index out of bounds exception: " + index + " " + array.getArraySize());	
					} 
					throw new ExpressionException("Array index invalid number: " + parameters.get(1).toString());
				}
				throw new ExpressionException("Invalid operation: " + parameters.get(0).toString() + " is not an array.");
			}
		});
		addOperator(new Operator("&&", 8, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				boolean b0 = !(p0.getNumericValue() == 0);
				boolean b1 = !(p1.getNumericValue() == 0);
				result.setValue((b0 && b1)?1:0);
			}
		});
		addOperator(new Operator("||", 8, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				boolean b0 = !(p0.getNumericValue() == 0);
				boolean b1 = !(p1.getNumericValue() == 0);
				result.setValue((b0 || b1)?1:0);
			}
		});
		addOperator(new Operator(">", 10, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				result.setValue((p0.getNumericValue() > p1.getNumericValue())?1:0);
			}
		});
		addOperator(new Operator("gt", 10, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				operators.get(">").eval(parameters, result);
			}
		});
		addOperator(new Operator(">=", 10, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				result.setValue((p0.getNumericValue() >= p1.getNumericValue())?1:0);
			}
		});
		addOperator(new Operator("ge", 10, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				operators.get(">=").eval(parameters, result);
			}
		});
		addOperator(new Operator("<", 10, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				result.setValue((p0.getNumericValue() < p1.getNumericValue())?1:0);
			}
		});	
		addOperator(new Operator("lt", 10, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				operators.get("<").eval(parameters, result);
			}
		});
		addOperator(new Operator("<=", 10, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				result.setValue((p0.getNumericValue() <= p1.getNumericValue())?1:0);
			}
		});	
		addOperator(new Operator("le", 10, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException{
				operators.get("<=").eval(parameters, result);
			}
		});
		addOperator(new Operator("==", 7, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;

				result.setValue((p0.getNumericValue() == p1.getNumericValue())?1:0);
			}
		});
		addOperator(new Operator("!=", 7, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;
				
				result.setValue((p0.getNumericValue() != p1.getNumericValue())?1:0);
			}
		});
		addOperator(new Operator("<>", 7, false) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {

				operators.get("!=").eval(parameters, result);
			}
		});


		addFunction(new Function("LENGTH", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isArray()){
					result.setValue(p0.getArraySize());
				} else {
					result.setValue(0d);
				}
				return;					
			}
		});
		addFunction(new Function("NOT", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				result.setValue((p0.getNumericValue() == 0)?1:0);
			}
		});
		addFunction(new Function("IF", 3) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				ExpressionVar p1 = parameters.get(1).eValuated;
				ExpressionVar p2 = parameters.get(2).eValuated;

				result.setValues((p0.getNumericValue() == 1)?p1.getValues():p2.getValues());
			}
		});
		addFunction(new Function("RANDOM", 0) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				result.setValue(Math.random());
			}
		});
		addFunction(new Function("SIN", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.sin(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("SIN doesnt work with arrays");
				}
				throw new ExpressionException("SIN doesnt work with strings");
			}
		});
		addFunction(new Function("COS", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.cos(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("COS doesnt work with arrays");
				}
				throw new ExpressionException("COS doesnt work with strings");
			}
		});
		addFunction(new Function("TAN", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.tan(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("TAN doesnt work with arrays");
				}
				throw new ExpressionException("TAN doesnt work with strings");
			}
		});
		addFunction(new Function("ASIN", 1) { // added by av
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.asin(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("ASIN doesnt work with arrays");
				}
				throw new ExpressionException("ASIN doesnt work with strings");
			}
		});
		addFunction(new Function("ACOS", 1) { // added by av
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.acos(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("ACOS doesnt work with arrays");
				}
				throw new ExpressionException("ACOS doesnt work with strings");
			}
		});
		addFunction(new Function("ATAN", 1) { // added by av
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.atan(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("ATAN doesnt work with arrays");
				}
				throw new ExpressionException("ATAN doesnt work with strings");
			}
		});
		addFunction(new Function("SINH", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.sinh(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("SINH doesnt work with arrays");
				}
				throw new ExpressionException("SINH doesnt work with strings");
			}
		});
		addFunction(new Function("COSH", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.cosh(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("COSH doesnt work with arrays");
				}
				throw new ExpressionException("COSH doesnt work with strings");										
			}
		});
		addFunction(new Function("TANH", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.tanh(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("TANH doesnt work with arrays");
				}
				throw new ExpressionException("TANH doesnt work with strings");										
			}
		});
		addFunction(new Function("RAD", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.toRadians(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("RAD doesnt work with arrays");
				}
				throw new ExpressionException("RAD doesnt work with strings");										
			}
		});
		addFunction(new Function("DEG", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.toDegrees(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("DEG doesnt work with arrays");
				}
				throw new ExpressionException("DEG doesnt work with strings");										
			}
		});
		addFunction(new Function("ARRAY", -1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				if (parameters.size() == 0) {
					throw new ExpressionException("ARRAY requires at least one parameter");
				}
				if(!result.isArray() || parameters.size() != result.getArraySize()) {
					result.clear();
					for(ExpressionNode var: parameters) {
						if(var.eValuated.isNumeric())
							result.addValue(var.eValuated.getNumericValue());
						else
							result.addValue(var.eValuated.getStringValue());
					}
				} else {
					ExpressionVar varVal;
					for(int i = 0; i < parameters.size(); i++) {
						varVal = parameters.get(i).eValuated;
						if(varVal.isNumeric())
							result.setArrayIndex(i, varVal.getNumericValue());
						else
							result.setArrayIndex(i, varVal.getStringValue());
					}	
				}
				result.cleanup();
			}
		});
		addFunction(new Function("CLONE", -1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				throw new ExpressionException("CLONE is not working yet..");
				/*
				if (parameters.size() == 0) {
					throw new ExpressionException("ARRAY requires at least one parameter");
				} else {
					if(parameters.size() == 1){
						if(parameters.get(0).isArray){
							ArrayList<ExpressionVar> newArray = new ArrayList<ExpressionVar>();
							ExpressionVar param;
							for(int i = 0; i < parameters.get(0).getParamSize(); i++){
								param = parameters.get(0).getParam(i).eval();
								if(param.isNumber){
									newArray.add(new ExpressionVar(param.getNumberValue()));
								} else {
									newArray.add(new ExpressionVar(param.getStringValue()));
								}
							}
							return new ExpressionVar(new ArrayList<ExpressionVar>(newArray));
						} 
					}
					return new ExpressionVar(new ArrayList<ExpressionVar>(parameters));
				}
				*/
			}
		});
		addFunction(new Function("MAX", -1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				if (parameters.size() == 0) {
					throw new ExpressionException("MAX requires at least one parameter");
				}
				ExpressionNode max = null;
				for (ExpressionNode parameter : parameters) {
					if(parameter.isNumeric()) {
						if(!parameter.isArray()) {
							if (max == null || parameter.compareTo(max) > 0) {
								max = parameter;
							}
						} else {
							throw new ExpressionException("MIN doesnt work with arrays");
						}
					} else {
						throw new ExpressionException("MIN doesnt work with strings");										
					}
				}
					
				result.setValue(max.getNumberValue());
			}
		});
		addFunction(new Function("MIN", -1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				if (parameters.size() == 0) {
					throw new ExpressionException("MIN requires at least one parameter");
				}

				ExpressionNode min = null;
				for (ExpressionNode parameter : parameters) {
					if(parameter.isNumeric()) {
						if(!parameter.isArray()) {
							if (min == null || parameter.compareTo(min) < 0) {
								min = parameter;
							}
						} else {
							throw new ExpressionException("MIN doesnt work with arrays");
						}
					} else {
						throw new ExpressionException("MIN doesnt work with strings");										
					}
				}

				result.setValue(min.getNumberValue());
			}
		});
		addFunction(new Function("LERP", 3) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				if (parameters.size() < 3) {
					throw new ExpressionException("LERP requires three parameters: step, start value, end value");
				}
				ExpressionVar step = parameters.get(0).eValuated;
				ExpressionVar start = parameters.get(1).eValuated;
				ExpressionVar stop = parameters.get(2).eValuated;

				
				if(start.isArray() ^ stop.isArray()) {
					throw new ExpressionException("LERP requires start value and end value to be of the same type");					
				}

				if(start.isArray() && stop.isArray() && (start.getArraySize() != stop.getArraySize())) {
					throw new ExpressionException("LERP start array and end array need to be of the same size");										
				}

				if(!step.isNumeric() || !start.isNumeric() || !stop.isNumeric()) {
					throw new ExpressionException("LERP requires step, start and end value to be numbers");										
				}
				double st, ed;
							
				if(start.isArray()) {
					if((result.isArray()) || result.getArraySize() != start.getArraySize()) {
						result.clear();
						for (int i = 0; i < start.getArraySize(); i++) {
							st = start.getArrayIndex(i).getNumericValue();
							ed = stop.getArrayIndex(i).getNumericValue();
							result.addValue(st + step.getNumericValue() * (ed - st));
						}					
					} else {
						for (int i = 0; i < start.getArraySize(); i++) {
							st = start.getArrayIndex(i).getNumericValue();
							ed = stop.getArrayIndex(i).getNumericValue();
							result.setArrayIndex(i, st + step.getNumericValue() * (ed - st));
						}					
					}
				} else {
					if(result.isArray()) {
						result.reset();
					}
					st = start.getNumericValue();
					ed = stop.getNumericValue();
					result.setValue(st + step.getNumericValue() * (ed - st));
				}
			}
		});
		addFunction(new Function("ABS", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				
				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.abs(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("ABS doesnt work with arrays");
				}
					throw new ExpressionException("ABS doesnt work with strings");										
			}
		});
		addFunction(new Function("LOG", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				
				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.log(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("LOG doesnt work with arrays");
				}
				throw new ExpressionException("LOG doesnt work with strings");										
			}
		});
		addFunction(new Function("LOG10", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
				
				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.log10(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("LOG10 doesnt work with arrays");
				}
				throw new ExpressionException("LOG10 doesnt work with strings");										
			}
		});
		addFunction(new Function("ROUND", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.round(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("ROUND doesnt work with arrays");
				}
				throw new ExpressionException("ROUND doesnt work with strings");										
			}
		});
		addFunction(new Function("FLOOR", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.floor(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("FLOOR doesnt work with arrays");
				}
				throw new ExpressionException("FLOOR doesnt work with strings");										
			}
		});
		addFunction(new Function("CEILING", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;
	
				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						result.setValue(Math.ceil(p0.getNumericValue()));
						return;
					}
					throw new ExpressionException("CEILING doesnt work with arrays");										
				}
				throw new ExpressionException("CEILING doesnt work with strings");										
			}
		});
		addFunction(new Function("SQRT", 1) {
			@Override
			public void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException {
				ExpressionVar p0 = parameters.get(0).eValuated;

				if(p0.isNumeric()) {
					if(!p0.isArray()) {
						if(p0.getNumericValue() == 0) {
							result.setValue(0);
						} else {
							if(Math.signum(p0.getNumericValue()) < 0){
								throw new ExpressionException(
										"Argument to SQRT() function must not be negative");		
							}
							result.setValue(Math.sqrt(p0.getNumericValue()));
						}
						return;
					}
					throw new ExpressionException("SQRT doesnt work with arrays");										
				}
				throw new ExpressionException("SQRT doesnt work with strings");										

			}
		});


		addStaticVariableNull();
		setStaticVariable("PI", 3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679);
		setStaticVariable("TRUE", 1);
		setStaticVariable("FALSE", 0);
	}

	/**
	 * Adds an operator to the list of supported operators.
	 * 
	 * @param operator
	 *            The operator to add.
	 * @return The previous operator with that name, or <code>null</code> if
	 *         there was none.
	 */
	protected Operator addOperator(Operator operator) {
		return operators.put(operator.getOper(), operator);
	}

	/**
	 * Adds a function to the list of supported functions
	 * 
	 * @param function
	 *            The function to add.
	 * @return The previous operator with that name, or <code>null</code> if
	 *         there was none.
	 */
	protected Function addFunction(Function function) {
		return functions.put(function.getName(), function);
	}	

	/**
	 * Clears all gobal variables all variables in the highest domain
	 */
	public void clearVariables() {
		allVarScopes.get(0).clear();
	}

	/**
	 * Sets a variable value. If the variable already exists, it uses the existing one, 
	 * otherwise it will create a new own on the specified domain
	 * 
	 * @param variable The variable name.
	 * @param value The variable value.
	 * @param scope the scope to write the variable to: 0 - local scope, 1 - next higher etc.
	 * @return reference to the protected variable, null if the domain is incorrect
	 */
	public ExpressionNode setVariable(String variable, ExpressionNode value, int scope) {
		if(scope < allVarScopes.size()){
			ExpressionNode v = getVar(variable);
			if(v == value) {
				return value;
			} else if(v != null){
				return v.setValues(value.getValues());
			} else {
				allVarScopes.get(allVarScopes.size() - 1 - scope).put(variable, value.setUsedAsVariable());
				return value;
			}
		}
		return null;
	}

	/**
	 * Sets a variable value. If the variable already exists (no matter which scope), 
	 * it uses the existing one, otherwise it will create one in the local domain
	 * 
	 * @param variable
	 *            The variable name.
	 * @param value
	 *            The variable value.
	 * @return reference to the protected variable
	 */
	public ExpressionNode setVariable(String variable, ExpressionNode value) {
		ExpressionNode v = getVar(variable);
		if(v == value) {
			return value;
		} else if(v != null){
			return v.setValues(value.getValues());
		} else {
			localVarScope.put(variable, value.setUsedAsVariable());
			return value;
		}
	}

	/**
	 * adds this node as a variable inside the local scope no matter if there are other variables of the 
	 * same name in higher scopes. If there is already a variable, it will inject the content of the
	 * value into its Expression
	 * @param variable String identifier
	 * @param value ExpressionVar object
	 * @return the reference object of this variable
	 */
	public ExpressionNode addLocalVariable(String variable, ExpressionNode value) {
		ExpressionNode v = localVarScope.get(variable);
		if(v == value) {
			return value;
		} else if(v != null){
			if(value.eValuated.isArray()){
				return v.copyFrom(value);
			} else {
				return v.setValues(value.getValues());
			}
		} else {
			localVarScope.put(variable, value.setUsedAsVariable());
			return value;
		}
	}

	/**
	 * Sets a variable value. If a local variable of the same name already exists, 
	 * it uses the existing one, otherwise it will create a new one
	 * 
	 * @param variable
	 *            The variable name.
	 * @param value
	 *            The variable value.
	 * @return reference to the protected variable
	 */
	public ExpressionNode setLocalVariable(String variable, double value) {
		ExpressionNode v = localVarScope.get(variable);
		if(v != null){
			return v.setValue(value);
		} else {
			v = new ExpressionNode(value).setUsedAsVariable();
			localVarScope.put(variable, v);
			return v;
		}
	}

	/**
	 * Sets a variable value. If a local variable of the same name already exists, 
	 * it uses the existing one, otherwise it will create a new one
	 * 
	 * @param variable
	 *            The variable name.
	 * @param value
	 *            The variable value.
	 * @return reference to the protected variable
	 */
	public ExpressionNode setLocalVariable(String variable, String value) {
		ExpressionNode v = localVarScope.get(variable);
		if(v != null){
			return v.setValue(value);
		} else {
			v = new ExpressionNode(value).setUsedAsVariable();
			localVarScope.put(variable, v);
			return v;
		}
	}

	/**
	 * Sets a variable values. If a local variable of the same name already exists, 
	 * it uses the existing one, otherwise it will create a new one
	 * 
	 * @param variable
	 *            The variable name.
	 * @param value
	 *            The variable value.
	 * @return reference to the protected variable
	 */
	public ExpressionNode setLocalVariable(String variable, ExpressionVar value) {
		ExpressionNode v = localVarScope.get(variable);
		if(v != null){
			return v.setValues(value);
		} else {
			v = new ExpressionNode(value).setUsedAsVariable();
			localVarScope.put(variable, v);
			return v;
		}
	}

	/**
	 * Returns the specified variable, looks first at the local scope and keeps on moving upwards.
	 * @param variable
	 * @return null if none of this name exists.
	 */
	public ExpressionNode getVar(String variable){
		for(int i = allVarScopes.size() - 1; i >= 0; i--){
			ExpressionNode v = allVarScopes.get(i).get(variable);
			if(v != null){
				return v;
			}
		}
		return null;
	}

	/**
	 * Returns the specified variable, looks only at the local scope.
	 * @param variable
	 * @return null if none of this name exists.
	 */
	public ExpressionNode getLocalVar(String variable){
		for(int i = localVarScope.size() - 1; i >= 0; i--){
			ExpressionNode v = localVarScope.get(variable);
			if(v != null){
				return v;
			}
		}
		return null;
	}

	/**
	 * looks for the specified variable
	 * @param variable
	 * @return true if one of with this name exists.
	 */
	public boolean containsVar(String variable){
		for(Map<String, ExpressionNode> m: allVarScopes){
			if(m.containsKey(variable)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Tells how many scope levels exist iniside this rt
	 * @return
	 */
	public int getScopeLevels(){
		return allVarScopes.size();
	}
	
	/**
	 * Returns the specified scope level
	 * @param level - the top level is 0, the lowest level is getScopeLevels() - 1
	 * @return
	 */
	public Map<String, ExpressionNode> getScope(int level){
		return allVarScopes.get(level);
	}

	/**
	 * Sets a static variable value.
	 * 
	 * @param variable
	 *            The variable name.
	 * @param value
	 *            The variable value.
	 */
	private void setStaticVariable(String variable, double value) {
		if(staticVars.containsKey(value))
			staticVars.get(value).setValue(value);
		else
			staticVars.put(variable, new ExpressionNode(value));
	}

	/**
	 * Add a static variable NULL
	 */
	private void addStaticVariableNull() {
		if(!staticVars.containsKey("NULL"))
			staticVars.put("NULL", new ExpressionNode());
	}

	protected abstract class Operation {
		/**
		 * Name of the Operation
		 */
		protected String oper;
		/**
		 * Implementation for an Operation.
		 * 
		 * @param parameters
		 *            Parameters will be passed by the expression evaluator as a
		 *            {@link List} of {@link ExpressionNode} values.
		 * @return The function must return a new {@link ExpressionNode} value as a
		 *         computing result.
		 */
		public abstract void eval(List<ExpressionNode> parameters, ExpressionVar result) throws ExpressionException;		
	}

	/**
	 * Abstract definition of a supported expression function. A function is
	 * defined by a name, the number of parameters and the actual processing
	 * implementation.
	 */
	protected abstract class Function extends Operation{
		/**
		 * Number of parameters expected for this function. 
		 * <code>-1</code> denotes a variable number of parameters.
		 */
		private int numParams;

		/**
		 * Creates a new function with given name and parameter count.
		 * 
		 * @param name
		 *            The name of the function.
		 * @param numParams
		 *            The number of parameters for this function.
		 *            <code>-1</code> denotes a variable number of parameters.
		 */
		public Function(String name, int numParams) {
			this.oper = name.toUpperCase(Locale.ROOT);
			this.numParams = numParams;
		}

		public String getName() {
			return oper;
		}

		public int getNumParams() {
			return numParams;
		}

		public boolean numParamsVaries() {
			return numParams < 0;
		}
	}

	/**
	 * Abstract definition of a supported operator. An operator is defined by
	 * its name (pattern), precedence and if it is left- or right associative.
	 */
	protected abstract class Operator extends Operation{
		/**
		 * Operators precedence.
		 */
		private int precedence;
		/**
		 * Operator is left associative.
		 */
		private boolean leftAssoc;

		/**
		 * Creates a new operator.
		 * 
		 * @param oper
		 *            The operator name (pattern).
		 * @param precedence
		 *            The operators precedence.
		 * @param leftAssoc
		 *            <code>true</code> if the operator is left associative,
		 *            else <code>false</code>.
		 */
		public Operator(String oper, int precedence, boolean leftAssoc) {
			this.oper = oper;
			this.precedence = precedence;
			this.leftAssoc = leftAssoc;
		}

		public String getOper() {
			return oper;
		}

		public int getPrecedence() {
			return precedence;
		}

		public boolean isLeftAssoc() {
			return leftAssoc;
		}
	}

}
