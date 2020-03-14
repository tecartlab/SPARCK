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


import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;

import com.tecartlab.jay3dee.*;
import com.tecartlab.tools.math.la.*;
import com.tecartlab.tools.threedee.*;
import com.tecartlab.utils.Debug;
/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */
public class CameraTest extends MaxObject {

	private Node parent;
	private Camera camera;

	int dimX, dimY;

	public CameraTest(Atom args[])
	{
		camera = new Camera();
		parent = new Node();
		camera.setParent(parent);

		dimX = 320;
		dimY = 240;

		post("1");
	}

	public void out()
	{
	}

	public void notifyDeleted()
	{
	}

	public void size(Atom[] args){
		if(args.length == 2){
			camera.viewport.width = args[0].toFloat();
			camera.viewport.height = args[1].toFloat();
		}
	}

	public void getviewportray(Atom[] args){
		if(args.length == 2)
			outlet(getInfoIdx(), "pickray", Atom.newAtom(camera.getViewportRay(args[0].toInt(), args[1].toInt()).getTwoPoints()));
	}

	public void getscreenpos(Atom[] args){
		if(args.length == 3)
			outlet(getInfoIdx(), "screen", Atom.newAtom(camera.worldToScreen(new Vector3f(args[0].toFloat(), args[1].toFloat(), args[2].toFloat())).get()));
	}

	public void frustum(Atom[] args){
		if(args.length == 6){
			camera.frustum = new Frustum(Atom.toFloat(args));
		}
	}

	public void position(Atom[] args){
		if(args.length == 3){
			camera.setPosition(new Vector3f(Atom.toFloat(args)));
		}
		outlet(getInfoIdx(), "viewProjectionMatrix", Atom.newAtom(camera.getViewProjectionMatrix().get()));
	}

	public void rotatexyz(Atom[] args){
		if(args.length == 3){
			camera.setOrientation(new Vector3f(Atom.toFloat(args)));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(camera.getWorldTransformationMatrix().get()));
	}

	public void scale(Atom[] args){
		if(args.length == 3){
			camera.setScale(new Vector3f(Atom.toFloat(args)));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(camera.getWorldTransformationMatrix().get()));
	}

	public void setparentposition(Atom[] args){
		if(args.length == 3){
			parent.setPosition(new Vector3f(Atom.toFloat(args)));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(camera.getWorldTransformationMatrix().get()));
	}

	public void setparentrotation(Atom[] args){
		if(args.length == 3){
			parent.setOrientation(new Vector3f(Atom.toFloat(args)));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(camera.getWorldTransformationMatrix().get()));
	}

	public void setparentscale(Atom[] args){
		if(args.length == 3){
			parent.setScale(new Vector3f(Atom.toFloat(args)));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(camera.getWorldTransformationMatrix().get()));
	}


}
