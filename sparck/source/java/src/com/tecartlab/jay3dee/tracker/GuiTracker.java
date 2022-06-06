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

package com.tecartlab.jay3dee.tracker;

import com.tecartlab.jay3dee.Env;
import com.tecartlab.jay3dee.mirror.JitGlCameraHandler;
import com.tecartlab.jay3dee.mirror.JitWindowHandler;
import com.tecartlab.tools.math.la.Linef;
import com.tecartlab.tools.threedee.Node;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynException;
import com.tecartlab.utils.dyndist.DynPublication;
import com.tecartlab.utils.dyndist.DynPublisher;
import com.tecartlab.utils.dyndist.DynSubscriber;
import com.tecartlab.utils.dyndist.DynSubscription;

import com.cycling74.jitter.*;
import com.cycling74.max.MaxSystem;

import java.util.*;

public class GuiTracker implements JitterNotifiable {

	private String name = null;
	private String j3d_context = null;
	private String camName = null;
	private JitGlCameraHandler camera = null;
	private JitWindowHandler window = null;

	private TrackerPublisher publisher;
	private CameraSubscriber camSubscriber;
	private WindowSubscriber winSubscriber;

	private MouseEvent mouseEvent;

	private CameraOrbiter orbiter;

	private TrackerEvent eventHelper;
	private boolean enabled = true;

	protected GuiTracker(String _name, String _j3d_context, String _camera) {
		name = _name;
		j3d_context = _j3d_context;
		camName = _camera;
		publisher = new TrackerPublisher();
		camSubscriber = new CameraSubscriber();
		winSubscriber = new WindowSubscriber();
		eventHelper = new TrackerEvent();
	}

	protected void init() throws DynException {
		winSubscriber.subscribeWindow(j3d_context, this);
		camSubscriber.subscribeCamera(camName);
		publisher.publishTracker(name, this);
	}

	public void enable(int _enable){
		enabled = (_enable == 1)? true: false;
	}

	public void j3dcontext(String _j3d_context){
		j3d_context = _j3d_context;
		winSubscriber.subscribeWindow(j3d_context, this);
	}

	public void cameraorbiting(int _orbitType){
		orbiter = new CameraOrbiter(_orbitType);
	}

	protected void keyEvent(KeyEvent _key){
		if(enabled){
			if(eventHelper.update(_key))
				sendTrackerEvent();
		}
	}

	protected void mouseEvent(MouseEvent _mouse){
		mouseEvent = _mouse;
	}

	public JitGlCameraHandler getCameraHandler(){
		return camera;
	}

	/**
	 * This Method is called when the attached windows context gets a mouse interaction
	 * It is the workhorse of this class
	 */
	public void notify(JitterEvent event){
		if(enabled && isOperational()){
			eventHelper.update(mouseEvent, event, window.getRect());
			sendTrackerEvent();
			if(orbiter != null && orbiter.isEnabled() && eventHelper.keyPressed_mouse && eventHelper.mouseInsideCameraCanvas){
				Node transform = orbiter.event(
						eventHelper.mouseDiffPosX,
						eventHelper.mouseDiffPosY,
						eventHelper.keyPressed_Shift,
						eventHelper.keyPressed_Ctrl,
						eventHelper.keyPressed_Alt);

				//System.out.println("rotation: " + transform.getWorldOrientation());
				//System.out.println("position: " + transform.getWorldPosition());

				camera.position(transform.getWorldPosition().get());
				camera.quat(transform.getWorldOrientation().get());
			}
		}
	}

	private void sendTrackerEvent(){
		Iterator<TrackerListener> i = publisher.listener.iterator();
		while(i.hasNext()){
			i.next().trackerEvent(eventHelper);
		}
	}

	public boolean isOperational(){
		if(camera != null)
			if(window != null)
				return true;
			else
				Debug.warning("Jay3DeeTracker["+name+"]", "Missing Window: " + j3d_context);
		else
			Debug.warning("Jay3DeeTracker["+name+"]", "Missing Camera: " + camName);
		return false;
	}

	protected class TrackerPublisher implements DynPublisher{

		private DynPublication publication;

		public ArrayList<TrackerListener> listener;

		public TrackerPublisher(){
			listener = new ArrayList<TrackerListener>();
		}

		public void publishTracker(String _name, GuiTracker tracker) throws DynException{
			Debug.verbose("Jay3DeeTracker["+name+"]", "Publishing this Tracker");
			publication = Env.getEnv().trackerDistributor.create(this, _name, tracker);
			publication.publish();
		}

		public void notifyDeleted(){
			Debug.verbose("Jay3DeeTracker["+name+"]", "Recalling this Tracker");
			publication.recall();
			listener.clear();
		}

		public void subscriptionConnected(String distributor,
				DynSubscription subscription) {
			Debug.verbose("Jay3DeeTracker["+name+"]", "Tracker got Subscribed");
			listener.add((TrackerListener) subscription.getCallbackObject());
		}

		public void subscriptionDisconnected(String distributor,
				DynSubscription subscription) {
			Debug.verbose("Jay3DeeTracker["+name+"]", "Tracker got Unsubscribed");
			listener.remove((TrackerListener) subscription.getCallbackObject());
		}

		public boolean subscriptionCallback(String distributor,
				DynSubscription subscription) {
			return false;
		}
	}

	protected class CameraSubscriber implements DynSubscriber{

		private DynSubscription subscription;

		public CameraSubscriber(){
		}

		public void subscribeCamera(String _camera){
			Debug.verbose("Jay3DeeTracker["+name+"]", "Subscribing to get connected to Camera: " + _camera);
			subscription = Env.getEnv().cameraDistributor.create(this, _camera, null);
			subscription.subscribe();
		}

		public void notifyDeleted(){
			Debug.verbose("Jay3DeeTracker["+name+"]", "Unsubscribe from Camera: " + camName);
			subscription.unsubscribe();
		}

		public void publicationConnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeTracker["+name+"]", "Connected to published camera: " + camName);
			camera = (JitGlCameraHandler) subscription.getPublishedObject();
			eventHelper.setActive(camera);
		}

		public void publicationDisonnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeTracker["+name+"]", "Disconnected from recalled camera: " + camName);
			camera = null;
			eventHelper.setActive(null);
		}

	}

	protected class WindowSubscriber implements DynSubscriber{

		private DynSubscription subscription;

		public WindowSubscriber(){
		}

		public void subscribeWindow(String _j3d_context, JitterNotifiable _listener){
			if(subscription != null && subscription.isConnected()){
				Debug.verbose("Jay3DeeTracker["+name+"]", "UnSubscribing to Window: " + subscription.getPublicationIdentifier());
				subscription.unsubscribe();
			}
			Debug.verbose("Jay3DeeTracker["+name+"]", "Subscribing to get connected to Window: " + _j3d_context);
			subscription = Env.getEnv().windowsDistributor.create(this, _j3d_context, _listener);
			subscription.subscribe();
		}

		public void notifyDeleted(){
			Debug.verbose("Jay3DeeTracker["+name+"]", "Unsubscribe from Window: " + j3d_context);
			subscription.unsubscribe();
		}

		public void publicationConnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeTracker["+name+"]", "Connected to published window: " + j3d_context);
			window = (JitWindowHandler) subscription.getPublishedObject();
			window.idlemouse(1);
		}

		public void publicationDisonnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeTracker["+name+"]", "Disconnected from recalled window: " + j3d_context);
			window = null;
		}

	}

	public void notifyDeleted(){
		publisher.notifyDeleted();
		camSubscriber.notifyDeleted();
		winSubscriber.notifyDeleted();
	}

}
