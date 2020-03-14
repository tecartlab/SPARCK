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

package com.tecartlab.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.tecartlab.utils.Debug;

public class GlobalPrefs {

	static private GlobalPrefs _instance;
	private Properties props;

	// methods and attributes for Singleton pattern
	private GlobalPrefs() {
		props = new Properties();
	}

	static public GlobalPrefs getInstance() {
		if (_instance == null) {
			synchronized(GlobalPrefs.class) {
				if (_instance == null)
					_instance = new GlobalPrefs();
			}
		}
		return _instance;
	}

	private String _dataPath = "";
	/*
	 * returns the absolute path. the specified addPath, which is relative to the
	 * data folder, is being added to the absolute path of the data folder
	 */

	/**
	 * Sets the actual absolute data path. needs an "/" at the end of the path;
	 * @param path
	 */
	public void setDataPath(String path){
		_dataPath = path;
		try {
			FileInputStream file = new FileInputStream(path + "properties.txt");
			props.load(file);
			file.close();
		} catch (IOException exp) {
			Debug.getInstance().errorMessage(this.getClass(),
					"No Property File found: " + exp.getMessage());
			;
		}
	}

	/**
	 *
	 * @param addPath 	the specified addPath, starts without "/", which is relative to the
	 * data folder, is being added to the absolute path of the data folder
	 * @return	the absolute path
	 */
	public String getAbsDataPath(String addPath){
		return _dataPath + addPath;
	}

	public String getAbsResourcePath(String addPath){
		return _dataPath + "resource/" + addPath;
	}

    public InputStream getStream(String filename)
    {
		try {
	        URL url;
			url = new URL("file:///" +_dataPath+filename);
	        return url.openStream();
		} catch (MalformedURLException e) {
	        Debug.getInstance().fatalMessage(this.getClass(), "### ERROR @getStream / "+ _dataPath + filename + " / " + e.getMessage());
		} catch (IOException e) {
	        Debug.getInstance().fatalMessage(this.getClass(), "### ERROR @getStream / "+ _dataPath + filename + " / " + e.getMessage());
		}
        return null;
    }

    public String getStringProperty(String key, String _default){
    	if(props.containsKey(key))
    		return props.getProperty(key, _default);
		Debug.getInstance().errorMessage(this.getClass(),"No Property found for key: " + key + "... returning default value: "+_default);
		return _default;
    }

    public int getIntProperty(String key, int _default){
    	if(props.containsKey(key))
    		return Integer.parseInt(props.getProperty(key));
		Debug.getInstance().errorMessage(this.getClass(),"No Property found for key: " + key + "... returning default value: "+_default);
		return _default;
    }

    public float getfloatProperty(String key, float _default){
    	if(props.containsKey(key))
    		return Float.parseFloat(props.getProperty(key));
		Debug.getInstance().errorMessage(this.getClass(),"No Property found for key: " + key + "... returning default value: "+_default);
		return _default;
    }
}
