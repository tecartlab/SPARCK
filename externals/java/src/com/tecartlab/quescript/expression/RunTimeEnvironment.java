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
	private ArrayList<Map<String, ExpressionVar>> allVarScopes;
	
	/**
	 * local variable scope
	 */
	private HashMap<String, ExpressionVar> localVarScope;
	
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
	protected Map<String, ExpressionVar> staticVars;

	public RunTimeEnvironment(RunTimeEnvironment rt) {
		operators = rt.operators;
		functions = rt.functions;
		staticVars = rt.staticVars;
		allVarScopes = new ArrayList<Map<String, ExpressionVar>>();
		for(Map<String, ExpressionVar> m: rt.allVarScopes)
			allVarScopes.add(m);
		localVarScope = new HashMap<String, ExpressionVar>();
		allVarScopes.add(localVarScope);		
	}

	public RunTimeEnvironment() {
		allVarScopes = new ArrayList<Map<String, ExpressionVar>>();
		operators = new HashMap<String, Operator>();
		functions = new HashMap<String, Function>();
		staticVars = new HashMap<String, ExpressionVar>();
		localVarScope = new HashMap<String, ExpressionVar>();
		allVarScopes.add(localVarScope);
		
		addOperator(new Operator("+", 20, true) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return parameters.get(0).add(parameters.get(1));
			}
		});
		addOperator(new Operator("-", 20, true) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(parameters.get(0).getNumberValue() - parameters.get(1).getNumberValue());
			}
		});
		addOperator(new Operator("*", 30, true) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(parameters.get(0).getNumberValue() * parameters.get(1).getNumberValue());
			}
		});
		addOperator(new Operator("/", 30, true) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(parameters.get(0).getNumberValue() / parameters.get(1).getNumberValue());
			}
		});
		addOperator(new Operator("%", 30, true) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(parameters.get(0).getNumberValue() % parameters.get(1).getNumberValue());
			}
		});
		addOperator(new Operator("^", 40, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.pow(parameters.get(0).getNumberValue(), parameters.get(1).getNumberValue()));
			}
		});
		
		addOperator(new Operator("[]", 50, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				ExpressionVar array = parameters.get(0);
				ExpressionVar indice = parameters.get(1);
				if(array.isArray){
					if(indice.isNumber){
						int index = (int)indice.getNumberValue();
						if(index < array.getParamSize()){
							array.arrayIndex = index;
							return array.getParam(index);
						} 					
						throw new ExpressionException("Array index out of bounds exception: " + index + " " + array.getParamSize());	
					} 
					throw new ExpressionException("Array index invalid number: " + parameters.get(1).getStringValue());
				}
				throw new ExpressionException("Invalid operation: " + parameters.get(0).getStringValue() + " is not an array.");
			}
		});

		addOperator(new Operator("&&", 4, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				boolean b1 = !parameters.get(0).equals(ExpressionVar.ZERO);
				boolean b2 = !parameters.get(1).equals(ExpressionVar.ZERO);
				return b1 && b2 ? ExpressionVar.ONE : ExpressionVar.ZERO;
			}
		});

		addOperator(new Operator("||", 2, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				boolean b1 = !parameters.get(0).equals(ExpressionVar.ZERO);
				boolean b2 = !parameters.get(1).equals(ExpressionVar.ZERO);
				return b1 || b2 ? ExpressionVar.ONE : ExpressionVar.ZERO;
			}
		});

		addOperator(new Operator(">", 10, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return parameters.get(0).compareTo(parameters.get(1)) == 1 ? ExpressionVar.ONE : ExpressionVar.ZERO;
			}
		});

		addOperator(new Operator("gt", 10, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				return operators.get(">").eval(parameters);
			}
		});

		addOperator(new Operator(">=", 10, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return parameters.get(0).compareTo(parameters.get(1)) >= 0 ? ExpressionVar.ONE : ExpressionVar.ZERO;
			}
		});

		addOperator(new Operator("ge", 10, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				return operators.get(">=").eval(parameters);
			}
		});

		addOperator(new Operator("<", 10, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return parameters.get(0).compareTo(parameters.get(1)) == -1 ? ExpressionVar.ONE
						: ExpressionVar.ZERO;
			}
		});
		
		addOperator(new Operator("lt", 10, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				return operators.get("<").eval(parameters);
			}
		});

		addOperator(new Operator("<=", 10, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return parameters.get(0).compareTo(parameters.get(1)) <= 0 ? ExpressionVar.ONE : ExpressionVar.ZERO;
			}
		});
		
		addOperator(new Operator("le", 10, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException{
				return operators.get("<=").eval(parameters);
			}
		});

		addOperator(new Operator("=", 7, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException{
				if(parameters.size() == 2 && parameters.get(0).isUsedAsVariable){
					if(parameters.get(0).isArray){
						if(parameters.get(1).isArray){
							// if we want to assign an array to a variable inside an expr node:
							return parameters.get(0).copyFrom(parameters.get(1));
						} else {
							// assigned = assignee
							// we need to evaluate the array to assigned again, incase the same array
							// is also part of the evaluation tree inside the assignee
							parameters.get(0).eval();
							// this will set the the arrayIndex correctly.
							int arrayIndex = parameters.get(0).getParam(0).arrayIndex;
							// if it is an array we dive into the parameter:
							// 		parameters.get(0) 							        // gets us the assigned var
							//						 .getParam(0) 				        // gets us the array var
							//                                   .getParam(arrayIndex)  // gets us the individual entry
							//
							return parameters.get(0).getParam(0).getParam(arrayIndex).set(parameters.get(1));
						}
					} else {
						return parameters.get(0).set(parameters.get(1));
					}
				}	
				throw new ExpressionException("= can only assign to a variable");
			}
		});

		addOperator(new Operator("==", 7, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				return parameters.get(0).compareTo(parameters.get(1)) == 0 ? ExpressionVar.ONE : ExpressionVar.ZERO;
			}
		});

		addOperator(new Operator("!=", 7, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return parameters.get(0).compareTo(parameters.get(1)) != 0 ? ExpressionVar.ONE : ExpressionVar.ZERO;
			}
		});
		
		addOperator(new Operator("<>", 7, false) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				return operators.get("!=").eval(parameters);
			}
		});

		addFunction(new Function("LENGTH", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				if(parameters.get(0).isArray){
					return new ExpressionVar(parameters.get(0).getParamSize());
				}
				return ExpressionVar.ZERO;
			}
		});
		addFunction(new Function("NOT", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				boolean zero = parameters.get(0).compareTo(ExpressionVar.ZERO) == 0;
				return zero ? ExpressionVar.ONE : ExpressionVar.ZERO;
			}
		});

		addFunction(new Function("IF", 3) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return (parameters.get(0).getNumberValue() == 1) ? parameters.get(1) : parameters.get(2);
			}
		});

		addFunction(new Function("RANDOM", 0) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.random());
			}
		});
		addFunction(new Function("SIN", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.sin(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("COS", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.cos(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("TAN", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.tan(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("ASIN", 1) { // added by av
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.asin(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("ACOS", 1) { // added by av
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.acos(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("ATAN", 1) { // added by av
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.atan(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("SINH", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.sinh(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("COSH", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.cosh(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("TANH", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.tanh(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("RAD", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.toRadians(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("DEG", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.toDegrees(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("ARRAY", -1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				if (parameters.size() == 0) {
					throw new ExpressionException("ARRAY requires at least one parameter");
				}
				return new ExpressionVar(new ArrayList<ExpressionVar>(parameters));
			}
		});
		addFunction(new Function("CLONE", -1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
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
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				if (parameters.size() == 0) {
					throw new ExpressionException("MAX requires at least one parameter");
				}
				ExpressionVar max = null;
				for (ExpressionVar parameter : parameters) {
					if (max == null || parameter.compareTo(max) > 0) {
						max = parameter;
					}
				}
				return max;
			}
		});
		addFunction(new Function("MIN", -1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				if (parameters.size() == 0) {
					throw new ExpressionException("MIN requires at least one parameter");
				}
				ExpressionVar min = null;
				for (ExpressionVar parameter : parameters) {
					if (min == null || parameter.compareTo(min) < 0) {
						min = parameter;
					}
				}
				return min;
			}
		});
		addFunction(new Function("ABS", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.abs(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("LOG", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.log(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("LOG10", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.log10(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("ROUND", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.round(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("FLOOR", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return  new ExpressionVar(Math.floor(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("CEILING", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) {
				return new ExpressionVar(Math.ceil(parameters.get(0).getNumberValue()));
			}
		});
		addFunction(new Function("SQRT", 1) {
			@Override
			public ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException {
				if (parameters.get(0).compareTo(ExpressionVar.ZERO) == 0) {
					return new ExpressionVar(0);
				}
				if (Math.signum(parameters.get(0).getNumberValue()) < 0) {
					throw new ExpressionException(
							"Argument to SQRT() function must not be negative");
				}

				return new ExpressionVar(Math.sqrt(parameters.get(0).getNumberValue()));
			}
		});

		setStaticVariable("NULL", ExpressionVar.NULL);
		setStaticVariable("PI", ExpressionVar.PI);
		setStaticVariable("TRUE", ExpressionVar.ONE);
		setStaticVariable("FALSE", ExpressionVar.ZERO);
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
	public ExpressionVar setVariable(String variable, ExpressionVar value, int scope) {
		if(scope < allVarScopes.size()){
			ExpressionVar v = getVar(variable);
			if(v != null){
				return v.set(value);
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
	public ExpressionVar setVariable(String variable, ExpressionVar value) {
		ExpressionVar v = getVar(variable);
		if(v != null){
			return v.set(value);
		} else {
			localVarScope.put(variable, value.setUsedAsVariable());
			return value;
		}
	}

	/**
	 * Sets this variable inside the local scope no matter if there are other variables of the 
	 * same name in higher scopes. If there is already a variable, it will inject the content of the
	 * value into its Expression
	 * @param variable String identifier
	 * @param value ExpressionVar object
	 * @return the reference object of this variable
	 */
	public ExpressionVar setLocalVariable(String variable, ExpressionVar value) {
		ExpressionVar v = localVarScope.get(variable);
		if(v != null){
			if(value.isArray){
				return v.copyFrom(value);
			} else {
				return v.set(value);
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
	public ExpressionVar setLocalVariable(String variable, double value) {
		ExpressionVar v = localVarScope.get(variable);
		if(v != null){
			return v.setValue(value);
		} else {
			v = new ExpressionVar(value).setUsedAsVariable();
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
	public ExpressionVar setLocalVariable(String variable, String value) {
		ExpressionVar v = localVarScope.get(variable);
		if(v != null){
			return v.setValue(value);
		} else {
			v = new ExpressionVar(value).setUsedAsVariable();
			localVarScope.put(variable, v);
			return v;
		}
	}
	
	/**
	 * Returns the specified variable, looks first at the local scope and keeps on moving upwards.
	 * @param variable
	 * @return null if none of this name exists.
	 */
	public ExpressionVar getVar(String variable){
		for(int i = allVarScopes.size() - 1; i >= 0; i--){
			ExpressionVar v = allVarScopes.get(i).get(variable);
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
	public ExpressionVar getLocalVar(String variable){
		for(int i = localVarScope.size() - 1; i >= 0; i--){
			ExpressionVar v = localVarScope.get(variable);
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
		for(Map<String, ExpressionVar> m: allVarScopes){
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
	public Map<String, ExpressionVar> getScope(int level){
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
	private void setStaticVariable(String variable, ExpressionVar value) {
		if(staticVars.containsKey(value))
			staticVars.get(value).set(value);
		else
			staticVars.put(variable, value);
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
		 *            {@link List} of {@link ExpressionVar} values.
		 * @return The function must return a new {@link ExpressionVar} value as a
		 *         computing result.
		 */
		public abstract ExpressionVar eval(List<ExpressionVar> parameters) throws ExpressionException;		
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
