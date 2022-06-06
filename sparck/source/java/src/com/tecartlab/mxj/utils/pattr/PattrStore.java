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

package com.tecartlab.mxj.utils.pattr;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynException;
import com.tecartlab.utils.dyndist.DynPublication;
import com.tecartlab.utils.dyndist.DynPublisher;
import com.tecartlab.utils.dyndist.DynSubscription;

import com.cycling74.max.Callback;

public class PattrStore{

	private String storename;
	private PattrCallback callback;

	private StorePublisher publisher;

	public PattrStore(){
		publisher = new StorePublisher();
	}

	public void init(PattrCallback _callback){
		callback = _callback;
	}

	public void register(String _storename) throws PattrException{
		storename = _storename;
		try{
			publisher.registerStore(this, _storename);
		} catch(DynException e){
			throw new PattrException(e.getMessage());
		}
	}

	public String getStoreName(){
		return storename;
	}

	public void addClient(String clientAddress){
		Debug.verbose("PattrStorage[" + storename + "]", "registered: " + clientAddress);
	}

	public boolean clientEvent(String event, float value){
		publisher.clientEvent(event, value);
		return false;
	}

	/**
	 * is beeing called if the mxj-wrapper is deleted
	 */
	public void notifyDeleted(){
		publisher.notifyDeleted();
	}

	protected class StorePublisher implements DynPublisher, PattrCallback{
		DynPublication publication;
		ArrayList<LinkCallback> connections;

		StorePublisher(){
			connections = new ArrayList<LinkCallback>();
		}

		protected void notifyDeleted(){
			if(publication != null)
				publication.recall();
			connections.clear();
		}

		protected void registerStore(PattrStore store, String _storename) throws DynException{
			if(publication != null){
				publication.recall();
			}
			Debug.verbose("PattrStore["+ storename + "]"," published store");
			publication = PattrSystem.getEnv().registerStore(this, _storename, this);
			publication.publish();
//			Debugger.verbose("PattrStore", "Published Object: " + _storename);
		}

		protected boolean clientEvent(String event, float value){
			for(int i = 0; i < connections.size(); i++){
				connections.get(i).set(event, value);
			}
			return true;
		}

		public void subscriptionConnected(String distributor, DynSubscription subscription) {
			LinkCallback link = (LinkCallback) subscription.getCallbackObject();
			Debug.verbose("PattrStore["+ storename + "]"," connected to subscription");
			connections.add(link);
			dumpAllValues();
		}

		public void subscriptionDisconnected(String distributor, DynSubscription subscription) {
			LinkCallback link = (LinkCallback) subscription.getCallbackObject();
			Debug.verbose("PattrStore["+ storename + "]"," disconnected from subscription");
			connections.remove(link);
		}

		public boolean subscriptionCallback(String distributor, DynSubscription subscription) {
			return true;
		}

		public void dumpAllValues(){
			callback.dumpAllValues();
		}

		public void setAddressValue(String address, float value) {
			clientEvent(address, value);
			callback.setAddressValue(address, value);
		}
	}

}
