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

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

import com.tecartlab.utils.Debug;
import com.tecartlab.utils.DebugLogger;

public class Debugger extends MaxObject implements DebugLogger{

	private boolean print = false;

	public Debugger(Atom[] args){
		Debug.setLevelToError();
		declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});
		this.setOutletAssist(new String[]{"info messages", "error messages"});
		this.declareAttribute("print", "getprint", "setprint");
	}

	public void loadbang(){
		Debug.getInstance().setOutputObject(this);
	}

	public void getprint(){
		outlet(2, "print", print);
	}

	public void setprint(int i){
		print = (i == 0)?false:true;
	}

	public void debugger(String _level){
		if(_level.equals("verbose")){
			Debug.setLevelToVerbose();
		}else if(_level.equals("debug")){
			Debug.setLevelToDebug();
		}else if(_level.equals("info")){
			Debug.setLevelToInfo();
		}else if(_level.equals("warning")){
			Debug.setLevelToWarning();
		}else if(_level.equals("error")){
			Debug.setLevelToError();
		}else if(_level.equals("fatal")){
			Debug.setLevelToFatal();
		}
	}

	public void printInfo(boolean _showTime, String _level, String _className, String _message) {
		Atom[] mylist = null;
		if(_showTime){
			mylist = new Atom[]{
					Atom.newAtom("[" + System.currentTimeMillis() + "]"),
					Atom.newAtom(_level),
					Atom.newAtom(_className),
					Atom.newAtom(_message)};
		} else {
			mylist = new Atom[]{
					Atom.newAtom(_level),
					Atom.newAtom(_className),
					Atom.newAtom(_message)};
		}
		outlet(0, mylist);
		if(print)
			post(Atom.toOneString(mylist));
	}

	public void printError(boolean _showTime, String _level, String _className, String _message) {
		Atom[] mylist = null;
		if(_showTime){
			mylist = new Atom[]{
					Atom.newAtom("[" + System.currentTimeMillis() + "]"),
					Atom.newAtom(_level),
					Atom.newAtom(_className),
					Atom.newAtom(_message)};
		} else {
			mylist = new Atom[]{
					Atom.newAtom(_level),
					Atom.newAtom(_className),
					Atom.newAtom(_message)};
		}
		outlet(1, mylist);
		if(print)
			error(Atom.toOneString(mylist));

	}
}
