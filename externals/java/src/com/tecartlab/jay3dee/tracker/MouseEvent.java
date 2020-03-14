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

package com.tecartlab.jay3dee.tracker;

import com.cycling74.max.Atom;

public class MouseEvent {

	public int screenPosX, screenPosY, screenDiffX, screenDiffY;

	private boolean pressed;

	public MouseEvent(Atom[] args){
		pressed = (args[0].getInt() == 0)? false: true;
		screenPosX = args[1].getInt();
		screenPosY = args[2].getInt();
		screenDiffX = args[3].getInt();
		screenDiffY = args[4].getInt();
	}

	public boolean isInWindow(int[] rect){
		if((rect[0] < screenPosX && screenPosX < rect[2]) &&
				(rect[1] < screenPosY && screenPosY < rect[3]))
			return true;
		return false;
	}

	public boolean isInCamera(int[] rect, float[] viewport){
		int width = rect[2]-rect[0];
		int heigth = rect[3]-rect[1];
		int left = rect[0] + (int)(viewport[0] * width);
		int right = rect[0] + (int)(viewport[0] * width) + (int)(viewport[2] * width);
		int bottom = rect[3] - (int)(viewport[1] * rect[3]);
		int top = rect[3] - (int)(viewport[1] * heigth) - (int)(viewport[3] * heigth);
		if((left < screenPosX && screenPosX < right) &&
				(top < screenPosY && screenPosY < bottom))
			return true;
		return false;
	}

}
