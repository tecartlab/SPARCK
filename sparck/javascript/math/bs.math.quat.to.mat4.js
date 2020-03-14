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
	Quaternion to RotationMatrix	
*/

// set up inlets/outlets/assist strings
inlets = 1;
outlets = 1;
setinletassist(0,"quaternion");
setoutletassist(0,"rotation matrix");

var mymessagename = null;

// patcher arguments
if (jsarguments.length > 1){
    mymessagename = jsarguments[1];
}

var matrix = new Array();

function anything()
{
	var args = arrayfromargs(arguments);
	if(arguments.length < 4)
		args = arrayfromargs(messagename, arguments);

	quatToMatrix(args[0], args[1], args[2], args[3]);

	if(mymessagename != null)
		outlet(0, mymessagename, matrix);
	else
		outlet(0, matrix);
}

function quatToMatrix(X, Y, Z, W){

	var xx      = X * X;
    var xy      = X * Y;
    var xz      = X * Z;
    var xw      = X * W;

    var yy      = Y * Y;
    var yz      = Y * Z;
    var yw      = Y * W;

    var zz      = Z * Z;
    var zw      = Z * W;

	matrix[0] = 1 - 2 * ( yy + zz );
	matrix[1] = 2 * ( xy - zw );
	matrix[2] = 2 * ( xz + yw );
	matrix[3] = 0;

	matrix[4] = 2 * ( xy + zw );
	matrix[5] = 1 - 2 * ( xx + zz );
	matrix[6] = 2 * ( yz - xw );
	matrix[7] = 0;

	matrix[8] = 2 * ( xz - yw );
	matrix[9] = 2 * ( yz + xw );
	matrix[10] = 1 - 2 * ( xx + yy );
	matrix[11] = 0;

	matrix[12] = 0;
	matrix[13] = 0;
	matrix[14] = 0;
	matrix[15] = 1;
}

/*
MatToQuat(float m[4][4], QUAT * quat)
{
float  tr, s, q[4];
int    i, j, k;
int nxt[3] = {1, 2, 0};
tr = m[0][0] + m[1][1] + m[2][2];
// check the diagonal
if (tr > 0.0) {
s = sqrt (tr + 1.0);
quat->w = s / 2.0;
s = 0.5 / s;
quat->x = (m[1][2] - m[2][1]) * s;
quat->y = (m[2][0] - m[0][2]) * s;
quat->z = (m[0][1] - m[1][0]) * s;
} else {
// diagonal is negative
i = 0;
if (m[1][1] > m[0][0]) i = 1;
if (m[2][2] > m[i][i]) i = 2;
j = nxt[i];
k = nxt[j];
s = sqrt ((m[i][i] - (m[j][j] + m[k][k])) + 1.0);
q[i] = s * 0.5;
if (s != 0.0) s = 0.5 / s;
q[3] = (m[j][k] - m[k][j]) * s;
q[j] = (m[i][j] + m[j][i]) * s;
q[k] = (m[i][k] + m[k][i]) * s;
quat->x = q[0];
quat->y = q[1];
quat->z = q[2];
quat->w = q[3];
}
}
*/
