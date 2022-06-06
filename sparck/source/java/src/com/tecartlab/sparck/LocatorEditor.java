package com.tecartlab.sparck;

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

import com.cycling74.max.*;
import com.cycling74.jitter.*;

import java.util.*;

import com.tecartlab.tools.math.la.Linef;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.threedee.Camera;
import com.tecartlab.tools.threedee.Node;
import com.tecartlab.tools.threedee.Viewport;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynSubscriber;
import com.tecartlab.utils.dyndist.DynSubscription;
import com.tecartlab.sparck.calibration.CalibObject;
import com.tecartlab.sparck.calibration.NumberCruncher;
import com.tecartlab.sparck.calibration.ProjProps;
import com.tecartlab.sparck.calibration.ProjPropsSubscribed;
import com.tecartlab.jay3dee.*;
import com.tecartlab.jay3dee.tracker.GuiTracker;
import com.tecartlab.jay3dee.tracker.TrackerEvent;
import com.tecartlab.jay3dee.tracker.TrackerListener;

public class LocatorEditor extends MaxObject implements ProjPropsSubscribed.Listener {

	static final int EDIT_M_TARGET = 0;
	static final int EDIT_M_WARP = 1;

	static final int OUTLET_MAIN = 0;
	static final int OUTLET_SKETCH = 1;
	static final int OUTLET_DUMP = 2;

	String editorname;

	String trackername;
	String objectName;
	String propertyName;

	private String[] drawingContexts;

	private TrackerSubscriber trackerSubscriber;
	private ObjectSubscriber objSubscriber;

	GuiTracker tracker;
	ObjectContainer modelObject;
	Camera virtualCamera;
	ProjPropsSubscribed props;
	CalibObject calibObject;
	NumberCruncher crunchy;

	private Callback infoOutlet;

	int editMode = 0;

	public Vector3f textureSize = new Vector3f();

	boolean isEnabled;

	public LocatorEditor(Atom args[])
	{
		virtualCamera = createVirtualCamera();
		calibObject = new CalibObject();
		objSubscriber = new ObjectSubscriber();
		props = new ProjPropsSubscribed();
		crunchy = new NumberCruncher(this, virtualCamera, props, calibObject);
		trackerSubscriber = new TrackerSubscriber();

		if (args.length != 2)
			Debug.warning("LocatorEditor", "needs an editorname and a trackername as arguments. otherwise use 'seteditorname', 'settracker'.");
		else{
			setmsgtitle(args[0].toString());
			settrackername(args[1].toString());
		}

		declareIO(1,3);
		declareAttribute("objectname", null, "setobjectname");
		declareAttribute("camproperties", null, "setcamproperties");
		this.setOutletAssist(OUTLET_SKETCH, "sketch draw data");
		isEnabled = true;
	}

	private Camera createVirtualCamera(){
		Camera virt = new Camera();
		Node parent = new Node();
		Node greatParent = new Node();
		Node greatGreatParent = new Node();
		Node greatGreatGreatParent = new Node();

		greatGreatParent.setParent(greatGreatGreatParent);
		greatParent.setParent(greatGreatParent);
		parent.setParent(greatParent);
		virt.setParent(parent);
		return virt;
	}

	public void editmode(int _mode){
		if(_mode == EDIT_M_TARGET)
			editMode = EDIT_M_TARGET;
		if(_mode == EDIT_M_WARP)
			editMode = EDIT_M_WARP;
	}

	/**
	 * Sets the editorname.
	 * @param _editorname
	 */
	public void setmsgtitle(String _editorname){
		editorname = _editorname;
	}

	/**
	 * Sets the name of the tracker the editor is listening to.
	 * @param _trackerName name of the tracker
	 */
	public void settrackername(String _trackerName){
		trackerSubscriber.subscribeTracker(_trackerName);
	}

	/**
	 * Sets the name of the object the editor applies to.
	 * @param _objectName name of the object
	 */
	public void setobjectname(String _objectName){
		objSubscriber.subscribeObject(_objectName);
		objectName = _objectName;
	}

	/**
	 * Sets the storage name of properties available through the PattrSystem
	 * @param _storeName
	 */
	public void setcamproperties(String _storeName){
		propertyName = _storeName;
		props.setEditorName(editorname);
		props.registerListener(this);
		props.registerStore(_storeName);
	}

	/**
	 * Scales the calibration object drawing. This can be useful if the calibration object appears
	 * Skewed compared to the object. This happens if the Jay3DeeCamera renders to a jit.gl.node object
	 *
	 * @param scale
	 */
	public void scale_calibration(Atom[] scale){
		calibObject.scale(scale);
	}

	public void enable(int _enable){
		isEnabled = (_enable == 0)?false: true;
		calibObject.enable(_enable);
		updateCalibrationObject();
	}

	public void enable(String _context, int _enable){
		calibObject.enable(_context, _enable);
	}

	public void dim(int _textureWidth, int _textureHeight){
		textureSize.setX(_textureWidth);
		textureSize.setY(_textureHeight);
	}

	public void drawto(Atom[] _contexts){
		String[] newContexts = Atom.toString(_contexts);
		// first remove the already set additional drawing contexts
		if(drawingContexts != null){
			calibObject.removeContexts(drawingContexts);
		}
		// then add the new drawing contexts
		if(newContexts != null){
			drawingContexts = newContexts;
			calibObject.addContexts(drawingContexts);
		}
	}

	/**
	 * Loads the calibration object
	 * @param _filepath
	 */
	public void read(String _filepath){
		calibObject.load(_filepath);
		if(isOperational()){
			props.applyTo(virtualCamera);
			updateCalibrationObject();
		}
	}

	/**
	 * Saves the calibration object under the loaded filepath
	 */
	public void save(){
		if(isOperational()){
			updateCalibrationObject();
			calibObject.save();
			calibObject.saveDelaunayMesh();
		}
	}

	/**
	 * Saves the calibration object under the specified filepath
	 * @param _filepath
	 */
	public void saveas(String _filepath){
		if(isOperational()){
			updateCalibrationObject();
			calibObject.saveas(_filepath);
			calibObject.saveDelaunayMesh();
		}
	}

	public void startcrunch(){
		// if there is an origin vertice, make first sure the origin is set properly
		if(calibObject.hasOriginVertice()){
			Vector3f offset = calibObject.getOriginVertice().getModelWorldVertice();
			props.properties[ProjProps.IDX_lookAtTargetPosX] = offset.x();
			props.properties[ProjProps.IDX_lookAtTargetPosY] = offset.y();
			props.properties[ProjProps.IDX_lookAtTargetPosZ] = offset.z();
			props.refresh();
			propertyEvent();
			crunchy.startShiftFinder();
			while(crunchy.isAlive()){;} // very ugly hack to wait until the shiftFinder has done its job.
		}
		crunchy.startCycle();
	}

	public void stopcrunch(){
		crunchy.stopCycle();
	}

	/**
	 * Updates the calibration object.
	 */
	public void updateCalibrationObject(){
		if(isEnabled){
			if(tracker != null && tracker.isOperational()){
				//virtualCamera.viewport = new Viewport(tracker.getCameraHandler().getViewport());
				String context = tracker.getCameraHandler().getContext();
				calibObject.addContext(context);
			}
			if(calibObject.isLoaded() && modelObject.isOperational()){
				if(calibObject.isLinked()){
					calibObject.update(modelObject);
					calibObject.update(virtualCamera);
					calibObject.draw();
				} else {
					calibObject.link(modelObject);
					calibObject.update(virtualCamera);
					calibObject.draw();
				}
			}
		}
	}


	/**
	 * This method is called by the Property-PattrHub if a property value was changed
	 */
	public void propertyEvent() {
		if(props.isOperational()){
			//the virtual cameras properties are set
			props.applyTo(virtualCamera);
		}
		if(isOperational()){
			updateCalibrationObject();
		}
	}


	/**
	 * This message is called by the Tracker if a user interface event has happened.
	 * @param tevent
	 */
	public void trackEvent(TrackerEvent tevent) {
		if(isOperational() && isEnabled){
			//virtualCamera.viewport = new Viewport(tevent.cameraWidth, tevent.cameraHeigth);
			// if there is no object attached or the object is not operational, none of this makes sense...

			if(tevent.mouseInsideCameraCanvas){			//only if mouse is inside camera canvas
				calibObject.resetModified();

				if(calibObject.isModifyModeModel() || calibObject.isModifyModeTarget()){
					if(tevent.keyPressed_Ctrl && tevent.keyPressed_mouse){
						props.properties[props.IDX_screenHeight] = props.properties[props.IDX_screenHeight] * (1.f + (float)tevent.mouseDiffPosY / 100.f);
						props.properties[props.IDX_screenWidth] = props.properties[props.IDX_screenWidth] * (1.f + (float)tevent.mouseDiffPosX / 100.f);
						props.refresh();
						props.applyTo(virtualCamera);
					}

					if(tevent.keyPressed_Shift && tevent.keyPressed_mouse){
						props.properties[props.IDX_lookAtTargetPosX] = props.properties[props.IDX_lookAtTargetPosX]  + (float)tevent.mouseDiffPosX / 100.f;
						props.properties[props.IDX_lookAtTargetPosY] = props.properties[props.IDX_lookAtTargetPosY]  + (float)tevent.mouseDiffPosY / 100.f;
						props.refresh();
						props.applyTo(virtualCamera);
					}

					if(tevent.keyPressed_Alt && tevent.keyPressed_mouse){
						props.properties[props.IDX_azimuthAngle] = props.properties[props.IDX_azimuthAngle] + tevent.mouseDiffPosX;
						props.properties[props.IDX_polarAngle] = props.properties[props.IDX_polarAngle] - tevent.mouseDiffPosY;
						props.refresh();
						props.applyTo(virtualCamera);
					}
				}

				if(tevent.keyDown_Char == tevent.KEY_H && !tevent.keyPressed_Shift){//key Help
					boolean show = calibObject.toggleHelpVisibility();
					if(calibObject.isModifyModeTarget()){
						modelObject.drawer.anything("enable", Atom.newAtom((show)?1:0));
					} else {
						modelObject.drawer.anything("enable", Atom.newAtom(1));
					}
				}
				if(tevent.keyDown_Char == tevent.KEY_H && tevent.keyPressed_Shift){//key Help
					calibObject.changeHelpColor();
				}
				// store calibration file
				if(tevent.keyDown_Char == tevent.KEY_S){
					calibObject.save();
					calibObject.saveDelaunayMesh();
				}
				// Switching Editmodes...
				if(tevent.keyDown_Tab){						// else if tab is pressed
					if(tevent.keyPressed_Shift){
						if(!calibObject.isModifyModeTarget()){
							calibObject.setModifyModeToTarget();
							modelObject.editRenderMode(false);
							editMode = EDIT_M_TARGET;
						} else if(!calibObject.isModifyModeWarp()){
							calibObject.setModifyModeToWarp();
							modelObject.drawer.setRenderMode(modelObject.drawer.RENDER_LINES);
							editMode = EDIT_M_WARP;
						}
					} else {
						if(!calibObject.isModifyModeModel()){
							calibObject.setModifyModeToModel();
							modelObject.editRenderMode(true);
							modelObject.drawer.setRenderMode(modelObject.drawer.RENDER_ALL);
							editMode = EDIT_M_TARGET;
						} else if(!calibObject.isModifyModeTarget()){
							calibObject.setModifyModeToTarget();
							modelObject.editRenderMode(false);
							editMode = EDIT_M_TARGET;
						}
					}
					updateCalibrationObject();
				}

				if(calibObject.isModifyModeWarp()){
					if(calibObject.isEditModeNone()){
						if(calibObject.pick(tevent.mouseNormPosX, tevent.mouseNormPosY)){
							if(tevent.keyPressed_mouse){
								calibObject.doubleSelect();
								calibObject.setEditModeToSelected();
							}
						}

						// create a new warp vertice
						if(tevent.keyDown_Char == tevent.KEY_W){
							calibObject.add(virtualCamera, tevent.mouseNormPosX, tevent.mouseNormPosY);
						}

						//Set frame subdiv
						if(tevent.keyDown_Char == tevent.KEY_F){
							calibObject.setEditModeToValueListening("f");
						}

						//Set frame subdiv
						if(tevent.keyDown_Char == tevent.KEY_D){
							calibObject.setEditModeToValueListening("d");
						}

						//Adjust all warp vertices
						if(tevent.keyDown_Char == tevent.KEY_J){
							calibObject.setEditModeToValueListening("j");
						}
					} else if(calibObject.isEditModeSelected()){
						// set it as a target vertice
						if(tevent.keyDown_Char == tevent.KEY_T)
							calibObject.getSelectedVertice().setTargetType(true);

						//remove vertice
						if(tevent.keyDown_Char == tevent.KEY_X){
							calibObject.remove(false, true);
						}

						//Adjust this vertice
						if(tevent.keyDown_Char == tevent.KEY_J){
							calibObject.setEditModeToValueListening("j");
						}

						//create delaunay lines
						if(tevent.keyDown_Char >= 48  && tevent.keyDown_Char <= 57){//key 0...9
							if(calibObject.isDoubleSelected()){
								calibObject.createLine(tevent.keyDown_Char-48);
							}
						}

						// move vertice with arrowkeys
						if(tevent.keyDown_Char == -9){//key UP pressed
							calibObject.addmove(0, 0.5f / tevent.cameraHeigth);
							calibObject.update(virtualCamera);
						} else if(tevent.keyDown_Char == -10){//key DOWN pressed
							calibObject.addmove(0, - 0.5f / tevent.cameraHeigth);
							calibObject.update(virtualCamera);
						} else if(tevent.keyDown_Char == -11){//key LEFT pressed
							calibObject.addmove(- 0.5f / tevent.cameraWidth, 0);
							calibObject.update(virtualCamera);
						} else if(tevent.keyDown_Char == -12){//key RIGHT pressed
							calibObject.addmove(0.5f / tevent.cameraWidth, 0);
							calibObject.update(virtualCamera);
						}

						//deselect vertice
						if(tevent.keyDown_mouse){
							if(calibObject.pick(tevent.mouseNormPosX, tevent.mouseNormPosY)){
								calibObject.doubleSelect();
							} else {
								calibObject.deselect();
								calibObject.setEditModeToNone();
							}
						}

						//grab vertice with mouse
						if(tevent.keyDown_Char == tevent.KEY_G)
							calibObject.setEditModeToGrab();
					}

					//grabing with mouse
					if(calibObject.isEditModeGrab()){
						//moove vertice with mouse until mouseclick or enter
						calibObject.move(tevent.mouseNormPosX, tevent.mouseNormPosY);
						calibObject.update(virtualCamera);
						if(tevent.keyDown_Return || tevent.keyDown_mouse){
							calibObject.setEditModeToSelected();
						}
					}

					//set frame subdivision
					if(calibObject.isEditModeValueListening("f")){
						if(tevent.keyDown_Char >= 48  && tevent.keyDown_Char <= 57){//key 0...9
							calibObject.setDelaunayMeshFrame(tevent.keyDown_Char-48);
							calibObject.setEditModeToNone();
						} else if (tevent.keyDown_Esc){
							calibObject.setEditModeToNone();
						}
					}

					// set subdivision
					if(calibObject.isEditModeValueListening("d")){
						if(tevent.keyDown_Char >= 48  && tevent.keyDown_Char <= 53){//key 0...5
							calibObject.setDelaunayMeshSubDiv(tevent.keyDown_Char-48);
							calibObject.setEditModeToNone();
						} else if (tevent.keyDown_Esc){
							calibObject.setEditModeToNone();
						}
					}

					//adjusting to vertice
					if(calibObject.isEditModeValueListening("j")){
						if(tevent.keyDown_Char == tevent.KEY_T){
							calibObject.adjustWarpToTarget();
							calibObject.setEditModeToSelected();
						} else if(tevent.keyDown_Char == tevent.KEY_M){
							calibObject.adjustWarpToModel();
							calibObject.setEditModeToSelected();
						} else if (tevent.keyDown_Esc){
							calibObject.setEditModeToSelected();
						}
					}
				}

				if(calibObject.isModifyModeTarget()){
					/*
					 * Setting Target Vertices
					 */
					if(calibObject.isEditModeNone()){
						if(calibObject.pick(tevent.mouseNormPosX, tevent.mouseNormPosY)){
							if(tevent.keyDown_mouse && calibObject.select()){
								calibObject.setEditModeToSelected();
							}
						}

						//Adjust all target vertices
						if(tevent.keyDown_Char == tevent.KEY_J){
							calibObject.setEditModeToValueListening("j");
						}
					} else if(calibObject.isEditModeSelected()){
						//switch render appearance
						if(tevent.keyDown_Char == tevent.KEY_Z){
							if(modelObject.drawer.getRenderMode() == (modelObject.drawer.RENDER_LINES))
								modelObject.drawer.setRenderMode(modelObject.drawer.RENDER_ALL);
							else
								modelObject.drawer.setRenderMode(modelObject.drawer.RENDER_LINES);
						}

						// remove target vertice
						if(tevent.keyDown_Char == tevent.KEY_X){
							calibObject.remove(true, false);
						}

						// add warp vertice to this vertice
						if(tevent.keyDown_Char == tevent.KEY_W){
							calibObject.getSelectedVertice().setWarpType(true);
						}

						//Adjust this vertice
						if(tevent.keyDown_Char == tevent.KEY_J){
							calibObject.setEditModeToValueListening("j");
						}

						//Adjust this vertice
						if(tevent.keyDown_Char == tevent.KEY_U){
							calibObject.setSelectedIDToDisregardID();
						}

						// move vertice with arrowkeys
						if(tevent.keyDown_Char == -9)//key UP pressed
							calibObject.addmove(0, 0.5f / tevent.cameraHeigth);
						if(tevent.keyDown_Char == -10)//key DOWN pressed
							calibObject.addmove(0, - 0.5f / tevent.cameraHeigth);
						if(tevent.keyDown_Char == -11)//key LEFT pressed
							calibObject.addmove(- 0.5f / tevent.cameraWidth, 0);
						if(tevent.keyDown_Char == -12)//key RIGHT pressed
							calibObject.addmove(0.5f / tevent.cameraWidth, 0);

						//swap index
						if(tevent.keyDown_Char >= 48  && tevent.keyDown_Char <= 57)//key 0...9
							calibObject.swapIndex(tevent.keyDown_Char-48);

						// find origin shift
						if(tevent.keyDown_Char == tevent.KEY_O){
							if(calibObject.isSelectedOriginVertice()){
								Vector3f offset = calibObject.getOriginVertice().getModelWorldVertice();
								props.properties[props.IDX_lookAtTargetPosX] = offset.x();
								props.properties[props.IDX_lookAtTargetPosY] = offset.y();
								props.properties[props.IDX_lookAtTargetPosZ] = offset.z();
								props.refresh();
								propertyEvent();
								crunchy.startShiftFinder();
							}
						}

						// deselect vertice
						if(tevent.keyDown_mouse){
							if(calibObject.pick(tevent.mouseNormPosX, tevent.mouseNormPosY)){
								calibObject.select();
							} else {
								calibObject.deselect();
								calibObject.setEditModeToNone();
							}
						}

						// grab vertice with mouse
						if(tevent.keyDown_Char == tevent.KEY_G)
							calibObject.setEditModeToGrab();
					}

					if(calibObject.isEditModeGrab()){
						// move vertice with mouse until mouse click or enter
						calibObject.move(tevent.mouseNormPosX, tevent.mouseNormPosY);
						if(tevent.keyDown_Return || tevent.keyDown_mouse)
							calibObject.setEditModeToSelected();

					}

					//adjust target to
					if(calibObject.isEditModeValueListening("j")){
						if(tevent.keyDown_Char == tevent.KEY_W){
							calibObject.adjustTargetToWarp();
							calibObject.setEditModeToSelected();
						} else if(tevent.keyDown_Char == tevent.KEY_M){
							calibObject.adjustTargetToModel();
							calibObject.setEditModeToNone();
						} else if (tevent.keyDown_Esc){
							calibObject.setEditModeToSelected();
						}
					}

				} else if(calibObject.isModifyModeModel()){
					/*
					 * Creating and removing Target Vertices
					 */
					// purge calibration file
					if(tevent.keyDown_Char == tevent.KEY_P){
						calibObject.setEditModeToValueListening("p");
					}

					//purge calibration
					if(calibObject.isEditModeValueListening("p")){
						if(tevent.keyDown_Char == tevent.KEY_Y){
							calibObject.reset();
							calibObject.setEditModeToNone();
						} else if (tevent.keyDown_Esc){
							calibObject.setEditModeToNone();
						}
					}

					if(calibObject.isEditModeNone()){
						// select vertice
						modelObject.pickVertice(virtualCamera.getViewportRay( tevent.mouseNormPosX, tevent.mouseNormPosY));
						if(tevent.keyDown_mouse){
							modelObject.selectPickVertice();
							calibObject.setEditModeToSelected();
						}
					} else if(calibObject.isEditModeSelected()){

						//create vertice with target and warp
						if(tevent.keyDown_Char == tevent.KEY_V){
							calibObject.add(modelObject, virtualCamera, true, true, false);
						}

						//create vertice with warp
						if(tevent.keyDown_Char == tevent.KEY_W){
							calibObject.add(modelObject, virtualCamera, false, true, false);
						}

						//create vertice with target
						if(tevent.keyDown_Char == tevent.KEY_T){
							calibObject.add(modelObject, virtualCamera, true, false, false);
						}

						//create origin vertice
						if(tevent.keyDown_Char == tevent.KEY_O){
							if(!calibObject.hasOriginVertice()){
								calibObject.add(modelObject, virtualCamera, true, true, true);
								Vector3f offset = modelObject.getSelectedVerticeWorld();
								props.properties[props.IDX_lookAtTargetPosX] = offset.x();
								props.properties[props.IDX_lookAtTargetPosY] = offset.y();
								props.properties[props.IDX_lookAtTargetPosZ] = offset.z();
								props.refresh();
								propertyEvent();
							}
						}

						//remove vertice
						if(tevent.keyDown_Char == tevent.KEY_X){
							calibObject.remove(modelObject.getSelectedVerticeWorld());
						}

						//deselect
						if(modelObject.pickVertice(virtualCamera.getViewportRay( tevent.mouseNormPosX, tevent.mouseNormPosY))){
							//modelObject.unselectPickVertice();
							calibObject.setEditModeToNone();
						}
					}
				}
				if(calibObject.wasModified()){
					calibObject.draw();
					outlet(OUTLET_SKETCH, "editmode", editMode);
					if(editMode == EDIT_M_WARP){
						outlet(OUTLET_SKETCH, "jit_matrix", calibObject.generateGeometry());
						outlet(OUTLET_SKETCH, "draw_mode", "triangles");
					}

				}
			}
		}
	}

	/**
	 * Called my Max if this MaxObject is deleted
	 */
	public void notifyDeleted(){
		stopcrunch();
		trackerSubscriber.notifyDeleted();
		objSubscriber.notifyDeleted();
		props.notifyDeleted();
		calibObject.notifyDeleted();
		crunchy.notifyDeleted();
	}

	public boolean isOperational(){
		if(tracker != null)
			if(tracker.isOperational())
				if(modelObject != null)
					if(modelObject.isOperational())
						if(props.isOperational())
							return true;
						else
							Debug.warning("LocatorEditor [" + editorname + "]", "Projection Porperties are not connected :" + propertyName);
					else
						Debug.warning("LocatorEditor [" + editorname + "]", "Object is not operational : " + objectName);
				else
					Debug.warning("LocatorEditor [" + editorname + "]", "Object is not connected : " + objectName);
			else
				Debug.warning("LocatorEditor [" + editorname + "]", "Tracker is not operational : " + trackername);
		else
			Debug.warning("LocatorEditor [" + editorname + "]", "Tracker is not connected : " + trackername);

		return false;
	}

	protected class TrackerSubscriber implements DynSubscriber, TrackerListener{

		private DynSubscription subscription;

		public TrackerSubscriber(){
		}

		public void subscribeTracker(String _trackerName){
			Debug.verbose("LocatorEditor [" + editorname + "]", "Subscribing to get connected to Tracker: " + _trackerName);
			subscription = Env.getEnv().trackerDistributor.create(this, _trackerName, this);
			trackername = _trackerName;
			subscription.subscribe();
		}

		public void notifyDeleted(){
			Debug.verbose("LocatorEditor [" + editorname + "]", "Unsubscribe from Tracker: " + trackername);
			if(subscription != null)
				subscription.unsubscribe();
			tracker = null;
		}

		public void publicationConnected(String distributor, DynSubscription subscription) {
			Debug.verbose("LocatorEditor [" + editorname + "]", "Connected to published tracker: " + trackername);
			tracker = (GuiTracker) subscription.getPublishedObject();
		}

		public void publicationDisonnected(String distributor, DynSubscription subscription) {
			Debug.verbose("LocatorEditor [" + editorname + "]", "disconnected from recalled tracker: " + trackername);
			tracker = null;
		}

		public void trackerEvent(TrackerEvent tevent) {
			trackEvent(tevent);
		}

	}

	protected class ObjectSubscriber implements DynSubscriber, ObjectContainer.ObjectListener{

		private DynSubscription subscription;

		String subscribedObject;

		public ObjectSubscriber(){
		}

		public void subscribeObject(String _objectName){
			if(subscription != null){
				Debug.verbose("LocatorEditor [" + editorname + "]", "Unsubscribe from Object: " + subscribedObject);
				subscription.unsubscribe();
			}
			subscribedObject = _objectName;
			Debug.verbose("LocatorEditor [" + editorname + "]", "Subscribing to get connected to Object: " + _objectName);
			subscription = Env.getEnv().objectDistributor.create(this, _objectName, this);
			subscription.subscribe();
		}

		public void notifyDeleted(){
			if(subscription != null){
				Debug.verbose("LocatorEditor [" + editorname + "]", "Unsubscribed from object: " + objectName);
				subscription.unsubscribe();
			}
		}

		public void publicationConnected(String distributor, DynSubscription subscription) {
			Debug.verbose("LocatorEditor [" + editorname + "]", "Connected to published object: " + subscribedObject);
			modelObject = (ObjectContainer) subscription.getPublishedObject();
		}

		public void publicationDisonnected(String distributor, DynSubscription subscription) {
			Debug.verbose("LocatorEditor ["  + editorname + "]", "Disconnected from recalled object: " + subscribedObject);
			modelObject = null;
		}


		public void transformationEvent() {
			updateCalibrationObject();
		}
	}


}
