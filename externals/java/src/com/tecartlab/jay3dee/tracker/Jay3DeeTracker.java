package com.tecartlab.jay3dee.tracker;

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

import com.tecartlab.utils.Debug;
import com.tecartlab.utils.dyndist.DynException;
import com.tecartlab.utils.dyndist.DynPublication;
import com.tecartlab.utils.dyndist.DynPublisher;
import com.tecartlab.utils.dyndist.DynSubscription;
import com.tecartlab.jay3dee.*;
import com.tecartlab.jay3dee.tracker.GuiTracker;
import com.tecartlab.jay3dee.tracker.MouseEvent;

public class Jay3DeeTracker extends MaxObject {

	private String name;
	private String context;
	private String cameraname;

	private GuiTracker tracker;

	public Jay3DeeTracker(Atom args[])
	{
		if (args.length < 3)
			Debug.info("Jay3DeeTracker", "needs a name, a context and a cameraname as arguments. otherwise use 'setname', 'setcontext', 'setcameraname'. BEWARE: all messages are discarded as long those values are not set");
		else{
			name = args[0].toString();
			context = args[1].toString();
			cameraname = args[2].toString();
			init();
		}

		declareIO(1,1);
	}

	private void init(){
		if(name!= null && context != null && cameraname != null){
			notifyDeleted();
			tracker = new GuiTracker(name, context, cameraname);
			try {
				tracker.init();
			} catch (DynException e) {
				Debug.fatal("Jay3DeeTracker" , e.getMessage());
			}
		}
	}

	public void setname(String _name){
		name = _name;
		init();
	}

	public void setcontext(String _context){
		context = _context;
		init();
	}

	public void setcameraname(String _cameraname){
		cameraname = _cameraname;
		init();
	}

	public void enable(int _enable){
		if(tracker != null)
			tracker.enable(_enable);
	}

	public void j3dcontext(String _context){
		if(tracker != null)
			tracker.j3dcontext(_context);
	}

	public void cameraorbiting(int _type){
		if(tracker != null)
			tracker.cameraorbiting(_type);
	}

	public void mouse(Atom[] args){
		if(tracker != null)
			tracker.mouseEvent(new MouseEvent(args));
	}

	public void key(Atom[] args){
		if(tracker != null)
			tracker.keyEvent(new KeyEvent(args[0].toInt(), args[1].toInt()));
	}

	public void notifyDeleted(){
		if(tracker != null)
			tracker.notifyDeleted();
	}

}
