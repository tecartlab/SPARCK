package com.tecartlab.sparck.msg;
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
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;

public class UtilKey extends MaxObject{
	Atom key = null;
	Atom sub = null;

	String subName = "substitute";
	String routName = "route";
	String routpassName = "routepass";

	Atom offvaluelist[] = null;

	String current = subName;

	public UtilKey(Atom args[]) {
		declareInlets(new int[] { DataTypes.ALL, DataTypes.ALL });
		declareOutlets(new int[] { DataTypes.ALL, DataTypes.ALL });
		createInfoOutlet(true);

	}

	public void anything(String messagename, Atom args[]){
		int inlet_num = getInlet();
		//post("utilKey: " + messagename + " -> " + arguments[0] + "\n");
		if(inlet_num == 0){
			if(!messagename.equals("off")){
				// if a change happens, the offvalues are first sent,
				//  because if the new address doesnt have this key,
				//  this outlet will react as if the selection is off.
				if(offvaluelist != null)
					outlet(2, offvaluelist);
				outlet(0, "set", "msg::" + messagename + "::" + key);
				outlet(1, args);
			} else {
				outlet(0, "set");
				if(offvaluelist != null)
					outlet(2, offvaluelist);
			}
		}
		if(inlet_num == 1){
			if(messagename.indexOf("outoff") == 0){
				offvaluelist = args;
			} else if (messagename.indexOf("out") == 0){
				key = args[0];
				sub = args[0];
				if(args.length == 2)
					sub = args[1];
				if(sub.getString().equals("><"))
					changeObjects(routName);
				else{
					if(key == sub)
						changeObjects(routpassName);
					else
						changeObjects(subName);
				}
			}
		}
//		cpost("utilKey EXIT: " + messagename + " -> " + arguments[0] + "\n");

	}

	private void changeObjects(String newObjectName){
		MaxBox theReceiver = this.getParentPatcher().getNamedBox("receiver");
		MaxBox theOutlet = this.getParentPatcher().getNamedBox("outlet");
		MaxBox jsOutlet1 = this.getParentPatcher().getNamedBox("jsoutlet1");
		this.getParentPatcher().getNamedBox("substitute").remove();
		MaxBox theObject;
		if(newObjectName == routName)
			theObject =  this.getParentPatcher().newDefault(26, 135, newObjectName, new Atom[]{key});
		if(newObjectName == routpassName)
			theObject =  this.getParentPatcher().newDefault(26, 135, newObjectName, new Atom[]{key});
		else
			theObject =  this.getParentPatcher().newDefault(26, 135, newObjectName, new Atom[]{key, sub, Atom.newAtom(1)});
		theObject.setName("substitute");
		this.getParentPatcher().connect(jsOutlet1, 0, theObject, 0);
		this.getParentPatcher().connect(theReceiver, 0, theObject, 0);
		this.getParentPatcher().connect(theObject, 0, theOutlet, 0);
		current = newObjectName;
	}
}
