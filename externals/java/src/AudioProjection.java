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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxObject;
import com.tecartlab.tools.math.la.*;

public class AudioProjection extends MaxObject{

	float[] quat = new float[4];
	float[] pos = new float[3];

	Vector3f unitVector;

	Planef[] mySides;

	public AudioProjection(Atom args[])
	{
		declareIO(1,2);

		unitVector = new Vector3f(1,0,0);
		mySides = new Planef[6];
		
		mySides[0] = new Planef(new Vector3f(6, 0, 0), new Vector3f(-1, 0, 0));
		mySides[1] = new Planef(new Vector3f(-6, 0, 0), new Vector3f(1, 0, 0));
		mySides[2] = new Planef(new Vector3f(0, 11, 0), new Vector3f(0, -1, 0));
		mySides[3] = new Planef(new Vector3f(0, -11, 0), new Vector3f(0, 1, 0));
		mySides[4] = new Planef(new Vector3f(0, 0, 6), new Vector3f(0, 0, -1));
		mySides[5] = new Planef(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1));

	}
	
	public void calc(Atom args[]) {
		for(int i = 0; i < 3; i++) {
			pos[i] = args[i].getFloat();
		}

		for(int i = 0; i < 4; i++) {
			quat[i] = args[i+3].getFloat();
		}

		Vector3f myPos = new Vector3f(pos);

		Quaternionf myQuat = new Quaternionf(quat);	
		
		Vector3f direction = unitVector.clone().rotateBy(myQuat);
		
		Linef lineOfSight = new Linef(myPos, direction);			
		
		Vector3f[] intersects = new Vector3f[6];
		float[] lengths = new float[6];	
		float[] dir = new float[6];	
		
		float shortest = Float.MAX_VALUE;
		int shortIndex = -1;
		
		for(int i = 0; i < 6; i++) {
			intersects[i] = mySides[i].getIntersection(lineOfSight);
			if(intersects[i] != null) {				
				lengths[i] = intersects[i].subMake(myPos).magnitude();
				dir[i] = direction.dot(intersects[i].subMake(myPos).normalize());
				if(dir[i] > 0 && shortest > lengths[i]) {
					shortest = lengths[i];
					shortIndex = i;
				}
			} else {
				lengths[i] = 0;
				dir[i] = -1;
			}
		}
		if(shortIndex > -1 && shortIndex < 5)
		{
			outlet(1, "ground", 0);			
			outlet(1, "outofbounds", 0);			
			outlet(0, "position", Atom.newAtom(intersects[shortIndex].get()));
		} else if (shortIndex == 5){
			outlet(1, "ground", 1);			
			outlet(1, "outofbounds", 0);			
			outlet(0, "position", Atom.newAtom(intersects[shortIndex].get()));
		}else {
			outlet(1, "outofbounds", 1);			
		}
	}
	
	
	static public void main(String[] args){

		float[] quat = new float[4];
		float[] pos = new float[3];

		for(int i = 0; i < 3; i++) {
			pos[i] = Float.parseFloat(args[i]);
		}

		for(int i = 0; i < 4; i++) {
			quat[i] = Float.parseFloat(args[i+3]);
		}

		Vector3f myPos = new Vector3f(pos);

		Quaternionf myQuat = new Quaternionf(quat);
		
		Vector3f unitVector = new Vector3f(1,0,0);
		
		
		unitVector.rotateBy(myQuat);
		
		Linef lineOfSight = new Linef(myPos, unitVector);
		
		Planef[] mySides = new Planef[6];
			
		mySides[0] = new Planef(new Vector3f(6, 0, 0), new Vector3f(-1, 0, 0));
		mySides[1] = new Planef(new Vector3f(-6, 0, 0), new Vector3f(1, 0, 0));
		mySides[2] = new Planef(new Vector3f(0, 11, 0), new Vector3f(0, -1, 0));
		mySides[3] = new Planef(new Vector3f(0, -11, 0), new Vector3f(0, 1, 0));
		mySides[4] = new Planef(new Vector3f(0, 0, 6), new Vector3f(0, 0, -1));
		mySides[5] = new Planef(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1));
		
		Vector3f[] intersects = new Vector3f[6];
		float[] lengths = new float[6];	
		float[] dir = new float[6];	
		
		float shortest = Float.MAX_VALUE;
		int shortIndex = -1;
		
		for(int i = 0; i < 6; i++) {
			intersects[i] = mySides[i].getIntersection(lineOfSight);
			if(intersects[i] != null) {				
				lengths[i] = intersects[i].subMake(myPos).magnitude();
				dir[i] = unitVector.dot(intersects[i].subMake(myPos).normalize());
				if(dir[i] > 0 && shortest > lengths[i]) {
					shortest = lengths[i];
					shortIndex = i;
				}
			} else {
				lengths[i] = 0;
				dir[i] = -1;
			}
		}
		System.out.println("myQuat = " + myQuat.toString());
		System.out.println("lineofsight = " + unitVector.toString());
		System.out.println("plane: " + shortIndex + " | direction : " + dir[shortIndex] + " | length : " + lengths[shortIndex] + " | intersection point = " + intersects[shortIndex].toString());
	}
}
