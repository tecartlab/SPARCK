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

import com.tecartlab.tools.threedee.Frustum;

/**
 * Implementation of a 4x4 matrix 
 * it is suited for use in a 2D and 3D graphics rendering engine.
 * 
 * The Matrix is structured the followin way:
 * 
 *  m00,  m01,  m02,  m03
 *  m10,  m11,  m12,  m13
 *  m20,  m21,  m22,  m23
 *  m30,  m31,  m32,  m33
 *  
 *  where a translation matrix looks like:
 *  
 *  1  ,  0  ,  0  ,  0
 *  0  ,  1  ,  0  ,  0
 *  0  ,  0  ,  1  ,  0
 *  tx ,  ty ,  tz ,  1 
 * 
 *  and a scale matrix looks like:
 *  
 *  sx ,  0  ,  0  ,  0
 *  0  ,  sy ,  0  ,  0
 *  0  ,  0  ,  sz ,  0
 *  0  ,  0  ,  0  ,  1 
 * 
 * Part of this code is from openFrameworks.org
 */   
public class Matrix4x4f 
{
	private float[]  m_;  // of 16

	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	private static final int W = 3;

	private final double SQRTHALF = 0.7071067811865475244;

	/**
	 * Construct a 4x4 identity matrix.
	 */
	public Matrix4x4f()
	{
		initialize();
		setIdentity();
	}



	/**
	 * Construct a 4x4 matrix with the specified element values.
	 * 
	 * @param m  Array of 16 ROW-oriented matrix elements, m00, m01, etc.
	 */
	public Matrix4x4f (float[] m)
	{
		initialize();
		set (m);
	}



	/**
	 * Constrauct a 4x4 matrix as a copy of the specified matrix.
	 * 
	 * @param matrix  Matrix to copy.
	 */
	public Matrix4x4f (Matrix4x4f matrix)
	{
		initialize();
		set (matrix);
	}

	/**
	 * Construct a Rotation matrix from the specified quaternion.
	 * 
	 * @param quat  normalized Quaternion.
	 */
	public Matrix4x4f (Quaternionf quat)
	{
		initialize();
		set (quat);
	}

	/**
	 * Construct a Translation matrix from the specified Vector.
	 * 
	 * @param translation  Translation vector.
	 */
	public Matrix4x4f (Vector3f translation)
	{
		initialize();
		set(translation);
	}

	/**
	 * Construct a Scale matrix from the specified factors.
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @param scaleZ
	 */
	public Matrix4x4f (float scaleX, float scaleY, float scaleZ)
	{
		initialize();
		set(scaleX, scaleY, scaleZ);
	}

	/**
	 * Construct a 4x4 frustum matrix from the specified frustum.
	 * 
	 * @param frust  Frustum.
	 */
	public Matrix4x4f (Frustum frust)
	{
		initialize();
		if(frust.isOrtho())
			setOrthograficMatrix(frust.left,
					frust.right, 
					frust.bottom, 
					frust.top, 
					frust.zNear, 
					frust.zFar);
		else
			setPerspectiveMatrix(frust.left,
					frust.right, 
					frust.bottom, 
					frust.top, 
					frust.zNear, 
					frust.zFar);

	}


	/**
	 * Construct a 4x4 matrix with the specified values.
	 * 
	 * where m[row, column]
	 * 
	 * @param m00  Value of element m[0,0].
	 * @param m01  Value of element m[0,1].
	 * @param m02  Value of element m[0,2].
	 * @param m03  Value of element m[0,3].
	 * @param m10  Value of element m[1,0].
	 * @param m11  Value of element m[1,1].
	 * @param m12  Value of element m[1,2].
	 * @param m13  Value of element m[1,3].
	 * @param m20  Value of element m[2,0].
	 * @param m21  Value of element m[2,1].
	 * @param m22  Value of element m[2,2].
	 * @param m23  Value of element m[2,3].
	 * @param m30  Value of element m[3,0].
	 * @param m31  Value of element m[3,1].
	 * @param m32  Value of element m[3,2].
	 * @param m33  Value of element m[3,3].
	 */
	public Matrix4x4f (float m00, float m01, float m02, float m03,
			float m10, float m11, float m12, float m13,
			float m20, float m21, float m22, float m23,
			float m30, float m31, float m32, float m33)
	{
		initialize();
		set (m00, m01, m02, m03,
				m10, m11, m12, m13,
				m20, m21, m22, m23,
				m30, m31, m32, m33);
	}



	/**
	 * Initialize the matrix.
	 */
	private void initialize()
	{
		m_ = new float[16];
	}



	/**
	 * Make an identity matrix out of this 4x4 matrix.
	 */
	public void setIdentity()
	{
		for (int i=0; i<4; i++)
			for (int j=0; j<4; j++)
				m_[i*4 + j] = i == j ? 1.0f : 0.0f;
	}



	/**
	 * Set the value of this 4x4matrix according to the specified
	 * matrix
	 *
	 * @param matrix  Matrix to copy.
	 */
	public void set(Matrix4x4f matrix)
	{
		for (int i=0; i<16; i++)
			m_[i] = matrix.m_[i];
	}

	/**
	 * Set the values of this 4x4 matrix.
	 * 
	 * @param m  Array of 16 ROW-oriented matrix elements, m00, m01, etc.
	 * @throws   ArrayOutOfBoundsException
	 */
	public void set(float[] m)
	{
		for (int i=0; i<16; i++)
			m_[i] = m[i];
	}

	/**
	 * Set the values of this 4x4 matrix.
	 * 
	 * @param m  Array of 16 COLUMN-oriented matrix elements, m00, m10, etc.
	 * @throws   ArrayOutOfBoundsException
	 */
	public void setCol(float[] m)
	{
		for (int i=0; i<16; i++){
			m_[i/4 + i%4*4] = m[i];
		}
	}

	/**
	 * Set the values of this 4x4 matrix.
	 * 
	 * where m[row, column]
	 * 
	 * @param m00  Value of element m[0,0].
	 * @param m01  Value of element m[0,1].
	 * @param m02  Value of element m[0,2].
	 * @param m03  Value of element m[0,3].
	 * @param m10  Value of element m[1,0].
	 * @param m11  Value of element m[1,1].
	 * @param m12  Value of element m[1,2].
	 * @param m13  Value of element m[1,3].
	 * @param m20  Value of element m[2,0].
	 * @param m21  Value of element m[2,1].
	 * @param m22  Value of element m[2,2].
	 * @param m23  Value of element m[2,3].
	 * @param m30  Value of element m[3,0].
	 * @param m31  Value of element m[3,1].
	 * @param m32  Value of element m[3,2].
	 * @param m33  Value of element m[3,3].
	 */
	public void set (float m00, float m01, float m02, float m03,
			float m10, float m11, float m12, float m13,
			float m20, float m21, float m22, float m23,
			float m30, float m31, float m32, float m33)
	{
		m_[0]  = m00;
		m_[1]  = m01;
		m_[2]  = m02;
		m_[3]  = m03;  

		m_[4]  = m10;
		m_[5]  = m11;
		m_[6]  = m12;
		m_[7]  = m13;  

		m_[8]  = m20;
		m_[9]  = m21;
		m_[10] = m22;
		m_[11] = m23;  

		m_[12] = m30;
		m_[13] = m31;
		m_[14] = m32;
		m_[15] = m33;  
	}

	/**
	 * Fills this 4x4 with a 3x3 rotation matrix
	 * 
	 * @param rot_matrix3x3
	 */
	public void setRotationMatrix (float[] rot_matrix3x3)
	{
		m_[0]  = rot_matrix3x3[0];
		m_[1]  = rot_matrix3x3[1];
		m_[2]  = rot_matrix3x3[2];
		m_[3]  = 0;  

		m_[4]  = rot_matrix3x3[3];
		m_[5]  = rot_matrix3x3[4];
		m_[6]  = rot_matrix3x3[5];
		m_[7]  = 0;  

		m_[8]  = rot_matrix3x3[6];
		m_[9]  = rot_matrix3x3[7];
		m_[10] = rot_matrix3x3[8];
		m_[11] = 0;  

		m_[12] = 0;
		m_[13] = 0;
		m_[14] = 0;
		m_[15] = 1;  
	}

	/**
	 * Sets this matrix as a perspective projection matrix
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param zNear
	 * @param zFar
	 */
	public void setPerspectiveMatrix(float left, float right,
			float bottom, float top,
			float zNear, float zFar){

		float A = (right+left)/(right-left);
		float B = (top+bottom)/(top-bottom);
		float C = -(zFar+zNear)/(zFar-zNear);
		float D = -2.0f*zFar*zNear/(zFar-zNear);
		initialize();
		setElement(0, 0, 2.0f*zNear/(right-left));
		setElement(1, 1, 2.0f*zNear/(top-bottom));
		setElement(2, 0, A);
		setElement(2, 1, B);
		setElement(2, 2, C);
		setElement(2, 3, -1.0f);
		setElement(3, 2, D);
}

	/**
	 * Sets this matrix as a orthografic projection matrix
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param zNear
	 * @param zFar
	 */
	public void setOrthograficMatrix(float left, float right,
			float bottom, float top,
			float zNear, float zFar){

		float tx = -(right+left)/(right-left);
		float ty = -(top+bottom)/(top-bottom);
		float tz = -(zFar+zNear)/(zFar-zNear);
		initialize();
		setElement(0, 0, 2.0f/(right-left));
		setElement(1, 1, 2.0f/(top-bottom));
		setElement(2, 2, -2.0f/(zFar-zNear));
		setElement(3, 0, tx);
		setElement(3, 1, ty);
		setElement(3, 2, tz);
	}

	/**
	 * Sets a Translation Matrix from a Vector.
	 * 
	 * @param vec  Translation Vector.
	 */
	public void set(Vector3f vec){
		setIdentity();
		setElement (3, 0, vec.x());
		setElement (3, 1, vec.y());
		setElement (3, 2, vec.z());  
	}

	/**
	 * Sets the translation elements with the provided vector without changing
	 * the rest of the matrix
	 * 
	 * @param vec
	 */
	public void setTranslation(Vector3f vec){
		setElement (3, 0, vec.x());
		setElement (3, 1, vec.y());
		setElement (3, 2, vec.z());  
	}

	/**
	 * Sets a Scale Matrix from set factors
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @param scaleZ
	 */
	public void set(float scaleX, float scaleY, float scaleZ){
		setIdentity();
		setElement (0, 0, scaleX);
		setElement (1, 1, scaleY);
		setElement (2, 2, scaleZ);  
	}

	/**
	 * Sets Rotation Matrix from a normalized Quaternion.
	 * 
	 * @param quat  normalized Quaternion4f.
	 */
	public void set(Quaternionf quat){
		//quat.normalize();
		float sqw = quat.w()*quat.w();
		float sqx = quat.x()*quat.x();
		float sqy = quat.y()*quat.y();
		float sqz = quat.z()*quat.z();


		float m00 = ( sqx - sqy - sqz + sqw); // since sqw + sqx + sqy + sqz =1/invs*invs
		float m11 = (-sqx + sqy - sqz + sqw);
		float m22 = (-sqx - sqy + sqz + sqw);

		float tmp1 = quat.x()*quat.y();
		float tmp2 = quat.z()*quat.w();
		float m01 = 2.0f * (tmp1 + tmp2);
		float m10 = 2.0f * (tmp1 - tmp2);
		tmp1 = quat.x()*quat.z();
		tmp2 = quat.y()*quat.w();
		float m02 = 2.0f * (tmp1 - tmp2);
		float m20 = 2.0f * (tmp1 + tmp2);
		tmp1 = quat.y()*quat.z();
		tmp2 = quat.x()*quat.w();
		float m12 = 2.0f * (tmp1 + tmp2);
		float m21 = 2.0f * (tmp1 - tmp2);      

		set (m00, m01, m02, 0,
				m10, m11, m12, 0,
				m20, m21, m22, 0,
				0, 0, 0, 1);
	}


	/**
	 * Return the values of this 4x4 matrix.
	 * 
	 * @return  Array ov values: m00, m01, etc.
	 */
	public float[] get()
	{
		return m_;
	}

	/**
	 * Return the values of this 4x4 matrix in COLUMN-oriented fashion.
	 * 
	 */
	public float[] getCol()
	{
		float[] ret = new float[16];
		for (int i=0; i<16; i++){
			ret[i] = m_[i/4 + i%4*4];
		}
		return ret;
	}

	/**
	 * Returns this 4x4 matrix in as a quaternion.<br>
	 * 
	 * This function will only result in a correct normalized Quaternion if
	 * the matrix is a pure rotation matrix.
	 * 
	 * Otherwise use please decompose()
	 * 
	 * see discussion: http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/
	 *
	 * @return        Quaternion4f
	 * 
	 */
	public Quaternionf getQuaternion(){
		float tr = m_[0] + m_[5] + m_[10];
		float qw, qx, qy, qz;

		if (tr > 0) { 
			float S = (float)Math.sqrt(tr+1.0) * 2.0f; // S=4*qw 
			qw = 0.25f * S;
			qx = (m_[6] - m_[9]) / S;
			qy = (m_[8] - m_[2]) / S; 
			qz = (m_[1] - m_[4]) / S; 
		} else if ((m_[0] > m_[5])&(m_[0] > m_[10])) { 
			float S = (float)Math.sqrt(1.0 + m_[0] - m_[5] - m_[10]) * 2; // S=4*qx 
			qw = (m_[6] - m_[9]) / S;
			qx = 0.25f * S;
			qy = (m_[1] + m_[4]) / S; 
			qz = (m_[8] + m_[2]) / S; 
		} else if (m_[5] > m_[10]) { 
			float S = (float)Math.sqrt(1.0 + m_[5] - m_[0] - m_[10]) * 2; // S=4*qy
			qw = (m_[8] - m_[2]) / S;
			qx = (m_[1] + m_[4]) / S; 
			qy = 0.25f * S;
			qz = (m_[6] + m_[9]) / S; 
		} else { 
			float S = (float)Math.sqrt(1.0 + m_[10] - m_[0] - m_[5]) * 2; // S=4*qz
			qw = (m_[1] - m_[4]) / S;
			qx = (m_[8] + m_[2]) / S;
			qy = (m_[6] + m_[9]) / S;
			qz = 0.25f * S;
		}

		Quaternionf ret = new Quaternionf(qx, qy, qz, qw);
		return ret;
	}

	/**
	 * Gets the Translation part of this Matrix
	 * @return
	 */
	public Vector3f getTranslation(){
		return new Vector3f(getElement (3, 0),getElement (3, 1),getElement (3, 2));  
	}

	/**
	 * Check if this 4x4 matrix equals the specified object.
	 * 
	 * @param object  Object to check.
	 * @return        True if the two are equal, false otherwise.
	 * @throws        ClassCastException if object is not of type Matrix4x4.
	 */
	public boolean equals (Object object)
	{
		Matrix4x4f matrix = (Matrix4x4f) object;

		for (int i=0; i<16; i++)
			if (m_[i] != matrix.m_[i]) return false;
		return true;
	}

	/**
	 * Transposes this matrix
	 * @return this instance
	 */
	public Matrix4x4f transpose(){
		Matrix4x4f trans = new Matrix4x4f(getCol());
		set(trans);
		return this;
	}


	/**
	 * Return matrix element [i,j].
	 * 
	 * @param i  Row of element to get (first row is 0).
	 * @param j  Column of element to get (first column is 0).
	 * @return   Element at specified position.
	 * @throws   ArrayOutOfBoundsException
	 */
	public float getElement (int i, int j)
	{
		return m_[i*4 + j];  
	}

	/**
	 * Return Row as a vector.
	 * 
	 * @param row  Row to get (first row is 0).
	 * @return   Vector3f at specified position.
	 * @throws   ArrayOutOfBoundsException
	 */
	public Vector3f getRowAsVector(int row){
		return new Vector3f(getElement(row, 0), getElement(row, 1), getElement(row, 1));
	}

	/**
	 * Set specified matrix element.
	 * 
	 * @param i      Row of element to set (first row is 0).
	 * @param j      Column of element to set (first column is 0).
	 * @param value  New element value.
	 * @throws       ArrayOutOfBoundsException
	 */
	public void setElement (int i, int j, float value)
	{
		m_[i*4 + j] = value;
	}


	/**
	 * Add the specified 4x4 matrix to this matrix.
	 * 
	 * @param matrix  Matrix to add.
	 * @return this instance
	 */
	public Matrix4x4f add(Matrix4x4f matrix)
	{
		for (int i=0; i<4; i++)
			for (int j=0; j<4; j++)
				m_[i*4 + j] += matrix.m_[i*4 + j];
		return this;
	}


	/**
	 * Add the specified 4x4 matrix to this matrix and return a new instance.
	 * This method does NOT modify this instance
	 * 
	 * @param matrix  Matrix to add.
	 * @return this instance
	 */
	public Matrix4x4f addMake(Matrix4x4f matrix){
		return clone().add(matrix);
	}

	/**
	 * Add two matrices and return the result matrix.
	 * 
	 * @param m1  First matrix to add.
	 * @param m2  Second matrix to add.
	 * @return    Sum m1 + m2.
	 */
	public static Matrix4x4f addMake(Matrix4x4f m1, Matrix4x4f m2)
	{
		Matrix4x4f m = new Matrix4x4f (m1);
		m.add (m2);
		return m;
	}



	/**
	 * Multiply this 4x4 matrix with the specified matrix and
	 * store the result in this 4x4 matrix.
	 * 
	 * @param matrix Matrix to multiply with.
	 * @return Product of this instance * matrix.
	 */
	public Matrix4x4f multiply(Matrix4x4f matrix){
		Matrix4x4f product = new Matrix4x4f();

		for (int i = 0; i < 16; i += 4) { //step through rows
			for (int j = 0; j < 4; j++) { //step through columns
				product.m_[i + j] = 0.0f; //set value of marixvector = 0
				for (int k = 0; k < 4; k++) // step though each Row (k) and each Column(k*4)
					product.m_[i + j] += m_[i + k] * matrix.m_[k*4 + j]; // and add the multiplied result
			}
		}

		set(product);
		return this;
	}

	/**
	 * Multiply two matrices and return the result matrix. This instance will
	 * NOT be modified
	 * 
	 * @param matrix Matrix to multiply with.
	 * @return Product of this instance * matrix.
	 */
	public Matrix4x4f multiplyMake (Matrix4x4f matrix){
		return clone().multiply(matrix);
	}


	/**
	 * Multiply this 4x4 matrix with the specified row vector.
	 * This is the preferred multiplication, since this matrix
	 * is row oriented as well.
	 * 
	 * @param vector4  row Vector to multiply with.
	 * @return         Result of operation.
	 */
	public Vector4d multiply(Vector4d vector4)
	{
		Vector4d  product = new Vector4d();

		for (int i = 0; i < 4; i++) {
			float value = 0.0f;
			for (int j = 0; j < 4; j++)
				value += getElement(j, i) * vector4.getElement (j);
			product.setElement (i, value);
		}

		return product;
	}
	
	/**
	 * Multiply this 4x4 matrix with the specified column vector.
	 * This is equal to a row multiplication with the transposed
	 * matrix
	 * 
	 * @param vector4  columns Vector to multiply with.
	 * @return         Result of operation.
	 */
	public Vector4d multiplyColumn(Vector4d vector4)
	{
		Vector4d  product = new Vector4d();

		for (int i = 0; i < 4; i++) {
			float value = 0.0f;
			for (int j = 0; j < 4; j++)
				value += getElement(i, j) * vector4.getElement (j);
			product.setElement (i, value);
		}

		return product;
	}

	/**
	 * Transform a Vector using this 4x4 matrix. This is basically a 4 elements vector 
	 * multiplication and subsequent normalization.
	 * 
	 * return a new instance without modifying the provided instance
	 * 
	 * @param point  
	 * @return vector
	 */
	public Vector3f multiplyMake(Vector3f point)
	{
		return multiply(point.clone());
	}

	/**
	 * Transform a Vector using this 4x4 matrix. This is basically a 4 elements vector 
	 * multiplication and subsequent normalization.
	 * 
	 * It applies the result to the provided instance and also returns it.
	 * 	
	 * @param point  
	 * @return point
	 */
	public Vector3f multiply(Vector3f point)
	{
		float d = 1.0f / (point.x() * m_[3] + point.y() * m_[7] + point.z() * m_[11] + m_[15]);
		float x = (point.x() * m_[0]  +
				point.y() * m_[4]  +
				point.z() * m_[8]  +  m_[12]) * d;

		float y = (point.x() * m_[1]  +
				point.y() * m_[5]  +
				point.z() * m_[9]  +  m_[13]) * d;

		float z = (point.x() * m_[2]   +
				point.y() * m_[6]   +
				point.z() * m_[10]  + m_[14]) * d;

		point.set(x, y, z);
		return point;
	}


	/**
	 * Transform one coordinate using this 4x4 matrix. 
	 * 
	 * This method will only give correct results if the matrix is a 
	 * classical transformation matrix (ie. scale, rotate, translate or
	 * a combination of the three)
	 * 
	 * @param point  [x0,y0,z0]
	 * @return       Result of operation: [x0',y0',z0']
	 */
	public float[] transformPoint (float[] point)
	{
		float[]  result = new float[3];

		result[0] = point[0] * m_[0]  +
				point[1] * m_[4]  +
				point[2] * m_[8]  + m_[12];

		result[1] = point[0] * m_[1]  +
				point[1] * m_[5]  +
				point[2] * m_[9]  + m_[13];

		result[2] = point[0] * m_[2]   +
				point[1] * m_[6]   +
				point[2] * m_[10]  + m_[14];

		return result;
	}



	/**
	 * Transform a set of 3D coordinates using this 4x4 matrix.
	 * The result of the operation is put back in the original array.
	 * 
	 * This method will only give correct results if the matrix is a 
	 * classical transformation matrix (ie. scale, rotate, translate or
	 * a combination of the three)
	 * 
	 * @param points  Points to transform [x0,y0,z0,x1,y1,z1,...]
	 */
	public void transformPoints (float[] points)
	{
		for (int i = 0; i < points.length; i += 3) {
			float x = points[i + 0] * m_[0]  +
					points[i + 1] * m_[4]  +
					points[i + 2] * m_[8]  + m_[12];

			float y = points[i + 0] * m_[1]  +
					points[i + 1] * m_[5]  +
					points[i + 2] * m_[9]  + m_[13];

			float z = points[i + 0] * m_[2]   +
					points[i + 1] * m_[6]   +
					points[i + 2] * m_[10]  + m_[14];

			points[i + 0] = x;
			points[i + 1] = y;
			points[i + 2] = z;            
		}
	}



	/**
	 * Transform a set of 2D (x,y) coordinates using this 4x4 matrix.
	 * The result of the operation is put back in the original array
	 * rounded to the nearest integer.
	 * 
	 * This method will only give correct results if the matrix is a 
	 * classical transformation matrix (ie. scale, rotate, translate or
	 * a combination of the three)
	 * 
	 * @param points  Points to transform [x0,y0,x1,y1,...].
	 */
	public void transformXyPoints (float[] points)
	{
		for (int i = 0; i < points.length; i += 2) {
			float x = points[i + 0] * m_[0]  +
					points[i + 1] * m_[4]  + m_[12];

			float y = points[i + 0] * m_[1]  +
					points[i + 1] * m_[5]  + m_[13];

			points[i + 0] = x;
			points[i + 1] = y;
		}
	}



	/**
	 * Transform a set of 3D coordinates using this 4x4 matrix.
	 * The result of the operation is put back in the original array.
	 * 
	 * This method will only give correct results if the matrix is a 
	 * classical transformation matrix (ie. scale, rotate, translate or
	 * a combination of the three)
	 * 
	 * @param points  Points to transform [x0,y0,z0,x1,y1,z1,...].
	 */
	public void transformPoints (int[] points)
	{
		for (int i = 0; i < points.length; i += 3) {
			float x = points[i + 0] * m_[0]  +
					points[i + 1] * m_[4]  +
					points[i + 2] * m_[8]  + m_[12];

			float y = points[i + 0] * m_[1]  +
					points[i + 1] * m_[5]  +
					points[i + 2] * m_[9]  + m_[13];

			float z = points[i + 0] * m_[2]  +
					points[i + 1] * m_[6]  +
					points[i + 2] * m_[10] + m_[14];

			points[i + 0] = (int) Math.round (x);
			points[i + 1] = (int) Math.round (y);
			points[i + 2] = (int) Math.round (z);            
		}
	}



	/**
	 * Transform a set of 2D (x,y) coordinates using this 4x4 matrix.
	 * The result of the operation is put back in the original array
	 * rounded to the nearest integer.
	 * 
	 * This method will only give correct results if the matrix is a 
	 * classical transformation matrix (ie. scale, rotate, translate or
	 * a combination of the three)
	 * 
	 * @param points  Points to transform [x0,y0,x1,y1,...].
	 */
	public void transformXyPoints (int[] points)
	{
		for (int i = 0; i < points.length; i += 2) {
			float x = points[i + 0] * m_[0] +
					points[i + 1] * m_[4] + m_[12];

			float y = points[i + 0] * m_[1]  +
					points[i + 1] * m_[5]  + m_[13];

			points[i + 0] = (int) Math.round (x);
			points[i + 1] = (int) Math.round (y);
		}
	}

	/**
	 * multiply the specified translation matrix with this matrix.
	 * 
	 * @param dx  x translation of translation matrix.
	 * @param dy  y translation of translation matrix.
	 * @param dz  z translation of translation matrix.
	 * @return this instance
	 */
	public Matrix4x4f translate(float dx, float dy, float dz)
	{
		translate(new Vector3f(dx, dy, dz));
		return this;
	}

	/**
	 * multiply the specified translation matrix to this matrix.
	 * 
	 * @param vec  Vector3f  of translation matrix
	 * @return this instance
	 */
	public Matrix4x4f translate(Vector3f vec)
	{
		Matrix4x4f  translationMatrix = new Matrix4x4f(vec);
		multiply (translationMatrix);
		return this;
	}

	/**
	 * Apply rotation around X axis to this matrix.
	 * 
	 * @param angle  Angle to rotate [radians].
	 * @return this instance
	 */
	public Matrix4x4f rotateX (float angle)
	{
		Matrix4x4f rotationMatrix = new Matrix4x4f();

		float cosAngle = (float)Math.cos (angle);
		float sinAngle = (float)Math.sin (angle);  

		rotationMatrix.setElement (1, 1,  cosAngle);
		rotationMatrix.setElement (1, 2,  sinAngle);
		rotationMatrix.setElement (2, 1, -sinAngle);
		rotationMatrix.setElement (2, 2,  cosAngle);

		multiply (rotationMatrix);
		return this;
	}

	/**
	 * Apply rotation around Y axis to this matrix.
	 * 
	 * @param angle  Angle to rotate [radians].
	 * @return this instance
	 */
	public Matrix4x4f rotateY (float angle)
	{
		Matrix4x4f rotationMatrix = new Matrix4x4f();

		float cosAngle = (float)Math.cos (angle);
		float sinAngle = (float)Math.sin (angle);  

		rotationMatrix.setElement (0, 0,  cosAngle);
		rotationMatrix.setElement (0, 2, -sinAngle);
		rotationMatrix.setElement (2, 0,  sinAngle);
		rotationMatrix.setElement (2, 2,  cosAngle);

		multiply (rotationMatrix);
		return this;
	}

	/**
	 * Apply rotation around z axis to this matrix.
	 * 
	 * @param angle  Angle to rotate [radians].
	 * @return this instance
	 */
	public Matrix4x4f rotateZ (float angle)
	{
		Matrix4x4f rotationMatrix = new Matrix4x4f();

		float cosAngle = (float)Math.cos (angle);
		float sinAngle = (float)Math.sin (angle);  

		rotationMatrix.setElement (0, 0,  cosAngle);
		rotationMatrix.setElement (0, 1,  sinAngle);
		rotationMatrix.setElement (1, 0, -sinAngle);
		rotationMatrix.setElement (1, 1,  cosAngle);

		multiply (rotationMatrix);
		return this;
	}

	/**
	 * Apply rotation around an arbitrary axis.
	 *
	 * Ref: http://www.swin.edu.au/astronomy/pbourke/geometry/rotate/
	 * (but be aware of errors, corrected here)
	 *
	 * @param angle  Angle to rotate [radians]
	 * @param p0     First point defining the axis (x,y,z)
	 * @param p1     Second point defining the axis (x,y,z)
	 * @return this instance
	 */
	public Matrix4x4f rotate(float angle, float[] p0, float[] p1)
	{
		// Represent axis of rotation by a unit vector [a,b,c]
		float a = p1[0] - p0[0];
		float b = p1[1] - p0[1];
		float c = p1[2] - p0[2];  

		float length = (float)Math.sqrt (a*a + b*b + c*c);

		a /= length;
		b /= length;
		c /= length;  

		float d = (float)Math.sqrt (b*b + c*c);

		// Coefficients used for step 2 matrix
		float e = d == 0.0f ? 1.0f : c / d;
		float f = d == 0.0f ? 0.0f : b / d;  

		// Coefficients used for the step 3 matrix
		float k = d;
		float l = a;

		// Coefficients for the step 5 matrix (inverse of step 3)
		float m = d / (a*a + d*d);
		float n = a / (a*a + d*d);  

		// Coefficients for the step 4 matrix
		float cosAngle = (float)Math.cos (angle);
		float sinAngle = (float)Math.sin (angle);  

		//
		// Step 1
		//
		Matrix4x4f  step1 = new Matrix4x4f();
		step1.setElement (3, 0, -p0[0]);
		step1.setElement (3, 1, -p0[1]);
		step1.setElement (3, 2, -p0[2]);

		//
		// Step 2
		//
		Matrix4x4f  step2 = new Matrix4x4f();
		step2.setElement (1, 1,  e);
		step2.setElement (1, 2,  f);
		step2.setElement (2, 1, -f);
		step2.setElement (2, 2,  e);      

		//
		// Step 3
		//
		Matrix4x4f  step3 = new Matrix4x4f();
		step3.setElement (0, 0,  k);
		step3.setElement (0, 2,  l);
		step3.setElement (2, 0, -l);
		step3.setElement (2, 2,  k);

		//
		// Step 4
		//
		Matrix4x4f  step4 = new Matrix4x4f();
		step4.setElement (0, 0,  cosAngle);
		step4.setElement (0, 1,  sinAngle);
		step4.setElement (1, 0, -sinAngle);
		step4.setElement (1, 1,  cosAngle);

		//
		// Step 5 (inverse of step 3)
		//
		Matrix4x4f  step5 = new Matrix4x4f();
		step5.setElement (0, 0,  m);
		step5.setElement (0, 2, -n);
		step5.setElement (2, 0,  n);
		step5.setElement (2, 2,  m);

		//
		// Step 6 (inverse of step 2)
		//
		Matrix4x4f  step6 = new Matrix4x4f();
		step6.setElement (1, 1,  e);
		step6.setElement (1, 2, -f);
		step6.setElement (2, 1,  f);
		step6.setElement (2, 2,  e);      

		//
		// Step 7 (inverse of step 1)
		//
		Matrix4x4f  step7 = new Matrix4x4f();
		step7.setElement (3, 0, p0[0]);
		step7.setElement (3, 1, p0[1]);
		step7.setElement (3, 2, p0[2]);

		multiply (step1);
		multiply (step2);
		multiply (step3);
		multiply (step4);
		multiply (step5);
		multiply (step6);
		multiply (step7);
		
		return this;
	}

	/**
	 * Apply rotation with a quaternion.
	 *
	 * @param quat   normalized Quaternion
	 * @return this instance
	 */
	public Matrix4x4f rotate(Quaternionf quat){
		multiply(new Matrix4x4f(quat));
		return this;
	}

	/**
	 * Apply scaling (relative to origo) to this 4x4 matrix.
	 * 
	 * @param xScale  Scaling in x direction.
	 * @param yScale  Scaling in y direction.
	 * @param zScale  Scaling in z direction.
	 * @return this instance
	 */
	public Matrix4x4f scale(float xScale, float yScale, float zScale)
	{
		Matrix4x4f  scalingMatrix = new Matrix4x4f(xScale, yScale, zScale);
		multiply (scalingMatrix);
		return this;
	}

	/**
	 * Apply scaling-vector (relative to origo) to this 4x4 matrix.
	 * 
	 * @param scale  Scaling Vector3f.
	 * @return this instance
	 */
	public Matrix4x4f scale(Vector3f scale)
	{
		scale(scale.x(), scale.y(), scale.z());
		return this;
	}

	/**
	 * Apply scaling relative to a fixed point to this 4x4 matrix.
	 * 
	 * @param xScale      Scaling in x direction.
	 * @param yScale      Scaling in y direction.
	 * @param zScale      Scaling in z direction.
	 * @param fixedPoint  Scaling origo.
	 */
	public void scale(float xScale, float yScale, float zScale,
			Vector3f fixedPoint)
	{
		translate(fixedPoint.scaleMake(-1.0f));
		scale(xScale, yScale, zScale);
		translate(fixedPoint);
	}

	/**
	 * Invert this 4x4 matrix.
	 * @return this instance
	 */
	public Matrix4x4f invert()
	{
		float[] tmp = new float[12];
		float[] src = new float[16];
		float[] dst = new float[16];  

		// Transpose matrix
		for (int i = 0; i < 4; i++) {
			src[i +  0] = m_[i*4 + 0];
			src[i +  4] = m_[i*4 + 1];
			src[i +  8] = m_[i*4 + 2];
			src[i + 12] = m_[i*4 + 3];
		}

		// Calculate pairs for first 8 elements (cofactors) 
		tmp[0] = src[10] * src[15];
		tmp[1] = src[11] * src[14];
		tmp[2] = src[9]  * src[15];
		tmp[3] = src[11] * src[13];
		tmp[4] = src[9]  * src[14];
		tmp[5] = src[10] * src[13];
		tmp[6] = src[8]  * src[15];
		tmp[7] = src[11] * src[12];
		tmp[8] = src[8]  * src[14];
		tmp[9] = src[10] * src[12];
		tmp[10] = src[8] * src[13];
		tmp[11] = src[9] * src[12];

		// Calculate first 8 elements (cofactors)
		dst[0]  = tmp[0]*src[5] + tmp[3]*src[6] + tmp[4]*src[7];
		dst[0] -= tmp[1]*src[5] + tmp[2]*src[6] + tmp[5]*src[7];
		dst[1]  = tmp[1]*src[4] + tmp[6]*src[6] + tmp[9]*src[7];
		dst[1] -= tmp[0]*src[4] + tmp[7]*src[6] + tmp[8]*src[7];
		dst[2]  = tmp[2]*src[4] + tmp[7]*src[5] + tmp[10]*src[7];
		dst[2] -= tmp[3]*src[4] + tmp[6]*src[5] + tmp[11]*src[7];
		dst[3]  = tmp[5]*src[4] + tmp[8]*src[5] + tmp[11]*src[6];
		dst[3] -= tmp[4]*src[4] + tmp[9]*src[5] + tmp[10]*src[6];
		dst[4]  = tmp[1]*src[1] + tmp[2]*src[2] + tmp[5]*src[3];
		dst[4] -= tmp[0]*src[1] + tmp[3]*src[2] + tmp[4]*src[3];
		dst[5]  = tmp[0]*src[0] + tmp[7]*src[2] + tmp[8]*src[3];
		dst[5] -= tmp[1]*src[0] + tmp[6]*src[2] + tmp[9]*src[3];
		dst[6]  = tmp[3]*src[0] + tmp[6]*src[1] + tmp[11]*src[3];
		dst[6] -= tmp[2]*src[0] + tmp[7]*src[1] + tmp[10]*src[3];
		dst[7]  = tmp[4]*src[0] + tmp[9]*src[1] + tmp[10]*src[2];
		dst[7] -= tmp[5]*src[0] + tmp[8]*src[1] + tmp[11]*src[2];

		// Calculate pairs for second 8 elements (cofactors)
		tmp[0]  = src[2]*src[7];
		tmp[1]  = src[3]*src[6];
		tmp[2]  = src[1]*src[7];
		tmp[3]  = src[3]*src[5];
		tmp[4]  = src[1]*src[6];
		tmp[5]  = src[2]*src[5];
		tmp[6]  = src[0]*src[7];
		tmp[7]  = src[3]*src[4];
		tmp[8]  = src[0]*src[6];
		tmp[9]  = src[2]*src[4];
		tmp[10] = src[0]*src[5];
		tmp[11] = src[1]*src[4];

		// Calculate second 8 elements (cofactors)
		dst[8]   = tmp[0] * src[13]  + tmp[3] * src[14]  + tmp[4] * src[15];
		dst[8]  -= tmp[1] * src[13]  + tmp[2] * src[14]  + tmp[5] * src[15];
		dst[9]   = tmp[1] * src[12]  + tmp[6] * src[14]  + tmp[9] * src[15];
		dst[9]  -= tmp[0] * src[12]  + tmp[7] * src[14]  + tmp[8] * src[15];
		dst[10]  = tmp[2] * src[12]  + tmp[7] * src[13]  + tmp[10]* src[15];
		dst[10] -= tmp[3] * src[12]  + tmp[6] * src[13]  + tmp[11]* src[15];
		dst[11]  = tmp[5] * src[12]  + tmp[8] * src[13]  + tmp[11]* src[14];
		dst[11] -= tmp[4] * src[12]  + tmp[9] * src[13]  + tmp[10]* src[14];
		dst[12]  = tmp[2] * src[10]  + tmp[5] * src[11]  + tmp[1] * src[9];
		dst[12] -= tmp[4] * src[11]  + tmp[0] * src[9]   + tmp[3] * src[10];
		dst[13]  = tmp[8] * src[11]  + tmp[0] * src[8]   + tmp[7] * src[10];
		dst[13] -= tmp[6] * src[10]  + tmp[9] * src[11]  + tmp[1] * src[8];
		dst[14]  = tmp[6] * src[9]   + tmp[11]* src[11]  + tmp[3] * src[8];
		dst[14] -= tmp[10]* src[11 ] + tmp[2] * src[8]   + tmp[7] * src[9];
		dst[15]  = tmp[10]* src[10]  + tmp[4] * src[8]   + tmp[9] * src[9];
		dst[15] -= tmp[8] * src[9]   + tmp[11]* src[10]  + tmp[5] * src[8];

		// Calculate determinant
		float det = src[0]*dst[0] + src[1]*dst[1] + src[2]*dst[2] + src[3]*dst[3];

		// Calculate matrix inverse
		det = 1.0f / det;
		for (int i = 0; i < 16; i++)
			m_[i] = dst[i] * det;
		
		return this;
	}

	/**
	 * Return the inverse of the specified matrix without modifying this instance
	 * 
	 * @return new Instance of the inverse of this matrix.
	 */
	public Matrix4x4f invertMake(){
		return clone().invert();
	}

	/**
	 * Solve the A x = b equation, where A is this 4x4 matrix, b is the
	 * specified result vector and the returned vector is the unknown x.
	 *
	 * @param vector  Result vector
	 * @return        Unknown vector.
	 */
	public Vector4d solve(Vector4d vector){
		return invertMake().multiply(vector);
	}

	/**
	 * Make this 4x4 matrix a world-2-device transformation matrix.
	 * <p>
	 * The world system is defined as follows:
	 *
	 * <pre>
	 *        w2 o 
	 *           |
	 *           |
	 *           |
	 *        w0 o-------o w1
	 * <pre>
	 * <p>
	 * Each point is defined with x,y,z so this system may in effect be
	 * arbitrary oriented in space, and may include sharing.
	 * <p>
	 * The device system is defined as follows:
	 *
	 * <pre>
	 *             width
	 *     x0,y0 o-------o
	 *           |
	 *    height |
	 *           |
	 *           o
	 * </pre>
	 * <p>
	 * The matrix maps w2 to (x0,y0), w0 to the lower left corner of the
	 * device rectangle, and w1 to the lower right corner of the device
	 * rectangle.
	 *
	 * @param w0      x,y,z coordinate of first world position.
	 * @param w1      x,y,z coordinate of second world position.
	 * @param w2      x,y,z coordinate of third world position.
	 * @param x0      X coordinate of upper left corner of device.
	 * @param y0      Y coordinate of upper left corner of device.
	 * @param width   Width of device
	 * @param height  Height of device.
	 */
	public void setWorld2DeviceTransform (float[] w0, float[] w1, float[] w2,
			int x0, int y0, int width, int height)
	{
		setIdentity();

		float[] x = new float[4];
		float[] y = new float[4];
		float[] z = new float[4];

		// Make direction vectors for new system
		x[0] = w2[0];          y[0] = w2[1];          z[0] = w2[2];
		x[1] = w1[0] - w0[0];  y[1] = w1[1] - w0[1];  z[1] = w1[2] - w0[2];
		x[2] = w0[0] - w2[0];  y[2] = w0[1] - w2[1];  z[2] = w0[2] - w2[2];

		x[3] = y[1]*z[2] - z[1]*y[2];
		y[3] = z[1]*x[2] - x[1]*z[2];
		z[3] = x[1]*y[2] - y[1]*x[2];

		// Normalize new z-vector, in case someone needs
		// new z-value in addition to device coordinates */
		float length = (float)Math.sqrt (x[3]*x[3] + y[3]*y[3] + z[3]*z[3]); 
		x[3] /= length;
		y[3] /= length;
		z[3] /= length;

		// Translate back to new origin                                
		translate (-x[0], -y[0], -z[0]);

		// Multiply with inverse of definition of new coordinate system
		float a = y[2]*z[3] - z[2]*y[3];
		float b = z[1]*y[3] - y[1]*z[3];
		float c = y[1]*z[2] - z[1]*y[2];

		float det = x[1]*a + x[2]*b + x[3]*c;

		float[] m = new float[16];

		m[0]  = a / det; 
		m[1]  = b / det; 
		m[2]  = c / det; 
		m[3]  = 0.0f;

		m[4]  = (x[3]*z[2] - x[2]*z[3]) / det;   
		m[5]  = (x[1]*z[3] - x[3]*z[1]) / det; 
		m[6]  = (z[1]*x[2] - x[1]*z[2]) / det;               
		m[7]  = 0.0f;

		m[8]  = (x[2]*y[3] - x[3]*y[2]) / det;  
		m[9]  = (y[1]*x[3] - x[1]*y[3]) / det;  
		m[10] = (x[1]*y[2] - y[1]*x[2]) / det;
		m[11] = 0.0f;

		m[12] = 0.0f; 
		m[13] = 0.0f; 
		m[14] = 0.0f; 
		m[15] = 1.0f;

		Matrix4x4f matrix = new Matrix4x4f (m);
		multiply (matrix);

		// Scale according to height and width of viewport
		matrix.setIdentity();
		matrix.setElement (0, 0, width);
		matrix.setElement (1, 1, height);
		multiply (matrix);

		// Translate according to origin of viewport
		matrix.setIdentity();
		matrix.setElement (3, 0, x0);
		matrix.setElement (3, 1, y0);
		multiply (matrix);
	}


	/**
	 * Get the rotation of this Matrix. It uses internally the decompose method - 
	 * it is therefore not very fast..
	 * 
	 * @return rotation quaternion
	 */
	public Quaternionf getRotation(){
		Quaternionf ret = new Quaternionf();
		decompose(new Vector3f(), ret, new Vector3f(), new Quaternionf());
		return ret;
	}
	
	/**
	 * Decompose this Matrix into a translsation vector, a scale vector and a
	 * quaternion rotation
	 * 
	 * The code used for this task was taken from openFrameworks.org math class ofMatrix4x4
	 * 
	 * @param translation
	 * @param rotation
	 * @param scale
	 * @param so
	 */
	public void decompose( Vector3f translation,
			Quaternionf rotation,
			Vector3f scale,
			Quaternionf so ){

		AffineParts parts = new AffineParts();
		double[][] hmatrix = new double[4][4];

		// Transpose copy of LTW
		for ( int i =0; i<4; i++){
			for ( int j=0; j<4; j++){
				hmatrix[i][j] = this.getElement(j, i);
			}
		}

		decompAffine(hmatrix, parts);

		float mul = 1.0f;
		if (parts.translation.getElement(W) != 0.0)
			mul = 1.0f / (float)parts.translation.getElement(W);

		translation.setX((float)parts.translation.getElement(X) * mul);
		translation.setY((float)parts.translation.getElement(Y) * mul);
		translation.setZ((float)parts.translation.getElement(Z) * mul);

		rotation.set(parts.rotation.x(), parts.rotation.y(), parts.rotation.z(), parts.rotation.w());

		mul = 1.0f;
		if (parts.k.w() != 0.0)
			mul = 1.0f / parts.k.w();

		// mul be sign of determinant to support negative scales.
		mul *= parts.f;
		scale.setX(parts.k.x() * mul);
		scale.setY(parts.k.y() * mul);
		scale.setZ(parts.k.z() * mul);

		so.set(parts.stretch.x(), parts.stretch.y(), parts.stretch.z(), parts.stretch.w());
	}

	
	
	/* Decompose 4x4 affine matrix A as TFRUK(U transpose), where t contains the
	 * translation components, q contains the rotation R, u contains U, k contains
	 * scale factors, and f contains the sign of the determinant.
	 * Assumes A transforms column vectors in right-handed coordinates.
	 * See Ken Shoemake and Tom Duff. Matrix Animation and Polar Decomposition.
	 * Proceedings of Graphics Interface 1992.
	 */
	private void decompAffine(double[][] A, AffineParts parts)
	{
		double[][] Q = new double[4][4];
		double[][] S = new double[4][4]; 
		double[][] U = new double[4][4];
		Quaternionf p;

		//Translation component.
		parts.translation = new Vector4d(A[X][W], A[Y][W], A[Z][W], 0);
		double det = polarDecomp(A, Q, S);
		if (det < 0){
			mat_copy(Q, "=", mat_mult(Q, -1), 3);
			parts.f = -1f;
		}
		else
			parts.f = 1;

		parts.rotation = quatFromMatrix(Q);
		parts.k = spectDecomp(S, U);
		parts.stretch = quatFromMatrix(U);
		p = snuggle(parts.stretch, parts.k);
		parts.stretch.multiply(p);
	}
	
	private class AffineParts{

		Vector4d translation;     // t Translation Component;
		Quaternionf rotation;           // q Essential Rotation
		Quaternionf stretch;          // u Stretch rotation
		Quaternionf k;          //Sign of determinant
		double f;          // Sign of determinant

		AffineParts(){
			;
		}
	}

	/* Given a unit quaternion, q, and a scale vector, k, find a unit quaternion, p,
	 * which permutes the axes and turns freely in the plane of duplicate scale
	 * factors, such that q p has the largest possible w component, i.e. the
	 * smallest possible angle. Permutes k's components to go with q p instead of q.
	 * See Ken Shoemake and Tom Duff. Matrix Animation and Polar Decomposition.
	 * Proceedings of Graphics Interface 1992. Details on p. 262-263.
	 */
	private Quaternionf snuggle(Quaternionf q, Quaternionf k)
	{

		Quaternionf p = new Quaternionf();
		double[] ka = new double[4];
		int i, turn = -1;
		ka[X] = k.x(); 
		ka[Y] = k.y(); 
		ka[Z] = k.z();

		if (ka[X]==ka[Y]) {
			if (ka[X]==ka[Z])
				turn = W;
			else turn = Z;
		} else {
			if (ka[X]==ka[Z])
				turn = Y;
			else if (ka[Y]==ka[Z])
				turn = X;
		}
		if (turn>=0) {
			Quaternionf qtoz, qp;
			int  win;
			double[] mag = new double[3]; 
			double t;
			switch (turn) {
			case X: 
				qtoz = new Quaternionf(0f,(float)SQRTHALF,0f,(float)SQRTHALF);
				q.multiply(qtoz); 
				swap(ka,X,Z);
				break;
			case Y: 
				qtoz = new Quaternionf((float)SQRTHALF,0f, 0f,(float)SQRTHALF);
				q.multiply(qtoz); 
				swap(ka,Y,Z); 
				break;
			case Z: 
				qtoz = new Quaternionf(); 
				break;
			default: return q.conjugateMake();
			}


			q.conjugate();
			mag[0] = (double)q.z()*q.z()+(double)q.w()*q.w()-0.5;
			mag[1] = (double)q.x()*q.z()-(double)q.y()*q.w();
			mag[2] = (double)q.y()*q.z()+(double)q.x()*q.w();

			boolean[] neg = new boolean[3];
			for (i=0; i<3; i++)
			{
				neg[i] = (mag[i]<0.0);
				if (neg[i]) mag[i] = -mag[i];
			}

			if (mag[0]>mag[1]) {
				if (mag[0]>mag[2])
					win = 0;
				else win = 2;
			}
			else {
				if (mag[1]>mag[2]) win = 1;
				else win = 2;
			}

			switch (win) {
			case 0: 
				if (neg[0]) 
					p = new Quaternionf(1.0f, 0.0f, 0.0f, 0.0f); 
				else 
					p = new Quaternionf(); 
				break;
			case 1: 
				if (neg[1]) 
					p = new Quaternionf(0.5f, 0.5f, -0.5f, -0.5f); 
				else 
					p = new Quaternionf( 0.5f, 0.5f, 0.5f, 0.5f); 
				cycle(ka,false); 
				break;
			case 2: 
				if (neg[2]) 
					p = new Quaternionf(-0.5f, 0.5f,-0.5f,-0.5f); 
				else 
					p = new Quaternionf( 0.5f, 0.5f, 0.5f,-0.5f); 
				cycle(ka,true); 
				break;
			}

			qp = q.multiplyMake(p);
			t = Math.sqrt(mag[win]+0.5);
			p.multiply(new Quaternionf(0f,0f,(float)(-qp.z()/t),(float)(qp.w()/t)));
			p = qtoz.multiplyMake(p.conjugateMake());
		}
		else {
			double[] qa = new double[4];
			double[] pa = new double[4];
			int lo, hi;
			boolean par = false;
			boolean[] neg = new boolean[4];
			double all, big, two;
			qa[0] = q.x(); qa[1] = q.y(); qa[2] = q.z(); qa[3] = q.w();
			for (i=0; i<4; i++) {
				pa[i] = 0.0;
				neg[i] = (qa[i]<0.0);
				if (neg[i]) 
					qa[i] = -qa[i];
				par ^= neg[i];
			}

			/* Find two largest components, indices in hi and lo */
			if (qa[0]>qa[1]) 
				lo = 0;
			else 
				lo = 1;

			if (qa[2]>qa[3]) 
				hi = 2;
			else 
				hi = 3;

			if (qa[lo]>qa[hi]) {
				if (qa[lo^1]>qa[hi]) {
					hi = lo; lo ^= 1;
				}
				else {
					hi ^= lo; lo ^= hi; hi ^= lo;
				}
			}
			else {
				if (qa[hi^1]>qa[lo]) 
					lo = hi^1;
			}

			all = (qa[0]+qa[1]+qa[2]+qa[3])*0.5;
			two = (qa[hi]+qa[lo])*SQRTHALF;
			big = qa[hi];
			if (all>two) {
				if (all>big) {/*all*/
					for (int j = 0; j<4; j++) 
						pa[j] = sgn(neg[j], 0.5);
					cycle(ka,par);
				}
				else {/*big*/ 
					pa[hi] = sgn(neg[hi],1.0);
				}
			} else {
				if (two>big) { /*two*/
					pa[hi] = sgn(neg[hi],SQRTHALF);
					pa[lo] = sgn(neg[lo], SQRTHALF);
					if (lo>hi) {
						hi ^= lo; lo ^= hi; hi ^= lo;
					}
					if (hi==W) {
						int[] lo2hi = new int[]{1, 2, 0};
						hi = lo2hi[lo];
						//hi = "\001\002\000"[lo];
						lo = 3-hi-lo;
					}
					swap(ka,hi,lo);
				}
				else {/*big*/
					pa[hi] = sgn(neg[hi],1.0);
				}
			}
			p.setX((float)-pa[0]); 
			p.setY((float)-pa[1]); 
			p.setZ((float)-pa[2]); 
			p.setW((float)pa[3]); 
		}
		k.setX((float)ka[X]);
		k.setY((float)ka[Y]);
		k.setZ((float)ka[Z]);
		return p;
	}


	private void cycle(double[] a,boolean p){
		if (p) {
			a[3]=a[0]; 
			a[0]=a[1]; 
			a[1]=a[2];
			a[2]=a[3];
		} else {
			a[3]=a[2]; 
			a[2]=a[1]; 
			a[1]=a[0]; 
			a[0]=a[3];
		}
	}

	
	private double sgn(boolean n,double v) {
		return ((n)?-(v):(v));
	}

	
	private void swap(double[] a,int i,int j) {
		a[3]=a[i]; 
		a[i]=a[j]; 
		a[j]=a[3];
	}


	/******* Spectral Decomposition *******/
	/* Compute the spectral decomposition of symmetric positive semi-definite S.
	 * Returns rotation in U and scale factors in result, so that if K is a diagonal
	 * matrix of the scale factors, then S = U K (U transpose). Uses Jacobi method.
	 * See Gene H. Golub and Charles F. Van Loan. Matrix Computations. Hopkins 1983.
	 */
	private Quaternionf spectDecomp(double[][] S, double[][] U)
	{
		double[] Diag = new double[3];
		double[] OffD = new double[3]; /* OffD is off-diag (by omitted index) */
		double g,h,fabsh,fabsOffDi,t,theta,c,s,tau,ta,OffDq,a,b;
		char[] nxt = new char[]{Y,Z,X};
		int sweep, i, j;
		mat_copy(U,"=",mat_id(),4);
		Diag[X] = S[X][X]; Diag[Y] = S[Y][Y]; Diag[Z] = S[Z][Z];
		OffD[X] = S[Y][Z]; OffD[Y] = S[Z][X]; OffD[Z] = S[X][Y];
		for (sweep=20; sweep>0; sweep--) {
			double sm = Math.abs(OffD[X])+Math.abs(OffD[Y])+Math.abs(OffD[Z]);
			if (sm==0.0) break;
			for (i=Z; i>=X; i--) {
				int p = nxt[i]; int q = nxt[p];
				fabsOffDi = Math.abs(OffD[i]);
				g = 100.0*fabsOffDi;
				if (fabsOffDi>0.0) {
					h = Diag[q] - Diag[p];
					fabsh = Math.abs(h);
					if (fabsh+g==fabsh) {
						t = OffD[i]/h;
					} else {
						theta = 0.5*h/OffD[i];
						t = 1.0/(Math.abs(theta)+Math.sqrt(theta*theta+1.0));
						if (theta<0.0) t = -t;
					}
					c = 1.0/Math.sqrt(t*t+1.0); s = t*c;
					tau = s/(c+1.0);
					ta = t*OffD[i]; OffD[i] = 0.0;
					Diag[p] -= ta; Diag[q] += ta;
					OffDq = OffD[q];
					OffD[q] -= s*(OffD[p] + tau*OffD[q]);
					OffD[p] += s*(OffDq   - tau*OffD[p]);
					for (j=Z; j>=X; j--) {
						a = U[j][p]; b = U[j][q];
						U[j][p] -= s*(b + tau*a);
						U[j][q] += s*(a - tau*b);
					}
				}
			}
		}
		return new Quaternionf((float)Diag[X], (float)Diag[Y], (float)Diag[Z], 1.0f);
	}
	

	/******* Polar Decomposition *******/
	/* Polar Decomposition of 3x3 matrix in 4x4,
	 * M = QS.  See Nicholas Higham and Robert S. Schreiber,
	 * Fast Polar Decomposition of An Arbitrary Matrix,
	 * Technical Report 88-942, October 1988,
	 * Department of Computer Science, Cornell University.
	 */
	private double polarDecomp(double[][] M, double[][] Q, double[][] S)
	{

		double TOL = 1.0e-6;
		double[][] Mk = new double[4][4];
		double[][] MadjTk = new double[4][4]; 
		double[][] Ek = new double[4][4];

		double det, M_one, M_inf, MadjT_one, MadjT_inf, E_one, gamma, g1, g2;
		int i, j;

		mat_tpose(Mk,"=",M,3);
		M_one = norm_one(Mk);  M_inf = norm_inf(Mk);

		do
		{
			adjoint_transpose(Mk, MadjTk);
			det = vdot(Mk[0], MadjTk[0]);
			if (det==0.0)
			{
				do_rank2(Mk, MadjTk, Mk);
				break;
			}

			MadjT_one = norm_one(MadjTk);
			MadjT_inf = norm_inf(MadjTk);

			gamma = Math.sqrt(Math.sqrt((MadjT_one*MadjT_inf)/(M_one*M_inf))/Math.abs(det));
			g1 = gamma*0.5;
			g2 = 0.5/(gamma*det);
			mat_copy(Ek,"=",Mk,3); // there was a duplicate function in the original!!!?
			matBinop(Mk,"=",mat_mult(Mk, g1),"+",mat_mult(MadjTk, g2),3);
			mat_copy(Ek,"-=",Mk,3);
			E_one = norm_one(Ek);
			M_one = norm_one(Mk);
			M_inf = norm_inf(Mk);

		} while(E_one>(M_one*TOL));

		mat_tpose(Q,"=",Mk,3); 
		mat_pad(Q);
		mat_mult(Mk, M, S);  
		mat_pad(S);

		for (i=0; i<3; i++)
			for (j=i; j<3; j++)
				S[i][j] = S[j][i] = 0.5*(S[i][j]+S[j][i]);
		return (det);
	}
	

	/* Construct a unit quaternion from rotation matrix.  Assumes matrix is
	 * used to multiply column vector on the left: vnew = mat vold.  Works
	 * correctly for right-handed coordinate system and right-handed rotations.
	 * Translation and perspective components ignored. */
	private Quaternionf quatFromMatrix(double[][] mat)
	{
		/* This algorithm avoids near-zero divides by looking for a large component
		 * - first w, then x, y, or z.  When the trace is greater than zero,
		 * |w| is greater than 1/2, which is as small as a largest component can be.
		 * Otherwise, the largest diagonal entry corresponds to the largest of |x|,
		 * |y|, or |z|, one of which must be larger than |w|, and at least 1/2. */
		Quaternionf qu = new Quaternionf();
		double tr, s;

		tr = mat[X][X] + mat[Y][Y]+ mat[Z][Z];
		if (tr >= 0.0)
		{
			s = Math.sqrt(tr + mat[W][W]);
			qu.setW((float)s*0.5f);
			s = 0.5 / s;
			qu.setX((float)((mat[Z][Y] - mat[Y][Z]) * s));
			qu.setY((float)((mat[X][Z] - mat[Z][X]) * s));
			qu.setZ((float)((mat[Y][X] - mat[X][Y]) * s));
		}
		else
		{
			int h = X;
			if (mat[Y][Y] > mat[X][X]) h = Y;
			if (mat[Z][Z] > mat[h][h]) h = Z;
			switch (h) {
			case X:
				s = Math.sqrt( (mat[X][X] - (mat[Y][Y]+mat[Z][Z])) + mat[W][W] );
				qu.setX((float)s*0.5f);
				s = 0.5 / s;
				qu.setY((float)((mat[X][Y] + mat[Y][X]) * s));
				qu.setZ((float)((mat[Z][X] + mat[X][Z]) * s));
				qu.setW((float)((mat[Z][Y] - mat[Y][Z]) * s));
				break;
			case Y:
				s = Math.sqrt( (mat[Y][Y] - (mat[Z][Z]+mat[X][X])) + mat[W][W] );
				qu.setY((float)s*0.5f);
				s = 0.5 / s;
				qu.setZ((float)((mat[Y][Z] + mat[Z][Y]) * s));
				qu.setX((float)((mat[X][Y] + mat[Y][X]) * s));
				qu.setW((float)((mat[X][Z] - mat[Z][X]) * s));
				break;
			case Z:
				s = Math.sqrt( (mat[Z][Z] - (mat[X][X]+mat[Y][Y])) + mat[W][W] );
				qu.setZ((float)s*0.5f);
				s = 0.5 / s;
				qu.setX((float)((mat[Z][X] + mat[X][Z]) * s));
				qu.setY((float)((mat[Y][Z] + mat[Z][Y]) * s));
				qu.setW((float)((mat[Y][X] - mat[X][Y]) * s));
				break;
			}
		}
		if (mat[W][W] != 1.0) 
			qu.scale((float)(1/Math.sqrt(mat[W][W])));
		return (qu);
	}

	/** Copy transpose of nxn matrix A to AT using "gets" for assignment
	 * 
	 * @param AT
	 * @param gets ("=")
	 * @param A
	 * @param n
	 */
	private void mat_tpose(double[][] AT, String gets , double[][] A, int n) {
		int i,j; 
		for(i=0;i<n;i++){
			for(j=0;j<n;j++){
				if(gets.equals("="))
					AT[i][j] = (A[j][i]);
			}
		}
	}

	/** Copy nxn matrix A to C using "gets" for assignment
	 * 
	 * @param C
	 * @param gets ("=", "-=")
	 * @param A
	 * @param n
	 */
	private void mat_copy(double[][] C, String gets, double[][]A, int n) {
		int i,j; 
		for(i=0;i<n;i++){
			for(j=0;j<n;j++){
				if(gets.equals("="))
					C[i][j] = (A[i][j]);
				if(gets.equals("-="))
					C[i][j] -= (A[i][j]);
			}
		}
	}

	/** Assign nxn matrix C the element-wise combination of A and B using "op"
	 * 
	 * @param C
	 * @param gets ("=")
	 * @param A
	 * @param op ("*", "+")
	 * @param B
	 * @param n
	 */
	private void matBinop(double[][] C, String gets, double[][] A, String op, double[][] B, int n) {
		int i,j; 
		for(i=0;i<n;i++){
			for(j=0;j<n;j++){
				double operation = 0;
				if(op.equals("*"))
					operation = (A[i][j]) * (B[i][j]);
				if(op.equals("+"))
					operation = (A[i][j]) + (B[i][j]);
				if(gets.equals("="))
					C[i][j] = operation;
			}
		}
	}

	/**
	 * Returns the scalar multiplication of the provided matrix
	 * @param M
	 * @param factor
	 * @return
	 */
	private double[][] mat_mult(double[][] M, double factor){
		double[][] ret = new double[M.length][M[0].length];
		for(int i = 0; i < M.length; i++)
			for(int j = 0; j < M[0].length; j++)
				ret[i][j] = M[i][j] * factor;
		return ret;
	}

	/** Multiply the upper left 3x3 parts of A and B to get AB
	 * 
	 * @param A
	 * @param B
	 * @param AB
	 */
	private void mat_mult(double[][] A, double[][] B, double[][] AB)
	{
		int i, j;
		for (i=0; i<3; i++) 
			for (j=0; j<3; j++)
				AB[i][j] = A[i][0]*B[0][j] + A[i][1]*B[1][j] + A[i][2]*B[2][j];
	}

	private double mat_norm(double[][] M, boolean tpose)
	{
		int i;
		double sum, max;
		max = 0.0;
		for (i=0; i<3; i++) {
			if(tpose) 
				sum = Math.abs(M[0][i])+Math.abs(M[1][i])+Math.abs(M[2][i]);
			else       
				sum = Math.abs(M[i][0])+Math.abs(M[i][1])+Math.abs(M[i][2]);
			if(max<sum)
				max = sum;
		}
		return max;
	}

	private double norm_inf(double[][] M) {
		return mat_norm(M, false);
	}

	private double norm_one(double[][] M) {
		return mat_norm(M, true);
	}

	/** Set MadjT to transpose of inverse of M times determinant of M **/
	private void adjoint_transpose(double[][] M, double[][] MadjT)
	{
		vcross(M[1], M[2], MadjT[0]);
		vcross(M[2], M[0], MadjT[1]);
		vcross(M[0], M[1], MadjT[2]);
	}

	private void vcross(double[] va, double[] vb, double[] v)
	{
		v[0] = va[1]*vb[2] - va[2]*vb[1];
		v[1] = va[2]*vb[0] - va[0]*vb[2];
		v[2] = va[0]*vb[1] - va[1]*vb[0];
	}

	/** Return dot product of length 3 vectors va and vb **/
	private double vdot(double[] va, double[] vb)
	{
		return (va[0]*vb[0] + va[1]*vb[1] + va[2]*vb[2]);
	}

	/** Setup u for Household reflection to zero all v components but first
	 * 
	 * @param v
	 * @param u
	 */
	private void make_reflector(double[] v, double[] u)
	{
		double s = Math.sqrt(vdot(v, v));
		u[0] = v[0]; 
		u[1] = v[1];
		u[2] = v[2] + ((v[2]<0.0) ? -s : s);
		s = Math.sqrt(2.0/vdot(u, u));
		u[0] = u[0]*s; 
		u[1] = u[1]*s; 
		u[2] = u[2]*s;
	}

	/** Apply Householder reflection represented by u to column vectors of M
	 * 
	 * @param M
	 * @param u
	 */
	private void reflect_cols(double[][] M, double[] u)
	{
		int i, j;
		for (i=0; i<3; i++) {
			double s = u[0]*M[0][i] + u[1]*M[1][i] + u[2]*M[2][i];
			for (j=0; j<3; j++) 
				M[j][i] -= u[j]*s;
		}
	}

	/** Apply Householder reflection represented by u to row vectors of M
	 * 
	 * @param M
	 * @param u
	 */
	private void reflect_rows(double[][] M, double[] u)
	{
		int i, j;
		for (i=0; i<3; i++) {
			double s = vdot(u, M[i]);
			for (j=0; j<3; j++) 
				M[i][j] -= u[j]*s;
		}
	}

	/** Find orthogonal factor Q of rank 2 (or less) M using adjoint transpose 
	 * 
	 * @param M
	 * @param MadjT
	 * @param Q
	 */
	private void do_rank2(double[][] M, double[][] MadjT, double[][] Q)
	{
		double[] v1 = new double[3];
		double[] v2 = new double[3];
		double w, x, y, z, c, s, d;
		int col;
		/* If rank(M) is 2, we should find a non-zero column in MadjT */
		col = find_max_col(MadjT);
		if (col<0){
			do_rank1(M, Q); 
			return;
		} /* Rank<2 */
		v1[0] = MadjT[0][col]; 
		v1[1] = MadjT[1][col]; 
		v1[2] = MadjT[2][col];
		make_reflector(v1, v1);
		reflect_cols(M, v1);
		vcross(M[0], M[1], v2);
		make_reflector(v2, v2); 
		reflect_rows(M, v2);
		w = M[0][0]; x = M[0][1]; y = M[1][0]; z = M[1][1];
		if (w*z>x*y) {
			c = z+w; 
			s = y-x; 
			d = Math.sqrt(c*c+s*s); 
			c = c/d; 
			s = s/d;
			Q[0][0] = Q[1][1] = c; 
			Q[0][1] = -(Q[1][0] = s);
		} else {
			c = z-w; s = y+x; d = Math.sqrt(c*c+s*s); c = c/d; s = s/d;
			Q[0][0] = -(Q[1][1] = c); Q[0][1] = Q[1][0] = s;
		}
		Q[0][2] = Q[2][0] = Q[1][2] = Q[2][1] = 0.0; Q[2][2] = 1.0;
		reflect_cols(Q, v1); reflect_rows(Q, v2);
	}

	/** Find orthogonal factor Q of rank 1 (or less) M **/
	private void do_rank1(double[][] M, double[][] Q)
	{
		double[] v1 = new double[3];
		double[] v2 = new double[3];
		double s;
		int col;
		mat_copy(Q,"=",mat_id(),4);
		/* If rank(M) is 1, we should find a non-zero column in M */
		col = find_max_col(M);
		if (col<0) return; /* Rank is 0 */
		v1[0] = M[0][col]; 
		v1[1] = M[1][col]; 
		v1[2] = M[2][col];
		make_reflector(v1, v1); 
		reflect_cols(M, v1);
		v2[0] = M[2][0]; 
		v2[1] = M[2][1]; 
		v2[2] = M[2][2];
		make_reflector(v2, v2); 
		reflect_rows(M, v2);
		s = M[2][2];
		if (s<0.0) Q[2][2] = -1.0;
		reflect_cols(Q, v1); 
		reflect_rows(Q, v2);
	}

	/** Return index of column of M containing maximum abs entry, or -1 if M=0
	 * 
	 * @param M
	 * @return
	 */
	private int find_max_col(double[][] M)
	{
		double abs, max;
		int i, j, col;
		max = 0.0; col = -1;
		for (i=0; i<3; i++) for (j=0; j<3; j++) {
			abs = M[i][j]; if (abs<0.0) abs = -abs;
			if (abs>max) {max = abs; col = j;}
		}
		return col;
	}

	private static double[][] mat_id(){
		return new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	}

	/** Fill out 3x3 matrix to 4x4 **/
	private void mat_pad(double[][] A){
		A[W][X]=A[X][W]=A[W][Y]=A[Y][W]=A[W][Z]=A[Z][W]=0;
		A[W][W]=1;
	}

	public Matrix4x4f clone(){
		return new Matrix4x4f(this);
	}

	/**
	 * Create a string representation of this matrix.
	 * 
	 * @return  String representing this matrix.
	 */
	public String toString()
	{
		String string = "Matrix4x4:";
		for (int i=0; i<4; i++) {
			string += (i == 0)?"[[":"\n[";
			for (int j=0; j<4; j++)
				string += getElement(i,j) + "\t";
			string += "]";
		}
		string += "]";

		return string;
	}

	public static void main(String[] args) {
		Quaternionf rot = new Quaternionf(20f, 30f, 40f);
		Vector3f trans = new Vector3f(10f, 20f, 30f);
		Vector3f scale = new Vector3f(1f, 2f, 3f);
		
		Matrix4x4f mat = new Matrix4x4f();
		mat.scale(scale);
		mat.rotate(rot);
		mat.translate(trans);
		
		Quaternionf de_rot = new Quaternionf();
		Vector3f de_trans = new Vector3f();
		Vector3f de_scale = new Vector3f();
		Quaternionf de_so = new Quaternionf();
		
		mat.decompose(de_trans, de_rot, de_scale, de_so);
		
		System.out.println(rot.toString());
		System.out.println(de_rot.toString());
		System.out.println(de_trans.toString());
		System.out.println(de_scale.toString());
		System.out.println(de_so.toString());
		
		/* multiplying matrices */

	}

}
