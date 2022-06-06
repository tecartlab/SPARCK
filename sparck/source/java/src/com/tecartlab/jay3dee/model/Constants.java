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

public interface Constants {

	 static final int CUSTOM       = 0; // user-specified fanciness
	 static final int ORTHOGRAPHIC = 2; // 2D isometric projection
	 static final int PERSPECTIVE  = 3; // perspective matrix


	  // shapes

	  // the low four bits set the variety,
	  // higher bits set the specific shape type

	  //static final int GROUP           = (1 << 2);

	 static final int POINT           = 2;  // shared with light (!)
	 static final int POINTS          = 2;

	  static final int LINE            = 4;
	  static final int LINES           = 4;

	  static final int TRIANGLE        = 8;
	  static final int TRIANGLES       = 9;
	  static final int TRIANGLE_STRIP  = 10;
	  static final int TRIANGLE_FAN    = 11;

	  static final int QUAD            = 16;
	  static final int QUADS           = 16;
	  static final int QUAD_STRIP      = 17;

	  static final int POLYGON         = 20;
	  static final int PATH            = 21;

	  static final int RECT            = 30;
	  static final int ELLIPSE         = 31;
	  static final int ARC             = 32;

	  static final int SPHERE          = 40;
	  static final int BOX             = 41;

	  static final int MAX_INT = Integer.MAX_VALUE;
	  static final int MIN_INT = Integer.MIN_VALUE;
	  static final float MAX_FLOAT = Float.MAX_VALUE;
	  static final float MIN_FLOAT = -Float.MAX_VALUE;

	  // uv texture orientation modes

	  /** texture coordinates in 0..1 range */
	  static final int NORMAL     = 1;
	  /** texture coordinates based on image width/height */
	  static final int IMAGE      = 2;


}
