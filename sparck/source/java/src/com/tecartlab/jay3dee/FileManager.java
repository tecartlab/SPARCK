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

import java.io.*;
import java.util.ArrayList;

import com.tecartlab.jay3dee.model.*;

public class FileManager {

	private ArrayList<ModelData> undoList;

	private int currentIndex;
	private int autoSaveCounter, autoSaveIndex, autoSaveSteps;
	public String originalFileName;
	private int undoSteps;

	public FileManager(int autosave){
		currentIndex = 0;
		undoList = new ArrayList<ModelData>();
		autoSaveSteps = autosave;
		undoSteps = 30;
	}

	public void saveAs(ModelData model, String filename){
		removeAutosaveFiles();
		originalFileName = filename;
		model.saveAs(filename);
	}

	public void load(ModelData model, String filename){
		removeAutosaveFiles();
		currentIndex = 0;
		autoSaveCounter = 0;
		autoSaveIndex = 0;
		originalFileName = filename;
		model.load(filename);
		undoList.add(currentIndex, model.clone());
	}

	public void save(ModelData model){
		if(model.saveAs(originalFileName))
			removeAutosaveFiles();
	}

	public ModelData unDo(ModelData model){
		if(currentIndex > 0){
			currentIndex--;
			return undoList.get(currentIndex).clone();
		}
		return model;
	}

	public ModelData reDo(ModelData model){
		if(currentIndex < (undoList.size() - 1)){
			currentIndex++;
			return undoList.get(currentIndex).clone();
		}
		return model;
	}

	/*
	 * the object has been altered and a undo step has to be saved.
	 */
	public void newDo(ModelData model){
		// remove all the old history
		for(int i = currentIndex + 1; i < undoList.size();){
			undoList.remove(i);
		}
		undoList.add(model.clone());
		if(currentIndex < undoSteps){
			currentIndex++;
		}else{
			//System.out.println("removing undo step from list with size: "+undoList.size());
			undoList.remove(0);
			//System.out.println("new size: "+undoList.size());
		}
		if(autoSaveCounter++ >= autoSaveSteps){
			model.saveAs(autosaveFilename(autoSaveIndex++));
			autoSaveCounter = 0;
		}
	}

	private String autosaveFilename(int index){
		return originalFileName + "_autosave_" + index;
	}

	private void removeAutosaveFiles(){
		int start = 0;
		boolean hasMoreFiles = true;
		File deleteFile;
		while(hasMoreFiles){
			deleteFile = new File(autosaveFilename(start++));
			if(deleteFile.exists()){
				deleteFile.delete();
			}else{
				hasMoreFiles = false;
			}
		}
		autoSaveIndex = 0;
		autoSaveCounter = 0;
	}
}
