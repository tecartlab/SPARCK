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

package com.tecartlab.jay3dee.tracker;

import com.tecartlab.jay3dee.mirror.JitGlCameraHandler;
import com.tecartlab.tools.math.la.Linef;
import com.tecartlab.utils.Debug;

import com.cycling74.jitter.JitterEvent;
import com.cycling74.max.Atom;

public class TrackerEvent {

	private JitterEvent jevent;
	private MouseEvent mevent;
	private int[] window;

	/**
	 * jit.gl.camera Viewport coordinates which set
	 * the left, bottom, width, and height of the viewport
	 */
	public float[] viewport;

	public boolean isActive = false;

	public boolean mouseInsideWindow = false;
	public boolean mouseInsideCameraCanvas = false;

	public int windowWidth, windowHeigth;

	public int cameraWidth, cameraHeigth;

	/**
	 * Camera canvas coordinates relative to the screen
	 */
	public int cameraScreenRight, cameraScreenLeft;
	/**
	 * Camera canvas coordinates relative to the screen
	 */
	public int cameraScreenTop, cameraScreenBottom;

	/**
	 * Camera canvas coordinates relative to the window
	 */
	public int cameraWindowRight, cameraWindowLeft;
	/**
	 * Camera canvas coordinates relative to the window
	 */
	public int cameraWindowTop, cameraWindowBottom;

	public boolean mouseIdle = false;
	public boolean mousewheel = false;

	/**
	 * Mouse coordinates relative to the Window
	 */
	public int mouseWindowPosX, mouseWindowPosY;

	/**
	 * Mouse coordinates relative to the Screen
	 */
	public int mouseScreenPosX, mouseScreenPosY;

	/**
	 * Mouse coordinates relative to the Camera canvas
	 */
	public int mouseCameraPosX, mouseCameraPosY;

	public int mouseDiffPosX, mouseDiffPosY;

	/**
	 * Normalized (0....1) Mouse coordinates relative to the Camera canvas, but
	 * with reference from the lower left corner
	 */
	public float mouseNormPosX, mouseNormPosY;

	private boolean keyMouseButton = false;
	public 	boolean keyPressed_mouse = false;
	public 	boolean keyUp_mouse = false;
	public 	boolean keyDown_mouse = false;

	private boolean keyCmd = false;
	public 	boolean keyPressed_Cmd = false;
	public 	boolean keyUp_Cmd = false;
	public 	boolean keyDown_Cmd = false;

	private boolean keyAlt = false;
	public boolean keyPressed_Alt = false;
	public boolean keyUp_Alt = false;
	public boolean keyDown_Alt = false;

	private boolean keyCtrl = false;
	public boolean keyPressed_Ctrl = false;
	public boolean keyUp_Ctrl = false;
	public boolean keyDown_Ctrl = false;

	private boolean keyShift = false;
	public boolean keyPressed_Shift = false;
	public boolean keyUp_Shift = false;
	public boolean keyDown_Shift = false;

	private boolean keyCapslock = false;
	public boolean keyPressed_Capslock = false;
	public boolean keyUp_Capslock = false;
	public boolean keyDown_Capslock = false;

	public boolean keyPressed_Spacebar = false;
	public boolean keyUp_Spacebar = false;
	public boolean keyDown_Spacebar = false;

	public boolean keyPressed_Esc = false;
	public boolean keyUp_Esc = false;
	public boolean keyDown_Esc = false;

	public boolean keyPressed_Return = false;
	public boolean keyUp_Return = false;
	public boolean keyDown_Return = false;

	public boolean keyPressed_Tab = false;
	public boolean keyUp_Tab = false;
	public boolean keyDown_Tab = false;

	public boolean keyPressed_Delete = false;
	public boolean keyUp_Delete = false;
	public boolean keyDown_Delete = false;

	public int 		keyPressed_Function = 0;
	public int	 	keyUp_Function = 0;
	public int	 	keyDown_Function = 0;

	public int 		keyPressed_Char = 0;
	public int 		keyUp_Char = 0;
	public int 		keyDown_Char = 0;

	public boolean keyChange_special = false;

	public float wheelDiffX = 0f;
	public float wheelDiffY = 0f;

	private JitGlCameraHandler camera;

	KeyEvent myKeyEvent = null;

	boolean justActivated = false;

	protected TrackerEvent(){
	}

	protected void update(MouseEvent _mevent, JitterEvent _jevent, int[] _window){
		if(isActive){
			resetKeys();
			setMouseEvent(_mevent);
			setJitterEvent(_jevent);
			setWindow(_window);
			if(camera.hasChanged() || justActivated){
				updateCameraData();
				justActivated = false;
			}
		}
	}

	public boolean update(KeyEvent _kevent){
		if(isActive && isInCamera()){
			resetKeys();
			myKeyEvent = _kevent;
			setKeyEvent();
			return true;
		}
		return false;
	}

	protected void setActive(JitGlCameraHandler _camera){
		camera = _camera;
		isActive = (camera != null)? true: false;
		justActivated = isActive;
	}

	private void setWindow(int[] _window){
		window = _window;
		mouseInsideWindow = isInWindow(window);

		windowWidth = window[2]-window[0];
		windowHeigth = window[3]-window[1];
	}

	private void setMouseEvent(MouseEvent _mevent){
		if(_mevent !=null){
			mevent = _mevent;
			mouseScreenPosX = mevent.screenPosX;
			mouseScreenPosY = mevent.screenPosY;
		}
	}

	private void resetKeys(){
		keyUp_mouse = false;
		keyDown_mouse = false;

		keyUp_Cmd = false;
		keyDown_Cmd = false;

		keyUp_Alt = false;
		keyDown_Alt = false;

		keyUp_Ctrl = false;
		keyDown_Ctrl = false;

		keyUp_Shift = false;
		keyDown_Shift = false;

		keyUp_Capslock = false;
		keyDown_Capslock = false;

		keyUp_Spacebar = false;
		keyDown_Spacebar = false;

		keyUp_Esc = false;
		keyDown_Esc = false;

		keyUp_Return = false;
		keyDown_Return = false;

		keyUp_Tab = false;
		keyDown_Tab = false;

		keyUp_Delete = false;
		keyDown_Delete = false;

		keyUp_Function = 0;
		keyDown_Function = 0;

		keyUp_Char = 0;
		keyDown_Char = 0;

		keyChange_special = false;
	}

	private void setKeyEvent(){
		if(myKeyEvent != null){
			if(myKeyEvent.key == -2){
				keyUp_Spacebar = (!myKeyEvent.pressed)? true: false;
				keyDown_Spacebar = (myKeyEvent.pressed)? true: false;
				keyPressed_Spacebar = (myKeyEvent.pressed)? true: false;
				setSpecialKey(keyUp_Spacebar || keyDown_Spacebar);
			}else if(myKeyEvent.key == -3){
				keyUp_Esc = (!myKeyEvent.pressed)? true: false;
				keyDown_Esc = (myKeyEvent.pressed)? true: false;
				keyPressed_Esc = (myKeyEvent.pressed)? true: false;
				setSpecialKey(keyUp_Esc || keyDown_Esc);
			}else if(myKeyEvent.key == -4){
				keyUp_Return = (!myKeyEvent.pressed)? true: false;
				keyDown_Return = (myKeyEvent.pressed)? true: false;
				keyPressed_Return = (myKeyEvent.pressed)? true: false;
				setSpecialKey(keyUp_Return || keyDown_Return);
			}else if(myKeyEvent.key == -5){
				keyUp_Tab = (!myKeyEvent.pressed)? true: false;
				keyDown_Tab = (myKeyEvent.pressed)? true: false;
				keyPressed_Tab = (myKeyEvent.pressed)? true: false;
				setSpecialKey(keyUp_Tab || keyDown_Tab);
			}else if(myKeyEvent.key == -7){
				keyUp_Delete = (!myKeyEvent.pressed)? true: false;
				keyDown_Delete = (myKeyEvent.pressed)? true: false;
				keyPressed_Delete = (myKeyEvent.pressed)? true: false;
				setSpecialKey(keyUp_Delete || keyDown_Delete);
			}else if(myKeyEvent.key <= -17 && -29 < myKeyEvent.key){
				keyUp_Function = (!myKeyEvent.pressed)? (myKeyEvent.key + 16) * -1:0;
				keyDown_Function = (myKeyEvent.pressed)? (myKeyEvent.key + 16) * -1:0;
				keyPressed_Function = (myKeyEvent.pressed)? (myKeyEvent.key + 16) * -1:0;
				setSpecialKey(keyUp_Function != 0 || keyDown_Function != 0);
			}else{
				keyUp_Char = (!myKeyEvent.pressed)? myKeyEvent.key: 0;
				keyDown_Char = (myKeyEvent.pressed)? myKeyEvent.key: 0;
				keyPressed_Char = (myKeyEvent.pressed)? myKeyEvent.key: 0;
			}
			myKeyEvent = null;
		}
	}

	private void setSpecialKey(boolean change){
		if(change)
			keyChange_special = change;
	}

	private void setJitterEvent(JitterEvent _jevent){
		jevent = _jevent;
		mouseIdle = false;
		mousewheel = false;

		if(jevent.getEventName().equals("mouse") ||
				jevent.getEventName().equals("mouseidle") ||
				jevent.getEventName().equals("mouseidleout")){
			mouseIdle = true;
		} else if(jevent.getEventName().equals("mousewheel")){
			mousewheel = true;
		}
		parseJitterEvent(jevent.getArgs());
	}

	private void parseJitterEvent(Atom[] args){
		if(args.length >= 8){
			mouseDiffPosX = args[0].getInt() - mouseWindowPosX;
			mouseDiffPosY = args[1].getInt() - mouseWindowPosY;

			mouseWindowPosX  = args[0].getInt();
			mouseWindowPosY = args[1].getInt();

			mouseCameraPosX = mouseWindowPosX - cameraWindowLeft;
			mouseCameraPosY = mouseWindowPosY - cameraWindowTop;

			mouseNormPosX = (float)mouseCameraPosX / (float)cameraWidth;
			mouseNormPosY = (float)(cameraHeigth - mouseCameraPosY) / (float)cameraHeigth;

			keyMouseButton = (args[2].getInt() == 1)? true: false;
			keyUp_mouse = (keyPressed_mouse && !keyMouseButton)? true: false;
			keyDown_mouse = (!keyPressed_mouse && keyMouseButton)? true: false;
			keyPressed_mouse = keyMouseButton;

			keyCmd = (args[3].getInt() == 1)? true: false;
			keyUp_Cmd = (keyPressed_Cmd && !keyCmd)? true: false;
			keyDown_Cmd = (!keyPressed_Cmd && keyCmd)? true: false;
			keyPressed_Cmd = keyDown_Cmd;
			setSpecialKey(keyUp_Cmd || keyDown_Tab);

			keyShift = (args[4].getInt() == 1)? true: false;
			keyUp_Shift = (keyPressed_Shift && !keyShift)? true: false;
			keyDown_Shift = (!keyPressed_Shift && keyShift)? true: false;
			keyPressed_Shift = keyShift;
			setSpecialKey(keyUp_Shift || keyDown_Shift);

			keyCapslock = (args[5].getInt() == 1)? true: false;
			keyUp_Capslock = (keyPressed_Capslock && !keyCapslock)? true: false;
			keyDown_Capslock = (!keyPressed_Capslock && keyCapslock)? true: false;
			keyPressed_Capslock = keyCapslock;
			setSpecialKey(keyUp_Capslock || keyDown_Capslock);

			keyAlt = (args[6].getInt() == 1)? true: false;
			keyUp_Alt = (keyPressed_Alt && !keyAlt)? true: false;
			keyDown_Alt = (!keyPressed_Alt && keyAlt)? true: false;
			keyPressed_Alt = keyAlt;
			setSpecialKey(keyUp_Alt || keyDown_Alt);

			keyCtrl = (args[7].getInt() == 1)? true: false;
			keyUp_Ctrl = (keyPressed_Ctrl && !keyCtrl)? true: false;
			keyDown_Ctrl = (!keyPressed_Ctrl && keyCtrl)? true: false;
			keyPressed_Ctrl = keyCtrl;
			setSpecialKey(keyUp_Ctrl || keyDown_Ctrl);

		}
		if(args.length == 10){
			wheelDiffX = args[8].getFloat();
			wheelDiffY = args[9].getFloat();
		}
	}

	private void updateCameraData(){
		viewport = camera.getViewport();

		cameraWindowLeft = (int)(viewport[0] * windowWidth);
		cameraWindowBottom = windowHeigth - (int)(viewport[1] * window[3]);
		cameraWindowRight = cameraWindowLeft + (int)(viewport[2] * windowWidth);
		cameraWindowTop = cameraWindowBottom - (int)(viewport[3] * windowHeigth);

		cameraScreenLeft = window[0] + cameraWindowLeft;
		cameraScreenRight = window[0] + cameraWindowRight;
		cameraScreenBottom = window[3] - cameraWindowTop;
		cameraScreenTop = window[3] - cameraWindowBottom;

		cameraWidth =  cameraScreenRight - cameraScreenLeft;
		cameraHeigth =  cameraScreenBottom - cameraScreenTop;

		mouseInsideCameraCanvas = isInCamera();
	}

	/**
	 * Get the ViewPortRay of the current Mouseposition.
	 * @return Linef of the ray if the mouse is inside the camera-canvas, otherwise null
	 */
	public Linef getViewPortRay(){
		if(mouseInsideCameraCanvas){
			float[] ray = camera.getViewportRay(mouseWindowPosX, mouseWindowPosY);
			if(ray != null)
				return new Linef(ray);
		}
		return null;
	}

	/**
	 * Get the ViewPortRay of the given window-coordinates.
	 * @return Linef of the ray if the coordinate is inside the camera-canvas, otherwise null
	 */
	public Linef getViewPortRay(int posX, int posY){
		float[] ray = camera.getViewportRay(posX, posY);
		if(ray != null)
			return new Linef(ray);
		return null;
	}

	/**
	 * checks if the given window-coordinates lie within the camera canvas
	 * @param posX
	 * @param posY
	 * @return true if this is the case
	 */
	public boolean isInsideCameraCanvas(int posX, int posY){
		if(cameraWindowLeft < posX && posX < cameraWindowRight &&
			cameraWindowTop < posY && posY < cameraWindowBottom)
			return true;
		return false;
	}

	private boolean isInWindow(int[] rect){
		if(mevent != null && (rect[0] < mevent.screenPosX && mevent.screenPosX < rect[2]) &&
				(rect[1] < mevent.screenPosY && mevent.screenPosY < rect[3]))
			return true;
		return false;
	}

	private boolean isInCamera(){
		if(mevent != null)
			if((cameraScreenLeft < mevent.screenPosX && mevent.screenPosX < cameraScreenRight) &&
				(cameraScreenTop < mevent.screenPosY && mevent.screenPosY < cameraScreenBottom))
				return true;
		return false;
	}

	public static final int KEY_PLUS = 43;
	public static final int KEY_MINUS = 45;
	public static final int KEY_STAR = 42;
	public static final int KEY_SLASH = 47;
	public static final int KEY_DOT = 46;
	public static final int KEY_COMMA = 44;

	public static final int KEY_0 = 48;
	public static final int KEY_1 = 49;
	public static final int KEY_2 = 50;
	public static final int KEY_3 = 51;
	public static final int KEY_4 = 52;
	public static final int KEY_5 = 53;
	public static final int KEY_6 = 54;
	public static final int KEY_7 = 55;
	public static final int KEY_8 = 56;
	public static final int KEY_9 = 57;

	public static final int KEY_A = 97;
	public static final int KEY_B = 98;
	public static final int KEY_C = 99;
	public static final int KEY_D = 100;
	public static final int KEY_E = 101;
	public static final int KEY_F = 102;
	public static final int KEY_G = 103;
	public static final int KEY_H = 104;
	public static final int KEY_I = 105;
	public static final int KEY_J = 106;
	public static final int KEY_K = 107;
	public static final int KEY_L = 108;
	public static final int KEY_M = 109;
	public static final int KEY_N = 110;
	public static final int KEY_O = 111;
	public static final int KEY_P = 112;
	public static final int KEY_Q = 113;
	public static final int KEY_R = 114;
	public static final int KEY_S = 115;
	public static final int KEY_T = 116;
	public static final int KEY_U = 117;
	public static final int KEY_V = 118;
	public static final int KEY_W = 119;
	public static final int KEY_X = 120;
	public static final int KEY_Y = 121;
	public static final int KEY_Z = 122;

}
