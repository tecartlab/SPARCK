package com.tecartlab.sparck.calib;
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

import java.util.Hashtable;
import java.util.Vector;

import com.cycling74.max.Atom;

import com.tecartlab.sparck.calibration.ProjPropsSubscribed.Listener;
import com.tecartlab.tools.math.la.Matrix4x4f;
import com.tecartlab.tools.math.la.Quaternionf;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.threedee.Camera;
import com.tecartlab.utils.Debug;

public class ProjProps {

	public static final String IDX_parent_ImplementParent 	= "parent::implementParent";
	public static final String IDX_parent_parentMatrix 		= "parent::parent::matrix";
	public static final String IDX_calibration_matrix 		= "calibration::matrix";
	public static final String IDX_beamer_matrix 			= "beamer::matrix";
	public static final String IDX_calibObjOffset_use 		= "calibObjOffset::use";
	public static final String IDX_calibObjOffset_posX 		= "calibObjOffset::PosX";
	public static final String IDX_calibObjOffset_posY 		= "calibObjOffset::PosY";
	public static final String IDX_calibObjOffset_posZ 		= "calibObjOffset::PosZ";
	public static final String IDX_calibObjOffset_rotX 		= "calibObjOffset::RotX";
	public static final String IDX_calibObjOffset_rotY 		= "calibObjOffset::RotY";
	public static final String IDX_calibObjOffset_rotZ 		= "calibObjOffset::RotZ";
	public static final String IDX_calibObjOffset_ScaleXYZ 	= "calibObjOffset::ScaleXYZ";
	public static final String IDX_intrinsic_frustumNear 	= "intrinsic::frustumNear";
	public static final String IDX_intrinsic_frustumFar 	= "intrinsic::frustumFar";
	public static final String IDX_intrinsic_frustumLeft 	= "intrinsic::frustumLeft";
	public static final String IDX_intrinsic_frustumRight 	= "intrinsic::frustumRight";
	public static final String IDX_intrinsic_frustumBottom 	= "intrinsic::frustumBottom";
	public static final String IDX_intrinsic_frustumTop 	= "intrinsic::frustumTop";
	public static final String IDX_resolution_height 		= "resolution::height";
	public static final String IDX_resolution_width 		= "resolution::width";
	public static final String IDX_flag_fixAspectRatio 		= "flags::fixAspectRatio";
	public static final String IDX_flag_zeroTangentDistance = "flags::zeroTangentDistance";
	public static final String IDX_flag_fixK1 				= "flags::fixK1";
	public static final String IDX_flag_fixK2 				= "flags::fixK2";
	public static final String IDX_flag_fixK3 				= "flags::fixK3";
	public static final String IDX_flag_fixK4 				= "flags::fixK4";
	public static final String IDX_flag_fixK5 				= "flags::fixK5";
	public static final String IDX_flag_fixK6 				= "flags::fixK6";
	public static final String IDX_flag_fixIntrinsic 		= "flags::fixIntrinsic";
	//public static final String IDX_flag_useIntrinsicGuess 	= "flags::useIntrinsicGuess";
	//public static final String IDX_flag_fixPrincipalPoint 	= "flags::fixPrincipalPoint";
	//public static final String IDX_flag_fixK1 				= "flags::fixK1";

	public Hashtable<String, Atom[]> properties;
	private Vector<String> changedProps;

	String[] propertyNames;

	private boolean isOperational;

	private Listener listener;

	public ProjProps(Listener _listener){
		listener = _listener;
		properties = new Hashtable<String, Atom[]>();
		changedProps = new Vector<String>();
		isOperational = false;
	}

	public void setOperational(){
		isOperational = true;
	}

	public boolean isOperational(){
		return isOperational;
	}

	/**
	 * Store all the Properties into the provided Camera object
	 * @param camera
	 */
	public void applyTo(Camera camera){
		applyStaticsTo(camera);
		applyDynamicsTo(camera);
	}

	/**
	 * Store the static Properties into the provided Camera object.
	 *
	 * @return
	 */
	public void applyStaticsTo(Camera camera){

		camera.setEulerRotationSequence(3, 1, 2);
	}

	/**
	 * Store the numbercruncher Properties into the provided Camera object.
	 *
	 * @param camera the properties are stored in
	 */
	public void applyDynamicsTo(Camera camera){

	    camera.frustum.left = getFloat(IDX_intrinsic_frustumLeft);
	    camera.frustum.right =  getFloat(IDX_intrinsic_frustumRight);
	    camera.frustum.bottom = getFloat(IDX_intrinsic_frustumBottom);
	    camera.frustum.top =  getFloat(IDX_intrinsic_frustumTop);
	    camera.frustum.zNear = getFloat(IDX_intrinsic_frustumNear);
	    camera.frustum.zFar = getFloat(IDX_intrinsic_frustumFar);

	    if(properties.containsKey(IDX_calibration_matrix))
	    	camera.setMatrix(new Matrix4x4f(getArray(IDX_calibration_matrix)));
	}

	/**
	 * sets a new value for the specified address
	 * @param _address property address
	 * @param _args values
	 */
	public void set(String _address, Atom[] _args){
		properties.put(_address, _args);
		changedProps.addElement(_address);
	}

	/**
	 * sets new values for the specified address
	 * @param _address property address
	 * @param _args values
	 */
	public void set(String _address, float[] _args){
		set(_address, Atom.newAtom(_args));
	}

	/**
	 * sets new value for the specified address
	 * @param _address property address
	 * @param _args values
	 */
	public void set(String _address, float _args){
		set(_address, new Atom[]{Atom.newAtom(_args)});
	}

	/**
	 * gets the original value
	 * @param _address property address
	 * @return the value or null if the address doesn't exist
	 */
	public Atom[] get(String _address){
		return properties.get(_address);
	}

	/**
	 * Gets the first value as a float
	 * @param _address property address
	 * @return first value as float or 0 if _address is unknown
	 */
	public float getFloat(String _address){
		Atom[] pros = properties.get(_address);
		if(pros != null){
			return pros[0].getFloat();
		}
		return 0;
	}

	/**
	 * Gets the first value as a int
	 * @param _address property address
	 * @return first value as float or 0 if _address is unknown
	 */
	public int getInt(String _address){
		Atom[] pros = properties.get(_address);
		if(pros != null){
			return pros[0].getInt();
		}
		return 0;
	}

	/**
	 * gets the array as float array
	 * @param _address property address
	 * @return
	 */
	public float[] getArray(String _address){
		Atom[] pros = properties.get(_address);
		if(pros != null){
			return Atom.toFloat(pros);
		}
		return null;
	}

	/**
	 * checks if this property has a value
	 * @param _address property address
	 * @return true if this is the case.
	 */
	public boolean hasAddress(String _address){
		return properties.containsKey(_address);
	}

	/**
	 * Called to update the current value with the ouside PattrStorage
	 * @param _address
	 * @param _value
	 */
	public void pattrSet(String _address, Atom[] _value){
		properties.put(_address, _value);
	}

	/**
	 * Tell this properties listener of changes at the properties
	 * Is called if max'properties were changed and this properties were updated
	 */
	public void changeEvent(){
		if(listener != null)
			listener.propertyEvent(changedProps);
		changedProps.clear();
	}

	public interface Listener{
		public void propertyEvent(Vector<String> _changedProps);
	}

}
