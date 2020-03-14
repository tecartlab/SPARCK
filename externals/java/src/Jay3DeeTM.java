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
public class Jay3DeeTM extends MaxObject {

	private Vector3f translation;
	private Vector3f scale;
	private Quaternionf rotation;

	public Jay3DeeTM(Atom args[])
	{
		reset();
	}

	public void reset(){
		translation = new Vector3f();
		scale = new Vector3f();
		rotation = new Quaternionf();
	}

	public void out()
	{
		Matrix4x4f result = new Matrix4x4f();
		result.scale(scale);
		result.rotate(rotation);
		result.translate(translation);
		float[] mat = result.get();
		Atom[] ret = new Atom[16];
		for(int i = 0; i < ret.length; i++){
			ret[i] = Atom.newAtom(mat[i]);
		}
		outlet(0, "matrix", ret);
	}

	public void notifyDeleted()
	{
	}

	public void quat(Atom[] args){
		if(args.length == 4){
			rotation = new Quaternionf(Atom.toFloat(args));
		}
		out();
	}

	public void scale(Atom[] args){
		if(args.length == 3){
			scale = new Vector3f(Atom.toFloat(args));
		}
		out();
	}

	public void position(Atom[] args){
		if(args.length == 3){
			translation = new Vector3f(Atom.toFloat(args));
		}
		out();
	}

}
