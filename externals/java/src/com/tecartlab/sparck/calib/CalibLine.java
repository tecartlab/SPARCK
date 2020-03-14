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

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.tecartlab.jay3dee.model.Vertice;
import com.tecartlab.mxj.utils.JitterObjectArray;
import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.utils.Debug;

public class CalibLine{

	protected int subdiv;

	protected static final String ELEMENT_LINE = "line";
	private static final String ATTR_INDEX1 = "vertexid1";
	private static final String ATTR_INDEX2 = "vertexid2";
	private static final String ATTR_SUBDIV = "subdiv";

	CalibVertice verticeA;
	CalibVertice verticeB;

	protected ArrayList<DelaunayVertice> delaunays;

	protected JitterObjectArray drawableText;

	protected CalibLine(CalibVertice _vertexid1, CalibVertice _vertexid2, int _subdiv){
		setup(_vertexid1, _vertexid2, _subdiv);
	}

	protected CalibLine(Node line, ArrayList<CalibVertice> vertices){
		NamedNodeMap values = line.getAttributes();
		Node s_index1 = values.getNamedItem(ATTR_INDEX1);
		Node s_index2 = values.getNamedItem(ATTR_INDEX2);
		Node s_subdiv = values.getNamedItem(ATTR_SUBDIV);
		int vertexid1 = (Integer.valueOf(s_index1.getTextContent())).intValue();
		int vertexid2 = (Integer.valueOf(s_index2.getTextContent())).intValue();
		int _sub = (Integer.valueOf(s_subdiv.getTextContent())).intValue();
		CalibVertice _vertexid1 = null;
		CalibVertice _vertexid2 = null;
		for(int i = 0; i < vertices.size(); i++){
			if(_vertexid1 == null){
				if(vertices.get(i).getIndex() == vertexid1)
					_vertexid1 = vertices.get(i);
			}else if(_vertexid2 == null){
				if(vertices.get(i).getIndex() == vertexid2)
					_vertexid2 = vertices.get(i);
			}
		}
		setup(_vertexid1, _vertexid2, _sub);
	}

	private void setup(CalibVertice _vertexid1, CalibVertice _vertexid2, int _subdiv){
		verticeA = _vertexid1;
		verticeB = _vertexid2;
		subdiv = _subdiv;
		delaunays = new ArrayList<DelaunayVertice>();
		update();
	}

	protected void update(){
		delaunays.clear();
		if(verticeA.isReady() && verticeB.isReady()){
			for(int i = 0; i < subdiv; i++){
				Vector3f vertice = verticeA.getWarpVerticeOrthoNormal().addMake(
						verticeB.getWarpVerticeOrthoNormal().subMake(verticeA.getWarpVerticeOrthoNormal()).divide((float)(subdiv + 1.f)).scale((float)(i+1)));
				Vector3f vta = (verticeA.isWarpOnlyType())?verticeA.getTargetVertice(): verticeA.getModelScreenVertice();
				Vector3f vtb = (verticeB.isWarpOnlyType())?verticeB.getTargetVertice(): verticeB.getModelScreenVertice();
				Vector3f texture = vta.addMake(vtb.subMake(vta).divide((float)(subdiv + 1.f)).scale((float)(i+1)));
				delaunays.add(new DelaunayVertice(vertice, texture));
			}
		}
	}

	protected void addNodeTo(Document document){
        Element verticeNode  = document.createElement(ELEMENT_LINE);

        Attr s_index1 = document.createAttribute(ATTR_INDEX1);
        s_index1.setValue(""+verticeA.getIndex());
        verticeNode.setAttributeNode(s_index1);

        Attr s_index2 = document.createAttribute(ATTR_INDEX2);
        s_index2.setValue(""+verticeB.getIndex());
        verticeNode.setAttributeNode(s_index2);

        Attr s_subdiv = document.createAttribute(ATTR_SUBDIV);
        s_subdiv.setValue(""+subdiv);
        verticeNode.setAttributeNode(s_subdiv);

        document.getDocumentElement().appendChild(verticeNode);
	}

	protected boolean hasVertice(CalibVertice _vertexid1){
		if(verticeA.getIndex() == _vertexid1.getIndex() || verticeB.getIndex() == _vertexid1.getIndex())
			return true;
		return false;
	}

	protected boolean isIdentical(CalibVertice _vertexid1, CalibVertice _vertexid2){
		if(verticeA.getIndex() == _vertexid1.getIndex() && verticeB.getIndex() == _vertexid2.getIndex() ||
			verticeB.getIndex() == _vertexid1.getIndex() && verticeA.getIndex() == _vertexid2.getIndex())
			return true;
		return false;

	}

	protected void clear(){
		verticeA = null;
		verticeB = null;
	}

}
