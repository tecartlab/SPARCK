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
 * This Interface needs to implement the publisher of a dynamic publication of an object
 *
 * @author maybites
 *
 */
public interface DynPublisher {

	/**
	 * This method is called when a subscription is connected to a published publication
	 *
	 * @param distributor identifies the distributor
	 * @param subscription
	 */
	public void subscriptionConnected(String distributor, DynSubscription subscription);

	/**
	 * This method is called when a subscription is disconnected from a published publication
	 *
	 * @param distributor identifies the distributor
	 * @param c
	 */
	public void subscriptionDisconnected(String distributor, DynSubscription c);

	/**
	 * This method is called if a connected subscribtion's callback() method is called
	 *
	 * @param distributor identifies the distributor
	 * @param subscription
	 * @return
	 */
	public boolean subscriptionCallback(String distributor, DynSubscription subscription);

}
