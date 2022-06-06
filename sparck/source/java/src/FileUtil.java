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

import java.io.File;
import java.util.*;

/**
 * @author Martin Froehlich
 *
 * Max object container for test purposes
 */
public class FileUtil extends MaxObject{

	public FileUtil(Atom args[]){
		declareInlets(new int[]{ DataTypes.ALL});
		declareOutlets(new int[]{ DataTypes.ALL});
		createInfoOutlet(true);
	}

	public void mkdir(String foldername){
		boolean success = false;

		File newFolder = new File(foldername);

		try{
			success = newFolder.mkdir();
		} catch (SecurityException Se){
			this.error("Error creating Folder: '" + foldername + "' -> " + Se.toString());
		}

		if(success)
			outlet(1, "done", "mkdir");
		else if(!success)
			outlet(1, "error", "mkdir");
	}

	public void mkdirs(String foldername){
		boolean success = false;

		File newFolder = new File(foldername);

		try{
			success = newFolder.mkdirs();
		} catch (SecurityException Se){
			this.error("Error creating Folder: '" + foldername + "' -> " + Se.toString());
		}

		if(success)
			outlet(1, "done", "mkdirs");
		else if(!success)
			outlet(1, "error", "mkdirs");
	}
}
