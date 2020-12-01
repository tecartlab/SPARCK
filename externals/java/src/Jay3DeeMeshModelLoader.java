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

import com.tecartlab.jay3dee.*;
import com.tecartlab.sparck.faceAR.ModelData;
import com.tecartlab.sparck.faceAR.MaxMeshGenerator;
/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */
public class Jay3DeeMeshModelLoader extends MaxObject implements CallBackInterface {

	FileManager fileManager;
	ModelData model;
	MaxMeshGenerator generator;

	String file;

	public Jay3DeeMeshModelLoader(Atom args[])
	{
		fileManager = new FileManager(10);
		declareIO(1,1);
		this.setInletAssist(new String[]{"read path to objfile"});
		this.setOutletAssist(new String[]{"mesh matrix"});
	}

	public void notifyDeleted(){
		if(generator != null)
			generator.notifyDeleted();
	}

	public void read(String filepath) {
		file = filepath;
		
		if(model == null) {
			model = new ModelData(this);
			generator = new MaxMeshGenerator();
		}
		model.load(file);
		generator.initialize(model);
	}
	
	public synchronized void dataEvent(String message) {
		this.outlet(0, "jit_matrix", generator.generateMatrix().getName());
		this.outlet(1, message);
	}

}
