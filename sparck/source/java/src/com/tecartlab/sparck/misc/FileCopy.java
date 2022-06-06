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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.*;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

import com.tecartlab.utils.Debug;

public class FileCopy extends MaxObject{

	private String destinationPath;

	public FileCopy(Atom args[]) {
		declareInlets(new int[] { DataTypes.ALL });
		declareOutlets(new int[] { DataTypes.ALL});

	}

	public void destination(String _path){
		destinationPath = _path;
	}

	public void copy(String _file){
		File f = new File(_file);

		String fileName = _file.substring(_file.lastIndexOf("/")+1);

		if(!f.isDirectory() && f.exists() && destinationPath != null){
			Path source = FileSystems.getDefault().getPath(_file);
			Path dest = FileSystems.getDefault().getPath(destinationPath + "/" + fileName);
			try {
				Files.copy(source, dest, REPLACE_EXISTING);

				outlet(0, "symbol", fileName);
			} catch (IOException e) {
				Debug.error("Dropping file error:", e.getMessage());
			}
		}
	}
}
