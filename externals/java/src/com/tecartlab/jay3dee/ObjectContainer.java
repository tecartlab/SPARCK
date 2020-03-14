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

import com.cycling74.max.*;
import com.cycling74.jitter.*;

import java.util.*;

import com.tecartlab.jay3dee.*;
import com.tecartlab.jay3dee.model.Vertice;
import com.tecartlab.jay3dee.model.drawer.ModelDrawer;
import com.tecartlab.jay3dee.model.drawer.MaxSketchModelDrawer;
import com.tecartlab.jay3dee.model.drawer.MaxSketchModelMeshDrawer;
import com.tecartlab.tools.*;
import com.tecartlab.tools.math.la.Linef;
import com.tecartlab.tools.math.la.Matrix4x4f;
import com.tecartlab.tools.math.la.Quaternionf;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.threedee.Node;
import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynException;
import com.tecartlab.utils.dyndist.DynPublication;
import com.tecartlab.utils.dyndist.DynPublisher;
import com.tecartlab.utils.dyndist.DynSubscriber;
import com.tecartlab.utils.dyndist.DynSubscription;

/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */
public class ObjectContainer{

	private ModelContainer model;
//	public MaxSketchModelDrawer drawer;
	public MaxSketchModelMeshDrawer drawer;
	String modelname;

	private String objectname;

	public Node objTransformation;

	private ModelSubscriber subscriber;
	private ObjectPublisher publisher;

	public ObjectContainer()
	{
		subscriber = new ModelSubscriber();
		publisher = new ObjectPublisher();
		objTransformation = new Node();

//		drawer = new MaxSketchModelDrawer();
		drawer = new MaxSketchModelMeshDrawer();
		drawer.setLayer(0);
		drawer.setRenderMode(drawer.RENDER_ALL);
	}

	public void init(String _objectname){
		try {
			publisher.publishObject(_objectname, this);
		} catch (DynException e) {
			Debug.error("Jay3DeeObject["+objectname+"]", e.getMessage());
		}
	}

	/**
	 * Forces drawing of object
	 * returns vertex matrix name
	 */
	public String draw(){
		return drawer.drawraw();
	}

	/**
	 * Used by the MaxObject to set the modelname
	 * @param _modelname
	 */
	public void setmodelname(String _modelname){
		subscriber.subscribeModel(_modelname, drawer);
		modelname = _modelname;
	}

	public void editRenderMode(boolean flag){
		drawer.editRenderMode(flag);
	}

	/**
	 * Called by the MaxObject when object is deleted
	 */
	public void notifyDeleted()
	{
		if(publisher != null){
			publisher.notifyDeleted();
			subscriber.notifyDeleted();
			drawer.notifyDeleted();
		}
	}

	/**
	 * Returns the objectname
	 * @return
	 */
	public String getObjectname(){
		return objectname;
	}

	/**
	 * Set the World - Position of the Object
	 * @param _position - Atom Array with x, y, and z float value
	 */
	public void position(Atom[] _position){
		drawer.position(_position);
		objTransformation.setPosition(new Vector3f(_position[0].toFloat(), _position[1].toFloat(), _position[2].toFloat()));
		publisher.transformationEvent();
	}

	/**
	 * Set the World - Quaternion of the Object
	 * @param _quat - Atom Array with x, y, z and w float value
	 */
	public void quat(Atom[] _quat){
		drawer.quat(_quat);
		objTransformation.setOrientation(new Quaternionf(_quat[0].toFloat(), _quat[1].toFloat(), _quat[2].toFloat(), _quat[3].toFloat()));
		publisher.transformationEvent();
	}

	/**
	 * Set the World - Rotation of the Object
	 * @param _quat - Atom Array with Euler Angle float value
	 */
	public void rotatexyz(Atom[] _rotatexyz){
		drawer.rotatexyz(_rotatexyz);
		Quaternionf rotatexyz = new Quaternionf();
		rotatexyz.setEulerAngles(_rotatexyz[0].toFloat(), _rotatexyz[1].toFloat(), _rotatexyz[2].toFloat());
		objTransformation.setOrientation(rotatexyz);
		publisher.transformationEvent();
	}

	/**
	 * Set the World - Sacle of the Object
	 * @param _quat - Atom Array with x, y and z float value
	 */
	public void scale(Atom[] _scale){
		drawer.scale(_scale);
		objTransformation.setScale(new Vector3f(_scale[0].toFloat(), _scale[1].toFloat(), _scale[2].toFloat()));
		publisher.transformationEvent();
	}


	/*******************************************
	 *          Edit Model methods
	 *******************************************/

	/**
	 * pick closest vertice to viewray (ergo: pickray)
	 * @param pickray
	 * @return true if new vertice was picked
	 */
	public boolean pickVertice(Linef pickray){
		if(model != null && pickray != null)
			return model.pickVertice(pickray.transform(objTransformation.getInvWorldTransformationMatrix()));
		return false;
	}

	/**
	 * selects the picked vertice and returns a clone of the vertice (in local coordinate system)
	 * @return t
	 */
	public Vertice selectPickVertice(){
		if(model != null)
			return model.selectPickedVertice();
		return null;
	}

	public void unselectPickVertice(){
		model.unselectPickedVertice();
	}

	/**
	 * returns the selectedVertices - in the local coordinate system.
	 * @return
	 */
	public Vertice[] getSelectedVerticesLocal(){
		Vertice[] vertices = null;
		if(model != null)
			vertices = model.getSelectedVertices();
		return vertices;
	}

	/**
	 * Returns the vertice (in local coord. system) with the lowest
	 * 	index number if more than one vertices are selected.
	 * @return null if no vertice has been selected
	 */
	public Vertice getSelectedVerticeLocal(){
		Vertice[] vertices = getSelectedVerticesLocal();
		if(vertices != null && vertices.length > 0)
			return vertices[0];
		return null;
	}

	/**
	 * Returns the vertice (in local coord. system) with the lowest
	 * 	index number if more than one vertices are selected.
	 * @return null if no vertice has been selected
	 */
	public Vertice getSelectedVerticeWorld(){
		Vertice[] vertices = getSelectedVerticesLocal();
		if(vertices != null && vertices.length > 0)
			return transformToWorld(vertices[0]);
		return null;
	}

	/**
	 * Returns the model vertice in local coordinate system at the provided index
	 * @param index
	 * @return
	 */
	public Vertice getModelVerticeLocal(int index){
		if(model != null)
			return model.getModelVertice(index);
		return null;
	}

	/**
	 * Returns the model vertice in world coordinate system at the provided index
	 * @param index
	 * @return
	 */
	public Vertice getModelVerticeWorld(int index){
		if(model != null)
			return transformToWorld(model.getModelVertice(index));
		return null;
	}

	/**
	 * Returns the nearest model vertice in local coordinate system at the provided point
	 * @param _pos in local coordinate system
	 * @return vertice in local coordinate system
	 */
	public Vertice getModelVerticeLocal(Vector3f _pos){
		if(model != null){
			return model.getModelVertice(_pos);
		}
		return null;
	}

	/**
	 * Returns the nearest model vertice in local coordinate system at the provided point
	 * @param _pos in local coordinate system
	 * @return vertice in local coordinate system
	 */
	public Vertice getModelVerticeWorld(Vector3f _pos){
		if(model != null){
			return transformToWorld(model.getModelVertice(_pos));
		}
		return null;
	}


	/*******************************************
	 *          Transform methods
	 *******************************************/

	/**
	 * Transforms the provided vertice into world coordinate system
	 * @param _vertice
	 * @return
	 */
	public Vertice transformToWorld(Vertice _vertice){
		return (Vertice)_vertice.transform(objTransformation.getWorldTransformationMatrix());
	}


	/*******************************************
	 *         Organizational methods
	 *******************************************/

	/**
	 * this method takes all the undefined messages and passes them to the faces-sketch.
	 *
	 * @param message
	 * @param args
	 */
	public void anything(String message, Atom[] args){
		drawer.anything(message, args);
		publisher.transformationEvent();
	}

	/**
	 * Called by the ModelSubscriber if a Model subscribed was published or unpublished
	 * @param _model
	 */
	void subscriptionEvent(ModelContainer _model){
		if(_model != null){
			publisher.transformationEvent();
			if(model == null){
				model = _model;
				drawer.initialize(_model.model);
				drawer.draw();
			}
		}else{
			drawer.reset();
			model = null;
		}
	}


	/**
	 * Checks if the Object fulfills all internal conditions to be operational. It is STRONGLY suggested
	 * to call the method before any operations are done.
	 * @return
	 */
	public boolean isOperational(){
		if(model != null)
			if(model.isOperational())
				return true;
			else
				Debug.warning("Jay3DeeObject["+objectname+"]", "Model is not operational: " + modelname);
		else
			Debug.warning("Jay3DeeObject["+objectname+"]", "Model is not connected: " + modelname);

		return false;
	}

	protected class ObjectPublisher implements DynPublisher{

		private DynPublication publication;
		private ArrayList<ObjectContainer.ObjectListener> objectListeners;

		public ObjectPublisher(){
			objectListeners = new ArrayList<ObjectContainer.ObjectListener>();
		}

		public void publishObject(String _objectname, ObjectContainer object) throws DynException{
			if(!_objectname.equals(objectname)){
				if(publication != null){
					Debug.verbose("Jay3DeeObject["+objectname+"]", "Recalling this object");
					publication.recall();
				}
				Debug.verbose("Jay3DeeObject["+_objectname+"]", "Publish this object");
				publication = Env.getEnv().objectDistributor.create(this, _objectname, object);
				objectname = _objectname;
				publication.publish();
			}
		}

		public void notifyDeleted(){
			Debug.verbose("Jay3DeeObject["+objectname+"]", "Recall this object");
			if(publication != null)
				publication.recall();
			if(objectListeners != null)
				objectListeners.clear();
		}

		public void subscriptionConnected(String distributor,
				DynSubscription subscription) {
			ObjectContainer.ObjectListener listener = (ObjectContainer.ObjectListener)subscription.getCallbackObject();
			if(listener != null)
				objectListeners.add(listener);
			Debug.verbose("Jay3DeeObject["+objectname+"]", "Object got Subscribed");
		}

		public void subscriptionDisconnected(String distributor,
				DynSubscription c) {
			ObjectContainer.ObjectListener listener = (ObjectContainer.ObjectListener)c.getCallbackObject();
			if(listener != null)
				objectListeners.remove(listener);
			Debug.verbose("Jay3DeeObject["+objectname+"]", "Object got Unsubscribed");
		}

		/**
		 * Called to tell all the subscribers of this object that a transformation event has occured
		 */
		public void transformationEvent(){
			Iterator<ObjectContainer.ObjectListener> i =  objectListeners.iterator();
			while(i.hasNext())
				i.next().transformationEvent();
		}

		public boolean subscriptionCallback(String distributor,
				DynSubscription subscription) {
			return false;
		}
	}

	protected class ModelSubscriber implements DynSubscriber{

		private DynSubscription subscription;
		private String subscribedModelName;

		public ModelSubscriber(){
		}

		public void subscribeModel(String _modelname, ModelDrawer _drawer){
			if(subscription != null){
				Debug.verbose("Jay3DeeObject["+objectname+"]", "Unsubscribe from model: " + subscribedModelName);
				subscription.unsubscribe();
			}
			subscribedModelName = _modelname;
			Debug.verbose("Jay3DeeObject["+objectname+"]", "Subscribe to model: " + _modelname);
			subscription = Env.getEnv().modelDistributor.create(this, _modelname, _drawer);
			subscription.subscribe();
		}

		public void notifyDeleted(){
			Debug.verbose("Jay3DeeObject["+objectname+"]", "Unsubscribe from Model: " + subscribedModelName);
			if(subscription != null){
				subscription.unsubscribe();
			}
		}

		public void publicationConnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeObject["+objectname+"]", "Connected to published model: " + subscribedModelName);
			subscriptionEvent((ModelContainer) subscription.getPublishedObject());
		}

		public void publicationDisonnected(String distributor, DynSubscription subscription) {
			Debug.verbose("Jay3DeeObject["+objectname+"]", "Disconnected from recalled model: " + subscribedModelName);
			subscriptionEvent(null);
		}

	}

	public interface ObjectListener{

		/**
		 * Method called by the publisher of this object to the implementing object subscribers to
		 * inform about a transformational event
		 */
		public void transformationEvent();
	}
}
