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
public class Jay3DeeMVPMatrix extends MaxObject {

	private Matrix4x4f model_matrix;
	private Matrix4x4f view_matrix;
	private Matrix4x4f projection_matrix;

	private final int PATTR_MODEL_INLET = 1;
	private final int PATTR_VIEW_INLET = 2;
	private final int PATTR_PROJECTION_INLET = 3;

	private final int PATTR_OUTLET = 1;

	public Jay3DeeMVPMatrix(Atom args[])
	{
		declareIO(3,1);

	}

	public void anything(String message, Atom[] args){
		if(args.length == 16){
			if(getInlet() == PATTR_MODEL_INLET){
				if(model_matrix != null)
					model_matrix = new Matrix4x4f(Atom.toFloat(args));
				if(view_matrix != null)
					view_matrix = new Matrix4x4f(Atom.toFloat(args));
				if(projection_matrix != null)
					projection_matrix = new Matrix4x4f(Atom.toFloat(args));
			}
			if(model_matrix != null && view_matrix != null && projection_matrix != null){
				outlet(PATTR_OUTLET, projection_matrix.multiplyMake(view_matrix).multiply(model_matrix).get());
			}
		} else {
			this.error("Jay3DeeMVPMatrix requires 16 values");
		}
	}

}
