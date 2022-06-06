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
import java.util.Arrays;
import java.util.Collections;
import java.nio.*;

public class Segment {
	public ArrayList<Face> faces;

	public IntBuffer indexIB;
	public FloatBuffer dataFB;

	/**
	 * Constructor for the ModelSegment, each Segment holds a Vector of
	 * Elements. each element is a collection of indexes to the vert, normal,
	 * and UV arrays that make up a single face.
	 */
	public Segment() {
		faces = new ArrayList<Face>();
	}

	/**
	 * Needs to be called after any change of the geometry. It refreshes the Subfaces
	 */
	public void refreshGeometry(){
		for (int f = 0; f < getFaceCount(); f++) {
			getFace(f).refreshSubFaces();
		}
	}

	public Face getFace(int index) {
		return faces.get(index);
	}

	public Face[] getFaces() {
		return faces.toArray(new Face[faces.size()]);
	}

	public Vertice[] getIndices() {
		ArrayList<Vertice> indices = new ArrayList<Vertice>();

		for (int i = 0; i < faces.size(); i++)
			indices.addAll(Arrays.asList(getFace(i).getVertices()));

		return indices.toArray(new Vertice[indices.size()]);
	}


	public int getFaceCount() {
		return faces.size();
	}

	public int getIndexCount() {
		int count = 0;

		for (int i = 0; i < getFaceCount(); i++)
			count += (getFace(i)).getVertexIndexCount();

		return count;
	}

	public void sortFacesByX() {
		Collections.sort(faces, Face.FaceXComparator);
	}

	public void sortFacesByY() {
		Collections.sort(faces, Face.FaceYComparator);
	}

	public void sortFacesByZ() {
		Collections.sort(faces, Face.FaceZComparator);
	}

	public Segment clone(){
		Segment clone = new Segment();
		for(int i = 0; i < getFaceCount(); i++){
			clone.faces.add(getFace(i).clone());
		}
		clone.dataFB = this.dataFB;
		clone.indexIB = this.indexIB;
		return clone;
	}

}
