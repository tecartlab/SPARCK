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

package com.tecartlab.quescript;

import java.util.ArrayList;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

import com.tecartlab.quescript.commands.QSManager;
import com.tecartlab.quescript.commands.QueMsgFactory;
import com.tecartlab.quescript.expression.ExpressionAtom;
import com.tecartlab.quescript.expression.ExpressionNode;
import com.tecartlab.quescript.expression.ExpressionVar;
import com.tecartlab.utils.Debug;

/**
 * QueScript External for MaxMSP
 *
 * @author Martin Froehlich http://maybites.ch
 *
 */
public class Que extends MaxObject implements OutputConnector{

	final int OUTLET_SEND = 0;
	final int OUTLET_TRIGGER = 1;
	final int OUTLET_INFO = 2;
	final int OUTLET_DUMP = 3;

	QSManager queManager;

	public Que(Atom[] _args){

		declareInlets(new int[]{DataTypes.ALL, DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});

		queManager = new QSManager();

		QueMsgFactory.setMsgTypeToMax();

		post("Que Version 1.0.0 alpha");
	}

	public void loadbang(){
		queManager.registerConnector(this);
	}

	/**
	 * read script file
	 * @param _fileName
	 */
	public void read(String _fileName){
		queManager.load(_fileName);
	}

	/**
	 * create next frame
	 */
	public void bang(){
		queManager.bang();
	}

	/**
	 * set global variable
	 * @param name
	 * @param val
	 */
	public void var(String name, int val){
		queManager.var(name, val);
	}

	/**
	 * set global variable
	 * @param name
	 * @param val
	 */
	public void var(String name, float val){
		queManager.var(name, val);
	}

	/**
	 * set global variable
	 * @param name
	 * @param val
	 */
	public void var(String name, String val){
		queManager.var(name, val);
	}

	/**
	 * set global variable
	 * @param name
	 * @param val
	 */
	public void var(Atom[] val){
		ArrayList<ExpressionAtom> values = new ArrayList<ExpressionAtom>();
		for(int i = 1; i < val.length; i++){
			if(val[i].isString())
				values.add(new ExpressionAtom(val[i].getString()));
			else if(val[i].isFloat())
				values.add(new ExpressionAtom(val[i].getFloat()));
			else if(val[i].isInt())
				values.add(new ExpressionAtom(val[i].getInt()));
		}
		ExpressionVar value = new ExpressionVar(values);
		queManager.var(val[0].getString(), value);
	}

	/**
	 * set a variable for a specific que
	 * @param val
	 */
	public void quevar(Atom[] val){
		// we actually dont want quevar, because they set the <var>-defined variables,
		// and they are reset to their initial expression every time the que restarts.
		// this means, no variable ever set through this channel will be accessible
		/*
		if(val.length >= 3){
			ArrayList<ExpressionVar> values = new ArrayList<ExpressionVar>();
			for(int i = 2; i < val.length; i++){
				if(val[i].isString())
					values.add(new ExpressionVar(val[i].getString()));
				else if(val[i].isFloat())
					values.add(new ExpressionVar(val[i].getFloat()));
				else if(val[i].isFloat())
					values.add(new ExpressionVar(val[i].getInt()));
			}
			queManager.quevar(val[0].getString(), val[1].getString(), values);
		} else {
			error("QueScript: quevar expects the follwing format: quescript 'quename' 'varname' value(s)... | but it received: " + val.toString());
		}
		*/
	}

	/**
	 * autostart = 1 will play the first que of the script upon loading the script
	 * @param _autostart
	 */
	public void autostart(int _autostart){
		queManager.autostart(_autostart);
	}

	/**
	 * clears all global Variables, stops all que's and reloads the script
	 */
	public void reset(){
		queManager.reset();
	}

	/**
	 * Start the output of information about the currently playing que's, including the time each
	 * frame takes in milliseconds.
	 * @param _frequency the number of updates per second.
	 */
	public void progressupdate(int _frequency){
		queManager.progressupdate(_frequency);
	}

	/**
	 * Start playing specified que name
	 * @param queName
	 */
	public void play(String queName){
		queManager.play(queName);
	}

	/**
	 * trigger message
	 * @param _args list
	 */
	public void trigger(Atom[] _args){
		String[] args = new String[_args.length - 1];
		for(int i = 1; i < _args.length; i++){
			args[i - 1] = _args[i].getString();
		}
		queManager.trigger(_args[0].toString(), args);
	}

	/**
	 * trigger message
	 * @param _triggerName string
	 */
	public void trigger(String _triggerName){
		queManager.trigger(_triggerName, null);
	}

	/**
	 * stops all running que's
	 */
	public void stop(){
		queManager.stop();
	}

	/**
	 * stops specified que
	 * @param queName
	 */
	public void stop(String queName){
		queManager.stop(queName);
	}

	/**
	 * resumes playing all paused que's
	 */
	public void resume(){
		queManager.resume();
	}

	/**
	 * resumes playing specified que
	 * @param queName
	 */
	public void resume(String queName){
		queManager.resume(queName);
	}

	/**
	 * pause all running que's
	 */
	public void pause(){
		queManager.pause();
	}

	/**
	 * pause specified que
	 * @param queName
	 */
	public void pause(String queName){
		queManager.pause(queName);
	}

	/**
	 * shutdown all que's
	 */
	public void shutdown(){
		queManager.shutdown();
	}

	/**
	 * Shutdown specified que
	 * @param queName
	 */
	public void shutdown(String queName){
		queManager.shutdown(queName);
	}

	/**
	 * switch debug mode
	 * @param _debug 0 = off, 1 = on
	 */
	public void debug(int _debug){
		queManager.setDebug(_debug);
	}

	/**
	 * Debugger level
	 * @param _level (verbose, debug, info, warning, error, fatal)
	 */
	public void java_debug(String _level){
		if(_level.equals("verbose"))
			Debug.setLevelToVerbose();
		else if(_level.equals("debug"))
			Debug.setLevelToDebug();
		else if(_level.equals("info"))
			Debug.setLevelToInfo();
		else if(_level.equals("warning"))
			Debug.setLevelToWarning();
		else if(_level.equals("error"))
			Debug.setLevelToError();
		else if(_level.equals("fatal"))
			Debug.setLevelToFatal();
	}

	public void outputSendMsg(QueMessage msg) {
		if(msg.hasAtoms())
			outlet(OUTLET_SEND, (Atom[])msg.getAtoms());
	}

	public void outputInfoMsg(QueMessage msg) {
		if(msg.hasAtoms())
			outlet(OUTLET_INFO, (Atom[])msg.getAtoms());
	}

	public void outputDumpMsg(QueMessage msg) {
		if(msg.hasAtoms())
			outlet(OUTLET_DUMP, (Atom[])msg.getAtoms());
	}

}
