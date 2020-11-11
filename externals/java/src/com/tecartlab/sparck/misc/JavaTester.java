package com.tecartlab.sparck.misc;
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

public class JavaTester extends MaxObject{

	public JavaTester(Atom args[]) {
		declareInlets(new int[] { DataTypes.ALL });
		declareOutlets(new int[] { DataTypes.ALL, DataTypes.ALL});
	}

	public void bang(){
		outlet(0, 1);
		outlet(1, 0);
		outlet(2, System.getProperty("java.version"));
		// checking for updates are done inside the Property
		/*
		if(Requestor.checkInternetAccess("tecartlab.com") && !Requestor.isCurrentVersion()){
			outlet(1, 1);
		} else {
			outlet(1, 0);
		}
		*/
	}
	
	public void version(String _version) {
		String iV = System.getProperty("java.version");
		String[] sVA = _version.split("\\.");
		String[] iVA = iV.split("\\.");
		outlet(0, (iVA[0].compareTo(sVA[0]) == 0)?1:0 );
		outlet(1, 0);
		outlet(2, System.getProperty("java.version"));
	}

}
