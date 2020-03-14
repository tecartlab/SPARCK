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

package com.tecartlab.jay3dee;

import java.util.*;

import com.cycling74.jitter.JitterNotifiable;
import com.cycling74.max.MaxSystem;

import com.tecartlab.jay3dee.mirror.JitGlCameraMirror;
import com.tecartlab.jay3dee.mirror.JitGlCameraHandler;
import com.tecartlab.jay3dee.mirror.JitWindowHandler;
import com.tecartlab.jay3dee.model.drawer.ModelDrawer;
import com.tecartlab.jay3dee.tracker.GuiTracker;
import com.tecartlab.jay3dee.tracker.TrackerListener;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynDistributor;

public class Env {

	private static Env theonlyone = new Env();

	public DynDistributor<ObjectContainer, Object> objectDistributor;
	public DynDistributor<ModelContainer, ModelDrawer> modelDistributor;
	public DynDistributor<GuiTracker, TrackerListener> trackerDistributor;
	public DynDistributor<JitWindowHandler, JitterNotifiable> windowsDistributor;

	public DynDistributor<JitGlCameraHandler, Object> cameraDistributor;

	private Env(){
		objectDistributor = new DynDistributor<ObjectContainer, Object>("ObjectDistributor");
		modelDistributor = new DynDistributor<ModelContainer, ModelDrawer>("ModelDistributor");
		trackerDistributor = new DynDistributor<GuiTracker, TrackerListener>("TrackerDistributor");
		windowsDistributor = new DynDistributor<JitWindowHandler, JitterNotifiable>("WindowDistributor");
		cameraDistributor = new DynDistributor<JitGlCameraHandler, Object>("CameraDistributor");
	}

	public static Env getEnv(){
		return theonlyone;
	}

	public static void terminate(){
		theonlyone = null;
		System.gc();
	}


	public void finalize(){
		windowsDistributor.clear();
		objectDistributor.clear();
		modelDistributor.clear();
		trackerDistributor.clear();
		cameraDistributor.clear();

		windowsDistributor = null;
		objectDistributor = null;
		modelDistributor = null;
		trackerDistributor = null;
		cameraDistributor = null;
		System.gc();
		theonlyone = new Env();
	}

}
