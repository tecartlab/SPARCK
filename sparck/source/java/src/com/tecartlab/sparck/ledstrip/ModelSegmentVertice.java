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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.tecartlab.tools.math.la.Vector3f;
import com.tecartlab.utils.Debug;

public class ModelSegmentVertice {

	protected static final String ELEMENT_VERTICE = "coord";
	private static final String ATTR_LOCAL_X = "x";
	private static final String ATTR_LOCAL_Y = "y";
	private static final String ATTR_LOCAL_Z = "z";

	protected Vector3f modelLocalVertice;

	public ModelSegmentVertice(Node vertice){
		NamedNodeMap values = vertice.getAttributes();
		Node s_localX = values.getNamedItem(ATTR_LOCAL_X);
		Node s_localY = values.getNamedItem(ATTR_LOCAL_Y);
		Node s_localZ = values.getNamedItem(ATTR_LOCAL_Z);

		modelLocalVertice = new Vector3f(
				(Float.valueOf(s_localX.getTextContent())).floatValue(),
				(Float.valueOf(s_localY.getTextContent())).floatValue(),
				(Float.valueOf(s_localZ.getTextContent())).floatValue());

		Debug.verbose("Led Strip Segment Vertice", " vertice data = " + modelLocalVertice.toString());
	}

}
