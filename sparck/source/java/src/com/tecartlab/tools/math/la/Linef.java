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

package com.tecartlab.tools.math.la;

public class Linef{

	public Vector3f theOrigin;

	public Vector3f direction;

	public Linef() {
		theOrigin = new Vector3f();
		direction = new Vector3f();
	}

	/**
	 * Creates a line in theOrigin with its direction
	 *
	 * @param	_theOrigin	the reference point of that line
	 * @param	_theDirection	the direction vector of that line
	 */
	public Linef(Vector3f _theOrigin, Vector3f _theDirection) {
		theOrigin = new Vector3f(_theOrigin);
		direction = new Vector3f(_theDirection);
	}

	/**
	 * Creates a line defined by a float array with 6 values. The first three
	 * values define x1,y1,z1 of the first point, the second three values define
	 * x2,y2,z2 of the second point.
	 *
	 * @param twoPoints - 6 indices float array [x1,y1,z1,x2,y2,z2]
	 */
	public Linef(float[] twoPoints) {
		set(twoPoints);
	}

	/**
	 * Creates a copy of a line
	 *
	 * @param	theOther	the line to be copied
	 */
	public Linef(Linef theOther) {
		theOrigin = new Vector3f(theOther.theOrigin);
		direction = new Vector3f(theOther.direction);
	}

	/**
	 * sets the line defined by two point: theOrigin and another point
	 *
	 * @param	_theOrigin		the reference point in that line
	 * @param	_theOtherPoint	the other point of that line
	 */
	public void set(Vector3f _theOrigin, Vector3f _theOtherPoint) {
		theOrigin = new Vector3f(_theOrigin);
		direction = _theOtherPoint.subMake(_theOrigin);
	}

	/**
	 * Sets the line defined by a float array with 6 values. The first three
	 * values define x1,y1,z1 of the first point, the second three values define
	 * x2,y2,z2 of the second point.
	 *
	 * @param twoPoints - 6 indices float array
	 */
	public void set(float[] twoPoints){
		set(new Vector3f(twoPoints[0], twoPoints[1], twoPoints[2]), new Vector3f(twoPoints[3], twoPoints[4], twoPoints[5]));
	}

	/**
	 * Returns a float array with 6 values, the first 3 values are the origin, the second
	 * 3 values are the target.
	 *
	 * @return float array
	 */
	public float[] get(){
		float[] ret = new float[6];
		ret[0] = theOrigin.x();
		ret[1] = theOrigin.y();
		ret[2] = theOrigin.z();
		Vector3f theOther = theOtherPoint();
		ret[3] = theOther.x();
		ret[4] = theOther.y();
		ret[5] = theOther.z();
		return ret;
	}

	/**
	 * Gets the line in a float array: The first three
	 * values define x1,y1,z1 of the first point, the second three values define
	 * x2,y2,z2 of the second point.
	 *
	 * @return twoPoints - 6 indices float array
	 */
	public float[] getTwoPoints(){
		float[] ret = new float[6];
		Vector3f theOther = theOtherPoint();
		ret[0] = theOrigin.x();
		ret[1] = theOrigin.y();
		ret[2] = theOrigin.z();
		ret[3] = theOther.x();
		ret[4] = theOther.y();
		ret[5] = theOther.z();
		return ret;
	}

	/**
	 * returns another point on this line
	 * it is the result of a vector addition of theOrigin and the direction
	 *
	 * @return	theOtherPoint	the other point on that line
	 */
	protected Vector3f theOtherPoint(){
		return theOrigin.addMake(direction);
	}


	/**
	 * This method sets theOrigin closest to the space origin.
	 */
	public void originize(){
		Vector3f unity = theOrigin.crossMake(direction);
		Vector3f center = unity.crossMake(direction);
		center.normalize();
		center.scale(center.angle(theOrigin)*theOrigin.magnitude());
		theOrigin = center;
	}

	/**
	 * returns the shortest distance between to lines
	 *
	 * @param	line	the other line
	 * @return	distance vector
	 */
	public Vector3f getDistanceVector(Linef line){
		Vector3f dirVW = line.direction.crossMake(direction);
		Vector3f r0s = line.theOrigin.subMake(theOrigin);
		if(dirVW.magnitude() != 0.0f){
			dirVW.normalize();
			dirVW.scale((float)Math.abs(dirVW.dot(r0s)));
			return dirVW;
		}
		// if they are parallel
		Linef lineA = new Linef(this);
		Linef lineB = new Linef(line);
		lineA.originize();
		lineB.originize();
		Vector3f diff = lineB.theOrigin.subMake(lineA.theOrigin);
		return diff;
	}

	/**
	 * checks if the specified line is parallel to this line
	 *
	 * @param	otherLine	the other line
	 * @return	true if parallel
	 */
	public boolean isParallel(Linef otherLine){
		Vector3f dirVW = direction.crossMake(otherLine.direction);
		return (dirVW.magnitude() == 0.0f)? true: false;
	}

	/**
	 * returns the shortest distance between to lines
	 *
	 * @param	otherLine	the other line
	 * @return	distance
	 */
	public float getDistance(Linef otherLine){
		return getDistanceVector(otherLine).magnitude();
	}

	/**
	 * returns the shortest distance vector between this line and the specified point
	 *
	 * @param	point
	 * @return	distance vector
	 */
	public Vector3f getDistanceVector(Vector3f point){
		Vector3f ret = point.subMake(theOrigin);
		Vector3f dir = new Vector3f(direction);
		float scale = dir.dot(ret) / (dir.magnitude() * dir.magnitude());
		dir.scale(scale);
		ret.sub(dir);
		return ret;
	}

	/**
	 * returns the shortest distance between this line and the specified point
	 *
	 * @param	point
	 * @return	distance
	 */
	public float getDistance(Vector3f point){
		Vector3f temp = point.subMake(theOrigin).cross(direction);
		return temp.magnitude() / direction.magnitude();
	}

	/**
	 * checks if this line and the specified line intersect
	 *
	 * @param	otherLine
	 * @return	true if they intersect
	 */
	public boolean intersect(Vector3f otherLine){
		return (getDistance(otherLine) == 0.0f)? true: false;
	}

	/**
	 * Applies the transformation matrix to this linef-instance and returns it
	 * @param mat
	 * @return the transformed instance
	 */
	public Linef transform(Matrix4x4f mat){
		Vector3f newOrigin = mat.multiplyMake(theOrigin);
		direction = mat.multiply(theOtherPoint()).subMake(newOrigin);
		theOrigin = newOrigin;
		return this;
	}

	public String toString() {
		return "origin + " + theOrigin + " / " + " direction " + direction;
	}

	public static void main(String[] args) {
	        /* multiplying matrices */

		Linef line = new Linef(new Vector3f(2, 0, 0), new Vector3f(1, 0, 0));

		Quaternionf rot = new Quaternionf(0, 0, 90);
		Vector3f scale = new Vector3f(2, 2, 2);
		Vector3f translate = new Vector3f(0, 2, 0);
		Matrix4x4f mat = new Matrix4x4f();
		mat.scale(scale);
		mat.rotate(rot);
		mat.translate(translate);

		mat.invert();

		line.transform(mat);

		System.out.println(line);

		/*

		Linef crossline = new Linef(new Vector3f(2, 2, 1), new Vector3f(1, 1, 1));

		boolean parallel = line.isParallel(crossline);

		float distance = line.getDistance(crossline);

		Vector3f distanceV = line.getDistanceVector(crossline);

		System.out.println(distance);
		*/

	 }

}
