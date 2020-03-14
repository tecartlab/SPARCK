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
outlets = 1;
setinletassist(0,"bang");
setoutletassist(0,"matrix");

var m = new JitterMatrix(4, "float32", 4, 4);

function lerp(v0, v1, t) {
  return (1-t)*v0 + t*v1;
}

function bang(){
	m = new JitterMatrix(4, "float32", 4, 4);

	var v0, v1, v2, v3;
	var length;

	for(i = 0; i<4; i++)
	{
		for(j = 0; j<4; j++)
		{
			v0 = Math.random() * 2.0 - 1.0;
			v1 = Math.random() * 2.0 - 1.0;
			v2 = 0.0;
			v3 = 1.0;

			length = Math.sqrt(Math.pow(v0,2) + Math.pow(v1,2));

			v0 /= length;
			v1 /= length;

			m.setcell(j,i,"val",v0, v1, v2, v3);
//			post("[" + v0 + ", " + v1 + ", " + v2 + ", " + v3 + "]\n");
		}
	}

	outlet(0,"jit_matrix",m.name);
}
