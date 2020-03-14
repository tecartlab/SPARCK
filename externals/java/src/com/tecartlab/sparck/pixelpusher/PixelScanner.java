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
import java.util.ListIterator;
import java.util.Timer;

import com.cycling74.jitter.JitterMatrix;
import com.cycling74.max.Atom;

import com.tecartlab.utils.Debug;

public class PixelScanner {
	private static int BLUE = 3;
	private static int GREEN = 2;
	private static int RED = 1;
	private static int ALPHA = 0;

	private int[] refImageVector;
	private int[] refCopyVector;
	private int refWidth;
	private int refHeight;

	private int colorWidth;
	private int colorHeight;

	private int[] offset;
	private int[] refColorVector;


	int koord;

	private String myMatrixName;

	private StripMapper myMapper;
	private PixelPusherData thePusherData;

	private ArrayList<ScanLineStore> myScanLines;

	String parentTitle = "";

	public PixelScanner(StripMapper _myMapper, PixelPusherData _pusherData){
		offset = new int[]{0, 0};
		myMapper = _myMapper;
		thePusherData = _pusherData;
		myScanLines = new ArrayList<ScanLineStore>();
	}

	private void clearScanLines(){
		for(int i = 0; i < myScanLines.size(); i++){
			myScanLines.get(i).clear();
		}
		myScanLines.clear();
	}

	public void setRef(String _matrixName){
		myMatrixName = _matrixName;
		JitterMatrix _matrix = new JitterMatrix(_matrixName);

		int[] dim = _matrix.getDim();

		if(refImageVector == null || (dim[0] != refWidth && dim[1] != refHeight)){
			refWidth = dim[0];
			refHeight = dim[1];

			refImageVector = new int[refWidth * refHeight * 4];
			refCopyVector = new int[refWidth * 4];
		}

		for (int i=0; i<refHeight; i++) {
			offset[1] = i;

			_matrix.copyVectorToArray(0, offset, refImageVector, refWidth * 4, i * refWidth * 4);

		}
		_matrix.freePeer();
	}

	public void detecting(String _matrixName){
		myMatrixName = _matrixName;
		JitterMatrix _matrix = new JitterMatrix(_matrixName);

		int[] dim = _matrix.getDim();
		int scanLine = 0;

		ListIterator<PusherPixel> list;
		PusherPixel pixl;
		thePusherData.startLedCounter();

		int posX;
		byte _red, _green, _blue;

		if(refColorVector == null || (dim[0] != colorWidth && dim[1] != colorHeight)){
			colorWidth = dim[0];
			colorHeight = dim[1];

			refColorVector = new int[colorWidth * 4];
		}

		for (int i=0; i < myScanLines.size(); i++) {

			if(scanLine != myScanLines.get(i).getMyScanline(colorHeight)){
				scanLine = myScanLines.get(i).getMyScanline(colorHeight);
				offset[1] = scanLine;
				_matrix.copyVectorToArray(0, offset, refColorVector, colorWidth * 4, 0);
			}

			list = myScanLines.get(i).listIterator();
			while(list.hasNext()){
				pixl = list.next();
				posX = pixl.getPosX(colorWidth) * 4;
				_red = (byte)(refColorVector[posX + RED] / 2);
				_green = (byte)(refColorVector[posX + GREEN] / 2);
				_blue = (byte)(refColorVector[posX + BLUE] / 2);
				thePusherData.setPixel(pixl.stripID, pixl.pos, _red, _green, _blue);
//				Debugger.info("PixelPusher", "detecting pixel at pos = [" + x + ", " + scanLine + "] with red = " + _red + " | green = " + _green + " | blue = " + _blue);
			}

		}
		_matrix.freePeer();
	}

	public void scanning(int _stripMask){
		if(refImageVector != null){
			clearScanLines();

			int y,x, k, addCounter, dropCounter;
			int[] color = new int[4];
			int myLedID, myPos;
			ScanLineStore myStore;
			addCounter = 0;
			dropCounter = 0;

			for (y=0;y<refHeight;y++) {
				myStore = new ScanLineStore(y, refWidth, refHeight);
				for (x=0;x<refWidth;x++) {
					getRefColor(color, x, y);
//					Debugger.info("PixelPusher_img", "found pixel at pos = [" + i + ", " + j + "] with val = " + result[BLUE] + " | " + result[GREEN] + " | " + result[RED]);
					myLedID = (255 - color[RED]);
					if(myLedID != 255){
						int myBitID = (int) Math.pow(2, myLedID + 1);
						if((myBitID & _stripMask) > 0){
							myPos = (color[BLUE] * 100 + color[GREEN]);
							if(thePusherData.hasPixel(myMapper.getStripID(myLedID), myPos)){
								myStore.addPixel(x, y, myMapper.getStripID(myLedID), myPos);
								addCounter++;
//								Debugger.info("PixelPusher", "scanning stored pixel at pos = [" + x + ", " + y + "] with myLedId = " + myLedID + " | myPos = " + myPos);
							} else {
								dropCounter++;
//								Debugger.info("PixelPusher", "scanning dropped pixel at pos = [" + x + ", " + y + "] with myLedId = " + myLedID + " | myPos = " + myPos);
							}
						}
					}
				}
				if(myStore.isTouched())
					myScanLines.add(myStore);

			}
			thePusherData.resetStrips();
			Debug.verbose("PixelPusher ["+parentTitle+"]", "within <"+myMatrixName+"> matrix found " + myScanLines.size() + " scanlines. Added a total of " + addCounter + " Leds and dropped a total of " + dropCounter+ " Leds.");
		}
	}

	public void getRefColor(int[] _color, int x, int y){
		koord = y * refWidth * 4 + x * 4;
		_color[0] = refImageVector[koord];
		_color[1] = refImageVector[koord+1];
		_color[2] = refImageVector[koord+2];
		_color[3] = refImageVector[koord+3];
	}

}
