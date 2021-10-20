package com.tecartlab.quescript.expression;

import java.util.ArrayList;

import com.tecartlab.quescript.expression.RunTimeEnvironment.Operation;

public class ExpressionEvaluated {

	private ArrayList<ExpressionAtom> values;

	private boolean isNumeric = false;
	private boolean isArray = false;
	private boolean isDirty = true;
	
	/**
	 * Creates an ExpressionEvaluated with a NULL - state
	 */
	private ExpressionEvaluated(){
		values = new ArrayList<ExpressionAtom>();
	}
	
	/**
	 * Creates an ExpressionEvaluated with a numeric - Value
	 */
	public ExpressionEvaluated(double val){
		this();
		setValue(val);
	}
	
	/**
	 * Creates an ExpressionEvaluated with a String - Value
	 */
	public ExpressionEvaluated(String val){
		this();
		setValue(val);
	}

	/**
	 * Creates an ExpressionEvaluated with an array
	 */
	public ExpressionEvaluated(ArrayList<ExpressionAtom> val){
		this();
		setValue(val);
	}

	/**
	 * check if value has been changed by any other mean than
	 * an evaluation
	 * @return
	 */
	protected boolean isDirty() {
		return isDirty;
	}

	/**
	 * indicate that the value has been evaluated
	 */
	protected void cleaned() {
		isNumeric = true;		
		for(ExpressionAtom atom: values) {
			if(!atom.isNumeric())
				isNumeric = false;
		}
		isArray = (values.size() > 1);
		isDirty = false;
	}
	
	/**
	 * resets this instance to a null state
	 */
	protected void clear() {
		values.clear();
		isNumeric = false;
		isArray = false;
		isDirty = true;
	}
	
	/**
	 * start interpreting this Evaluated as array
	 */
	protected void makeArray() {
		isArray = true;
		isDirty = true;
	}
	
	/**
	 * checks if evaluated is numeric
	 * @return
	 */
	protected boolean isNumeric() {
		return isNumeric;
	}

	/**
	 * checks if evaluated is array
	 * @return
	 */
	protected boolean isArray() {
		return isArray;
	}

	/**
	 * Set the value as a string
	 * @param val
	 */
	protected void setValue(String val) {
		values.clear();
		values.add(new ExpressionAtom(val));
		isNumeric = false;
		isArray = false;
		isDirty = true;
	}

	/**
	 * Set the value as a numeric
	 * @param val
	 */
	protected void setValue(double val) {
		values.clear();
		values.add(new ExpressionAtom(val));
		isNumeric = true;
		isArray = false;
		isDirty = true;
	}

	/** 
	 * Set the value with Atome
	 * @param val
	 */
	protected void setValue(ExpressionAtom val) {
		values.clear();
		values.add(val);
		isNumeric = val.isNumeric();
		isArray = false;
		isDirty = true;
	}

	/**
	 * Set the value as a array
	 * @param val
	 */
	protected void setValue(ArrayList<ExpressionAtom> val) {
		values.clear();
		int countString = 0;
		for(ExpressionAtom atom: val) {
			if(atom.isNumeric()) {
				values.add(new ExpressionAtom(atom.getNumericValue()));
			} else {
				values.add(new ExpressionAtom(atom.getStringValue()));	
				countString++;
			}
		}
		isNumeric = (countString == 0);
		isArray = true;
		isDirty = true;
	}

	/**
	 * set array index with numeric value
	 * @param index
	 * @param val
	 */
	protected void setArrayIndex(int index, double val) {
		if(index < getArraySize())
			this.values.get(index).setNumericValue(val);
		isDirty = true;
	}

	/**
	 * set array index with string value
	 * @param index
	 * @param val
	 */
	protected void setArrayIndex(int index, String val) {
		if(index < getArraySize())
			this.values.get(index).setStringValue(val);
		isDirty = true;
	}

	/**
	 * set array index with ExpressionAtom value
	 * @param index
	 * @param val
	 */
	protected void setArrayIndex(int index, ExpressionAtom val) {
		if(index < getArraySize())
			this.values.set(index, val);
		isDirty = true;
	}

	/**
	 * adds a numeric value to the array
	 * @param val
	 */
	protected void addValue(double val) {
		values.add(new ExpressionAtom(val));
		isArray = true;
		isDirty = true;
	}

	/** 
	 * adds a string value to the array
	 * @param val
	 */
	protected void addValue(String val) {
		values.add(new ExpressionAtom(val));
		isArray = true;
		isNumeric = false;
		isDirty = true;
	}

	/**
	 * get the numerical value
	 * @return
	 */
	protected double getNumericValue() {
		if(isNumeric && values.get(0).isNumeric()) {
			return values.get(0).getNumericValue();
		}
		return 0;
	}

	/**
	 * get the string value
	 * @return
	 */
	protected String getStringValue() {
		if(!isNumeric  && !values.get(0).isNumeric()) {
			return values.get(0).getStringValue();
		}
		return "null";
	}

	/**
	 * Get the atom value
	 * @return
	 */
	protected ExpressionAtom getValue(){
		return this.values.get(0);
	}

	/**
	 * get the array as reference
	 * @return array as reference
	 */
	protected ArrayList<ExpressionAtom> getValues(){
		return this.values;
	}

	/**
	 * get the array as values
	 * @return
	 */
	protected ArrayList<ExpressionAtom> getClonedValues(){
		ArrayList<ExpressionAtom> clone = new ArrayList<ExpressionAtom>();
		for(ExpressionAtom each: values) {
			clone.add(each.clone());
		}
		return clone;
	}

	/**
	 * get size
	 * @return
	 */
	protected int getArraySize() {
		return this.values.size();
	}
	

	/**
	 * gets the Atom at this index. 
	 * @param index
	 * @return a null-state atom if the index is out of bounds
	 */
	protected ExpressionAtom getArrayIndex(int index) {
		if(index < getArraySize())
			return this.values.get(index);
		return new ExpressionAtom();
	}
	
	/**
	 * Compares the numeric value of this instance with the argument.
	 * @param v2
	 * @returns
	 * -1 if this instance is < than the argument
	 * 0 if both are the same
	 * +1 if this instance is > than the argument
	 */
	protected int compareTo(ExpressionEvaluated v2) {
		if(this.isNumeric && v2.isNumeric)
			return (getNumericValue() == v2.getNumericValue())? 0: (getNumericValue() > v2.getNumericValue())? 1: -1;
		else
			return (getStringValue().equals(v2.getStringValue()))? 0: 1;
	}
	
	/**
	 * parse contents to string
	 */
	public String toString() {
		if(!isNumeric)
			return getStringValue();
		if(isArray) {
			String retrn = "[";
			for (ExpressionAtom parameter : values) {
				if(parameter.isNumeric())
					retrn += String.format("%.2f",parameter.getNumericValue());
				else
					retrn += parameter.getStringValue();
				retrn += ", ";
			}
			retrn += "]";
			return retrn;
		}
		// now it only can be numeric and not an array
		double dValue = values.get(0).getNumericValue();
	    if(dValue == (long) dValue)
	        return String.format("%.2f", dValue);
	    else
	        return String.format("%s", dValue);
	}
	
	/**
	 * clone this evaluated
	 */
	protected ExpressionEvaluated clone() {
		return new ExpressionEvaluated(this.values);
	}
	
}
