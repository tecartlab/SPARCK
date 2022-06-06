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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import com.cycling74.jitter.JitterEvent;
import com.cycling74.jitter.JitterListener;
import com.cycling74.jitter.JitterNotifiable;
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

public class JitWindowMirror extends JitterMirror{

	private WindowPublisher publisher;
	private JitWindowHandler window;

	private String contextName;

	public JitWindowMirror(Atom[] args) {
		super(args);
		if (args.length < 1)
			bail("Jay3DeeWindow: needs a contextname as argument");
		else{
			contextName = args[0].toString();
		}
		publisher = new WindowPublisher();
		window = new JitWindowHandler(super.connector);
	}

	public void loadbang(){
		publishObject();
	}

	public void j3dcontext(String _context){
		contextName = _context;
		publishObject();
	}

	private void publishObject(){
		try {
			publisher.publishObject(contextName, window);
		} catch (DynException e) {
			bail(e.getMessage());
		}
	}

	public void jitterEvent(String message, Atom[] args){
		if(message.equals("mouseidle") ||
				message.equals("mouseidleout") ||
				message.equals("mouse") ||
				message.equals("mousewheel") ||
				message.equals("pickray")){
			if(publisher.listeners.size() > 0){
				// send it to the listeners
				JitterEvent event = new JitterEvent(contextName, message, args);
				Iterator<JitterNotifiable> i = publisher.listeners.iterator();
				while(i.hasNext()){
					i.next().notify(event);
				}
			}
			// and send it to the jitter dumpout
			messageOutput(message, args);
		} else {
			super.jitterEvent(message, args);
		}
	}

	public void notifyDeleted(){
		publisher.notifyDeleted();
	}

	protected class WindowPublisher implements DynPublisher{

		private DynPublication publication;

		public ArrayList<JitterNotifiable> listeners;

		public WindowPublisher(){
			listeners = new ArrayList<JitterNotifiable>();
		}

		public void publishObject(String _contextname,  JitWindowHandler _window) throws DynException{
			if(publication != null){
				Debug.verbose("Jay3DeeWindow["+contextName+"]", "Recall this window");
				publication.recall();
			}
			Debug.verbose("Jay3DeeWindow["+contextName+"]", "Publish this window");
			publication = Env.getEnv().windowsDistributor.create(this, _contextname, _window);
			publication.publish();
		}

		public void notifyDeleted(){
			Debug.verbose("Jay3DeeWindow["+contextName+"]", "Recall this window");
			publication.recall();
		}

		public void subscriptionConnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeWindow["+contextName+"]", "Window got subscribed");
			listeners.add((JitterNotifiable) subscription.getCallbackObject());
		}

		public void subscriptionDisconnected(String distributor, DynSubscription subscription) {
			listeners.remove((JitterNotifiable) subscription.getCallbackObject());
			Debug.verbose("Jay3DeeWindow["+contextName+"]", "Window got unsubscribed");
		}

		public boolean subscriptionCallback(String distributor, DynSubscription subscription) {
			return false;
		}
	}


}
