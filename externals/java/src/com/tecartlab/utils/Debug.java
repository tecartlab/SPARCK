package com.tecartlab.utils;
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

public class Debug{

	private com.tecartlab.utils.DebugLogger outlet = null;

	private static final int LEVEL_VERBOSE = 0;
	private static final int LEVEL_DEBUG = 1;
	private static final int LEVEL_INFO = 2;
	private static final int LEVEL_WARNING = 3;
	private static final int LEVEL_ERROR = 4;
	private static final int LEVEL_FATAL = 5;

	private static final String[] LEVEL_MESG = {	"Verbose: ",
													"Debug: ",
													"Info: ",
													"Warning: ",
													"Error: ",
													"Fatal: "};

	// methods and attributes for Singleton pattern
	private Debug(int l) {
		myLevel = l;
		_showClassNames = true;
	}

	public void setOutputObject(com.tecartlab.utils.DebugLogger _reference){
		outlet = _reference;
	}

	public int myLevel;
	private boolean _showClassNames;
	private boolean _showMillis;

	static private Debug _instance = new Debug(LEVEL_INFO);

	static public void showClassNames(){

	}

	static public void showMillis(boolean _show){
		_instance._showMillis = _show;
	}

	static public void setLevelToVerbose(){
		//_instance._showMillis = true;
		_instance.myLevel = LEVEL_VERBOSE;
	}

	static public void setLevelToDebug(){
		_instance.myLevel = LEVEL_DEBUG;
	}

	static public void setLevelToInfo(){
		_instance.myLevel = LEVEL_INFO;
	}

	static public void setLevelToWarning(){
		_instance.myLevel = LEVEL_WARNING;
	}

	static public void setLevelToError(){
		_instance.myLevel = LEVEL_ERROR;
	}

	static public void setLevelToFatal(){
		//_instance._showMillis = false;
		_instance.myLevel = LEVEL_FATAL;
	}

	/**
	 * Get the global Debugger instance
	 * @return
	 */
	static public Debug getInstance() {
		return _instance;
	}

	private String constructMessageString(String _class, String message, int level){
		String msg = "";
		if(_showMillis)
			msg ="[" + System.currentTimeMillis() + "]";
		msg = msg + LEVEL_MESG[level];
		if(_class != null && _showClassNames)
			msg += "from " + _class + ": ";
		msg += message;
		return msg;
	}

	private void message(String _class, String message, int level){
		if(outlet != null){
			outlet.printInfo(_showMillis, LEVEL_MESG[level], _class, message);
		} else {
			System.out.println(constructMessageString(_class, message, level));
		}
	}

	private void message(Class<?> o, String message, int level){
		message(o.getName(), message, level);
	}

	private void messageErr(String _class, String message, int level){
		if(outlet != null){
			outlet.printError(_showMillis, LEVEL_MESG[level], _class, message);
		} else {
			System.err.println(constructMessageString(_class, message, level));
		}
	}

	private void messageErr(Class<?> o, String message, int level){
		messageErr(o.getName(), message, level);
	}

	public void verboseMessage(Class<?> o, String message){
		if(myLevel <= LEVEL_VERBOSE)
			message(o, message, LEVEL_VERBOSE);
	}

	public void verboseMessage(String o, String message){
		if(myLevel <= LEVEL_VERBOSE)
			message(o, message, LEVEL_VERBOSE);
	}

	/**
	 * Lowest Priority Message
	 * @param o
	 * @param message
	 */
	public static void verbose(Class<?> o, String message){
		getInstance().verboseMessage(o, message);
	}

	public static void verbose(String o, String message){
		getInstance().verboseMessage(o, message);
	}

	public void debugMessage(Class<?> o, String message){
		if(myLevel <= LEVEL_DEBUG)
			message(o, message, LEVEL_DEBUG);
	}

	/**
	 * 2nd Lowest Priority Message
	 * @param o
	 * @param message
	 */
	public static void debug(Class<?> o, String message){
		getInstance().debugMessage(o, message);
	}

	public void infoMessage(Class<?> o, String message){
		if(myLevel <= LEVEL_INFO)
			message(o, message, LEVEL_INFO);
	}

	public void infoMessage(String o, String message){
		if(myLevel <= LEVEL_INFO)
			message(o, message, LEVEL_INFO);
	}

	/**
	 * 3rd Lowest Priority Message
	 * @param o
	 * @param message
	 */
	public static void info(Class<?> o, String message){
		getInstance().infoMessage(o, message);
	}

	public static void info(String o, String message){
		getInstance().infoMessage(o, message);
	}

	public void warningMessage(Class<?> o, String message){
		if(myLevel <= LEVEL_WARNING)
			message(o, message, LEVEL_WARNING);
	}

	public void warningMessage(String o, String message){
		if(myLevel <= LEVEL_WARNING)
			message(o, message, LEVEL_WARNING);
	}

	/**
	 * Warning Message - possibly severe
	 * @param o
	 * @param message
	 */
	public static void warning(Class<?> o, String message){
		getInstance().warningMessage(o, message);
	}

	public static void warning(String o, String message){
		getInstance().warningMessage(o, message);
	}

	public void errorMessage(Class<?> o, String message){
		if(myLevel <= LEVEL_ERROR)
			messageErr(o, message, LEVEL_ERROR);
	}

	public void errorMessage(String o, String message){
		if(myLevel <= LEVEL_ERROR)
			messageErr(o, message, LEVEL_ERROR);
	}

	/**
	 * Error Message - very severe
	 * @param o
	 * @param message
	 */
	public static void error(Class<?> o, String message){
		getInstance().errorMessage(o, message);
	}

	public static void error(String o, String message){
		getInstance().errorMessage(o, message);
	}

	public void fatalMessage(Class<?> o, String message){
		if(myLevel <= LEVEL_FATAL)
			messageErr(o, message, LEVEL_FATAL);
	}

	public void fatalMessage(String o, String message){
		if(myLevel <= LEVEL_FATAL)
			messageErr(o, message, LEVEL_FATAL);
	}

	/**
	 * Fatal Message - very severe - Application will probably crash
	 * @param o
	 * @param message
	 */
	public static void fatal(Class<?> o, String message){
		getInstance().fatalMessage(o, message);
	}

	public static void fatal(String o, String message){
		getInstance().fatalMessage(o, message);
	}

}
