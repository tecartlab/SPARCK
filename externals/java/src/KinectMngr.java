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
import java.io.IOException;
import java.net.* ;

import com.tecartlab.sparck.kinect.*;
import com.tecartlab.tools.math.la.*;
import com.tecartlab.tools.threedee.*;
import com.tecartlab.utils.Debug;
/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */

public class KinectMngr extends MaxObject{

	KinectMatrixClient rgbMatrix;
	KinectMatrixClient depthMatrix;
	KinectMatrixClient rawMatrix;

	String address;

	public KinectMngr(Atom args[]){
		declareInlets(new int[]{ DataTypes.ALL, DataTypes.ALL});
		declareOutlets(new int[]{ DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
		createInfoOutlet(false);

		if(args.length == 1)
			setaddress(args[0].getString());

	}

	/**
	 * Open with an remote IP-address and a remote Port
	 * @param args
	 */
	public void setaddress(String _address){
		disconnect();
		address = _address;
			/*
				rgbMatrix = new KinectMatrixClient(
						"KinectRGB-TCPClient",
						new Callback(this, "rgbframe", "none"),
						args[0].getString(),
						34101);

				depthMatrix = new KinectMatrixClient(
						"KinectDepth-TCPClient",
						new Callback(this, "depthframe", "none"),
						args[0].getString(),
						34102);

				rawMatrix = new KinectMatrixClient(
						"KinectRAW-TCPClient",
						new Callback(this, "rawframe", "none"),
						args[0].getString(),
						34103);
							}
				*/
	}

	public void rgbframe(String matrixname){
		outlet(0, "jit_matrix", matrixname);
	}

	public void depthframe(String matrixname){
		outlet(1, "jit_matrix", matrixname);
	}

	public void rawframe(String matrixname){
		outlet(2, "jit_matrix", matrixname);
	}

	public void connect(){
		connectrgbstream();
		connectdepthstream();
		connectrawstream();
	}

	public void connectrgbstream(){
		if(rgbMatrix == null)
			rgbMatrix = new KinectMatrixClient(
					"KinectRGB-TCPClient",
					new Callback(this, "rgbframe", "none"),
					address,
					34101);
			rgbMatrix.start();
	}

	public void connectdepthstream(){
		if(depthMatrix == null)
			depthMatrix = new KinectMatrixClient(
					"KinectDepth-TCPClient",
					new Callback(this, "depthframe", "none"),
					address,
					34102);

			depthMatrix.start();
	}

	public void connectrawstream(){
		if(rawMatrix == null)
			rawMatrix = new KinectMatrixClient(
					"KinectRAW-TCPClient",
					new Callback(this, "rawframe", "none"),
					address,
					34103);
			rawMatrix.start();
	}

	public void disconnect(){
		disconnectrgbstream();
		disconnectdepthstream();
		disconnectrawstream();
	}

	public void disconnectrgbstream(){
		if(rgbMatrix != null){
			if(rgbMatrix.isAlive()){
				//post("KinectClient attempts to closing connection...");
				rgbMatrix.close();
			}
			rgbMatrix = null;
		}
	}

	public void disconnectdepthstream(){
		if(depthMatrix != null){
			if(depthMatrix.isAlive()){
				//post("KinectClient attempts to closing connection...");
				depthMatrix.close();
			}
			depthMatrix = null;
		}
	}

	public void disconnectrawstream(){
		if(rawMatrix != null){
			if(rawMatrix.isAlive()){
				//post("KinectClient attempts to closing connection...");
				rawMatrix.close();
			}
			rawMatrix = null;
		}
	}

	public void bang(){
		nextframe();
	}

	public void nextframe(){
		if(rgbMatrix != null)
			rgbMatrix.nextframe();
		if(rawMatrix != null)
			rawMatrix.nextframe();
		if(depthMatrix != null)
			depthMatrix.nextframe();
	}

	public void notifyDeleted(){
		disconnect();
	}

}
