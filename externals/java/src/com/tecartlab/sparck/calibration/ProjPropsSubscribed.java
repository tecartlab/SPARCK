package com.tecartlab.sparck.calibration;
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

import java.util.ArrayList;
import java.util.Iterator;

import com.cycling74.max.MaxSystem;

import com.tecartlab.mxj.utils.pattr.*;
import com.tecartlab.mxj.utils.pattr.PattrCallback;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.threedee.Camera;
import com.tecartlab.tools.threedee.Node;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynSubscriber;
import com.tecartlab.utils.dyndist.DynSubscription;

public class ProjPropsSubscribed extends ProjProps{

	private PattrSubscriber subscriber;
	private Listener listener;

	private String editorname;

	public  ProjPropsSubscribed(){
		super();
	}

	public void setEditorName(String _editorName){
		editorname = _editorName;
	}

	/**
	 * Link this properties with Max Properties
	 * @param store the storename
	 */
	public  void registerStore(String store){
		if(subscriber == null)
			subscriber = new PattrSubscriber(store);
		subscriber.init();
	}

	/**
	 * Update Max's linked properties with this properties (this -> max)
	 * Needs to be called if the properties were changed locally (inside java)
	 */
	public  void refresh(){
		if(subscriber != null){
			for(int i = 0; i < properties.length; i++){
				String propertyname = getPropertyName(i);
				subscriber.setSubscriptionProperty(propertyname, properties[i]);
			}
		}
	}

	/**
	 * Register Listener for change events (max -> this)
	 * @param _listener
	 */
	public void registerListener(Listener _listener){
		listener = _listener;
	}

	/**
	 * Tell this properties listener of changes at the properties
	 * Is called if max'properties were changed and this properties were updated
	 */
	private void changeEvent(){
		if(listener != null)
			listener.propertyEvent();
	}

	public boolean isOperational(){
		return subscriber.isOperational();
	}

	/**
	 * If this properties were registered, free them again
	 *
	 * IMPORTANT!!!
	 *
	 */
	public void notifyDeleted(){
		if(subscriber != null)
			subscriber.notifyDeleted();
	}

	private class PattrSubscriber implements DynSubscriber{

		private DynSubscription sbscr;

		private PattrCallback call;

		private String store;

		PattrSubscriber(String _store){
			store = _store;
		}

		public void init(){
			try {
				String getterMethod = "getPropertyValue";
				String setterMethod = "setPropertyFromSubscription";
				Debug.verbose("LocatorEditor [" + editorname + "]: ProjectorProperties", "Subscribing to Pattr: " + store);
				sbscr = PattrSystem.getEnv().createLink(this, store, this, getterMethod, setterMethod);
				sbscr.subscribe();
			} catch (PattrException e) {
				e.printStackTrace();
			}
		}

		/**
		 * This method is called by the PattrPublisher to set the changed
		 * property value
		 *
		 * @param _property 	propertyname
		 * @param _value	propertyvalue
		 */
		private void setPropertyFromSubscription(String _property, float _value){
			int propertyIndex = getIndex(_property);
//			Debugger.verbose("ProjPropsSubscribed", "Setting of property '"+_property+"' '"+_value+"' '"+propertyIndex+"'");
			if(propertyIndex == -1){
				//Debugger.verbose("ProjProrpSubscribed", "Setting of property '"+_property+"' failed, no such property defined");
			} else if(properties[propertyIndex] != _value){
				properties[propertyIndex] = _value;
				changeEvent();
			}
		}

		/**
		 * This method is called by the PattrPublisher to get the specified
		 * property value
		 *
		 * @param _address 	propertyname
		 * @returns value of property. 0 if no property of this name is defined.
		 */
		private float getPropertyValue(String _address){
			int propertyIndex = getIndex(_address);
			if(propertyIndex == -1){
				//Debugger.verbose("ProjProrpSubscribed", "Getting of property '"+_address+"' failed, no such property defined");
			} else{
				return properties[getIndex(_address)];
			}
			return 0.0f;
		}

		/**
		 * This method is called to propagate changes of the properties to the PattrPublisher
		 * @param _address
		 * @param _value
		 */
		protected void setSubscriptionProperty(String _address, float _value){
			if(isOperational())
				call.setAddressValue(_address, _value);
		}

		protected void notifyDeleted(){
			sbscr.unsubscribe();
		}

		public void publicationConnected(String distributor,
				DynSubscription subscription) {
			call = (PattrCallback)sbscr.getPublishedObject();
			Debug.verbose("LocatorEditor [" + editorname + "]: ProjectorProperties", "Subscribed to Pattr: " + store);
			changeEvent();
		}

		public void publicationDisonnected(String distributor,
				DynSubscription subscription) {
			call = null;
			Debug.verbose("LocatorEditor [" + editorname + "]: ProjectorProperties", "Unsubscribed from Pattr: " + store);
		}

		protected boolean isOperational(){
			if(call != null)
				return true;
			return false;
		}

	}

	public interface Listener{
		public void propertyEvent();
	}

}
