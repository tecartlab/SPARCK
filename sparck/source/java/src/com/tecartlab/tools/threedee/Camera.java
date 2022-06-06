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

package com.tecartlab.tools.threedee;

import com.tecartlab.tools.math.la.Linef;
import com.tecartlab.tools.math.la.Matrix4x4f;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.tools.math.la.Vector4d;

public class Camera extends Node {

	public Frustum frustum;
	public Viewport viewport;

	public Camera(){
		super();
		frustum = new Frustum();
		viewport = new Viewport();
	}

	public Camera(float width, float height){
		super();
		frustum = new Frustum();
		viewport = new Viewport(width, height);
	}

	public Camera clone(){
		Camera clone = new Camera();
		if(parent != null)
			clone.parent	 = parent.clone();
		clone.axis			 = axis.clone();
		clone.position		 = position.clone();
		clone.orientation	 = orientation.clone();
		clone.scale			 = scale.clone();
		clone.frustum		 = frustum.clone();
		clone.viewport		 = viewport.clone();
		clone.createMatrix();
		return clone;
	}

	public Matrix4x4f getProjectionMatrix(){
		return frustum.getProjectionMatrix();
	}

	public Matrix4x4f getViewMatrix(){
		return getInvWorldTransformationMatrix().clone();
	}

	public Matrix4x4f getViewProjectionMatrix(){
		return getViewMatrix().multiply(getProjectionMatrix());
	}

	/**
	 * Returns a new modelViewProjectionMatrix
	 *
	 * @param modelMatrix - the 'model' (or local) transformation matrix
	 * @return
	 */
	public Matrix4x4f getModelViewProjectionMatrix(final Matrix4x4f modelMatrix){
		return modelMatrix.multiplyMake(getViewMatrix()).multiply(getProjectionMatrix());
	}

	/**
	 * General Local to Screen Transformation. This function is meant do be used in
	 * calculation intensive programs, since the modelViewProjectionMatrix has to be
	 * calculated only once.
	 *
	 * @param local coordinates
	 * @param  modelViewProjectionMatrix
	 * @param _viewport custom viewport
	 * @return screen coordinates.
	 * 			the z-value is between -1. (near clipping plane) and 1. (far clipping plane)
	 */
	public Vector3f localToScreen(Vector3f local, Matrix4x4f modelViewProjectionMatrix, Viewport _viewport) {
		return worldToScreen(local, modelViewProjectionMatrix, _viewport);
	}

	/**
	 * General World to Screen Transformation. This function is meant do be used in
	 * calculation intensive programs, since it viewProjectionMatrix has to be
	 * calculated only once.
	 *
	 * @param world world coordinates
	 * @param viewProjectionMatrix
	 * @param _viewport custom viewport
	 * @return screen coordinates.
	 * 			the z-value is between -1. (near clipping plane) and 1. (far clipping plane)
	 */
	public Vector3f worldToScreen(final Vector3f world, final Matrix4x4f viewProjectionMatrix, final Viewport _viewport) {

		// create clip coordinate
		Vector3f clip = viewProjectionMatrix.multiplyMake(world);
		Vector3f ScreenXYZ = new Vector3f();

		//transform clip to screen
		ScreenXYZ.setX((float)(clip.x() + 1.0f) / 2.0f * _viewport.width + _viewport.posX);
		ScreenXYZ.setY((float)(clip.y() + 1.0f) / 2.0f * _viewport.height + _viewport.posY);
		ScreenXYZ.setZ((float)clip.z());

		return ScreenXYZ;
	}

	/**
	 * World to Screen Transformation
	 *
	 * @param world
	 * @return screen coordinates.
	 * 			the z-value is between -1. (near clipping plane) and 1. (far clipping plane)
	 */
	public Vector3f worldToScreen(Vector3f world) {
		return worldToScreen(world, getViewProjectionMatrix(), viewport);
	}

	/**
	 * General Screen to Local Transformation. This function is meant do be used in
	 * calculation intensive programs, since the inverse modelViewProjectionMatrix has to be
	 * calculated only once.
	 *
	 * choosing the Z-value (-1. to 1.) of the provided screen vector allows to set the world-z-position
	 * in between the near (-1.) and far (1.) clipping plane
	 *
	 * @param screen
	 * @param invModelViewProjectionMatrix the inverse of modelViewProjectionMatrix
	 * @param _viewport
	 * @return
	 */
	public Vector3f screenToLocal(Vector3f screen, Matrix4x4f invModelViewProjectionMatrix, Viewport _viewport) {
		return screenToWorld(screen, invModelViewProjectionMatrix, _viewport);
	}

	/**
	 * General Screen to World Transformation. This function is meant do be used in
	 * calculation intensive programs, since the inverse viewProjectionMatrix has to be
	 * calculated only once.
	 *
	 * choosing the Z-value (-1. to 1.) of the provided screen vector allows to set the world-z-position
	 * in between the near (-1.) and far (1.) clipping plane
	 *
	 * @param screen
	 * @param invViewProjectionMatrix the inverse of viewProjectionMatrix
	 * @param _viewport
	 * @return
	 */
	public Vector3f screenToWorld(Vector3f screen, Matrix4x4f invViewProjectionMatrix, Viewport _viewport) {

		//convert from screen to clip
		Vector3f clip = new Vector3f();
		clip.setElement(0, 2.0f * (screen.x() - _viewport.posX) / _viewport.width - 1.0f);
		clip.setElement(1, 2.0f * (screen.y() - _viewport.posY) / _viewport.height - 1.0f);
		clip.setElement(2, screen.z());

		//convert clip to world
		return invViewProjectionMatrix.multiply(clip);
	}


	/**
	 * Screen to World Transformation.
	 *
	 * choosing the Z-value (-1. to 1.) of the provided screen vector allows to set the world-z-position
	 * in between the near (-1.) and far (1.) clipping plane
	 *
	 * @param screen
	 * @return world coordinates
	 */
	public Vector3f screenToWorld(Vector3f screen) {
		return screenToWorld(screen, getViewProjectionMatrix().invert(), viewport);
	}

	/**
	 * Get the viewport ray in world coordinates in form of a linef object.
	 *
	 * @param screenPosX relative from the lower left corner
	 * @param screenPosY relative from the lower left corner
	 * @return linef with the origin at the near clipping plane and pointing to the far end
	 * 		of the frustum
	 */
	public Linef getViewportRay(float screenPosX, float screenPosY){
		return getViewportRay(screenPosX, screenPosY, viewport);
	}

	/**
	 * Get the viewport ray in world coordinates in form of a linef object.
	 *
	 * @param screenPosX relative from the lower left corner
	 * @param screenPosY relative from the lower left corner
	 * @param _viewport provide a custom viewport
	 * @return linef with the origin at the near clipping plane and pointing to the far end
	 * 		of the frustum
	 */
	public Linef getViewportRay(float screenPosX, float screenPosY, Viewport _viewport){
		Vector3f near = screenToWorld(new Vector3f(screenPosX, screenPosY, -1), getViewProjectionMatrix().invert(), _viewport);
		Vector3f far = screenToWorld(new Vector3f(screenPosX, screenPosY, 1), getViewProjectionMatrix().invert(), _viewport);
		Linef ret = new Linef();
		ret.set(near, far);
		return ret;
	}

	public static void main(String[] args) {
		Camera cam = new Camera(100, 100);

		Matrix4x4f frustum = cam.getProjectionMatrix();
		Matrix4x4f viewPro = cam.getViewProjectionMatrix();

		float fov = cam.frustum.getFOV();

		Vector3f world = new Vector3f(0.693359f, -0.048828f, -0.083984f);

		Vector4d clip = new Vector4d(world);

		clip = viewPro.multiply(clip);

		clip.normalize();

		Vector3f CameraXYZ = new Vector3f(clip);

		Vector3f screen = cam.worldToScreen(world);

		//screen.setZ(-1);

		Vector3f wrld = cam.screenToWorld(screen);

		Linef line = cam.getViewportRay((int)screen.x(), (int)screen.y());

		wrld.clone();

	}

}
