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

import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.cycling74.jitter.JitterObject;

import com.tecartlab.jay3dee.model.Vertice;
import com.tecartlab.mxj.utils.JitterObjectArray;
import com.tecartlab.tools.math.la.Matrix4x4f;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.threedee.Camera;
import com.tecartlab.utils.Debug;

public class CalibVertice {

	protected int isDoubleSelected;

	protected boolean isEnabled;

	protected static final String ELEMENT_VERTICE = "vertice";
	private static final String ATTR_TYPE = "type";
	private static final String ATTR_INDEX = "index";
	private static final String ATTR_ORIGIN = "origin";
	private static final String ATTR_LOCAL_X = "localX";
	private static final String ATTR_LOCAL_Y = "localY";
	private static final String ATTR_LOCAL_Z = "localZ";
	private static final String ATTR_TARGET_X = "targetX";
	private static final String ATTR_TARGET_Y = "targetY";
	private static final String ATTR_WARP_X = "warpX";
	private static final String ATTR_WARP_Y = "warpY";

	protected static int TYPE_NONE = 0;
	protected static int TYPE_ALL = 1;
	protected static int TYPE_TARGET = 2;
	protected static int TYPE_WARP = 3;

	private Vector3f modelWorldVertice;
	private Vector3f modelLocalVertice;
	private Vector3f modelScreenVertice;
	private Vector3f targetVertice;
	private Vector3f warpVertice;

	protected JitterObject titleText;
	protected JitterObject infoText;

	private boolean isReady = false;

	private int type = 0;

	private int index = 0;

	public float calibrationError = -1;

	protected CalibVertice(Vector3f _modelLocal, Vector3f _modelWorld, boolean isTargetType, boolean isWarpType){
		modelLocalVertice = _modelLocal;
		modelWorldVertice = _modelWorld;
		setTargetType(isTargetType);
		setWarpType(isWarpType);
		createTextObject();
		index = -1;
	}

	protected CalibVertice(Node vertice){
		NamedNodeMap values = vertice.getAttributes();
		Node s_type = values.getNamedItem(ATTR_TYPE);
		Node s_index = values.getNamedItem(ATTR_INDEX);
		Node s_origin = values.getNamedItem(ATTR_ORIGIN);
		Node s_localX = values.getNamedItem(ATTR_LOCAL_X);
		Node s_localY = values.getNamedItem(ATTR_LOCAL_Y);
		Node s_localZ = values.getNamedItem(ATTR_LOCAL_Z);
		Node s_targetX = values.getNamedItem(ATTR_TARGET_X);
		Node s_targetY = values.getNamedItem(ATTR_TARGET_Y);
		Node s_warpX = values.getNamedItem(ATTR_WARP_X);
		Node s_warpY = values.getNamedItem(ATTR_WARP_Y);

		type = (Integer.valueOf(s_type.getTextContent())).intValue();
		index = (Integer.valueOf(s_index.getTextContent())).intValue();
		modelLocalVertice = new Vector3f(
				(Float.valueOf(s_localX.getTextContent())).floatValue(),
				(Float.valueOf(s_localY.getTextContent())).floatValue(),
				(Float.valueOf(s_localZ.getTextContent())).floatValue());
		modelWorldVertice = modelLocalVertice.clone();
		targetVertice = new Vector3f(
				(Float.valueOf(s_targetX.getTextContent())).floatValue(),
				(Float.valueOf(s_targetY.getTextContent())).floatValue(), 0.f);
		warpVertice = new Vector3f(
				(Float.valueOf(s_warpX.getTextContent())).floatValue(),
				(Float.valueOf(s_warpY.getTextContent())).floatValue(), 0.f);
		createTextObject();
	}

	protected void addNodeTo(Document document){
        Element verticeNode  = document.createElement(ELEMENT_VERTICE);
        Attr s_index = document.createAttribute(ATTR_INDEX);
        s_index.setValue(""+index);
        verticeNode.setAttributeNode(s_index);

        Attr s_type = document.createAttribute(ATTR_TYPE);
        s_type.setValue("" +type);
        verticeNode.setAttributeNode(s_type);

        Attr s_localX = document.createAttribute(ATTR_LOCAL_X);
        s_localX.setValue(""+modelLocalVertice.x());
        verticeNode.setAttributeNode(s_localX);
        Attr s_localY = document.createAttribute(ATTR_LOCAL_Y);
        s_localY.setValue(""+modelLocalVertice.y());
        verticeNode.setAttributeNode(s_localY);
        Attr s_localZ = document.createAttribute(ATTR_LOCAL_Z);
        s_localZ.setValue(""+modelLocalVertice.z());
        verticeNode.setAttributeNode(s_localZ);

        Attr s_targetX = document.createAttribute(ATTR_TARGET_X);
        s_targetX.setValue(""+targetVertice.x());
        verticeNode.setAttributeNode(s_targetX);
        Attr s_targetY = document.createAttribute(ATTR_TARGET_Y);
        s_targetY.setValue(""+targetVertice.y());
        verticeNode.setAttributeNode(s_targetY);

        Attr s_warpX = document.createAttribute(ATTR_WARP_X);
        s_warpX.setValue(""+warpVertice.x());
        verticeNode.setAttributeNode(s_warpX);
        Attr s_warpY = document.createAttribute(ATTR_WARP_Y);
        s_warpY.setValue(""+warpVertice.y());
        verticeNode.setAttributeNode(s_warpY);

        document.getDocumentElement().appendChild(verticeNode);
	}

	protected void setContext(String[] _context){
		//drawableText.call("drawto", _context);
		titleText.call("drawto", "editor");
		titleText.call("layer", 21);
		titleText.call("transform_reset", 2);
		titleText.call("font", "Courier New");
		titleText.call("face", "bold");
		titleText.call("scale", 1f);
		titleText.call("size", 20);

		infoText.call("drawto", "editor");
		infoText.call("layer", 21);
		infoText.call("transform_reset", 2);
		infoText.call("font", "Courier New");
		infoText.call("face", "bold");
		infoText.call("scale", 1f);
		infoText.call("size", 15);
	}

	protected void setIndex(int _index){
		index = _index;
	}

	protected int getIndex(){
		return index;
	}

	/**
	 * Set the vertice's target type.
	 * @param _traget
	 * @return false if the vertice is no type anymore
	 */
	public boolean setTargetType(boolean _traget){
		if(_traget){
			if(type == TYPE_NONE)
				type = TYPE_TARGET;
			if(type == TYPE_WARP)
				type = TYPE_ALL;
		} else {
			if(type == TYPE_ALL)
				type = TYPE_WARP;
			if(type == TYPE_TARGET){
				type = TYPE_NONE;
				return false;
			}
		}
		return true;
	}

	/**
	 * Set the vertice's warp type.
	 * @param _traget
	 * @return false if the vertice is no type anymore
	 */
	public boolean setWarpType(boolean _warp){
		if(_warp){
			if(type == TYPE_NONE)
				type = TYPE_WARP;
			if(type == TYPE_TARGET)
				type = TYPE_ALL;
		} else {
			if(type == TYPE_ALL)
				type = TYPE_TARGET;
			if(type == TYPE_WARP){
				type = TYPE_NONE;
				return false;
			}
		}
		return true;
	}

	public boolean isTargetType(){
		return (type == TYPE_ALL || type == TYPE_TARGET)?true: false;
	}

	public boolean isWarpType(){
		return (type == TYPE_ALL)?true: false;
	}

	public boolean isWarpOnlyType(){
		return (type == TYPE_WARP)?true: false;
	}

	public Vector3f orthoNormalize(Vector3f _transform){
		return new Vector3f(2.f * _transform.x() - 1.f, 2.f * _transform.y() - 1.f, 0.f);
	}

	public boolean isReady(){
		return isReady;
	}

	private void setReady(){
		if(modelScreenVertice != null && targetVertice != null)
			isReady = true;
	}

	public void setModelScreenVertice(Camera _camera){
		Matrix4x4f vpm = _camera.getViewProjectionMatrix();
		modelScreenVertice  = _camera.worldToScreen(modelWorldVertice, vpm, _camera.viewport);
		setReady();
	}

	public void setModelScreenVertice(Vector3f _vert){
		modelScreenVertice  = _vert;
		setReady();
	}

	public void setTargetVertice(Vector3f _targetVertice){
		targetVertice = _targetVertice;
		targetVertice.setZ(0);
		setReady();
	}

	public void setWarpVertice(Vector3f _warpVertice){
		warpVertice = _warpVertice;
		setReady();
	}

	/**
	 * get the ModelWorldPosition for the calibration
	 * @return
	 */
	public MatOfPoint3f getObjectPoint(){
		return new MatOfPoint3f(new Point3( modelWorldVertice.x(), modelWorldVertice.y(), modelWorldVertice.z()));
	}

	/**
	 * get the ImagePosition for the calibration
	 * @param width
	 * @param height
	 * @return
	 */
	public MatOfPoint2f getImagePoint(float width, float height){
		return new MatOfPoint2f(new Point( targetVertice.x() * width, (1.0 - targetVertice.y()) * height));
	}

	public Vector3f getModelScreenVerticeOrthoNormal(){
		return orthoNormalize(modelScreenVertice);
	}

	public Vector3f getTargetVerticeOrthoNormal(){
		return orthoNormalize(targetVertice);
	}

	public Vector3f getWarpVerticeOrthoNormal(){
		return orthoNormalize(warpVertice);
	}

	public Vector3f getModelWorldVertice(){
		return modelWorldVertice;
	}

	public Vector3f getModelLocalVertice(){
		return modelLocalVertice;
	}

	public Vector3f getModelScreenVertice(){
		return modelScreenVertice;
	}

	public Vector3f getTargetVertice(){
		return targetVertice;
	}

	public Vector3f getWarpVertice(){
		return warpVertice;
	}

	public void doubleSelect(boolean _select){
		if(_select)
			isDoubleSelected = 1;
		else if(isDoubleSelected == 1){
			isDoubleSelected = 2;
		}else if(isDoubleSelected == 2){
			isDoubleSelected = 0;
		}
	}

	private void createTextObject(){
		titleText = new JitterObject("jit.gl.text");
		titleText.call("mode", "2d");
		infoText = new JitterObject("jit.gl.text");
		infoText.call("mode", "2d");
		//drawableText.call("transform_reset", 2);
	}

	public void update(Vertice _vertice){
		modelWorldVertice = _vertice;
	}

	public void clear(){
		titleText.freePeer();
		infoText.freePeer();
	}

}
