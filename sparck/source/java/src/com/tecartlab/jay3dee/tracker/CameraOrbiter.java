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

import com.tecartlab.tools.math.la.Quaternionf;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.threedee.Node;

public class CameraOrbiter {

	public static final int ORBITING_OFF 		= 0;
	public static final int ORBITING_TURNTABLE 	= 1;

	private static final float slowFactor = .05f;

	private Node rot;
	private Node pan;
	private Node orbit;
	private Node shift;

	private int orbiting;

	public CameraOrbiter(int _orbitingType){
		orbiting = _orbitingType;

		rot = new Node();
		pan = new Node();
		orbit = new Node();
		shift = new Node();

		pan.setParent(rot);
		orbit.setParent(pan);
		shift.setParent(pan);

		orbit.move(0f, 0f, 4f);
	}

	public boolean isEnabled(){
		return (orbiting != ORBITING_OFF)? true: false;
	}

	public Node event(int mouseDeltaX, int mouseDeltaY, boolean keyShift, boolean keyCtrl, boolean keyAlt){
		if(orbiting == ORBITING_TURNTABLE){
			if(keyShift){
				float orbitFactor = (1f - 1f / Math.abs(orbit.getPosition().z()));
				shift.move(-(float)mouseDeltaX * slowFactor * orbitFactor, (float)mouseDeltaY * slowFactor * orbitFactor, 0);
				rot.setPosition(shift.getWorldPosition());
				shift.setPosition(new Vector3f());
			}else if (keyCtrl){
				orbit.move(0, 0, (float)mouseDeltaY * slowFactor);
			}else if (keyAlt){
				Quaternionf rotation = new Quaternionf();
				rotation.setEulerAngles(0f, 0f, -mouseDeltaX);
				rot.rotate(rotation);
				rotation.setEulerAngles(-mouseDeltaY, 0f, 0f);
				pan.rotate(rotation);
			}
		}
		return orbit.clone();
	}


}
