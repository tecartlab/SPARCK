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
import com.tecartlab.utils.Debug;
/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */
public class Jay3DeeLA extends MaxObject {

	private Linef line;

	public Jay3DeeLA(Atom args[])
	{
	}

	public void out()
	{
	}

	public void notifyDeleted()
	{
	}

	public void quat2matrix4(Atom[] args){
		if(args.length == 4){
			Quaternionf quat = new Quaternionf(args[0].getFloat(), args[1].getFloat(),args[2].getFloat(),args[3].getFloat());
			Matrix4x4f matrix = new Matrix4x4f(quat);
			float[] mat = matrix.getCol();
			Atom[] ret = new Atom[16];
			for(int i = 0; i < ret.length; i++){
				ret[i] = Atom.newAtom(mat[i]);
			}
			outlet(0, "matrix", ret);
		}
	}

	public void matrix4toQuat(Atom[] args){
		if(args.length == 16){
			float[] mat = Atom.toFloat(args);
			Matrix4x4f matrix = new Matrix4x4f(mat);
			Quaternionf quat = matrix.getQuaternion();
			outlet(0, "quat", Atom.newAtom(quat.get()));
		}
	}

	public void line(Atom[] args){
		if(args.length == 6){
			float[] mat = Atom.toFloat(args);
			line = new Linef(new Vector3f(mat[0], mat[1], mat[2]), new Vector3f(mat[3], mat[4], mat[5]));
			post("line: "+ line.toString());
		}
	}

	public void pointDistanceToLine(Atom[] args){
		if(args.length == 3){
			float[] mat = Atom.toFloat(args);
			Vector3f point = new Vector3f(mat[0], mat[1], mat[2]);
			outlet(0, "distance", Atom.newAtom(line.getDistance(point)));
		}
	}

	public void lineDistanceToLine(Atom[] args){
		if(args.length == 6){
			float[] mat = Atom.toFloat(args);
			Linef crossline = new Linef(new Vector3f(mat[0], mat[1], mat[2]), new Vector3f(mat[3], mat[4], mat[5]));
			post("crossline: "+ crossline.toString());
			post("distance: "+ line.getDistance(crossline));
			outlet(0, "distance", Atom.newAtom(line.getDistance(crossline)));
		}
	}

	public void pointDistanceVectorToLine(Atom[] args){
		if(args.length == 3){
			float[] mat = Atom.toFloat(args);
			Vector3f point = new Vector3f(mat[0], mat[1], mat[2]);
			outlet(0, "distancevector", Atom.newAtom(line.getDistanceVector(point).get()));
		}
	}

}
