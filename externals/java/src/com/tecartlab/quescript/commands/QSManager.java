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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.XMLConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.tecartlab.quescript.QueMessage;
import com.tecartlab.quescript.OutputConnector;
import com.tecartlab.quescript.expression.ExpressionNode;
import com.tecartlab.quescript.expression.ExpressionVar;
import com.tecartlab.quescript.expression.RunTimeEnvironment;
import com.tecartlab.quescript.messages.CMsgTrigger;
import com.tecartlab.quescript.messages.ScriptMsgException;
import com.tecartlab.utils.Debug;

/**
 *
 * @author maybites
 *
 */
public class QSManager implements OutputInterface{
	// info messages //

	// QueList -  for filling the que selection menu
	private final String QUELIST = "quelist";
	private final String QUELIST_START = "start";
	private final String QUELIST_NAME = "name";
	private final String QUELIST_DONE = "done";
	private final String QUELIST_STOP = "stop";
	private final String QUELIST_PLAY = "play";

	// Script - shows current running ques and their position
	private final String SCRIPT = "script";
	private final String FRAMEDONE = "framedone";

	protected static String PARSING = "parsing";
	private static String PARSING_OK = "ok";
	protected static String PARSING_ERROR = "error";

	private static String SCHEMA_FILENAME = "/queListSchema.xsd";

	private ArrayList<CMsgTrigger> triggerQueue;
	private ArrayList<CMsgTrigger> triggerQueCopy;

	protected Validator validator;

	private boolean debugMode = false;

	private boolean autostart = false;

	private int viewplayingquesFreq = 0;

	private long lastviewTime = 0;

	private String fileName = null;

	private RunTimeEnvironment globalExprEnvironment;

	private CmndScript myScript;

	private ArrayList<String[]> selfCommands;

	private OutputConnector conn;

	public QSManager(){
		triggerQueue = new ArrayList<CMsgTrigger>();
		triggerQueCopy = new ArrayList<CMsgTrigger>();

		// creating a validator for validating the script files
		try {
			// create a SchemaFactory capable of understanding WXS schemas
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// load a WXS schema, represented by a Schema instance

			// first locate the schema inside the jar file
			// open an inputstream
			InputStreamReader stream = new InputStreamReader(getClass().getResourceAsStream(SCHEMA_FILENAME));

			// and load the schema file
			Source schemaFile = new StreamSource(stream);

	    	Schema schema = factory.newSchema(schemaFile);

	    	// create a Validator instance, which can be used to validate an instance document
	    	validator = schema.newValidator();

	    	// validate the DOM tree
	    	validator.setErrorHandler(new MyErrorHandler());

	    	stream.close();

	    	globalExprEnvironment = new RunTimeEnvironment();

			Calendar md = Calendar.getInstance();
			// create those variables so they are there once we need them on creation time.
			globalExprEnvironment.setLocalVariable("$HOUR", md.get(Calendar.HOUR_OF_DAY));
			globalExprEnvironment.setLocalVariable("$MIN", md.get(Calendar.MINUTE));
			globalExprEnvironment.setLocalVariable("$SEC", md.get(Calendar.SECOND));
			globalExprEnvironment.setLocalVariable("$MILLI", md.get(Calendar.MILLISECOND));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		selfCommands = new ArrayList<String[]>();

		myScript = new CmndScript();
	}

	public void bang(){
		executeInternalMessages();

		long timer = System.currentTimeMillis();
		// and then keep on going

		Calendar md = Calendar.getInstance();
		globalExprEnvironment.setLocalVariable("$HOUR", md.get(Calendar.HOUR_OF_DAY));
		globalExprEnvironment.setLocalVariable("$MIN", md.get(Calendar.MINUTE));
		globalExprEnvironment.setLocalVariable("$SEC", md.get(Calendar.SECOND));
		globalExprEnvironment.setLocalVariable("$MILLI", md.get(Calendar.MILLISECOND));

		// make sure that no concurrent triggers get lost.
		triggerQueCopy = triggerQueue;
		if(triggerQueue.size() > 0)
			triggerQueue = new ArrayList<CMsgTrigger>();

		for(Cmnd e: myScript.getQues()){
//			Debugger.verbose("QueScript", "banging...: " + nextElement.getQueName());
			((CmndQue)e).bang(triggerQueCopy);
//			Debugger.verbose("QueScript", "... banged: " + nextElement.getQueName());
		}
		triggerQueue.clear();

		if(viewplayingquesFreq > 0 && lastviewTime + (1000 / viewplayingquesFreq) < timer ){
			lastviewTime = timer;
			int outCounter = 0;
			outputInfoMsg(QueMsgFactory.getMsg(SCRIPT).add("playtime").add(System.currentTimeMillis() - timer).done());
			for(Cmnd child: myScript.getQues()){
				CmndQue q = (CmndQue)child;
				if(q.isPlaying){
					outputInfoMsg(QueMsgFactory.getMsg(SCRIPT).add(outCounter).add(q.scriptLineSize).add(q.waitLineNumber - q.lineNumber).add("que("+q.queName+") "+q.waitLineMsg).add(1).done());
					outCounter++;
				}
			}
			for(int i = outCounter; i < 12; i++){
				outputInfoMsg(QueMsgFactory.getMsg(SCRIPT).add(i).add(20).add(1).add("-").add(0).done());
			}
		}
		//outputInfoMsg(QueMsgFactory.getMsg(FRAMEDONE).done());
	}

	public void executeInternalMessages(){
		// first execute self inflicted commands (the script calls itself)
		if(selfCommands.size() > 0){
			ArrayList<String[]> copyCommands = new ArrayList<String[]>();
			for(String[] cmd: selfCommands){
				copyCommands.add(cmd);
			}
			// then clear all the commands
			selfCommands.clear();
			for(String[] cmd: copyCommands){
				if(cmd[0].equals(CmndInternal.NODE_NAME_PLAY)){
					play(cmd[2]);
				}else if(cmd[0].equals(CmndInternal.NODE_NAME_STOP)){
					if(cmd[2] == null){ // no name attribute was set at the stop message
						stopExcept(cmd[1]);
					} else { // there was a name attribute
						stop(cmd[2]);
					}
				}else if(cmd[0].equals(CmndInternal.NODE_NAME_SHUTDOWN)){
					if(cmd[2] == null){ // no name attribute was set at the shutdown message
						shutDownExcept(cmd[1]);
					} else { // there was a name attribute
						shutdown(cmd[2]);
					}
				}else if(cmd[0].equals(CmndInternal.NODE_NAME_PAUSE)){
					if(cmd[2] == null){ // no name attribute was set at the pause message
						pauseExcept(cmd[1]);
					} else { // there was a name attribute
						pause(cmd[2]);
					}
				}else if(cmd[0].equals(CmndInternal.NODE_NAME_RESUME)){
					if(cmd[2] == null){ // no name attribute was set at the play message
						resume();
					} else { // there was a name attribute
						resume(cmd[2]);
					}
				}else if(cmd[0].equals(CmndMessage.NODE_NAME_TRIGGER)){
					// the structure of the internal message is:
					// 'trigger' <quename> 'trigger' <triggername> (<arg> <arg> etc)
					if(cmd.length == 4){ //
						trigger(cmd[3], null);
					} else if(cmd.length > 4){
						String[] args = new String[cmd.length - 4];
						for(int i = 4; i < cmd.length; i++){
							args[i - 4] = cmd[i];
						}
						trigger(cmd[3], args);
					}
				}
			}
		}
	}

	/**
	 * loads a script file and returns the que's names it contains
	 * @param _filepath
	 */
	public void load(String _filepath){
		myScript.setOutput(this);

		// before loading the new ques, que will be removed if
		//  ->  ques are not playing
		//  ->  if the same script is loaded again
		for(Iterator<Cmnd> e = myScript.getChildren().iterator(); e.hasNext();){
			Cmnd cmd = e.next();
			if(cmd.cmdName.equals(CmndQue.NODE_NAME)){
				// everything that is a que will be tested
				CmndQue que = (CmndQue)cmd;
				if(!que.isPlaying || _filepath.equals(fileName)){
					que.clear();
					e.remove();
				}
			} else {
				// everything else will be removed (var - nodes)
				e.remove();
			}
		}

		try {
		    // Validate Script against the XSD
		    SAXSource source = new SAXSource(new InputSource(new java.io.FileInputStream(_filepath)));
		    validator.validate(source);

		    // Load the Script and make it accessible for building
		    InputStream is = new java.io.FileInputStream(_filepath);
			Document document = PositionalXMLReader.readXML(is);

			document.getDocumentElement().normalize();
			
			// add this info so CmndInput can get the current file path to find its import file
			document.setUserData("filepath", _filepath, null);
			document.setUserData("qsmanager", this, null);

			myScript.build(document.getFirstChild());

			// checking for a <stop> node to stop all ques still playing
			for(Cmnd q: myScript.getStops()){
				CmndInternal stop = (CmndInternal) q;
				// all ques still playing need to be stopped:
				for(Iterator<Cmnd> e = myScript.getQues().iterator(); e.hasNext();){
					Cmnd cmd = e.next();
					CmndQue que = (CmndQue)cmd;
					if(que.isPlaying){
						if(stop.hasName()){
							// in this case we stop the indicated que:
							if(que.queName.equals(stop.getName())){
								que.stop();
								que.clear();
								e.remove();
								Debug.info("QueScript", "stopped que '" +que.queName + "'");
							}
						} else {
							que.stop();
							que.clear();
							e.remove();
							Debug.info("QueScript", "stopped que '" + que.queName + "'");
						}
					}
				}
			}

			myScript.setup(globalExprEnvironment);

			Debug.info("QueScript", "loaded " +_filepath + " with " + myScript.getChildren().size() + " que's");

			outputInfoMsg(QueMsgFactory.getMsg(PARSING).add(PARSING_OK).done());

		} catch (SAXParseException e) {
			Debug.error("QueScript", "Error at line[" + e.getLineNumber() +
					"] col[" + e.getColumnNumber() + "]: " +
					e.getMessage().substring(e.getMessage().indexOf(":")+1));
			outputInfoMsg(QueMsgFactory.getMsg(PARSING).add(PARSING_ERROR).add(e.getMessage().substring(e.getMessage().indexOf(":")+1) + " at line(" + e.getLineNumber() + ") col(" + e.getColumnNumber() + ")").done());
			return;

		} catch (ScriptMsgException e) {
			Debug.error("QueScript", "Error: " + e.getMessage());
			outputInfoMsg(QueMsgFactory.getMsg(PARSING).add(PARSING_ERROR).add(e.getMessage()).done());
			return;

		} catch (Exception e) {
			Debug.error("QueScript", "DocumentBuilder Exceptions:" + e.getMessage());
			outputInfoMsg(QueMsgFactory.getMsg(PARSING).add(PARSING_ERROR).add(e.getMessage()).done());
			e.printStackTrace();
			return;
		}


		// checking for a <play> node to play all indicated ques
		for(Cmnd q: myScript.getPlays()){
			CmndInternal play = (CmndInternal) q;
			// all ques still playing need to be stopped:
			for(Iterator<Cmnd> e = myScript.getQues().iterator(); e.hasNext();){
				Cmnd cmd = e.next();
				CmndQue que = (CmndQue)cmd;
				// in this case we play the indicated que:
				if(que.queName.equals(play.getName())){
					Debug.info("QueScript", "autostarted que '" + que.queName + "'");
					que.play(debugMode);
				}
			}
		}

		outputInfoMsg(QueMsgFactory.getMsg(QUELIST).add(QUELIST_START).done());
		for(int i = myScript.getQues().size() - 1; i >= 0; i--){
			CmndQue _next = (CmndQue)myScript.getQues().get(i);
			outputInfoMsg(QueMsgFactory.getMsg(QUELIST).add(QUELIST_NAME).add(_next.getQueName()).done());
		}
		outputInfoMsg(QueMsgFactory.getMsg(QUELIST).add(QUELIST_DONE).done());

		fileName = _filepath;
	}

	public void var(String name, double value){
		if(globalExprEnvironment.containsVar(name)){
			globalExprEnvironment.setLocalVariable(name, value);
		}
	}

	public void var(String name, String value){
		if(globalExprEnvironment.containsVar(name)){
			globalExprEnvironment.setLocalVariable(name, value);
		}
	}

	public void var(String name, ExpressionVar values){
		if(globalExprEnvironment.containsVar(name)){
			globalExprEnvironment.setLocalVariable(name, values);
		}
	}

	public void quevar(String _que, String _name, ArrayList<ExpressionNode> _values){
		// we actually dont want quevar, because they set the <var>-defined variables,
		// and they are reset to their initial expression every time the que restarts.
		// this means, no variable ever set through this channel will be accessible
		// myScript.getQue(_que).setQueVar(_name, _values);
	}

	public void clearGlobalVars(){
		globalExprEnvironment.clearVariables();
	}

	public void reset(){
		clearGlobalVars();
		stop();
		if(fileName != null)
			load(fileName);

	}

	public void play(String _queName){
		outputInfoMsg(QueMsgFactory.getMsg(QUELIST).add(QUELIST_PLAY).add(_queName).done());

		if(myScript.hasQue(_queName)){
			if(debugMode)
				Debug.verbose("QueScript", "play... :" + _queName);
			myScript.getQue(_queName).play(debugMode);
			if(debugMode)
				Debug.verbose("QueScript", "... play :" + _queName);
		}
	}

	/**
	 * ShutDown all ques
	 */
	public void shutdown(){
		if(debugMode)
			Debug.verbose("QueScript", "shuting down... : all");
		for(Cmnd e: myScript.getQues()){
			((CmndQue)e).shutDown();
		}
		if(debugMode)
			Debug.verbose("QueScript", "... shut all down");
	}

	/**
	 * shutdown only specified que
	 * @param _exception
	 */
	public void shutdown(String _name){
		for(Cmnd e: myScript.getQues()){
			CmndQue _next = (CmndQue)e;
			if(_next.queName.equals(_name)){
				if(debugMode)
					Debug.verbose("QueScript", "shutDown... : " + _name);
				_next.shutDown();
				if(debugMode)
					Debug.verbose("QueScript", ".... shutDown : " + _name);
			}
		}
	}

	/**
	 * shutdown all ques except the specified
	 * @param _exceptionName
	 */
	public void shutDownExcept(String _exceptionName){
		for(Cmnd e: myScript.getQues()){
			CmndQue _next = (CmndQue)e;
			if(!_next.queName.equals(_exceptionName)){
				if(debugMode)
					Debug.verbose("QueScript", "shutDownExcept.... : " + _exceptionName);
				_next.shutDown();
				if(debugMode)
					Debug.verbose("QueScript", ".... shutDownExcept: " + _exceptionName);
			}
		}
	}

	/**
	 * resume all executed que's
	 */
	public void resume(){
		if(debugMode)
			Debug.verbose("QueScript", "resume all executed que's");
		for(Cmnd e: myScript.getQues()){
			((CmndQue)e).resume();
		}
		if(debugMode)
			Debug.verbose("QueScript", "... all paused que's are resumed");
	}

	/**
	 * resume specified executed que
	 * @param _name
	 */
	public void resume(String _name){
		for(Cmnd e: myScript.getQues()){
			CmndQue _next = (CmndQue)e;
			if(_next.queName.equals(_name)){
				if(debugMode)
					Debug.verbose("QueScript", "resume paused que: " + _name);
				_next.resume();
				if(debugMode)
					Debug.verbose("QueScript", "... paused que is resumed playing: " + _name);
			}
		}
	}

	/**
	 * pause all executed que's
	 */
	public void pause(){
		if(debugMode)
			Debug.verbose("QueScript", "pause all executed que's");
		for(Cmnd e: myScript.getQues()){
			((CmndQue)e).pause();
		}
		if(debugMode)
			Debug.verbose("QueScript", "... all executed que's are paused");
	}

	/**
	 * pause specified executed que
	 * @param _name
	 */
	public void pause(String _name){
		for(Cmnd e: myScript.getQues()){
			CmndQue _next = (CmndQue)e;
			if(_next.queName.equals(_name)){
				if(debugMode)
					Debug.verbose("QueScript", "pause executed que: " + _name);
				_next.pause();
				if(debugMode)
					Debug.verbose("QueScript", "... executed que is paused: " + _name);
			}
		}
	}

	/**
	 * Pause all que's except the specified
	 * @param _exceptionName
	 */
	public void pauseExcept(String _exceptionName){
		for(Cmnd e: myScript.getQues()){
			CmndQue _next = (CmndQue)e;
			if(!_next.queName.equals(_exceptionName)){
				if(debugMode)
					Debug.verbose("QueScript", "pause all, except.... : " + _exceptionName);
				_next.pause();
				if(debugMode)
					Debug.verbose("QueScript", "... paused all, except: " + _exceptionName);
			}
		}
	}

	/**
	 * Stop all ques
	 */
	public void stop(){
		outputInfoMsg(QueMsgFactory.getMsg(QUELIST).add(QUELIST_STOP).done());

		if(debugMode)
			Debug.verbose("QueScript", "stoping all...");
		for(Cmnd e: myScript.getQues()){
			((CmndQue)e).stop();
		}
		if(debugMode)
			Debug.verbose("QueScript", "... all stopped");
	}

	/**
	 * Stops only specified que
	 * @param _exception
	 */
	public void stop(String _name){
		for(Cmnd e: myScript.getQues()){
			CmndQue _next = (CmndQue)e;
			if(_next.queName.equals(_name)){
				if(debugMode)
					Debug.verbose("QueScript", "stopping... : " + _name);
				_next.stop();
				if(debugMode)
					Debug.verbose("QueScript", "...stopped: " + _name);
			}
		}
	}

	/**
	 * Stops all ques except the specified
	 * @param _exceptionName
	 */
	public void stopExcept(String _exceptionName){
		for(Cmnd e: myScript.getQues()){
			CmndQue _next = (CmndQue)e;
			if(!_next.queName.equals(_exceptionName)){
				if(debugMode)
					Debug.verbose("QueScript", "stopExcept.... : " + _exceptionName);
				_next.stop();
				if(debugMode)
					Debug.verbose("QueScript", "... stopped Except: " + _exceptionName);
			}
		}
	}

	public void progressupdate(int _frequency){
		viewplayingquesFreq = _frequency;
	}


	/**
	 * Starts first que if a script is loaded and no que is playing.
	 * @param _autostart
	 */
	public void autostart(int _autostart){
		autostart = (_autostart == 1)? true: false;
		if(fileName != null){
			if(!myScript.hasQuePlaying()){
				if(!myScript.getQues().isEmpty()){
					String firstQueName = ((CmndQue)myScript.getQues().get(0)).getQueName();
					play(firstQueName);
					Debug.info("QueScript", "autostarted: " + firstQueName);
				} else {
					Debug.warning("QueScript", "autostart failed due to missing que in script.");
				}
			} else {
				Debug.warning("QueScript", "autostart didnt start first que due to already playing ques.");
			}
		}
	}

	public void trigger(String _triggerName, String[] args){
		triggerQueue.add(new CMsgTrigger(_triggerName, args));
	}

	public void printStructure(){
		myScript.printStructure();
	}

	protected void notifyDeleted(){
	}

	private static class MyErrorHandler extends DefaultHandler {
		public void warning(SAXParseException e) throws SAXException {
			printInfo(e);
		}

		public void error(SAXParseException e) throws SAXException {
			printInfo(e);
		}

		public void fatalError(SAXParseException e) throws SAXException {
			printInfo(e);
		}

		private void printInfo(SAXParseException e) {
			Debug.error("QueScript", "Error at line(" + e.getLineNumber() + ") col(" + e.getColumnNumber() +
					"): " + e.getMessage().substring(e.getMessage().indexOf(":")+2));
		}
	}

	public void setDebug(int _debug){
		debugMode = (_debug == 1)? true: false;
		myScript.setDebugMode(debugMode);
	}

	public void registerConnector(OutputConnector conn){
		this.conn = conn;
	}

	public void outputSendMsg(QueMessage msg) {
		conn.outputSendMsg(msg);
	}

	public void outputInfoMsg(QueMessage msg) {
		conn.outputInfoMsg(msg);
	}

	public void outputDumpMsg(QueMessage msg) {
		conn.outputDumpMsg(msg);
	}

	@Override
	public void selfCommand(String[] _comnd) {
		selfCommands.add(_comnd);
	}
}
