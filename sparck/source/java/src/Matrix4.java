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
import com.tecartlab.tools.threedee.Frustum;
import com.tecartlab.utils.Debug;
/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */
public class Matrix4 extends MaxObject {

	private Matrix4x4f matrix;

	public Matrix4(Atom args[])
	{
	}

	public void out()
	{
	}

	public void notifyDeleted()
	{
	}

	public void setmatrix(Atom[] args){
		if(args.length == 16){
			matrix = new Matrix4x4f(Atom.toFloat(args));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(matrix.get()));
	}

	public void settranslation(Atom[] args){
		if(args.length == 3){
			matrix = new Matrix4x4f(new Vector3f(Atom.toFloat(args)));
		}
		this.
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(matrix.get()));
	}

	public void setscale(Atom[] args){
		if(args.length == 3){
			matrix = new Matrix4x4f(args[0].toFloat(), args[1].toFloat(), args[2].toFloat());
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(matrix.get()));
	}

	public void setrotation(Atom[] args){
		if(args.length == 4){
			matrix = new Matrix4x4f(new Quaternionf(Atom.toFloat(args)));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(matrix.get()));
	}

	public void setfrustum(Atom[] args){
		if(args.length == 6){
			matrix = new Matrix4x4f(new Frustum(Atom.toFloat(args)));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(matrix.get()));
	}

	public void multiply(Atom[] args){
		if(args.length == 16){
			matrix.multiply(new Matrix4x4f(Atom.toFloat(args)));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(matrix.get()));
	}

	public void invert(Atom[] args){
		if(args.length == 0){
			outlet(getInfoIdx(), "matrix4", Atom.newAtom(matrix.invert().get()));
		}
		outlet(getInfoIdx(), "matrix4", Atom.newAtom(matrix.get()));
	}

	public void makeinvert(Atom[] args){
		if(args.length == 16){
			outlet(0, "matrix4", Atom.newAtom((new Matrix4x4f(Atom.toFloat(args)).invert().get())));
		}
	}

}
