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

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

/**
 * This Class sets the BeamStreamer Version
 *
 * @author maybites
 *
 */
public class Versioner extends MaxObject{
	// DO NOT CHANGE UNLESS YOU ALSO CHANGE SERVER FOLDER
	private static final String RELEASE_DB_CURRENT = "current";
	private static final String RELEASE_DB_ALPHA = "alpha";
	private static final String RELEASE_DB_BETA = "beta";

	private static final String RELEASE_CURRENT = "Version";
	private static final String RELEASE_ALPHA = "Alpha";
	private static final String RELEASE_BETA = "Beta";

	// THIS NUMBER IS TO TEST IF SPARCK IS UP TO DATE.
	// IF YOU WANT TO ENFORCE THE UPDATE OF SPARCK
	// WHEN REGISTERING FOR LICENSE YOU MUST CHANGE
	// THE SERVERFILE <SERVER_RELEASE_DB>/build/current.txt
	// to the build number you require
	public static final int BUILD_NUMBER = 25;
	public static final String VERSION = "1.0.0";

	public static final String BUILD_YEAR 	= "2020";
	public static final String BUILD_MONTH 	= "02";
	public static final String BUILD_DAY 	= "01";
	public static final String BUILD_DATE = BUILD_YEAR + "-" +BUILD_MONTH + "-" + BUILD_DAY;

	// set the releaseDB server folder name to get all the settings
	public static final String SERVER_RELEASE_DB = RELEASE_DB_BETA;

	// set the releaseLable for the 'About' Page
	public static final String RELEASE = RELEASE_BETA;

	// changeing this nummer will break all previous created registrations!!!!
	public static final int REGISTRATION_VERSION_NUMBER = 5;

	public Versioner(Atom args[]) {
		declareInlets(new int[] { DataTypes.ALL });
		declareOutlets(new int[] { DataTypes.ALL });
	}

	public void loadbang(){
		outlet(0, "set", RELEASE + " " + VERSION + " (build " + BUILD_NUMBER + " / " + BUILD_DATE + ")");
	}


}
