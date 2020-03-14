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
 
WARP.MeshLoader = function ( ) {
    this.objects;
};

WARP.MeshLoader.prototype = {

	constructor: WARP.MeshLoader,

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
			f.close();
			return this.parse( lines );
		} else {
			error("MeshLoader: could not open file: " + url + "\n");
		}
        return false;
	},

  	parse: function ( lines ) {

        this.objects = [];
		var object, geometry, material;

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

		// v float float float

		var vertex_pattern = /v( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

		// vn float float float

		var normal_pattern = /vn( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

		// vt float float

		var uv_pattern = /vt( +[\d|\.|\+|\-|e|E]+)( +[\d|\.|\+|\-|e|E]+)/;

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

		for ( var i = 0; i < lines.length; i ++ ) {

			var line = lines[ i ];
			line = line.trim();

			var result;

			if ( line.length === 0 || line.charAt( 0 ) === '#' ) {

				continue;

			} else if ( ( result = vertex_pattern.exec( line ) ) !== null ) {

				// ["v 1.0 2.0 3.0", "1.0", "2.0", "3.0"]

				geometry.vertices.push(
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

				geometry = new WARP.Geometry();

				material = {
					name: ''
				};

				object = {
					name: line.substring( 2 ).trim(),
					geometry: geometry,
					material: material
				};

				this.objects.push( object )

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

            if(geometry.uvs.length == 0){
                error( 'MeshLoader: mesh doesnt contain any uvs!' );
                return false;
            } else if(geometry.normals.length == 0){
                error( 'MeshLoader: mesh doesnt contain any normals!' );
                return false;
            } else {
                //post( 'MeshLoader: mesh matches!' );
                return true;
            }
		}

		error( 'MeshLoader: no valid geometry found' );
        return false;
 	},

    getMesh: function ( ) {
  		for ( var i = 0, l = this.objects.length; i < l; i ++ ) {
			var object = this.objects[ i ];
			var geometry = object.geometry;

            if(geometry.uvs.length > 0 && geometry.normals.length > 0){
                return geometry;
            }
		}
    },

    setMesh: function ( _geometryManager ) {
  		for ( var i = 0, l = this.objects.length; i < l; i ++ ) {
			var object = this.objects[ i ];
			var geometry = object.geometry;

            if(geometry.uvs.length > 0 && geometry.normals.length > 0){
                _geometryManager.load(geometry);
            }
		}
    }
};
