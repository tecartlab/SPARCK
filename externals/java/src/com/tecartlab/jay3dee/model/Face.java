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
import java.util.Comparator;

import com.tecartlab.tools.math.la.*;

import com.cycling74.max.*;

/**
 * @author tatsuyas
 * @author mattDitton
 * @author Ekene Ijeoma
 *
 *         Each model element contains the indexes to the vertices, normals and
 *         UV's needed to make a face
 */

public class Face implements Constants, Comparable {

	public int indexType = POLYGON;

	// These Indices point to the model data
	public ArrayList<Integer> vertexIndices;
	public ArrayList<Integer> uvIndices;
	public ArrayList<Integer> normalIndices;

	// These are local references to the model data
	public ArrayList<Vertice> vertices;
	public ArrayList<Vector3f> normals;
	public ArrayList<Vertice> uvs;

	private int flagForDeletionCounter = 0;

	public FaceSubDivision _mySubFaces;

	private float textureShiftX = -0.5f;
	private float textureShiftY = -0.5f;

	/**
	 * Constructor for the Face. The Face class contains an a collection of arrays for the vert, normal and uv indexes.
	 * For convenience there are Vector3f Arrays that hold the verts, normals and uv's of the face.
	 * These arrays are only references back to the main Arrays that live in OBJModel.
	 */
	public Face() {
		vertexIndices = new ArrayList<Integer>();
		uvIndices = new ArrayList<Integer>();
		normalIndices = new ArrayList<Integer>();

		vertices = new ArrayList<Vertice>();
		normals = new ArrayList<Vector3f>();
		uvs = new ArrayList<Vertice>();
	}

	public void makeSubFaces(int subDivision){
		if(subDivision > 0){
			_mySubFaces = new FaceSubDivision(this, subDivision);
		}else{
			_mySubFaces = null;
		}
	}

	/**
	 * Before this method is call it is recommended to update the vertices, normals and texture
	 * coordinates.
	 */
	public void refreshSubFaces(){
		if(_mySubFaces != null){
			_mySubFaces.refreshSubFaces();
		}
	}

	public Face clone(){
		Face clone = new Face();
		for(int i = 0; i < vertexIndices.size(); i++){
			clone.vertexIndices.add(vertexIndices.get(i));
		}
		for(int i = 0; i < uvIndices.size(); i++){
			clone.uvIndices.add(uvIndices.get(i));
		}
		for(int i = 0; i < normalIndices.size(); i++){
			clone.normalIndices.add(normalIndices.get(i));
		}
		clone._mySubFaces = _mySubFaces;
		return clone;
	}

	public int getVertexIndexCount() {
		return vertexIndices.size();
	}

	public int getTextureIndexCount() {
		return uvIndices.size();
	}

	public int getNormalIndexCount() {
		return normalIndices.size();
	}

	public int getVertexCount() {
		return vertices.size();
	}

	public int getNormalCount() {
		return normals.size();
	}

	public int getUVCount() {
		return uvs.size();
	}

	public int[] getVertexIndices() {
		int[] v = new int[getVertexIndexCount()];

		for (int i = 0; i < v.length; i++)
			v[i] = getVertexIndex(i);

		return v;
	}

	public int[] getNormalIndices() {
		int[] v = new int[getNormalIndexCount()];

		for (int i = 0; i < v.length; i++)
			v[i] = getNormalIndex(i);

		return v;
	}

	public int[] getTextureIndices() {

		int[] v = new int[getTextureIndexCount()];

		for (int i = 0; i < v.length; i++)
			v[i] = getTextureIndex(i);

		return v;
	}

	/**
	 * Returns an array of Vector3fs that make up this face
	 * @return Vector3f []
	 */

	public Vertice[] getVertices() {
		return vertices.toArray(new Vertice[vertices.size()]);
	}

	public Vertice getVertice(int i) {
		try{
			return vertices.get(i);
		}catch(IndexOutOfBoundsException e){
			;
		}
		return null;
	}

	/**
	 * Returns an array of normals that make up this face
	 * @return Vector3f []
	 */
	public Vector3f[] getNormals() {
		return normals.toArray(new Vector3f[normals.size()]);
	}

	public Vector3f getNormal(int i) {
		try{
			return normals.get(i);
		}catch(IndexOutOfBoundsException e){
			;
		}
		return null;
	}

	/**
	 * Checks if there can be a UV vertice with this index
	 * @param i
	 * @return
	 */
	public boolean hasUVs(int i){
		return (uvs.size() > i)? true: false;
	}

	/**
	 * Returns an array of uvs that make up this face
	 * @return Vector3f []
	 */
	public Vertice[] getUvs() {
		return uvs.toArray(new Vertice[uvs.size()]);
	}

	public Vertice getUvs(int i) {
		try{
			return uvs.get(i);
		}catch(IndexOutOfBoundsException e){
			;
		}
		return null;
	}

	/**
	 * Get's the center position of the face.
	 * @return Vector3f []
	 */
	public Vertice getCenter() {
		Vertice c = new Vertice();

		for (int i = 0; i < vertices.size(); i++)
			c.setAdd(c, vertices.get(i));

		c.divide(vertices.size());

		return c;
	}

	/**
	 * Returns the face normal. The face normal is calculated from the face center using the cross product of the first and last vert.
	 * An alternate method would be to get the average of all vert normals. But that one you can do yourself, because in certain situations it's not reliable..
	 * @return a normalized Vector3f
	 */

	public Vector3f getNormal() {
		// center vertex
		Vector3f c = getCenter();

		// center - first vertex
		Vector3f aToB = new Vector3f();
		aToB.setSub(c, vertices.get(0));
		// center - last vertex
		Vector3f cToB = new Vector3f();
		cToB.setSub(c, vertices.get(vertices.size() - 1));
		Vector3f n = cToB.crossMake(aToB);

		n.normalize();

		return n;
	}

	// Arrays start at 0 (hence the -1) But OBJ files start the
	// indices at 1.
	public int getVertexIndex(int i) {
		return vertexIndices.get(i) - 1;
	}

	public int getTextureIndex(int i) {
		return uvIndices.get(i) - 1;
	}

	public int getNormalIndex(int i) {
		return normalIndices.get(i) - 1;
	}

	/**
	 * Used for knowing if a face is pointing in the direction of the supplied Vector3f.
	 * In a dense mesh it can be faster to check to see if the face should be draw before drawing it.
	 * Also it can look cool.
	 *
	 * @param position
	 * @return True if the angle made between the face normal and position from the face center is less than 90.
	 */


	public boolean isFacingPosition(Vector3f position) {
		Vertice c = getCenter();

		// this works out the vector from the camera to the face.
		Vector3f positionToFace = new Vector3f(position.x() - c.x(), position.y() - c.y(), position.z() - c.z());

		// We now know the vector from the camera to the face,
		// and the vector that describes which direction the face
		// is pointing, so we just need to do a dot-product and
		// based on that we can tell if it's facing the camera or not
		// float result = Vector3f.dot(cameraToFace, faceNormal);
		float result = positionToFace.dot(getNormal());

		// if the result is positive, then it is facing the camera.
		return result < 0;
	}

	/**
	 * Returns a float value from 0 - 1. With 1 occurring if the position is in direct line with the face normal.
	 * Likewise 0 is facing completely away from the face normal. And you guessed it. A value of 0.5 comes is from perpendicular faces.
	 * @param position
	 * @return
	 */

	public float getFacingAmount(Vector3f position) {
		Vector3f c = getCenter();

		// this works out the vector from the camera to the face.
		Vector3f positionToFace = new Vector3f(position.x() - c.x(), position.y() - c.y(), position.z() - c.z());

		c.normalize();

		positionToFace.normalize();

		return (1.0f - (positionToFace.dot(getNormal()) + 1.0f) / 2.0f);
	}

	public int compareTo(Object f2) throws ClassCastException {
		if (!(f2 instanceof Face))
			throw new ClassCastException("Face object expected.");

		Vector3f f1Center = getCenter();
		Vector3f f2Center = ((Face) f2).getCenter();

		return (int) (f1Center.x() - f2Center.x());
	}

	public static Comparator<Face> FaceXComparator = new Comparator<Face>() {
		public int compare(Face f1, Face f2) {
			Vector3f f1Center = f1.getCenter();
			Vector3f f2Center = f2.getCenter();

			return (int) (f1Center.x() - f2Center.x());
		}
	};

	public static Comparator<Face> FaceYComparator = new Comparator<Face>() {
		public int compare(Face f1, Face f2) {
			Vector3f f1Center = f1.getCenter();
			Vector3f f2Center = f2.getCenter();

			return (int) (f1Center.y() - f2Center.y());
		}
	};

	public static Comparator<Face> FaceZComparator = new Comparator<Face>() {
		public int compare(Face f1, Face f2) {
			Vector3f f1Center = f1.getCenter();
			Vector3f f2Center = f2.getCenter();

			return (int) (f1Center.z() - f2Center.z());
		}
	};
}
