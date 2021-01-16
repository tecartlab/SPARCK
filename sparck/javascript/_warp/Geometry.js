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
 
WARP.Geometry = function () {


	this.vertices = []; // original vertice
	this.vertices_mod = []; // modified vertice
    this.vertices_mod_lat = []; // modified and latticed deformed vertice

    this.vertices_mod_tmp = []; // modifed vertice temporary storage

    this.uvs = []; // original uv
	this.uvs_mod = []; // modified uvs
	this.normals = [];
	this.faces = [];

    this.selectedVertices = []; // original vertice
    this.hasSelectedVertices = -1;

    this.selectedStore = []; // selected vertices storage

    this.pickRayIndx = -1;

    this.myCursor = new THREE.Vector3( 0, 0, 0);
    this.myCursor_mod = new THREE.Vector3( 0, 0, 0);
    this.myCursor_mod_lat = new THREE.Vector3( 0, 0, 0);

};

WARP.Geometry.prototype = {

	constructor: WARP.Geometry,

    // _dim is the number of vertice rows and cols
    create: function (_dim) {
        var stepSize = 2.0 / (_dim - 1);
        var UVstepSize = 1.0 / (_dim - 1);
        this.vertices = new Array(_dim * _dim);
        this.vertices_mod = new Array(_dim * _dim);
        this.vertices_mod_lat = new Array(_dim * _dim);
        this.selectedVertices = new Array(_dim * _dim);
        this.uvs = new Array(_dim * _dim);
        this.uvs_mod = new Array(_dim * _dim);

        for(var x = 0; x < _dim; x++){
            for(var y = 0; y < _dim; y++){
                this.vertices[x + y * _dim] = new THREE.Vector3( -1 + x * stepSize, -1 + y * stepSize, 0);
                this.vertices_mod[x + y * _dim] = new THREE.Vector3( -1 + x * stepSize, -1 + y * stepSize, 0);
                this.vertices_mod_lat[x + y * _dim] = new THREE.Vector3( -1 + x * stepSize, -1 + y * stepSize, 0);
                this.selectedVertices[x + y * _dim] = 0;
                this.uvs[x + y * _dim] = new THREE.Vector2(x * UVstepSize, y * UVstepSize);
                this.uvs_mod[x + y * _dim] = new THREE.Vector2(x * UVstepSize, y * UVstepSize);
            }
        }

        this.normals = new Array(1);
        this.normals[0] = new THREE.Vector3( 0, 0, 1);

        var faceDim = (_dim - 1) * (_dim - 1);
    	this.faces = new Array(faceDim * 2);

        for(var x = 0; x < (_dim - 1); x++){
            for(var y = 0; y < (_dim - 1); y++){
                this.faces[x + y * (_dim - 1)] = new WARP.Face3(x+y*_dim, x+1+y*_dim, x+1+(y+1)*_dim, x+y*_dim, x+1+y*_dim, x+1+(y+1)*_dim, 0, 0, 0);
                this.faces[x + y * (_dim - 1) + faceDim] = new WARP.Face3(x+1+(y+1)*_dim, x+(y+1)*_dim, x+y*_dim, x+1+(y+1)*_dim, x+(y+1)*_dim, x+y*_dim, 0, 0, 0);
            }
        }

        this.selectedStore = new Array(10);
        this.myCursor = new THREE.Vector3( 0, 0, 0);
        this.myCursor_mod = new THREE.Vector3( 0, 0, 0);
//        this.myCursor_mod_lat = new THREE.Vector3( 0, 0, 0);
//        post("myCursor: " +this.myCursor_mod.x + " " + this.myCursor_mod.y + " " + this.myCursor_mod.z +"\n");
   },

    load: function ( _newGeometry , _interpolate) {
        var thisClone = this.clone();

//        post(" importing modiefied vertices " + _newGeometry.vertices_mod.length + "\n");
//        post(" importing  vertices " + _newGeometry.vertices.length + "\n");

        this.vertices = new Array(_newGeometry.vertices.length);
        this.vertices_mod = new Array(_newGeometry.vertices.length);
        this.vertices_mod_lat = new Array(_newGeometry.vertices.length);
        this.selectedVertices = new Array(_newGeometry.vertices.length);

        for(var i = 0; i < this.vertices.length; i++){ // go through all the new vertices
            this.vertices[i] = _newGeometry.vertices[i].clone();
            if(_newGeometry.vertices_mod[i] != null){ // this only applies if the new geometry has stored modified vertices
                this.vertices_mod[i] = _newGeometry.vertices_mod[i].clone();
                this.vertices_mod_lat[i] = _newGeometry.vertices_mod[i].clone();
            } else {
                //clone the new vertice and make it  modified vertice.
                this.vertices_mod[i] = null;
                this.vertices_mod_lat[i] = null;
                this.selectedVertices[i] = 0;
                for(var ci = 0; ci < thisClone.vertices.length; ci++){ // go through all the old vertices
                    // compare each new vertice with the old one...
                    if(_newGeometry.vertices[i].clone().sub(thisClone.vertices[ci]).length() < 0.00001){
                        //.. if they are the same, take the old modified vertice
                        this.vertices_mod[i] = thisClone.vertices_mod[ci];
                        this.vertices_mod_lat[i] = thisClone.vertices_mod[ci];
                        // ... and also select the vertice if the old one was selected
                        this.selectedVertices[i] = thisClone.selectedVertices[ci];
                    }
                }
            }
        }

        // find all vertices that have no equivalent to the old mesh and thus no modification information:
        for(var i = 0; i < this.vertices.length; i++){ // go through all the new vertices
            if(this.vertices_mod[i] == null){
                this.vertices_mod[i] = this.vertices[i].clone();
                this.vertices_mod_lat[i] = this.vertices[i].clone();
                //post("found unmatched vertice\n");

                if(_interpolate){
                    // go through all the faces of the old mesh and test if the vertice is inside of the face
                    for( var j = 0; j < thisClone.faces.length; j++){
                        if(this.pointInTriangle( this.vertices[i],
                                            thisClone.vertices[thisClone.faces[j].vertA],
                                            thisClone.vertices[thisClone.faces[j].vertB],
                                            thisClone.vertices[thisClone.faces[j].vertC])){
                            // get the barycentric coordinates
                            var bc = this.getBarycentricCoordinates(this.vertices[i],
                                            thisClone.vertices[thisClone.faces[j].vertA],
                                            thisClone.vertices[thisClone.faces[j].vertB],
                                            thisClone.vertices[thisClone.faces[j].vertC]);

                            // create the new transformed vertice by applying the bc:
                            var newPoint = thisClone.vertices_mod[thisClone.faces[j].vertA].clone().multiplyScalar(bc[0]);
                            newPoint.add(thisClone.vertices_mod[thisClone.faces[j].vertB].clone().multiplyScalar(bc[1]));
                            newPoint.add(thisClone.vertices_mod[thisClone.faces[j].vertC].clone().multiplyScalar(bc[2]));

                            this.vertices_mod[i] = newPoint.clone();
                            this.vertices_mod_lat[i] = newPoint.clone();
                            //post("found vertice inside face: orig: " + this.vertices[i].x + " " + this.vertices[i].y + " " + this.vertices[i].z + " modified: " + newPoint.x + " " + newPoint.y + " " + newPoint.z + "\n");
                        }
                    }
                }
            }
        }

        // now do the same with the UV vertices
        // Currently no UV vertice modification is possible, so we will keep the originals from the new mesh.
        // If an interpolation like with the mesh vertices is planned, it should only apply if the previous mesh
        // was modified, otherwise we should keep the original (same applied to the mesh above, but ...)
        this.uvs = new Array(_newGeometry.uvs.length);
        this.uvs_mod = new Array(_newGeometry.uvs.length);
        for(var i = 0; i < this.uvs.length; i++){ // go through all the new vertices
            this.uvs[i] = _newGeometry.uvs[i].clone();
            if(_newGeometry.uvs_mod[i] != null){ // this only applies if the new geometry has stored modified vertices
                this.uvs_mod[i] = _newGeometry.uvs_mod[i].clone();
            } else {
                this.uvs_mod[i] = this.uvs[i].clone();
                /*
                for(var ci = 0; ci < thisClone.uvs.length; ci++){ // go through all the old vertices
                    // compare each new vertice with the old one...
                    if(_newGeometry.uvs[i].clone().sub(thisClone.uvs[ci]).length() < 0.00001){
                        //.. if they are the same, take the old modified vertice
                        this.uvs_mod[i] = thisClone.uvs_mod[ci];
                    }
                }
                */
            }
        }

        this.normals = new Array( _newGeometry.normals.length );
        for(var i = 0; i < _newGeometry.normals.length; i++){
            this.normals[i] = _newGeometry.normals[i];
        }

        this.faces = new Array( _newGeometry.faces.length );
        for(var i = 0; i < _newGeometry.faces.length; i++){
            this.faces[i] = _newGeometry.faces[i].clone();
        }

        this.selectedStore = new Array(10);
        for(var i = 0; i < _newGeometry.selectedStore.length; i++){
            this.selectedStore[i] = _newGeometry.selectedStore[i].slice(0);
        }

        this.myCursor = new THREE.Vector3( 0, 0, 0);
        this.myCursor_mod = new THREE.Vector3( 0, 0, 0);
//        this.myCursor_mod_lat = new THREE.Vector3( 0, 0, 0);
 //       post("myCursor: " +this.myCursor_mod.x + " " + this.myCursor_mod.y + " " + this.myCursor_mod.z +"\n");
    },

    // checks if the point is inisde the face
    // _p   the point
    //
    pointInTriangle: function (_p, _v0, _v1, _v2){
        var b1, b2, b3 = false;

        b1 = (this.sign(_p, _v0, _v1) < 0.0)? true: false;
        b2 = (this.sign(_p, _v1, _v2) < 0.0)? true: false;
        b3 = (this.sign(_p, _v2, _v0) < 0.0)? true: false;

        return ((b1 == b2) && (b2 == b3));
    },

    // helper function of checkin if the fertice is inside a face
    sign: function (p1, p2, p3){
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    },

    // get the Barycentric coordinates
    // takes
    // p    the point
    // v0 ... v1  the faces vertices
    getBarycentricCoordinates: function (_p, _v0, _v1, _v2){
        // based on an algorithm found here:
        // http://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/barycentric-coordinates

        // compute plane's normal
        var v0v1 = _v1.clone().sub(_v0);
        var v0v2 = _v2.clone().sub(_v0);
        // no need to normalize
        var N = v0v1.cross(v0v2); // N
        var area2 = N.length();

        // Step 1: finding P
        // We project the new point p orthognally to the old face and calculate with the
        // intersection point.

         // Create a ray the is perpendicular to the x-y plane
        var NdotRayDirection = new THREE.Vector3(0, 0, 1);

        // compute d parameter using equation 2
        var d = N.clone().dot(_v0);

         // compute t (equation 3)
        var t = (N.clone().dot(_p) + d) / NdotRayDirection;

         // compute the intersection point using equation 1
        var P = _p.clone().add(NdotRayDirection.clone().multiplyScalar(t));

        // edge 1
        var edge1 = _v2.clone().sub(_v1);
        var vp1 = P.clone().sub(_v1);
        var C = edge1.cross(vp1);
        var u = C.length() / area2;

        // edge 2
        var edge2 = _v0.clone().sub(_v2);
        var vp2 = P.clone().sub(_v2);
        C = edge2.cross(vp2);
        var v = C.length() / area2;

        var w = 1 - u - v;

//        post("triangle has a coordinate of " + u + " / " + v + " / " + w + " \n");
        return new Array(u, v, w);
    },

    clone: function ( ) {
        var clon = new WARP.Geometry();

        clon.vertices = new Array( this.vertices.length );
        for(var i = 0; i < this.vertices.length; i++){
            clon.vertices[i] = this.vertices[i].clone();
        }

        clon.vertices_mod = new Array( this.vertices_mod.length );
        for(var i = 0; i < this.vertices_mod.length; i++){
            clon.vertices_mod[i] = this.vertices_mod[i].clone();
        }

        clon.vertices_mod_lat = new Array( this.vertices_mod_lat.length );
        for(var i = 0; i < this.vertices_mod_lat.length; i++){
            clon.vertices_mod_lat[i] = this.vertices_mod_lat[i].clone();
        }

        clon.uvs = new Array( this.uvs.length );
        for(var i = 0; i < this.uvs.length; i++){
            clon.uvs[i] = this.uvs[i].clone();
        }

        clon.uvs_mod = new Array( this.uvs_mod.length );
        for(var i = 0; i < this.uvs_mod.length; i++){
            clon.uvs_mod[i] = this.uvs_mod[i].clone();
        }

        clon.normals = new Array( this.normals.length );
        for(var i = 0; i < this.normals.length; i++){
            clon.normals[i] = this.normals[i].clone();
        }

        clon.faces = new Array( this.faces.length );
        for(var i = 0; i < this.faces.length; i++){
            clon.faces[i] = this.faces[i].clone();
        }

        clon.selectedVertices = this.selectedVertices.slice(0,this.selectedVertices.length);

        clon.hasSelectedVertices = this.hasSelectedVertices;

        clon.selectedStore = new Array(this.selectedStore.length);
        for(var i = 0; i < this.selectedStore.length; i++){
            if(this.selectedStore[i] != null)
                clon.selectedStore[i] = this.selectedStore[i].slice(0);
        }

        clon.myCursor = new THREE.Vector3(this.myCursor.x, this.myCursor.y, this.myCursor.z);
        clon.myCursor_mod = new THREE.Vector3(this.myCursor_mod.x, this.myCursor_mod.y, this.myCursor_mod.z);
//        clon.myCursor_mod_lat = new THREE.Vector3(this.myCursor_mod_lat.x, this.myCursor_mod_lat.y, this.myCursor_mod_lat.z);

        return clon;
    },

    draw: function ( _lattice_sketch, _drawMode, _modFlag ) {
        _lattice_sketch.glcolor(0., 1., 0., 1.);
        _lattice_sketch.glpointsize(10.);
      // draw first the faces and lines
        for(var i = 0; i < this.faces.length; i++){
            var useVertice;
            if(_modFlag == 1){
                useVertice = this.vertices_mod;
            } else if(_modFlag == 2){
                useVertice = this.vertices_mod_lat;
            } else {
                useVertice = this.vertices;
            }

            _lattice_sketch.linesegment(useVertice[this.faces[i].vertA].x, useVertice[this.faces[i].vertA].y, useVertice[this.faces[i].vertA].z, useVertice[this.faces[i].vertB].x, useVertice[this.faces[i].vertB].y, useVertice[this.faces[i].vertB].z);
            _lattice_sketch.linesegment(useVertice[this.faces[i].vertB].x, useVertice[this.faces[i].vertB].y, useVertice[this.faces[i].vertB].z, useVertice[this.faces[i].vertC].x, useVertice[this.faces[i].vertC].y, useVertice[this.faces[i].vertC].z);
            _lattice_sketch.linesegment(useVertice[this.faces[i].vertA].x, useVertice[this.faces[i].vertA].y, useVertice[this.faces[i].vertA].z, useVertice[this.faces[i].vertC].x, useVertice[this.faces[i].vertC].y, useVertice[this.faces[i].vertC].z);
        }
        // then draw the points
        if(_drawMode == 'edit'){
            _lattice_sketch.layer = 30;
            _lattice_sketch.gllinewidth(1.0);
//            post("draw pickRayIndex = " + this.pickRayIndx + " / array lenth = "+this.vertices.length+"\n");
            for(var j = 0; j < this.vertices.length; j++){
                 if(this.pickRayIndx == j){
                   _lattice_sketch.glcolor(0., 0.9, 0., 1.);
                    // _lattice_sketch.glpointsize(10.);
                    if(_modFlag == 1)
                        _lattice_sketch.point(this.vertices_mod[j].x, this.vertices_mod[j].y, this.vertices_mod[j].z);
                    if(_modFlag == 2)
                        _lattice_sketch.point(this.vertices_mod_lat[j].x, this.vertices_mod_lat[j].y, this.vertices_mod_lat[j].z);
                    else
                        _lattice_sketch.point(this.vertices[j].x, this.vertices[j].y, this.vertices[j].z);
                }
               // DRAW SELECTED POINT
                if(this.selectedVertices[j] == 1){
                    _lattice_sketch.glcolor(0., 0., 0., 1.);
                    // _lattice_sketch.glpointsize(8.);
                    if(_modFlag == 1){
                        _lattice_sketch.point(this.vertices_mod[j].x, this.vertices_mod[j].y, this.vertices_mod[j].z);
                    } else if(_modFlag == 2){
                        _lattice_sketch.point(this.vertices_mod_lat[j].x, this.vertices_mod_lat[j].y, this.vertices_mod_lat[j].z);
                    } else {
                        _lattice_sketch.point(this.vertices[j].x, this.vertices[j].y, this.vertices[j].z);
                    }
                    _lattice_sketch.glcolor(1., 1., 1., 1.);
                    // _lattice_sketch.glpointsize(6.);
                    if(_modFlag == 1){
                        _lattice_sketch.point(this.vertices_mod[j].x, this.vertices_mod[j].y, this.vertices_mod[j].z);
                    } else if(_modFlag == 2){
                        _lattice_sketch.point(this.vertices_mod_lat[j].x, this.vertices_mod_lat[j].y, this.vertices_mod_lat[j].z);
                    } else {
                        _lattice_sketch.point(this.vertices[j].x, this.vertices[j].y, this.vertices[j].z);
                    }
                } else {
                    //IF NO SELECTION, DRAW IT NORMALLY
                    _lattice_sketch.glcolor(0., 1., 0., 1.);
                    // _lattice_sketch.glpointsize(5.);
                    if(_modFlag == 1)
                        _lattice_sketch.point(this.vertices_mod[j].x, this.vertices_mod[j].y, this.vertices_mod[j].z);
                    if(_modFlag == 2)
                        _lattice_sketch.point(this.vertices_mod_lat[j].x, this.vertices_mod_lat[j].y, this.vertices_mod_lat[j].z);
                    else
                        _lattice_sketch.point(this.vertices[j].x, this.vertices[j].y, this.vertices[j].z);
                }
            }
            // draw cursor:
            var useCursor;
            if(_modFlag == 1){
                useCursor = this.myCursor_mod;
            } if(_modFlag == 2){
                useCursor = this.myCursor_mod_lat;
            } else {
                useCursor = this.myCursor;
            }
            _lattice_sketch.glcolor(0., 0., 0., 1.);
            _lattice_sketch.linesegment(useCursor.x - 0.06, useCursor.y, useCursor.z, useCursor.x + 0.06, useCursor.y, useCursor.z);
            _lattice_sketch.linesegment(useCursor.x, useCursor.y - 0.06, useCursor.z, useCursor.x, useCursor.y + 0.06, useCursor.z);
            _lattice_sketch.moveto(useCursor.x, useCursor.y, useCursor.z);
            _lattice_sketch.gllinewidth(2.0);
            _lattice_sketch.framecircle(.05);

            _lattice_sketch.glcolor(0.7, 0.0, 0.7, 1.);
        } else {
            _lattice_sketch.layer = 20;
            _lattice_sketch.glcolor(0.3, 0.3, 0.3, 1.);
        }
  	},

    generateMatrix: function ( _meshMatrix,  _subDiv, _color) {
        _meshMatrix.dim = this.faces.length * 3 * Math.pow(4, _subDiv);
    	for(var j = 0; j < this.faces.length; j++) {
 //           this.faces[j].populate(_meshMatrix, j * 3 , _subDiv, this.vertices_mod, this.uvs_mod, this.normals);
            this.faces[j].populate(_meshMatrix, j * 3 , _subDiv, this.vertices_mod_lat, this.uvs_mod, this.normals, _color);
         }
        return _meshMatrix;
    },

    hasSelection: function ( ) {
        return (this.hasSelectedVertices > 0)?true: false;
    },

    setCursor: function ( ) {
//        this.myCursor = new THREE.Vector3(0, 0, 0);
        this.myCursor.set(0, 0, 0);
        this.myCursor_mod.set(0, 0, 0);
        var counter = 0;
        for(var j = 0; j < this.vertices.length; j++){
            if(this.selectedVertices[j] == 1){
                this.myCursor.add(this.vertices[j]);
                this.myCursor_mod.add(this.vertices_mod[j]);
                counter++;
            }
        }
        if(counter > 0){
            this.myCursor.multiplyScalar(1.0 / counter);
            this.myCursor_mod.multiplyScalar(1.0 / counter);
        }
        //post("myCursor: " +this.myCursor.x + " " + this.myCursor.y + " " + this.myCursor.z +"\n");
 	},


    select: function ( ) {
        if(this.pickRayIndx != -1){
            for(var j = 0; j < this.vertices.length; j++){
                this.selectedVertices[j] = 0;
            }
            this.selectedVertices[this.pickRayIndx] = 1;
            this.hasSelectedVertices = 1;
        }
        //post("select " + this.pickRayIndx + " value = " + this.selectedVertices[this.pickRayIndx] + " selected: " + this.hasSelectedVertices  + "\n");
 	},

    selectAdd: function ( ) {
        if(this.selectedVertices[this.pickRayIndx] == 0){
            this.selectedVertices[this.pickRayIndx] = 1;
            this.hasSelectedVertices++;
        } else {
            this.selectedVertices[this.pickRayIndx] = 0;
            this.hasSelectedVertices--;
        }
        //post("select add = " + this.pickRayIndx + " value = " + this.selectedVertices[this.pickRayIndx] + " selected: " + this.hasSelectedVertices  + "\n");
    },

    // stores the selected vertice indexes in the provided store idx
    storeSelection: function ( _storeIndex) {
        if( _storeIndex < this.selectedStore.length){
            this.selectedStore[_storeIndex] = new Array();
            for(var j = 0; j < this.vertices.length; j++){
                if(this.selectedVertices[j] == 1){
                    this.selectedStore[_storeIndex].push(j);
                }
            }
        }
    },

    // recalles the selected vertice indexes in the provided store idx
    recallStoreSelection: function ( _storeIndex) {
        // checks first if all vertices inside this store are already selected
        var valu = 0;
        if( _storeIndex < this.selectedStore.length && this.selectedStore[_storeIndex] != null){
            for(var j = 0; j < this.selectedStore[_storeIndex].length; j++){
                if(valu == 0 && this.selectedVertices[this.selectedStore[_storeIndex][j]] == 0){
                    valu = 1;
                }
            }
            for(var j = 0; j < this.selectedStore[_storeIndex].length; j++){
                this.selectedVertices[this.selectedStore[_storeIndex][j]] = valu;
            }
        }
    },

    getSelectStoreList: function ( _storeIndex) {
        var storeList = "";
        if( _storeIndex < this.selectedStore.length && this.selectedStore[_storeIndex] != null){
            for(var j = 0; j < this.selectedStore[_storeIndex].length; j++){
                storeList = storeList + " " + (this.selectedStore[_storeIndex][j] + 1);
            }
        }
        return storeList;
    },

    selectAll: function ( ) {
        var valu = 1;
        if(this.hasSelectedVertices == this.vertices.length){
            valu = 0;
        }
        for(var j = 0; j < this.vertices.length; j++){
            this.selectedVertices[j] = valu;
        }
        this.hasSelectedVertices = this.vertices.length * valu;
    },

    pickRay: function ( _pickRay ) {
        var distance = 10000000;
        for(var j = 0; j < this.vertices.length; j++){
            if(_pickRay.distanceToPoint(this.vertices[j]) < distance){
                distance = _pickRay.distanceToPoint(this.vertices[j]);
                this.pickRayIndx = j;
            }
        }
        return this.pickRayIndx;
	},

    pickRay_lattice: function ( _pickRay ) {
        var distance = 10000000;
        for(var j = 0; j < this.vertices_mod_lat.length; j++){
            if(_pickRay.distanceToPoint(this.vertices_mod_lat[j]) < distance){
                distance = _pickRay.distanceToPoint(this.vertices_mod_lat[j]);
                this.pickRayIndx = j;
            }
        }
        return this.pickRayIndx;
	},

    setVertice: function ( _point) {
//        post("have selected vertice  = " + this.hasSelectedVertices + "\n");
        for(var j = 0; j < this.vertices.length; j++){
            if(this.selectedVertices[j] == 1){
//                post("set vertice  = " + j + "\n");
                this.vertices_mod[j].add(_point);
            }
        }
    },

    verticeSnapshot: function ( ) {
        this.vertices_mod_tmp = new Array(this.vertices_mod.length);
        for(var j = 0; j < this.vertices_mod.length; j++){
            this.vertices_mod_tmp[j] = this.vertices_mod[j].clone();
        }
    },

    // applies the lattice modification as the mesh modifications
    applyLattice: function ( ) {
        this.vertices_mod = new Array(this.vertices_mod.length);
        for(var j = 0; j < this.vertices_mod.length; j++){
            this.vertices_mod[j] = this.vertices_mod_lat[j].clone();
        }
    },

    // take a verticeSnapshot before you scale anything
    scaleVertice: function ( _currentPoint, _originPoint ) {
        var dist_current = _currentPoint.clone().sub(this.myCursor_mod).length();
        var dist_origin = _originPoint.clone().sub(this.myCursor_mod).length();

//        post("have selected vertice  = " + this.hasSelectedVertices + "\n");
        for(var j = 0; j < this.vertices.length; j++){
            if(this.selectedVertices[j] == 1){
//                post("set vertice  = " + j + "\n");
                var dirToCursor = this.vertices_mod_tmp[j].clone().sub(this.myCursor_mod).multiplyScalar(dist_current / dist_origin);
                this.vertices_mod[j] = this.myCursor_mod.clone().add(dirToCursor);
            }
        }
    },

    // take a verticeSnapshot before you scale anything
    rotateVertice: function ( _currentPoint, _originPoint ) {
        var cursor_current = _currentPoint.clone().sub(this.myCursor_mod);
        var cursor_origin = _originPoint.clone().sub(this.myCursor_mod);

        cursor_current.sub(cursor_origin);
        var angle = cursor_current.x + cursor_current.y;

        var inverseTransform = new THREE.Matrix4();
        inverseTransform.makeTranslation(this.myCursor_mod.x, this.myCursor_mod.y, this.myCursor_mod.z);
        var rotationMatrix = new THREE.Matrix4();
        rotationMatrix.makeRotationZ(angle);
        var transform = new THREE.Matrix4();
        transform.makeTranslation(-this.myCursor_mod.x, -this.myCursor_mod.y, -this.myCursor_mod.z);

        inverseTransform.multiply(rotationMatrix).multiply(transform);

        for(var j = 0; j < this.vertices.length; j++){
            if(this.selectedVertices[j] == 1){
                this.vertices_mod[j] = this.vertices_mod_tmp[j].clone().applyMatrix4(inverseTransform);
            }
        }
    },

    resetVertice: function ( ) {
        for(var j = 0; j < this.vertices.length; j++){
            if(this.selectedVertices[j] == 1){
                this.vertices_mod[j] = this.vertices[j].clone() ;
            }
        }
    },

    applyMatrix: function ( matrix ) {

		var normalMatrix = new THREE.Matrix3().getNormalMatrix( matrix );

		for ( var i = 0, il = this.vertices.length; i < il; i ++ ) {

			var vertex = this.vertices[ i ];
			vertex.applyMatrix4( matrix );

		}

	},

	rotateX: function () {

		// rotate geometry around world x-axis

		var m1;

		return function rotateX( angle ) {

			if ( m1 === undefined ) m1 = new THREE.Matrix4();

			m1.makeRotationX( angle );

			this.applyMatrix( m1 );

			return this;

		};

	},

	rotateY: function () {

		// rotate geometry around world y-axis

		var m1;

		return function rotateY( angle ) {

			if ( m1 === undefined ) m1 = new THREE.Matrix4();

			m1.makeRotationY( angle );

			this.applyMatrix( m1 );

			return this;

		};

	},

	rotateZ: function () {

		// rotate geometry around world z-axis

		var m1;

		return function rotateZ( angle ) {

			if ( m1 === undefined ) m1 = new THREE.Matrix4();

			m1.makeRotationZ( angle );

			this.applyMatrix( m1 );

			return this;

		};

	},

	translate: function () {

		// translate geometry

		var m1;

		return function translate( x, y, z ) {

			if ( m1 === undefined ) m1 = new THREE.Matrix4();

			m1.makeTranslation( x, y, z );

			this.applyMatrix( m1 );

			return this;

		};

	},

	scale: function () {

		// scale geometry

		var m1;

		return function scale( x, y, z ) {

			if ( m1 === undefined ) m1 = new THREE.Matrix4();

			m1.makeScale( x, y, z );

			this.applyMatrix( m1 );

			return this;

		};
	}

};
