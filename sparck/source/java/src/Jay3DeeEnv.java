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
import com.cycling74.jitter.*;
import java.util.*;

import com.tecartlab.jay3dee.*;
import com.tecartlab.utils.Debug;

/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */

public class Jay3DeeEnv extends MaxObject {

	public Jay3DeeEnv(Atom args[])
	{
		Debug.setLevelToError();
		declareIO(1,1);
		declareAttribute("debug", null, "debug");
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

	public void notifyDeleted()
	{
		Env.getEnv().terminate();
	}

}
