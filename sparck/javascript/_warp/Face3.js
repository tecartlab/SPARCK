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
 
WARP.Face3 = function ( _vertA, _vertB, _vertC, _uvA, _uvB, _uvC, _normA, _normB, _normC) {

	this.vertA = _vertA;
	this.vertB = _vertB;
	this.vertC = _vertC;

    this.uvA = _uvA;
    this.uvB = _uvB;
    this.uvC = _uvC;

    this.normA = _normA;
    this.normB = _normB;
    this.normC = _normC;

};

WARP.Face3.prototype = {

	constructor: WARP.Face3,

	populate: function ( _meshMatrix, _idx, _subDiv, _vertices, _uvs, _normals ) {
            var vA = _vertices[this.vertA];
            var vB = _vertices[this.vertB];
            var vC = _vertices[this.vertC];
            var uA = _uvs[this.uvA];
            var uB = _uvs[this.uvB];
            var uC = _uvs[this.uvC];
            var nA = _normals[this.normA];
            var nB = _normals[this.normB];
            var nC = _normals[this.normC];
            if(nA == null){
                nA = new THREE.Vector3( 0, 0, 1);
                nB = nA;
                nC = nA;
            }
            _meshMatrix.setcell(_idx + 0,"val",vA.x, vA.y, vA.z, uA.x, uA.y, nA.x, nA.y, nA.z, 1., 1., 1., 1.);
            _meshMatrix.setcell(_idx + 1,"val",vB.x, vB.y, vB.z, uB.x, uB.y, nB.x, nB.y, nB.z, 1., 1., 1., 1.);
            _meshMatrix.setcell(_idx + 2,"val",vC.x, vC.y, vC.z, uC.x, uC.y, nC.x, nC.y, nC.z, 1., 1., 1., 1.);
            //post("setcell("+_idx +",val,"+vA.x+","+ vA.y+","+ vA.z+"," +uA.x+"," +uA.y+","+ nA.x+","+ nA.y+","+ nA.z+","+ 1.+","+ 1.+","+ 1.+","+ 1.+")\n");
	},

    clone: function ( ) {
        var clon = new WARP.Face3();

       	clon.vertA = this.vertA;
        clon.vertB = this.vertB;
        clon.vertC = this.vertC;

        clon.uvA = this.uvA;
        clon.uvB = this.uvB;
        clon.uvC = this.uvC;

        clon.normA = this.normA;
        clon.normB = this.normB;
        clon.normC = this.normC;

        return clon;
    }
};
