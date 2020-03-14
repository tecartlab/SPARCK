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

package com.tecartlab.sparck;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

public class LookAt extends MaxObject{

	public LookAt(Atom args[]) {
		declareInlets(new int[] { DataTypes.ALL });
		declareOutlets(new int[] { DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
		createInfoOutlet(true);
	}

	float[] mat = new float[16];

	float eyeX = 0.f;
	float eyeY = 0.f;
	float eyeZ = 1.f;

	float atX = 0.f;
	float atY = 0.f;
	float atZ = 0.f;

	float upX = 0.f;
	float upY = 0.f;
	float upZ = 1.f;

	int direction = 0;
	int hasChanged = 0;

	public void bang(){
		if(hasChanged == 1)
			calc();
		hasChanged = 0;
	}

	public void eye(float _args1, float _args2, float _args3){
		eyeX = _args1;
		eyeY = _args2;
		eyeZ = _args3;
		hasChanged = 1;
	}

	public void at(float _args1, float _args2, float _args3){
		atX = _args1;
		atY = _args2;
		atZ = _args3;
		hasChanged = 1;
	}

	public void up(int _sel){
		if(_sel == 0){
			upX = 1.f;
			upY = 0.f;
			upZ = 0.f;
		}else if(_sel == 1){
			upX = 0.f;
			upY = 1.f;
			upZ = 0.f;
		} else if(_sel == 2){
			upX = 0.f;
			upY = 0.f;
			upZ = 1.f;
		}else if(_sel == 3){
			upX = -1.f;
			upY = 0.f;
			upZ = 0.f;
		}else if(_sel == 4){
			upX = 0.f;
			upY = -1.f;
			upZ = 0.f;
		} else if(_sel == 5){
			upX = 0.f;
			upY = 0.f;
			upZ = -1.f;
		}
		hasChanged = 1;
		outlet(2, "bang");
	}

	public void dir(int _val){
		direction = _val;
		hasChanged = 1;
		outlet(2, "bang");
	}

	public void calc(){
		// zaxis = normal(At - Eye);

		float zaxisX = atX - eyeX;
		float zaxisY = atY - eyeY;
		float zaxisZ = atZ - eyeZ;

		if(direction == 1){
			zaxisX = eyeX - atX;
			zaxisY = eyeY - atY;
			zaxisZ = eyeZ - atZ;
		}

		float zAxisLength = (float) Math.sqrt(zaxisX * zaxisX + zaxisY * zaxisY + zaxisZ * zaxisZ);

		outlet(1, "adapt", zAxisLength);

		zaxisX = zaxisX / zAxisLength;
		zaxisY = zaxisY / zAxisLength;
		zaxisZ = zaxisZ / zAxisLength;

		//xaxis = normal(cross(Up, zaxis))

		float xaxisX = upY * zaxisZ - upZ * zaxisY;
		float xaxisY = upZ * zaxisX - upX * zaxisZ;
		float xaxisZ = upX * zaxisY - upY * zaxisX;

		float xAxisLength = (float) Math.sqrt(xaxisX * xaxisX + xaxisY * xaxisY + xaxisZ * xaxisZ);

		xaxisX /= xAxisLength;
		xaxisY /= xAxisLength;
		xaxisZ /= xAxisLength;

	 	//post(" " + xaxisX + " " + xaxisY + " " + xaxisZ + " \n");

		//yaxis = cross(zaxis, xaxis)

		float yaxisX =  zaxisY * xaxisZ - zaxisZ * xaxisY;
		float yaxisY =  zaxisZ * xaxisX - zaxisX * xaxisZ;
		float yaxisZ =  zaxisX * xaxisY - zaxisY * xaxisX;

		float yAxisLength = (float) Math.sqrt(yaxisX * yaxisX + yaxisY * yaxisY + yaxisZ * yaxisZ);

		yaxisX /= yAxisLength;
		yaxisY /= yAxisLength;
		yaxisZ /= yAxisLength;

		//float dotX = xaxisX * eyeX + xaxisY * eyeY + xaxisZ * eyeZ;
		//float dotY = yaxisX * eyeX + yaxisY * eyeY + yaxisZ * eyeZ;
		//float dotZ = zaxisX * eyeX + zaxisY * eyeY + zaxisZ * eyeZ;

		/*
	 	xaxis.x           yaxis.x           zaxis.x          0
	 	xaxis.y           yaxis.y           zaxis.y          0
	 	xaxis.z           yaxis.z           zaxis.z          0
		-dot(xaxis, eye)  -dot(yaxis, eye)  -dot(zaxis, eye)  l
		*/

		mat[0] = xaxisX;
		mat[1] = xaxisY;
		mat[2] = xaxisZ;
		mat[3] = 0;
		mat[4] = yaxisX;
		mat[5] = yaxisY;
		mat[6] = yaxisZ;
		mat[7] = 0;
		mat[8] = zaxisX;
		mat[9] = zaxisY;
		mat[10] = zaxisZ;
		mat[11] = 0;
		/*
		mat[12] = -dotX;
		mat[13] = -dotY;
		mat[14] = -dotZ;
		*/
		mat[12] = eyeX;
		mat[13] = eyeY;
		mat[14] = eyeZ;
		mat[15] = 1;

		outlet(0, "transform", mat);
	}
}
