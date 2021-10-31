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
 * 
 * derived from code by
 * Patrick Kochlik + Dennis Paul
 * Openframeworks
 *
 */

package com.tecartlab.tools.math.la;

import com.tecartlab.tools.Const;

/**
 * 
 * @author Martin Fr�hlich
 *
 * This class is heavily indebted by the openframeworks Quaternion class and code from
 * Patrick Kochlik + Dennis Paul. And others...
 * 
 */
public class Quaternionf {

	private float[] v_;

	// euler angle sequence
	private int eas1 = 2;
	private int eas2 = 3;
	private int eas3 = 1;

	/**
	 * Construct an empty Quaternion 
	 */
	public Quaternionf() {
		reset();
	}

	/**
	 * Construct a Quaternion with the specified element values.
	 * 
	 * @param args  Array of 4 imaginary elements: x, y, z, w
	 */
	public Quaternionf(float[] args) {
		reset();
		for(int i = 0; i < 4; i++)
			v_[i] = args[i];
	}

	/**
	 * Construct a Quaternion with the specified euler angles.
	 * @param xAxis
	 * @param yAxis
	 * @param zAxis
	 */
	public Quaternionf(float xAxis, float yAxis, float zAxis) {
		reset();
		setEulerAngles(xAxis, yAxis, zAxis);
	}

	/**
	 * Construct a Quaternion from a rotation around a vector
	 * @param angle
	 * @param vector
	 */
	public Quaternionf(float angle, Vector3f vector) {
		reset();
		setRotate(angle, vector);
	}

	/**
	 * Construct an empty Quaternion 
	 * 
	 * @param theX  imaginary x value
	 * @param theY  imaginary y value
	 * @param theW  imaginary z value
	 * @param theZ  imaginary w value
	 */
	public Quaternionf(float theX, float theY, float theZ, float theW) {
		reset();
		set(theX, theY, theZ, theW);
	}

	public float magnitude(){
		return (float)Math.sqrt(v_[0] * v_[0] + v_[1] * v_[1] + v_[2] * v_[2] + v_[3] * v_[3]);
	}

	public void normalize(){
		float magnitude = magnitude();
		v_[0] /= magnitude;
		v_[1] /= magnitude;
		v_[2] /= magnitude;
		v_[3] /= magnitude;
	}
	
	public void reset() {
		v_ = new float[4];
		for (int i = 0; i < 4; i++)
			v_[i] = 0.0f;
		v_[3] = 1.0f;
	}

	public float x(){
		return v_[0];
	}

	public float y(){
		return v_[1];
	}

	public float z(){
		return v_[2];
	}

	public float w(){
		return v_[3];
	}

	public void setX(float x){
		v_[0] = x;
	}

	public void setY(float y){
		v_[1] = y;
	}

	public void setZ(float z){
		v_[2] = z;
	}

	public void setW(float w){
		v_[3] = w;
	}

	/**
	 * Sets the Euler angles in the traditional way (yzx sequence)
	 * 
	 * @param heading
	 * @param attitude
	 * @param bank
	 */
	public final void setEuler(float bank, float heading, float attitude) {
	    // Assuming the angles are in radians.
	    float c1 = (float)Math.cos(heading * (float)Const.DEG_TO_RAD/2.f);
	    float s1 = (float)Math.sin(heading * (float)Const.DEG_TO_RAD/2.f);
	    float c2 = (float)Math.cos(attitude * (float)Const.DEG_TO_RAD/2.f);
	    float s2 = (float)Math.sin(attitude * (float)Const.DEG_TO_RAD/2.f);
	    float c3 = (float)Math.cos(bank * (float)Const.DEG_TO_RAD/2.f);
	    float s3 = (float)Math.sin(bank * (float)Const.DEG_TO_RAD/2.f);
	    float c1c2 = c1*c2;
	    float s1s2 = s1*s2;
	    v_[3] =c1c2*c3 - s1s2*s3;
	    v_[0] =c1c2*s3 + s1s2*c3;
	    v_[1] =s1*c2*c3 + c1*s2*s3;
	    v_[2] =c1*s2*c3 - s1*c2*s3;
	}
	
	/**
	 * Sets the Sequence of the calculation of the euler angles
	 * 	default = 2, 3, 1.
	 * 		1 = x axis
	 * 		2 = y axis
	 * 		3 = z axis
	 * 
	 * @param a1 
	 * @param a2
	 * @param a3
	 */
	public void setEulerAngleSequence(int a1, int a2, int a3){
		eas1 = a1;
		eas2 = a2;
		eas3 = a3;
	}
	
	/**
	 * Sets this Quaternion according to the euler angles in degrees
	 * 
	 * calc based on from http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
	 * 
	 * @param xAxis
	 * @param yAxis
	 * @param zAxis
	 */
	public void setEulerAngles(float xAxis, float yAxis, float zAxis) {
		//if the default sequence is set, use the faster method.
		if(eas1 == 2 && eas2 == 3 && eas3 == 1){
			setEuler(xAxis, yAxis, zAxis);
		}else{		
			Quaternionf q1 = new Quaternionf();
			Quaternionf q2 = new Quaternionf();
			Quaternionf q3 = new Quaternionf();
			switch(eas1){
			case 1:
				q1 = new Quaternionf((float)Math.sin(xAxis * (float)Const.DEG_TO_RAD / 2.0), 0.f, 0.f, (float)Math.cos(xAxis * (float)Const.DEG_TO_RAD / 2.0));
				break;
			case 2:
				q1 = new Quaternionf(0.f, (float)Math.sin(yAxis * (float)Const.DEG_TO_RAD / 2.0), 0.f, (float)Math.cos(yAxis * (float)Const.DEG_TO_RAD / 2.0));
				break;
			case 3:
				q1 = new Quaternionf(0.f, 0.f, (float)Math.sin(zAxis * (float)Const.DEG_TO_RAD / 2.0), (float)Math.cos(zAxis * (float)Const.DEG_TO_RAD / 2.0));
				break;
			}
			switch(eas2){
			case 1:
				q2 = new Quaternionf((float)Math.sin(xAxis * (float)Const.DEG_TO_RAD / 2.0), 0.f, 0.f, (float)Math.cos(xAxis * (float)Const.DEG_TO_RAD / 2.0));
				break;
			case 2:
				q2 = new Quaternionf(0.f, (float)Math.sin(yAxis * (float)Const.DEG_TO_RAD / 2.0), 0.f, (float)Math.cos(yAxis * (float)Const.DEG_TO_RAD / 2.0));
				break;
			case 3:
				q2 = new Quaternionf(0.f, 0.f, (float)Math.sin(zAxis * (float)Const.DEG_TO_RAD / 2.0), (float)Math.cos(zAxis * (float)Const.DEG_TO_RAD / 2.0));
				break;
			}
			switch(eas3){
			case 1:
				q3 = new Quaternionf((float)Math.sin(xAxis * (float)Const.DEG_TO_RAD / 2.0), 0.f, 0.f, (float)Math.cos(xAxis * (float)Const.DEG_TO_RAD / 2.0));
				break;
			case 2:
				q3 = new Quaternionf(0.f, (float)Math.sin(yAxis * (float)Const.DEG_TO_RAD / 2.0), 0.f, (float)Math.cos(yAxis * (float)Const.DEG_TO_RAD / 2.0));
				break;
			case 3:
				q3 = new Quaternionf(0.f, 0.f, (float)Math.sin(zAxis * (float)Const.DEG_TO_RAD / 2.0), (float)Math.cos(zAxis * (float)Const.DEG_TO_RAD / 2.0));
				break;
			}
			set(q1.multiply(q2).multiply(q3));
		}
	}
	
	public void set(float theW, Vector3f theVector3f) {
		v_[0] = theVector3f.x();
		v_[1] = theVector3f.y();
		v_[2] = theVector3f.z();
		v_[3] = theW;
	}

	public void set(float theX, float theY, float theZ, float theW) {
		v_[0] = theX;
		v_[1] = theY;
		v_[2] = theZ;
		v_[3] = theW;
	}

	public void set(Quaternionf theQuaternion) {
		v_[0] = theQuaternion.x();
		v_[1] = theQuaternion.y();
		v_[2] = theQuaternion.z();
		v_[3] = theQuaternion.w();
	}

	/**
	 * Set the elements of the Quat to represent a rotation of angle
	 * (degrees) around the axis (x,y,z)
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setRotate( float angle, float x, float y, float z ) {
		angle = angle * (float)Const.DEG_TO_RAD;
		float epsilon = 0.0000001f;

		float length = (float)Math.sqrt( x * x + y * y + z * z );
		if (length < epsilon) {
			// ~zero length axis, so reset rotation to zero.
			reset();
			return;
		}

		float inversenorm  = 1.0f / length;
		float coshalfangle = (float)Math.cos( 0.5f * angle );
		float sinhalfangle = (float)Math.sin( 0.5f * angle );

		setX(x * sinhalfangle * inversenorm);
		setY(y * sinhalfangle * inversenorm);
		setZ(z * sinhalfangle * inversenorm);
		setW(coshalfangle);
	}

	/**
	 * Set the elements of the Quat to represent a rotation of angle
	 * (degrees) around the axis vector
	 * 
	 * @param angle
	 * @param vec
	 */
	public void setRotate(float angle, Vector3f vec) {
		setRotate( angle, vec.x(), vec.y(), vec.z() );
	}

	/**
	 * Set the elements of the Quat to represent a rotation of 3 angles
	 * (degrees) around the three axis vectors
	 * 
	 * @param angle1
	 * @param axis1
	 * @param angle2
	 * @param axis2
	 * @param angle3
	 * @param axis3
	 */
	public void setRotate(float angle1, Vector3f axis1,
	                      float angle2, Vector3f axis2,
	                      float angle3, Vector3f axis3) {
	       Quaternionf q1 = new Quaternionf(); 
	       q1.setRotate(angle1,axis1);
	       Quaternionf q2 = new Quaternionf(); 
	       q2.setRotate(angle2,axis2);
	       Quaternionf q3 = new Quaternionf(); 
	       q3.setRotate(angle3,axis3);

	       
	       setMultiply(q1, q2);
	       multiply(q3);
	}
	
	/**
	 * This method returns the imaginary values (x, y, z) inside Vector
	 * @return
	 */
	public Vector3f getImaginaryQuaternion(){
		return new Vector3f(x(), y(), z());
	}
		
	/**
	 * get the Quaternion values in an array 
	 * 
	 * @return 	float[] array
	 */
	public float[] get(){
		return v_;
	}
	
	/**
	 * Set this quaternion as a result of the multiplication of the two specified quaternions
	 * @param theA
	 * @param theB
	 */
	public void setMultiply(Quaternionf theA, Quaternionf theB) {
		v_[0] = theA.w() * theB.x() + theA.x() * theB.w() + theA.y() * theB.z() - theA.z() * theB.y();
		v_[1] = theA.w() * theB.y() - theA.x() * theB.z() + theA.y() * theB.w() + theA.z() * theB.x();
		v_[2] = theA.w() * theB.z() + theA.x() * theB.y() - theA.y() * theB.x() + theA.z() * theB.w();
		v_[3] = theA.w() * theB.w() - theA.x() * theB.x() - theA.y() * theB.y() - theA.z() * theB.z();
	}

	/**
	 * Set this quaternion as a result of the multiplication of the two specified quaternions.
	 * 
	 * This method uses the factorized-method and is supposed to be faster, but doesnt give
	 * the same results (from the 4th digit behind the point)
	 * 
	 * @param theA
	 * @param theB
	 */
	public void setMultiplyFactorized(Quaternionf a, Quaternionf b) {

		float ww = (a.z() + a.x()) * (b.x() + b.y());
		float yy = (a.w() - a.y()) * (b.w() + b.z());
		float zz = (a.w() + a.y()) * (b.w() - b.z());
		float xx = ww + yy + zz;
		float qq = 0.5f * (xx + (a.z() - a.x()) * (b.x() - b.y()));

		v_[0] = qq - xx + (a.x() + a.w()) * (b.x() + b.w());
		v_[1] = qq - yy + (a.w() - a.x()) * (b.y() + b.z());
		v_[2] = qq - zz + (a.z() + a.y()) * (b.w() - b.x());
		v_[3] = qq - ww + (a.z() - a.y()) * (b.y() - b.z());
	}
	
	/**
	 * Multiply this quaternion with the specified quaternion and return this instance
	 * @param theA
	 * @return this instance
	 */
	public Quaternionf multiply(Quaternionf theA) {
		setMultiply(clone(), theA);
		return this;
	}
	
	/**
	 * Returns the result of the multiplication between this and the specified
	 * Quaternion without modifying this instance
	 * @param theA
	 * @return the new instance of a quaternion
	 */
	public Quaternionf multiplyMake(Quaternionf theA) {
		return clone().multiply(theA);
	}

	/**
	 * Scales this quaternion and returns this instance
	 * @param scale
	 * @return
	 */
	public Quaternionf scale(float scale){
		v_[0] *= scale;
		v_[1] *= scale;
		v_[2] *= scale;
		v_[3] *= scale;
		return this;
	}
	
	/**
	 * Conjugates this Quaternion (changes the sign of the x, y and z values)
	 * 
	 * @return this instance
	 */
	public Quaternionf conjugate(){
		v_[0] *= -1;
		v_[1] *= -1;
		v_[2] *= -1;
		return this;
	}
	
	/**
	 * Conjugates this Quaternion (changes the sign of the x, y and z values) and returns 
	 * the result as a new Instance without modifying this instance
	 * 
	 * @return new instance
	 */
	public Quaternionf conjugateMake(){
		return clone().conjugate();
	}
	
	public float length2(){
		return x() * x() + y() * y() + z() * z() + w() * w();
	}
	
	public float length(){
		return (float)Math.sqrt(length2());
	}
	
	public Quaternionf inverse(){
		conjugate().scale(length2());
		return this;
	}
	
	public Quaternionf inverseMake(){
		return clone().inverse();
	}
	
	/**
	 * Returns the Euler angles inside a vector in degrees
	 * 
	 * math based on http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
	 * 
	 * @return vector with euler angles [bank, heading, attitude]
	 */
	public Vector3f getEuler() {
		float heading, attitude, bank;
	    float sqw = w()*w();
	    float sqx = x()*x();
	    float sqy = y()*y();
	    float sqz = z()*z();
	    // if normalised is one, otherwise is correction factor
	    float unit = sqx + sqy + sqz + sqw; 
	    float test = x()*y() + z()*w();
		if (test > 0.499*unit) { // singularity at north pole
			heading = 2.0f * (float)Math.atan2(x(),w());
			attitude = (float)Const.HALF_PI;
			bank = 0;
		}else if (test < -0.499*unit) { // singularity at south pole
			heading = -2.0f * (float)Math.atan2(x(),w());
			attitude = -(float)Const.HALF_PI;
			bank = 0;
		}else{
		    heading = (float)Math.atan2(2.0f*y()*w()-2.0f*x()*z() , sqx - sqy - sqz + sqw);
			attitude = (float)Math.asin(2*test/unit);
			bank = (float)Math.atan2(2.0*x()*w()-2.0*y()*z() , -sqx + sqy - sqz + sqw);
		}
		return new Vector3f(bank * (float)Const.RAD_TO_DEG, heading * (float)Const.RAD_TO_DEG, attitude * (float)Const.RAD_TO_DEG);
	}
	
	/**
	 * get an exact copy of this quaternion
	 */
	public Quaternionf clone(){
		return new Quaternionf(x(), y(), z(), w());
	}

	 /**
	  * Create a string representation of this vector.
	  * 
	  * @return  String representing this vector.
	  */
	 public String toString()
	 {
		 return ("Quaternionf: [x=" + 
				 v_[0] + ", y=" + v_[1] + ", z=" + v_[2] + ", w=" + v_[3] + "]");
	 }

	public static void main(String[] args) {
        /* multiplying matrices */
	
		Quaternionf q1 = new Quaternionf(60, 45, 30);
		Quaternionf q2 = new Quaternionf();
		q2.setEulerAngleSequence(1, 2, 3);
		q2.setEulerAngles(60, 45, 30);

		System.out.println("q1 ="+q1.toString());
		System.out.println("q2 ="+q2.toString());

//		Quaternionf qr = q1.multiplyMake(q2.inverse());
		
//		Vector3f euler = qr.getEuler();
		
//		System.out.println("Euler: a="+euler.x() + " b=" + euler.y() + " c=" + euler.z());

	}

}
