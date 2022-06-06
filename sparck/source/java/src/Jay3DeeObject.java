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
import com.tecartlab.utils.dyndist.DynSubscriber;
import com.tecartlab.utils.dyndist.DynSubscription;
import com.tecartlab.jay3dee.*;
import com.tecartlab.jay3dee.model.drawer.MaxSketchMessage;
import com.tecartlab.jay3dee.tracker.GuiTracker;

/**
 * @author Martin Fr�hlich
 *
 * Max object container for test purposes
 */
public class Jay3DeeObject extends MaxObject{

	ObjectContainer jayobject;

	Atom[] drawto;
	String modelname;

	public Jay3DeeObject(Atom args[])
	{
		String objectname = null;

		if (args.length < 1 || args[0].toString().contains("0"))
			Debug.info("Jay3DeeObject", "no objectname set. use 'setobjectname'. BEWARE: all messages are discarded as long no object name is set");
		else{
			objectname = args[0].toString();
		}
		jayobject = new ObjectContainer();
		setobjectname(objectname);

		declareIO(1,1);
		declareAttribute("drawto", null, "drawto");
		declareAttribute("modelname", null, "modelname");
	}

	public void setobjectname(String _objectname){
		if(_objectname != null)
			jayobject.init(_objectname);
	}

	/**
	 * calling this method is causing an new draw of the object/model
	 */
	public void draw(){
		if(jayobject != null){
			jayobject.drawer.drawraw();
//			outlet(1, "jit_matrix", jayobject.drawer.drawraw());
		}
	}

	public void render_mode(int mode){
		if(jayobject != null){
			jayobject.drawer.setRenderMode(mode);
		}
	}

	public void enable(int flag){
		if(jayobject != null){
			if(flag == 1)
				jayobject.drawer.updateRenderMode();
			else {
				jayobject.drawer.anything("enable", Atom.newAtom(0));
			}
		}
	}

	public void layer(int layer){
		if(jayobject != null){
			jayobject.drawer.setLayer(layer);
		}
	}

	public void position(Atom[] args){
		if(jayobject != null && args.length == 3){
			jayobject.position(args);
		}
	}

	public void scale(Atom[] args){
		if(jayobject != null && args.length == 3){
			jayobject.scale(args);
		}
	}

	public void rotatexyz(Atom[] args){
		if(jayobject != null && args.length == 3){
			jayobject.rotatexyz(args);
		}
	}

	public void quat(Atom[] args){
		if(jayobject != null && args.length == 4){
			jayobject.quat(args);
		}
	}

	public void anything(String message, Atom[] args){
		if(jayobject != null){
			jayobject.anything(message, args);
		}
	}

	/**
	 * this method is used to dump the sketch - commands
	 */
	public void drawGlCommand(MaxSketchMessage command){
		outlet(0, command.messagename, command.args);
	}

	public void drawto(Atom[] _drawto){
		drawto = _drawto;
		if(jayobject != null)
			jayobject.drawer.setContext(_drawto);
	}

	public void modelname(String _modelname){
		modelname = _modelname;
		if(jayobject != null)
			jayobject.setmodelname(_modelname);
	}


	public void notifyDeleted()
	{
		if(jayobject != null)
			jayobject.notifyDeleted();
		jayobject = null;
	}

}
