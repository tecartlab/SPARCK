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

package com.tecartlab.jay3dee.model.drawer;

import com.cycling74.max.*;

public class MaxSketchMessageFactory {

	public static MaxSketchMessage sketch_glcolor(float[] color){
		if(color.length == 4){
			return new MaxSketchMessage(new Atom[]{Atom.newAtom("glcolor"), Atom.newAtom(color[0]), Atom.newAtom(color[1]), Atom.newAtom(color[2]), Atom.newAtom(color[3])});
		}
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("glcolor"), Atom.newAtom(1.f), Atom.newAtom(1.f), Atom.newAtom(1.f), Atom.newAtom(1.f)});
	}

	public static MaxSketchMessage sketch_reset(){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("reset")});
	}

	public static MaxSketchMessage sketch_moveto(float x, float y, float z){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("moveto"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)});
	}

	public static MaxSketchMessage sketch_lineto(float x, float y, float z){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("lineto"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)});
	}

	public static MaxSketchMessage sketch_point(float x, float y, float z){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("point"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)});
	}

	public static MaxSketchMessage sketch_glpointSize(float size){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("glpointsize"), Atom.newAtom(size)});
	}

	public static MaxSketchMessage sketch_enable(int enable){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("enable"), Atom.newAtom(enable)});
	}

	public static MaxSketchMessage sketch_layer(int layer){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("layer"), Atom.newAtom(layer)});
	}

	public static MaxSketchMessage sketch_context(Atom[] context){
		return new MaxSketchMessage(Atom.union(new Atom[]{Atom.newAtom("drawto")}, context));
	}

	public static MaxSketchMessage sketch_beginShape(){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("glbegin"), Atom.newAtom("polygon")});
	}

	public static MaxSketchMessage sketch_vertex(float x, float y, float z){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("glvertex"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)});
	}

	public static MaxSketchMessage sketch_normal(float x, float y, float z){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("glnormal"), Atom.newAtom(x), Atom.newAtom(y), Atom.newAtom(z)});
	}

	public static MaxSketchMessage sketch_texture(float x, float y){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("gltexcoord"), Atom.newAtom(x), Atom.newAtom(y)});
	}

	public static MaxSketchMessage sketch_endShape(){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("glend")});
	}

	public static MaxSketchMessage sketch_cullFace(int cf){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("cull_face"), Atom.newAtom(cf)});
	}

	public static MaxSketchMessage poly_mode(int pf, int pb){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("poly_mode"), Atom.newAtom(pf), Atom.newAtom(pb)});
	}

	public static MaxSketchMessage draw_mode(String draw_mode){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("draw_mode"), Atom.newAtom(draw_mode)});
	}

	public static MaxSketchMessage lighting_enable(int _enable){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("lighting_enable"), Atom.newAtom(_enable)});
	}

	public static MaxSketchMessage point_size(float _size){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("point_size"), Atom.newAtom(_size)});
	}

	public static MaxSketchMessage blend_enable(int _enable){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("blend_enable"), Atom.newAtom(_enable)});
	}

	public static MaxSketchMessage color(float[] _color){
		return new MaxSketchMessage(new Atom[]{Atom.newAtom("color"), Atom.newAtom(_color[0]), Atom.newAtom(_color[1]), Atom.newAtom(_color[2]), Atom.newAtom(_color[3])});
	}

}
