package com.tecartlab.quescript.expression;

public class ExpressionAtom {

	private boolean isNumeric = true;
	
	private double dValue;
	private String sValue;

	/**
	 * Creates an ExpressionAtom with a NULL - Value
	 */
	public ExpressionAtom(){
		isNumeric = false;
		this.dValue = 0;
		this.sValue = "null";
	}
	
	/**
	 * Creates an ExpressionAtom with a numeric value
	 * @param val
	 */
	public ExpressionAtom(double val){
		isNumeric = true;
		this.dValue = val;
		this.sValue = "";
	}

	/**
	 * Creates an ExpressionAtom with a numeric value
	 * @param val
	 */
	public ExpressionAtom(String val){
		isNumeric = false;
		this.dValue = 0;
		this.sValue = val;
	}

	protected double getNumericValue() {
		if(isNumeric) {
			return dValue;			
		} else {
			return 0;
		}
	}

	protected String getStringValue() {
		return getStringValue("%.2f");
	}

	protected String getStringValue(String format) {
		if(!isNumeric) {
			return sValue;
		} else {
			if(dValue == (long) dValue)
		        return String.format("%d", (long) dValue);
		    else
		        return String.format("%.2f", dValue);
		}
	}

	protected void setStringValue(String val) {
		isNumeric = false;
		sValue = val;
	}

	protected void setNumericValue(double val) {
		isNumeric = true;
		dValue = val;
	}
	
	/**
	 * Queries the type of atom
	 * @return
	 */
	protected boolean isNumeric() {
		return isNumeric;
	}
	
	/**
	 * returns a clone of this object
	 */
	protected ExpressionAtom clone() {
		ExpressionAtom clone = new ExpressionAtom();
		if(isNumeric)
			clone.setNumericValue(dValue);
		else
			clone.setStringValue(sValue);
		return clone;
	}
}
