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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.cycling74.jitter.JitterMatrix;
import com.cycling74.jitter.JitterObject;
import com.cycling74.max.Atom;

import com.tecartlab.sparck.OBJWriter;
import com.tecartlab.mxj.utils.JitterObjectArray;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.utils.Debug;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.TriangleMesh;
import toxi.geom.mesh.Vertex;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.subdiv.MidpointSubdivision;
import toxi.geom.mesh2d.Voronoi;

public class DelaunayMesh {

    // radius of the root triangle which encompasses (MUST) all other points
    float DSIZE = 10000;

	ArrayList<DelaunayVertice> vertices;
	Voronoi voronoi;
	WETriangleMesh mesh;

	private int frame;
	private int subdiv;

	public DelaunayMesh(){
		vertices = new ArrayList<DelaunayVertice>();
		voronoi = new Voronoi();
		mesh = new WETriangleMesh();
		frame = 0;
		subdiv = 0;
	}

	public void setFrame(int _frame){
		frame = _frame;
	}

	public void setSubDiv(int _subdiv){
		subdiv = _subdiv;
	}

	public int getFrame(){
		return frame;
	}

	public int getSubDiv(){
		return subdiv;
	}

	public void generateMesh(int _subdiv){
		for(int i = 0; i < vertices.size(); i++){
			voronoi.addPoint(vertices.get(i).voronoi);
		}
	    for (Triangle2D t : voronoi.getTriangles()) {
	    	if(Math.abs(t.a.x)!=DSIZE && Math.abs(t.a.y)!=DSIZE){
		    	DelaunayVertice a = identify(t.a);
		    	DelaunayVertice b = identify(t.b);
		    	DelaunayVertice c = identify(t.c);
		    	//mesh.addFace(a.vertice, b.vertice, c.vertice, new Vec3D(0.f, 0.f, 1.f), a.uv, b.uv, c.uv);
		    	FaceSubDivider subdiv = new FaceSubDivider(a.vertice, b.vertice, c.vertice,  a.uv, b.uv, c.uv);
		    	for(FaceSubDivider f: subdiv.subdivide(_subdiv)){
			    	mesh.addFace(f.vecA, f.vecB, f.vecC, f.texA, f.texB, f.texC);
		    	}
	    	}
	    }
    	//mesh.computeFaceNormals();
	    //saveAsOBJ("/Users/maybites/Desktop/warper.obj");
	}

	/**
	 * Add face commands for geometry jit.gl.matrix
	 * @param _commands
	 */
	public void createSketchCommands(ArrayList<Atom[]> _commands){
		if (mesh.faces.size() > 0) {

	        for (Face f : mesh.faces) {
				_commands.add(GLCommands.sketch_beginPlygonShape()); // specify render mode

				_commands.add(GLCommands.sketch_texture(f.uvA.x, f.uvA.y));
	        	_commands.add(GLCommands.sketch_normal(f.normal.x, f.normal.y, f.normal.z));
	        	_commands.add(GLCommands.sketch_vertex(f.a.x, f.a.y, f.a.z));

	        	_commands.add(GLCommands.sketch_texture(f.uvB.x, f.uvB.y));
	        	_commands.add(GLCommands.sketch_normal(f.normal.x, f.normal.y, f.normal.z));
	        	_commands.add(GLCommands.sketch_vertex(f.b.x, f.b.y, f.b.z));

	        	_commands.add(GLCommands.sketch_texture(f.uvC.x, f.uvC.y));
	        	_commands.add(GLCommands.sketch_normal(f.normal.x, f.normal.y, f.normal.z));
	        	_commands.add(GLCommands.sketch_vertex(f.c.x, f.c.y, f.c.z));

	        	_commands.add(GLCommands.sketch_endShape());
	        }
		}
	}

	/**
	 * Add face commands for jit.gl.sketch model
	 * @param _geometry
	 */
	public void createMatrixGeomCommands(JitterMatrix _geometry){
		if (mesh.faces.size() > 0) {
			_geometry.clear();
			int[] dim = {mesh.faces.size() * 3};
			_geometry.setDim(dim);
//			Debugger.info("matrix creation", "has " + mesh.faces.size() + " faces..");
	        for (int i = 0; i < mesh.faces.size(); i++) {
	        	Face f = mesh.faces.get(i);
	        	_geometry.setcell(GLCommands.matrix_setCell(i * 3 + 0, f.a.x, f.a.y, f.a.z, f.uvA.x, f.uvA.y, f.normal.x, f.normal.y, f.normal.z));
	        	_geometry.setcell(GLCommands.matrix_setCell(i * 3 + 1, f.b.x, f.b.y, f.b.z, f.uvB.x, f.uvB.y, f.normal.x, f.normal.y, f.normal.z));
	        	_geometry.setcell(GLCommands.matrix_setCell(i * 3 + 2, f.c.x, f.c.y, f.c.z, f.uvC.x, f.uvC.y, f.normal.x, f.normal.y, f.normal.z));
	        }
		}
	}

	protected void createLineCommands(ArrayList<Atom[]> _commands){
		if (mesh.faces.size() > 0) {
	        for (Face f : mesh.faces) {
	        	_commands.add(GLCommands.sketch_moveto(f.c.x, f.c.y, f.c.z));
	        	_commands.add(GLCommands.sketch_lineto(f.a.x, f.a.y, f.a.z));
	        	_commands.add(GLCommands.sketch_lineto(f.b.x, f.b.y, f.b.z));
	        	_commands.add(GLCommands.sketch_lineto(f.c.x, f.c.y, f.c.z));
			}
		}
	}

    public void saveAsOBJ(String path) {
        OBJWriter obj = new OBJWriter();
        obj.beginSave(path);
        int vOffset = obj.getCurrVertexOffset() + 1;
        int nOffset = obj.getCurrNormalOffset() + 1;
        int tOffset = obj.getCurrTextureOffset() + 1;
        obj.newObject("warper");
        // vertices
        for (Vertex v : mesh.vertices.values()) {
        	obj.vertex(v);
        }
        // normals
        for (Vertex v : mesh.vertices.values()) {
        	obj.normal(v.normal);
        }
        // textures
        for (Face f : mesh.faces) {
        	obj.texture(f.uvA);
        	obj.texture(f.uvB);
        	obj.texture(f.uvC);
        }
        // faces
        int texNum = 0;
        for (Face f : mesh.faces) {
        	obj.faceWithNormals(
        			f.a.id + vOffset, f.b.id + vOffset, f.c.id + vOffset,
        			texNum++ + tOffset, texNum++ + tOffset, texNum++ + tOffset,
        			f.a.id + nOffset, f.b.id + nOffset, f.c.id + nOffset);
        }
        obj.endSave();
    }

	private DelaunayVertice identify(Vec2D _id){
		for(int i = 0; i < vertices.size(); i++){
			if(vertices.get(i).voronoi.x == _id.x &&
					vertices.get(i).voronoi.y == _id.y)
				return vertices.get(i);
		}
		return null;
	}

	public void draw(JitterObject _drawable){
		for(int i = 0; i < mesh.faces.size(); i++){
			_drawable.call("linesegment",new float[]{
					mesh.faces.get(i).a.x,
					mesh.faces.get(i).a.y,
					mesh.faces.get(i).a.z,
					mesh.faces.get(i).b.x,
					mesh.faces.get(i).b.y,
					mesh.faces.get(i).b.z});
			_drawable.call("linesegment",new float[]{
					mesh.faces.get(i).b.x,
					mesh.faces.get(i).b.y,
					mesh.faces.get(i).b.z,
					mesh.faces.get(i).c.x,
					mesh.faces.get(i).c.y,
					mesh.faces.get(i).c.z});
			_drawable.call("linesegment",new float[]{
					mesh.faces.get(i).c.x,
					mesh.faces.get(i).c.y,
					mesh.faces.get(i).c.z,
					mesh.faces.get(i).a.x,
					mesh.faces.get(i).a.y,
					mesh.faces.get(i).a.z});
		}
	}

	public void clear(){
		vertices.clear();
		voronoi = new Voronoi();
		mesh.clear();
	}

	public void addCalibVertices(ArrayList<CalibVertice> _vertices){
		for(int i = 0; i < _vertices.size(); i++){
			if(_vertices.get(i).isReady()){
				if(_vertices.get(i).isWarpType())
					vertices.add(new DelaunayVertice(_vertices.get(i).getWarpVerticeOrthoNormal(), _vertices.get(i).getModelScreenVertice()));
				else if (_vertices.get(i).isWarpOnlyType())
					vertices.add(new DelaunayVertice(_vertices.get(i).getWarpVerticeOrthoNormal(), _vertices.get(i).getWarpVertice()));
			}
		}
	}

	public void addLineDelaunays(ArrayList<DelaunayVertice> _vertices){
		for(int i = 0; i < _vertices.size(); i++){
			vertices.add(_vertices.get(i));
		}
	}

	public void createFrame(){
		if(frame > 0){
			CalibVertice topLeft = new CalibVertice(new Vector3f(), new Vector3f(), false, true);
			topLeft.setModelScreenVertice(new Vector3f());
			topLeft.setWarpVertice(new Vector3f(0.f, 1.0f, 0.f));
			topLeft.setTargetVertice(new Vector3f(0.f, 1.0f, 0.f));
			topLeft.setIndex(-100000);

			CalibVertice topRight = new CalibVertice(new Vector3f(), new Vector3f(), false, true);
			topRight.setModelScreenVertice(new Vector3f());
			topRight.setWarpVertice(new Vector3f(1.f, 1.0f, 0.f));
			topRight.setTargetVertice(new Vector3f(1.f, 1.0f, 0.f));
			topRight.setIndex(-100001);

			CalibVertice bottomRight = new CalibVertice(new Vector3f(), new Vector3f(), false, true);
			bottomRight.setModelScreenVertice(new Vector3f());
			bottomRight.setWarpVertice(new Vector3f(1.f, 0.0f, 0.f));
			bottomRight.setTargetVertice(new Vector3f(1.f, 0.0f, 0.f));
			bottomRight.setIndex(-100002);

			CalibVertice bottomLeft = new CalibVertice(new Vector3f(), new Vector3f(), false, true);
			bottomLeft.setModelScreenVertice(new Vector3f());
			bottomLeft.setWarpVertice(new Vector3f(0.f, 0.0f, 0.f));
			bottomLeft.setTargetVertice(new Vector3f(0.f, 0.0f, 0.f));
			bottomLeft.setIndex(-100003);

			CalibLine lineTop = new CalibLine(topLeft, topRight, frame - 1);
			CalibLine lineBottom = new CalibLine(bottomLeft, bottomRight, frame - 1);
			CalibLine lineLeft = new CalibLine(topLeft, bottomLeft, frame - 1);
			CalibLine lineRight = new CalibLine(topRight, bottomRight, frame - 1);

			addLineDelaunays(lineTop.delaunays);
			addLineDelaunays(lineBottom.delaunays);
			addLineDelaunays(lineLeft.delaunays);
			addLineDelaunays(lineRight.delaunays);

			vertices.add(new DelaunayVertice(topLeft.getWarpVerticeOrthoNormal(), topLeft.getWarpVertice()));
			vertices.add(new DelaunayVertice(topRight.getWarpVerticeOrthoNormal(), topRight.getWarpVertice()));
			vertices.add(new DelaunayVertice(bottomLeft.getWarpVerticeOrthoNormal(), bottomLeft.getWarpVertice()));
			vertices.add(new DelaunayVertice(bottomRight.getWarpVerticeOrthoNormal(), bottomRight.getWarpVertice()));
		}
	}

}
