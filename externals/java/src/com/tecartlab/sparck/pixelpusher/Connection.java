package com.tecartlab.sparck.pixelpusher;
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
import java.util.Observable;
import java.util.Observer;

import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;

import com.tecartlab.utils.Debug;

public class Connection implements Observer{
	private BSDeviceRegistry devices;

	private Thread myPostPoner = null;
	private Thread myConTester = null;

	private String postponedPusherBooking = null;

	private String myPusherID = null;

	public PixelPusher myPusher = null;

	private BSPixelPusher parent;

	private boolean autoThrottleFlag = false;
	private long extraDelay = 0;

	public Connection(){
		devices = BSDeviceRegistry.getRegistry();
	}

	public void init(BSPixelPusher _parent){
		parent = _parent;
		devices.addObserver(this);
	}

	/**
	 * Checks if a valid connection is active
	 * @return
	 */
	public boolean isOpen(){
		try{
			if(myPusher != null && devices.registry.lastSeen(myPusher) == 0)
				return true;
		} catch (java.lang.NullPointerException e){
			reviver();
		}
		return false;
	}

	/**
	 * Checks if an attempted connection is existing
	 * @return
	 */
	public boolean isPending(){
		if(myPusher != null || myPostPoner != null)
			return true;
		return false;
	}

	private void reviver(){
		if(myPusher != null && myPusherID != null){
//			System.out.println("reviving Connection...");
			devices.releasePusher(myPusher);
			devices.expireDevice(myPusherID);
//			devices.printAllPushers();
			parent.gotDisconnected(myPusherID);
			reconnect(myPusherID);
		}
	}

	public ArrayList<String> getAvailablePushers(){
		return devices.getAvailablePushers();
	}

	public String getPusherId(){
		if(myPusher != null)
			return devices.getPusherId(myPusher);
		else if(myPusherID != null)
			return myPusherID;
		else if(postponedPusherBooking != null)
			return postponedPusherBooking;
		return "off";
	}

	public void disconnect(){
		release();
	}

	public void connect(String _pusherID){
		if(myPusherID == null || !myPusherID.equals(_pusherID)){
			reconnect(_pusherID);
		}
	}

	private void reconnect(String _pusherID){
		release();

		if(devices.hasPusher(_pusherID)){
			try{
				myPusher = devices.bookPusher(_pusherID);
				myPusher.setAutoThrottle(autoThrottleFlag);
				myPusher.setExtraDelay(extraDelay);
				myPusherID = _pusherID;
				startConTester();
				setFirstPixels();

				parent.gotConnected(myPusher);
			} catch (java.lang.IllegalThreadStateException e){
				Debug.error("PixelPusher", "something went wrong inside the device registry: " + e.getStackTrace());
				postponeSelection(_pusherID);
			}

		} else {
			postponeSelection(_pusherID);
		}

		parent.update();
	}

	public void setAllLedOff(){
		setAllPixels(new Pixel());
	}

	private void setAllPixels(Pixel _pixel){
		if(myPusher != null){
			for(int i = 0; i < myPusher.getStrips().size(); i++){
				for(int t = 0; t < myPusher.getStrip(i).getLength(); t++){
					myPusher.getStrip(i).setPixel(_pixel, t);
				}
			}
		}
	}

	private void setFirstPixels(){
		if(myPusher != null){
			for(int i = 0; i < myPusher.getStrips().size(); i++){
				if(myPusher.getStrip(i).getLength() >= 3){
					myPusher.getStrip(i).setPixel(new Pixel((byte)(127), (byte)(0), (byte)(0)), 0);
					myPusher.getStrip(i).setPixel(new Pixel((byte)(0), (byte)(127), (byte)(0)), 1);
					myPusher.getStrip(i).setPixel(new Pixel((byte)(0), (byte)(0), (byte)(127)), 2);
				}
			}
		}
	}

	private void release(){
		setAllPixels(new Pixel((byte)0, (byte)0, (byte)0));
		if(myPusher != null)
			devices.releasePusher(myPusher);
		myPusher = null;
		myPusherID = null;
		postponedPusherBooking = null;
	}

	public void setAutoThrottle(boolean _state){
		autoThrottleFlag = _state;
		if(myPusher != null)
			myPusher.setAutoThrottle(_state);
	}

	public void setExtraDelay(long _delay){
		extraDelay = _delay;
		if(myPusher != null)
			myPusher.setExtraDelay(_delay);
	}

	public void notifyDeleted(){
		release();
		devices.removeObserver(this);
		devices = null;
	}

	private void postponeSelection(String _pusherID){
		postponedPusherBooking = _pusherID;
		if(myPostPoner == null){
			try{
				myPostPoner = new Thread(new PostPoner());
				myPostPoner.start();
				myPostPoner.setPriority(Thread.MIN_PRIORITY);
			} catch(Exception e){
				e.printStackTrace(System.out);
			}
		}
	}

	private void startConTester(){
		if(myConTester == null){
			try{
				myConTester = new Thread(new ConnectionTester());
				myConTester.start();
				myConTester.setPriority(Thread.MIN_PRIORITY);
			} catch(Exception e){
				e.printStackTrace(System.out);
			}
		}
	}

	protected class ConnectionTester implements Runnable{

		public void run(){
			Thread thisThread = Thread.currentThread();
			Debug.info("PixelPusher", "ConnectionTester started...");

			while(myPusherID != null){
				try {
					thisThread.sleep(1000);
				} catch (InterruptedException e){;}

				if(myPusherID != null && !isOpen() && postponedPusherBooking == null){
					Debug.warning("PixelPusher", "got disconnected. try to reestablish connection...");
					reviver();
				}
			}
			myConTester = null;
		}
	}

	protected class PostPoner implements Runnable{

		public void run(){
			Thread thisThread = Thread.currentThread();

			while(postponedPusherBooking != null){
				try {
					thisThread.sleep(500);
				} catch (InterruptedException e){;}

				if(devices != null && devices.hasPusher(postponedPusherBooking)){
					connect(postponedPusherBooking);
					postponedPusherBooking = null;
				}
			}
			myPostPoner = null;
		}
	}

	@Override
	public void update(Observable arg0, Object _updatedDevice) {
//		System.out.println("Registry changed!");
	    if (_updatedDevice != null) {
//	    	System.out.println("Device change: " + _updatedDevice);
	    }
	}

}
