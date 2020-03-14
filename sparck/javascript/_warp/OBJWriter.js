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
 
WARP.OBJWriter = function ( ) {

    this.fileWriter;

    this.numVerticesWritten;
    this.numNormalsWritten;
    this.numTexturesWritten;
 };

WARP.OBJWriter.prototype = {

	constructor: WARP.OBJWriter,

    save: function ( _geometry, _filepath, _objectName) {

        this.beginSave(_filepath);
        var vOffset = this.getCurrVertexOffset() + 1;
        var nOffset = this.getCurrNormalOffset() + 1;
        var tOffset = this.getCurrTextureOffset() + 1;
        this.newObject(_objectName);
        // vertices
        for (var i = 0; i < _geometry.vertices.length; i++) {
        	this.vertex(_geometry.vertices[i].x, _geometry.vertices[i].y, _geometry.vertices[i].z);
        }
        // normals
        for (var i = 0; i < _geometry.normals.length; i++) {
        	this.normal(_geometry.normals[i].x, _geometry.normals[i].y, _geometry.normals[i].z);
        }
        // textures
        for (var i = 0; i < _geometry.uvs.length; i++) {
        	this.texture(_geometry.uvs[i].x, _geometry.uvs[i].y);
        }
        // faces
        var texNum = 0;
//        post("exporting _geometry.faces.length : " + _geometry.faces.length)
        for (var i = 0; i < _geometry.faces.length; i++) {
        	this.faceWithNormals(
        			_geometry.faces[i].vertA + vOffset, _geometry.faces[i].vertB + vOffset, _geometry.faces[i].vertC + vOffset,
        			_geometry.faces[i].uvA + tOffset, _geometry.faces[i].uvB + tOffset, _geometry.faces[i].uvC + tOffset,
        			_geometry.faces[i].normA + nOffset, _geometry.faces[i].normB + nOffset, _geometry.faces[i].normC + nOffset);
        }
        this.endSave();

 	},

	beginSave: function ( _filepath ) {

        this.fileWriter = new File(_filepath,"write","TEXT");

        if (this.fileWriter.isopen) {
            this.fileWriter.eof = 0;
            this.fileWriter.writeline("# Warp File created by MeshWarp / sparck");
            this.fileWriter.writeline("# www.tecartlab.com");
            this.numVerticesWritten = 0;
            this.numNormalsWritten = 0;
            this.numTexturesWritten = 0;
            return true;
        }
        return false;
 	},

    endSave: function ( ) {
        this.fileWriter.close();
 	},

    newObject: function ( _objectname ) {
        this.fileWriter.writeline("o " + _objectname);
	},

	face: function ( _a, _b, _c ) {
        this.fileWriter.writeline("f " + _a + " " + _b + " " + _c);
	},

    faceList: function ( ) {
        this.fileWriter.writeline("s off");
	},

    faceWithNormals: function ( _a, _b, _c, _ta, _tb, _tc, _na, _nb, _nc ) {
        this.fileWriter.writeline("f " +
                _a + "/" + _ta + "/" + _na + " " +
                _b + "/" + _tb + "/" + _nb + " " +
                _c + "/" + _tc + "/" + _nc);
	},

    getCurrNormalOffset: function () {
        return this.numNormalsWritten;
    },

    getCurrVertexOffset: function () {
        return this.numVerticesWritten;
    },

    getCurrTextureOffset: function () {
        return this.numTexturesWritten;
    },

    texture: function(_tx, _ty) {
        this.fileWriter.writeline("vt " + _tx + " " + _ty);
        this.numTexturesWritten++;
    },

    normal: function(_nx, _ny, _nz) {
        this.fileWriter.writeline("vn " + _nx + " " + _ny + " " + _nz);
        this.numNormalsWritten++;
    },

    vertex: function(_vx, _vy, _vz) {
        this.fileWriter.writeline("v " + _vx + " " + _vy + " " + _vz);
        this.numVerticesWritten++;
    }

};
