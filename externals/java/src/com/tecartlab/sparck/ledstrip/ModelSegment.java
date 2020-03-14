package com.tecartlab.sparck.ledstrip;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import toxi.geom.Vec3D;

import com.cycling74.jitter.JitterObject;
import com.cycling74.max.Atom;

import com.tecartlab.mxj.utils.JitterObjectArray;
import com.tecartlab.jay3dee.model.Fast3DModelling;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.utils.Debug;

public class ModelSegment {

	protected static final String ELEMENT_SEGMENT 		= "segment";
	protected static final String		ATTR_NAME 			= "name";

	private ArrayList<ModelSegmentVertice> vertices;

	protected String name;

	private float[] color;

	private int totalLeds = 0;

	private Fast3DModelling objectToStage2;

	private JitterObject objectToStage;
	private JitterObject objectToRender;

	private float pointSize = 0.001f;

	private int direction;

	public ModelSegment(Node _segment, float[] _color, Fast3DModelling _objectToStage2, JitterObject _objectToStage, JitterObject _objectToRender){
		color = _color;
		vertices = new ArrayList<ModelSegmentVertice>();
		objectToStage = _objectToStage;
		objectToRender = _objectToRender;
		objectToStage2 = _objectToStage2;

		 /*
			<segment name="BezierCurve.001">
				<coord x="-1.00" y="0.00" z="0.00"></coord>
				<coord x="-0.62" y="0.21" z="0.00"></coord>
				<coord x="-0.19" y="0.19" z="0.00"></coord>
				<coord x="0.34" y="0.07" z="0.00"></coord>
				<coord x="1.00" y="0.00" z="0.00"></coord>
			</segment>
		  */

		NamedNodeMap values = _segment.getAttributes();
		Node s_type = values.getNamedItem(ATTR_NAME);

		name = s_type.getTextContent();

		NodeList verticeList = _segment.getChildNodes();

		for(int i = 0; i < verticeList.getLength(); i++){
			Node vertice = verticeList.item(i);
			if(vertice.getNodeName().equals(ModelSegmentVertice.ELEMENT_VERTICE)){
				ModelSegmentVertice newVertice = new ModelSegmentVertice(vertice);
				vertices.add(newVertice);
			}
		}

		direction = 0;

		Debug.verbose("Led Strip Segment", " " + name + " loaded " + vertices.size() + " vertice");

	}

	public void setPointSize(float _pointSize){
		pointSize = _pointSize;
	}

	public void setColor(float[] _color){
		color = _color;
	}

	protected void drawAsUnused(){
		if(vertices.size() > 1){
			objectToStage.call("gllinewidth", 2.f);
			objectToStage.call("glcolor", Atom.newAtom(new float[]{0.5f, 0.5f, 0.5f, 1.0f}));
			objectToStage.call("moveto", vertices.get(0).modelLocalVertice.get());
			for(int i = 1; i < vertices.size(); i++){
				objectToStage.call("lineto", vertices.get(i).modelLocalVertice.get());
			}
		}
	}

	protected int draw(boolean _render4Shader, int _stripID, int _ledIdStartIndex, float _ledsPerMeter, int _ledInset, int _ledOffset, int _direction){
		setDirection(_direction);
		totalLeds = _ledIdStartIndex;
		if(vertices.size() > 1 && !_render4Shader){
			objectToStage.call("gllinewidth", 2.f);
			objectToStage.call("glcolor", Atom.newAtom(color));
			objectToStage.call("moveto", vertices.get(0).modelLocalVertice.get());
			for(int i = 1; i < vertices.size(); i++){
				objectToStage.call("lineto", vertices.get(i).modelLocalVertice.get());
			}
		}

		if(vertices.size() > 1){
			// draw leds on segment
			objectToStage.call("glcolor", Atom.newAtom(new float[]{1f, 1f, 1f, 1f}));
			objectToRender.call("glpointsize", 1.f);

			// draw ledInlet
			if(!_render4Shader){
				objectToStage.call("glpointsize", pointSize * 1000.f);
				objectToStage.call("glcolor", Atom.newAtom(new float[]{0f, 1f, 0f, 1f}));
				// draw green dots for positive inlets
				for(int i = 0; i < _ledInset; i++){
					//objectToStage.call("moveto", getPixelPosition(1f / (float) ledCount * i).get());
					objectToStage.call("point", vertices.get(0).modelLocalVertice.addMake(getFirstSegmentLine().scale((i + 1) * 1.f / _ledsPerMeter)).get());
				}
				// draw yellow dots for negative inlets
				objectToStage.call("glcolor", Atom.newAtom(new float[]{1f, 1f, 0f, 1f}));
				for(int i = _ledInset; i < 0; i++){
					//objectToStage.call("moveto", getPixelPosition(1f / (float) ledCount * i).get());
					objectToStage.call("point", vertices.get(0).modelLocalVertice.addMake(getFirstSegmentLine().scale((i + 1) * 1.f / _ledsPerMeter)).get());
				}
			}

			// draw leds
			float segLength = getSegmentLength();
			int ledCount = (int)(segLength * _ledsPerMeter);
			int stripID = 255 - _stripID;

			int ledIndexDigit34 = 0;
			int ledIndexDigit12 = 0;

			// change sign of ledinset to make the following algorithm simpler
			_ledInset = _ledInset * -1;

			for(int i = _ledInset; i < ledCount + _ledOffset; i++){
				// draw leds only if the _ledIdStartIndex is >= 0
				//objectToStage.call("moveto", getPixelPosition(1f / (float) ledCount * i).get());
				float[] pixelPos = getPixelPosition(i, ledCount, _ledsPerMeter).get();
				objectToStage2.addOctahedron(new Vec3D(pixelPos[0], pixelPos[1], pixelPos[2]), pointSize);
				// draw ledInlet
				if(i >= 0){
					ledIndexDigit34 = _ledIdStartIndex / 100;
					ledIndexDigit12 = _ledIdStartIndex % 100;
					objectToRender.call("glcolor", Atom.newAtom(new float[]{(float)stripID / 255f, (float)ledIndexDigit12 / 255f, (float)ledIndexDigit34 / 255f, 1f}));
					objectToRender.call("point", pixelPos);
				}

				_ledIdStartIndex ++;
			}

			// draw ledOutlet
			if(!_render4Shader){
				objectToStage.call("glpointsize", pointSize * 1000.f);
				// draw blue dots for negative inlets
				objectToStage.call("glcolor", Atom.newAtom(new float[]{0f, 0f, 1f, 1f}));
				for(int i = 0; i < _ledOffset; i++){
					//objectToStage.call("moveto", getPixelPosition(1f / (float) ledCount * i).get());
					objectToStage.call("point", vertices.get(vertices.size() - 1).modelLocalVertice.addMake(getLastSegmentLine().scale((i + 1) * 1.f / _ledsPerMeter)).get());
				}
				// draw magenta dots for negative inlets
				objectToStage.call("glcolor", Atom.newAtom(new float[]{1f, 0f, 1f, 1f}));
				for(int i = _ledOffset; i < 0; i++){
					//objectToStage.call("moveto", getPixelPosition(1f / (float) ledCount * i).get());
					objectToStage.call("point", vertices.get(vertices.size() - 1).modelLocalVertice.addMake(getLastSegmentLine().scale((i + 1) * 1.f / _ledsPerMeter)).get());
				}
			}

		}
		totalLeds = _ledIdStartIndex - totalLeds;

		return _ledIdStartIndex;
	}

	/**
	 * switches the direction of this segment through swapping the vertices.
	 * @param _dir
	 */
	private void setDirection(int _dir){
		if(direction != _dir){
			direction = _dir;
			ArrayList<ModelSegmentVertice> newDir = new ArrayList<ModelSegmentVertice>();
			for(int i = 0; i < vertices.size(); i++){
				newDir.add(0, vertices.get(i));
			}
			vertices = newDir;
		}
	}

	public int getSegmentLedCount(){
		return totalLeds;
	}

	public float getSegmentLength(){
		float length = 0;
		for(int i = 0; i < vertices.size() - 1; i++){
			length += vertices.get(i + 1).modelLocalVertice.distance(vertices.get(i).modelLocalVertice);
		}
		return length;
	}

	private Vector3f getFirstSegmentLine(){
		return vertices.get(0).modelLocalVertice.subMake(vertices.get(1).modelLocalVertice).normalize();
	}

	private Vector3f getLastSegmentLine(){
		return vertices.get(vertices.size() - 1).modelLocalVertice.subMake(vertices.get(vertices.size() - 2).modelLocalVertice).normalize();
	}

	private Vector3f getPixelPosition(int _index, int _segLeds, float _ledsPerMeter){
		// calculate
		if(_index < 0){
			return vertices.get(0).modelLocalVertice.addMake(getFirstSegmentLine().scale((float)(_index) * - 1.f / _ledsPerMeter));
		} else if(_index >= _segLeds){
			return vertices.get(vertices.size() - 1).modelLocalVertice.addMake(getLastSegmentLine().scale((float)(_index - _segLeds + 1) * 1.f / _ledsPerMeter));
		} else {
			float _pos = (float)_index / _ledsPerMeter;
			Vector3f pixel;
			float length_in = 0;
			float length_out = 0;
			for(int i = 0; i < vertices.size() - 1; i++){
				length_in += vertices.get(i + 1).modelLocalVertice.distance(vertices.get(i).modelLocalVertice);
				if(length_in > _pos && length_out < _pos){
					pixel = vertices.get(i).modelLocalVertice.clone();
					pixel.add(vertices.get(i + 1).modelLocalVertice.subMake(vertices.get(i).modelLocalVertice).normalize().scale(_pos - length_out));
					return pixel;
				}
				length_out = length_in;
			}
		}
		return vertices.get(0).modelLocalVertice.clone();
	}

	/**
	 * Clears all vertices
	 */
	public void clear(){
		vertices.clear();
	}

}
