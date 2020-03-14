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

/*
inverts a quaternion
returns a quaternion
*/
exports.inverse = function(theQuat){
	var revec = exports.conjugate(theQuat);
	return exports.scaleQuatScal(revec, exports.length2(revec));
}

exports.length2 = function(theQuat){
	return theQuat[0] * theQuat[0] + theQuat[1] * theQuat[1] +
		theQuat[2] * theQuat[2] + theQuat[3] * theQuat[3];
}

/*
Scales a quat with a scalar
returns a quaternion
*/
exports.scaleQuatScal = function(theQuat, scalar){
	var revec = new Array(4);
	revec[0] = theQuat[0] * scalar;
	revec[1] = theQuat[1] * scalar;
	revec[2] = theQuat[2] * scalar;
	revec[3] = theQuat[3] * scalar;
	return revec;
}

/*
conjugate a quaternion
returns a quaternion
*/
exports.conjugate = function(theQuat){
	var revec = new Array(4);
	revec[0] = theQuat[0] * -1;
	revec[1] = theQuat[1] * -1;
	revec[2] = theQuat[2] * -1;
	revec[3] = theQuat[3];
	return revec;
}
