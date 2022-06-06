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


package com.tecartlab.sparck.faceAR;

import com.cycling74.jitter.JitterMatrix;
import com.cycling74.max.Atom;
import com.tecartlab.jay3dee.model.Face;
import com.tecartlab.jay3dee.model.Segment;
import com.tecartlab.jay3dee.model.Vertice;
import com.tecartlab.sparck.faceAR.*;
import com.tecartlab.tools.math.la.Vector3f;

public class MaxMeshGenerator {

	private ModelData model;

	protected JitterMatrix original;

	protected JitterMatrix target;

	public float[] faceEditColor 		= {1.0f, 1.0f, 1.0f, 1.0f};

	private boolean isInitialized = false;

	public MaxMeshGenerator(){

		original = new JitterMatrix(12, "float32", 10);
		target = new JitterMatrix(12, "float32", 10);
	}

	public void initialize(ModelData _model){
		model = _model;
		isInitialized = true;
	}

	public void reset(){
		isInitialized = false;
	}

	public void draw() {
		if(isInitialized){
		}
	}

	public JitterMatrix generateMatrix() {
		original.clear();

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
					vertexCounter_F += 4;
				}
			}
		}

		original.setDim(new int[]{vertexCounter_F});

		// generate all faces
		int faceCounter = 0;
		for (int s = 0; s < model.getSegmentCount(); s++) {
			tmpModelSegment = model.segments.get(s);
			for (int f = 0; f < tmpModelSegment.getFaceCount(); f++) {
				if(tmpModelSegment.getFace(f).getVertexCount() == 3){
					generateFaceCommands(tmpModelSegment.getFace(f), faceCounter * 3, new int[] {0,1,2});
					faceCounter++;
				}	else if(tmpModelSegment.getFace(f).getVertexCount() == 4){
					generateFaceCommands(tmpModelSegment.getFace(f), faceCounter * 4, new int[] {0,1,2,3});
					faceCounter++;
				}
			}
		}
		return original;
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
		original.setcell(
				new Atom[]{Atom.newAtom(indx),
						Atom.newAtom("val"),
						Atom.newAtom(vertex.x()), Atom.newAtom(vertex.y()), Atom.newAtom(vertex.z()),
						Atom.newAtom(texture.x()), Atom.newAtom(texture.y()),
						Atom.newAtom(normal.x()), Atom.newAtom(normal.y()), Atom.newAtom(normal.z()),
						Atom.newAtom(faceEditColor[0]), Atom.newAtom(faceEditColor[1]), Atom.newAtom(faceEditColor[2]), Atom.newAtom(faceEditColor[3])});
	}


	public void notifyDeleted()
	{
		original.freePeer();
		target.freePeer();
	}
}
