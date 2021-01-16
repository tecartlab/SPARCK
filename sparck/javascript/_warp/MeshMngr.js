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
 
WARP.MeshMngr = function ( manager ) {

    this.meshEdits = new Array();
    this.meshEditsMaxSize = 20;

    var initGeometry = new WARP.Geometry();
    initGeometry.create(10);

    this.hasItChanged = true;
	this.hasMeshChanged = true;

    this.meshEdits.push(initGeometry);
    this.meshEditsIndex = 0;

    this.modifiedMesh = null;
    this.lastPickRayIndex = -1;

    this.interpolateLoadedMesh = false;

 };

WARP.MeshMngr.prototype = {

	constructor: WARP.MeshMngr,

    // returns the current mesh
    getCurrentMesh: function ( ) {
 		return this.meshEdits[this.meshEditsIndex];
 	},

    // this method clones a new mesh for modification and basically
    // manages the undo/redo functionality
    // it returns the newly created mesh
    makeNewMesh: function ( ) {
        var plusIndex = this.meshEditsIndex + 1;
        // clone a new mesh at the new edits index and remove all the meshes
        // that follow
        if(plusIndex == this.meshEdits.length){
            this.meshEdits.push(this.getCurrentMesh().clone());
        } else {
            this.meshEdits.splice(plusIndex, this.meshEdits.length - plusIndex, this.getCurrentMesh().clone());
        }
        // if the list exeeds the maximum size, remove the firt
        if(this.meshEditsMaxSize < this.meshEdits.length){
            this.meshEdits.splice(0, 1);
            plusIndex--;
        }
        this.meshEditsIndex = plusIndex;
 		return this.getCurrentMesh();
 	},

    // save the current geometry
    // if _diff is true, save it with difference mesh
    save: function ( fout, _diff ) {
        if (fout.isopen) {
            //vertices
            for(var i = 0; i < this.getCurrentMesh().vertices.length; i++){
                fout.writeline("v " + this.getCurrentMesh().vertices[i].x + " " + this.getCurrentMesh().vertices[i].y + " " + this.getCurrentMesh().vertices[i].z);
            }
            //texturecoord
            for(var i = 0; i < this.getCurrentMesh().uvs.length; i++){
                fout.writeline("vt " + this.getCurrentMesh().uvs[i].x + " " + this.getCurrentMesh().uvs[i].y);
            }
            //normals
            for(var i = 0; i < this.getCurrentMesh().normals.length; i++){
                fout.writeline("vn " + this.getCurrentMesh().normals[i].x + " " + this.getCurrentMesh().normals[i].y + " " + this.getCurrentMesh().normals[i].z);
            }
            //faces
            for(var i = 0; i < this.getCurrentMesh().faces.length; i++){
                fout.writeline("f " +
                (this.getCurrentMesh().faces[i].vertA + 1) + "/" + (this.getCurrentMesh().faces[i].uvA + 1) + "/" + (this.getCurrentMesh().faces[i].normA + 1) + " " +
                (this.getCurrentMesh().faces[i].vertB + 1) + "/" + (this.getCurrentMesh().faces[i].uvB + 1) + "/" + (this.getCurrentMesh().faces[i].normB + 1) + " " +
                (this.getCurrentMesh().faces[i].vertC + 1) + "/" + (this.getCurrentMesh().faces[i].uvC + 1) + "/" + (this.getCurrentMesh().faces[i].normC + 1));
            }
            // selection storage
            for(var i = 0; i < this.getCurrentMesh().selectedStore.length; i++){
                var ss = this.getCurrentMesh().getSelectStoreList(i);
                if(ss.length > 0){
                    fout.writeline("ss" + ss);
                }
            }
            // differences to the original vertice
            if(_diff){
                // the modified vertices
                for(var i = 0; i < this.getCurrentMesh().vertices.length; i++){
                    fout.writeline("vm " + this.getCurrentMesh().vertices_mod[i].x + " " + this.getCurrentMesh().vertices_mod[i].y + " " + this.getCurrentMesh().vertices_mod[i].z);
                }
                for(var i = 0; i < this.getCurrentMesh().uvs.length; i++){
                    fout.writeline("vtm " + this.getCurrentMesh().uvs_mod[i].x + " " + this.getCurrentMesh().uvs_mod[i].y);
                }
            }
            return true;
        }
        return false;
    },

    hasChanged: function ( ) {
        return this.hasItChanged;
	},

    hasGeometryChanged: function ( ) {
        return this.hasMeshChanged;
	},

    // returns the mesh matrix from the current mesh which is modified by the lattice
    generateMatrix: function ( meshMatrix,  _subDiv, _color ) {
        //post(" generateMatrix "+this.getCurrentMesh().myCursor_mod +"\n");
        return this.getCurrentMesh().generateMatrix( meshMatrix,  _subDiv, _color );
    },

    load: function ( _newGeometry ) {
//        post(" loading new geom\n");
        this.makeNewMesh().load( _newGeometry , this.interpolateLoadedMesh);
        this.hasItChanged = true;
        this.hasMeshChanged = true;
//        post(" loading new geom.done\n");
	},

    // set the flag if the newly loaded mesh should be interpolated
    interpolateNewMesh: function( _interpolate ){
        this.interpolateLoadedMesh = _interpolate;
    },

    // modify the current mesh with the provided lattice
	modifyWith: function ( _latticeManager ) {
        //post("modify mesh with lattice: "+this.getCurrentMesh().myCursor_mod.x +"\n");        
        _latticeManager.modify(this.getCurrentMesh(), this.hasMeshChanged);
    },

    // apply the lattice modification as the mesh modification
	applyLattice: function ( ) {
        this.getCurrentMesh().applyLattice();
        this.hasItChanged = true;
        this.hasMeshChanged = true;
   },

    // create a new mesh
	create: function ( _dim ) {
        this.makeNewMesh().create( _dim );
        this.hasItChanged = true;
        this.hasMeshChanged = true;
	},

    // called when jumping into a modify mode.
    makeClone: function ( ) {
        this.makeNewMesh();
    },

    undoMesh: function ( ) {
        if(this.meshEditsIndex > 0){
            this.meshEditsIndex--;
            this.hasItChanged = true;
            this.hasMeshChanged = true;
        }
   },

    redoMesh: function ( ) {
        if((this.meshEditsIndex + 1) < this.meshEdits.length){
            this.meshEditsIndex++;
            this.hasItChanged = true;
            this.hasMeshChanged = true;
        }
   },

    // draw the current mesh, modified by the lattice
	drawLatMod: function ( _lattice_sketch, _drawMode  ) {
//        post(" drawLatMod\n");
        this.getCurrentMesh().draw( _lattice_sketch, _drawMode, 2 );
        this.hasItChanged = false;
        this.hasMeshChanged = false;
	},

    hasSelection: function ( ) {
        return this.getCurrentMesh().hasSelection( );
    },

	select: function ( ) {
        this.getCurrentMesh().select( );
        this.hasItChanged = true;
	},

	selectAdd: function ( ) {
        //post("meshmangr: select all :in\n")
        this.getCurrentMesh().selectAdd( );
        this.hasItChanged = true;
        //post("meshmangr: select all :out\n")
	},

    // recall the store for selection
    recallStoreSelection: function ( _storeIndex ) {
        this.getCurrentMesh().recallStoreSelection( _storeIndex );
        this.hasItChanged = true;
	},

    // stores the selected vertice under this index
    storeSelection: function ( _storeIndex ) {
        this.getCurrentMesh().storeSelection( _storeIndex );
 	},

    selectAll: function ( ) {
        this.getCurrentMesh().selectAll( );
        this.hasItChanged = true;
    },

    setCursor: function ( ) {
        this.getCurrentMesh().setCursor( );
        this.hasItChanged = true;
        this.hasMeshChanged = true;
    },

	setVertice: function ( _point) {
//        post("add to Vertice  = " + _point.x + " " + _point.y + " " + _point.z + "\n");
        this.getCurrentMesh().setVertice( _point);
        this.hasItChanged = true;
        this.hasMeshChanged = true;
    },

    verticeSnapshot: function ( _currentPoint, _originPoint) {
        this.getCurrentMesh().verticeSnapshot();
    },

    // call verticeSnapshot before scaling
	scaleVertice: function ( _currentPoint, _originPoint) {
//        post("add to Vertice  = " + _point.x + " " + _point.y + " " + _point.z + "\n");
        this.getCurrentMesh().scaleVertice( _currentPoint, _originPoint );
        this.hasItChanged = true;
        this.hasMeshChanged = true;
    },

    // call verticeSnapshot before scaling
	rotateVertice: function ( _currentPoint, _originPoint) {
//        post("add to Vertice  = " + _point.x + " " + _point.y + " " + _point.z + "\n");
        this.getCurrentMesh().rotateVertice( _currentPoint, _originPoint );
        this.hasItChanged = true;
        this.hasMeshChanged = true;
    },

	resetVertice: function ( ) {
        this.getCurrentMesh().resetVertice( );
        this.hasItChanged = true;
        this.hasMeshChanged = true;
    },

    // gets the vertice index from the lattice-modified mesh and passes it to
    // the current mesh
    pickRayLatMod: function ( _pickRay ) {
        //==>> this should be a pickray that picks  the lattice modified vertices !!!
        var prindex = this.getCurrentMesh().pickRay_lattice( _pickRay );
        if(prindex[0]*5 + prindex[1] !=  this.lastPickRayIndex[0]*5 + this.lastPickRayIndex[1]){
            this.hasItChanged = true;
        }
        this.lastPickRayIndex = prindex;
        this.getCurrentMesh().pickRayIndx = prindex;
	},

	pickRay: function ( _pickRay ) {
        var prindex = this.getCurrentMesh().pickRay( _pickRay );
        if(prindex[0]*5 + prindex[1] !=  this.lastPickRayIndex[0]*5 + this.lastPickRayIndex[1]){
           this.hasItChanged = true;
        }
        this.lastPickRayIndex = prindex;
 	}
};
