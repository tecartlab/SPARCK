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

package com.tecartlab.jay3dee.model;

import java.io.File;

import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.Vertex;
import toxi.geom.mesh.WETriangleMesh;
import com.tecartlab.sparck.OBJWriter;
import com.tecartlab.utils.Debug;

import com.cycling74.jitter.JitterObject;
import com.cycling74.max.Atom;

/**
 * This class is a wrapper for a couple of steps to be performed when
 * the need arrises to create a fast rendering object inside jitter.
 * It allows to draw 3d models with some custom methods, saves them to
 * a temporary file and loads the file into jit.gl.model wrapper to have
 * it displayed.
 *
 * @author maybites
 *
 */
public class Fast3DModelling {

	JitterObject myfastModel;

	WETriangleMesh myMesh;

	private Thread myPostPoner = null;
	String postponedFilePath = null;
	boolean postponeProlong = false;

	public Fast3DModelling(String _context){
		myfastModel = new JitterObject("jit.gl.model", _context);
		myMesh = new WETriangleMesh();
	}

	/**
	 * clear the mesh from its drawing commands
	 */
	public void clear(){
		myMesh.clear();
		myMesh.clear();
	}

    private void saveAsOBJ(String path) {
        OBJWriter obj = new OBJWriter();
        obj.beginSave(path);
        int vOffset = obj.getCurrVertexOffset() + 1;
        int nOffset = obj.getCurrNormalOffset() + 1;
        int tOffset = obj.getCurrTextureOffset() + 1;
        obj.newObject("warper");
        // vertices
        for (Vertex v : myMesh.vertices.values()) {
        	obj.vertex(v);
        }
        // normals
//        for (Vertex v : myMesh.vertices.values()) {
//        	obj.normal(v.normal);
//        }
        // textures
//        for (Face f : myMesh.faces) {
//        	obj.texture(f.uvA);
//        	obj.texture(f.uvB);
//        	obj.texture(f.uvC);
//        }
        // faces
        obj.faceList();

        int texNum = 0;
        for (Face f : myMesh.faces) {
        	obj.face(f.a.id + vOffset, f.b.id + vOffset, f.c.id + vOffset);
        }
        obj.endSave();
    }

	public void addOctahedron(Vec3D _pos, float _radius){
		Vec3D pA = _pos.add(_radius, -_radius, 0);
		Vec3D pB = _pos.add(-_radius, -_radius, 0);
		Vec3D pC = _pos.add(-_radius, _radius, 0);
		Vec3D pD = _pos.add(_radius, _radius, 0);

		Vec3D pTo = _pos.add(0, 0, _radius);
		Vec3D pBo = _pos.add(0, 0, -_radius);

		myMesh.addFace(pA, pB, pTo);
		myMesh.addFace(pB, pC, pTo);
		myMesh.addFace(pC, pD, pTo);
		myMesh.addFace(pD, pA, pTo);

		myMesh.addFace(pB, pA, pBo);
		myMesh.addFace(pC, pB, pBo);
		myMesh.addFace(pD, pC, pBo);
		myMesh.addFace(pA, pD, pBo);
	}

	/**
	 * After the adding of Meshes/Faces is done, the object needs to saved and
	 * reloaded for the JitterObject
	 */
	public void update(String _tmpFilePath){
		postponeLoadingModelFile(_tmpFilePath);
	}

	public void enable(int _enable){
		if(postponedFilePath == null)
			myfastModel.setAttr("enable", _enable);
	}

	public void drawto(Atom[] _context){
		myfastModel.setAttr("drawto", _context);
	}

	public void texture(Atom[] _args){
		myfastModel.setAttr("texture", _args);
	}

	public void shader(String _args){
		myfastModel.setAttr("shader", _args);
	}

	public void shader(){
		myfastModel.call("shader");
	}

	public void setAttr(String _attribute, String _arg1){
		myfastModel.setAttr(_attribute, _arg1);
	}

	public void setAttr(String _attribute, int _arg1){
		myfastModel.setAttr(_attribute, _arg1);
	}

	public void setAttr(String _attribute, float _arg1){
		myfastModel.setAttr(_attribute, _arg1);
	}

	public void setAttr(String _attribute, Atom[] _arg1){
		myfastModel.setAttr(_attribute, _arg1);
	}

	public void notifyDeleted(){
		myfastModel.freePeer();
	}

	private void postponeLoadingModelFile(String _pusherID){
		postponedFilePath = _pusherID;
		if(myPostPoner == null){
			try{
				postponeProlong = false;
				myPostPoner = new Thread(new PostPoner());
				myPostPoner.start();
				myPostPoner.setPriority(Thread.MIN_PRIORITY);
			} catch(Exception e){
				e.printStackTrace(System.out);
			}
		} else {
			// if the thread is still running, the waiting loop should be repeated.
			postponeProlong = true;
		}
	}

	protected class PostPoner implements Runnable{

		public void run(){
			Thread thisThread = Thread.currentThread();

			while(postponedFilePath != null){
				try {
					thisThread.sleep(50);
				} catch (InterruptedException e){;}

				if(!postponeProlong){
					// check if mesh has faces to be stored
					if(myMesh.getFaces().size() > 0)
						saveAsOBJ(postponedFilePath);
					else{
						//if not, then postponeProlong and make sure the threat gets an exit.
						postponeProlong = true;
						postponedFilePath = null;
					}
				}

				try {
					thisThread.sleep(200);
				} catch (InterruptedException e){;}

				if(!postponeProlong){
					myfastModel.setAttr("enable", 1);
					myfastModel.setAttr("file", postponedFilePath);
					postponedFilePath = null;
				} else {
					postponeProlong = false;
				}

				try {
					thisThread.sleep(200);
				} catch (InterruptedException e){;}
			}
			myPostPoner = null;
		}
	}

}
