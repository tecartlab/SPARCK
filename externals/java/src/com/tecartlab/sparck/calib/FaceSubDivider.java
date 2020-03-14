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

import com.tecartlab.jay3dee.model.SubFace;
import com.tecartlab.jay3dee.model.Vertice;
import com.tecartlab.tools.math.la.Vector3f;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public class FaceSubDivider {

	Vec3D vecA;
	Vec3D vecB;
	Vec3D vecC;

	Vec2D texA;
	Vec2D texB;
	Vec2D texC;

	public FaceSubDivider(Vec3D _va, Vec3D _vb, Vec3D _vc, Vec2D _ta, Vec2D _tb, Vec2D _tc){
		vecA = _va;
		vecB = _vb;
		vecC = _vc;
		texA = _ta;
		texB = _tb;
		texC = _tc;
	}

	public ArrayList<FaceSubDivider> subdivide(int _subdiv){
		ArrayList<FaceSubDivider> result = new ArrayList<FaceSubDivider>();
		if(_subdiv > 0){
			createSubFaces(result, _subdiv - 1);
		} else {
			result.add(this);
		}
		return result;
	}

	private void createSubFaces(ArrayList<FaceSubDivider> _result, int _subiv) {
		Vec3D[] vertice = new Vec3D[6];
		vertice[0] = vecA;
		vertice[1] = vecB;
		vertice[2] = vecC;
		vertice[3] = vertice[0].add(vertice[1]).scale(.5f);
		vertice[4] = vertice[1].add(vertice[2]).scale(.5f);
		vertice[5] = vertice[2].add(vertice[0]).scale(.5f);

		Vec2D[] uvs = new Vec2D[6];
		uvs[0] = texA;
		uvs[1] = texB;
		uvs[2] = texC;
		uvs[3] = uvs[0].add(uvs[1]).scale(.5f);
		uvs[4] = uvs[1].add(uvs[2]).scale(.5f);
		uvs[5] = uvs[2].add(uvs[0]).scale(.5f);

		/*
		 * add first subface:
		 */
		FaceSubDivider face = new FaceSubDivider(
				vertice[0], vertice[3], vertice[5],
				uvs[0], uvs[3], uvs[5]);
		_result.addAll(face.subdivide(_subiv));

		/*
		 * add second subface:
		 */
		face = new FaceSubDivider(
				vertice[1], vertice[4], vertice[3],
				uvs[1], uvs[4], uvs[3]);
		_result.addAll(face.subdivide(_subiv));

		/*
		 * add third subface:
		 */
		face = new FaceSubDivider(
				vertice[2], vertice[5], vertice[4],
				uvs[2], uvs[5], uvs[4]);
		_result.addAll(face.subdivide(_subiv));

		/*
		 * add fourth subface:
		 */
		face = new FaceSubDivider(
				vertice[3], vertice[4], vertice[5],
				uvs[3], uvs[4], uvs[5]);
		_result.addAll(face.subdivide(_subiv));
	}

}
