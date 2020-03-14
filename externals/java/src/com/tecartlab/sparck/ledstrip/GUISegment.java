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

import com.cycling74.max.MaxObject;

public class GUISegment {
	String name;
	float ledsPerMeter;
	int ledinset;
	int ledoffset;
	int direction;
	protected ModelSegment model;

	public GUISegment(String _name, float _ledsPerMeter, int _ledinset, int _ledoffset, int _direction){
		name = _name;
		ledsPerMeter = _ledsPerMeter;
		ledinset = _ledinset;
		ledoffset = _ledoffset;
		direction = _direction;
	}

	public void print(LedStrip _out, int _sID){
		_out.setSegment(_sID, "segmentname", name);
		_out.setSegment(_sID, "ledspermeter", ledsPerMeter);
		_out.setSegment(_sID, "ledinset", ledinset);
		_out.setSegment(_sID, "ledoffset", ledoffset);
		_out.setSegment(_sID, "direction", direction);
	}

	public ModelSegment getModel(){
		return model;
	}

}
