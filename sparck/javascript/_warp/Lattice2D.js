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
 
WARP.Lattice2D = function () {

    this.dim;
    this.rimL = 0.;
    this.rimR = 0.;
    this.rimT = 0.;
    this.rimB = 0.;
    this.vertices;   // two dimensional array
    this.selectedVertices;
    this.pickrayindx;
    this.hasSelectedVertices = false;

    this.create([3, 3, 0., 0., 0., 0.]);
};

WARP.Lattice2D.prototype = {

	constructor: WARP.Lattice2D,

    clone: function ( ) {
        var newClone = new WARP.Lattice2D();
        newClone.dim = [this.dim[0], this.dim[1]];
        newClone.rimL = this.rimL;
        newClone.rimR = this.rimR;
        newClone.rimT = this.rimT;
        newClone.rimB = this.rimB;
		newClone.vertices = new Array(this.dim[0]);
        newClone.selectedVertices = new Array(this.dim[0]);
 		for (var i = 0; i < this.dim[0]; i++) {
 			newClone.vertices[i] = new Array(this.dim[1]);
            newClone.selectedVertices[i] = new Array(this.dim[1]);
		}
		for(var i = 0; i < this.dim[0]; i++){
			for(var j = 0; j < this.dim[1]; j++){
				newClone.vertices[i][j] = this.vertices[i][j].clone();
                newClone.selectedVertices[i][j] = this.selectedVertices[i][j];
 			}
		}
        newClone.pickrayindx = this.pickrayindx;
        newClone.hasSelectedVertices = this.hasSelectedVertices;
        return newClone;
    },

	create: function ( _dim ) {
        // expecting X-dim, Y-dim and rim-size (float)
        this.dim = [_dim[0], _dim[1]];
		this.rimL = _dim[2];
		this.rimR = _dim[3];
		this.rimT = _dim[4];
		this.rimB = _dim[5];
		this.vertices = new Array(this.dim[0]);
        this.selectedVertices = new Array(this.dim[0]);
 		for (var i = 0; i < this.dim[0]; i++) {
 			this.vertices[i] = new Array(this.dim[1]);
            this.selectedVertices[i] = new Array(this.dim[1]);
		}
		for(var i = 0; i < this.dim[0]; i++){
			for(var j = 0; j < this.dim[1]; j++){
				this.vertices[i][j] = new THREE.Vector3((((2.+(this.rimL + this.rimR))/(this.dim[0]-1) * i) - (1.+ this.rimL)), (((2.+(this.rimT + this.rimB))/(this.dim[1]-1) * j) - (1.+ this.rimB)), 0);
                this.selectedVertices[i][j] = 0;
 			}
		}
        this.hasSelectedVertices = false;
		this.pickrayindx = new Array(-1, -1);
	},

    load: function ( _dim, _rim, _loadverts ) {
        if(_loadverts.length == _dim[0] * _dim[1]){
            this.dim = _dim;
            this.rimL = _rim[0];
            this.rimR = _rim[1];
            this.rimT = _rim[2];
            this.rimB = _rim[3];
            this.vertices = new Array(this.dim[0]);
            for (var i = 0; i < this.dim[0]; i++) {
                this.vertices[i] = new Array(this.dim[1]);
                this.selectedVertices[i] = new Array(this.dim[1]);
            }
            for(var i = 0; i < _loadverts.length; i++){
                var x = Math.floor(i / _dim[0]);
                var y = i - x * _dim[1];
                this.vertices[x][y] = _loadverts[i];
                this.selectedVertices[x][y] = 0;
            }
        }
        this.hasSelectedVertices = false;
		this.pickrayindx = new Array(-1, -1);
	},

    draw: function ( _lattice_sketch, _drawMode ) {
        _lattice_sketch.glpointsize(10.);
        for(var i = 0; i < this.dim[0]; i++){
            for(var j = 0; j < this.dim[1]; j++){
                _lattice_sketch.glcolor(1., 0., 0., 1.);
               // draw first the lines
                if(j < this.dim[1] - 1){
                    _lattice_sketch.linesegment(this.vertices[i][j].x, this.vertices[i][j].y, this.vertices[i][j].z, this.vertices[i][j+1].x, this.vertices[i][j+1].y, this.vertices[i][j+1].z);
                }
                if(i < this.dim[0] - 1){
                    _lattice_sketch.linesegment(this.vertices[i][j].x, this.vertices[i][j].y, this.vertices[i][j].z, this.vertices[i+1][j].x, this.vertices[i+1][j].y, this.vertices[i+1][j].z);
                }
                // then draw the points
                if(_drawMode == 'edit'){
                    _lattice_sketch.glcolor(1., 0., 0., 1.);
                    // _lattice_sketch.glpointsize(5.);
                    _lattice_sketch.point(this.vertices[i][j].x, this.vertices[i][j].y, this.vertices[i][j].z);
                    if(this.pickrayindx[0] == i && this.pickrayindx[1] == j){
                        _lattice_sketch.glcolor(0.9, 0., 0., 1.);
                        // _lattice_sketch.glpointsize(10.);
                        _lattice_sketch.point(this.vertices[i][j].x, this.vertices[i][j].y, this.vertices[i][j].z);
                    }
                    if(this.selectedVertices[i][j] == 1){
                        _lattice_sketch.glcolor(0., 0., 0., 1.);
                        // _lattice_sketch.glpointsize(8.);
                        _lattice_sketch.point(this.vertices[i][j].x, this.vertices[i][j].y, this.vertices[i][j].z);
                        _lattice_sketch.glcolor(1., 1., 1., 1.);
                        // _lattice_sketch.glpointsize(6.);
                        _lattice_sketch.point(this.vertices[i][j].x, this.vertices[i][j].y, this.vertices[i][j].z);
                    }
                }
            }
        }
        if(_drawMode == 'edit'){
            _lattice_sketch.layer = 30;
        } else {
            _lattice_sketch.layer = 20;
        }
	},

    hasSelection: function ( ) {
        return (this.hasSelectedVertices > 0)?true: false;
    },

	select: function ( ) {
        if(this.pickrayindx[0] != -1){
            for(var i = 0; i < this.dim[0]; i++){
                for(var j = 0; j < this.dim[1]; j++){
                    this.selectedVertices[i][j] = 0;
                }
            }
            this.selectedVertices[this.pickrayindx[0]][this.pickrayindx[1]] = 1;
            this.hasSelectedVertices = 1;
        }
	},

    selectAdd: function ( ) {
        this.selectedVertices[this.pickrayindx[0]][this.pickrayindx[1]] = 1;
        this.hasSelectedVertices++;
    },

    selectAll: function ( ) {
        var valu = 1;
        if(this.hasSelectedVertices == this.dim[0] * this.dim[1]){
            valu = 0;
        }
        for(var i = 0; i < this.dim[0]; i++){
            for(var j = 0; j < this.dim[1]; j++){
                this.selectedVertices[i][j] = valu;
            }
        }
        this.hasSelectedVertices = this.dim[0] * this.dim[1] * valu;
    },

	pickray: function ( _pickray ) {
        var distance = 10000000;
        for(var i = 0; i < this.dim[0]; i++){
            for(var j = 0; j < this.dim[1]; j++){
                if(_pickray.distanceToPoint(this.vertices[i][j]) < distance){
                    distance = _pickray.distanceToPoint(this.vertices[i][j]);
                    this.pickrayindx = [i, j];
                }
            }
        }
        return this.pickrayindx;
	},

    setVertice: function ( _point) {
        for(var i = 0; i < this.dim[0]; i++){
            for(var j = 0; j < this.dim[1]; j++){
                if(this.selectedVertices[i][j] == 1){
                    this.vertices[i][j].add(_point);
                }
            }
        }
    },

    resetVertice: function ( ) {
        for(var i = 0; i < this.dim[0]; i++){
            for(var j = 0; j < this.dim[1]; j++){
                if(this.selectedVertices[i][j] == 1){
                    this.vertices[i][j] = new THREE.Vector3((((2.+(this.rimL + this.rimR))/(this.dim[0]-1) * i) - (1.+ this.rimL)), (((2.+(this.rimT + this.rimB))/(this.dim[1]-1) * j) - (1.+ this.rimB)), 0);

                    //this.vertices[i][j] = new THREE.Vector3((((2.+2*this.rim)/(this.dim[0]-1) * i) - (1.+ this.rim)), (((2.+2*this.rim)/(this.dim[1]-1) * j) - (1.+ this.rim)), 0);
                }
            }
        }
    },

	grabKey: function ( _key, _step) {
        for(var i = 0; i < this.dim[0]; i++){
            for(var j = 0; j < this.dim[1]; j++){
                if(this.selectedVertices[i][j] == 1){
                    if(_key == 'up')
                        this.vertices[this.selectindx[0]][this.selectindx[1]].y += _step;
                    if(_key == 'down')
                        this.vertices[this.selectindx[0]][this.selectindx[1]].y -= _step;
                    if(_key == 'left')
                        this.vertices[this.selectindx[0]][this.selectindx[1]].x -= _step;
                    if(_key == 'right')
                        this.vertices[this.selectindx[0]][this.selectindx[1]].x += _step;
                }
            }
        }
	}
};
