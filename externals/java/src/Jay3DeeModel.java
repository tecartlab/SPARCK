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
import com.tecartlab.jay3dee.model.drawer.ModelDrawer;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynException;
import com.tecartlab.utils.dyndist.DynPublication;
import com.tecartlab.utils.dyndist.DynPublisher;
import com.tecartlab.utils.dyndist.DynSubscriber;
import com.tecartlab.utils.dyndist.DynSubscription;
/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */
public class Jay3DeeModel extends MaxObject {

	ModelContainer sketch;

	String file;

	public Jay3DeeModel(Atom args[])
	{
		sketch = new ModelContainer();
		if (args.length == 0)
			Debug.info("Jay3DeeModel", "no modelname set. use 'setmodelname'.");
		else{
			setmodelname(args[0].toString());
		}
		declareAttribute("file", "getfilepath", "read");
	}

	public void setmodelname(String _modelname){
		try{
			sketch.init(_modelname);
		}catch (DynException e){
			bail(e.getMessage());
		}
	}

	public void notifyDeleted(){
		if(sketch != null)
			sketch.notifyDeleted();
	}

	public String getfilepath(){
		return file;
	}

	public void read(String filepath) {
		file = filepath;
		if(sketch != null)
			sketch.load(file);
	}

	/**
	 * creates a model with the specified points.
	 * three point make a vertice, with each vertice drawing a line to the XY plane
	 * @param vertices
	 */
	public void createToXY(Atom[] vertices) {
		if((vertices.length % 3) == 0){
			if(sketch != null)
				sketch.create(vertices, 0);
		} else {
			Debug.error("Jay3DeeModel", "'createToZ' the number of values need to be multiple of 3.");
		}
	}


}
