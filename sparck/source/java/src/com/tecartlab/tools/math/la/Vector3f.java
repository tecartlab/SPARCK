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

package com.tecartlab.tools.math.la;

import java.io.Serializable;
import java.util.Random;

/**
 * Implementation of a 3-element Vector suited for use with
 * Matrix4x4
 */
public class Vector3f
{

	  protected float[] v_;

	  /**
	   * threshold to maintain the minimal difference between two vectors before
	   * they are almost the same
	   */
	  protected static final float ALMOST_THRESHOLD = 0.001f;

	  /**
	   * Utility for random
	   */
	  protected static final Random generator = new Random();

	  protected void initialize()
	  {
	    v_ = new float[3];
	    for (int i = 0; i < 3; i++)
	      v_[i] = 0.0f;
	  }

	/**
	 * Create a default 3-element vector (all elements set to 0.0).
	 */
	public Vector3f()
	{
		initialize();
	}

	/**
	 * Create a 3-element vector with the specified values.
	 *
	 * @param v1  1st element.
	 * @param v2  2nd element.
	 * @param v3  3rd element.
	 */
	public Vector3f (float v1, float v2, float v3)
	{
	    initialize();
	    set (v1, v2, v3);
	}

	/**
	 * Create a 3-element vector with the specified values.
	 *
	 * @param args  3 element float array.
	 */
	public Vector3f (float[] args)
	{
	    initialize();
	    set (args[0], args[1], args[2]);
	}

	/**
	 * Construct a 3-element vector as a copy of the specified Vector.
	 *
	 * @param vector
	 */
	public Vector3f (Vector3f vector)
	{
	    initialize();
		set(vector);
	}

	/**
	 * Construct a 3-element vector from the specified 4-element Vector. It disregards
	 * the w-value
	 *
	 * @param vector
	 */
	public Vector3f (Vector4d vector)
	{
	    initialize();
		set((float)vector.x(), (float)vector.y(),(float) vector.z());
	}


	 /**
	   * Set the elements of this vector.
	   *
	   * @param v1  1st element.
	   * @param v2  2nd element.
	   * @param v3  3rd element.
	   */
	  public void set (float v1, float v2, float v3)
	  {
	    v_[0] = v1;
	    v_[1] = v2;
	    v_[2] = v3;
	  }


	/**
	 * Set the elements of this vector according to the specified vector.
	 *
	 * @param vector  Vector to copy.
	 */
	public void set (Vector3f vector)
	{
		for (int i = 0; i < 3; i++)
			v_[i] = vector.v_[i];
	}


	  /**
	   * Return the i'th element of this Vector.
	   *
	   * @param i  Index of element to get (first is 0).
	   * @return   i'th element of this point.
	   */
	  public float getElement (int i)
	  {
	    return v_[i];
	  }

	  /**
	   * Set the i'th element of this vector.
	   *
	   * @param i  		Index of element to set (first is 0).
	   * @param value   Value to set.
	   */
	  public void setElement (int i, float value)
	  {
	    v_[i] = value;
	  }

	  public final float x(){
		  return v_[0];
	  }

	  public final float y(){
		  return v_[1];
	  }

	  public final float z(){
		  return v_[2];
	  }

	  public final void setX(float x){
		  v_[0] = x;
	  }

	  public final void setY(float y){
		  v_[1] = y;
	  }

	  public final void setZ(float z){
		  v_[2] = z;
	  }

	  public final float[] get() {
	      return v_;
	  }

	  public final boolean isNaN() {
	      if (Float.isNaN(v_[0]) || Float.isNaN(v_[1]) || Float.isNaN(v_[2])) {
	          return true;
	      } else {
	          return false;
	      }
	  }


	/**
	 * Returns a new Vector with the result of the addition of the
	 * specified Vectors with this Vector. This instance will NOT be
	 * modified
	 *
	 * Usage: Vector3f newVec = thisVector.add(otherVector)
	 *
	 * @param theVector	the Vector to be added
	 * @return the addition
	 */
	public final Vector3f addMake(Vector3f theVector) {
		return clone().add(theVector);
	}

	/**
	 * Adds the specified Vector to this instance and returns it. This method modifies this instance.
	 *
	 * @param theVector	the Vector to be added
	 * @return the addition
	 */
	public final Vector3f add(Vector3f theVector) {
		setAdd(this, theVector);
		return this;
	}

	/**
	 * Replace this Vector with the result of the addition of the two
	 * specified Vectors. This method modifies this instance.
	 *
	 * Usage 1: add the otherVector from thisVector and apply the result to thisVector:
	 * 	thisVector.sub(thisVector, otherVector);
	 *
	 * Usage 2: add the otherVector from firstVector and apply the result to thisVector:
	 * 	thisVector.sub(firstVector, otherVector);
	 *
	 * @param theVectorA	of class Vector3f
	 * @param theVectorB	of class Vector3f
	 */
	public final void setAdd(Vector3f theVectorA, Vector3f theVectorB) {
		v_[0] = theVectorA.v_[0] + theVectorB.v_[0];
		v_[1] = theVectorA.v_[1] + theVectorB.v_[1];
		v_[2] = theVectorA.v_[2] + theVectorB.v_[2];
	}

	/**
	 * Replace this Vector with the result of the subtraction of the two
	 * specified Vectors). This method modifies this instance.
	 *
	 * Usage 1: substract the otherVector from thisVector and apply the result to thisVector:
	 * 	thisVector.sub(thisVector, otherVector);
	 *
	 * Usage 2: substract the otherVector from firstVector and apply the result to thisVector:
	 * 	thisVector.sub(firstVector, otherVector);
	 *
	 * @param theVectorA	of class Vector3f
	 * @param theVectorB	of class Vector3f
	 */
	public final void setSub(Vector3f theVectorA, Vector3f theVectorB) {
		v_[0] = theVectorA.v_[0] - theVectorB.v_[0];
		v_[1] = theVectorA.v_[1] - theVectorB.v_[1];
		v_[2] = theVectorA.v_[2] - theVectorB.v_[2];
	}


	/**
	 * Substract the specified vector from this instance and return it. This method modifies this instance.
	 *
	 * @param theVectorA	of class Vector3f
	 * @return this instance
	 */
	public final Vector3f sub(Vector3f theVectorA) {
		v_[0] -= theVectorA.v_[0];
		v_[1] -= theVectorA.v_[1];
		v_[2] -= theVectorA.v_[2];
		return this;
	}

	/**
	 * Returns a new Vector with the result of the subtraction of the
	 * specified Vectors from this Vector. This instance will NOT be
	 * modified
	 *
	 * Usage: Vector3f newVec = thisVector.sub(otherVector)
	 *
	 * @param theVector	the Vector to be substracted
	 * @return the subtraction
	 */
	public final Vector3f subMake(Vector3f theVector) {
		return clone().sub(theVector);
	}


	/**
	 * Use this method to negate a vector. The result of the negation is vector
	 * with the same magnitude but opposite direction. Mathematically the
	 * negation is the additive inverse of the vector. The sum of a value and
	 * its additive inerse is always zero.
	 *
	 * @related scale ( )
	 */
	public final void negate() {
		scale( -1);
	}



	/**
	 *
	 * @param theVector
	 *            Vector3f: vector with the value each coord is scaled with
	 * @return this instance
	 */
	public final Vector3f scale(final Vector3f theVector) {
		scale(theVector.v_[0], theVector.v_[1], theVector.v_[2]);
		return this;
	}


	/**
	 *	Scales this instance with the specified values
	 *
	 * @param theX float
	 * @param theY float
	 * @param theZ float
	 * @return this instance
	 */
	public final Vector3f scale(float theX, float theY, float theZ) {
		v_[0] *= theX;
		v_[1] *= theY;
		v_[2] *= theZ;
		return this;
	}

	/**
	 * Use this method to scale a vector. To scale a vector each of its
	 * coordinates is multiplied with the given scalar. The result is a vector
	 * that is parallel with its origin, with a different length and possibly
	 * opposite direction.<br>
	 * You can also scale a vector with another vector, in this case each coord
	 * of the vector is multiplied with related coord of the given vector.<br>
	 * Another possibillity is to set and scale the vector, this means the
	 * vector is set to the given vector multiplied with the given scalar.
	 *
	 * @param scalar float
	 * @return this instance
	 */
	public final Vector3f scale(float scalar) {
		scale(scalar, scalar, scalar);
		return this;
	}

	/**
	 *	Scales this instance with the scalar and returns a new Vector.
	 *  This method does NOT modify this instance
	 *
	 * @param scalar float
	 * @return a new instance
	 */
	public final Vector3f scaleMake(float scalar) {
		return clone().scale(scalar);
	}

	/**
	 *	Scales this instance with the specified values and returns a new Vector.
	 *  This method does NOT modify this instance
	 *
	 * @param theX float
	 * @param theY float
	 * @param theZ float
	 * @return a new instance
	 */
	public final Vector3f scaleMake(float theX, float theY, float theZ) {
		return clone().scale(theX, theY, theZ);
	}

	/**
	 * @param theScalar
	 *            float or int: value the given vector is scaled with
	 * @param theVector
	 *            Vector3f: vector the vector is set to
	 * @return this instance
	 */
	public final Vector3f scale(final float theScalar, final Vector3f theVector) {
		v_[0] = theScalar * theVector.v_[0];
		v_[1] = theScalar * theVector.v_[1];
		v_[2] = theScalar * theVector.v_[2];
		return this;
	}

	/**
	 * Dividing is the the same as scaling
	 *
	 * @param theDivisor
	 * @return this instance
	 */
	public final Vector3f divide(final float theDivisor) {
		scale(1f / theDivisor);
		return this;
	}

	/**
	 * Dividing is the the same as scaling
	 *
	 * @param theDivisor
	 * @return this instance
	 */
	public final Vector3f divide(final Vector3f theVector) {
		v_[0] /= theVector.v_[0];
		v_[1] /= theVector.v_[1];
		v_[2] /= theVector.v_[2];
		return this;
	}

	public final float lengthSquared() {
		return v_[0] * v_[0] + v_[1] * v_[1] + v_[2] * v_[2];
	}

	/**
	 * Use this method to calculate the length of a vector, the length of a
	 * vector is also known as its magnitude. Vectors have a magnitude and a
	 * direction. These values are not explicitly expressed in the vector so
	 * they have to be computed.
	 *
	 * @return float: the length of the vector
	 */
	public final float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	/**
	 * See length()
	 *
	 * @return float: the length of the vector
	 */
	public final float magnitude() {
		return length();
	}

	/**
	 * Replace this Vector with the result of the crossproduct of the two
	 * specified Vectors. This method modifies this instance.
	 *
	 * Usage 1: cross the otherVector with thisVector and apply the result to thisVector:
	 * 	thisVector.cross(thisVector, otherVector);
	 *
	 * Usage 2: cross the otherVector with firstVector and apply the result to thisVector:
	 * 	thisVector.cross(firstVector, otherVector);
	 *
	 * @param theVectorA
	 * @param theVectorB
	 */
	public final void setCross(final Vector3f theVectorA, final Vector3f theVectorB) {
		set(theVectorA.v_[1] * theVectorB.v_[2] - theVectorA.v_[2] * theVectorB.v_[1],
			theVectorA.v_[2] * theVectorB.v_[0] - theVectorA.v_[0] * theVectorB.v_[2],
			theVectorA.v_[0] * theVectorB.v_[1] - theVectorA.v_[1] * theVectorB.v_[0]);
	}


	/**
	 * This vector will be crossed with the specified vector and the result will be
	 * stored in this instance. The cross product returns a
	 * vector standing vertical on the two vectors.
	 *
	 * @param theVector	the other vector
	 * @return this modified instance
	 */
	public Vector3f cross(final Vector3f theVector) {
		setCross(clone(), theVector);
		return this;
	}

	/**
	 * Returns the cross product of this instance and the
	 * specified vector. The cross product returns a
	 * vector standing vertical on the two vectors. This instance will NOT be
	 * modified
	 *
	 * @param theVector	the other vector
	 * @return the cross product
	 */
	public Vector3f crossMake(final Vector3f theVector) {
		return clone().cross(theVector);
	}

	/**
	 * Returns the cosine of the angle between two vectors.
	 *
	 * @param theVector	the other vector
	 * @return float	cosine of two vectors
	 */
	public final float angle(Vector3f theVector) {
		return this.dot(theVector) / (magnitude() * theVector.magnitude());
	}

	/**
	 * Returns the dot product of two vectors. The dot product is the cosinus of
	 * the angle between two vectors
	 *
	 * @param theVector	 the other vector
	 * @return float dot product of two vectors
	 */
	public final float dot(Vector3f theVector) {
		return v_[0] * theVector.v_[0] + v_[1] * theVector.v_[1] + v_[2] * theVector.v_[2];
	}

	/**
	 * Returns the, by the provided matrix, transformed instance of this vector.
	 * @param matrix
	 * @return transformed instance
	 */
	public Vector3f transform(Matrix4x4f matrix){
		return matrix.multiply(this);
	}

	/**
	 * Returns the, by the provided matrix, transformed clone of this vector. This
	 * instance stays unchanged
	 * @param matrix
	 * @return cloned and transformed vector of this instance
	 */
	public Vector3f transformMake(Matrix4x4f matrix){
		return matrix.multiply(this.clone());
	}

	/**
	 * Sets the vector to the given one and norms it to the length of 1
	 *
	 * @param theVector
	 * @return normalized vector
	 */
	public final Vector3f normalize(Vector3f theVector) {
		set(theVector);
		normalize();
		return this;
	}

	/**
	 * Norms the vector to the length of 1 and returns this instance
	 * @return normalized vector
	 */
	public final Vector3f normalize() {
		float inverseMag = 1.0f / magnitude();
		v_[0] *= inverseMag;
		v_[1] *= inverseMag;
		v_[2] *= inverseMag;
		return this;
	}

	/**
	 * Returns a normalized copy of this vector
	 * @return normalized vector
	 */
	public final Vector3f normalizeMake() {
		return clone().normalize();
	}


	/**
	 * Interpolates between this vector and the given vector by a given blend
	 * value. The blend value has to be between 0 and 1. A blend value 0 would
	 * change nothing, a blend value 1 would set this vector to the given one.
	 *
	 * @param blend float, blend value for interpolation
	 * @param i_vector Vector3f, other vector for interpolation
	 */
	public void interpolate(final float blend, final Vector3f i_vector) {
		v_[0] = v_[0] + blend * (i_vector.v_[0] - v_[0]);
		v_[1] = v_[1] + blend * (i_vector.v_[1] - v_[1]);
		v_[2] = v_[2] + blend * (i_vector.v_[2] - v_[2]);
	}


	/**
	 * Sets a position randomly distributed inside a sphere of unit radius
	 * centered at the origin. Orientation will be random and length will range
	 * between 0 and 1
	 */
	public void randomize() {
		v_[0] = generator.nextFloat() * 2.0F - 1.0F;
		v_[1] = generator.nextFloat() * 2.0F - 1.0F;
		v_[2] = generator.nextFloat() * 2.0F - 1.0F;
		normalize();
	}

	/**
	 * Returns the squared distance of the specified vector to this vector
	 * @param theVector
	 * @return the squared distance
	 */
	public final float distanceSquared(Vector3f theVector) {
		float dx = v_[0] - theVector.v_[0];
		float dy = v_[1] - theVector.v_[1];
		float dz = v_[2] - theVector.v_[2];
		return dx * dx + dy * dy + dz * dz;
	}

	/**
	 * Returns the distance of the specified vector to this vector
	 * @param theVector
	 * @return the distance
	 */
	public final float distance(Vector3f theVector) {
		return (float) Math.sqrt(distanceSquared(theVector));
	}

	public final void min(Vector3f theMin) {
		if (v_[0] < theMin.v_[0]) {
			v_[0] = theMin.v_[0];
		}
		if (v_[1] < theMin.v_[1]) {
			v_[1] = theMin.v_[1];
		}
		if (v_[2] < theMin.v_[2]) {
			v_[2] = theMin.v_[2];
		}
	}

	public final void min(float theX, float theY, float theZ) {
		if (v_[0] < theX) {
			v_[0] = theX;
		}
		if (v_[1] < theY) {
			v_[1] = theY;
		}
		if (v_[2] < theZ) {
			v_[2] = theZ;
		}
	}

	public final void max(Vector3f theMax) {
		if (v_[0] > theMax.v_[0]) {
			v_[0] = theMax.v_[0];
		}
		if (v_[1] > theMax.v_[1]) {
			v_[1] = theMax.v_[1];
		}
		if (v_[2] > theMax.v_[2]) {
			v_[2] = theMax.v_[2];
		}
	}

	public final void max(float theX, float theY, float theZ) {
		if (v_[0] > theX) {
			v_[0] = theX;
		}
		if (v_[1] > theY) {
			v_[1] = theY;
		}
		if (v_[2] > theZ) {
			v_[2] = theZ;
		}
	}

	public final boolean equals(final Vector3f theVector) {
		if (v_[0] == theVector.v_[0] && v_[1] == theVector.v_[1] && v_[2] == theVector.v_[2]) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if this 3-element vector equals the specified object.
	 *
	 * @return  TRue if the two equals, false otherwise.
	 */
	 public final boolean equals(final Object theVector) {
		 if (! (theVector instanceof Vector3f)) {
			 return false;
		 }

		 return equals( (Vector3f) theVector);
	 }

	 public final boolean almost(final Vector3f theVector) {
		 if (Math.abs(v_[0] - theVector.v_[0]) < ALMOST_THRESHOLD
				 && Math.abs(v_[1] - theVector.v_[1]) < ALMOST_THRESHOLD
				 && Math.abs(v_[2] - theVector.v_[2]) < ALMOST_THRESHOLD) {
			 return true;
		 } else {
			 return false;
		 }
	 }

	 public Vector3f rotateBy(Quaternionf rotation){
		 set(rotation.clone().multiply(new Quaternionf(this.x(), this.y(), this.z(), 0)).multiply(rotation.inverseMake()).getImaginaryQuaternion());
		 return this;
	 }

	 /**
	  * rotate the vector around its Z Axis and return this instance
	  * @param angle
	  * @return this instance
	  */
	 public final Vector3f rotZ(float angle){
		 float x1 = v_[0] * (float)Math.cos(angle) + v_[1] * (float)Math.sin(angle);
		 float y1 = v_[1] * (float)Math.cos(angle) - v_[0] * (float)Math.sin(angle);
		 v_[0] = x1;
		 v_[1] = y1;
		 return this;
	 }

	 /**
	  * rotate the vector around its Z Axis and return a new instance.
	  * This method does NOT modify this instance.
	  * @param angle
	  * @return this instance
	  */
	 public final Vector3f rotZ_Make(float angle){
		 return clone().rotZ(angle);
	 }

	 /**
	  * rotate the vector around its Y Axis and return this instance
	  * @param angle
	  * @return this instance
	  */
	 public final Vector3f rotY(float angle){
		 float x1 = v_[0] * (float)Math.cos(angle) - v_[2] * (float)Math.sin(angle);
		 float z1 = v_[2] * (float)Math.cos(angle) + v_[0] * (float)Math.sin(angle);
		 v_[0] = x1;
		 v_[2] = z1;
		 return this;
	 }

	 /**
	  * rotate the vector around its Y Axis and return a new instance.
	  * This method does NOT modify this instance.
	  * @param angle
	  * @return this instance
	  */
	 public final Vector3f rotY_Make(float angle){
		 return clone().rotY(angle);
	 }

	 /**
	  * rotate the vector around its X Axis and return this instance
	  * @param angle
	  * @return this instance
	  */
	 public final Vector3f rotX(float angle){
		 float y1 = v_[1] * (float)Math.cos(angle) + v_[2] * (float)Math.sin(angle);
		 float z1 = v_[2] * (float)Math.cos(angle) - v_[1] * (float)Math.sin(angle);
		 v_[1] = y1;
		 v_[2] = z1;
		 return this;
	 }

	 /**
	  * rotate the vector around its X Axis and return a new instance.
	  * This method does NOT modify this instance.
	  * @param angle
	  * @return this instance
	  */
	 public final Vector3f rotX_Make(float angle){
		 return clone().rotX(angle);
	 }

	 public Vector3f clone() {
		 Vector3f clone = new Vector3f(v_[0], v_[1], v_[2]);
		 return clone;
	 }


	  public static final int X = 0;

	  public static final int Y = 1;

	  public static final int Z = 2;

	  public static final int LENGTH = 3;

	  public static int COMPARE_TYPE = LENGTH;


	 public int compareTo(Vector3f theVector3f) {
		 if (COMPARE_TYPE == LENGTH) {
			 final float myLengthSquared = lengthSquared();
			 final float myOtherLengthSquared = theVector3f.lengthSquared();
			 return myLengthSquared > myOtherLengthSquared ? 1 : (myLengthSquared < myOtherLengthSquared ? -1 : 0);
		 } else if (COMPARE_TYPE == X) {
			 return v_[0] > theVector3f.v_[0] ? 1 : (v_[0] < theVector3f.v_[0] ? -1 : 0);
		 } else if (COMPARE_TYPE == Y) {
			 return v_[1] > theVector3f.v_[1] ? 1 : (v_[1] < theVector3f.v_[1] ? -1 : 0);
		 } else if (COMPARE_TYPE == Z) {
			 return v_[2] > theVector3f.v_[2] ? 1 : (v_[2] < theVector3f.v_[2] ? -1 : 0);
		 } else {
			 return 0;
		 }
	 }


	 /**
	  * Create a string representation of this vector.
	  *
	  * @return  String representing this vector.
	  */
	 public String toString()
	 {
		 return ("Vector3f: [" +
				 v_[0] + "," + v_[1] + "," + v_[2] + "]");
	 }
}
