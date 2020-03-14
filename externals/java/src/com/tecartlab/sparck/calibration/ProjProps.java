package com.tecartlab.sparck.calibration;
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

import java.util.ArrayList;
import java.util.Iterator;

import com.cycling74.max.MaxSystem;

import com.tecartlab.mxj.utils.pattr.*;
import com.tecartlab.tools.math.la.Quaternionf;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.threedee.Camera;
import com.tecartlab.tools.threedee.Node;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynSubscriber;
import com.tecartlab.utils.dyndist.DynSubscription;

public class ProjProps {

	public static final int IDX_lensDistance 		= 0;
	public static final int IDX_screenWidth			= 1;
	public static final int IDX_screenHeight		= 2;
	public static final int IDX_frustumShiftY		= 3;
	public static final int IDX_frustumShiftX		= 4;
	public static final int IDX_polarAngle			= 5;
	public static final int IDX_rollAngle			= 6;
	public static final int IDX_tiltAngle			= 7;
	public static final int IDX_azimuthAngle		= 8;
	public static final int IDX_off					= 9;
	public static final int IDX_lookAtShiftOffsetX	= 10;
	public static final int IDX_lookAtShiftOffsetY	= 11;
	public static final int IDX_lookAtShiftX		= 12;
	public static final int IDX_lookAtShiftY		= 13;
	public static final int IDX_frustumNearClipping	= 14;
	public static final int IDX_frustumFarClipping	= 15;
	public static final int IDX_showProjectorInPreview	= 16;
	public static final int IDX_showFrustumInPreview	= 17;
	public static final int IDX_lookAtTargetPosX		= 18;
	public static final int IDX_lookAtTargetPosY		= 19;
	public static final int IDX_lookAtTargetPosZ		= 20;
	public static final int IDX_lookAtTargetRotX		= 21;
	public static final int IDX_lookAtTargetRotY		= 22;
	public static final int IDX_lookAtTargetRotZ		= 23;
	public static final int IDX_stageOffsetPosX		= 24;
	public static final int IDX_stageOffsetPosY		= 25;
	public static final int IDX_stageOffsetPosZ		= 26;
	public static final int IDX_stageOffsetRotX		= 27;
	public static final int IDX_stageOffsetRotY		= 28;
	public static final int IDX_stageOffsetRotZ		= 29;
	public static final int IDX_stagePosX		= 30;
	public static final int IDX_stagePosY		= 31;
	public static final int IDX_stagePosZ		= 32;
	public static final int IDX_stageRotX		= 33;
	public static final int IDX_stageRotY		= 34;
	public static final int IDX_stageRotZ		= 35;
	public float[] properties;

	String[] propertyNames;


	public ProjProps(){
		properties = new float[36];
		propertyNames = new String[36];

		propertyNames[IDX_off] 						= "off";
		propertyNames[IDX_lensDistance] 			= "lensDistance";
		propertyNames[IDX_screenHeight] 			= "screenHeight";
		propertyNames[IDX_screenWidth] 				= "screenWidth";
		propertyNames[IDX_frustumShiftY] 			= "frustumShiftY";
		propertyNames[IDX_frustumShiftX] 			= "frustumShiftX";
		propertyNames[IDX_polarAngle] 				= "polarAngle";
		propertyNames[IDX_rollAngle] 				= "rollAngle";
		propertyNames[IDX_tiltAngle] 				= "tiltAngle";
		propertyNames[IDX_azimuthAngle]				= "azimuthAngle";
		propertyNames[IDX_lookAtShiftOffsetX] 		= "lookAtShiftOffsetX";
		propertyNames[IDX_lookAtShiftOffsetY] 		= "lookAtShiftOffsetY";
		propertyNames[IDX_lookAtShiftX] 			= "lookAtShiftX";
		propertyNames[IDX_lookAtShiftY] 			= "lookAtShiftY";
		propertyNames[IDX_frustumNearClipping] 		= "frustumNearClipping";
		propertyNames[IDX_frustumFarClipping] 		= "frustumFarClipping";
		propertyNames[IDX_showProjectorInPreview] 	= "showProjectorInPreview";
		propertyNames[IDX_showFrustumInPreview] 	= "showFrustumInPreview";
		propertyNames[IDX_lookAtTargetPosX] 		= "lookAtTargetPosX";
		propertyNames[IDX_lookAtTargetPosY] 		= "lookAtTargetPosY";
		propertyNames[IDX_lookAtTargetPosZ] 		= "lookAtTargetPosZ";
		propertyNames[IDX_lookAtTargetRotX] 		= "lookAtTargetRotX";
		propertyNames[IDX_lookAtTargetRotY] 		= "lookAtTargetRotY";
		propertyNames[IDX_lookAtTargetRotZ] 		= "lookAtTargetRotZ";
		propertyNames[IDX_stageOffsetPosX] 			= "stageOffsetPosX";
		propertyNames[IDX_stageOffsetPosY] 			= "stageOffsetPosY";
		propertyNames[IDX_stageOffsetPosZ] 			= "stageOffsetPosZ";
		propertyNames[IDX_stageOffsetRotX] 			= "stageOffsetRotX";
		propertyNames[IDX_stageOffsetRotY] 			= "stageOffsetRotY";
		propertyNames[IDX_stageOffsetRotZ] 			= "stageOffsetRotZ";
		propertyNames[IDX_stagePosX] 				= "stagePosX";
		propertyNames[IDX_stagePosY] 				= "stagePosY";
		propertyNames[IDX_stagePosZ] 				= "stagePosZ";
		propertyNames[IDX_stageRotX] 				= "stageRotX";
		propertyNames[IDX_stageRotY] 				= "stageRotY";
		propertyNames[IDX_stageRotZ] 				= "stageRotZ";
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
		Node greatParent = camera.getParent().getParent();
		Node greatGreatParent = greatParent.getParent();
		Node greatGreatGreatParent = greatGreatParent.getParent();

		greatParent.setOrientation(new Vector3f(properties[IDX_lookAtTargetRotX], properties[IDX_lookAtTargetRotY], properties[IDX_lookAtTargetRotZ]));
		greatParent.setPosition(new Vector3f(properties[IDX_lookAtTargetPosX], properties[IDX_lookAtTargetPosY], properties[IDX_lookAtTargetPosZ]));

		greatGreatParent.setOrientation(new Vector3f(properties[IDX_stageOffsetRotX], properties[IDX_stageOffsetRotY], properties[IDX_stageOffsetRotZ]));
		greatGreatParent.setPosition(new Vector3f(properties[IDX_stageOffsetPosX], properties[IDX_stageOffsetPosY], properties[IDX_stageOffsetPosZ]));

		greatGreatGreatParent.setOrientation(new Vector3f(properties[IDX_stageRotX], properties[IDX_stageRotY], properties[IDX_stageRotZ]));
		greatGreatGreatParent.setPosition(new Vector3f(properties[IDX_stagePosX], properties[IDX_stagePosY], properties[IDX_stagePosZ]));

		camera.setEulerRotationSequence(3, 1, 2);
	}

	/**
	 * Store the numbercruncher Properties into the provided Camera object.
	 *
	 * @param camera the properties are stored in
	 */
	public void applyDynamicsTo(Camera camera){
		Node parent = camera.getParent();

	    // update frustum
	    float focalDistance = properties[IDX_lensDistance];
	    float frustumWith = properties[IDX_frustumNearClipping] * properties[IDX_screenWidth] / (focalDistance);
	    float frustumHeight = properties[IDX_frustumNearClipping] * properties[IDX_screenHeight] / (focalDistance);

	    camera.frustum.left = - properties[IDX_frustumNearClipping] * properties[IDX_frustumShiftX] / focalDistance - frustumWith / 2.f;
	    camera.frustum.right = - properties[IDX_frustumNearClipping] * properties[IDX_frustumShiftX] / focalDistance + frustumWith / 2.f;
	    camera.frustum.bottom = - properties[IDX_frustumNearClipping] * properties[IDX_frustumShiftY] / focalDistance - frustumHeight / 2.f;
	    camera.frustum.top = - properties[IDX_frustumNearClipping] * properties[IDX_frustumShiftY] / focalDistance + frustumHeight / 2.f;
	    camera.frustum.zNear = properties[IDX_frustumNearClipping];
	    camera.frustum.zFar = properties[IDX_frustumFarClipping];

	    Vector3f parorient = new Vector3f(90.0f - properties[IDX_polarAngle], properties[IDX_rollAngle], properties[IDX_azimuthAngle]);
	    parent.setOrientation(parorient);

	    Vector3f camorient = new Vector3f(
	    		(float)(Math.atan((properties[IDX_frustumShiftY] - (properties[IDX_lookAtShiftY] + properties[IDX_lookAtShiftOffsetY]) * properties[IDX_screenHeight])/focalDistance)*180.0/Math.PI),
	    		(float)(-Math.atan((properties[IDX_frustumShiftX] + (properties[IDX_lookAtShiftX] + properties[IDX_lookAtShiftOffsetX]) * properties[IDX_screenWidth])/focalDistance)*180.0/Math.PI),
	    		properties[IDX_tiltAngle]);
	    camera.setOrientation(camorient);

	    camera.setPosition(new Vector3f(0, 0, focalDistance));
	}

	protected ProjProps clone(){
		ProjProps clone = new ProjProps();
		clone.properties = properties.clone();
		return clone;
	}

	protected int getIndex(String propName){
		for(int i = 0; i < propertyNames.length; i++){
			if(propertyNames[i].equals(propName))
				return i;
		}
		return -1;
	}

	protected String getPropertyName(int index){
		return propertyNames[index];
	}

}
