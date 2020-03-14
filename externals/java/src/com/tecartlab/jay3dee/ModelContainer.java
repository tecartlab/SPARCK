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

package com.tecartlab.jay3dee;

import com.tecartlab.jay3dee.model.*;
import com.tecartlab.jay3dee.model.drawer.ModelDrawer;
import com.tecartlab.tools.math.la.*;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynException;
import com.tecartlab.utils.dyndist.DynPublication;
import com.tecartlab.utils.dyndist.DynPublisher;
import com.tecartlab.utils.dyndist.DynSubscription;

import com.cycling74.max.*;
import java.util.*;

public class ModelContainer {

	ModelPublisher publisher;
	ModelData model;
	String modelname;

	public static final String LIST_MSG_SELECTED	 	= "selectedVertice";
	public static final String LIST_MSG_MODIFIED_MODEL	= "modifiedModel";
	public static final String LIST_MSG_MODIFIED_TEX	= "modifiedTexture";
	public static final String LIST_MSG_FILEPARSED 		= "fileparsed";

	public FileManager fileManager;

	public static int HANDLER_NONE = 0;
	public static int HANDLER_ROTATING = 1;
	public static int HANDLER_GRABBING = 2;
	public static int HANDLER_SCALING = 3;

	private static int HANDLING_NOAXIS = 0;
	private static int HANDLING_ALLAXIS = 1;
	private static int HANDLING_XAXIS = 2;
	private static int HANDLING_YAXIS = 3;
	private static int HANDLING_ZAXIS = 4;

	private GLCanvas glCommandCanvas;
	private float[] bkgFaceColor = { 1, 1, 1, .3f };
	private float[] bkgLineColor = { 1, 1, 1, 1 };
	private float[] faceMainColor = { 1, 1, 1, 1 };
	private float[] faceEditColor = { 1, 1, 1, .5f };
	private float[] lineColor = { 1, 0, 0, 1 };
	private float[] pointColor = { 0, 1, 0, 1 };
	private float[] selectedPointColor = { 0, 0, 1, 1 };
	private float pointSize = 5.f;

	private float[] axisColorX = { 1, 1, 0, 1 };
	private float[] axisColorY = { 0, 1, 1, 1 };
	private float[] axisColorZ = { 1, 0, 1, 1 };

	private float _scale3DHandlerX = 1.f;
	private float _scale3DHandlerY = 1.f;
	private float _scale3DHandlerZ = 1.f;

	private float _scale2DHandlerX = 1.f;
	private float _scale2DHandlerY = 1.f;
	private float _scale2DHandlerZ = 0.f;

	private Vector3f center3DHandler, center2DHandler;

	private ModelData referenceModel;

	private int handling3DType = HANDLING_NOAXIS;
	private int handler3DMode = HANDLER_GRABBING;

	private int handling2DType = HANDLING_NOAXIS;
	private int handler2DMode = HANDLER_GRABBING;

	private boolean maniplutationFlag = false;

	private float textureShiftX = -0.5f; // there is another mirrored set in the Face-Object!!
	private float textureShiftY = -0.5f;

	private ArrayList<ModelDrawer> drawingListeners;

	public ModelContainer() {
		model = new ModelData(this);
		publisher = new ModelPublisher();
		fileManager = new FileManager(10);
		drawingListeners = new ArrayList<ModelDrawer>();
	}

	public void init(String _modelName) throws DynException{
		publisher.publishModel(_modelName, this);
	}

	/****************************************************************
	 * FILES / UN-DO
	 ****************************************************************/

	public void undo() {
		model = fileManager.unDo(model);
		center3DHandler = model.getSelected3DAverageCenter();
		center2DHandler = model.getSelected2DAverageCenter();
	}

	public void redo() {
		model = fileManager.reDo(model);
		center3DHandler = model.getSelected3DAverageCenter();
		center2DHandler = model.getSelected2DAverageCenter();
	}

	/**
	 * creates a model with the specified points.
	 * three point make a vertice, with each vertice drawing a line to the XY plane
	 * @param vertices
	 */
	public void create(Atom[] vertices, int plane){
		ArrayList<Vertice> verts = new ArrayList<Vertice>(vertices.length);
		for(int i = 0; i < vertices.length; i = i + 3){
			verts.add(new Vertice(vertices[i].toFloat(), vertices[i+1].toFloat(), vertices[i+2].toFloat()));
		}
		model.create(verts, plane);
	}

	public void load(String filename) {
		fileManager.load(model, filename);
	}

	public void saveAs(String filename) {
		fileManager.saveAs(model, filename);
	}

	public void save() {
		fileManager.save(model);
	}

	/****************************************************************
	 * 3D Transformations
	 ****************************************************************/

	public void modelRotatingStart(int useX, int useY, int useZ, float x1,
			float y1, float z1, float x2, float y2, float z2) {
		handling3DType = get3DHandlingType(useX, useY, useZ, x2, y2, z2);
		referenceModel = model.clone();
	}

	public void modelRotating(int useX, int useY, int useZ, float x1, float y1,
			float z1, float x2, float y2, float z2) {
		Vector3f origVertex, transVertex;
		if (handling3DType != HANDLING_NOAXIS) {
			for (int i = 0; i < referenceModel.getModelVerticesCount(); i++) {
				if(referenceModel.getModelVertice(i).isSelected()){
					origVertex = model.getModelVertice(i);
					transVertex = referenceModel.getModelVertice(i);
					Vector3f dist = transVertex.subMake(center3DHandler);
					if (useZ == 0
							&& (handling3DType == HANDLING_XAXIS || handling3DType == HANDLING_YAXIS)) {
						dist.rotZ((x1 - x2) + (y1 - y2));
					}
					if (useY == 0
							&& (handling3DType == HANDLING_XAXIS || handling3DType == HANDLING_ZAXIS)) {
						dist.rotY((x1 - x2) + (z1 - z2));
					}
					if (useX == 0
							&& (handling3DType == HANDLING_ZAXIS || handling3DType == HANDLING_YAXIS)) {
						dist.rotX((z1 - z2) + (y1 - y2));
					}
					dist.setAdd(dist, center3DHandler);
					origVertex.set(dist);
				}
			}
			maniplutationFlag = true;
		}
		center3DHandler = model.getSelected3DAverageCenter();
		updateData();
	}

	public void modelRotatingStop() {
		if (maniplutationFlag)
			fileManager.newDo(model);
		maniplutationFlag = false;
	}

	public void modelScalingStart(int useX, int useY, int useZ, float x1,
			float y1, float z1, float x2, float y2, float z2) {
		handling3DType = get3DHandlingType(useX, useY, useZ, x2, y2, z2);
		referenceModel = model.clone();
	}

	public void modelScaling(int useX, int useY, int useZ, float x1, float y1,
			float z1, float x2, float y2, float z2) {
		Vector3f origVertex, transVertex;
		if (handling3DType != HANDLING_NOAXIS) {
			for (int i = 0; i < referenceModel.getModelVerticesCount(); i++) {
				if(referenceModel.getModelVertice(i).isSelected()){
					origVertex = model.getModelVertice(i);
					transVertex = referenceModel.getModelVertice(i);
					Vector3f dist = transVertex.subMake(center3DHandler);
					if (handling3DType == HANDLING_XAXIS) {
						dist.setX(dist.x() * (1 + (x1 - x2)));
					}else if (handling3DType == HANDLING_YAXIS) {
						dist.setY(dist.y() * (1 + (y1 - y2)));
					}else if (handling3DType == HANDLING_ZAXIS) {
						dist.setZ(dist.z() * (1 + (z1 - z2)));
					}else if (handling3DType == HANDLING_ALLAXIS) {
						dist.scale(1 + (x1 - x2));
					}
					dist.setAdd(dist, center3DHandler);
					origVertex.set(dist);
				}
			}
			maniplutationFlag = true;
		}
		center3DHandler = model.getSelected3DAverageCenter();
		updateData();
	}

	public void modelScalingStop() {
		if (maniplutationFlag)
			fileManager.newDo(model);
		maniplutationFlag = false;
	}

	public void modelGrabbingStart(int useX, int useY, int useZ, float x1,
			float y1, float z1, float x2, float y2, float z2) {
		handling3DType = get3DHandlingType(useX, useY, useZ, x2, y2, z2);
		referenceModel = model.clone();
	}

	public void modelGrabbing(int useX, int useY, int useZ, float x1, float y1,
			float z1, float x2, float y2, float z2) {
		Vector3f origVertex, transVertex;
		if (handling3DType != HANDLING_NOAXIS) {
			for (int i = 0; i < referenceModel.getModelVerticesCount(); i++) {
				if(referenceModel.getModelVertice(i).isSelected()){
					origVertex = model.getModelVertice(i);
					transVertex = referenceModel.getModelVertice(i);
					origVertex.set(transVertex);
					if (handling3DType == HANDLING_XAXIS) {
						origVertex.setAdd(origVertex, new Vector3f(x1 - x2, 0.f, 0.f));
					} else if (handling3DType == HANDLING_YAXIS) {
						origVertex.setAdd(origVertex, new Vector3f(0.f, y1 - y2, 0.f));
					} else if (handling3DType == HANDLING_ZAXIS) {
						origVertex.setAdd(origVertex, new Vector3f(0.f, 0.f, z1 - z2));
					} else if (handling3DType == HANDLING_ALLAXIS) {
						origVertex.setAdd(origVertex, new Vector3f(x1 - x2, y1 - y2, z1 - z2));
					}
				}
			}
			maniplutationFlag = true;
		}
		center3DHandler = model.getSelected3DAverageCenter();
		updateData();
	}

	public void modelGrabbingStop() {
		if (maniplutationFlag)
			fileManager.newDo(model);
		maniplutationFlag = false;
	}

	public void modelKeyGrabbing(float xDiff, float yDiff, float zDiff) {
		referenceModel = model.clone();
		Vector3f origVertex, transVertex;
		for (int i = 0; i < referenceModel.getModelVerticesCount(); i++) {
			if(referenceModel.getModelVertice(i).isSelected()){
				origVertex = model.getModelVertice(i);
				transVertex = referenceModel.getModelVertice(i);
				origVertex.set(transVertex);
				origVertex.setAdd(origVertex, new Vector3f(xDiff, yDiff, zDiff));
			}
		}
		center3DHandler = model.getSelected3DAverageCenter();
		updateData();
		fileManager.newDo(model);
		maniplutationFlag = false;
	}

	public void modelTranslationPosition(float xDiff, float yDiff, float zDiff) {
		referenceModel = model.clone();
		Vector3f origVertex, transVertex;
		for (int i = 0; i < referenceModel.getModelVerticesCount(); i++) {
			origVertex = model.getModelVertice(i);
			transVertex = referenceModel.getModelVertice(i);
			origVertex.set(transVertex);
			origVertex.setAdd(origVertex, new Vector3f(xDiff, yDiff, zDiff));
		}
		center3DHandler = model.getSelected3DAverageCenter();
		updateData();
		fileManager.newDo(model);
		maniplutationFlag = false;
	}

	public void modelTranslationRotation(float xDiff, float yDiff, float zDiff) {
		referenceModel = model.clone();
		Vector3f origVertex, transVertex;
		for (int i = 0; i < referenceModel.getModelVerticesCount(); i++) {
			origVertex = model.getModelVertice(i);
			transVertex = referenceModel.getModelVertice(i);
			origVertex.set(transVertex);
			origVertex.rotX(xDiff);
			origVertex.rotY(yDiff);
			origVertex.rotZ(zDiff);
		}
		center3DHandler = model.getSelected3DAverageCenter();
		updateData();
		fileManager.newDo(model);
		maniplutationFlag = false;
	}

	public void modelTranslationScale(float xDiff, float yDiff, float zDiff) {
		referenceModel = model.clone();
		Vector3f origVertex, transVertex;
		for (int i = 0; i < referenceModel.getModelVerticesCount(); i++) {
			origVertex = model.getModelVertice(i);
			transVertex = referenceModel.getModelVertice(i);
			origVertex.set(transVertex);
			origVertex.scale(new Vector3f(xDiff, yDiff, zDiff));
		}
		center3DHandler = model.getSelected3DAverageCenter();
		updateData();
		fileManager.newDo(model);
		maniplutationFlag = false;
	}

	private int get3DHandlingType(int useX, int useY, int useZ, float x,
			float y, float z) {
		float xHandlingCenter = 0.f;
		float yHandlingCenter = 0.f;
		float zHandlingCenter = 0.f;
		if (useX == 1) {
			xHandlingCenter = center3DHandler.x() + _scale3DHandlerX * .5f;
			yHandlingCenter = center3DHandler.y();
			zHandlingCenter = 0;
			if (liesIn3DHandlingSpace(xHandlingCenter, yHandlingCenter,
					zHandlingCenter, x, y, z))
				return HANDLING_XAXIS;
			zHandlingCenter = center3DHandler.z();
			yHandlingCenter = 0.f;
			if (liesIn3DHandlingSpace(xHandlingCenter, yHandlingCenter,
					zHandlingCenter, x, y, z))
				return HANDLING_XAXIS;
		}
		if (useY == 1) {
			xHandlingCenter = 0.f;
			yHandlingCenter = center3DHandler.y() + _scale3DHandlerY * .5f;
			zHandlingCenter = center3DHandler.z();
			if (liesIn3DHandlingSpace(xHandlingCenter, yHandlingCenter,
					zHandlingCenter, x, y, z))
				return HANDLING_YAXIS;
			xHandlingCenter = center3DHandler.x();
			zHandlingCenter = 0.f;
			if (liesIn3DHandlingSpace(xHandlingCenter, yHandlingCenter,
					zHandlingCenter, x, y, z))
				return HANDLING_YAXIS;
		}
		if (useZ == 1) {
			xHandlingCenter = 0.f;
			yHandlingCenter = center3DHandler.y();
			zHandlingCenter = center3DHandler.z() + _scale3DHandlerZ * .5f;
			if (liesIn3DHandlingSpace(xHandlingCenter, yHandlingCenter,
					zHandlingCenter, x, y, z))
				return HANDLING_ZAXIS;
			xHandlingCenter = center3DHandler.x();
			yHandlingCenter = 0.f;
			if (liesIn3DHandlingSpace(xHandlingCenter, yHandlingCenter,
					zHandlingCenter, x, y, z))
				return HANDLING_ZAXIS;
		}
		xHandlingCenter = 0.f;
		yHandlingCenter = 0.f;
		zHandlingCenter = 0.f;
		if (useX == 1 && useY == 1) {
			xHandlingCenter = center3DHandler.x();
			yHandlingCenter = center3DHandler.y();
		} else if (useX == 1 && useZ == 1) {
			xHandlingCenter = center3DHandler.x();
			zHandlingCenter = center3DHandler.z();
		} else if (useZ == 1 && useY == 1) {
			zHandlingCenter = center3DHandler.z();
			yHandlingCenter = center3DHandler.y();
		}
		if (liesIn3DHandlingSpace(xHandlingCenter, yHandlingCenter,
				zHandlingCenter, x, y, z))
			return HANDLING_ALLAXIS;
		return HANDLING_NOAXIS;
	}

	private boolean liesIn3DHandlingSpace(float centerX, float centerY,
			float centerZ, float x, float y, float z) {
		if (centerZ + _scale3DHandlerZ * .1f > z
				&& centerZ - _scale3DHandlerZ * .1f < z
				&& centerY + _scale3DHandlerY * .1f > y
				&& centerY - _scale3DHandlerY * .1f < y
				&& centerX + _scale3DHandlerX * .1f > x
				&& centerX - _scale3DHandlerX * .1f < x)
			return true;
		return false;
	}

	/****************************************************************
	 * 2D Texture Transformations
	 ****************************************************************/

	public void textureRotatingStart(float x1, float y1, float x2, float y2) {
		handling2DType = get2DHandlingType(x2, y2);
		referenceModel = model.clone();
	}

	public void textureRotating(float x1, float y1, float x2, float y2) {
		Vector3f origVertex, transVertex;
		for (int i = 0; i < referenceModel.getTextureVerticeCount(); i++) {
			if(referenceModel.getTextureVertice(i).isSelected()){
				origVertex = model.getTextureVertice(i);
				transVertex = referenceModel.getTextureVertice(i);
				Vector3f dist = transVertex.subMake(center2DHandler);
				dist.rotZ((x1 - x2) + (y1 - y2));
				dist.setAdd(dist, center2DHandler);
				origVertex.set(dist);
			}
		}
		maniplutationFlag = true;
		update2DHandler();
		updateData();
	}

	public void textureRotatingStop() {
		if (maniplutationFlag)
			fileManager.newDo(model);
		maniplutationFlag = false;
	}

	public void textureScalingStart(float x1, float y1, float x2, float y2) {
		handling2DType = get2DHandlingType(x2, y2);
		referenceModel = model.clone();
	}

	public void textureScaling(float x1, float y1, float x2, float y2) {
		Vector3f origVertex, transVertex;
		for (int i = 0; i < referenceModel.getTextureVerticeCount(); i++) {
			if(referenceModel.getTextureVertice(i).isSelected()){
				origVertex = model.getTextureVertice(i);
				transVertex = referenceModel.getTextureVertice(i);
				if (handling2DType == HANDLING_XAXIS) {
					Vector3f dist = transVertex.subMake(center2DHandler);
					dist.setX(dist.x() * (1 + (x1 - x2)));
					dist.setAdd(dist, center2DHandler);
					origVertex.set(dist);
					maniplutationFlag = true;
				} else if (handling2DType == HANDLING_YAXIS) {
					Vector3f dist = transVertex.subMake(center2DHandler);
					dist.setY(dist.y() * (1 + (y1 - y2)));
					dist.setAdd(dist, center2DHandler);
					origVertex.set(dist);
					maniplutationFlag = true;
				} else if (handling2DType == HANDLING_ALLAXIS) {
					Vector3f dist = transVertex.subMake(center2DHandler);
					dist.scale(1 + (x1 - x2));
					dist.setAdd(dist, center2DHandler);
					origVertex.set(dist);
					maniplutationFlag = true;
				}
			}
		}
		update2DHandler();
		updateData();
	}

	public void textureScalingStop() {
		if (maniplutationFlag)
			fileManager.newDo(model);
		maniplutationFlag = false;
	}

	public void textureGrabbingStart(float x1, float y1, float x2, float y2) {
		handling2DType = get2DHandlingType(x2, y2);
		referenceModel = model.clone();
	}

	public void textureGrabbing(float x1, float y1, float x2, float y2) {
		Vector3f origVertex, transVertex;
		for (int i = 0; i < referenceModel.getTextureVerticeCount(); i++) {
			if(referenceModel.getTextureVertice(i).isSelected()){
				origVertex = model.getTextureVertice(i);
				transVertex = referenceModel.getTextureVertice(i);
				if (handling2DType == HANDLING_XAXIS) {
					origVertex.set(transVertex);
					origVertex.setAdd(origVertex, new Vector3f(x1 - x2, 0.f, 0.f));
					maniplutationFlag = true;
				} else if (handling2DType == HANDLING_YAXIS) {
					origVertex.set(transVertex);
					origVertex.setAdd(origVertex, new Vector3f(0.f, y1 - y2, 0.f));
					maniplutationFlag = true;
				} else if (handling2DType == HANDLING_ALLAXIS) {
					origVertex.set(transVertex);
					origVertex.setAdd(origVertex, new Vector3f(x1 - x2, y1 - y2, 0.f));
					maniplutationFlag = true;
				}
			}
		}
		update2DHandler();
		updateData();
	}

	public void textureGrabbingStop() {
		if (maniplutationFlag)
			fileManager.newDo(model);
		maniplutationFlag = false;
	}

	private int get2DHandlingType(float x, float y) {
		float xHandlingCenter = 0.f;
		float yHandlingCenter = 0.f;
		xHandlingCenter = center2DHandler.x() + _scale2DHandlerX * .5f;
		yHandlingCenter = center2DHandler.y();
		if (liesIn2DHandlingSpace(xHandlingCenter, yHandlingCenter, x-textureShiftX, y-textureShiftY))
			return HANDLING_XAXIS;
		yHandlingCenter = center2DHandler.y() + _scale2DHandlerY * .5f;
		xHandlingCenter = center2DHandler.x();
		if (liesIn2DHandlingSpace(xHandlingCenter, yHandlingCenter, x-textureShiftX, y-textureShiftY))
			return HANDLING_YAXIS;
		yHandlingCenter = center2DHandler.y();
		xHandlingCenter = center2DHandler.x();
		if (liesIn2DHandlingSpace(xHandlingCenter, yHandlingCenter, x-textureShiftX, y-textureShiftY))
			return HANDLING_ALLAXIS;
		return HANDLING_NOAXIS;
	}

	private boolean liesIn2DHandlingSpace(float centerX, float centerY,
			float x, float y) {
		if (centerY + _scale2DHandlerY * .1f > y
				&& centerY - _scale2DHandlerY * .1f < y
				&& centerX + _scale2DHandlerX * .1f > x
				&& centerX - _scale2DHandlerX * .1f < x)
			return true;
		return false;
	}

	/****************************************************************
	 * Selections
	 ****************************************************************/

	public void selectAll() {
		if (model.getSelectedModelVerticeCount() == model
				.getModelVerticesCount()) {
			model.unselectAllVertices();
			center3DHandler = model.getSelected3DAverageCenter();
			update2DHandler();
			fileManager.newDo(model);
			callDrawers(this.LIST_MSG_SELECTED);
		} else {
			model.selectAllVertices();
			center3DHandler = model.getSelected3DAverageCenter();
			update2DHandler();
			fileManager.newDo(model);
			callDrawers(this.LIST_MSG_SELECTED);
		}
	}

	public void selectModel(int useX, int useY, int useZ, float x1, float y1,
			float z1, float x2, float y2, float z2) {
		if (model.selectModelVertices(useX, useY, useZ, x1, y1, z1, x2, y2, z2) > 0)
			fileManager.newDo(model);
		center3DHandler = model.getSelected3DAverageCenter();
		update2DHandler();
		callDrawers(this.LIST_MSG_SELECTED);
	}

	public void unSelectModel(int useX, int useY, int useZ, float x1, float y1,
			float z1, float x2, float y2, float z2) {
		if (model.unSelectModelVertices(useX, useY, useZ, x1, y1, z1, x2, y2,
				z2) > 0)
			fileManager.newDo(model);
		center3DHandler = model.getSelected3DAverageCenter();
		center2DHandler = model.getSelected2DAverageCenter();
		callDrawers(this.LIST_MSG_SELECTED);
	}

	public void selectTexture(float x1, float y1, float x2, float y2) {
		if (model.selectTextureVertices(x1-textureShiftX, y1-textureShiftY, x2-textureShiftX, y2-textureShiftY) > 0)
			fileManager.newDo(model);
		center3DHandler = model.getSelected3DAverageCenter();
		update2DHandler();
		callDrawers(this.LIST_MSG_SELECTED);
	}

	public void unSelectTexture(float x1, float y1, float x2, float y2) {
		if (model.unSelectTextureVertices(x1-textureShiftX, y1-textureShiftY, x2-textureShiftX, y2-textureShiftY) > 0)
			fileManager.newDo(model);
		center3DHandler = model.getSelected3DAverageCenter();
		callDrawers(this.LIST_MSG_SELECTED);
	}

	private void update2DHandler(){
		center2DHandler = model.getSelected2DAverageCenter();
	}


	/****************************************************************
	 * New Picking - Selections
	 ****************************************************************/

	/**
	 * pick closest vertice to viewray (ergo: pickray)
	 * @param pickray
	 * @return true if new vertice was picked
	 */
	public boolean pickVertice(Linef pickray){
		if(model.pickModelVertice(pickray)){
			callDrawers(LIST_MSG_SELECTED);
			return true;
		}
		return false;
	}

	/**
	 * selects the picked vertice and returns a clone of the vertice
	 * @return
	 */
	public Vertice selectPickedVertice(){
		Vertice temp = model.selectPickedModelVertice();
		if(temp != null){
			callDrawers(LIST_MSG_SELECTED);
			return temp.clone();
		}
		return null;
	}

	public void unselectPickedVertice(){
		model.unselectAllVertices();
		callDrawers(this.LIST_MSG_SELECTED);
	}

	/**
	 * Returns clones of all selected vertices.
	 * @param trans
	 * @return
	 */
	public Vertice[] getSelectedVertices(){
		Vertice[] vertices = model.getSelectedVertices();
		for(int i = 0; i < vertices.length; i++)
			vertices[i] = (Vertice)vertices[i].clone();
		return vertices;
	}

	/**
	 * Returns a clone of the modelvertice at this index.
	 * @param index
	 * @return
	 */
	public Vertice getModelVertice(int index){
		return model.getModelVertice(index).clone();
	}

	/**
	 * Returns a clone of the nearest modelvertice to the specified point.
	 * @param _pos in local coordinate system
	 * @return clone of model vertice
	 */
	public Vertice getModelVertice(Vector3f _pos){
		return model.getModelVertice(_pos).clone();
	}

	/****************************************************************
	 * 3D Drawing
	 ****************************************************************/

	public void draw3DHandler(GLCanvas canvas) {
		glCommandCanvas = canvas;
		//command(MessageFactory.sketch_reset());
		if (handler3DMode == HANDLER_GRABBING) {
			//draw3DGrabbingHandler();
		}
		if (handler3DMode == HANDLER_ROTATING) {
			//draw3DRotatingHandler();
		}
		if (handler3DMode == HANDLER_SCALING) {
			//draw3DScalingHandler();
		}
	}

	/*
	private void draw3DGrabbingHandler() {
		if (model.getSelectedModelVerticeCount() > 0) {
			command(MessageFactory.sketch_glcolor(axisColorX));
			command(MessageFactory.sketch_moveto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y()
					- _scale3DHandlerY * .05f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y()
					+ _scale3DHandlerY * .05f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));

			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z() - _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));

			command(MessageFactory.sketch_glcolor(axisColorY));
			command(MessageFactory.sketch_moveto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z() - _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .6f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z() + _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));

			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					- _scale3DHandlerX * .05f, center3DHandler.y()
					+ _scale3DHandlerY * .5f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .6f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .05f, center3DHandler.y()
					+ _scale3DHandlerY * .5f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));

			command(MessageFactory.sketch_glcolor(axisColorZ));
			command(MessageFactory.sketch_moveto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					- _scale3DHandlerX * .05f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .05f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));

			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .05f,
					center3DHandler.z() + _scale3DHandlerZ * .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() - _scale3DHandlerY * .05f,
					center3DHandler.z() + _scale3DHandlerZ * .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));
		}
	}

	private void draw3DScalingHandler() {
		if (model.getSelectedModelVerticeCount() > 0) {
			command(MessageFactory.sketch_glcolor(axisColorX));
			command(MessageFactory.sketch_moveto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y()
					- _scale3DHandlerY * .05f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y()
					- _scale3DHandlerY * .05f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y()
					+ _scale3DHandlerY * .05f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y()
					+ _scale3DHandlerY * .05f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));

			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z() - _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y(),
					center3DHandler.z() - _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));

			command(MessageFactory.sketch_glcolor(axisColorY));
			command(MessageFactory.sketch_moveto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z() - _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .6f,
					center3DHandler.z() - _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .6f,
					center3DHandler.z() + _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z() + _scale3DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));

			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					- _scale3DHandlerX * .05f, center3DHandler.y()
					+ _scale3DHandlerY * .5f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					- _scale3DHandlerX * .05f, center3DHandler.y()
					+ _scale3DHandlerY * .6f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .05f, center3DHandler.y()
					+ _scale3DHandlerY * .6f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .05f, center3DHandler.y()
					+ _scale3DHandlerY * .5f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));

			command(MessageFactory.sketch_glcolor(axisColorZ));
			command(MessageFactory.sketch_moveto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					- _scale3DHandlerX * .05f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					- _scale3DHandlerX * .05f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .05f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .05f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));

			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .05f,
					center3DHandler.z() + _scale3DHandlerZ * .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .05f,
					center3DHandler.z() + _scale3DHandlerZ * .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() - _scale3DHandlerY * .05f,
					center3DHandler.z() + _scale3DHandlerZ * .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() - _scale3DHandlerY * .05f,
					center3DHandler.z() + _scale3DHandlerZ * .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));
		}
	}

	private void draw3DRotatingHandler() {
		if (model.getSelectedModelVerticeCount() > 0) {
			command(MessageFactory.sketch_glcolor(axisColorX));
			command(MessageFactory.sketch_moveto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y()
					+ _scale3DHandlerY * .1f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));

			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y(),
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .6f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .1f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .5f, center3DHandler.y(),
					center3DHandler.z()));

			command(MessageFactory.sketch_glcolor(axisColorY));
			command(MessageFactory.sketch_moveto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .6f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .6f,
					center3DHandler.z() + _scale3DHandlerZ * .1f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));

			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .6f,
					center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .1f, center3DHandler.y()
					+ _scale3DHandlerY * .6f, center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .5f,
					center3DHandler.z()));

			command(MessageFactory.sketch_glcolor(axisColorZ));
			command(MessageFactory.sketch_moveto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z()));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x()
					+ _scale3DHandlerX * .1f, center3DHandler.y(),
					center3DHandler.z() + _scale3DHandlerZ * .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));

			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y() + _scale3DHandlerY * .1f,
					center3DHandler.z() + _scale3DHandlerZ * .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .6f));
			command(MessageFactory.sketch_lineto(center3DHandler.x(),
					center3DHandler.y(), center3DHandler.z() + _scale3DHandlerZ
							* .5f));
		}
	}

	*/

	/****************************************************************
	 * 2D Drawing
	 ****************************************************************/

	public void draw2DHandler(GLCanvas canvas) {
		update2DHandler();
		glCommandCanvas = canvas;
		//command(MessageFactory.sketch_reset());
		if (handler2DMode == HANDLER_GRABBING) {
			//draw2DGrabbingHandler();
		}
		if (handler2DMode == HANDLER_ROTATING) {
			//draw2DRotatingHandler();
		}
		if (handler2DMode == HANDLER_SCALING) {
			//draw2DScalingHandler();
		}
	}

	/*
	private void draw2DGrabbingHandler() {
		Vector3f shifted2DHandler = new Vector3f(textureShiftX, textureShiftY, 0.f);
		shifted2DHandler.setAdd(shifted2DHandler, center2DHandler);

		if (model.getSelectedModelVerticeCount() > 0) {
			command(MessageFactory.sketch_glcolor(axisColorX));
			command(MessageFactory.sketch_moveto(shifted2DHandler.x(),
					shifted2DHandler.y(), shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y()
					- _scale2DHandlerY * .05f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y()
					+ _scale2DHandlerY * .05f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));

			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z() - _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z() + _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));

			command(MessageFactory.sketch_glcolor(axisColorY));
			command(MessageFactory.sketch_moveto(shifted2DHandler.x(),
					shifted2DHandler.y(), shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z() - _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .6f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z() + _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));

			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					- _scale2DHandlerX * .05f, shifted2DHandler.y()
					+ _scale2DHandlerY * .5f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .6f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .05f, shifted2DHandler.y()
					+ _scale2DHandlerY * .5f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));
		}
	}

	private void draw2DScalingHandler() {
		Vector3f shifted2DHandler = new Vector3f(textureShiftX, textureShiftY, 0.f);
		shifted2DHandler.setAdd(shifted2DHandler, center2DHandler);

		if (model.getSelectedModelVerticeCount() > 0) {
			command(MessageFactory.sketch_glcolor(axisColorX));
			command(MessageFactory.sketch_moveto(shifted2DHandler.x(),
					shifted2DHandler.y(), shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y()
					- _scale2DHandlerY * .05f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y()
					- _scale2DHandlerY * .05f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y()
					+ _scale2DHandlerY * .05f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y()
					+ _scale2DHandlerY * .05f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));

			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z() - _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y(),
					shifted2DHandler.z() - _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y(),
					shifted2DHandler.z() + _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z() + _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));

			command(MessageFactory.sketch_glcolor(axisColorY));
			command(MessageFactory.sketch_moveto(shifted2DHandler.x(),
					shifted2DHandler.y(), shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z() - _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .6f,
					shifted2DHandler.z() - _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .6f,
					shifted2DHandler.z() + _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z() + _scale2DHandlerZ * .05f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));

			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					- _scale2DHandlerX * .05f, shifted2DHandler.y()
					+ _scale2DHandlerY * .5f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					- _scale2DHandlerX * .05f, shifted2DHandler.y()
					+ _scale2DHandlerY * .6f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .05f, shifted2DHandler.y()
					+ _scale2DHandlerY * .6f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .05f, shifted2DHandler.y()
					+ _scale2DHandlerY * .5f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));
		}
	}

	private void draw2DRotatingHandler() {
		Vector3f shifted2DHandler = new Vector3f(textureShiftX, textureShiftY, 0.f);
		shifted2DHandler.setAdd(shifted2DHandler, center2DHandler);

		if (model.getSelectedModelVerticeCount() > 0) {
			command(MessageFactory.sketch_glcolor(axisColorX));
			command(MessageFactory.sketch_moveto(shifted2DHandler.x(),
					shifted2DHandler.y(), shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y()
					+ _scale2DHandlerY * .1f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));

			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y(),
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .6f, shifted2DHandler.y(),
					shifted2DHandler.z() + _scale2DHandlerZ * .1f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .5f, shifted2DHandler.y(),
					shifted2DHandler.z()));

			command(MessageFactory.sketch_glcolor(axisColorY));
			command(MessageFactory.sketch_moveto(shifted2DHandler.x(),
					shifted2DHandler.y(), shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .6f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .6f,
					shifted2DHandler.z() + _scale2DHandlerZ * .1f));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));

			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .6f,
					shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x()
					+ _scale2DHandlerX * .1f, shifted2DHandler.y()
					+ _scale2DHandlerY * .6f, shifted2DHandler.z()));
			command(MessageFactory.sketch_lineto(shifted2DHandler.x(),
					shifted2DHandler.y() + _scale2DHandlerY * .5f,
					shifted2DHandler.z()));
		}
	}

	*/

	/****************************************************************
	 * Setter Methods
	 ****************************************************************/

	public void setFaceSubdivision(int sub) {
		model.setFaceSubDivision(sub);
		updateData();
	}

	public void set3DHandlerGrabbing() {
		handler3DMode = HANDLER_GRABBING;
	}

	public void set3DHandlerRotating() {
		handler3DMode = HANDLER_ROTATING;
	}

	public void set3DHandlerScaling() {
		handler3DMode = HANDLER_SCALING;
	}

	public void set2DHandlerGrabbing() {
		handler2DMode = HANDLER_GRABBING;
	}

	public void set2DHandlerRotating() {
		handler2DMode = HANDLER_ROTATING;
	}

	public void set2DHandlerScaling() {
		handler2DMode = HANDLER_SCALING;
	}

	public void setFaceEditColor(float[] color) {
		faceEditColor = color;
	}

	public void setFaceMainColor(float[] color) {
		faceMainColor = color;
	}

	public void setLineColor(float[] color) {
		lineColor = color;
	}

	public void setPointColor(float[] color) {
		pointColor = color;
	}

	public void setSelectedPointColor(float[] color) {
		selectedPointColor = color;
	}

	public void scale3DHandler(float x, float y, float z) {
		_scale3DHandlerX = 1.f / x;
		_scale3DHandlerY = 1.f / y;
		_scale3DHandlerZ = 1.f / z;
	}

	public void scale2DHandler(float x, float y, float z) {
		_scale2DHandlerX = 1.f / x;
		_scale2DHandlerY = 1.f / y;
	}

	/**
	 * This Method refreshes the draw commands of the model. need to be called after a manipulation of the
	 * vertices (model or texture)
	 *
	 */
	private void updateData(){
		model.refreshGeometry();
	}


	/****************************************************************
	 * Drawer Methods
	 ****************************************************************/

	/**
	 * These method is called by the ModelData Threads to tell their tasks have been
	 * accomplished
	 */
	public synchronized void dataEvent(String message) {
		if(message.equals(LIST_MSG_FILEPARSED))
			callDrawers(message);
	}

	public synchronized void callDrawers(String _message){
		Iterator i = drawingListeners.iterator();
		ModelDrawer listeningDrawer;
		while(i.hasNext()){
			listeningDrawer = (ModelDrawer)i.next();
			if(_message.equals(ModelContainer.LIST_MSG_SELECTED))
				listeningDrawer.selectionChange();
			if(_message.equals(ModelContainer.LIST_MSG_MODIFIED_MODEL))
				listeningDrawer.modelChange();
			if(_message.equals(ModelContainer.LIST_MSG_MODIFIED_TEX))
				listeningDrawer.textureChange();
			if(_message.equals(ModelContainer.LIST_MSG_FILEPARSED)){
				listeningDrawer.modelChange();
				listeningDrawer.textureChange();
			}

			listeningDrawer.draw();
		}
	}

	public void notifyDeleted()
	{
		if(publisher != null)
			publisher.notifyDeleted();
		drawingListeners.clear();
	}

	public boolean isOperational(){
		if(model.getFaceCount() > 0){
			return true;
		} else if(model.getModelVerticesCount() > 0){
			return true;
		} else {
			Debug.warning("Jay3DeeModel["+modelname+"]", "No valid model loaded!");
		}
		return false;
	}

	protected class ModelPublisher implements DynPublisher{

		private DynPublication publication;

		public ModelPublisher(){
		}

		public void publishModel(String _modelname, ModelContainer modelobject) throws DynException{
			if(!_modelname.equals(modelobject)){
				if(publication != null){
					Debug.verbose("Jay3DeeModel["+modelname+"]", "Recalling this model");
					publication.recall();
				}
				Debug.verbose("Jay3DeeModel["+_modelname+"]", "Published This model");
				publication = Env.getEnv().modelDistributor.create(this, _modelname, modelobject);
				modelname = _modelname;
				publication.publish();
			}
		}

		public void notifyDeleted(){
			if(publication != null){
				Debug.verbose("Jay3DeeModel["+modelname+"]", "Recalling this model");
				publication.recall();
			}
		}

		public void subscriptionConnected(String distributor,
				DynSubscription subscription) {
			Debug.verbose("Jay3DeeModel["+modelname+"]", "Model got Subscribed");
			ModelDrawer _listener = (ModelDrawer)subscription.getCallbackObject();
			if(!drawingListeners.contains(_listener)){
				_listener.initialize(model);
				drawingListeners.add(_listener);
			}
		}

		public void subscriptionDisconnected(String distributor,
				DynSubscription subscription) {
			Debug.verbose("Jay3DeeModel["+modelname+"]", "Model got Unsubscribed");
			ModelDrawer _listener = (ModelDrawer)subscription.getCallbackObject();
			if(!drawingListeners.contains(_listener)){
				_listener.reset();
				drawingListeners.remove(_listener);
			}
		}

		public boolean subscriptionCallback(String distributor,
				DynSubscription subscription) {
			return false;
		}
	}

}
