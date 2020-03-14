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

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 2;
setinletassist(0,"fov");
setoutletassist(0,"frustum");
setoutletassist(1,"matrix");

var kernelSize = 128;
var kernelArray = new Array();

function lerp(v0, v1, t) {
  return (1-t)*v0 + t*v1;
}

function create(usedSize){
	var kernelArray = new Array();
	var kernel = new Array(3);
	for (var i = 0; i < kernelSize; ++i) {
		kernel[0] = Math.random() * 2.0 - 1.0;
		kernel[1] = Math.random() * 2.0 - 1.0;
		kernel[2] =	Math.random();
		var length = Math.sqrt(Math.pow(kernel[0],2) + Math.pow(kernel[1],2) + Math.pow(kernel[2],2));
		distributeInHemisphere = Math.random();
		kernel[0] = kernel[0] / length * distributeInHemisphere;
		kernel[1] = kernel[1] / length * distributeInHemisphere;
		kernel[2] = kernel[2] / length * distributeInHemisphere;

		var scale = i / usedSize;
		scale = lerp(0.1, 1.0, scale * scale);
		kernel[0] *= scale;
		kernel[1] *= scale;
		kernel[2] *= scale;

//		post(kernel[0] + "\n");

		kernelArray.push(kernel[0]);
		kernelArray.push(kernel[1]);
		kernelArray.push(kernel[2]);
	}

	outlet(0, kernelArray);
}
