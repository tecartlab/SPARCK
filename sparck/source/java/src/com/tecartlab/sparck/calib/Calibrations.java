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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.cycling74.jitter.JitterMatrix;
import com.cycling74.jitter.JitterObject;
import com.cycling74.max.Atom;

import com.tecartlab.jay3dee.ObjectContainer;
import com.tecartlab.jay3dee.model.Vertice;
import com.tecartlab.mxj.utils.JitterObjectArray;
import com.tecartlab.tools.math.la.Matrix4x4f;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.threedee.Camera;
import com.tecartlab.tools.threedee.Viewport;
import com.tecartlab.utils.Debug;

/**
 * <Calibration>
 * 	<Vertice index="1" screenX="12" screenY="4"/>
 * 	<Vertice index="2" screenX="15" screenY="435"/>
 * 	<Vertice index="3" screenX="1" screenY="5"/>
 * 	<Vertice index="67" screenX="152" screenY="95"/>
 * </Calibration>
 *
 * @author maybites
 *
 */
public class Calibrations {

	public static final String VERSION = "1";

	public static final String 	ELEMENT_CALIBRATION = "calibration";
	private static final String 	ATTR_FRAME 			= "frame";
	private static final String		ATTR_SUBDIV 		= "subdiv";
	private static final String		ATTR_VERSION 		= "version";

	public static final int MODIFY_MODE = 0;
	public static final int MODIFY_MODE_MODEL = 1;
	public static final int MODIFY_MODE_TARGET = 2;
	public static final int MODIFY_MODE_WARP = 3;

	public static final int EDIT_MODE_NONE = 0;
	public static final int EDIT_MODE_GRAB = 1;
	public static final int EDIT_MODE_SELECTED = 2;
	public static final int EDIT_MODE_VALUE_LISTENING = 3;

	private float[] modifyModeModelColor = 	new float[]{1.f, 0.0f, 0.f, 1.f};
	private float[] modifyModeTargetColor = new float[]{0.f, 1.0f, 0.f, 1.f};
	private float[] modifyModeWarpColor = 	new float[]{0.f, 0.0f, 1.f, 1.f};

	private float[] calibSelectedColor = 	new float[]{0.1f, 0.25f, 0.85f, 1.f};
	private float[] calibPickedColor = 		new float[]{0.2f, 0.8f, 0.2f, 1.f};
	private float[] calibColor = 			new float[]{0.8f, 0.15f, 0.15f, 1.f};

	private float[] originSelectedColor = 	new float[]{0.5f, 0.f, 1.f, 1.f};
	private float[] originColor = 			new float[]{0.4f, 0.f, 0.7f, 1.f};
	private float[] originPickedColor = 	new float[]{0.25f, 0.f, 0.5f, 1.f};

	private float[] crossHairLinesColor = 	new float[]{0.1f, 0.1f, 0.1f, 1.f};
	private float[] linkColor = 			new float[]{0.8f, 0.0f, 0.0f, 1.f};

	private float[] delaunaySetEdge = 		new float[]{0.0f, 0.0f, 1.0f, 1.f};
	private float[] delaunayEdge = 			new float[]{0.0f, 0.0f, 0.5f, 1.f};

	private Document document;

	private float pickRadius = 0.02f;

	private ArrayList<CalibVertice> vertices;
	private ArrayList<CalibLine> lines;

	private DelaunayMesh delaunayMesh;

	public Vector<String> context;

	private JitterMatrix viewportWarpGeometry;
	private JitterMatrix viewportDefaultGeometry;

	private JitterObject drawable;
	private EditorHelper helpText;

	private int modifyMode = MODIFY_MODE;
	private int editMode = EDIT_MODE_NONE;
	private String editModeValueListenKey = "";

	private int pickedVerticeID;
	private int selectedVerticeID;
	private int disregardID;

	private boolean isLinked = false;
	private int warpIndex = -1;

	private boolean drawHelp = true;

	private boolean drawLabel = true;
	private boolean drawCalibResult = false;

	private boolean modifyFlag = false;
	private boolean crossHairMode = true;

	public Calibrations(){
		vertices = new ArrayList<CalibVertice>();
		lines = new ArrayList<CalibLine>();
		drawable = new JitterObject("jit.gl.sketch");
		viewportWarpGeometry = new JitterMatrix(12, "float32", 10);
		helpText = new EditorHelper();
		delaunayMesh = new DelaunayMesh();
		pickedVerticeID = -1;
		selectedVerticeID = -1;
		disregardID = 0;
		context = new Vector<String>();

		viewportDefaultGeometry = new JitterMatrix(12, "float32", 6);
//		viewportDefaultGeometry.setcell(GLCommands.matrix_setCell(0, 1f, 1f, 0, ux, uy, nx, ny, nz));
		enable(0);
	}

	/**
	 * Clears all calibration vertices
	 */
	public void reset(){
		drawable.call("reset");
		for(int i = 0; i < vertices.size(); i++)
			 vertices.get(i).clear();
		vertices.clear();
		for(int i = 0; i < lines.size(); i++)
			 lines.get(i).clear();
		lines.clear();
		pickedVerticeID = -1;
		selectedVerticeID = -1;
		delaunayMesh.clear();
	}

	/**
	 * Sets the provided drawing context
	 * @param context
	 */
	public void addContexts(String[] _context){
		for(int i = 0; i < _context.length; i++){
			addContext(_context[i]);
		}
	}

	/**
	 * Sets the provided drawing context
	 * @param context
	 */
	public void removeContexts(String[] _context){
		for(int i = 0; i < _context.length; i++){
			removeContext(_context[i]);
		}
	}

	/**
	 * Adds a new visualization to the provided drawing contexts
	 * @param context
	 */
	public void addContext(String _context){
		if(!context.contains(_context)){
			context.add(_context);
			updateContext();
		}
	}

	/**
	 * Adds a new visualization to the provided drawing contexts
	 * @param context
	 */
	public void removeContext(String _context){
		if(context.contains(_context)){
			context.remove(_context);
			updateContext();
		}
	}

	private void updateContext(){
		String contxt[] = context.toArray(new String[0]);
		drawable.call("drawto", contxt);
		drawable.call("layer", 20);
		drawable.call("transform_reset", 2);
		for(int i = 0; i < vertices.size(); i++){
			vertices.get(i).setContext(context.toArray(new String[0]));
		}
	}

	public void enable(int _enable){
		drawable.call("enable", _enable);
		for(int i = 0; i < vertices.size(); i++){
			 vertices.get(i).titleText.call("enable", _enable);
			 vertices.get(i).infoText.call("enable", _enable);
		}
		helpText.enable(_enable);
	}

//	public void enable(String _context, int _enable){
//		drawable.call("enable", _enable);
//		for(int i = 0; i < vertices.size(); i++){
//			 vertices.get(i).drawableText.call("enable", _enable);
//		}
//	}

	public void scale(Atom[] scale){
		drawable.call("scale", scale);
	}

	//***********************************************
	//
	// 			MANGEMENT / UPDATE
	//
	//***********************************************

	public void update(Camera _camera){
		CalibVertice vertice;
		Matrix4x4f vpm = _camera.getViewProjectionMatrix();

		for(int i = 0; i < vertices.size(); i++){
			vertice = vertices.get(i);
			vertice.setModelScreenVertice(_camera);
		}

		updateDelaunayMesh();
	}

	public void updateDelaunayMesh(){
		delaunayMesh.clear();
		delaunayMesh.addCalibVertices(vertices);
		for(int i = 0; i < lines.size(); i++){
			lines.get(i).update();
			delaunayMesh.addLineDelaunays(lines.get(i).delaunays);
		}
		delaunayMesh.createFrame();
		delaunayMesh.generateMesh((delaunayMesh.getSubDiv() < 3)?delaunayMesh.getSubDiv() : 2);
		modifyFlag = true;
	}

	public boolean isLinked(){
		return isLinked;
	}

	public void unlink(){
		isLinked = false;
	}

	/**
	 * Updates the calibration object with the model object
	 * @param vertice
	 */
	public void update(ObjectContainer _object){
		for(int i = 0; i < getVerticeCount(); i++){
			if(!getVertice(i).isWarpOnlyType())
				getVertice(i).update(_object.getModelVerticeWorld(getVertice(i).getIndex()));
		}
	}

	/**
	 * Links the calibration object with the model object
	 * @param vertice
	 */
	public void link(ObjectContainer _object){
		for(CalibVertice calVertice: vertices){
			if(!calVertice.isWarpOnlyType()){
				Vertice model = _object.getModelVerticeLocal(calVertice.getModelLocalVertice());
				if(calVertice.getModelLocalVertice().distanceSquared(model) < 0.01f){
					calVertice.update(model);
					calVertice.setIndex(model.getIndex());
				}
			}
		}
		isLinked = true;
	}

	//***********************************************
	//
	// 			DRAW
	//
	//***********************************************

	public void draw(){
		CalibVertice vertice;

		drawable.call("reset");


		switch(modifyMode){
		case MODIFY_MODE_MODEL:
			drawable.call("glcolor", modifyModeModelColor);
			break;
		case MODIFY_MODE_TARGET:
			drawable.call("glcolor", modifyModeTargetColor);
			break;
		case MODIFY_MODE_WARP:
			drawable.call("glcolor", modifyModeWarpColor);
			break;
		}
		//drawable.call("gllinewidth", 2.f);
		drawable.call("framequad", new float[]{1.f, 1.f, 0.f, 1.f, -1.f, 0.f, -1.f, -1.f, 0.f, -1.f, 1.f, 0.f});

		for(int index = 0; index < vertices.size(); index++){
			vertice = vertices.get(index);
			vertice.titleText.call("text", "");
			vertice.infoText.call("text", "");
			Vector3f model_v = vertices.get(index).getModelScreenVerticeOrthoNormal();
			Vector3f target_v = vertices.get(index).getTargetVerticeOrthoNormal();
			Vector3f warp_v = vertices.get(index).getWarpVerticeOrthoNormal();

			if(modifyMode == MODIFY_MODE || modifyMode == MODIFY_MODE_MODEL){
				drawable.call("glcolor", calibSelectedColor);
				vertice.titleText.call("color", calibSelectedColor);
				vertice.infoText.call("color", calibSelectedColor);
				if(vertice.isTargetType()){
					if(drawLabel){
						vertice.titleText.call("position", ssCorrLabelPos(target_v));
						vertice.titleText.call("text", "t" + index);
					}
					if(this.drawCalibResult){
						vertice.infoText.call("position", ssCorrLabelPos(target_v));
						if(vertice.calibrationError >= 0){
							vertice.infoText.call("text", "d=" + String.format ("%.1f", vertice.calibrationError));
						}
					}
					drawable.call("moveto", target_v.get());
				} else if (vertice.isWarpType() || vertice.isWarpOnlyType()) {
					if(drawLabel){
						vertice.titleText.call("position", ssCorrLabelPos(warp_v));
						vertice.titleText.call("text", "w" + index);
					}
					drawable.call("moveto", warp_v.get());
				}
				//drawable.call("gllinewidth", 2.f);
				drawable.call("frameellipse", new float[]{0.03f, 0.03f, 0.f, 360.f});
			}else {
				if(index == selectedVerticeID){
					drawable.call("glcolor", calibSelectedColor);
					vertice.titleText.call("color", calibSelectedColor);
				}else if(index == pickedVerticeID){
					drawable.call("glcolor", calibPickedColor);
					vertice.titleText.call("color", calibPickedColor);
				}else{
					drawable.call("glcolor", calibColor);
					vertice.titleText.call("color", calibColor);
				}
				if(modifyMode == MODIFY_MODE_TARGET && vertice.isTargetType() && crossHairMode){
					drawable.call("moveto", target_v.get());
					if(drawLabel){
						vertice.titleText.call("position", ssCorrLabelPos(target_v));
						vertice.titleText.call("text", "t" + index);
					}
					if(drawCalibResult){
						vertice.infoText.call("position", ssCorrLabelPos(target_v));
						if(vertice.calibrationError >= 0){
							vertice.infoText.call("text", "d=" + String.format ("%.1f", vertice.calibrationError));
						}
					}
				}else if(modifyMode == MODIFY_MODE_WARP && ( vertice.isWarpType() || vertice.isWarpOnlyType()) && drawLabel){
					drawable.call("moveto", warp_v.get());
					vertice.titleText.call("position", ssCorrLabelPos(warp_v));
					if(vertice.isWarpType())
						vertice.titleText.call("text", "w" + index);
					if(vertice.isWarpOnlyType())
						vertice.titleText.call("text", "w");
				}
				if(modifyMode == MODIFY_MODE_TARGET && vertice.isTargetType() && crossHairMode){
					//drawable.call("gllinewidth", 2.f);
					drawable.call("frameellipse", new float[]{0.04f, 0.04f, 0.f, 360.f});
				} else if(modifyMode == MODIFY_MODE_WARP && ( vertice.isWarpType() || vertice.isWarpOnlyType())){
					//drawable.call("gllinewidth", 2.f);
					drawable.call("frameellipse", new float[]{0.03f, 0.03f, 0.f, 360.f});
					if(vertice.isDoubleSelected > 0){
						//drawable.call("gllinewidth", 1.f);
						drawable.call("frameellipse", new float[]{0.01f, 0.01f, 0.f, 360.f});
					}
				}

				if(modifyMode == MODIFY_MODE_TARGET && vertice.isTargetType() && crossHairMode){
					//drawable.call("gllinewidth", 1.f);
					drawable.call("linesegment", new float[]{target_v.x() - 0.01f, target_v.y() - 0.01f, 0, target_v.x() - 0.02f, target_v.y() - 0.02f, 0});
					drawable.call("linesegment", new float[]{target_v.x() - 0.01f, target_v.y() + 0.01f, 0, target_v.x() - 0.02f, target_v.y() + 0.02f, 0});
					drawable.call("linesegment", new float[]{target_v.x() + 0.01f, target_v.y() - 0.01f, 0, target_v.x() + 0.02f, target_v.y() - 0.02f, 0});
					drawable.call("linesegment", new float[]{target_v.x() + 0.01f, target_v.y() + 0.01f, 0, target_v.x() + 0.02f, target_v.y() + 0.02f, 0});
				}

			}


			/*
			 * DRAWING CROSSHAIR
			 */
			if(index == pickedVerticeID || index == selectedVerticeID){
				//drawable.call("gllinewidth", 1.f);
				drawable.call("glcolor", crossHairLinesColor);
				if(modifyMode == MODIFY_MODE_TARGET){
					if(crossHairMode == true){
						drawable.call("linesegment", new float[]{target_v.x(), -1.f, 0, target_v.x(), 1.f, 0});
						drawable.call("linesegment", new float[]{-1.f, target_v.y(), 0, 1.f, target_v.y(), 0});
					} else {
						drawable.call("glcolor", new float[]{1, 1, 1, 1});
						drawable.call("quad", new float[]{
								target_v.x(), target_v.y(), 0,
								-1.0f, target_v.y(), 0,
								-1.0f, -1.0f, 0,
								target_v.x(), -1.f, 0});
						drawable.call("quad", new float[]{
								target_v.x(), target_v.y(), 0,
								1.0f, target_v.y(), 0,
								1.0f, 1.0f, 0,
								target_v.x(), 1.f, 0});
						drawable.call("glcolor", new float[]{0, 0, 0, 1});
						drawable.call("quad", new float[]{
								target_v.x(), target_v.y(), 0,
								target_v.x(), 1.f, 0,
								-1.0f, 1.0f, 0,
								-1.0f, target_v.y(), 0});
						drawable.call("quad", new float[]{
								target_v.x(), target_v.y(), 0,
								target_v.x(), -1.f, 0,
								1.0f, -1.0f, 0,
								1.0f, target_v.y(), 0});

					}
				}else if (modifyMode == MODIFY_MODE_WARP){
					drawable.call("linesegment", new float[]{warp_v.x(), -1.f, 0, warp_v.x(), 1.f, 0});
					drawable.call("linesegment", new float[]{-1.f, warp_v.y(), 0, 1.f, warp_v.y(), 0});
				}
			}

			if (modifyMode == MODIFY_MODE_WARP && vertice.isWarpType() ||
					(modifyMode != MODIFY_MODE_TARGET && vertice.isWarpOnlyType() && crossHairMode)){
				drawable.call("glcolor", linkColor);
				drawable.call("linesegment",new float[]{
						model_v.x(),
						model_v.y(),
						model_v.z(),
						warp_v.x(),
						warp_v.y(),
						warp_v.z()});
				drawable.call("moveto", model_v.get());
				drawable.call("frameellipse", new float[]{0.02f, 0.02f, 0.f, 360.f});
			} else if(modifyMode != MODIFY_MODE_WARP && vertice.isTargetType() && crossHairMode){
				drawable.call("glcolor", linkColor);
				drawable.call("linesegment",new float[]{
						model_v.x(),
						model_v.y(),
						model_v.z(),
						target_v.x(),
						target_v.y(),
						target_v.z()});
			}
		}

		/*
		 * DRAW WARP MESH
		 */
		if(modifyMode == MODIFY_MODE_WARP){
			for(int i = 0; i < lines.size(); i++){
				CalibLine line = lines.get(i);
				//drawable.call("gllinewidth", 3.f);
				drawable.call("glcolor", delaunaySetEdge);
				drawable.call("linesegment",new float[]{
						line.verticeA.getWarpVerticeOrthoNormal().x(),
						line.verticeA.getWarpVerticeOrthoNormal().y(),
						line.verticeA.getWarpVerticeOrthoNormal().z(),
						line.verticeB.getWarpVerticeOrthoNormal().x(),
						line.verticeB.getWarpVerticeOrthoNormal().y(),
						line.verticeB.getWarpVerticeOrthoNormal().z()});
			}

			//drawable.call("gllinewidth", 1.f);
			drawable.call("glcolor", delaunayEdge);
			delaunayMesh.draw(drawable);
		}

		if(drawHelp){
			if(modifyMode == MODIFY_MODE){
				if(editMode == EDIT_MODE_NONE)
					helpText.printModelNONE();
				if(this.isEditModeValueListening("p"))
					helpText.printModelVALUE_P();
			}
			if(modifyMode == MODIFY_MODE_MODEL){
				if(editMode == EDIT_MODE_SELECTED)
					helpText.printModelSEL();
			} else if(modifyMode == MODIFY_MODE_TARGET){
				if(editMode == EDIT_MODE_SELECTED)
					helpText.printTargetSEL(disregardID);
				if(editMode == EDIT_MODE_GRAB)
					helpText.printTargetGRAB();
				if(this.isEditModeValueListening("j"))
					helpText.printTargetVALUE_J();
			} else if(modifyMode == MODIFY_MODE_WARP){
				if(editMode == EDIT_MODE_NONE)
					helpText.printWarpNONE(delaunayMesh.getFrame(), delaunayMesh.getSubDiv());
				if(editMode == EDIT_MODE_SELECTED)
					helpText.printWarpSEL(isDoubleSelected());
				if(editMode == EDIT_MODE_GRAB)
					helpText.printWarpGRAB();
				if(this.isEditModeValueListening("f"))
					helpText.printWarpVALUE_F();
				if(this.isEditModeValueListening("d"))
					helpText.printWarpVALUE_D();
				if(this.isEditModeValueListening("j"))
					helpText.printWarpVALUE_J();
			}
		}
				//drawable.call("draw");
	}

	/**
	 * screen space corrected label position<br>
	 * calculates an appropriate position for the label so it always is on screen
	 * @param _vec expects a Vector3f object
	 * @return a float array
	 */
	private float[] ssCorrLabelPos(Vector3f _vec){
		return new float[] {(_vec.x() < 0.9)?(_vec.x() + .025f):(_vec.x() - 0.1f), (_vec.y() < 0.9)?(_vec.y() + .025f):(_vec.y() - 0.1f), 0};
	}

	public String generateGeometry(){
		delaunayMesh.createMatrixGeomCommands(viewportWarpGeometry);
		return viewportWarpGeometry.getName();
	}

	//***********************************************
	//
	// 			Edit Helper
	//
	//***********************************************

	public void resetModified(){
		modifyFlag = false;
	}

	public boolean wasModified(){
		return modifyFlag;
	}

	public boolean toggleHelpVisibility(){
		drawHelp = !drawHelp;
		helpText.enable((drawHelp)?1:0);
		return drawHelp;
	}

	public void changeHelpColor(){
		helpText.changeColor();
	}

	/**
	 * Picks the first vertice that is within picking distance
	 * @param pickPosX normalized coordinates
	 * @param pickPosY normalized coordinates
	 * @return
	 */
	public boolean pick(float pickPosX, float pickPosY){
		CalibVertice vertice;
		boolean pick = false;
		for(int i = 0; i < vertices.size(); i++){
			vertice = vertices.get(i);
			if(!pick){
				if(modifyMode == MODIFY_MODE_WARP || vertice.isWarpOnlyType()){
					if(Math.abs(vertice.getWarpVertice().x() - pickPosX) < pickRadius &&
							Math.abs(vertice.getWarpVertice().y() - pickPosY) < pickRadius){
						pickedVerticeID = i;
						pick = true;
						modifyFlag = true;
					}
				}else{
					if(Math.abs(vertice.getTargetVertice().x() - pickPosX) < pickRadius &&
							Math.abs(vertice.getTargetVertice().y() - pickPosY) < pickRadius){
						pickedVerticeID = i;
						pick = true;
						modifyFlag = true;
					}
				}
			}
		}
		if(!pick)
			pickedVerticeID = -1;
		return pick;
	}

	public boolean isPickedAWarpOnly(){
		if(pickedVerticeID != -1){
			return vertices.get(pickedVerticeID).isWarpOnlyType();
		}
		return false;
	}

	public boolean select(){
		selectedVerticeID = pickedVerticeID;
		modifyFlag = true;
		return (selectedVerticeID != -1)? true: false;
	}

	public void deselect(){
		selectedVerticeID = -1;
		modifyFlag = true;
		for(int j = 0; j < vertices.size(); j++){
			vertices.get(j).doubleSelect(false);
			vertices.get(j).doubleSelect(false);
		}
	}

	public boolean doubleSelect(){
		if(pickedVerticeID != -1){
			CalibVertice vertice = vertices.get(pickedVerticeID);
			for(int j = 0; j < vertices.size(); j++){
				if(j != pickedVerticeID){
					// goes through all the vertices except the one picket
					if(vertice.isDoubleSelected != 1){
						// and sets the douleselect if the picket vertice wasn't picket before.
						vertices.get(j).doubleSelect(false);
					}
				}
			}
			modifyFlag = true;
			vertice.doubleSelect(true);
			selectedVerticeID = pickedVerticeID;
			return true;
		}
		return false;
	}

	public boolean isDoubleSelected(){
		CalibVertice vertice;
		int selected = 0;
		for(int i = 0; i < vertices.size(); i++){
			vertice = vertices.get(i);
			if(vertice.isDoubleSelected > 0){
				selected++;
			}
		}
		return (selected == 2)?true: false;
	}

	/**
	 * get the ObjectPoints for calibration
	 * @return
	 */
	public MatOfPoint3f getObjectPoints(){
		MatOfPoint3f objectPoints = new MatOfPoint3f();
		for(CalibVertice vert: vertices){
			if(vert.isTargetType()){
				objectPoints.push_back(vert.getObjectPoint());
			}
		}
		return objectPoints;
	}

	public MatOfPoint2f getImagePoints(float width, float height){
		MatOfPoint2f imagePoints = new MatOfPoint2f();
		for(CalibVertice vert: vertices){
			if(vert.isTargetType()){
				imagePoints.push_back(vert.getImagePoint(width, height));
			}
		}
		return imagePoints;
	}

	public void printStats(int width, int height){
		for(CalibVertice vert: vertices){
			if(vert.isTargetType()){
				double distance =
						Math.sqrt(
						Math.pow(Math.abs((double)(vert.getTargetVertice().x() - vert.getModelScreenVertice().x())) * width,2) +
						Math.pow(Math.abs((double)(vert.getTargetVertice().y() - vert.getModelScreenVertice().y())) * height,2));
				Debug.info("vertice " + vert.getIndex(), "distance: " + distance);
			}
		}
	}

	public int getVerticeCount(){
		return (vertices != null)?vertices.size():0;
	}

	public int getLineCount(){
		return (lines != null)?lines.size():0;
	}

	/**
	 * returns the calibration vertice at the specified index
	 * @param index
	 * @return
	 */
	public CalibVertice getVertice(int index){
		return vertices.get(index);
	}

	public CalibVertice getSelectedVertice(){
		return vertices.get(selectedVerticeID);
	}

	private boolean updateVerticeWithSameLocalPos(ObjectContainer object){
		for(int id = 0; id < getVerticeCount(); id++){
			CalibVertice vert = vertices.get(id);
			Vertice model = object.getModelVerticeLocal(vert.getModelLocalVertice());
			if(vert.getModelLocalVertice().distanceSquared(model) < 0.01f){
				vertices.get(id).update(model);
				return true;
			}
		}
		return false;
	}

	private boolean hasVerticeWithSameIndex(Vertice _vertice){
		for(int id = 0; id < getVerticeCount(); id++){
			if(vertices.get(id).getIndex() == _vertice.getIndex())
				return true;
		}
		return false;
	}


	//***********************************************
	//
	// 			LOAD  and SAVE
	//
	//***********************************************

	public void load(String _filepath){
		File dieXMLDatei = new File(_filepath);

		if(dieXMLDatei.exists()){
			Debug.verbose("loading calibration targets ", "'"+_filepath+"'");

			try {
				DocumentBuilderFactory dasDBFactoryObjekt = DocumentBuilderFactory.newInstance();
				DocumentBuilder dasDBObjekt = dasDBFactoryObjekt.newDocumentBuilder();
				document = dasDBObjekt.parse(dieXMLDatei);
			} catch (Exception e) {
				Debug.error(this.getClass(), "Error on loading calibration targets:" + e.getMessage());
				return;
			}

			document.getDocumentElement().normalize();

			Element root = document.getDocumentElement();
			NamedNodeMap values = root.getAttributes();
			Node version = values.getNamedItem(ATTR_VERSION);
			if(version != null && version.getTextContent().equals(VERSION)){
				delaunayMesh.setFrame((Integer.valueOf(values.getNamedItem(ATTR_FRAME).getTextContent())).intValue());
				delaunayMesh.setSubDiv((Integer.valueOf(values.getNamedItem(ATTR_SUBDIV).getTextContent())).intValue());

				NodeList verticeList = document.getElementsByTagName(CalibVertice.ELEMENT_VERTICE);
				NodeList lineList = document.getElementsByTagName(CalibLine.ELEMENT_LINE);

				reset();

				for(int i = 0; i < verticeList.getLength(); i++){
					Node vertice = verticeList.item(i);
					CalibVertice newVertice = new CalibVertice(vertice);
					vertices.add(newVertice);
				}

				for(int i = 0; i < lineList.getLength(); i++){
					Node line = lineList.item(i);
					lines.add(new CalibLine(line, vertices));
				}

				//check if there is a sceneOffset Vertice
				for(int i = 0; i < vertices.size(); i++){
					if(vertices.get(i).isWarpOnlyType())
						warpIndex = vertices.get(i).getIndex();
				}

				isLinked = false;
				Debug.verbose("Calibration Object", "loaded " + vertices.size() + " vertice-targets");
			} else {
				Debug.warning("Calibration Object", "loaded script (" + _filepath + ") has an invalid version. Script requires Version=" + VERSION);
			}
			updateContext();
			updateDelaunayMesh();
		} else {
			Debug.warning("Calibration Object", "attempted to load script (" + _filepath + "). Script doesn't exist");
		}

	}

	/**
	 * @return true if a calibration file was successfully loaded
	 */
	public boolean isLoaded(){
		return (vertices.size() > 0)? true: false;
	}

	public boolean save(String _filePath){
		if(_filePath != null)
			return saveas(_filePath);
		return false;
	}

	public boolean saveas(String _filepath){
		try {
			DocumentBuilderFactory dasDBFactoryObjekt = DocumentBuilderFactory.newInstance();
			DocumentBuilder dasDBObjekt = dasDBFactoryObjekt.newDocumentBuilder();
			document = dasDBObjekt.newDocument();
			Element calibration = document.createElement(ELEMENT_CALIBRATION);

			Attr s_version = document.createAttribute(ATTR_VERSION);
	        s_version.setValue(""+VERSION);
	        calibration.setAttributeNode(s_version);

			Attr s_frame = document.createAttribute(ATTR_FRAME);
			s_frame.setValue(""+delaunayMesh.getFrame());
	        calibration.setAttributeNode(s_frame);

			Attr s_subdiv = document.createAttribute(ATTR_SUBDIV);
			s_subdiv.setValue(""+delaunayMesh.getSubDiv());
	        calibration.setAttributeNode(s_subdiv);


			document.appendChild(calibration);

			for(int i = 0; i < vertices.size(); i++){
				vertices.get(i).addNodeTo(document);
			}

			for(int i = 0; i < lines.size(); i++){
				lines.get(i).addNodeTo(document);
			}

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource        source = new DOMSource( document );
			FileOutputStream os = new FileOutputStream( new File( _filepath ) );
			StreamResult     result = new StreamResult( os );
			transformer.transform( source, result );
			System.out.println("Saved calibration-data at: " + _filepath);
			return true;
		} catch (Exception e) {
			Debug.error(this.getClass(), "DocumentBuilder Exceptions:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public void saveDelaunayMesh(String _filepath){
		_filepath = _filepath.replace("\\", "/");
		String filename = _filepath.substring(_filepath.lastIndexOf("/"), _filepath.lastIndexOf(".xml")) + ".obj";
		String file = _filepath.substring(0, _filepath.lastIndexOf("/_projectors")) + "/_models/_warps/" + filename;

		if(delaunayMesh.vertices.size() > 0){
			delaunayMesh.generateMesh(delaunayMesh.getSubDiv());
			delaunayMesh.saveAsOBJ(file);
			delaunayMesh.generateMesh((delaunayMesh.getSubDiv() < 3)?delaunayMesh.getSubDiv() : 2);
			System.out.println("Saved calibration-mesh at: " + file);
		}
	}

	//***********************************************
	//
	// 			Settings
	//
	//***********************************************

	public void swapIndex(int _newIndex){
		if(pickedVerticeID != -1 && _newIndex  < vertices.size()){
			CalibVertice verticePick;
			verticePick = vertices.get(pickedVerticeID);
			CalibVertice verticeSwapped = vertices.get(_newIndex);
			vertices.set(_newIndex, verticePick);
			vertices.set(pickedVerticeID, verticeSwapped);
			pickedVerticeID = _newIndex;
		}
	}

	public void setSelectedIDToDisregardID(){
		if(this.selectedVerticeID != -1)
			disregardID = selectedVerticeID;
	}

	public void setDelaunayMeshFrame(int _frame){
		delaunayMesh.setFrame(_frame);
		updateDelaunayMesh();
	}

	public void setDelaunayMeshSubDiv(int _subdiv){
		delaunayMesh.setSubDiv(_subdiv);
		updateDelaunayMesh();
	}

	//***********************************************
	//
	// 			Modify  Vertices
	//
	//***********************************************

	/**
	 * Adds new lines based on the seleced targets. If _sub is 0 an existing line will be removed.
	 *
	 * @param _sub sets the subdivision for this line.
	 */
	public void createLine(int _sub){
		int[] vert_indx = new int[2];
		int idx = 0;
		CalibVertice vertice;
		for(int i = 0; i < vertices.size(); i++){
			vertice = vertices.get(i);
			if(idx <= 1 && vertice.isDoubleSelected > 0){
				vert_indx[idx++] = i;
			}
		}
		if(idx == 2){ // there were twoe selected target vertices
			for(int i = 0; i < getLineCount(); i++){
				if(lines.get(i).isIdentical(vertices.get(vert_indx[0]), vertices.get(vert_indx[1]))){
					lines.get(i).clear();
					lines.remove(i);
				}
			}
			if(_sub != 0)
				lines.add(new CalibLine(vertices.get(vert_indx[0]), vertices.get(vert_indx[1]), _sub));

			updateDelaunayMesh();
		}
	}

	/**
	 * moves the selected vertice to the specified position
	 * @param mousePosX normalized coordinate
	 * @param mousePosY normalized coordinate
	 * @return
	 */
	public void move(float mousePosX, float mousePosY){
		if(selectedVerticeID != -1){
			if(modifyMode == MODIFY_MODE_TARGET){
				vertices.get(selectedVerticeID).setTargetVertice(new Vector3f(mousePosX, mousePosY, 0.f));
			}else if(modifyMode == MODIFY_MODE_WARP){
				vertices.get(selectedVerticeID).setWarpVertice(new Vector3f(mousePosX, mousePosY, 0.f));
			}
		}
		modifyFlag = true;
	}

	/**
	 * adds the values to the picked vertice
	 * @param diffPosX normalized coordinate
	 * @param diffPosY normalized coordinate
	 * @return
	 */
	public void addmove(float diffPosX, float diffPosY){
		if(selectedVerticeID != -1){
			if(modifyMode == MODIFY_MODE_TARGET){
				Vector3f target = vertices.get(selectedVerticeID).getTargetVertice();
				vertices.get(selectedVerticeID).setTargetVertice(target.addMake(new Vector3f(diffPosX, diffPosY, 0.f)));
			}else if(modifyMode == MODIFY_MODE_WARP){
				Vector3f warp = vertices.get(selectedVerticeID).getWarpVertice();
				vertices.get(selectedVerticeID).setWarpVertice(warp.addMake(new Vector3f(diffPosX, diffPosY, 0.f)));
			}
		}
		modifyFlag = true;
	}

	/**
	 * Adds a new vertice to the calibration object,
	 * or updates it if there is one with the same index
	 *
	 * @param _object
	 * @param _camera
	 * @param _isTarget
	 * @param _isWarp
	 * @param _isWarpOnly
	 * @param _isOrigin
	 */
	public void add(ObjectContainer _object, Camera _camera, boolean _isTarget, boolean _isWarp){
		if(_object != null && _camera != null){
			if(!hasVerticeWithSameIndex(_object.getSelectedVerticeWorld())){
				Vertice localVertice = _object.getSelectedVerticeLocal();
				Vertice worldVertice = _object.getSelectedVerticeWorld();

				CalibVertice cvertice;

				cvertice = new CalibVertice(localVertice, worldVertice, _isTarget, _isWarp);

				Vector3f screen = _camera.worldToScreen(worldVertice, _camera.getViewProjectionMatrix(), _camera.viewport);

				cvertice.setModelScreenVertice(_camera);
				cvertice.setTargetVertice(screen);
				cvertice.setWarpVertice(screen);
				cvertice.setIndex(localVertice.getIndex());

				vertices.add(cvertice);
				updateContext();
				updateDelaunayMesh();
			}
		}
	}

	/**
	 * add warp only calibration vertice
	 * @param _camera
	 * @param _warpPosX
	 * @param _warpPosY
	 */
	public void add(Camera _camera, float _warpPosX, float _warpPosY){
		CalibVertice cvertice = new CalibVertice(new Vector3f(), new Vector3f(), false, true);

		cvertice.setModelScreenVertice(_camera);
		cvertice.setWarpVertice(new Vector3f(_warpPosX, _warpPosY, 0.f));
		cvertice.setTargetVertice(new Vector3f(_warpPosX, _warpPosY, 0.f));
		cvertice.setIndex(--warpIndex);

		vertices.add(cvertice);
		updateContext();
		updateDelaunayMesh();
	}

	/**
	 * Removes the currently selected vertice.
	 *
	 * @param vertice
	 */
	public void remove(){
		if(selectedVerticeID != -1)
			remove(selectedVerticeID);
	}

	/**
	 * Removes from the currently selected vertice selectively the target-vertice or the warp-vertice.
	 *
	 * @param _target if true it removes the target-vertice
	 * @param _warp if true it removes the warp-vertice
	 */
	public void remove(boolean _target, boolean _warp){
		if(selectedVerticeID != -1){
			if(_target){
				if(!vertices.get(selectedVerticeID).setTargetType(false))
					remove(selectedVerticeID);
			}
			if(_warp){
				if(vertices.get(selectedVerticeID).isWarpOnlyType() ||
					!vertices.get(selectedVerticeID).setWarpType(false))
					remove(selectedVerticeID);
			}
		}
	}

	/**
	 * Removes the calibration vertice with the same index as the provided one.
	 *
	 * @param vertice
	 */
	public void remove(Vertice vertice){
		if(vertice != null){
			for(int id = 0; id < getVerticeCount(); id++){
				if(vertices.get(id).getIndex() == vertice.getIndex()){
					remove(id);
				}
			}
		}
		modifyFlag = true;
	}

	/**
	 * Removes the vertice with the provided index.
	 *
	 * @param _id
	 */
	private void remove(int _id){
		// removes lists that are using this vertice
		for(int i = 0; i < getLineCount(); i++){
			if(lines.get(i).hasVertice(vertices.get(_id))){
				lines.get(i).clear();
				lines.remove(i);
			}
		}
		vertices.get(_id).clear();
		vertices.remove(_id);
		updateDelaunayMesh();
	}

	public void adjustTargetToWarp(){
		if(selectedVerticeID != -1){
			vertices.get(selectedVerticeID).setTargetVertice(vertices.get(selectedVerticeID).getWarpVertice().clone());
		} else {
			for(int i = 0; i < vertices.size(); i++){
				if(vertices.get(i).isWarpType())
					vertices.get(i).setTargetVertice(vertices.get(i).getWarpVertice().clone());
			}
		}
		updateDelaunayMesh();
	}

	public void adjustTargetToModel(){
		if(selectedVerticeID != -1){
			vertices.get(selectedVerticeID).setTargetVertice(vertices.get(selectedVerticeID).getModelScreenVertice().clone());
		} else {
			for(int i = 0; i < vertices.size(); i++){
				if(vertices.get(i).isTargetType())
					vertices.get(i).setTargetVertice(vertices.get(i).getModelScreenVertice().clone());
			}
		}
		updateDelaunayMesh();
	}

	public void adjustWarpToTarget(){
		if(selectedVerticeID != -1){
			vertices.get(selectedVerticeID).setWarpVertice(vertices.get(selectedVerticeID).getTargetVertice().clone());
		} else {
			for(int i = 0; i < vertices.size(); i++){
				if(vertices.get(i).isTargetType())
					vertices.get(i).setWarpVertice(vertices.get(i).getTargetVertice().clone());
			}
		}
		updateDelaunayMesh();
	}

	public void adjustWarpToModel(){
		if(selectedVerticeID != -1){
			vertices.get(selectedVerticeID).setWarpVertice(vertices.get(selectedVerticeID).getModelScreenVertice().clone());
		} else {
			for(int i = 0; i < vertices.size(); i++){
				if(vertices.get(i).isWarpType())
					vertices.get(i).setWarpVertice(vertices.get(i).getModelScreenVertice().clone());
			}
		}
		updateDelaunayMesh();
	}

	//***********************************************
	//
	// Methods used for calibration / Number Cruncher
	//
	//***********************************************

	/**
	 * Enables all calibration vertices for calculation
	 */
	public void enableAllVertices(){
		for(int i = 0; i < vertices.size(); i++)
			 vertices.get(i).isEnabled = true;;
	}

	/**
	 * Disables the next calibration vertice for calculation
	 * @return true if it has disabled another vertice
	 */
	public boolean disableNextVertice(){
		for(int i = 0; i < vertices.size(); i++){
			 if(disregardID > i && vertices.get(i).isEnabled){
				 vertices.get(i).isEnabled = false;
				 return true;
			 }
		}
		return false;
	}

	//***********************************************
	//
	// Modify And Edit - Modes
	//
	//***********************************************

	public void setModifyModeToNone(){
		modifyMode = MODIFY_MODE;
		editMode = EDIT_MODE_NONE;
		modifyFlag = true;
	}

	public boolean isModifyModeNone(){
		return (modifyMode == MODIFY_MODE)?true: false;
	}

	public void setModifyModeToModel(){
		modifyMode = MODIFY_MODE_MODEL;
		editMode = EDIT_MODE_NONE;
		modifyFlag = true;
	}

	public boolean isModifyModeModel(){
		return (modifyMode == MODIFY_MODE_MODEL)?true: false;
	}

	public void setModifyModeToTarget(){
		modifyMode = MODIFY_MODE_TARGET;
		editMode = EDIT_MODE_NONE;
		modifyFlag = true;
	}

	public boolean isModifyModeTarget(){
		return (modifyMode == MODIFY_MODE_TARGET)?true: false;
	}

	public void setModifyModeToWarp(){
		modifyMode = MODIFY_MODE_WARP;
		editMode = EDIT_MODE_NONE;
		modifyFlag = true;
	}

	public boolean isModifyModeWarp(){
		return (modifyMode == MODIFY_MODE_WARP)?true: false;
	}

	public void setEditModeToNone(){
		editMode = EDIT_MODE_NONE;
		if(crossHairMode == false){
			toggleCrossHairMode();
		}
		modifyFlag = true;
	}

	public boolean isEditModeNone(){
		return (editMode == EDIT_MODE_NONE)?true: false;
	}

	public void setEditModeToSelected(){
		editMode = EDIT_MODE_SELECTED;
		modifyFlag = true;
	}

	public void setEditModeToValueListening(String _key){
		editMode = EDIT_MODE_VALUE_LISTENING;
		editModeValueListenKey = _key;
		modifyFlag = true;
	}

	public boolean isEditModeSelected(){
		return (editMode == EDIT_MODE_SELECTED)?true: false;
	}

	public void setEditModeToGrab(){
		editMode = EDIT_MODE_GRAB;
		modifyFlag = true;
	}

	public boolean isEditModeGrab(){
		return (editMode == EDIT_MODE_GRAB)?true: false;
	}

	public boolean isEditModeValueListening(String _key){
		return (editMode == EDIT_MODE_VALUE_LISTENING && editModeValueListenKey.equals(_key))?true: false;
	}

	public void toggleLabelVisibility(){
		drawLabel = !drawLabel;
		drawCalibResult = false;
		modifyFlag = true;
	}

	public void toggleCalibResultVisibility(){
		drawCalibResult = !drawCalibResult;
		drawLabel = !drawCalibResult;
		modifyFlag = true;
	}

	public void toggleCrossHairMode(){
		crossHairMode = !crossHairMode;
		if(crossHairMode == false){
			helpText.enable(0);
		} else {
			helpText.enable((drawHelp)?1:0);
		}
		modifyFlag = true;
	}

	public void notifyDeleted(){
		drawable.freePeer();
		helpText.free();
		viewportWarpGeometry.freePeer();
		for(int i = 0; i < vertices.size(); i++){
			 vertices.get(i).clear();
		}
	}

	public static void main(String[] args) {

		Calibrations obj = new Calibrations();

		obj.load("/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/data/models/cube.xml");

		Vertice vert1 = new Vertice(2f, 3f, 5f);
		Vertice vert2 = new Vertice(5f, 4f, 6f);

		obj.saveas("/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/data/models/cube2.xml");

		/* multiplying matrices */

	}

}
