package com.tecartlab.sparck.calib;
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

import com.cycling74.max.*;

public class GLCommands {

	public static Atom[] sketch_glcolor(float[] color){
		if(color.length == 4){
			return new Atom[]{Atom.newAtom("glcolor"), Atom.newAtom(color[0]), Atom.newAtom(color[1]), Atom.newAtom(color[2]), Atom.newAtom(color[3])};
		}
		return new Atom[]{Atom.newAtom("glcolor"), Atom.newAtom(1.f), Atom.newAtom(1.f), Atom.newAtom(1.f), Atom.newAtom(1.f)};
	}

	public static Atom[] sketch_reset(){
		return new Atom[]{Atom.newAtom("reset")};
	}

	public static Atom[] sketch_moveto(float x, float y, float z){
		return new Atom[]{Atom.newAtom("moveto"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)};
	}

	public static Atom[] sketch_lineto(float x, float y, float z){
		return new Atom[]{Atom.newAtom("lineto"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)};
	}

	public static Atom[] sketch_point(float x, float y, float z){
		return new Atom[]{Atom.newAtom("point"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)};
	}

	public static Atom[] sketch_pointSize(float size){
		return new Atom[]{Atom.newAtom("point_size"), Atom.newAtom(size)};
	}

	public static Atom[] sketch_beginPlygonShape(){
		return new Atom[]{Atom.newAtom("glbegin"), Atom.newAtom("polygon")};
	}

	public static Atom[] sketch_vertex(float x, float y, float z){
		return new Atom[]{Atom.newAtom("glvertex"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)};
	}

	public static Atom[] sketch_normal(float x, float y, float z){
		return new Atom[]{Atom.newAtom("glnormal"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)};
	}

	public static Atom[] sketch_texture(float x, float y){
		return new Atom[]{Atom.newAtom("gltexcoord"), Atom.newAtom(x), Atom.newAtom(y)};
	}

	public static Atom[] matrix_setCell(int _idx, float vx, float vy, float vz, float ux, float uy, float nx, float ny, float nz){
		return new Atom[]{
				Atom.newAtom(_idx),
				Atom.newAtom("val"),
				Atom.newAtom(vx),
				Atom.newAtom(vy),
				Atom.newAtom(vz),
				Atom.newAtom(ux),
				Atom.newAtom(uy),
				Atom.newAtom(nx),
				Atom.newAtom(ny),
				Atom.newAtom(nz),
				Atom.newAtom(1.f),
				Atom.newAtom(1.f),
				Atom.newAtom(1.f),
				Atom.newAtom(1.f),
				};
	}

	public static Atom[] sketch_endShape(){
		return new Atom[]{Atom.newAtom("glend")};
	}

}
