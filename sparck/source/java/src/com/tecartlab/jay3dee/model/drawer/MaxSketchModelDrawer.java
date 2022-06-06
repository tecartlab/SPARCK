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

import com.cycling74.jitter.JitterObject;
import com.cycling74.max.Atom;

import com.tecartlab.jay3dee.ModelContainer;
import com.tecartlab.jay3dee.SketchCanvas;
import com.tecartlab.jay3dee.model.Face;
import com.tecartlab.jay3dee.model.ModelData;
import com.tecartlab.jay3dee.model.Segment;
import com.tecartlab.jay3dee.model.Vertice;
import com.tecartlab.tools.math.la.Quaternionf;
import com.tecartlab.tools.math.la.Vector3f;

public class MaxSketchModelDrawer implements ModelDrawer {

	public static final int RENDER_NONE = 0;
	public static final int RENDER_ALL = 1;
	public static final int RENDER_FACES = 2;
	public static final int RENDER_LINES = 3;
	public static final int RENDER_POINTS = 4;
	public static final int RENDER_LINES_POINTS = 5;
	public static final int RENDER_FACES_LINES = 6;
	public static final int RENDER_FACES_POINTS = 7;

	private ModelData model;

	public SketchCanvas faces;
	public SketchCanvas lines;
	public SketchCanvas points;

	public SketchCanvas texFaces;
	public SketchCanvas texLines;
	public SketchCanvas texPoints;

	protected ArrayList<MaxSketchMessage> sketchFaceCommands;
	protected ArrayList<MaxSketchMessage> sketchLineCommands;
	protected ArrayList<MaxSketchMessage> sketchPointCommands;
	protected ArrayList<MaxSketchMessage> sketchTexFaceCommands;
	protected ArrayList<MaxSketchMessage> sketchTexLineCommands;
	protected ArrayList<MaxSketchMessage> sketchTexPointCommands;

	public float[] faceEditColor 		= {1.0f, 1.0f, 1.0f, 0.5f};
	public float[] lineColor 			= {0.5f, 1.0f, 0.5f, 1.0f};
	public float[] pointColor 			= {0.0f, 0.7f, 0.2f, 1.0f};
	public float[] selectedPointColor 	= {1.0f, 0.2f, 0.8f, 1.0f};
	public float[] pickedPointColor 	= {1.0f, 0.0f, 0.0f, 1.0f};
	public float[] bkgFaceColor			= {0.5f, 0.5f, 0.5f, 0.8f};

	private float textureShiftX = -0.5f;
	private float textureShiftY = -0.5f;

	public float pointSize 				= 5.f;
	public float pickedPointSize 		= 7.f;

	private boolean isInitialized = false;
	private boolean hasModelChanged = false;
	private boolean hasTextureChanged = false;
	private boolean hasSelectionChanged = false;

	private int renderMode;
	private int normalRenderMode = RENDER_ALL;
	private int editRenderMode = RENDER_LINES_POINTS;

	public MaxSketchModelDrawer(){
		faces = new SketchCanvas(1);
		lines = new SketchCanvas(2);
		points = new SketchCanvas(3);
		texFaces = null;
		texLines = null;
		texPoints = null;

		sketchFaceCommands = new ArrayList<MaxSketchMessage>();
		sketchLineCommands = new ArrayList<MaxSketchMessage>();
		sketchPointCommands = new ArrayList<MaxSketchMessage>();
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
				if(faces != null){
					faces.executeMessage(MaxSketchMessageFactory.sketch_reset());
					faces.drawGlCommand(sketchFaceCommands);
				}
				if(lines != null){
					lines.executeMessage(MaxSketchMessageFactory.sketch_reset());
					lines.drawGlCommand(sketchLineCommands);
				}
				if(points != null){
					points.executeMessage(MaxSketchMessageFactory.sketch_reset());
					points.drawGlCommand(sketchPointCommands);
				}
			}
			if(hasSelectionChanged){
				if(points != null){
					points.executeMessage(MaxSketchMessageFactory.sketch_reset());
					points.drawGlCommand(sketchPointCommands);
				}
			}
		}
	}

	/**
	 * Draws all
	 */
	public void drawraw() {
		if(isInitialized){
			updateFacesAndLines();
			updatePointCommands();
			updateTextureFacesAndLines();
			updateTexPointCommands();
			if(faces != null){
				faces.executeMessage(MaxSketchMessageFactory.sketch_reset());
				faces.drawGlCommand(sketchFaceCommands);
			}
			if(lines != null){
				lines.executeMessage(MaxSketchMessageFactory.sketch_reset());
				lines.drawGlCommand(sketchLineCommands);
			}
			if(points != null){
				points.executeMessage(MaxSketchMessageFactory.sketch_reset());
				points.drawGlCommand(sketchPointCommands);
			}
		}
	}

	public void anything(String message, Atom[] args){
		faces.call(message, args);
		lines.call(message, args);
		points.call(message, args);
	}

	public void anything(String message, Atom arg){
		Atom[] args = new Atom[1];
		args[0] = arg;
		anything(message, args);
	}

	private void enableFaces(boolean flag){
		faces.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
		faces.executeMessage(MaxSketchMessageFactory.sketch_cullFace(0));
	}

	private void enableLines(boolean flag){
		lines.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
		lines.executeMessage(MaxSketchMessageFactory.sketch_cullFace(1));
	}

	private void enablePoints(boolean flag){
		points.executeMessage(MaxSketchMessageFactory.sketch_enable((flag)?1: 0));
		points.executeMessage(MaxSketchMessageFactory.sketch_cullFace(1));
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
		faces.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer));
		lines.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer+1));
		points.executeMessage(MaxSketchMessageFactory.sketch_layer(_layer+2));
	}

	public void setContext(Atom[] _context){
		faces.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
		lines.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
		points.executeMessage(MaxSketchMessageFactory.sketch_context(_context));
	}

	public void position(Atom[] _position){
		faces.call("position", _position);
		lines.call("position", _position);
		points.call("position", _position);
	}

	public void quat(Atom[] _quat){
		faces.call("quat", _quat);
		lines.call("quat", _quat);
		points.call("quat", _quat);
	}

	public void rotatexyz(Atom[] _rotatexyz){
		faces.call("rotatexyz", _rotatexyz);
		lines.call("rotatexyz", _rotatexyz);
		points.call("rotatexyz", _rotatexyz);
	}

	public void scale(Atom[] _scale){
		faces.call("scale", _scale);
		lines.call("scale", _scale);
		points.call("scale", _scale);
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
		sketchPointCommands.clear();

		Vertice v = null;

		for (int f = 0; f < model.getModelVerticesCount(); f++) {
			v = model.getModelVertice(f);

			if(v.isPicked()){
				sketchPointCommands.add(MaxSketchMessageFactory.sketch_glcolor(pickedPointColor));
				sketchPointCommands.add(MaxSketchMessageFactory.sketch_glpointSize(pickedPointSize));
				sketchPointCommands.add(MaxSketchMessageFactory.sketch_point(v.x(), v.y(), v.z()));
			}

			if(v.isSelected()){
				sketchPointCommands.add(MaxSketchMessageFactory.sketch_glcolor(selectedPointColor));

			}else{
				sketchPointCommands.add(MaxSketchMessageFactory.sketch_glcolor(pointColor));
			}
			sketchPointCommands.add(MaxSketchMessageFactory.sketch_glpointSize(pointSize));
			sketchPointCommands.add(MaxSketchMessageFactory.sketch_point(v.x(), v.y(), v.z()));
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
			sketchTexPointCommands.add(MaxSketchMessageFactory.sketch_glpointSize(pointSize));
			sketchTexPointCommands.add(MaxSketchMessageFactory.sketch_point(v.x()+textureShiftX, v.y()+textureShiftY, v.z()));
		}
	}

	private void updateTextureFacesAndLines() {
		sketchTexLineCommands.clear();
		sketchTexLineCommands.add(MaxSketchMessageFactory.sketch_glcolor(lineColor));

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
		sketchLineCommands.clear();
		sketchLineCommands.add(MaxSketchMessageFactory.sketch_glcolor(lineColor));

		sketchFaceCommands.clear();
		sketchFaceCommands.add(MaxSketchMessageFactory.sketch_glcolor(faceEditColor));

		Segment tmpModelSegment;

		// render all triangles
		for (int s = 0; s < model.getSegmentCount(); s++) {
			tmpModelSegment = model.segments.get(s);
			for (int f = 0; f < tmpModelSegment.getFaceCount(); f++) {
				generateLineCommands(tmpModelSegment.getFace(f));
				generateFaceCommands(tmpModelSegment.getFace(f));
			}
		}
	}

	private void generateLineCommands(Face face){
		if(face._mySubFaces == null){
			if (face.getVertexCount() > 0) {
				Vertice vertice = face.getVertice(face.getVertexCount()-1);
				sketchLineCommands.add(MaxSketchMessageFactory.sketch_moveto(vertice.x(), vertice.y(), vertice.z()));

				for(int fp = 0; fp < face.getVertexCount(); fp++){
					Vertice vertex = face.getVertice(fp);
					if(vertex != null){
						sketchLineCommands.add(MaxSketchMessageFactory.sketch_lineto(vertex.x(), vertex.y(), vertex.z()));
					}
				}
			}
		}else{
			for (int i = 0; i < face._mySubFaces._faces.size(); i++) {
				generateLineCommands(face._mySubFaces._faces.get(i));
			}
		}
	}

	private void generateFaceCommands(Face face){
		if(face._mySubFaces == null){
			if (face.getVertexCount() > 0) {
				sketchFaceCommands.add(MaxSketchMessageFactory.sketch_beginShape()); // specify render mode
				for(int fp = 0; fp < face.getVertexCount(); fp++){
					Vertice texture = face.getUvs(fp);
					if(texture != null){
						sketchFaceCommands.add(MaxSketchMessageFactory.sketch_texture(texture.x(), texture.y()));
					}
					Vector3f normal = face.getNormal(fp);
					if(normal != null){
						sketchFaceCommands.add(MaxSketchMessageFactory.sketch_normal(normal.x(), normal.y(), normal.z()));
					}
					Vertice vertex = face.getVertice(fp);
					if(vertex != null){
						sketchFaceCommands.add(MaxSketchMessageFactory.sketch_vertex(vertex.x(), vertex.y(), vertex.z()));
					}
				}
				sketchFaceCommands.add(MaxSketchMessageFactory.sketch_endShape());
			}
		}else{
			for (int i = 0; i < face._mySubFaces._faces.size(); i++) {
				generateFaceCommands(face._mySubFaces._faces.get(i));
			}
		}
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
		faces.notifyDeleted();
		lines.notifyDeleted();
		points.notifyDeleted();
	}

}
