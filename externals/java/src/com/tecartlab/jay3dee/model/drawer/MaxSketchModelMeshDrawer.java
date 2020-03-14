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


package com.tecartlab.jay3dee.model.drawer;

import java.util.ArrayList;

import com.cycling74.jitter.JitterMatrix;
import com.cycling74.jitter.JitterObject;
import com.cycling74.max.Atom;

import com.tecartlab.jay3dee.MeshCanvas;
import com.tecartlab.jay3dee.ModelContainer;
import com.tecartlab.jay3dee.SketchCanvas;
import com.tecartlab.jay3dee.model.*;
import com.tecartlab.tools.math.la.Quaternionf;
import com.tecartlab.tools.math.la.Vector3f;

public class MaxSketchModelMeshDrawer implements ModelDrawer {

	public static final int RENDER_NONE = 0;
	public static final int RENDER_ALL = 1;
	public static final int RENDER_FACES = 2;
	public static final int RENDER_LINES = 3;
	public static final int RENDER_POINTS = 4;
	public static final int RENDER_LINES_POINTS = 5;
	public static final int RENDER_FACES_LINES = 6;
	public static final int RENDER_FACES_POINTS = 7;

	private ModelData model;

	public MeshCanvas facesFront;
	public MeshCanvas facesBack;
	public MeshCanvas linesFront;
	public MeshCanvas linesBack;
	public MeshCanvas pointsFront;
	public MeshCanvas pointsBack;

	public MeshCanvas simpleLines;

	public SketchCanvas selPoints;

	public SketchCanvas texFaces;
	public SketchCanvas texLines;
	public SketchCanvas texPoints;

	protected JitterMatrix sketchFaceFrontCommands;
	protected JitterMatrix sketchFaceBackCommands;
	protected JitterMatrix sketchLineBackCommands;
	protected JitterMatrix sketchLineFrontCommands;
	protected JitterMatrix sketchPointFrontCommands;
	protected JitterMatrix sketchPointBackCommands;

	protected JitterMatrix sketchSimpleLineCommands;

	protected ArrayList<MaxSketchMessage> selSketchPointCommands;

	protected ArrayList<MaxSketchMessage> sketchTexFaceCommands;
	protected ArrayList<MaxSketchMessage> sketchTexLineCommands;
	protected ArrayList<MaxSketchMessage> sketchTexPointCommands;

	public float[] faceEditColor 		= {0.7f, 0.7f, 0.7f, 0.5f};
	public float[] lineColorBack 		= {1.f, 0.45f, 0.40f, 0.2f};
	public float[] lineColorFront		= {1.f, 0.45f, 0.40f, 1.0f};
	public float[] pointColor 			= {1.f, 0.45f, 0.50f, 1.0f};
	public float[] pickedPointColor		= {1.0f, 0.8f, 0.8f, 1.0f};
	public float[] selectedPointColor 	= {1.0f, 0.0f, 0.0f, 1.0f};
	public float[] bkgFaceColor			= {0.7f, 0.7f, 0.7f, 0.2f};

	private float textureShiftX = -0.5f;
	private float textureShiftY = -0.5f;

	public float pointSizeFront 		= 5.f;
	public float pointSizeBack	 		= 1.f;
	public float pickedPointSize 		= 7.f;
	public float selectedPointSize      = 9.f;

	private boolean isInitialized = false;
	private boolean hasModelChanged = false;
	private boolean hasTextureChanged = false;
	private boolean hasSelectionChanged = false;

	private int renderMode;
	private int normalRenderMode = RENDER_ALL;
	private int editRenderMode = RENDER_LINES_POINTS;

	public MaxSketchModelMeshDrawer(){
		facesBack = new MeshCanvas(1);
		simpleLines = new MeshCanvas(1);
		linesBack = new MeshCanvas(2);
		pointsBack = new MeshCanvas(3);
		facesFront = new MeshCanvas(4);
		linesFront = new MeshCanvas(5);
		pointsFront = new MeshCanvas(6);
		selPoints = new SketchCanvas(7);
		texFaces = null;
		texLines = null;
		texPoints = null;

		sketchFaceFrontCommands = new JitterMatrix(12, "float32", 10);
		sketchLineFrontCommands = new JitterMatrix(12, "float32", 10);
		sketchPointFrontCommands = new JitterMatrix(12, "float32", 10);
		sketchFaceBackCommands = new JitterMatrix(12, "float32", 10);
		sketchLineBackCommands = new JitterMatrix(12, "float32", 10);
		sketchPointBackCommands = new JitterMatrix(12, "float32", 10);

		sketchSimpleLineCommands = new JitterMatrix(12, "float32", 10);

		selSketchPointCommands = new ArrayList<MaxSketchMessage>();
		sketchTexFaceCommands = new ArrayList<MaxSketchMessage>();
		sketchTexLineCommands = new ArrayList<MaxSketchMessage>();
		sketchTexPointCommands = new ArrayList<MaxSketchMessage>();
	}

	public void initialize(ModelData _model){
		model = _model;
		isInitialized = true;
		modelChange();
		textureChange();
	}

	public void reset(){
		isInitialized = false;
	}

	public void draw() {
		if(isInitialized){
			if(hasModelChanged){
				drawupdate();
			}
			if(hasSelectionChanged){
				if(selPoints != null){
					selPoints.executeMessage(MaxSketchMessageFactory.sketch_reset());
					selPoints.drawGlCommand(selSketchPointCommands);
				}
			}
		}
	}

	/**
	 * Draws all
	 * Returns the vertice matrix name
	 */
	public String drawraw() {
		if(isInitialized){
			updateFacesAndLines();
			updatePointCommands();
			updateTextureFacesAndLines();
			updateTexPointCommands();

			drawupdate();

			return sketchFaceFrontCommands.getName();
		}
		return "undefined";
	}

	public void drawupdate() {
		if(facesFront != null && facesBack != null){
			facesFront.jitMatrix(sketchFaceFrontCommands.getName());
			facesFront.executeMessage(MaxSketchMessageFactory.draw_mode("triangles"));
			facesFront.executeMessage(MaxSketchMessageFactory.lighting_enable(0));
			facesFront.executeMessage(MaxSketchMessageFactory.sketch_cullFace(1));
			facesFront.executeMessage(MaxSketchMessageFactory.poly_mode(0, 0));
			facesFront.executeMessage(MaxSketchMessageFactory.blend_enable(1));

			facesBack.jitMatrix(sketchFaceBackCommands.getName());
			facesBack.executeMessage(MaxSketchMessageFactory.draw_mode("triangles"));
			facesBack.executeMessage(MaxSketchMessageFactory.lighting_enable(0));
			facesBack.executeMessage(MaxSketchMessageFactory.sketch_cullFace(2));
			facesBack.executeMessage(MaxSketchMessageFactory.poly_mode(0, 0));
			facesBack.executeMessage(MaxSketchMessageFactory.blend_enable(1));
		}
		if(linesFront != null && linesBack != null){
			linesFront.jitMatrix(sketchLineFrontCommands.getName());
			linesFront.executeMessage(MaxSketchMessageFactory.draw_mode("triangles"));
			linesFront.executeMessage(MaxSketchMessageFactory.lighting_enable(0));
			linesFront.executeMessage(MaxSketchMessageFactory.sketch_cullFace(1));
			linesFront.executeMessage(MaxSketchMessageFactory.poly_mode(1, 1));

			linesBack.jitMatrix(sketchLineBackCommands.getName());
			linesBack.executeMessage(MaxSketchMessageFactory.draw_mode("triangles"));
			linesBack.executeMessage(MaxSketchMessageFactory.lighting_enable(0));
			linesBack.executeMessage(MaxSketchMessageFactory.sketch_cullFace(2));
			linesBack.executeMessage(MaxSketchMessageFactory.poly_mode(1, 1));
		}
		if(simpleLines != null){
			simpleLines.jitMatrix(sketchSimpleLineCommands.getName());
			simpleLines.executeMessage(MaxSketchMessageFactory.draw_mode("lines"));
			simpleLines.executeMessage(MaxSketchMessageFactory.lighting_enable(0));
			simpleLines.executeMessage(MaxSketchMessageFactory.sketch_cullFace(1));
			simpleLines.executeMessage(MaxSketchMessageFactory.poly_mode(1, 1));
		}
		if(pointsFront != null && pointsBack != null){
			pointsFront.jitMatrix(sketchPointFrontCommands.getName());
			pointsFront.executeMessage(MaxSketchMessageFactory.draw_mode("triangles"));
			pointsFront.executeMessage(MaxSketchMessageFactory.lighting_enable(0));
			pointsFront.executeMessage(MaxSketchMessageFactory.point_size(pointSizeFront));
			pointsFront.executeMessage(MaxSketchMessageFactory.sketch_cullFace(1));
			pointsFront.executeMessage(MaxSketchMessageFactory.poly_mode(2, 2));

			pointsBack.jitMatrix(sketchPointBackCommands.getName());
			pointsBack.executeMessage(MaxSketchMessageFactory.draw_mode("triangles"));
			pointsBack.executeMessage(MaxSketchMessageFactory.lighting_enable(0));
			pointsBack.executeMessage(MaxSketchMessageFactory.point_size(pointSizeBack));
			pointsBack.executeMessage(MaxSketchMessageFactory.sketch_cullFace(2));
			pointsBack.executeMessage(MaxSketchMessageFactory.poly_mode(2, 2));
		}
	}

	public void anything(String message, Atom[] args){
		facesFront.call(message, args);
		facesBack.call(message, args);
		linesFront.call(message, args);
		linesBack.call(message, args);
		simpleLines.call(message, args);
		pointsFront.call(message, args);
		pointsBack.call(message, args);
		selPoints.call(message, args);
	}

	public void anything(String message, Atom arg){
		Atom[] args = new Atom[1];
		args[0] = arg;
		anything(message, args);
	}

	private void enableFaces(boolean flag){
		facesFront.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
		facesBack.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
	}

	private void enableLines(boolean flag){
		linesFront.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
		linesBack.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
		simpleLines.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
	}

	private void enablePoints(boolean flag){
		pointsFront.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
		pointsBack.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));

		selPoints.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
	}

	public void editRenderMode(boolean flag){
		if(flag)
			renderMode = editRenderMode;
		else
			renderMode = normalRenderMode;
		updateRenderMode();
	}

	public void setRenderMode(int _renderMode){
		renderMode = _renderMode;
		normalRenderMode = renderMode;
		updateRenderMode();
	}

	public int getRenderMode(){
		return renderMode;
	}

	public void updateRenderMode(){
		switch(renderMode){
		case RENDER_NONE:
			enableFaces(false);
			enableLines(false);
			enablePoints(false);
			break;
		case RENDER_ALL:
			enableFaces(true);
			enableLines(true);
			enablePoints(true);
			break;
		case RENDER_FACES:
			enableFaces(true);
			enableLines(false);
			enablePoints(false);
			break;
		case RENDER_LINES:
			enableFaces(false);
			enableLines(true);
			enablePoints(false);
			break;
		case RENDER_POINTS:
			enableFaces(false);
			enableLines(false);
			enablePoints(true);
			break;
		case RENDER_LINES_POINTS:
			enableFaces(false);
			enableLines(true);
			enablePoints(true);
			break;
		case RENDER_FACES_LINES:
			enableFaces(true);
			enableLines(true);
			enablePoints(false);
			break;
		case RENDER_FACES_POINTS:
			enableFaces(true);
			enableLines(false);
			enablePoints(true);
			break;
		}
	}

	public void setLayer(int _layer){
		facesBack.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer));
		simpleLines.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer));
		linesBack.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer + 1));
		pointsBack.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer + 2));
		facesFront.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer + 3));
		linesFront.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer + 4));
		pointsFront.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer + 5));
		selPoints.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer + 6));
	}

	public void setContext(Atom[] _context){
		facesFront.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
		simpleLines.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
		facesBack.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
		linesFront.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
		linesBack.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
		pointsFront.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
		pointsBack.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
		selPoints.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
	}

	public void position(Atom[] _position){
		facesFront.call("position", _position);
		simpleLines.call("position", _position);
		facesBack.call("position", _position);
		linesFront.call("position", _position);
		linesBack.call("position", _position);
		pointsFront.call("position", _position);
		pointsBack.call("position", _position);
		selPoints.call("position", _position);
	}

	public void quat(Atom[] _quat){
		facesFront.call("quat", _quat);
		simpleLines.call("quat", _quat);
		facesBack.call("quat", _quat);
		linesFront.call("quat", _quat);
		linesBack.call("quat", _quat);
		pointsFront.call("quat", _quat);
		pointsBack.call("quat", _quat);
		selPoints.call("quat", _quat);
	}

	public void rotatexyz(Atom[] _rotatexyz){
		facesFront.call("rotatexyz", _rotatexyz);
		simpleLines.call("rotatexyz", _rotatexyz);
		facesBack.call("rotatexyz", _rotatexyz);
		linesFront.call("rotatexyz", _rotatexyz);
		linesBack.call("rotatexyz", _rotatexyz);
		pointsFront.call("rotatexyz", _rotatexyz);
		pointsBack.call("rotatexyz", _rotatexyz);
		selPoints.call("rotatexyz", _rotatexyz);
	}

	public void scale(Atom[] _scale){
		facesFront.call("scale", _scale);
		simpleLines.call("scale", _scale);
		facesBack.call("scale", _scale);
		linesFront.call("scale", _scale);
		linesBack.call("scale", _scale);
		pointsFront.call("scale", _scale);
		pointsBack.call("scale", _scale);
		selPoints.call("scale", _scale);
	}

	public void modelChange() {
		if(isInitialized){
			updateFacesAndLines();
			updatePointCommands();
			hasModelChanged = true;
		}
	}

	public void textureChange() {
		if(isInitialized){
			updateTextureFacesAndLines();
			updateTexPointCommands();
			hasTextureChanged = true;
		}
	}

	public void selectionChange() {
		if(isInitialized){
			updatePointCommands();
			updateTexPointCommands();
			hasSelectionChanged = true;
		}
	}

	private void updatePointCommands() {
		selSketchPointCommands.clear();

		Vertice v = null;

		for (int f = 0; f < model.getModelVerticesCount(); f++) {
			v = model.getModelVertice(f);

			if(v.isSelected()){
				selSketchPointCommands.add(MaxSketchMessageFactory.sketch_glcolor(selectedPointColor));
				selSketchPointCommands.add(MaxSketchMessageFactory.sketch_glpointSize(selectedPointSize));
				selSketchPointCommands.add(MaxSketchMessageFactory.sketch_point(v.x(), v.y(), v.z()));
			} else if(v.isPicked()){
				selSketchPointCommands.add(MaxSketchMessageFactory.sketch_glcolor(pickedPointColor));
				selSketchPointCommands.add(MaxSketchMessageFactory.sketch_glpointSize(pickedPointSize));
				selSketchPointCommands.add(MaxSketchMessageFactory.sketch_point(v.x(), v.y(), v.z()));
			} else {
				selSketchPointCommands.add(MaxSketchMessageFactory.sketch_glcolor(pointColor));
				selSketchPointCommands.add(MaxSketchMessageFactory.sketch_glpointSize(pointSizeBack));
				selSketchPointCommands.add(MaxSketchMessageFactory.sketch_point(v.x(), v.y(), v.z()));
			}
		}
	}

	private void updateTexPointCommands() {
		sketchTexPointCommands.clear();

		Vertice v = null;

		for (int f = 0; f < model.getTextureVerticeCount(); f++) {
			v = model.getTextureVertice(f);
			if(v.isPicked()){
				sketchTexPointCommands.add(MaxSketchMessageFactory.sketch_glcolor(pickedPointColor));
				sketchTexPointCommands.add(MaxSketchMessageFactory.sketch_glpointSize(pickedPointSize));
				sketchTexPointCommands.add(MaxSketchMessageFactory.sketch_point(v.x()+textureShiftX, v.y()+textureShiftY, v.z()));
			}
			if(v.isSelected()){
				sketchTexPointCommands.add(MaxSketchMessageFactory.sketch_glcolor(selectedPointColor));

			}else{
				sketchTexPointCommands.add(MaxSketchMessageFactory.sketch_glcolor(pointColor));
			}
			sketchTexPointCommands.add(MaxSketchMessageFactory.sketch_glpointSize(pointSizeBack));
			sketchTexPointCommands.add(MaxSketchMessageFactory.sketch_point(v.x()+textureShiftX, v.y()+textureShiftY, v.z()));
		}
	}

	private void updateTextureFacesAndLines() {
		sketchTexLineCommands.clear();
		sketchTexLineCommands.add(MaxSketchMessageFactory.sketch_glcolor(lineColorFront));

		sketchTexFaceCommands.clear();
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_glcolor(faceEditColor));

		generateTexFaceBackground();

		Segment tmpModelSegment;

		// render all triangles
		for (int s = 0; s < model.getSegmentCount(); s++) {
			tmpModelSegment = model.segments.get(s);
			for (int f = 0; f < tmpModelSegment.getFaceCount(); f++) {
				generateTexLineCommands(tmpModelSegment.getFace(f));
				generateTexFaceCommands(tmpModelSegment.getFace(f));
			}
		}
	}

	private void updateFacesAndLines() {
		sketchFaceFrontCommands.clear();
		sketchLineFrontCommands.clear();
		sketchPointFrontCommands.clear();
		sketchFaceBackCommands.clear();
		sketchLineBackCommands.clear();
		sketchPointBackCommands.clear();
		sketchSimpleLineCommands.clear();

		Segment tmpModelSegment;

		int vertexCounter_F = 0;
		int vertexCounter_L = 0;
		// render all triangles
		for (int s = 0; s < model.getSegmentCount(); s++) {
			tmpModelSegment = model.segments.get(s);
			for (int f = 0; f < tmpModelSegment.getFaceCount(); f++) {
				if(tmpModelSegment.getFace(f).getVertexCount() == 2){
					vertexCounter_L += 2;
				} if(tmpModelSegment.getFace(f).getVertexCount() == 3){
					vertexCounter_F += 3;
				} else if (tmpModelSegment.getFace(f).getVertexCount() == 4){
					vertexCounter_F += 6;
				}
			}
		}

		sketchFaceFrontCommands.setDim(new int[]{vertexCounter_F});
		sketchLineFrontCommands.setDim(new int[]{vertexCounter_F});
		sketchPointFrontCommands.setDim(new int[]{vertexCounter_F});
		sketchFaceBackCommands.setDim(new int[]{vertexCounter_F});
		sketchLineBackCommands.setDim(new int[]{vertexCounter_F});
		sketchPointBackCommands.setDim(new int[]{vertexCounter_F});

		sketchSimpleLineCommands.setDim(new int[]{vertexCounter_L});

		// generate all faces
		int faceCounter = 0;
		for (int s = 0; s < model.getSegmentCount(); s++) {
			tmpModelSegment = model.segments.get(s);
			for (int f = 0; f < tmpModelSegment.getFaceCount(); f++) {
				if(tmpModelSegment.getFace(f).getVertexCount() == 3){
					generateFaceCommands(tmpModelSegment.getFace(f), faceCounter * 3, new int[] {0,1,2});
					faceCounter++;
				}	else if(tmpModelSegment.getFace(f).getVertexCount() == 4){
					generateFaceCommands(tmpModelSegment.getFace(f), faceCounter * 3, new int[] {0,1,2});
					faceCounter++;
					generateFaceCommands(tmpModelSegment.getFace(f), faceCounter * 3, new int[] {2,3,0});
					faceCounter++;
				}
			}
		}
		// generate all lines
		int lineCounter = 0;
		for (int s = 0; s < model.getSegmentCount(); s++) {
			tmpModelSegment = model.segments.get(s);
			for (int f = 0; f < tmpModelSegment.getFaceCount(); f++) {
				if(tmpModelSegment.getFace(f).getVertexCount() == 2){
					generateLineCommands(tmpModelSegment.getFace(f), lineCounter * 2, new int[] {0,1});
					lineCounter++; // we carry on counting from the above...
				}
			}
		}
	}

	private void generateLineCommands(Face face, int indx, int[] faceIndexs){
		for(int fp = 0; fp < faceIndexs.length; fp++){
			Vertice texture = new Vertice(0.f, 0.f);
			Vector3f normal = new Vector3f(0.f, 0.f, 0.f);
			Vertice vertex = face.getVertice(faceIndexs[fp]);
			generateLineCommands(indx + fp, vertex, texture, normal);
		}
	}

	private void generateLineCommands(int indx, Vertice vertex, Vertice texture, Vector3f normal){
		sketchSimpleLineCommands.setcell(
				new Atom[]{Atom.newAtom(indx),
						Atom.newAtom("val"),
						Atom.newAtom(vertex.x()), Atom.newAtom(vertex.y()), Atom.newAtom(vertex.z()),
						Atom.newAtom(texture.x()), Atom.newAtom(texture.y()),
						Atom.newAtom(normal.x()), Atom.newAtom(normal.y()), Atom.newAtom(normal.z()),
						Atom.newAtom(lineColorFront[0]), Atom.newAtom(lineColorFront[1]), Atom.newAtom(lineColorFront[2]), Atom.newAtom(lineColorFront[3])});
	}

	private void generateFaceCommands(Face face, int indx, int[] faceIndexs){
		for(int fp = 0; fp < faceIndexs.length; fp++){
			Vertice texture = face.getUvs(faceIndexs[fp]);
			if(texture == null){
				texture = new Vertice(0.f, 0.f);
			}
			Vector3f normal = face.getNormal(faceIndexs[fp]);
			if(normal == null){
				normal = new Vector3f(0.f, 0.f, 0.f);
			}
			Vertice vertex = face.getVertice(faceIndexs[fp]);
			generateFaceCommands(indx + fp, vertex, texture, normal);
		}
	}

	private void generateFaceCommands(int indx, Vertice vertex, Vertice texture, Vector3f normal){
		sketchFaceFrontCommands.setcell(
				new Atom[]{Atom.newAtom(indx),
						Atom.newAtom("val"),
						Atom.newAtom(vertex.x()), Atom.newAtom(vertex.y()), Atom.newAtom(vertex.z()),
						Atom.newAtom(texture.x()), Atom.newAtom(texture.y()),
						Atom.newAtom(normal.x()), Atom.newAtom(normal.y()), Atom.newAtom(normal.z()),
						Atom.newAtom(faceEditColor[0]), Atom.newAtom(faceEditColor[1]), Atom.newAtom(faceEditColor[2]), Atom.newAtom(faceEditColor[3])});
		sketchFaceBackCommands.setcell(
				new Atom[]{Atom.newAtom(indx),
						Atom.newAtom("val"),
						Atom.newAtom(vertex.x()), Atom.newAtom(vertex.y()), Atom.newAtom(vertex.z()),
						Atom.newAtom(texture.x()), Atom.newAtom(texture.y()),
						Atom.newAtom(normal.x()), Atom.newAtom(normal.y()), Atom.newAtom(normal.z()),
						Atom.newAtom(bkgFaceColor[0]), Atom.newAtom(bkgFaceColor[1]), Atom.newAtom(bkgFaceColor[2]), Atom.newAtom(bkgFaceColor[3])});
		sketchLineFrontCommands.setcell(
				new Atom[]{Atom.newAtom(indx),
						Atom.newAtom("val"),
						Atom.newAtom(vertex.x()), Atom.newAtom(vertex.y()), Atom.newAtom(vertex.z()),
						Atom.newAtom(texture.x()), Atom.newAtom(texture.y()),
						Atom.newAtom(normal.x()), Atom.newAtom(normal.y()), Atom.newAtom(normal.z()),
						Atom.newAtom(lineColorFront[0]), Atom.newAtom(lineColorFront[1]), Atom.newAtom(lineColorFront[2]), Atom.newAtom(lineColorFront[3])});
		sketchLineBackCommands.setcell(
				new Atom[]{Atom.newAtom(indx),
						Atom.newAtom("val"),
						Atom.newAtom(vertex.x()), Atom.newAtom(vertex.y()), Atom.newAtom(vertex.z()),
						Atom.newAtom(texture.x()), Atom.newAtom(texture.y()),
						Atom.newAtom(normal.x()), Atom.newAtom(normal.y()), Atom.newAtom(normal.z()),
						Atom.newAtom(lineColorBack[0]), Atom.newAtom(lineColorBack[1]), Atom.newAtom(lineColorBack[2]), Atom.newAtom(lineColorBack[3])});
		sketchPointFrontCommands.setcell(
				new Atom[]{Atom.newAtom(indx),
						Atom.newAtom("val"),
						Atom.newAtom(vertex.x()), Atom.newAtom(vertex.y()), Atom.newAtom(vertex.z()),
						Atom.newAtom(texture.x()), Atom.newAtom(texture.y()),
						Atom.newAtom(normal.x()), Atom.newAtom(normal.y()), Atom.newAtom(normal.z()),
						Atom.newAtom(pointColor[0]), Atom.newAtom(pointColor[1]), Atom.newAtom(pointColor[2]), Atom.newAtom(pointColor[3])});
		sketchPointBackCommands.setcell(
				new Atom[]{Atom.newAtom(indx),
						Atom.newAtom("val"),
						Atom.newAtom(vertex.x()), Atom.newAtom(vertex.y()), Atom.newAtom(vertex.z()),
						Atom.newAtom(texture.x()), Atom.newAtom(texture.y()),
						Atom.newAtom(normal.x()), Atom.newAtom(normal.y()), Atom.newAtom(normal.z()),
						Atom.newAtom(pointColor[0]), Atom.newAtom(pointColor[1]), Atom.newAtom(pointColor[2]), Atom.newAtom(pointColor[3])});
	}

	private void generateTexFaceCommands(Face face){
		if(face._mySubFaces == null){
			if (face.getUVCount() > 0) {
				sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_beginShape());
				for(int fp = 0; fp < face.getUVCount(); fp++){
					Vertice texture = face.getUvs(fp);
					if(texture != null){
						sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_texture(texture.x(), texture.y()));
						sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_vertex(texture.x()+textureShiftX, texture.y()+textureShiftY, 0.f));
					}
				}
				sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_endShape());
			}
		}else{
			for (int i = 0; i < face._mySubFaces._faces.size(); i++) {
				generateTexFaceCommands(face._mySubFaces._faces.get(i));
			}
		}
	}

	private void generateTexLineCommands(Face face){
		if(face._mySubFaces == null){
			if (face.getUVCount() > 0) {
				Vertice vertice = face.getUvs(face.getUVCount()-1);
				sketchTexLineCommands.add(MaxSketchMessageFactory.sketch_moveto(vertice.x()+textureShiftX, vertice.y()+textureShiftY, 0.f));

				for(int fp = 0; fp < face.getUVCount(); fp++){
					Vertice vertex = face.getUvs(fp);
					if(vertex != null){
						sketchTexLineCommands.add(MaxSketchMessageFactory.sketch_lineto(vertex.x()+textureShiftX, vertex.y()+textureShiftY, 0.f));
					}
				}
			}
		}else{
			for (int i = 0; i < face._mySubFaces._faces.size(); i++) {
				generateTexLineCommands(face._mySubFaces._faces.get(i));
			}
		}
	}

	private void generateTexFaceBackground() {

		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_glcolor(bkgFaceColor));
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_beginShape());
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_texture(0, 0));
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_vertex(0+textureShiftX, 0+textureShiftY, 0));
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_texture(1, 0));
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_vertex(1+textureShiftX, 0+textureShiftY, 0));
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_texture(1, 1));
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_vertex(1+textureShiftX, 1+textureShiftY, 0));
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_texture(0, 1));
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_vertex(0+textureShiftX, 1+textureShiftY, 0));
		sketchTexFaceCommands.add(MaxSketchMessageFactory.sketch_endShape());
	}

	public void notifyDeleted()
	{
		facesFront.notifyDeleted();
		facesBack.notifyDeleted();
		simpleLines.notifyDeleted();
		linesFront.notifyDeleted();
		linesBack.notifyDeleted();
		pointsFront.notifyDeleted();
		pointsBack.notifyDeleted();

		selPoints.notifyDeleted();

		sketchFaceFrontCommands.freePeer();
		sketchFaceBackCommands.freePeer();
		sketchLineFrontCommands.freePeer();
		sketchLineBackCommands.freePeer();
		sketchSimpleLineCommands.freePeer();
		sketchPointFrontCommands.freePeer();
		sketchPointBackCommands.freePeer();
	}

}
