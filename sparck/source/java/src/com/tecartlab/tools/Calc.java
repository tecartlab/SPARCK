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

package com.tecartlab.tools;

public class Calc {

	public static float trim(float min, float value, float max) {
		if (value >= min) {
			if (value <= max) {
				return value;
			} else {
				return max;
			}
		} else {
			return min;
		}
	}

	public static float map(float val, float minVal, float maxVal,
			float minMap, float maxMap) {
		if (val > minVal) {
			if (val < maxVal) {
				return minMap + (val - minVal) / (maxVal - minVal)
						* (maxMap - minMap);
			} else {
				return maxMap;
			}
		}
		return minMap;
	}

	static public final float sq(float a) {
		return a * a;
	}

	static public final int min(int a, int b) {
		return (a < b) ? a : b;
	}

	static public final float min(float a, float b) {
		return (a < b) ? a : b;
	}

	/*
	 * static public final double min(double a, double b) { return (a < b) ? a :
	 * b; }
	 */

	static public final int min(int a, int b, int c) {
		return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
	}

	static public final float min(float a, float b, float c) {
		return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
	}

	static public final float dist(float x1, float y1, float x2, float y2) {
		return sqrt(sq(x2 - x1) + sq(y2 - y1));
	}

	static public final float dist(float x1, float y1, float z1, float x2,
			float y2, float z2) {
		return sqrt(sq(x2 - x1) + sq(y2 - y1) + sq(z2 - z1));
	}

	static public final float sqrt(float a) {
		return (float) Math.sqrt(a);
	}

}
