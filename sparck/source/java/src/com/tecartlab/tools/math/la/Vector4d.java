/*
 * Copyright (c) 2013 maybites.ch / openframeworks.org
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal 
 *  in the Software without restriction, including without limitation the rights 
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 *  copies of the Software, and to permit persons to whom the Software is furnished
 *  to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tecartlab.tools.math.la;

/**
 * Implementation of a 4-element vector suited for use with
 * Matrix4x4
 */   
public class Vector4d
{
	private double[] v_;

	public static int X = 0;
	public static int Y = 1;
	public static int Z = 2;
	public static int W = 3;

	private void initialize()
	{
		v_ = new double[4];
		for (int i = 0; i < 4; i++)
			v_[i] = 0.0;
	}



	/**
	 * Create a default 4-element vector (all elements set to 0.0).
	 */
	public Vector4d()
	{
		initialize();
	}



	/**
	 * Create a 4-element vector with the specified values.
	 * 
	 * @param v1  1st element.
	 * @param v2  2nd element.
	 * @param v3  3rd element.
	 * @param v4  4th element
	 */
	public Vector4d (double v1, double v2, double v3, double v4)
	{
		initialize();
		set (v1, v2, v3, v4);
	}



	/**
	 * Construct a 4-element vector as a copy of the specified vector.
	 * 
	 * @param Vector4d
	 */
	public Vector4d (Vector4d Vector4d)
	{
		initialize();
		set (Vector4d);
	}

	/**
	 * Construct a  4-element vector from a 3-element vector with 
	 * @param vector3
	 */
	public Vector4d(Vector3f vector3){
		initialize();
		set(vector3.x(), vector3.y(), vector3.z(), 1);
	}


	/**
	 * Set the elements of this vector.
	 * 
	 * @param v1  1st element.
	 * @param v2  2nd element.
	 * @param v3  3rd element.
	 * @param v4  4th element
	 */
	public void set (double v1, double v2, double v3, double v4)
	{
		v_[0] = v1;
		v_[1] = v2;
		v_[2] = v3;
		v_[3] = v4;
	}



	/**
	 * Set the elements of this vector according to the specified vector.
	 * 
	 * @param vector  Vector to copy.
	 */
	public void set (Vector4d vector)
	{
		for (int i = 0; i < 4; i++)
			v_[0] = vector.v_[i];
	}


	/**
	 * Check if this 4-element vector equals the specified object.
	 * 
	 * @param object
	 * @return  TRue if the two equals, false otherwise.
	 */
	public boolean equals(Object object){
		Vector4d vector = (Vector4d) object;

		return v_[0] == vector.v_[0] &&
				v_[1] == vector.v_[1] &&
				v_[2] == vector.v_[2] &&
				v_[3] == vector.v_[3];
	}

	/**
	 * Return the i'th element of this vector.
	 * 
	 * @param i  Index of element to get (first is 0).
	 * @return   i'th element of this vector.
	 */
	public double getElement (int i){
		return v_[i];
	}

	public double x(){
		return v_[0];
	}

	public double y(){
		return v_[1];
	}
	
	public double z(){
		return v_[2];
	}
	
	public double w(){
		return v_[3];
	}


	/**
	 * Set the i'th element of this vector.
	 * 
	 * @param i  Index of element to set (first is 0).
	 * @param value  Value to set.
	 */
	public void setElement (int i, double value)
	{
		v_[i] = value;
	}


	/**
	 * Normalizes this Vector: Divides the w-value through x, y, and z
	 * @return this instance
	 */
	public Vector4d normalize(){
		v_[0] /= v_[3];
		v_[1] /= v_[3];
		v_[2] /= v_[3];
		v_[3] = 1;
		return this;
	}

	/**
	 * Create a string representation of this vector.
	 * 
	 * @return  String representing this vector.
	 */
	public String toString()
	{
		return ("Vector4d: [" + 
				v_[0] + "," + v_[1] + "," + v_[2] + "," + v_[3] + "]");
	}
}

