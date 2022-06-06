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

package com.tecartlab.tools.threedee;

import com.tecartlab.tools.math.la.Matrix4x4f;
import com.tecartlab.tools.Const;

public class Frustum {

	public float left, right, bottom, top, zNear, zFar;

	private float fovy, aspectRatio;

	private boolean isOrtho = false;

	/**
	 * Creates a standard Frustum
	 */
	public Frustum(){
		reset();
	}

	/**
	 * Creates a Perspective Frustum Instance based on the provided value array
	 *
	 * @param _values float array [_left, _right, _bottom, _top, _zNear, _zFar]
	 */
	public Frustum(float[] _values){
		left = _values[0];
		right = _values[1];
		bottom = _values[2];
		top = _values[3];
		zNear = _values[4];
		zFar = _values[5];
		updateFovy();
	}

	/**
	 * Creates a Frustum Instance based on the provided frustum values
	 *
	 * @param _isOrtho	if true sets this Frustum orthogonal
	 * @param _left
	 * @param _right
	 * @param _bottom
	 * @param _top
	 * @param _zNear
	 * @param _zFar
	 */
	public Frustum(boolean _isOrtho, float _left, float _right,
			float _bottom, float _top,
			float _zNear, float _zFar){
		isOrtho = _isOrtho;
		left = _left;
		right = _right;
		bottom = _bottom;
		top = _top;
		zNear = _zNear;
		zFar = _zFar;
		updateFovy();
	}

	/**
	 * Creates a perspective frustum instance based on fov and aspectratio
	 * @param _fovy
	 * @param _aspectRatio
	 * @param _zNear
	 * @param _zFar
	 */
	public Frustum(float _fovy, float _aspectRatio,
			float _zNear, float _zFar){
		fovy = _fovy;
		aspectRatio = _aspectRatio;
		zNear = _zNear;
		zFar = _zFar;
		update();
	}

	/**
	 * Creates a frustum instance based on a projection matrix. If the matrix is not a valid
	 * projection matrix it will be set to a standard frustum
	 * @param fm
	 */
	public Frustum(Matrix4x4f fm){
	    if (fm.getElement(0, 3)!=0.0 ||
	    		fm.getElement(1, 3)!=0.0 ||
	    		fm.getElement(2, 3)!=-1.0 ||
	    		fm.getElement(3, 3)!=0.0) {
		    reset();
	    } else {
	    	// only othografical projection matrices have this fields set
	    	if(fm.getElement(3, 0) != 0.0 || fm.getElement(3, 1) != 0.0){
		        zNear = (fm.getElement(3, 2)+1.0f) / fm.getElement(2, 2);
		        zFar = (fm.getElement(3, 2)-1.0f) / fm.getElement(2, 2);

		        left = -(1.0f+fm.getElement(3, 0)) / fm.getElement(0, 0);
		        right = (1.0f-fm.getElement(3, 0)) / fm.getElement(0, 0);

		        bottom = -(1.0f+fm.getElement(3, 1)) / fm.getElement(1, 1);
		        top = (1.0f-fm.getElement(3, 1)) / fm.getElement(1,1);

		        isOrtho = true;
	    	} else {
			    zNear = fm.getElement(3, 2) / (fm.getElement(2, 2)-1.0f);
			    zFar = fm.getElement(3, 2) / (1.0f+fm.getElement(2, 2));

			    left = zNear * (fm.getElement(2, 0)-1.0f) / fm.getElement(0,0);
			    right = zNear * (1.0f+fm.getElement(2,0)) / fm.getElement(0,0);

			    top = zNear * (1.0f+fm.getElement(2, 1)) / fm.getElement(1,1);
			    bottom = zNear * (fm.getElement(2,1)-1.0f) / fm.getElement(1,1);
			    updateFovy();
	    	}
	    }
	}

	public Frustum clone(){
		return new Frustum(get());
	}

	public boolean isOrtho(){
		return isOrtho;
	}


	/**
	 * Returns the Frustum values in form of an float array
	 *
	 * return float array [_left, _right, _bottom, _top, _zNear, _zFar]
	 */
	public float[] get(){
		float[] _values = new float[6];
		_values[0] = left;
		_values[1] = right;
		_values[2] = bottom;
		_values[3] = top;
		_values[4] = zNear;
		_values[5] = zFar;
		return _values;

	}


	public float getFOV(){
		updateFovy();
		return fovy;
	}

	public float getAspectRatio(){
		updateFovy();
		return aspectRatio;
	}

	public Matrix4x4f getProjectionMatrix(){
		return new Matrix4x4f(this);
	}

	private void reset(){
		left = -0.05f;
		right = 0.05f;
		bottom = -0.05f;
		top = 0.05f;
		zNear = .1f;
		zFar = 100.0f;
		updateFovy();
	}

	private void update(){
	    // calculate the appropriate left, right etc.
	    float tan_fovy = (float)Math.tan(fovy*0.5f*Const.DEG_TO_RAD);
	    right  =  tan_fovy * aspectRatio * zNear;
	    left   = -right;
	    top    =  tan_fovy * zNear;
	    bottom =  -top;
	}

	private void updateFovy(){
		fovy = (float)((Math.atan(top/zNear)-Math.atan(bottom/zNear))*Const.RAD_TO_DEG);
	    aspectRatio = (right-left)/(top-bottom);
	}

	public String toString(){
		return "Frustum: " + left + " " + right + " " + bottom + " " + top + " " + zNear + " " + zFar;
	}

}
