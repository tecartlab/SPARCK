package com.tecartlab.sparck.calibration;
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

import com.tecartlab.tools.math.la.Vector3f;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public class DelaunayVertice {

	Vec2D voronoi;
	Vec3D vertice;
	Vec2D uv;

	public DelaunayVertice(Vector3f _vertice, Vector3f _texture){
		voronoi = new Vec2D(_vertice.x(), _vertice.y());
		vertice = new Vec3D(_vertice.x(), _vertice.y(), _vertice.z());
		uv = new Vec2D(_texture.x(), _texture.y());
	}

	public Vec2D getVoronoi(){
		return voronoi;
	}
}
