package com.tecartlab.quescript.expression;

import java.util.ArrayList;

public class ExpressionVar {

	private ArrayList<ExpressionAtom> values;

	private boolean isNumeric = false;
	private boolean isArray = false;
	
	/**
	 * Creates an ExpressionEvaluated with a NULL - state
	 */
	private ExpressionVar(){
		values = new ArrayList<ExpressionAtom>();
		reset();
	}
	
	/**
	 * Creates an ExpressionEvaluated with a numeric - Value
	 */
	public ExpressionVar(double val){
		this();
		setValue(val);
	}
	
	/**
	 * Creates an ExpressionEvaluated with a String - Value
	 */
	public ExpressionVar(String val){
		this();
		setValue(val);
	}

	/**
	 * Creates an ExpressionEvaluated with an array
	 */
	public ExpressionVar(ArrayList<ExpressionAtom> val){
		this();
		setValues(val);
	}

	/**
	 *  update the flags to properly represent the type of variable
	 */
	protected void cleanup() {
		isNumeric = true;		
		for(ExpressionAtom atom: values) {
			if(!atom.isNumeric())
				isNumeric = false;
		}
		isArray = (values.size() > 1);
	}

	/**
	 * clears this instance to a null state, ready to be filled with add..
	 */
	protected void clear() {
		values.clear();
		isNumeric = false;
		isArray = false;
	}
	
	/**
	 * resets this instance to a numeric 0
	 */
	protected void reset() {
		values.clear();
		values.add(new ExpressionAtom(0));
		isNumeric = true;
		isArray = false;
	}
	
	/**
	 * start interpreting this Evaluated as array
	 */
	protected void makeArray() {
		isArray = true;
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
		values.get(0).setStringValue(val);
		isNumeric = false;
		isArray = false;
	}

	/**
	 * Set the value as a numeric
	 * @param val
	 */
	protected void setValue(double val) {
		values.get(0).setNumericValue(val);
		isNumeric = true;
		isArray = false;
	}

	/** 
	 * Set the value with Atome
	 * @param val
	 */
	protected void setValue(ExpressionAtom val) {
		values.set(0, val);
		isNumeric = val.isNumeric();
		isArray = false;
	}

	/**
	 * Set the value as an array
	 * @param val
	 */
	protected void setValues(ArrayList<ExpressionAtom> val) {
		values = val;
		this.cleanup();
	}

	/**
	 * set array index with numeric value
	 * @param index
	 * @param val
	 */
	protected void setArrayIndex(int index, double val) {
		if(index < getArraySize())
			this.values.get(index).setNumericValue(val);
	}

	/**
	 * set array index with string value
	 * @param index
	 * @param val
	 */
	protected void setArrayIndex(int index, String val) {
		if(index < getArraySize())
			this.values.get(index).setStringValue(val);
		isNumeric = false;
	}

	/**
	 * set array index with ExpressionAtom value
	 * @param index
	 * @param val
	 */
	protected void setArrayIndex(int index, ExpressionAtom val) {
		if(index < getArraySize())
			this.values.set(index, val);
	}

	/**
	 * adds a numeric value to the array
	 * @param val
	 */
	protected void addValue(double val) {
		values.add(new ExpressionAtom(val));
		isArray = true;
	}

	/** 
	 * adds a string value to the array
	 * @param val
	 */
	protected void addValue(String val) {
		values.add(new ExpressionAtom(val));
		isArray = true;
		isNumeric = false;
	}

	/**
	 * get the numerical value
	 * @return
	 */
	protected double getNumericValue() {
		return values.get(0).getNumericValue();
	}

	/**
	 * get the string value
	 * @return
	 */
	protected String getStringValue() {
		return values.get(0).getStringValue();
	}

	
	/**
	 * get the string value. If it is a number, formated as specified 
	 * @return
	 */
	protected String getStringValue(String format) {
		return values.get(0).getStringValue(format);
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
	protected int compareTo(ExpressionVar v2) {
		if(this.isNumeric && v2.isNumeric)
			return (getNumericValue() == v2.getNumericValue())? 0: (getNumericValue() > v2.getNumericValue())? 1: -1;
		else
			return (getStringValue().equals(v2.getStringValue()))? 0: 1;
	}
	
	/**
	 * parse contents to string
	 */
	public String toString() {
		if(isArray) {
			String retrn = "[";
			ExpressionAtom parameter;
			for (int i = 0; i < values.size(); i++) {
				parameter = values.get(i);
				retrn += parameter.getStringValue();
				if((i + 1) < values.size())
					retrn += ", ";
			}
			retrn += "]";
			return retrn;
		}
		return getStringValue();
	}
	
	/**
	 * clone this evaluated
	 */
	protected ExpressionVar clone() {
		ExpressionVar clone = new ExpressionVar(this.getClonedValues());
		return clone;
	}
	
}
