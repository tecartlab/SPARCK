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

package com.tecartlab.quescript.commands;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.tecartlab.quescript.expression.RunTimeEnvironment;
import com.tecartlab.quescript.messages.CMsgShuttle;
import com.tecartlab.quescript.messages.ScriptMsgException;

public class CmndScript extends Cmnd{
	public static String NODE_NAME = "script";

	/** list with only que nodes */
	private ArrayList<Cmnd> queChildren;
	private ArrayList<Cmnd> playChildren;
	private ArrayList<Cmnd> stopChildren;
	private ArrayList<Cmnd> varChildren;

	OutputInterface output;

	public CmndScript() {
		super(null);
		queChildren = new ArrayList<Cmnd>();
		playChildren = new ArrayList<Cmnd>();
		stopChildren = new ArrayList<Cmnd>();
		varChildren = new ArrayList<Cmnd>();
		super.setCmndName(NODE_NAME);
	}

	public void build(Node _xmlNode) throws ScriptMsgException{
		super.build(_xmlNode);
		queChildren.clear();
		stopChildren.clear();
		playChildren.clear();
		varChildren.clear();
		
		ArrayList<Cmnd> importedChildren = new ArrayList<Cmnd>();
		// check first if there are import nodes
		for(Cmnd child: this.getChildren()){
			if(child.cmdName.equals(CmndImport.NODE_NAME)){
				// in this case get all the Ques and global vars
				// from the import and add them to the children in here.
				importedChildren.addAll(((CmndImport)child).getVars());
				importedChildren.addAll(((CmndImport)child).getQues());
			}
		}
		this.getChildren().addAll(importedChildren);
		
		for(Cmnd child: this.getChildren()){
			if(child.cmdName.equals(CmndQue.NODE_NAME)){
				queChildren.add(child);
			} else if(child.cmdName.equals(CmndInternal.NODE_NAME_STOP)){
				stopChildren.add(child);
			} else if(child.cmdName.equals(CmndInternal.NODE_NAME_PLAY)){
				playChildren.add(child);
			} else if(child.cmdName.equals(CmndVar.NODE_NAME)){
				varChildren.add(child);
			}
		}
	}

	public void setup(RunTimeEnvironment rt)throws ScriptMsgException{
		for(Cmnd child: this.getChildren()){
			child.setup(rt);
		}
	}

	public void setOutput(OutputInterface output){
		this.output = output;
	}

	public OutputInterface getOutput(){
		return output;
	}

	/**
	 * gets all this objects ques
	 * @return
	 */
	public List<Cmnd> getQues(){
		return queChildren;
	}

	/**
	 * gets all this objects stops
	 * @return
	 */
	public List<Cmnd> getStops(){
		return stopChildren;
	}

	/**
	 * gets all this objects plays
	 * @return
	 */
	public List<Cmnd> getPlays(){
		return playChildren;
	}

	/**
	 * gets all this objects global vars
	 * @return
	 */
	public List<Cmnd> getVars(){
		return varChildren;
	}

	/**
	 * Checks if this Script has a Que of this name
	 * @param queName
	 * @return true if this is the case
	 */
	public boolean hasQue(String queName){
		for(Cmnd q: getQues()){
			CmndQue que = (CmndQue) q;
			if(que.getQueName().equals(queName))
				return true;
		}
		return false;
	}

	/**
	 * Returns the first que of this name this script can find inside its children.
	 * @param queName
	 * @return the instance of the que
	 */
	public CmndQue getQue(String queName){
		for(Cmnd q: getQues()){
			CmndQue que = (CmndQue) q;
			if(que.getQueName().equals(queName)){
				return que;
			}
		}
		return null;
	}

	/**
	 * Checks if this Script has a playing que
	 * @return true if one of its ques is playing
	 */
	public boolean hasQuePlaying(){
		for(Cmnd q: getQues()){
			CmndQue que = (CmndQue) q;
			if(que.isPlaying){
				return true;
			}
		}
		return false;
	}

	@Override
	public void bang(CMsgShuttle _msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lockLessBang(CMsgShuttle _msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume(long _timePassed) {
		// TODO Auto-generated method stub

	}

}
