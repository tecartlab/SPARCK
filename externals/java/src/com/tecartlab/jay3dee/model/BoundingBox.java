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

import com.tecartlab.tools.math.la.*;
import com.tecartlab.utils.Debug;

public class BoundingBox implements Constants {

	private Vector3f min = new Vector3f(MAX_FLOAT,MAX_FLOAT,MAX_FLOAT);
	private Vector3f max = new Vector3f(MIN_FLOAT,MIN_FLOAT,MIN_FLOAT);
	private Vector3f center = new Vector3f(0,0,0);
	private Vector3f whd = new Vector3f(0,0,0);


	public BoundingBox(ModelData model) {

		Debug.info("Bounding Box","Getting the Bounding Box");

		int numberOfVerts = model.getModelVerticesCount();

		if (numberOfVerts == 0) {
			Debug.info("Bounding Box","The model has no verts. Have you loaded it yet?");
		} else {

			Vector3f v;

			for (int i = 0; i < numberOfVerts; i++) {

				v = model.getModelVertice(i);

				if(v.x() < min.x()) {min.setX(v.x());}
				if(v.x() > max.x()) {max.setX(v.x());}
				if(v.y() < min.y()) {min.setY(v.y());}
				if(v.y() > max.y()) {max.setY(v.y());}
				if(v.z() < min.z()) {min.setZ(v.z());}
				if(v.z() > max.z()) {max.setZ(v.z());}

			}

			whd.setX((float) Math.sqrt((max.x() - min.x()) * (max.x() - min.x())));
			whd.setY((float) Math.sqrt((max.y() - min.y()) * (max.y() - min.y())));
			whd.setZ((float) Math.sqrt((max.z() - min.z()) * (max.z() - min.z())));

			center.setAdd(center, min);
			center.setAdd(center, max);
			center.divide(2);

		}
	}

	public void draw() {

		/**
		parent.rectMode(CORNERS);

		parent.pushMatrix();
		parent.translate(center.x(),center.y(),center.z());

		parent.box(whd.x(), whd.y(), whd.z());
		parent.popMatrix();
		 **/
	}

	public Vector3f getMin() {
		return min;
	}

	public Vector3f getMax() {
		return max;
	}

	public Vector3f getCenter() {
		return center;
	}

	public Vector3f getWHD() {
		return whd;
	}

}
