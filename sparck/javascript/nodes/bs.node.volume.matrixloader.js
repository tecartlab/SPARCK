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

inlets = 1;
outlets = 5;

var outletIntensity = 0;
var outletGradient = 1;
var outletProgressBar = 2;
var matrixout = 3;
var outletDump = 4;

var dimX = 256;
var dimY = 256;
var dimZ = 256;

var imgDimX;
var imgDimY;

var frameOffset = 0;
var framesUsed = -1;
var fileCount = 0;
var stepSize = 1;
var encoding = "combined";

var intensityMatrix = new JitterMatrix(4,"char", dimX, dimY, dimZ);
var gradientMatrix = new JitterMatrix(4,"char", dimX, dimY, dimZ);
var storageMatrix = new JitterMatrix(4,"char", dimX, dimY, dimZ);
var loadedMatrix = new JitterMatrix(4,"char", dimX, dimY);
var maskedMatrix = new JitterMatrix(4,"char", dimX, dimY);
var mask = new JitterMatrix(4,"char", dimX, dimY);

var gradient = new JitterObject("jit.sobel");
var packMatrix = new JitterObject("jit.pack");
var unpackMatrix = new JitterObject("jit.unpack");
var masker = new JitterObject("jit.op");

var filelist = new Array();

var fileindex = 0;

var indexStart = 0;
var indexEnd = -1;

var	loadingFlag = 0;
var	loadingIndex = 0;
var	loadingLock = 0;

var hasMaskLoaded = 0;
var refresherTask = new Task(createMatrices, this);

function clear(){
	post("clear \n");
	filelist = new Array();
}

function importmovie(filepath){
	filelist.push(filepath);
}

function dim(_dim){
//	post("set dim: " + _dim + " \n");
	dimX = _dim;
	dimY = _dim;
	createMatrices();
}

function encodingmode(_encoding){
	encoding = _encoding;
	if(encoding != "dual"){
		outlet(outletGradient, "dim", 1, 1, 1);
	}

	createMatrices();
}

function frameoffset(_frameoffset){
	frameOffset = _frameoffset;
	refresherTask.schedule(500);
}

function framesused(_framesused){
	framesUsed = _framesused;
	refresherTask.schedule(500);
}

function stepsize(_stepsize){
	stepSize = _stepsize;
	refresherTask.schedule(500);
}

function gradientMode(_mode){
	gradient.mode = _mode;
	refresherTask.schedule(500);
}

function gradientThreshold(_thresh){
	gradient.thresh = _thresh;
	refresherTask.schedule(500);
}

function maskfilepath(_maskfile){
	if(_maskfile != undefined){
//		post("loadingMask: " + _maskfile + " \n");
		mask.adapt = 1;
		mask.importmovie(_maskfile);
		mask.bang;
//		post("Mask: Dim " + mask.dim[0] + " - " + mask.dim[1] + " \n");
		masker.op = "*";
		hasMaskLoaded = 1;
	} else {
		hasMaskLoaded = 0;
	}

	startloading();
}

//
function bang(){
	loadingloop();
}

// this function is called once all the filespaths
// inside a folder have been sent.
function loadfiles(_counter){
	fileCount = _counter;
	startloading();
}

// starts the loading procedure
function startloading(){
	if(fileCount > 0){
		loadedMatrix.adapt = 1;
		loadfile(filelist[0]);

		imgDimX = maskedMatrix.dim[0];
		imgDimY = maskedMatrix.dim[1];

//		post("maskedMatrix: Dim " + maskedMatrix.dim[0] + " - " + maskedMatrix.dim[1] + " \n");

		storageMatrix.dim = [imgDimX, imgDimY, fileCount]
		storageMatrix.usedstdim = 1;

		loadingFlag = 1;
		loadingIndex = 0;
		outlet(outletProgressBar, "start", fileCount);
	}
}

// loads all the files, one after another with each bang of the main metro
// this makes sure the app keeps running without interupt
function loadingloop(){
	if(loadingFlag == 1 && loadingLock == 0){
		loadingLock == 1;
		if(loadingIndex < fileCount){
			storageMatrix.dstdimstart = [0, 0, loadingIndex];
			storageMatrix.dstdimend = [(imgDimX - 1), (imgDimY - 1), loadingIndex];
			loadfile(filelist[loadingIndex]);
			storageMatrix.frommatrix(maskedMatrix);
			outlet(outletProgressBar, "loaded", loadingIndex);
			outlet(outletProgressBar, "mask_matrix", "jit_matrix", mask.name);
			outlet(outletProgressBar, "loaded_matrix", "jit_matrix", loadedMatrix.name);
			outlet(outletProgressBar, "masked_matrix", "jit_matrix", maskedMatrix.name);
			loadingIndex++;
		} else {
			outlet(outletProgressBar, "done");
			post("loaded " + fileCount + " images with dimX = " + imgDimX + " | dimY " + imgDimY + "  \n");
			loadingFlag = 0;
			createMatrices();
		}
		loadingLock == 0;
	}
}

//internal function that loads the image an applies the mask if available
function loadfile(filepath){
	if(filepath != ""){
		loadedMatrix.importmovie(filepath);
		loadedMatrix.bang;
//		outlet(matrixout, "jit_matrix", loadedMatrix.name);
		maskedMatrix.dim = [loadedMatrix.dim[0], loadedMatrix.dim[1]];
		if(hasMaskLoaded == 1){
			masker.matrixcalc([loadedMatrix, mask], maskedMatrix);
		} else {
			maskedMatrix.frommatrix(loadedMatrix);
		}
	}
}

// creates all the matrices
function createMatrices(){
	if(fileCount > 0){
		indexStart = frameOffset;
		if(framesUsed > 0 && (indexStart + framesUsed * stepSize) < fileCount)
			indexEnd = indexStart + framesUsed * stepSize
		else
			indexEnd = fileCount

		var frames = Math.floor((indexEnd - indexStart) / stepSize);

		calculateDimZ(frames);

		intensityMatrix.dim = [dimX, dimY, dimZ];

		intensityMatrix.usedstdim = 1;
		intensityMatrix.usesrcdim = 1;

		for(var inx = indexStart; inx < indexEnd; inx = inx + stepSize){
			intensityMatrix.dstdimstart = [0, 0, (inx / stepSize - indexStart)];
			intensityMatrix.dstdimend = [(dimX - 1), (dimY - 1), (inx / stepSize - indexStart)];
			intensityMatrix.srcdimstart = [0, 0, inx];
			intensityMatrix.srcdimend = [(storageMatrix.dim[0] - 1), (storageMatrix.dim[1] - 1), inx];

			intensityMatrix.frommatrix(storageMatrix);
		}

		intensityMatrix.bang;

		if(encoding == "dual"){
			updateGradienMatrix();
			outputGradienMatrix();
		}
		if(encoding == "combined"){
			intensityMatrix.bang;
			updateGradienMatrix();
			var intensity_r = new JitterMatrix(1,"char", dimX, dimY, dimZ);
			var intensity_g = new JitterMatrix(1,"char", dimX, dimY, dimZ);
			var intensity_b = new JitterMatrix(1,"char", dimX, dimY, dimZ);
			var intensity_a = new JitterMatrix(1,"char", dimX, dimY, dimZ);
			unpackMatrix.matrixcalc(intensityMatrix,[intensity_a,intensity_r,intensity_g,intensity_b]);
			var gradient_x = new JitterMatrix(1,"char", dimX, dimY, dimZ);
			var gradient_y = new JitterMatrix(1,"char", dimX, dimY, dimZ);
			var gradient_z = new JitterMatrix(1,"char", dimX, dimY, dimZ);
			var gradient_w = new JitterMatrix(1,"char", dimX, dimY, dimZ);
			unpackMatrix.matrixcalc(gradientMatrix,[gradient_w, gradient_x, gradient_y, gradient_z]);
			packMatrix.matrixcalc([intensity_r,gradient_x,gradient_y,gradient_z], intensityMatrix);
		}

		outlet(outletIntensity, "dim", dimX, dimY, dimZ);
		outlet(outletIntensity, "jit_matrix", intensityMatrix.name);

		outlet(outletDump, "dim", dimX, dimY, dimZ);
		outlet(outletDump, "encoding", encoding);
		outlet(outletDump, "filecount", fileCount);
		outlet(outletDump, "frameoffset", frameOffset);
		outlet(outletDump, "frames", frames);
		outlet(outletDump, "dimratio", (frames+0.0) / (dimZ+0.0));
	}
}

function updateGradienMatrix(){
	if(fileCount > 0){
		gradientMatrix.dim = [dimX, dimY, dimZ];
		gradient.matrixcalc(intensityMatrix, gradientMatrix);
	}
}

function outputGradienMatrix(){
	if(fileCount > 0){
		outlet(outletGradient, "dim", dimX, dimY, dimZ);
		outlet(outletGradient, "jit_matrix", gradientMatrix.name);
	}
}

function calculateDimZ(_counter){
	dimZ = 1;
	for(var i = 1; i < 15; i++){
		if(dimZ < _counter){
			dimZ = dimZ * 2;
		}else{
			return;
		}
	}
}

function notifydeleted(){
	gradient.freepeer();
 	packMatrix.freepeer();
	unpackMatrix.freepeer();
	masker.freepeer();

	intensityMatrix.freepeer();
	gradientMatrix.freepeer();
	storageMatrix.freepeer();
	loadedMatrix.freepeer();
	maskedMatrix.freepeer();
	mask.freepeer();
}
