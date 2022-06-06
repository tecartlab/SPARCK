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

import java.util.*;

import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynDistributor;
import com.tecartlab.utils.dyndist.DynPublication;
import com.tecartlab.utils.dyndist.DynPublisher;
import com.tecartlab.utils.dyndist.DynSubscriber;
import com.tecartlab.utils.dyndist.DynSubscription;

public class PattrSystem{

	private static PattrSystem theonlyone = new PattrSystem();

	private DynDistributor<Object, LinkCallback> factory;

	private PattrSystem(){
		factory = new DynDistributor<Object, LinkCallback>("PattrHub");
	}

	public static PattrSystem getEnv(){
		return theonlyone;
	}

	/**
	 *
	 * @param registrar
	 * @param storename the pattr storage address
	 * @param listener the listeners instance
	 * @param getMethod the getter method which has to return a float
	 * @param setMethod the setter method which has to expect a float
	 * @return
	 * @throws PattrException if the get or set-methods dont exist
	 */
	public DynSubscription createLink(
			DynSubscriber registrar,
			String storename,
			Object listener,
			String getMethod,
			String setMethod) throws PattrException{

		return factory.create(registrar, storename, new LinkCallback(listener, getMethod, setMethod));
	}

	public void removeLinks(DynSubscriber registrar){
		factory.unsubscribe(registrar);
	}

	protected DynPublication registerStore(DynPublisher store, String storename, PattrCallback pattr){
		return factory.create(store, storename, pattr);
	}

	protected void removeStore(PattrStore store){
		factory.recall(store.getStoreName());
	}



}
