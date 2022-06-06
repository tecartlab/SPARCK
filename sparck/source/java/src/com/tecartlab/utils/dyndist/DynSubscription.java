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

/**
 * The Dynamic Subscription connection to a Dynamic Publisher of an object
 * @author maybites
 *
 * @param <ObjectType>
 * @param <CallbackType>
 */
public class DynSubscription<ObjectType, CallbackType>{

	protected DynPublication publication;
	protected DynSubscriber subscriber;
	protected String identifier;
	protected CallbackType callback;
	protected DynDistributor factory;

	protected DynSubscription(DynDistributor _factory, DynSubscriber _subscriber, String _identifier, CallbackType _callback){
		factory = _factory;
		subscriber = _subscriber;
		identifier = _identifier;
		callback = _callback;
	}

	/**
	 * Subscribe and try to connect this Subscription.
	 *
	 * Careful: Calling this method does NOT guarantee to have a valid connection to a publication.
	 * It only means, that the request to subscribe was registered and it tries to connect if there
	 * is a matching publication.
	 *
	 * VERY Careful: If a valid connection can be established this method calls directly the
	 * Subscribers publicationConnected() method and returns once that method returns.
	 *
	 * @return this instance if a successful connection to a publication could be established, otherwise null.
	 */
	public DynSubscription subscribe(){
		return (factory.subscribe(this))? this: null;
	}

	/**
	 * This method disables and removes this subscription from the system
	 */
	public void unsubscribe(){
		factory.unsubscribe(this);
	}

	protected boolean equalsIdentifier(DynPublication _publication){
		return (identifier.equals(_publication.getIdentifier()))? true: false;
	}

	protected void set(DynPublication _publication){
		publication = _publication;
	}

	/**
	 * This method returns the Publisher's identifier. It returns this name even if there is no connection
	 * and therefore no publisher of this name available.
	 * @return
	 */
	public String getPublicationIdentifier(){
		return identifier;
	}

	/**
	 * returns true if there is a connection to a publication
	 *
	 * @return false if there is no connection
	 */
	public boolean isConnected(){
		return(publication != null)? true: false;
	}

	/**
	 * Gets the Instance of the publishers Object.
	 *
	 * Carefull: A successful subscription is only established once the subscribers publicationConnected()
	 * method is called. To test if a subscription is actually connected to a publication please use
	 * the isConnecet() method.
	 *
	 * @return null if no publication was set or if there is no connection
	 */
	public ObjectType getPublishedObject(){
		if(isConnected())
			return (ObjectType)publication.getObject();
		return null;
	}

	/**
	 * Gets the Callback Object
	 *
	 * @return null if no callback object was set
	 */
	public CallbackType getCallbackObject(){
		return callback;
	}

	/**
	 * Calls the Publishers subscriptionCallback-method. This is useful if the Publisher
	 * should be forced to call the callback object to do whatever.
	 *
	 * ATTENTION: This method should under no circumstances be called by the Publisher!!!
	 *
	 * @param _s the subscribers instance (to safeguard maluse)
	 * @return true if refresh was successful;
	 */
	public boolean callback(DynSubscriber _s){
		if(_s == subscriber)
			if(isConnected())
				return publication.subscriptionCallback(this);
		return false;
	}

}
