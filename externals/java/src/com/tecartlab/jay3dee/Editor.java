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

package com.tecartlab.jay3dee;

import java.util.ArrayList;

import com.tecartlab.jay3dee.tracker.GuiTracker;
import com.tecartlab.jay3dee.tracker.TrackerEvent;
import com.tecartlab.jay3dee.tracker.TrackerListener;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynException;
import com.tecartlab.utils.dyndist.DynPublication;
import com.tecartlab.utils.dyndist.DynPublisher;
import com.tecartlab.utils.dyndist.DynSubscriber;
import com.tecartlab.utils.dyndist.DynSubscription;

import com.cycling74.jitter.*;
import com.cycling74.max.MaxSystem;

public class Editor implements TrackerListener{

	String trackername;
	GuiTracker tracker;
	String objectname;

	private TrackerSubscriber trackerSubscriber;
	private ObjectSubscriber objSubscriber;

	ObjectContainer object;

	public Editor(String _context){
		trackername = _context;
	}

	public void init(){
		trackerSubscriber = new TrackerSubscriber(trackername, this);
		objSubscriber = new ObjectSubscriber();
	}

	public void setObjectName(String _objname){
		objSubscriber.subscribeObject(_objname);
		objectname = _objname;
	}

	public void notifyDeleted(){
		trackerSubscriber.notifyDeleted();
		objSubscriber.notifyDeleted();
	}


	public void trackerEvent(TrackerEvent tevent) {
		// if there is no object attached, none of this makes sense...
		if(object != null && object.isOperational()){
			if(tevent.mouseInsideCameraCanvas)
				object.pickVertice(tevent.getViewPortRay());

		} else {
			MaxSystem.error("Jay3DeeEditor: No Object attached. Use the @object-attribute to set an object");
		}
	}

	protected class TrackerSubscriber implements DynSubscriber{

		private DynSubscription subscription;

		public TrackerSubscriber(String _tracker, TrackerListener _listener){
			/*
			String[] err = Env.getEnv().trackerDistributor.getPublicationNames();
			for(int i = 0; i < err.length; i++){
				Debugger.verbose("Editor", "Published Tracker: " + err[i]);
			}
			*/
			Debug.verbose("Jay3DeeEditor", "Subscribe to Tracker: " + _tracker);
			subscription = Env.getEnv().trackerDistributor.create(this, _tracker, _listener);
			subscription.subscribe();
		}

		public void notifyDeleted(){
			Debug.verbose("Jay3DeeEditor", "Unsubscribe from Tracker: " + trackername);
			subscription.unsubscribe();
		}

		public void publicationConnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeEditor", "Connect to published tracker: " + trackername);
		}

		public void publicationDisonnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeEditor", "Disconnect from (recalled) tracker: " + trackername);
		}

	}

	protected class ObjectSubscriber implements DynSubscriber{

		private DynSubscription subscription;

		public ObjectSubscriber(){
		}

		public void subscribeObject(String _objectName){
			if(subscription != null){
				Debug.verbose("Jay3DeeEditor", "Unsubscribe from Object: " + objectname);
				subscription.unsubscribe();
			}
			Debug.verbose("Jay3DeeEditor", "Subscribe to Object: " + _objectName);
			subscription = Env.getEnv().objectDistributor.create(this, _objectName, null);
			subscription.subscribe();
		}

		public void notifyDeleted(){
			if(subscription != null)
				Debug.verbose("Jay3DeeEditor", "Unsubscribed from Object: " + objectname);
				subscription.unsubscribe();
		}

		public void publicationConnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeEditor", "Connected to published Object: " + objectname);
			object = (ObjectContainer) subscription.getPublishedObject();
		}

		public void publicationDisonnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeEditor", "Disconnected from (recalled) object: " + objectname);
			object = null;
		}
	}

}
