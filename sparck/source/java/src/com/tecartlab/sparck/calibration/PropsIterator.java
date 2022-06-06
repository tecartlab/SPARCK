package com.tecartlab.sparck.calibration;
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

import com.cycling74.max.MaxSystem;

import com.tecartlab.tools.threedee.Camera;

public class PropsIterator extends ProjProps {

	private PIter[] iter;

	private ProjPropsSubscribed original;

	public float[] startValues;
	public float[] storage;
	public float[] memory;
	public float[] lastMemory;

	private float progressTarget;

	private int progressPercent;

	public PropsIterator(ProjPropsSubscribed _store){
		super();

		original = _store;

		iter = new PIter[properties.length];
		startValues = new float[properties.length];
		storage = new float[properties.length];
		memory = new float[properties.length];
		lastMemory = new float[properties.length];

		for(int i = 0; i < iter.length; i++){
			iter[i] = new PIter(i);
			lastMemory[i] = original.properties[i];
		}
	}

	/**
	 * Call this method before starting any iterations, it also resets the storage.
	 */
	public void start(){
		for(int id = 0; id < iter.length; id++){
			startValues[id] = original.properties[id];
			properties[id] = original.properties[id];
			storage[id] = original.properties[id];
			iter[id].start();
		}
		progressTarget = 1;
		progressPercent = 0;
	}

	/**
	 * apply the stored properties inside the memory to the original. If you want to
	 * apply the current calculated properties call "store()" first.
	 */
	public void applyStoredProperties(){
		for(int id = 0; id < iter.length; id++)
			 original.properties[id] = storage[id];
		 original.refresh();
	}

	/**
	 * apply the memory properties inside the memory to the original. If you want to
	 * apply the current calculated properties call "memorize()" first.
	 */
	public void applyMemorizedProperties(){
		for(int id = 0; id < iter.length; id++)
			 original.properties[id] = memory[id];
		 original.refresh();
	}

	/**
	 * Applies the original properties to the camera object
	 * @param cam
	 */
	public void applyOriginalTo(Camera cam){
		original.applyDynamicsTo(cam);
	}

	/**
	 * Store the calculated properties to the storage
	 */
	public void store(){
		for(int id = 0; id < iter.length; id++){
			 storage[id] = properties[id];
		}
	}

	/**
	 * copy the stored properties to the memory
	 */
	public void memorize(){
		for(int id = 0; id < iter.length; id++){
			lastMemory[id] = memory[id];
			memory[id] = storage[id];
		}
	}

	/**
	 * Store the original properties to the memory
	 */
	public void memorizeOriginals(){
		for(int id = 0; id < iter.length; id++)
			 memory[id] = original.properties[id];
	}


	/**
	 * setup an individual iterator in a range around the original
	 * @param _id	 	its identification index (IDX_....)
	 * @param _steps	the amount of steps
	 * @param _size		the absolute size of the range
	 * @param _range	the relative position of the iterating steps. 0.5 is default
	 */
	public void setupSubIterator_range(int _id, int _steps, float _size, float _range){
		iter[_id].setup_range(_steps, _size, _range);
	}

	/**
	 * setup an individual iterator in a range around the original
	 * @param _id	 	its identification index (IDX_....)
	 * @param _steps	the amount of steps
	 * @param _relSize	the relative size of the range as a factor of the current value
	 * @param _range	the relative position of the iterating steps. 0.5 is default
	 */
	public void setupSubIterator_rangeRel(int _id, int _steps, float _relSize, float _range){
		iter[_id].setup_rangeRel(_steps, _relSize, _range);
	}

	/**
	 * setup an individual iterator in absolute values
	 * @param _id	 	its identification index (IDX_....)
	 * @param _steps	the amount of steps
	 * @param _start	where the iteration starts from
	 * @param _size		how far the iteration has to go to.
	 */
	public void setupSubIterator_abs(int _id, int _steps, float _start, float _size){
		iter[_id].setup_abs(_steps, _start, _size);
	}

	/**
	 * Setup all iterators
	 * @param steps		the amount of steps
	 * @param relsize	the size of the range relative to the original value
	 * @param range		the relative position of the iterating steps. 0.5 is default
	 */
	public void setup(int steps, float relsize, float range){
		for(int i = 0; i < iter.length; i++)
			iter[i].setup_range(steps, relsize, range);
	}

	/**
	 * Checks if the spezified iterator has more steps
	 * @param id
	 * @return
	 */
	public boolean nextStep(int id){
		return iter[id].nextStep();
	}

	/**
	 * Gets the progress of the iterative steps in percent.
	 *
	 * ATTENTION: hasProgress() must be called first
	 *
	 * @return percent of progress
	 */
	public int getProgress(){
		return progressPercent;
	}

	public boolean hasProgress(int _step){
		int _progress = 0;
		for(int i = 0; i < iter.length; i++)
			if(iter[i].isAcivated)
				_progress += iter[i].getProgress();
		_progress = (int)(_progress / progressTarget * 100.f);
		if(progressPercent + _step <= _progress){
			progressPercent = _progress;
			return true;
		}
		return false;
	}

	private class PIter{
		private int id;
		private int steps;
		private int stepIndex;
		private float stepSize;
		private float start;

		private boolean isAcivated;
		private int progressMultiplication;

		public PIter(int _id){
			id = _id;
			start();
		}

		/**
		 * Absolute Setup - Based on absolute values
		 * @param _steps
		 * @param _abssize
		 * @param _range
		 */
		public void setup_range(int _steps, float _abssize, float _range){
			stepIndex = 0;
			steps = _steps + 1;
			stepSize = _abssize / _steps;
			start = startValues[id] - _abssize * _range;
		}
		/**
		 *
		 * Relative Setup - Based on the original values
		 * @param _steps
		 * @param _relsize
		 * @param _range
		 */
		public void setup_rangeRel(int _steps, float _relsize, float _range){
			stepIndex = 0;
			steps = _steps + 1;
			stepSize = startValues[id] * _relsize / _steps;
			start = startValues[id] - _relsize * _range;
		}

		/**
		 * Absolute Setup - Based on the provided values
		 * @param _steps
		 * @param _start
		 * @param _size
		 */
		public void setup_abs(int _steps, float _start, float _size){
			stepIndex = 0;
			steps = _steps + 1;
			stepSize = _size / (_steps);
			start = _start;
		}

		public int getProgress(){
			return progressMultiplication * stepIndex;
		}

		/**
		 * Steps through each step and returns true unless it has to start again
		 * @return
		 */
		public boolean nextStep(){
			if(!isAcivated)
				activation();

			if(++stepIndex < steps){
				properties[id] = start + stepIndex * stepSize;
				return true;
			}
			stepIndex = -1;
			return false;
		}

		private void activation(){
			progressTarget *= steps;
			for(int i = 0; i < iter.length; i++)
				if(i != id)
					iter[i].setProgressMultiplication(steps);
			progressMultiplication = 1;
			isAcivated = true;
		}

		public void setProgressMultiplication(int _steps){
			progressMultiplication *= _steps;
		}

		public void start(){
			stepIndex = -1;
			steps = -1;
			isAcivated = false;
			progressMultiplication = 0;
		}

	}

}
