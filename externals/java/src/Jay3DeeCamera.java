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
import com.tecartlab.utils.Debug;

/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */
@Deprecated
public class Jay3DeeCamera extends MaxObject{

	CameraContainer jayobject;

	String context;
	String cameraname;

	private final int CAM_INLET = 1;
	private final int CAM_OUTLET = 1;

	public Jay3DeeCamera(Atom args[])
	{
		if (args.length < 2)
			bail("Jay3DeeCamera: needs a cameraname and a context as arguments");
		else{
			cameraname = args[0].toString();
			context = args[1].toString();
			jayobject = new CameraContainer(cameraname, context);
		}

		declareIO(1,1);
		declareAttribute("drawto", null, "drawto");
	}

	public void drawto(String _drawto){
		jayobject.setDrawTo(_drawto);
	}

	public void anything(String msg, Atom[] args){
		if(msg.startsWith("@") && args.length > 0){
			jayobject.setAttribute(msg, args);
		} else{
			Atom[] ret;
			if(args.length > 0)
				ret = jayobject.message(msg, args);
			else
				ret = jayobject.message(msg);

			if(ret != null && ret.length > 0)
				outlet(0, ret);
		}
	}

	public void notifyDeleted()
	{
		jayobject.notifyDeleted();
		jayobject = null;
	}


}
