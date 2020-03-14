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

import com.cycling74.jitter.JitterObject;

import com.tecartlab.mxj.utils.JitterObjectArray;

public class EditorHelper {

	JitterObject helpLine[] = new JitterObject[20];

	float currentColor = 1.0f;

	public EditorHelper(){
		for(int i = 0; i < helpLine.length; i++){
			helpLine[i] = new JitterObject("jit.gl.text");
			set(helpLine[i],new float[]{-.95f, 0.9f-.095f * i, 0.f});
		}
	}

	private void set(JitterObject _set, float[] _pos){
		_set.call("drawto", "editor");
		_set.call("layer", 20);
		_set.call("font", "Courier New");
		_set.call("transform_reset", 2);
		_set.call("face", "bold");
		_set.call("scale", 1.f);
		_set.call("size", 16);
		_set.call("position",  _pos );
		_set.call("color",  new float[]{currentColor, currentColor, currentColor, 1.0f} );
	}

	public void changeColor(){
		currentColor = (currentColor < 0f)? 1.0f: currentColor - 0.1f;
		for(int i = 0; i < helpLine.length; i++){
			helpLine[i].call("color",  new float[]{currentColor, currentColor, currentColor, 1.0f});
		}
	}

	public void enable(int _enable){
		for(int i = 0; i < helpLine.length; i++){
			helpLine[i].call("enable", _enable);
		}
	}

	public void printModelNONE(){
		helpLine[0].call("text", "Mode: [ MODEL ]");
		helpLine[1].call("text", "tab   : switch Mode");
		helpLine[2].call("text", "mouse : select vertice");
		helpLine[3].call("text", "s     : save calibration");
		helpLine[4].call("text", "p     : purge calibration");
		helpLine[5].call("text", "j     : adjust ALL target to warp- or model-vertice");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "");
		helpLine[16].call("text", "k     : toggle visibility of calibration results");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printModelSEL(){
		helpLine[0].call("text", "Mode: [ MODEL ]");
		helpLine[1].call("text", "tab   : switch Mode");
		helpLine[2].call("text", "v     : create calibration-vertice (target and warp)");
		helpLine[3].call("text", "w     : create warp-vertice linked to model-vertice");
		helpLine[4].call("text", "t     : create target-vertice linked to model-vertice");
		helpLine[5].call("text", "x     : delete calibration-vertice");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "");
		helpLine[16].call("text", "k     : toggle visibility of calibration results");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printTargetSEL(int _disregardID){
		helpLine[0].call("text", "Mode: [ TARGET ]");
		helpLine[1].call("text", "j     : adjust selected to warp- or model-vertice");
		helpLine[2].call("text", "w     : add warp-vertice linked to model-vertice");
		helpLine[3].call("text", "x     : remove target-vertice");
		helpLine[4].call("text", "g     : grab vertice with mouse");
		helpLine[5].call("text", "<^>   : move vertice with arrow keys");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "c     : toggle cross hair mode");
		helpLine[15].call("text", "o     : toggle visibility of calibration object");
		helpLine[16].call("text", "k     : toggle visibility of calibration results");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printTargetGRAB(){
		helpLine[0].call("text", "Mode: [ TARGET ]");
		helpLine[1].call("text", "mouse : move target, confirm with mouse click");
		helpLine[2].call("text", "");
		helpLine[3].call("text", "");
		helpLine[4].call("text", "");
		helpLine[5].call("text", "");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "o     : toggle visibility of calibration object");
		helpLine[16].call("text", "k     : toggle visibility of calibration results");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printTargetVALUE_J(){
		helpLine[0].call("text", "Mode: [ TARGET ]: adjust target-vertice");
		helpLine[1].call("text", "m     : adjust to model-vertice");
		helpLine[2].call("text", "w     : adjust to warp-vertice");
		helpLine[3].call("text", "esc   : exit entermode");
		helpLine[4].call("text", "");
		helpLine[5].call("text", "");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "o     : toggle visibility of calibration object");
		helpLine[16].call("text", "k     : toggle visibility of calibration results");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printWarpNONE(int _frame, int _subdiv){
		helpLine[0].call("text", "Mode: [ WARP ]");
		helpLine[1].call("text", "tab   : switch Mode");
		helpLine[2].call("text", "mouse : select warp-vertice");
		helpLine[3].call("text", "j     : adjust all to target- or model-vertice");
		helpLine[4].call("text", "w     : create unlinked warp-vertice");
		helpLine[5].call("text", "s     : save warp-object");
		helpLine[6].call("text", "f ["+_frame+"] : enter frame subdivision");
		helpLine[7].call("text", "d ["+_subdiv+"] : enter warp subdivision");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "");
		helpLine[16].call("text", "");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printWarpSEL(boolean isDoubleSelected){
		helpLine[0].call("text", "Mode: [ WARP ]");
		helpLine[1].call("text", "tab   : switch Mode");
		helpLine[2].call("text", "mouse : select second model-vertice");
		helpLine[3].call("text", "j     : adjust to target- or model-vertice");
		helpLine[4].call("text", "t     : add target-vertice linked to model-vertice");
		helpLine[5].call("text", "x     : remove warp-vertice");
		helpLine[6].call("text", "g     : grab and move vertice with mouse");
		helpLine[7].call("text", "<^>   : move vertice with keys");
		if(isDoubleSelected)
			helpLine[8].call("text", "0-9   : create subdiv line between two warp vertices");
		else
			helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "");
		helpLine[16].call("text", "");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printWarpGRAB(){
		helpLine[0].call("text", "Mode: [ WARP ]");
		helpLine[1].call("text", "tab   : switch Mode");
		helpLine[2].call("text", "mouse : move target, confirm with mouse click");
		helpLine[3].call("text", "");
		helpLine[4].call("text", "");
		helpLine[5].call("text", "");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "");
		helpLine[16].call("text", "");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printWarpVALUE_F(){
		helpLine[0].call("text", "Mode: [ WARP ]: set frame");
		helpLine[1].call("text", "0-9   : set frame subdiv");
		helpLine[2].call("text", "esc   : exit entermode");
		helpLine[3].call("text", "");
		helpLine[4].call("text", "");
		helpLine[5].call("text", "");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "");
		helpLine[16].call("text", "");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printWarpVALUE_D(){
		helpLine[0].call("text", "Mode: [ WARP ]: set subdivision");
		helpLine[1].call("text", "0-5   : set frame subdiv (only 2 are displayed)");
		helpLine[2].call("text", "esc   : exit entermode");
		helpLine[3].call("text", "");
		helpLine[4].call("text", "");
		helpLine[5].call("text", "");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "");
		helpLine[16].call("text", "");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printWarpVALUE_J(){
		helpLine[0].call("text", "Mode: [ WARP ]: adjust warp-vertice");
		helpLine[1].call("text", "m     : adjust to model-vertice");
		helpLine[2].call("text", "t     : adjust to target-vertice");
		helpLine[3].call("text", "esc   : exit entermode");
		helpLine[4].call("text", "");
		helpLine[5].call("text", "");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "");
		helpLine[16].call("text", "");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void printModelVALUE_P(){
		helpLine[0].call("text", "Mode:  [ MODEL ]: purge calibration?");
		helpLine[1].call("text", "y     : confirm");
		helpLine[2].call("text", "esc   : cancel");
		helpLine[3].call("text", "");
		helpLine[4].call("text", "");
		helpLine[5].call("text", "");
		helpLine[6].call("text", "");
		helpLine[7].call("text", "");
		helpLine[8].call("text", "");
		helpLine[9].call("text", "");
		helpLine[10].call("text", "");
		helpLine[11].call("text", "");
		helpLine[12].call("text", "");
		helpLine[13].call("text", "");
		helpLine[14].call("text", "");
		helpLine[15].call("text", "");
		helpLine[16].call("text", "k     : toggle visibility of calibration results");
		helpLine[17].call("text", "l     : toggle visibility of labels");
		helpLine[18].call("text", "H     : change help text color");
		helpLine[19].call("text", "h     : toggle this help");
	}

	public void free(){
		for(int i = 0; i < helpLine.length; i++){
			helpLine[i].freePeer();
		}
	}
}
