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
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.heroicrobot.dropbit.registry.DeviceRegistry;

import com.tecartlab.utils.Debug;

public class BSDeviceRegistry{

	public static DeviceRegistry registry = new DeviceRegistry();

	private static BSDeviceRegistry devices = new BSDeviceRegistry();
	private static boolean init = false;

	private static boolean isPushing = false;

	private ArrayList<PixelPusher> reservedPushers;

	private BSDeviceRegistry(){
		reservedPushers = new ArrayList<PixelPusher>();
	}

	public synchronized static BSDeviceRegistry getRegistry(){
		if(init == false) {
			registry.setLogging(false);
			registry.enableExpiry();
			prepareExitHandler();
			init = true;
		}
		return devices;
	}

	public ArrayList<String> getAvailablePushers(){
		ArrayList<String> list = new ArrayList<String>();
		List<PixelPusher> PusherMap = registry.getPushers();
		for(int i = 0; i < PusherMap.size(); i++){
			if(!reservedPushers.contains(PusherMap.get(i))){
				list.add(getPusherId(PusherMap.get(i)));
			}
		}
		return list;
	}

	public void printAllPushers(){
		List<PixelPusher> PusherMap = registry.getPushers();
		System.out.println("all pushers");
		for(int i = 0; i < PusherMap.size(); i++){
			System.out.println(i + " : " + getPusherId(PusherMap.get(i)));
		}
	}

	public void printAvailablePushers(){
		ArrayList<String> list = getAvailablePushers();
		System.out.println("available pushers");
		for(int i = 0; i < list.size(); i++){
			System.out.println(i + " : " + list.get(i));
		}

	}

	public void expireDevice(String _pusherID){
		if(_pusherID.indexOf(" (") >= 0 )
			registry.expireDevice(_pusherID.substring(0, _pusherID.indexOf(" (")));
	}

	public String getPusherId(PixelPusher _pusher){
		return _pusher.getMacAddress() +
				" ( group = " + _pusher.getGroupOrdinal() +
				" | id = " + _pusher.getControllerOrdinal() +
				" | ip = " + _pusher.getIp().toString() + " )";

	}

	/**
	 * booka a Pusher from the available Pushers.
	 *
	 * returns null if the specified pusher is unavailable
	 */
	public PixelPusher bookPusher(String _pusherID) throws java.lang.IllegalThreadStateException{
		if(!isPushing){
			registry.startPushing();
			isPushing = true;
		}
		List<PixelPusher> PusherMap = registry.getPushers();
		for(int i = 0; i < PusherMap.size(); i++){
			if(!reservedPushers.contains(PusherMap.get(i))){
				if(_pusherID.indexOf(" (") >= 0 && _pusherID.substring(0, _pusherID.indexOf(" (")).equals(PusherMap.get(i).getMacAddress())){
					reservedPushers.add(PusherMap.get(i));
					return PusherMap.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * Checks if Pusher exists.
	 *
	 * @param _pusherID
	 * @return
	 */
	public boolean hasPusher(String _pusherID){
		if(_pusherID != null){
			registry.startPushing();
			List<PixelPusher> PusherMap = registry.getPushers();
			for(int i = 0; i < PusherMap.size(); i++){
				if(!reservedPushers.contains(PusherMap.get(i))){
					if(_pusherID.indexOf(" (") >= 0 && _pusherID.substring(0, _pusherID.indexOf(" (")).equals(PusherMap.get(i).getMacAddress())){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * release the previsouly booked pusher
	 * @param _pusher
	 */
	public void releasePusher(PixelPusher _pusher){
		reservedPushers.remove(_pusher);

	}

	public void addObserver(Observer _observer){
		registry.addObserver(_observer);
	}

	public void removeObserver(Observer _observer){
		registry.deleteObserver(_observer);
	}

	private static void prepareExitHandler () {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run () {

				System.out.println("Shutdown hook running");

				List<Strip> strips = registry.getStrips();
				for (Strip strip : strips) {
					for (int i=0; i<strip.getLength(); i++)
						strip.setPixel(0, i);
				}
				for (int i=0; i<100000; i++)
					Thread.yield();
			}
		}));
	}

}
