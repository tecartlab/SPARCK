package com.tecartlab.sparck.calibration;
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

import com.cycling74.jitter.JitterObject;
import com.cycling74.max.Atom;
import com.cycling74.max.MaxSystem;

import com.tecartlab.sparck.LocatorEditor;
import com.tecartlab.mxj.utils.Atomizer;
import com.tecartlab.tools.math.la.Matrix4x4f;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.threedee.Camera;
import com.tecartlab.tools.threedee.Viewport;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.Timer;

public class NumberCruncher {

	private final static String MSG_TASK 				= "task";
	private final static String MSG_TASKPROGESS 		= "taskprogress";
	private final static String MSG_TASKTIME			= "tasktime";
	private final static String MSG_PROGESS 			= "progress";
	private final static String MSG_CALIB_MEAN_DIFF 	= "calibmeandiff";
	private final static String MSG_OFFSET_MEAN_DIFF 	= "offsetmeandiff";
	private final static String MSG_VERTICEDISTANCE 	= "verticedistance";
	private final static String MSG_VERTICECLEAR 		= "verticeclear";

	private LocatorEditor maxObject;
	private Camera orig_camera;
	private Camera calc_camera;
	private CalibObject calib;
	private PropsIterator iter;
	private float minimal_dist = Float.MAX_VALUE;
	private float minimal_offset_dist = Float.MAX_VALUE;

	private boolean isRunning = false;
	private String isRunningName = "none";

	private Thread cruncher = null;
	private Thread batch = null;
	private Thread cycler = null;

	private boolean keepCycleRunning;
	private boolean keepBatchRunning;

	/**
	 * global flag to tell if the brute batch improoved the result
	 */
	private boolean bruteBatchImprooved;

	/**
	 * global flag to interrupt brute crunches
	 */
	private boolean crunchInterruptor = false;

	private StringBuffer progressBar;

	private Timer myTimer;

	/**
	 * Constructor
	 * @param _camera
	 * @param _props
	 * @param _calib
	 */
	public NumberCruncher(LocatorEditor _maxobject, Camera _camera, ProjPropsSubscribed _props, CalibObject _calib){
		maxObject = _maxobject;
		orig_camera = _camera;
		calib = _calib;
		iter = new PropsIterator(_props);
		myTimer = new Timer();
		cruncher = new Thread();
		batch = new Thread();
		cycler = new Thread();

		// the higher level nodes transformations are not influenced by the
		// number cruncher and can be therefore calculated beforehand and added to the
		// cameras parent.
		iter.applyStaticsTo(orig_camera);

		calc_camera = _camera.clone();
	}

	private void displayProgress(int progress){
		infoOutlet(MSG_TASKPROGESS, progress);
		infoOutlet(MSG_TASKTIME, myTimer.estimation(progress));
	}

	/**
	 * Call before crunching
	 */
	private boolean startCrunching(String _task){
		//minimal_dist = Float.MAX_VALUE;

		infoOutlet(MSG_TASK, _task);

		iter.applyTo(calc_camera);

		myTimer.start();
		cruncher.setName(_task);
		cruncher.start();
		return true;
	}

	/**
	 * Call after crunching
	 */
	private void stopCrunching(){
		infoOutlet(MSG_VERTICECLEAR, "bang");
		infoOutlet(MSG_TASKPROGESS, 100);
		infoOutlet(MSG_TASKTIME, 0f);
//		infoOutlet(MSG_CALIB_MEAN_DIFF, (float)(Math.sqrt(calibMeanDiff()) * maxObject.textureSize.length()));
		infoOutlet(MSG_CALIB_MEAN_DIFF, calibMeanDiff());
		infoOutlet(MSG_OFFSET_MEAN_DIFF, (float)(Math.sqrt(sceneOffsetMeanDiff()) * maxObject.textureSize.length()));
		//MaxSystem.post( "Found MSG_CALIB_MEAN_DIFF during <" + cruncher.getName() +">:" + calibMeanDiff());

		//MaxSystem.post(" Elapsed Time: " + myTimer.stop());
		iter.applyOriginalTo(orig_camera);

		cruncher.setName(cruncher.getName() + "_done");
	}

	public void notifyDeleted(){
		if(!cruncher.isAlive()){
			cruncher.interrupt();
		}
		if(!batch.isAlive()){
			batch.interrupt();
		}
		if(!cycler.isAlive()){
			cycler.interrupt();
		}
	}

	public  boolean crunchShiftRaw(){
		if(cruncher.getName().equals("Shift Raw" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){
					iter.applyTo(calc_camera);
					minimal_offset_dist = Float.MAX_VALUE;

					//************************************************
					// Everything inside here is up for configuration

					iter.start();
					iter.setupSubIterator_abs(iter.IDX_lookAtShiftX, 100, -.5f, 1.0f);
					iter.setupSubIterator_abs(iter.IDX_lookAtShiftY, 100, -.5f, 1.0f);

					calcScreenOffsetVertice(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_lookAtShiftX)){
						while(iter.nextStep(iter.IDX_lookAtShiftY)){
							calcScreenOffsetVertice();
						}
					}

					iter.applyStoredProperties();

					iter.applyOriginalTo(orig_camera);
					setName(getName() + "_done");
				}
			};
			cruncher.setName("Shift Raw");
			cruncher.start();
			return true;
		}
		return false;
	}

	public  boolean crunchShiftFine(){
		if(cruncher.getName().equals("Shift Fine" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){
					iter.applyTo(calc_camera);
					minimal_offset_dist = Float.MAX_VALUE;

					//************************************************
					// Everything inside here is up for configuration

					iter.start();
					iter.setupSubIterator_range(iter.IDX_lookAtShiftX, 100, .02f, 0.5f);
					iter.setupSubIterator_range(iter.IDX_lookAtShiftY, 100, .02f, 0.5f);

					calcScreenOffsetVertice(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_lookAtShiftX)){
						while(iter.nextStep(iter.IDX_lookAtShiftY)){
							calcScreenOffsetVertice();
						}
					}

					iter.applyStoredProperties();
					iter.applyOriginalTo(orig_camera);
					setName(getName() + "_done");
				}
			};
			cruncher.setName("Shift Fine");
			cruncher.start();
			return true;
		}
		return false;
	}

	public  boolean crunchShiftSupreme(){
		if(cruncher.getName().equals("Shift Supreme" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){
					iter.applyTo(calc_camera);
					minimal_offset_dist = Float.MAX_VALUE;

					//************************************************
					// Everything inside here is up for configuration

					iter.start();
					iter.setupSubIterator_range(iter.IDX_lookAtShiftX, 100, .0004f, 0.5f);
					iter.setupSubIterator_range(iter.IDX_lookAtShiftY, 100, .0004f, 0.5f);

					calcScreenOffsetVertice(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_lookAtShiftX)){
						while(iter.nextStep(iter.IDX_lookAtShiftY)){
							calcScreenOffsetVertice();
						}
					}

					iter.applyStoredProperties();
					iter.applyOriginalTo(orig_camera);
					setName(getName() + "_done");
				}
			};
			cruncher.setName("Shift Supreme");
			cruncher.start();
			return true;
		}
		return false;
	}


	private boolean crunchScreenRaw(final int _offset){
		if(cruncher.getName().equals("Screen Size Raw" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					iter.setupSubIterator_abs(iter.IDX_frustumShiftY, 20 + _offset, -2, 2);
					iter.setupSubIterator_abs(iter.IDX_frustumShiftX, 20 + _offset, -2, 2);
					iter.setupSubIterator_abs(iter.IDX_screenWidth, 20 + _offset, 0, 10);
					iter.setupSubIterator_abs(iter.IDX_screenHeight, 20 + _offset, 0, 10);

					calcCalibVertices(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_frustumShiftY)){
						while(iter.nextStep(iter.IDX_frustumShiftX)){
							while(iter.nextStep(iter.IDX_screenWidth)){
								while(iter.nextStep(iter.IDX_screenHeight)){
									calcCalibVertices();
									if(iter.hasProgress(5))
										displayProgress(iter.getProgress());

								}
							}
						}
					}
					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Screen Size Raw");
		}
		return false;
	}


	private  boolean crunchScreenFine(final int _offset){
		if(cruncher.getName().equals("Screen Size Fine" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration

					iter.start();
					iter.setupSubIterator_range(iter.IDX_frustumShiftY, 20 + _offset, .3f, .5f);
					iter.setupSubIterator_range(iter.IDX_frustumShiftX, 20 + _offset, .3f, .5f);
					iter.setupSubIterator_range(iter.IDX_screenWidth, 20 + _offset, 1, .5f);
					iter.setupSubIterator_range(iter.IDX_screenHeight, 20 + _offset, 1, .5f);

					calcCalibVertices(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_frustumShiftY)){
						while(iter.nextStep(iter.IDX_frustumShiftX)){
							while(iter.nextStep(iter.IDX_screenWidth)){
								while(iter.nextStep(iter.IDX_screenHeight)){
									calcCalibVertices();
									if(iter.hasProgress(5))
										displayProgress(iter.getProgress());

								}
							}
						}
					}
					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Screen Size Fine");
		}
		return false;
	}


	private  boolean crunchScreenSupreme(final int _offset){
		if(cruncher.getName().equals("Screen Size Supreme" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration

					iter.start();
					iter.setupSubIterator_range(iter.IDX_frustumShiftY, 10 + _offset, .02f, .5f);
					iter.setupSubIterator_range(iter.IDX_frustumShiftX, 10 + _offset, .02f, .5f);
					iter.setupSubIterator_range(iter.IDX_screenWidth, 10 + _offset, .1f, .5f);
					iter.setupSubIterator_range(iter.IDX_screenHeight, 10 + _offset, .01f, .5f);

					calcCalibVertices(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_frustumShiftY)){
						while(iter.nextStep(iter.IDX_frustumShiftX)){
							while(iter.nextStep(iter.IDX_screenWidth)){
								while(iter.nextStep(iter.IDX_screenHeight)){
									calcCalibVertices();
									if(iter.hasProgress(5))
										displayProgress(iter.getProgress());

								}
							}
						}
					}
					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Screen Size Supreme");
		}
		return false;
	}


	private  boolean crunchAnglesRaw(final int _offset){
		if(cruncher.getName().equals("Angles Raw" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					iter.setupSubIterator_abs(iter.IDX_polarAngle, 30 + _offset, -90, 90);
					iter.setupSubIterator_abs(iter.IDX_azimuthAngle, 36 + _offset, 0, 360);
					iter.setupSubIterator_abs(iter.IDX_tiltAngle, 30 + _offset, -90, 90);

					calcCalibVertices(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_polarAngle)){
						while(iter.nextStep(iter.IDX_azimuthAngle)){
							while(iter.nextStep(iter.IDX_tiltAngle)){
								calcCalibVertices();
								if(iter.hasProgress(5))
									displayProgress(iter.getProgress());
							}
						}
					}
					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Angles Raw");
		}
		return false;
	}


	private  boolean crunchAnglesFine(final int _offset){
		if(cruncher.getName().equals("Angles Fine" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					iter.setupSubIterator_range(iter.IDX_polarAngle, 24 + _offset, 12, 0.5f);
					iter.setupSubIterator_range(iter.IDX_azimuthAngle, 24 + _offset, 12, 0.5f);
					iter.setupSubIterator_range(iter.IDX_tiltAngle, 24 + _offset, 12, 0.5f);

					calcCalibVertices(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_polarAngle)){
						while(iter.nextStep(iter.IDX_azimuthAngle)){
							while(iter.nextStep(iter.IDX_tiltAngle)){
								calcCalibVertices();
								if(iter.hasProgress(5))
									displayProgress(iter.getProgress());
							}
						}
					}
					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Angles Fine");
		}
		return false;
	}


	private  boolean crunchAnglesSupreme(final int _offset){
		if(cruncher.getName().equals("Angles Supreme" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					iter.setupSubIterator_range(iter.IDX_polarAngle, 20 + _offset, 1, 0.5f);
					iter.setupSubIterator_range(iter.IDX_azimuthAngle, 20 + _offset, 1, 0.5f);
					iter.setupSubIterator_range(iter.IDX_tiltAngle, 20 + _offset, 1, 0.5f);
					calcCalibVertices(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_polarAngle)){
						while(iter.nextStep(iter.IDX_azimuthAngle)){
							while(iter.nextStep(iter.IDX_tiltAngle)){
								calcCalibVertices();
								if(iter.hasProgress(5))
									displayProgress(iter.getProgress());
							}
						}
					}

					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Angles Supreme");
		}
		return false;
	}


	private  boolean crunchOffsetRaw(final int _offset){
		if(cruncher.getName().equals("Offset Raw" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					iter.setupSubIterator_range(iter.IDX_lookAtShiftOffsetX, 100 + _offset, .1f, .5f);
					iter.setupSubIterator_range(iter.IDX_lookAtShiftOffsetY, 100 + _offset, .1f, .5f);

					calcScreenOffsetVertice(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_lookAtShiftOffsetX)){
						while(iter.nextStep(iter.IDX_lookAtShiftOffsetY)){
							calcScreenOffsetVertice();
							if(iter.hasProgress(5))
								displayProgress(iter.getProgress());
						}
					}

					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Offset Raw");
		}
		return false;
	}

	private  boolean crunchOffsetFine(final int _offset){
		if(cruncher.getName().equals("Offset Fine" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					iter.setupSubIterator_range(iter.IDX_lookAtShiftOffsetX, 100 + _offset, .002f, .5f);
					iter.setupSubIterator_range(iter.IDX_lookAtShiftOffsetY, 100 + _offset, .002f, .5f);

					calcScreenOffsetVertice(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_lookAtShiftOffsetX)){
						while(iter.nextStep(iter.IDX_lookAtShiftOffsetY)){
							calcScreenOffsetVertice();
							if(iter.hasProgress(5))
								displayProgress(iter.getProgress());
						}
					}

					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Offset Fine");
		}
		return false;
	}


	private  boolean crunchOffsetSupreme(final int _offset){
		if(cruncher.getName().equals("Offset Supreme" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					iter.setupSubIterator_range(iter.IDX_lookAtShiftOffsetX, 10 + _offset, .00004f, .5f);
					iter.setupSubIterator_range(iter.IDX_lookAtShiftOffsetY, 10 + _offset, .00004f, .5f);

					calcScreenOffsetVertice(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_lookAtShiftOffsetX)){
						while(iter.nextStep(iter.IDX_lookAtShiftOffsetY)){
							calcScreenOffsetVertice();
							if(iter.hasProgress(5))
								displayProgress(iter.getProgress());
						}
					}

					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Offset Supreme");
		}
		return false;
	}


	private  boolean crunchDistanceRaw(final int _offset){
		if(cruncher.getName().equals("Distance Raw" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					iter.setupSubIterator_abs(iter.IDX_lensDistance, 1000 + _offset, 1.f, 100.f);

					calcCalibVertices(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_lensDistance)){
						calcCalibVertices();
						if(iter.hasProgress(5))
							displayProgress(iter.getProgress());
					}

					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Distance Raw");
		}
		return false;
	}

	private  boolean crunchIterationVector(final int _offset, final int noOfSteps){
		if(cruncher.getName().equals("Iteration Vector" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//Debugger.info("Iteration Vector", "start minimal distance = " + minimal_dist);
					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					for(int i = 0; i < iter.storage.length; i++){
						if(i != iter.IDX_lookAtShiftOffsetX && i != iter.IDX_lookAtShiftOffsetY){
							float diff =  iter.startValues[i] - iter.lastMemory[i];
							if(diff != 0.){
								iter.setupSubIterator_abs(i, noOfSteps, iter.startValues[i], diff);
//								Debugger.info("Iteration Vector", "setting index "+ i + " as subIterator with val="+ iter.memory[i] + " and size="+diff);
							}
						}
					}

					calcCalibVertices(); // This makes sure the current settings are at least compared.

					for(int s = 0; s < noOfSteps; s++){
						for(int i = 0; i < iter.storage.length; i++){
							float diff =  iter.startValues[i] - iter.lastMemory[i];
							if(diff != 0.){
								iter.nextStep(i);
							}
						}
						calcCalibVertices();
						if(iter.hasProgress(5))
							displayProgress(iter.getProgress());
					}


					iter.applyStoredProperties();

					//Debugger.info("Iteration Vector", "end minimal distance = " + minimal_dist);

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Iteration Vector");
		}
		return false;
	}


	private  boolean crunchDistanceFine(final int _offset){
		if(cruncher.getName().equals("Distance Fine" + "_done"))
			return true;
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();

					iter.setupSubIterator_range(iter.IDX_lensDistance, 200 + _offset, 0.2f, 0.5f);

					calcCalibVertices(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_lensDistance)){
						calcCalibVertices();
						if(iter.hasProgress(5))
							displayProgress(iter.getProgress());
					}

					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Distance Fine");
		}
		return false;
	}


	private  boolean crunchBrute(final int _offset, final float relRangeFactor, final float absRangeFactor){
		//MaxSystem.post("crunchBrute(): _offset:" + _offset + " relRangeFactor:" + relRangeFactor + " absRangeFactor:" + absRangeFactor);
		if(cruncher.getName().equals("Crunch steps:"+_offset+" factor:"+relRangeFactor + "_done")){
			//MaxSystem.post("crunchBrute done:" + cruncher.getName() );
			cruncher.setName("ready");
			return true;
		}
		if(!cruncher.isAlive()){
			cruncher = new Thread(){
				public void run(){

					//************************************************
					// Everything inside here is up for configuration
					iter.start();


					iter.setupSubIterator_range(iter.IDX_polarAngle, 3 + _offset, 5.f * absRangeFactor, 0.5f);
					iter.setupSubIterator_range(iter.IDX_azimuthAngle, 3 + _offset, 5.f * absRangeFactor, 0.5f);
					iter.setupSubIterator_range(iter.IDX_tiltAngle, 3 + _offset,  5.f * absRangeFactor, 0.5f);
					iter.setupSubIterator_range(iter.IDX_frustumShiftY, 3 + _offset, 1.f * absRangeFactor, .5f);
					iter.setupSubIterator_range(iter.IDX_frustumShiftX, 3 + _offset, 1.f * absRangeFactor, .5f);
					iter.setupSubIterator_rangeRel(iter.IDX_screenWidth, 3 + _offset, relRangeFactor, .5f);
					iter.setupSubIterator_rangeRel(iter.IDX_screenHeight, 3 + _offset, relRangeFactor, .5f);
					iter.setupSubIterator_rangeRel(iter.IDX_lensDistance, 3 + _offset, relRangeFactor, 0.5f);

					calcCalibVertices(); // This makes sure the current settings are at least compared.

					while(iter.nextStep(iter.IDX_polarAngle)){
						while(iter.nextStep(iter.IDX_azimuthAngle)){
							while(iter.nextStep(iter.IDX_tiltAngle)){
								while(iter.nextStep(iter.IDX_frustumShiftY)){
									while(iter.nextStep(iter.IDX_frustumShiftX)){
										while(iter.nextStep(iter.IDX_screenWidth)){
											while(iter.nextStep(iter.IDX_screenHeight)){
												while(iter.nextStep(iter.IDX_lensDistance)){
													calcCalibVertices();
													if(iter.hasProgress(1))
														displayProgress(iter.getProgress());
													if(crunchInterruptor){
														iter.applyStoredProperties();
														stopCrunching();
														return;
													}
												}
											}
										}
									}
								}
							}
						}
					}
					iter.applyStoredProperties();

					//*************************************************

					stopCrunching();

				}
			};
			startCrunching("Crunch steps:"+_offset+" factor:"+relRangeFactor);
		}
		return false;
	}

	public void stopCycle(){
		if(cycler.isAlive()){
			keepCycleRunning = false;
			keepBatchRunning = false;
			crunchInterruptor = true;
			MaxSystem.post("...cycler was stopped");
		}
	}

	public void startCycle(){
		if(!cycler.isAlive()){
			//MaxSystem.post("cycler started ...");
			keepCycleRunning = true;
			minimal_dist = Float.MAX_VALUE;
			cycler = new Thread(){
				public void run(){
					Thread thisThread = Thread.currentThread();

					//first enable all the vertices for calculation
					calib.enableAllVertices();
					boolean useOffset = true;

					int countSubSteps = 0;

					while(keepCycleRunning){
						switch(countSubSteps){
						case 0:
							if(runSmartBatch(useOffset)){
								countSubSteps++;
								//MaxSystem.post("smart batch didnt find any more solutions ...");
							}
							break;
						case 1:
							if(runBruteBatch(0)){
								if(bruteBatchImprooved) {
									countSubSteps = 0; // if it has found a better solution go again for smart batch
									//MaxSystem.post("brute batch 0 did find more solutions ...");
								}else{
									countSubSteps++; // otherwise go for more details
									//MaxSystem.post("brute batch 0 didnt find any more solutions ...");
								}
							}
							break;
						case 2:
							if(runBruteBatch(1)){
								if(bruteBatchImprooved) {
									countSubSteps = 0; // if it has found a better solution go again for smart batch
									//MaxSystem.post("brute batch 1 did find more solutions ...");
								}else{
									countSubSteps++; // otherwise go for more details
									//MaxSystem.post("brute batch 1 didnt find any more solutions ...");
								}
							}
							break;
						case 3:
							//it reaches this point if it cant find any better solutions, now
							// if it still uses offset, switch that of and start again
							if(useOffset){
								useOffset = ! useOffset;
								//MaxSystem.post("switch off offset ...");
								break;
							} else if(!calib.disableNextVertice()){// in this case it has do disregard less important vertices...
								// but if it can't, it has to stop.
								keepCycleRunning = false;
							}
							countSubSteps = 0;
							break;
						}
						try {
							thisThread.sleep(500);
						} catch (InterruptedException e){;}

					}

					calib.enableAllVertices();

					//if cycler got interrupted
					if(batch.isAlive())
						keepBatchRunning = false;
				}
			};
			cycler.start();
		}
	}

	/**
	 * Calling runSmartBatch starts a continuous loop of a special sequence of crunchers.
	 * @return true if the batch has stopped because it cannot find any better solution and false as long it is running
	 */
	private boolean runSmartBatch(final boolean _useOffset){
		if(batch.getName().equals("smart_done"))
			return true;
		if(!batch.isAlive()){
			//MaxSystem.post("Smart batch started ...");
			keepBatchRunning = true;
			batch = new Thread(){
				public void run(){
					Thread thisThread = Thread.currentThread();

					int badRunCounter = 0;
					int badRunCounterExit = 2;
					iter.start();
					iter.memorize(); // store the current settings in memory
					iter.applyTo(calc_camera);
					float minimal_dist_ofLastRun = calibVerticesMeanDiff(false);
					//MaxSystem.post( "Smart run starting with :" + minimal_dist_ofLastRun);

					int countSubSteps = 0;
					boolean firstPass = true;

					while(keepBatchRunning){
						switch(countSubSteps){
						case 0:
							if(_useOffset){
								if(crunchOffsetRaw(badRunCounter))
									countSubSteps++;
							} else {
								countSubSteps++;
							}
							break;
						case 1:
							if(_useOffset){
								if(crunchOffsetFine(badRunCounter))
								countSubSteps++;
							} else {
								countSubSteps++;
							}
							break;
						case 2:
							if(_useOffset){
								if(crunchOffsetSupreme(badRunCounter))
								countSubSteps++;
							} else {
								countSubSteps++;
							}
							break;
						case 3:
							if(firstPass){
								if(crunchAnglesRaw(badRunCounter))
									countSubSteps++;
							} else {
								countSubSteps++;
							}
							break;
						case 4:
							if(crunchAnglesFine(badRunCounter))
								countSubSteps++;
							break;
						case 5:
							if(crunchAnglesSupreme(badRunCounter))
								countSubSteps++;
							break;
						case 6:
							if(firstPass){
								if(crunchScreenRaw(badRunCounter))
									countSubSteps++;
							} else {
								countSubSteps++;
							}
							break;
						case 7:
							if(crunchScreenFine(badRunCounter))
								countSubSteps++;
							break;
						case 8:
							if(crunchScreenSupreme(badRunCounter))
								countSubSteps++;
							break;
						case 9:
							if(firstPass){
								if(crunchDistanceRaw(badRunCounter))
									countSubSteps++;
							} else {
								countSubSteps++;
							}
							break;
						case 10:
							if(crunchDistanceFine(badRunCounter))
								countSubSteps++;
							break;
						case 11:
							//if(crunchIterationVector(badRunCounter, 1000))
								countSubSteps++;
							break;
						case 12:
							//MaxSystem.post("current run: " + minimal_dist);
							if(minimal_dist_ofLastRun > minimal_dist){ //check if this run was better
								// memorize the best result of this run
								iter.memorize();
								minimal_dist_ofLastRun = minimal_dist;
								badRunCounter = 0;
								//MaxSystem.post("found better run: " + minimal_dist_ofLastRun);
							} else { // otherwise
								badRunCounter++;
								//MaxSystem.post("bad run no: " + badRunCounter);
							}
							countSubSteps = 0;
							firstPass = false;
							break;
						}
						try {
							infoOutlet(MSG_PROGESS, 100f / 12f * countSubSteps);
							thisThread.sleep(500);
						} catch (InterruptedException e){;}

						// if it a bad run happen so many times or the batch was stopped
						if(keepBatchRunning == false || badRunCounterExit == badRunCounter){
							// apply the best result of the last runs
							//MaxSystem.post("stay with memorized run: " + minimal_dist_ofLastRun);
							iter.applyMemorizedProperties();
							stopCrunching();
							keepBatchRunning = false;
						}
					}
					batch.setName("smart_done");
				}
			};
			batch.setName("smart");
			batch.start();
		}
		return false;
	}

	/**
	 * Calling runBruteBatch starts a continous loop of a sequence of brute crunchers.
	 * It also sets the flag 'bruteBatchImprooved' to true if has found a better solution
	 * @return true if the batch has finished and false as long it is running
	 */
	private boolean runBruteBatch(final int steps){
		if(batch.getName().equals("brute_done")){
			batch.setName("ready");
			return true;
		}
		if(!batch.isAlive()){
			//MaxSystem.post("BruteForce Crunching started ...");
			keepBatchRunning = true;
			batch = new Thread(){
				public void run(){
					Thread thisThread = Thread.currentThread();
					bruteBatchImprooved = false;

					iter.start();
					iter.memorize(); // store the current settings in memory
					iter.applyTo(calc_camera);
					float minimal_dist_ofLastRun = calibVerticesMeanDiff(false);
					MaxSystem.post( "Brute run starting with :" + minimal_dist_ofLastRun);

					crunchInterruptor = false;
					int countSubSteps = 0;
					while(keepBatchRunning){
						switch(countSubSteps){
						case 0:
							if(crunchBrute(steps, .04f , 0.4f))
								countSubSteps++;
							break;
						case 1:
							if((crunchBrute(steps, .03f, 0.3f)))
								countSubSteps++;
							break;
						case 2:
							if((crunchBrute(steps, .02f, 0.2f)))
								countSubSteps++;
							break;
						case 3:
							if((crunchBrute(steps, .01f, 0.1f)))
								countSubSteps++;
							break;
						case 4:
							if((crunchBrute(steps, .007f, 0.07f)))
								countSubSteps++;
							break;
						case 5:
							if((crunchBrute(steps, .005f, 0.05f)))
								countSubSteps++;
							break;
						case 6:
							countSubSteps = 0;
							keepBatchRunning = false;
							break;
						}
						try {
							infoOutlet(MSG_PROGESS, 100f / 5f * countSubSteps);
							thisThread.sleep(500);
						} catch (InterruptedException e){;}
					}
					// this only applies if the batch was interrupted
					if(cruncher.isAlive())
						crunchInterruptor = true;

					// if there was any improovments, set the global flag
					if(minimal_dist_ofLastRun > minimal_dist)
						bruteBatchImprooved = true;

					batch.setName("brute_done");
				}
			};
			batch.setName("brute");
			batch.start();
		}
		return false;
	}

	public void startShiftFinder(){
		if(!cycler.isAlive()){
			//MaxSystem.post("cycler started ...");
			keepCycleRunning = true;
			minimal_offset_dist = Float.MAX_VALUE;
			cycler = new Thread(){
				public void run(){
					Thread thisThread = Thread.currentThread();
					batch.setName("undefined");

					//first enable all the vertices for calculation
					calib.enableAllVertices();

					int countSubSteps = 0;

					while(keepCycleRunning){
						if(runShiftBatch())
							keepCycleRunning = false;
						try {
							thisThread.sleep(500);
						} catch (InterruptedException e){;}
					}

					//if cycler got interrupted
					if(batch.isAlive())
						keepBatchRunning = false;
				}
			};
			cycler.start();
		}
	}

	public boolean isAlive(){
		return cycler.isAlive();
	}

	private boolean runShiftBatch(){
		if(batch.getName().equals("shift_done"))
			return true;
		if(!batch.isAlive()){
			//MaxSystem.post("Smart batch started ...");
			keepBatchRunning = true;
			batch = new Thread(){
				public void run(){
					Thread thisThread = Thread.currentThread();

					int badRunCounter = 0;
					int badRunCounterExit = 2;
					iter.start();
					iter.memorize(); // store the current settings in memory
					iter.applyTo(calc_camera);
					//MaxSystem.post( "run starting with :" + minimal_dist_ofLastRun);

					int countSubSteps = 0;

					while(keepBatchRunning){
						switch(countSubSteps){
						case 0:
							if(crunchShiftRaw())
								countSubSteps++;
							break;
						case 1:
							if(crunchShiftFine())
								countSubSteps++;
							break;
						case 2:
							if(crunchShiftSupreme())
								countSubSteps++;
							break;
						case 3:
							keepBatchRunning = false;
							break;
						}

						try {
							thisThread.sleep(500);
						} catch (InterruptedException e){;}

					}
					batch.setName("shift_done");
				}
			};
			batch.setName("shift");
			batch.start();
		}
		return false;
	}

	/**
	 * gets the calibrate vertices mean difference
	 * @return
	 */
	private float calibMeanDiff(){
		iter.applyOriginalTo(calc_camera);

		return calibVerticesMeanDiff(true);
	}

	/**
	 * Does all the necessary calculation on calibration vertices.
	 * @return true if a better solution has been found
	 */
	private boolean calcCalibVertices(){
		iter.applyDynamicsTo(calc_camera);

		float calc_dist = calibVerticesMeanDiff(false);

		if(minimal_dist > calc_dist){
			minimal_dist = calc_dist;
			//MaxSystem.post( "Found better result :" + minimal_dist);
			iter.store();
			return true;
		}
		return false;
	}

	/**
	 * Calculates the calibration vertices mean distance from the target
	 * @param display
	 * @return the sum of all squared distances
	 */
	private float calibVerticesMeanDiff(boolean display){
		CalibVertice vertice;
		Matrix4x4f vpm = calc_camera.getViewProjectionMatrix();
		Viewport vp = new Viewport(1.0f, 1.0f);

		float calc_dist = 0;
		float dist = 0;
		for(int i = 0; i < calib.getVerticeCount(); i++){
			vertice = calib.getVertice(i);
			if(vertice.isEnabled && vertice.isTargetType()){
				Vector3f canvas_vertice = calc_camera.worldToScreen(vertice.getModelWorldVertice(), vpm, vp);
				Vector3f canvas_target = vertice.getTargetVertice();
				canvas_vertice.setZ(0);
				dist = canvas_target.distanceSquared(canvas_vertice);

				if(display){
					dist = (float)Math.sqrt(dist)  * maxObject.textureSize.length();
					infoOutlet(MSG_VERTICEDISTANCE, i, dist);
				} else {
					//punish vertices far away
					//dist = dist * dist * dist;
				}
				calc_dist += dist;
			}
		}

		if(display){
			return calc_dist / calib.getVerticeCount();
		}

		return calc_dist;
	}

	/**
	 * Does all the necessary calculation with the scene offset vertice.
	 * @return true if a better solution has been found
	 */
	private boolean calcScreenOffsetVertice(){
		if(calib.hasOriginVertice()){
			iter.applyDynamicsTo(calc_camera);

			float calc_dist = sceneOffsetVerticeMeanDiff();

			if(minimal_offset_dist > calc_dist){
				//MaxSystem.post("SceneOffset Mean: "+calc_dist);
				minimal_offset_dist = calc_dist;
				iter.store();

				return true;
			}
		}
		return false;
	}


	/**
	 * Calculates the scene offset vertice mean distance from the target
	 *
	 * @return the squared distance
	 */
	private float sceneOffsetVerticeMeanDiff(){
		if(calib.hasOriginVertice()){
			CalibVertice vertice;
			Matrix4x4f vpm = calc_camera.getViewProjectionMatrix();
			Viewport vp = new Viewport(1.0f, 1.0f);

			vertice = calib.getOriginVertice();
			Vector3f canvas_vertice = calc_camera.worldToScreen(vertice.getModelWorldVertice(), vpm, vp);
			Vector3f canvas_target = vertice.getTargetVertice();
			canvas_vertice.setZ(0);

			return canvas_target.distanceSquared(canvas_vertice);
		}
		return 0;
	}

	/**
	 * gets the scene offset vertice mean difference
	 * @return
	 */
	private float sceneOffsetMeanDiff(){
		iter.applyOriginalTo(calc_camera);

		return sceneOffsetVerticeMeanDiff();
	}


	/**
	 * Outputs the message and arguments to the info outlet
	 * @param msg
	 * @param args
	 */
	private void infoOutlet(String msg, Atom[] args){
		maxObject.outlet(maxObject.getInfoIdx(), msg, args);
	}

	/**
	 * Outputs the message and argument to the info outlet
	 * @param msg
	 * @param arg
	 */
	private void infoOutlet(String msg, String arg){
		maxObject.outlet(maxObject.getInfoIdx(), msg, arg);
	}

	/**
	 * Outputs the message and argument to the info outlet
	 * @param msg
	 * @param arg
	 */
	private void infoOutlet(String msg, int arg){
		maxObject.outlet(maxObject.getInfoIdx(), msg, arg);
	}

	/**
	 * Outputs the message and argument to the info outlet
	 * @param msg
	 * @param arg
	 */
	private void infoOutlet(String msg, int arg1, float arg2){
		Atomizer array = new Atomizer();
		array.append(Atom.newAtom(msg)).append(Atom.newAtom(arg1)).append(Atom.newAtom(arg2));
		maxObject.outlet(maxObject.getInfoIdx(), array.getArray());
	}

	/**
	 * Outputs the message and argument to the info outlet
	 * @param msg
	 * @param arg
	 */
	private void infoOutlet(String msg, float arg){
		maxObject.outlet(maxObject.getInfoIdx(), msg, arg);
	}

	/**
	 * Outputs the message to the info outlet
	 * @param msg
	 */
	private void infoOutlet(String msg){
		maxObject.outlet(maxObject.getInfoIdx(), msg);
	}

}
