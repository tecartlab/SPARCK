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
 
WARP.WarpLoader = function ( ) {
    this.objects;
    this.fileName;

};

WARP.WarpLoader.prototype = {

	constructor: WARP.WarpLoader,

	load: function ( url ) {

		var scope = this;

		var f = new File(url);
		var i,c, a;
		var lines = new Array();

		if (f.isopen) {
			c = f.eof;
//			post("file line count = " + c + "\n");
			i = 0;
			while (f.position < c) {
				i = i + 1;
				a = f.readline(150); //returns single line string
//				post("string at fileposition[" + f.position + "] linenumber[" + i + "] "  + a + "\n");
				lines.push(a);
			}
            this.fileName = url;
			f.close();
			return this.parse( lines );
		} else {
			error("WarpLoader: could not open file: " + url + "\n");
		}
        return false;
	},

  	parse: function ( lines ) {

        this.objects = [];
		var object, geometry, lattice, material;
        var ldim, lrim, lvertices;

 		function parseVertexIndex( value ) {
            if ( value !== undefined ){
                var index = parseInt( value );
                return index - 1;
            }
            return -1;
		}

		function parseNormalIndex( value ) {
            if ( value !== undefined ){
 			    var index = parseInt( value );
                return index - 1;
            }
            return -1;
		}

		function parseUVIndex( value ) {
            if ( value !== undefined ){
                var index = parseInt( value );
                return index - 1;
            }
           return -1;
		}

 		function addFace( a, b, c, d,  ua, ub, uc, ud, na, nb, nc, nd ) {
            var ia = parseVertexIndex( a );
			var ib = parseVertexIndex( b );
			var ic = parseVertexIndex( c );
 			var id = parseVertexIndex( d );
            var iua = parseUVIndex( ua );
			var	iub = parseUVIndex( ub );
			var	iuc = parseUVIndex( uc );
			var	iud = parseUVIndex( ud );
            var ina = parseNormalIndex( na );
			var	inb = parseNormalIndex( nb );
			var	inc = parseNormalIndex( nc );
			var	ind = parseNormalIndex( nd );

            if ( d !== undefined ) {
                geometry.faces.push(new WARP.Face3(ia, ib, id, iua, iub, iud, ina, inb, ind));
                geometry.faces.push(new WARP.Face3(ib, ic, id, iub, iuc, iud, inb, inc, ind));
			} else {
                geometry.faces.push(new WARP.Face3(ia, ib, ic, iua, iub, iuc, ina, inb, inc));
            }
		}

        // ld float float

		var lattice_dim_pattern = /ld( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

        // lr float float float float

		var lattice_rim_pattern = /lr( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

		// lv float float float

		var lattice_vertex_pattern = /lv( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

        // ss float float float ...

		var select_store_pattern = /ss( +-?\d+)+/;

		// v float float float

		var vertex_pattern = /v( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

        //mv float float float

        var mod_vertex_pattern = /vm( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

		// vn float float float

		var normal_pattern = /vn( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

		// vt float float

		var uv_pattern = /vt( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

        // mvt float float

		var mod_uv_pattern = /vtm( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

		// f vertex vertex vertex ...

		var face_pattern1 = /f( +-?\d+)( +-?\d+)( +-?\d+)( +-?\d+)?/;

		// f vertex/uv vertex/uv vertex/uv ...

		var face_pattern2 = /f( +(-?\d+)\/(-?\d+))( +(-?\d+)\/(-?\d+))( +(-?\d+)\/(-?\d+))( +(-?\d+)\/(-?\d+))?/;

		// f vertex/uv/normal vertex/uv/normal vertex/uv/normal ...

		var face_pattern3 = /f( +(-?\d+)\/(-?\d+)\/(-?\d+))( +(-?\d+)\/(-?\d+)\/(-?\d+))( +(-?\d+)\/(-?\d+)\/(-?\d+))( +(-?\d+)\/(-?\d+)\/(-?\d+))?/;

		// f vertex//normal vertex//normal vertex//normal ...

		var face_pattern4 = /f( +(-?\d+)\/\/(-?\d+))( +(-?\d+)\/\/(-?\d+))( +(-?\d+)\/\/(-?\d+))( +(-?\d+)\/\/(-?\d+))?/;

		//

//		var lines = text.split( '\n' );

        // create the first geometry

        geometry = new WARP.Geometry();

        ldim = [];
        lrim = [0, 0, 0, 0];
        lvertices = [];

        lattice = {
            ldim: ldim,
            lrim: lrim,
            lvertices: lvertices
        };

        material = {
            name: ''
        };

        object = {
            name: "default",
            geometry: geometry,
            material: material,
            lattice: lattice
        };

        this.objects.push( object );

        var geometryCounter = 0;


 		for ( var i = 0; i < lines.length; i ++ ) {

			var line = lines[ i ];
			line = line.trim();

			var result;

			if ( line.length === 0 || line.charAt( 0 ) === '#' ) {

				continue;

			} else if ( ( result = lattice_dim_pattern.exec( line ) ) !== null ) {

				// ["ld 4 8, "4", "8"]

				lattice.ldim.push(
                    parseInt( result[ 1 ] ),
					parseInt( result[ 2 ] ));

			} else if ( ( result = lattice_rim_pattern.exec( line ) ) !== null ) {

				// ["lr 0.1 0.2 0.1 0.0, "0.1", "0.2", "0.1", "0.0"]

                lattice.lrim = [];
				lattice.lrim.push(
                    parseFloat( result[ 1 ] ),
					parseFloat( result[ 2 ] ),
					parseFloat( result[ 3 ] ),
					parseFloat( result[ 4 ] ));

			} else if ( ( result = lattice_vertex_pattern.exec( line ) ) !== null ) {

				// ["lv 0.2 0.4 0.0, "0.2", "0.4", "0.0"]
    
				lattice.lvertices.push(
                    new THREE.Vector3(
					parseFloat( result[ 1 ] ),
					parseFloat( result[ 2 ] ),
					parseFloat( result[ 3 ] )));

			} else if ( ( result = select_store_pattern.exec( line ) ) !== null ) {

				// ["ss 7 2 1, "7.0", "2.0", "1.0"]
                var reslt2 = result[0].split(" "); // split the line into an array
                reslt2.shift(); // remove the first element
                var reslt3 = new Array();
                for(var ki = 0; ki < reslt2.length; ki++){
                    reslt3.push(parseInt(reslt2[ki]) - 1); //turn array into int array
                }

				geometry.selectedStore.push(reslt3);

			} else if ( ( result = vertex_pattern.exec( line ) ) !== null ) {

				// ["v 1.0 2.0 3.0", "1.0", "2.0", "3.0"]

				geometry.vertices.push(
                    new THREE.Vector3(
					parseFloat( result[ 1 ] ),
					parseFloat( result[ 2 ] ),
					parseFloat( result[ 3 ] )));

			} else if ( ( result = mod_vertex_pattern.exec( line ) ) !== null ) {

				// ["mv 1.0 2.0 3.0", "1.0", "2.0", "3.0"]

				geometry.vertices_mod.push(
                    new THREE.Vector3(
					parseFloat( result[ 1 ] ),
					parseFloat( result[ 2 ] ),
					parseFloat( result[ 3 ] )));

			} else if ( ( result = normal_pattern.exec( line ) ) !== null ) {

				// ["vn 1.0 2.0 3.0", "1.0", "2.0", "3.0"]

				geometry.normals.push(
                    new THREE.Vector3(
					parseFloat( result[ 1 ] ),
					parseFloat( result[ 2 ] ),
					parseFloat( result[ 3 ] )));

			} else if ( ( result = uv_pattern.exec( line ) ) !== null ) {

				// ["vt 0.1 0.2", "0.1", "0.2"]

				geometry.uvs.push(
                    new THREE.Vector2(
					parseFloat( result[ 1 ] ),
					parseFloat( result[ 2 ] )));

			} else if ( ( result = mod_uv_pattern.exec( line ) ) !== null ) {

				// ["mvt 0.1 0.2", "0.1", "0.2"]

				geometry.uvs_mod.push(
                    new THREE.Vector2(
					parseFloat( result[ 1 ] ),
					parseFloat( result[ 2 ] )));

			} else if ( ( result = face_pattern1.exec( line ) ) !== null ) {

				// ["f 1 2 3", "1", "2", "3", undefined]
  				addFace(
					result[ 1 ], result[ 2 ], result[ 3 ], result[ 4 ]
				);

			} else if ( ( result = face_pattern2.exec( line ) ) !== null ) {

				// ["f 1/1 2/2 3/3", " 1/1", "1", "1", " 2/2", "2", "2", " 3/3", "3", "3", undefined, undefined, undefined]
                addFace(
					result[ 2 ], result[ 5 ], result[ 8 ], result[ 11 ],
					result[ 3 ], result[ 6 ], result[ 9 ], result[ 12 ]
				);

			} else if ( ( result = face_pattern3.exec( line ) ) !== null ) {

				// ["f 1/1/1 2/2/2 3/3/3", " 1/1/1", "1", "1", "1", " 2/2/2", "2", "2", "2", " 3/3/3", "3", "3", "3", undefined, undefined, undefined, undefined]

				addFace(
					result[ 2 ], result[ 6 ], result[ 10 ], result[ 14 ],
					result[ 3 ], result[ 7 ], result[ 11 ], result[ 15 ],
					result[ 4 ], result[ 8 ], result[ 12 ], result[ 16 ]
				);

			} else if ( ( result = face_pattern4.exec( line ) ) !== null ) {

				// ["f 1//1 2//2 3//3", " 1//1", "1", "1", " 2//2", "2", "2", " 3//3", "3", "3", undefined, undefined, undefined]

				addFace(
					result[ 2 ], result[ 5 ], result[ 8 ], result[ 11 ],
					undefined, undefined, undefined, undefined,
					result[ 3 ], result[ 6 ], result[ 9 ], result[ 12 ]
				);

			} else if ( /^o /.test( line ) ) {

                // check if this is still the first geometry, if yes, simply
                // count up, otherwise create a new geometry
                //
                // this fix makes sure that malformed OBJ's with the o - tag at the beginning
                // of the file can be read without error.
                if(geometryCounter == 0){
                    geometryCounter++;
                } else {
                    geometryCounter++;

                    geometry = new WARP.Geometry();

                    ldim = [];
                    lrim = [0, 0, 0, 0];
                    lvertices = [];

                    lattice = {
                        ldim: ldim,
                        lrim: lrim,
                        lvertices: lvertices
                    };

                    material = {
                        name: ''
                    };

                    object = {
                        name: line.substring( 2 ).trim(),
                        geometry: geometry,
                        material: material,
                        lattice: lattice
                    };

                    this.objects.push( object )
                }

			} else if ( /^g /.test( line ) ) {

				// group

			} else if ( /^usemtl /.test( line ) ) {

				// material

				material.name = line.substring( 7 ).trim();

			} else if ( /^mtllib /.test( line ) ) {

				// mtl file

			} else if ( /^s /.test( line ) ) {

				// smooth shading

			} else {

				// console.log( "THREE.OBJLoader: Unhandled line " + line );

			}
		}

 		for ( var i = 0, l = this.objects.length; i < l; i ++ ) {

			object = this.objects[ i ];
			geometry = object.geometry;
            lattice = object.lattice;

            return true;
		}

		error( 'WarpLoader: no valid lattice found\n' );
        return false;
 	},

    setLattice: function ( _lattice ) {
  		for ( var i = 0, l = this.objects.length; i < l; i ++ ) {
			var object = this.objects[ i ];
			var lattice = object.lattice;

            if(lattice.lvertices.length == lattice.ldim[0] * lattice.ldim[1]){
                _lattice.load(lattice.ldim, lattice.lrim, lattice.lvertices);
            } else {
                 error( 'WarpLoader: lattice dimension doesnt match with lattice vertices!\n' );
            }
		}
    },

    setMesh: function ( _geometryManager ) {
  		for ( var i = 0, l = this.objects.length; i < l; i ++ ) {
            var object = this.objects[ i ];
			var geometry = object.geometry;
            //post("setMesh : " + geometry.uvs.length + "\n");

            if(geometry.uvs.length == 0){
                error( 'WarpLoader: mesh ('+this.fileName+') doesnt contain any uvs!\n' );
                return false;
            } else {
 //               post("have " + geometry.vertices.length + "\n");
                _geometryManager.load(geometry);
                return true;
            }
 		}
    }


};
