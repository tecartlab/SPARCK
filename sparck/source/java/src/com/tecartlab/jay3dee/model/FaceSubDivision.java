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

import java.util.ArrayList;

import com.cycling74.max.Atom;

import com.tecartlab.tools.math.la.*;

public class FaceSubDivision {

	public ArrayList<SubFace> _faces;
	public Face _myFace;

	public int _subDivision;

	public FaceSubDivision(Face myFace, int subDivision) {
		_myFace = myFace;
		_faces = new ArrayList<SubFace>();
		_subDivision = subDivision - 1;
	}

	public void refreshSubFaces() {
		//System.out.println("create subfaces...");
		if (_myFace.getVertexCount() == 4) {
			Vertice[] vertice = new Vertice[9];
			vertice[0] = _myFace.getVertice(0);
			vertice[2] = _myFace.getVertice(1);
			vertice[8] = _myFace.getVertice(2);
			vertice[6] = _myFace.getVertice(3);
			vertice[1] = (Vertice) vertice[0].addMake(vertice[2]).scale(.5f);
			vertice[3] = (Vertice) vertice[0].addMake(vertice[6]).scale(.5f);
			vertice[5] = (Vertice) vertice[2].addMake(vertice[8]).scale(.5f);
			vertice[7] = (Vertice) vertice[8].addMake(vertice[6]).scale(.5f);
			vertice[4] = (Vertice) vertice[1].addMake(vertice[7]).scale(.5f);

			Vector3f[] normals = new Vector3f[9];
			if (_myFace.getNormalCount() > 0) {
				normals[0] = _myFace.getNormal(0);
				normals[2] = _myFace.getNormal(1);
				normals[8] = _myFace.getNormal(2);
				normals[6] = _myFace.getNormal(3);
				normals[1] = normals[0].addMake(normals[2]).divide(2f);
				normals[3] = normals[0].addMake(normals[6]).divide(2f);
				normals[5] = normals[2].addMake(normals[8]).divide(2f);
				normals[7] = normals[8].addMake(normals[6]).divide(2f);
				normals[4] = normals[1].addMake(normals[7]).divide(2f);
			}

			Vertice[] uvs = new Vertice[9];
			if (_myFace.getUVCount() > 0) {
				uvs[0] = _myFace.getUvs(0);
				uvs[2] = _myFace.getUvs(1);
				uvs[8] = _myFace.getUvs(2);
				uvs[6] = _myFace.getUvs(3);
				uvs[1] = (Vertice) uvs[0].addMake(uvs[2]).divide(2f);
				uvs[3] = (Vertice) uvs[0].addMake(uvs[6]).divide(2f);
				uvs[5] = (Vertice) uvs[2].addMake(uvs[8]).divide(2f);
				uvs[7] = (Vertice) uvs[8].addMake(uvs[6]).divide(2f);
				uvs[4] = (Vertice) uvs[1].addMake(uvs[7]).divide(2f);
			}

			_faces.clear();

			/*
			 * add first subface:
			 */
			SubFace face = new SubFace(_myFace);
			face.vertices.add(vertice[0]);
			face.vertices.add(vertice[1]);
			face.vertices.add(vertice[4]);
			face.vertices.add(vertice[3]);

			if (_myFace.getUVCount() > 0) {
				face.uvs.add(uvs[0]);
				face.uvs.add(uvs[1]);
				face.uvs.add(uvs[4]);
				face.uvs.add(uvs[3]);
			}

			if (_myFace.getNormalCount() > 0) {
				face.normals.add(normals[0]);
				face.normals.add(normals[1]);
				face.normals.add(normals[4]);
				face.normals.add(normals[3]);
			}

			_faces.add(face);

			/*
			 * add second subface:
			 */
			face = new SubFace(_myFace);
			face.vertices.add(vertice[1]);
			face.vertices.add(vertice[2]);
			face.vertices.add(vertice[5]);
			face.vertices.add(vertice[4]);

			if (_myFace.getUVCount() > 0) {
				face.uvs.add(uvs[1]);
				face.uvs.add(uvs[2]);
				face.uvs.add(uvs[5]);
				face.uvs.add(uvs[4]);
			}

			if (_myFace.getNormalCount() > 0) {
				face.normals.add(normals[1]);
				face.normals.add(normals[2]);
				face.normals.add(normals[5]);
				face.normals.add(normals[4]);
			}

			_faces.add(face);

			/*
			 * add third subface:
			 */
			face = new SubFace(_myFace);
			face.vertices.add(vertice[3]);
			face.vertices.add(vertice[4]);
			face.vertices.add(vertice[7]);
			face.vertices.add(vertice[6]);

			if (_myFace.getUVCount() > 0) {
				face.uvs.add(uvs[3]);
				face.uvs.add(uvs[4]);
				face.uvs.add(uvs[7]);
				face.uvs.add(uvs[6]);
			}

			if (_myFace.getNormalCount() > 0) {
				face.normals.add(normals[3]);
				face.normals.add(normals[4]);
				face.normals.add(normals[7]);
				face.normals.add(normals[6]);
			}

			_faces.add(face);

			/*
			 * add fourth subface:
			 */
			face = new SubFace(_myFace);
			face.vertices.add(vertice[4]);
			face.vertices.add(vertice[5]);
			face.vertices.add(vertice[8]);
			face.vertices.add(vertice[7]);

			if (_myFace.getUVCount() > 0) {
				face.uvs.add(uvs[4]);
				face.uvs.add(uvs[5]);
				face.uvs.add(uvs[8]);
				face.uvs.add(uvs[7]);
			}

			if (_myFace.getNormalCount() > 0) {
				face.normals.add(normals[4]);
				face.normals.add(normals[5]);
				face.normals.add(normals[8]);
				face.normals.add(normals[7]);
			}

			_faces.add(face);

			/*
			 * refresh the faceCommands
			 */

			for (int i = 0; i < _faces.size(); i++) {
				_faces.get(i).makeSubFaces(_subDivision);
				_faces.get(i).refreshSubFaces();
			}

		}
		// threee vertexes
		if (_myFace.getVertexCount() == 3) {
			Vertice[] vertice = new Vertice[6];
			vertice[0] = _myFace.getVertice(0);
			vertice[1] = _myFace.getVertice(1);
			vertice[2] = _myFace.getVertice(2);
			vertice[3] = (Vertice) vertice[0].addMake(vertice[1]).scale(.5f);
			vertice[4] = (Vertice) vertice[1].addMake(vertice[2]).scale(.5f);
			vertice[5] = (Vertice) vertice[2].addMake(vertice[0]).scale(.5f);

			Vector3f[] normals = new Vector3f[9];
			if (_myFace.getNormalCount() > 0) {
				normals[0] = _myFace.getNormal(0);
				normals[1] = _myFace.getNormal(1);
				normals[2] = _myFace.getNormal(2);
				normals[3] = normals[0].addMake(normals[1]).divide(2f);
				normals[4] = normals[1].addMake(normals[2]).divide(2f);
				normals[5] = normals[2].addMake(normals[0]).divide(2f);
			}

			Vertice[] uvs = new Vertice[9];
			if (_myFace.getUVCount() > 0) {
				uvs[0] = _myFace.getUvs(0);
				uvs[1] = _myFace.getUvs(1);
				uvs[2] = _myFace.getUvs(2);
				uvs[3] = (Vertice) uvs[0].addMake(uvs[1]).divide(2f);
				uvs[4] = (Vertice) uvs[1].addMake(uvs[2]).divide(2f);
				uvs[5] = (Vertice) uvs[2].addMake(uvs[0]).divide(2f);
			}

			_faces.clear();

			/*
			 * add first subface:
			 */
			SubFace face = new SubFace(_myFace);
			face.vertices.add(vertice[0]);
			face.vertices.add(vertice[3]);
			face.vertices.add(vertice[5]);

			if (_myFace.getUVCount() > 0) {
				face.uvs.add(uvs[0]);
				face.uvs.add(uvs[3]);
				face.uvs.add(uvs[5]);
			}

			if (_myFace.getNormalCount() > 0) {
				face.normals.add(normals[0]);
				face.normals.add(normals[3]);
				face.normals.add(normals[5]);
			}

			_faces.add(face);

			/*
			 * add second subface:
			 */
			face = new SubFace(_myFace);
			face.vertices.add(vertice[1]);
			face.vertices.add(vertice[4]);
			face.vertices.add(vertice[3]);

			if (_myFace.getUVCount() > 0) {
				face.uvs.add(uvs[1]);
				face.uvs.add(uvs[4]);
				face.uvs.add(uvs[3]);
			}

			if (_myFace.getNormalCount() > 0) {
				face.normals.add(normals[1]);
				face.normals.add(normals[4]);
				face.normals.add(normals[3]);
			}

			_faces.add(face);

			/*
			 * add third subface:
			 */
			face = new SubFace(_myFace);
			face.vertices.add(vertice[2]);
			face.vertices.add(vertice[5]);
			face.vertices.add(vertice[4]);

			if (_myFace.getUVCount() > 0) {
				face.uvs.add(uvs[2]);
				face.uvs.add(uvs[5]);
				face.uvs.add(uvs[4]);
			}

			if (_myFace.getNormalCount() > 0) {
				face.normals.add(normals[2]);
				face.normals.add(normals[5]);
				face.normals.add(normals[4]);
			}

			_faces.add(face);

			/*
			 * add fourth subface:
			 */
			face = new SubFace(_myFace);
			face.vertices.add(vertice[3]);
			face.vertices.add(vertice[4]);
			face.vertices.add(vertice[5]);

			if (_myFace.getUVCount() > 0) {
				face.uvs.add(uvs[3]);
				face.uvs.add(uvs[4]);
				face.uvs.add(uvs[5]);
			}

			if (_myFace.getNormalCount() > 0) {
				face.normals.add(normals[3]);
				face.normals.add(normals[4]);
				face.normals.add(normals[5]);
			}

			_faces.add(face);

			/*
			 * refresh the faceCommands
			 */

			for (int i = 0; i < _faces.size(); i++) {
				_faces.get(i).makeSubFaces(_subDivision);
				_faces.get(i).refreshSubFaces();
			}

		}
	}

}
