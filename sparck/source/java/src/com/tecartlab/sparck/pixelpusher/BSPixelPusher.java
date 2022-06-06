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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;

import com.tecartlab.sparck.ledstrip.LedStrip.SegmentsMngr;
import com.tecartlab.sparck.pixelpusher.Connection.PostPoner;
import com.tecartlab.mxj.utils.Atomizer;
import com.tecartlab.utils.Debug;

import com.cycling74.jitter.JitterMatrix;
import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;
import com.heroicrobot.dropbit.registry.*;
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.PusherCommand;

public class BSPixelPusher extends MaxObject{
	private static int BLUE = 3;
	private static int GREEN = 2;
	private static int RED = 1;
	private static int ALPHA = 0;

	private static int MAX_SCANNERS = 6;
	private static int MAX_LEDSTRIPIDS = 256;

	private static String CMD_PPMENU = "ppMenu";
	private static String CMD_PPCTRL_ID = "ppControlID";
	private static String CMD_PPGROUP_ID = "ppGroupID";

	private Thread myLedSwitchOff = null;

	PixelScanner[] scanners;

	StripMapper stripMapper;

	Connection conx;

	int[] stripMask;
	int stripBitMask = 0;
	String myTitle = "PixelPusher";

	boolean isInit = false;
	boolean isEnabled = false;

	Atomizer myAtomList;

	PixelPusherData pusherData;

	public BSPixelPusher(Atom args[]){
		/*
		if (args.length != 1)
			Debugger.warning("PixelPusher", "needs a drawing context as argument.");
		else{
		}
		*/

//		Debugger.verbose("PixelPusher", "version 0");
		declareInlets(new int[]{DataTypes.ALL, DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});

		stripMask = new int[8];
		resetStripMask();
		conx = new Connection();
		myAtomList = new Atomizer();

		stripMapper = new StripMapper(MAX_LEDSTRIPIDS);
		pusherData = new PixelPusherData();

		scanners = new PixelScanner[MAX_SCANNERS];
		for(int i = 0; i < MAX_SCANNERS; i++){
			scanners[i] = new PixelScanner(stripMapper, pusherData);
		}

	}

	public void loadbang(){
		isInit = true;
		conx.init(this);
	}

	public void notifyDeleted(){
		conx.notifyDeleted();
	}

	public void title(String _title){
		myTitle = _title;
		// set this name to scanners for meaningfull printouts
		for(int i = 0; i < MAX_SCANNERS; i++){
			scanners[i].parentTitle = myTitle;
		}
	}

	public void enable(int _enable){
		isEnabled = (_enable == 1)? true: false;
		if(isInit && !isEnabled){
			if(myLedSwitchOff == null){
				try{
					myLedSwitchOff = new Thread(new SwitchOFF());
					myLedSwitchOff.start();
					myLedSwitchOff.setPriority(Thread.MIN_PRIORITY);
				} catch(Exception e){
					e.printStackTrace(System.out);
				}
			}
		}
	}

	public void renderpass(){
	}

	public void autothrottle(int _enable){
		if(isInit)
			conx.setAutoThrottle((_enable == 1)?true: false);
	}

	public void extradelay(long _extra){
		if(isInit)
			conx.setExtraDelay(_extra);
	}

	public void selection(String pusherID){
		if(isInit){
			if(pusherID != null && !pusherID.equals("off"))
				conx.connect(pusherID);
			else
				isDisconnected();
		}
	}

	/**
	 * This method is called by the connector if a PixelPusher got successfully connected
	 * DO NOT Call any connection - functions from inside this method!!
	 * @param _pusherID
	 */
	protected void gotConnected(PixelPusher _pusherID){
		pusherData.resetSize(_pusherID);
		for(int i = 0; i < scanners.length; i++){
			scanners[i].scanning(stripBitMask);
		}
		Debug.info("PixelPusher <" + myTitle + ">", "... established connection: " + _pusherID.getMacAddress());
		// dont add any  calls to the connector, otherwise strange things will happen
	}

	/**
	 * This method is called by the connector if is a previously connected PixelPusher got disconnected.
	 * DO NOT Call any connection - functions from inside this method!!
	 */
	protected void gotDisconnected(String _pusherID){
		Debug.info("PixelPusher <" + myTitle + ">", "lost connection: " + _pusherID);
		// dont add any  calls to the connector, otherwise strange things will happen
	}

	/**
	 * This method is called when the user chose to disconnect
	 */
	protected void isDisconnected(){
		Debug.info("PixelPusher <" + myTitle + ">", "closed connection");
		conx.disconnect();
		update();
	}

	/**
	 * Userinterface Update
	 */
	public void update(){
		if(isInit){
			// Update Menu to choose PixelPusher from
			ArrayList<String> PusherList = conx.getAvailablePushers();
			outlet(0, CMD_PPMENU, "clear");
			// first add all the PixelPusher available and not used yet
			for(int i = 0; i < PusherList.size(); i++){
				outlet(0, CMD_PPMENU, Atom.newAtom(new String[]{"insert", "0", PusherList.get(i)}));
			}
			// if there is a PixelPusher we are connected with add it now
			if(conx.isPending()){
				outlet(0, CMD_PPMENU, Atom.newAtom(new String[]{"insert", "0", conx.getPusherId()}));
			}
			// add the off selection. this makes sure off is at the top if the selection
			outlet(0, CMD_PPMENU, Atom.newAtom(new String[]{"insert", "0", "off"}));
			// if we are connected to a PixelPusher, select it without sending a message.
			if(conx.isPending()){
				outlet(0, CMD_PPMENU, Atom.newAtom(new String[]{"setsymbol", conx.getPusherId()}));
			}

			// send infos about the connected PixelPusher out to the max console
			if(conx.isOpen()){
				outlet(0, CMD_PPCTRL_ID, conx.myPusher.getControllerOrdinal());
				outlet(0, CMD_PPGROUP_ID, conx.myPusher.getGroupOrdinal());

				post("*************************************");
				post("PixelPusher <" + myTitle + ">" );
				post(" Mac-address     : " + conx.myPusher.getMacAddress());
				post(" Controller_ID   : " + conx.myPusher.getControllerOrdinal());
				post(" Group ID        : " + conx.myPusher.getGroupOrdinal());
				post(" IP Address      : " + conx.myPusher.getIp());
				post(" -------");
				post(" Number of Strips: " + conx.myPusher.getNumberOfStrips());
				post(" Pixels per Strip: " + conx.myPusher.getPixelsPerStrip());
				post(" Max Strips/Packg: " + conx.myPusher.getMaxStripsPerPacket());
				post(" -------");
				post(" Link Speed      : " + conx.myPusher.getLinkSpeed());
				post(" Extra Delay     : " + conx.myPusher.getExtraDelay());
				post(" Update Period   : " + conx.myPusher.getUpdatePeriod());
				post(" -------");
				post(" Power Domain    : " + conx.myPusher.getPowerDomain());
				post(" Power Total     : " + conx.myPusher.getPowerTotal());
				post(" Delta Sequence  : " + conx.myPusher.getDeltaSequence());
				post(" PusherFlags     : " + conx.myPusher.getPusherFlags());
				post(" -------");
				post(" Vendor ID       : " + conx.myPusher.getVendorId());
				post(" Device Type     : " + conx.myPusher.getDeviceType());
				post(" Product ID      : " + conx.myPusher.getProductId());
				post(" Protocol Version: " + conx.myPusher.getProtocolVersion());
				post(" Hardware Rev.   : " + conx.myPusher.getHardwareRevision());
				post(" Software Rev.   : " + conx.myPusher.getSoftwareRevision());
				post("*************************************");

			} else if(!conx.isPending()){
				outlet(0, CMD_PPCTRL_ID, 0);
				outlet(0, CMD_PPGROUP_ID, 0);
			}

			// and finally set the color for the menu to show its connection status
			if(conx.isOpen()){
				output(0, CMD_PPMENU, "bgfillcolor", Atom.newAtom(new float[]{ 0f, 100f/255f, 0f, 1f}));
			}else {
				if(conx.isPending()){
					output(0, CMD_PPMENU, "bgfillcolor", Atom.newAtom(new float[]{100f/255f, 0f, 0f, 1f}));
				} else {
					output(0, CMD_PPMENU, "bgfillcolor", Atom.newAtom(new float[]{96f/255f, 98/255f, 102/255f, 1f}));
				}
			}
		}
	}

	private void output(int _outlet, String _msg1, String _msg2, Atom[] _list){
		myAtomList.clear();
		myAtomList.append(Atom.newAtom(_msg2));
		for(int i = 0; i < _list.length; i++){
			myAtomList.append(_list[i]);
		}
		outlet(0, CMD_PPMENU, myAtomList.getArray());
	}

	public void strip(int _stripID, String _msg, int _msgINTValue){
//		Debugger.info("PixelPusher", "slot = " + _stripID + " has strip = " + _ledStripID);
		if(_msg.equals("id")){
			refreshStripMask(_stripID, _msgINTValue);
			stripMapper.setIDs(_stripID, _msgINTValue);
			pusherData.setStripEnable(_stripID, (_msgINTValue == -1)? false: true);
		} else if(_msg.equals("ledcount")){
			pusherData.setStripLedCount(_stripID, _msgINTValue);
		}
//		Debugger.info("PixelPusher", "stripBitMask = " + stripBitMask);
	}

	public void refreshStripMask(int _index, int _id){
		stripBitMask = 0;
		stripMask[_index] = _id;
		for(int i = 0; i < stripMask.length; i++){
			if(stripMask[i] >= 0)
				stripBitMask += Math.pow(2, stripMask[i] + 1);
		}
	}

	public void resetStripMask(){
		for(int i = 0; i < stripMask.length; i++){
			stripMask[i] = -1;
		}
	}

	public void ref_matrix(Atom[] _matrixList){
		for(int i = 0; i < _matrixList.length; i++){
			scanners[i].setRef(_matrixList[i].toString());
			scanners[i].scanning(stripBitMask);
		}
	}

	public void color_matrix(Atom[] _matrixList){
		if(isInit && conx.myPusher != null){
			// scan colorvalues from textures
			for(int i = 0; i < _matrixList.length; i++){
				scanners[i].detecting(_matrixList[i].toString());
			}
			if(isEnabled){
				// pass found values to pixelpusher strips
				for(int i = 0; i < conx.myPusher.getNumberOfStrips(); i++){
					if(pusherData.hasStrip(i))
						conx.myPusher.setStripValues(i, pusherData.getStrip(i));
					else
						Debug.error("PixelPusher <"+myTitle+">", "no pixels to be set for strip =" + i);

				}
			}
		}


	}

	protected class SwitchOFF implements Runnable{

		public void run(){
			Thread thisThread = Thread.currentThread();

			try {
				thisThread.sleep(100);
			} catch (InterruptedException e){;}

			if(isInit && conx.myPusher != null){
				conx.setAllLedOff();
			}
			myLedSwitchOff = null;
		}
	}

	public void clear(){
	}

}
