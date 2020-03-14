package com.tecartlab.sparck.calib;
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

import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

import com.tecartlab.tools.math.la.Matrix4x4d;
import com.tecartlab.tools.math.la.Matrix4x4f;
import com.tecartlab.tools.threedee.Frustum;

public class Intrinsics {

	Mat cameraMatrix;
	Size imageSize;
	Size sensorSize;
	Point fov;
	double focalLength, aspectRatio;
	Point principalPoint = new Point();

	public double fx, fy, cx, cy;

	public Intrinsics(){

	}

    public void setup(float focalLength, Size imageSize, Size sensorSize, Point principalPoint) {
        double focalPixels = (focalLength / sensorSize.width) * imageSize.width;
        double fx = focalPixels; // focal length in pixels on x
        double fy = focalPixels;  // focal length in pixels on y
        double cx = imageSize.width * principalPoint.x;  // image center in pixels on x
        double cy = imageSize.height * principalPoint.y;  // image center in pixels on y
        Mat cameraMatrix = Mat.eye(3, 3, CvType.CV_32FC1);
        cameraMatrix.put(0, 0, fx);
        cameraMatrix.put(1, 1, fx);
        cameraMatrix.put(0, 2, cx);
        cameraMatrix.put(1, 2, cy);
        setup(cameraMatrix, imageSize, sensorSize);
    }

    public void setup(Mat cameraMatrix, Size imageSize, Size sensorSize) {
        this.cameraMatrix = cameraMatrix;
        this.imageSize = imageSize;
        this.sensorSize = sensorSize;
        updateValues();
    }

    public void updateValues() {
    	double[] fovx = new double[1];
       	double[] fovy = new double[1];
       	double[] focal = new double[1];
       	double[] aspect = new double[1];
        Calib3d.calibrationMatrixValues(cameraMatrix,
                                imageSize,
                                sensorSize.width, sensorSize.height,
                                fovx,
                                fovy,
                                focal,
                                principalPoint, // sets principalPoint in mm
                                aspect);
        fov = new Point(fovx[0], fovy[0]);
        focalLength = focal[0];
        aspectRatio = aspect[0];
        fx = cameraMatrix.get(0, 0)[0];
        fy = cameraMatrix.get(1, 1)[0];
        cx = principalPoint.x;
        cy = principalPoint.y;
   }

    public void setImageSize(Size imgSize) {
        imageSize = imgSize;
    }

    public Mat getCameraMatrix() {
        return cameraMatrix;
    }

    public Size getImageSize() {
        return imageSize;
    }

    Size getSensorSize() {
        return sensorSize;
    }

    Point getFov() {
        return fov;
    }

    double getFocalLength() {
        return focalLength;
    }

    double getAspectRatio() {
        return aspectRatio;
    }

    Point getPrincipalPoint() {
        return principalPoint;
    }

    public Frustum getFrustum(float nearDist, float farDist){
        float w = (float)imageSize.width;
        float h = (float)imageSize.height;
        float fx = (float)cameraMatrix.get(0, 0)[0];
        float fy = (float)cameraMatrix.get(1, 1)[0];
        float cx = (float)principalPoint.x;
        float cy = (float)principalPoint.y;

        return new Frustum(false, nearDist * (-cx) / fx, nearDist * (w - cx) / fx,
                nearDist * (cy - h) / fy, nearDist * (cy) / fy,
                nearDist, farDist);
    }

    public Matrix4x4d getProjectionMatrix(double nearDist, double farDist) {
        double w = imageSize.width;
        double h = imageSize.height;
        double fx = cameraMatrix.get(0, 0)[0];
        double fy = cameraMatrix.get(1, 1)[0];
        double cx = principalPoint.x;
        double cy = principalPoint.y;

        Matrix4x4d pMatrix = new Matrix4x4d();
        pMatrix.setPerspectiveMatrix(
                                  nearDist * (-cx) / fx, nearDist * (w - cx) / fx,
                                  nearDist * (cy) / fy, nearDist * (cy - h) / fy,
                                  nearDist, farDist);
        return pMatrix;
    }

    public Matrix4x4f getProjectionMatrix(float nearDist, float farDist) {
        float w = (float)imageSize.width;
        float h = (float)imageSize.height;
        float fx = (float)cameraMatrix.get(0, 0)[0];
        float fy = (float)cameraMatrix.get(1, 1)[0];
        float cx = (float)principalPoint.x;
        float cy = (float)principalPoint.y;

        Matrix4x4f pMatrix = new Matrix4x4f();
        pMatrix.setPerspectiveMatrix(
                                  nearDist * (-cx) / fx, nearDist * (w - cx) / fx,
                                  nearDist * (cy) / fy, nearDist * (cy - h) / fy,
                                  nearDist, farDist);
        return pMatrix;
    }
}
