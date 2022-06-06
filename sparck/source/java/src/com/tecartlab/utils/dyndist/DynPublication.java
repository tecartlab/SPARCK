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

package com.tecartlab.utils.dyndist;

import java.util.ArrayList;

/**
 * The Dynamic Server for an object
 * @author maybites
 *
 * @param <ObjectType>
 */
public class DynPublication<ObjectType> {

	protected DynPublisher publisher;
	protected String identifier;
	protected ObjectType object;
	private DynDistributor distributor;

	private ArrayList<DynSubscription> subscribers;

	protected DynPublication(DynDistributor _distributor, DynPublisher _publisher, String _identifier, ObjectType _object){
		distributor = _distributor;
		publisher = _publisher;
		identifier = _identifier;
		object = _object;
		subscribers = new ArrayList<DynSubscription>();
	}

	protected void addSubscription(DynSubscription subscription){
		if(!subscribers.contains(subscription)){
			subscribers.add(subscription);
		}
	}

	protected boolean hasSubscriptions(){
		return (subscribers.size() > 0)? true: false;
	}

	protected DynSubscription removeNextSubscription(){
		if(subscribers.size() > 0)
			return subscribers.remove(0);
		return null;
	}

	protected void removeSubscription(DynSubscription link){
		subscribers.remove(link);
	}

	/**
	 * returns the pointed to object
	 * @return
	 */
	protected ObjectType getObject(){
		return object;
	}

	protected String getIdentifier(){
		return identifier;
	}

	protected boolean subscriptionCallback(DynSubscription subscription){
		return publisher.subscriptionCallback(distributor.getIdentifier(), subscription);
	}

	/**
	 * This method needs to be called in order to enable this publication
	 * @return this instance
	 * @throws DynException if the publications identifier has already been taken
	 */
	public DynPublication publish() throws DynException{
		distributor.publish(this);
		return this;
	}

	/**
	 * This method disables and removes this publication from the system
	 */
	public void recall(){
		distributor.recall(this);
	}


}
