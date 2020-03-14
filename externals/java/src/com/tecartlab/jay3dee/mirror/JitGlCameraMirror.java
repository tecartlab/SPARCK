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

package com.tecartlab.jay3dee.mirror;

import com.cycling74.max.Atom;

import com.tecartlab.jay3dee.Env;
import com.tecartlab.jay3dee.ModelContainer;
import com.tecartlab.jay3dee.ObjectContainer;
import com.tecartlab.jay3dee.model.drawer.ModelDrawer;
import com.tecartlab.mxj.utils.mirror.*;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynException;
import com.tecartlab.utils.dyndist.DynPublication;
import com.tecartlab.utils.dyndist.DynPublisher;
import com.tecartlab.utils.dyndist.DynSubscription;

public class JitGlCameraMirror extends JitterMirror{

	private CameraPublisher publisher;
	private JitGlCameraHandler camera;

	private String cameraidentifier;
	private String contextName;

	public JitGlCameraMirror(Atom[] args) {
		super(args);
		if (args.length < 2)
			Debug.info("Jay3DeeCamera", "needs a j3dcontext and a cameraname as arguments. otherwise use 'setj3dcontext', 'setcameraname'.");
		else{
			cameraidentifier = args[0].toString();
			contextName = args[1].toString();
		}
		publisher = new CameraPublisher();
		camera = new JitGlCameraHandler(super.connector);

	}

	public void setj3dcontext(String _context){
		contextName = _context;
		publishObject();
	}

	public void setcameraname(String _cameraname){
		cameraidentifier = _cameraname;
		publishObject();
	}

	public void loadbang(){
		publishObject();
	}

	private void publishObject(){
		try {
			if(cameraidentifier != null && contextName != null)
				publisher.publishObject(cameraidentifier, contextName, camera);
		} catch (DynException e) {
			Debug.fatal("Jay3DeeCamera" , e.getMessage());
		}
	}

	public void changeEvent(String message){
		camera.hasChanged = true;
	}

	public void notifyDeleted(){
		publisher.notifyDeleted();
	}

	protected class CameraPublisher implements DynPublisher{

		private DynPublication publication;

		public CameraPublisher(){
		}

		public void publishObject(String _cameraidentifier, String _contextname,  JitGlCameraHandler _camera) throws DynException{
			if(publication != null){
				Debug.verbose("Jay3DeeCamera["+cameraidentifier+"]", "Recall this camera from context: "+ contextName);
				publication.recall();
			}
			Debug.verbose("Jay3DeeCamera["+cameraidentifier+"]", "Publish this camera to context: "+ _contextname);
			publication = Env.getEnv().cameraDistributor.create(this, _cameraidentifier, _camera);
			publication.publish();
		}

		public void notifyDeleted(){
			Debug.verbose("Jay3DeeCamera["+cameraidentifier+"]", "Recall this amera from context: "+ contextName);
			if(publication != null)
				publication.recall();
		}

		public void subscriptionConnected(String distributor,
				DynSubscription subscription) {
			Debug.verbose("Jay3DeeCamera["+cameraidentifier+"]", "Camera got subscribed from context: "+ contextName);
		}

		public void subscriptionDisconnected(String distributor,
				DynSubscription c) {
			Debug.verbose("Jay3DeeCamera["+cameraidentifier+"]", "Camera got unsubscribed from context: "+ contextName);
		}

		public boolean subscriptionCallback(String distributor,
				DynSubscription subscription) {
			return false;
		}
	}


}
