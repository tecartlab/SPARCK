package com.tecartlab.sparck.misc;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.IllegalBlockSizeException;
import javax.net.ssl.HttpsURLConnection;

import com.cycling74.max.*;

import com.tecartlab.utils.Debug;

/**
 * The Sequence of Events:
 *
 * - This class checks first if all the paths exist (license / preferences / documents)
 * 		if not, create them
 * - then it waits for a loadbang
 * 		- loads the license and sets the license level of the app
 * 		- generates the menu
 * 		- if a preference file exists
 * 			- tell the preference to load its properties
 * 		- else it sets the default preferences and stores the file
 * 		- creates a default project inside the documents
 * 		- sends the bs::app::started message
 *
 * - then it waits for preference info
 * 		- if splash screen should be shown, splash screen is opened
 * 		- if autoload project file, project file is loaded (see load project)
 * 		- if publish anonymously usage data flag - send usage data
 *
 *	- then it waits for load - project
 *		- creates the project settings
 *		- propagates the project settings to the dict
 *		- creates (if necessary) the missing folder structure
 *		- sends the load command
 *
 *
 * @author maybites
 *
 */
public class Project extends MaxObject{
	public static String REL_PATH_SUPPORT = "/SPARCK"; // Application Support Folder
	public static String REL_PATH_REGISTRATION = "/registration"; // where the registration is located
	public static String REL_PATH_SETUP = "/setup"; // where the preference files are located

	public static String REL_PATH_DOCUMENT = "/SPARCK"; // where the documents files are located
	public static String REL_PATH_PROJECTS = "/projects"; // where the project files are located
	public static String REL_PATH_DEFAULT = "/default"; // where the default files are located

	public static String FILE_DEFAULT_PROJECT = "/default.sparck"; //name of the default project
	public static String FILE_APP_PREFERENCES = "/app_preferences.xml"; //name of the app preferences
	public static String FILE_PATCH_PREFERENCES = "/patcher_preferences.xml"; //name of the app preferences
	public static String FILE_REGISTRATION_EXT_NAME = "reg";
	public static String FILE_LICENSE_NAME = "/sparck." + FILE_REGISTRATION_EXT_NAME;
	public static String FILE_REQUEST_NAME = "/request." + FILE_REGISTRATION_EXT_NAME;

	// the folder structures would be :
	// for the license file: ~/Library/Application Support/REL_PATH_SUPPORT/REL_PATH_LICENSE/FILE_LICENSE_NAME
	// for the request file: ~/Library/Application Support/REL_PATH_SUPPORT/REL_PATH_LICENSE/FILE_REQUEST_NAME
	// for the app preferences: ~/Library/Application Support/REL_PATH_SUPPORT/REL_PATH_SETUP/FILE_APP_PREFERENCES
	// for the patch preferences: ~/Library/Application Support/REL_PATH_SUPPORT/REL_PATH_SETUP/FILE_PATCH_PREFERENCES
	// for the default project: ~/Documents/REL_PATH_DOCUMENT/REL_PATH_PROJECTS/REL_PATH_DEFAULT/FILE_DEFAULT_PROJECT


	protected static String MENU_FILE_ITEM_PROJECT_NEW = "project new...";
	protected static String MENU_FILE_ITEM_PROJECT_LOAD = "project load...";
	protected static String MENU_FILE_ITEM_PROJECT_SAVEAS = "project saveas...";
	protected static String MENU_FILE_ITEM_PROJECT_SAVE = "project save";
	protected static String MENU_FILE_ITEM_PROPERTIES = "properties...";
	protected static String MENU_FILE_ITEM_LOAD_SERIAL = "load account...";
	protected static String MENU_FILE_ITEM_LOAD_REGISTRATION = "load registartion...";
	protected static String MENU_FILE_ITEM_REQ_REGISTRATION = "request registration...";

	private static String PREF_SPLASHSCREEN = "splashScreen";
	private static String PREF_STARTUPCOUNTER = "startupCounter";
	private static String PREF_SHUTDOWNCOUNTER = "shutdownCounter";
	private static String PREF_TOTALRUNTIME = "totalRuntime";
	private static String PREF_PUBLISHANONYMOUSLY = "publishAnonymously";
	private static String PREF_CHECKLATESTUPDATE = "checkLatestUpdate";
	private static String PREF_SHOWLICENSE_EXPIRED_WARNING = "showExpiredLicenseWarning";
	private static String PREF_SHOWLICENSE_ABOUTTO_EXPIRE_WARNING = "showLicenseAboutToExpireWarning";


	protected static int OUT_THISPATCHER  = 0;
	protected static int OUT_SEND  = 1;
	protected static int OUT_COMMANDS  = 2;
	protected static int OUT_FILEMENU  = 3;
	protected static int OUT_PATTRSTOR  =4;
	protected static int OUT_INFOWINDOWS  =5;
	protected static int OUT_SETTINGS  =6;

	protected final static int MODMODE_NULL  = 0;
	protected final static int MODMODE_DONE  = 1;
	protected final static int MODMODE_NEW  = 2;
	protected final static int MODMODE_SAVE  = 3;
	protected final static int MODMODE_SAVEALL  = 4;
	protected final static int MODMODE_SAVEAS  = 5;
	protected final static int MODMODE_LOAD  = 6;
	protected final static int MODMODE_DEMO  = 7;
	protected final static int MODMODE_READONLY  = 8;
	protected final static int MODMODE_AUTOSAVE  = 9;
	protected final static int MODMODE_AUTOSAVEALL  = 10;

	protected static String TYPE_PATCHER  = "_project";
	protected static String TYPE_APP  = "_app";

	protected final static int LEVEL_OFF = 0;
	protected final static int LEVEL_FREE = 1;
	protected final static int LEVEL_DONATION = 3;
	protected final static int LEVEL_MASTER = 5;
	protected final static int LEVEL_PRO = 7;
	protected final static int LEVEL_DEV = 9;

	protected final static int REQUEST_TYPE_FREE = 1;
	protected final static int REQUEST_TYPE_BASIC = 3;
	protected final static int REQUEST_TYPE_MASTER = 5;
	protected final static int REQUEST_TYPE_PRO = 7;
	protected final static int REQUEST_TYPE_DEV = 9;
	protected final static int REQUEST_TYPE_SPECIAL = 20;

	protected String appDataSupportPath;
	protected String documentsPath;
	protected Vectorizer vector;		// public key

	protected boolean hasValidRegistration = false;
	protected boolean hasExpiredRegistration = false;
	protected boolean hasDeveloperRegistration = false;

	private Registration localized; 	// license
	private Registration request; 	// license

	private String propertyType = TYPE_PATCHER;

	MaxBox hlpObjDeferlow, hlpObjSendMsg, hlpObjRoutePass, hlpObjFilePath;

	MaxBox hlpObjAccDeferlow, hlpObjAccSendMsg;

	String projectPath, projectFilePath, projectName, projectExtension, projectSettingsPath;

	String lastProjectPath = "notdefined";
	String lastProjectFilePath = "notdefined";

	int modificationMode = MODMODE_NULL;

	boolean readOnlyMode = false;
	boolean demoMode = false;
	boolean demoModeAvailable = true;

	boolean isStandalone = false;

	boolean startedUp = false, sentUsageData = false;

	boolean pref_checkLatestUpdate = false;
	int pref_startupCounter, pref_shutdownCounter, pref_totalRunningTime;

	public Project(Atom args[]) {
		post("SPARCK starting up...");

		declareInlets(new int[] { DataTypes.ALL });
		declareOutlets(new int[] { DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
		createInfoOutlet(true);

		if (args.length == 1){
			propertyType = args[0].getString();
			if(propertyType.equals(TYPE_APP)){
				isStandalone = true;
			}else{
				ouch("ERROR: YOUR LICENSE DOES NOT ALLOW YOU TO PLAY WITH THIS. A MESSAGE WITH YOUR SERIAL NUMBER HAS BEEN SENT TO THE DEVELOPERS.");
			}
		} else {
			propertyType = TYPE_PATCHER;
		}

		try {
			if(!setAppDataSupportPath()){
				//SPARCK is running the first time on this machine...
				post("SPARCK is running the first time on this machine. Creating supporting folders..");
			}
			mkdir(appDataSupportPath);
			mkdir(appDataSupportPath + REL_PATH_REGISTRATION);
			mkdir(appDataSupportPath + REL_PATH_SETUP);
			if(!setDocumentsPath()){
				//SPARCK is running the first time on this machine...
				post("SPARCK is running the first time on this machine. Creating documents folders..");
				mkdir(documentsPath);
				mkdir(documentsPath + REL_PATH_PROJECTS);
				mkdir(documentsPath + REL_PATH_PROJECTS + REL_PATH_DEFAULT);
			}
		} catch (Exception e1) {
			ouch("ERROR: Unable to locate application root" + e1.getMessage());
		}

		try {
			vector = new Vectorizer();
		} catch (Exception e) {
			ouch("ERROR: Unable to authenticate license: " + e.getMessage());
		}
		post("...evironment checking done...");
	}

	public void bang(){
		post("...initializing...");
		post("...checking registration ...");
		loadRegistration();
		post("...registration checked.");
		if(!startedUp){
			post("...loading preferences...");
			String filePath;
			if(isStandalone){
				filePath =  appDataSupportPath + REL_PATH_SETUP + FILE_APP_PREFERENCES;
			} else {
				filePath =  appDataSupportPath + REL_PATH_SETUP + FILE_PATCH_PREFERENCES;
			}
			Debug.verbose("app", "loading preferences...");
			outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("path"), Atom.newAtom(filePath)});
			if(hasDir(filePath)){
				outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom(1)});
			} else { // if the settings file doesnt exist yet:
				Debug.verbose("app", "creating preferences...");
				mkdir(appDataSupportPath + REL_PATH_SETUP); // make sure the setup folder exists
				if(isStandalone){ // check if it is a standalone
					// set all the default values
					outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom("properties::help::projectFolderWarning"), Atom.newAtom(1)});
					outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom("properties::help::splashWindowOnStartup"), Atom.newAtom(1)});
					outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom("properties::help::workspaceSpaceBarHint"), Atom.newAtom(1)});
					outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom("properties::help::stageViewNaviHint"), Atom.newAtom(1)});
					outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom("properties::help::showExpiredLicenseWarning"), Atom.newAtom(1)});
					outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom("properties::help::showLicenseAboutToExpireWarning"), Atom.newAtom(1)});
				// make sure the publish usage data is opt out:
					outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom("properties::statistics::publishAnonymously"), Atom.newAtom(1)});
					outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom("store"), Atom.newAtom(1)});
					outlet(OUT_PATTRSTOR, "pattrstorage", new Atom[]{Atom.newAtom("exec"), Atom.newAtom("writexml"), Atom.newAtom(filePath)});
				}
				Debug.verbose("app", "...creating preferences done");
			}
			startedUp = true;
			// create a new default project
			modificationMode = MODMODE_NULL;
			Debug.verbose("app", "setup default project...");
			setupProjectSettings(validateFileName(documentsPath + REL_PATH_PROJECTS + REL_PATH_DEFAULT + FILE_DEFAULT_PROJECT));
			Debug.verbose("app", "... setup default project done");
			propagateProjectSettings();
			send("bs::app::started", new Atom[] {Atom.newAtom(1)});
			post("...loading preferences [done]...");
		}
		post("...initializing [done].");
	}

	public void preferences(Atom[] list){
		Debug.verbose("app", "receiving preference: " + list[0]);
		if(list.length > 0){
			if(list[0].isString()){
				if(list[0].getString().equals(PREF_SPLASHSCREEN)){
					if(list[1].getInt() == 1){
						if(pref_checkLatestUpdate){
							post("...checking for current updates...");
							if(Registrator.checkInternetAccess("tecartlab.com")){
								if(!Registrator.isCurrentVersion()){
									infoScreen("splashScreen", new Atom[] {Atom.newAtom(1)} );
								} else {
									infoScreen("splashScreen", new Atom[] {Atom.newAtom(0)} );
								}
							} else {
								post("...unable to check for current updates (most likely unavailable internet)...");
							}
							post("...checking for current updates [done]...");
						} else {
							infoScreen("splashScreen", new Atom[] {Atom.newAtom(0)});
						}
					}
				} else if(list[0].getString().equals(PREF_SHOWLICENSE_EXPIRED_WARNING)){
					if(localized != null && list[1].getInt() == 1 && localized.hasExpired())
						infoScreen("request", "displayRegistration");
				} else if(list[0].getString().equals(PREF_SHOWLICENSE_ABOUTTO_EXPIRE_WARNING)){
					if(localized != null && list[1].getInt() == 1 && localized.isAboutToExpire())
						infoScreen("request", "displayRegistration");
				} else if(list[0].getString().equals(PREF_CHECKLATESTUPDATE)){
					pref_checkLatestUpdate = (list[1].getInt() == 1)? true: false;
				} else if(list[0].getString().equals(PREF_STARTUPCOUNTER)){
					pref_startupCounter = list[1].getInt();
				} else if(list[0].getString().equals(PREF_SHUTDOWNCOUNTER)){
					pref_shutdownCounter = list[1].getInt();
				} else if(list[0].getString().equals(PREF_TOTALRUNTIME)){
					pref_totalRunningTime = list[1].getInt();
				} else if(list[0].getString().equals(PREF_PUBLISHANONYMOUSLY)){
					// send only statistics if the user consents. but make sure
					// the first time the application start, the data is sent
					if(list[1].getInt() == 1 && sentUsageData == false){
						try{
							HttpServer.sendStartupLog(
									localized.getMachineInfos(),
									Registration.REGISTRATION_LEVEL_SHORT[localized.getlevel()],
									localized.getSerialNumber(),
									pref_startupCounter,
									pref_shutdownCounter,
									pref_totalRunningTime);
							sentUsageData = true;
						} catch (Exception e) {
							;
						}
					}
				}
			}
		}
	}

	protected boolean setAppDataSupportPath() throws Exception{
		appDataSupportPath = getAppDataSupportPath();
		return hasDir(appDataSupportPath);
	}

	protected boolean setDocumentsPath() throws Exception{
		documentsPath = System.getProperty("user.home") + "/Documents" + REL_PATH_DOCUMENT;
		documentsPath = documentsPath.replace("\\", "/");
		return hasDir(documentsPath);
	}

	protected static String getAppDataSupportPath() throws Exception{
		String staticRootPath;
		if(MaxSystem.isOsMacOsX()){
			staticRootPath = System.getProperty("user.home") + "/Library/Application Support" + REL_PATH_SUPPORT;
			staticRootPath = staticRootPath.replace("\\", "/");
			return staticRootPath;
		} else if(MaxSystem.isOsWindows()){
			staticRootPath = System.getProperty("user.home") + "/AppData/Roaming" + REL_PATH_SUPPORT;
			staticRootPath = staticRootPath.replace("\\", "/");
			return staticRootPath;
		}
		throw new Exception("Unable to identify Operating System.");
	}

	/**
	 * Checks if filename has correct extension
	 * @param _filename
	 * @return
	 */
	private String validateFileName(String _filename){
		if(!_filename.endsWith(".sparck")){
			return _filename + ".sparck";
		}
		return _filename;
	}

	/**
	 * Called when a new project should be created
	 * @param _select
	 */
	public void newproject(String _select){
		setupProjectSettings(validateFileName(_select));
		propagateProjectSettings();
		outlet(OUT_COMMANDS, "new");
		modificationMode = MODMODE_DONE;
	}


	/**
	 * Called when a new project should be saved
	 * @param _select
	 */
	public void saveas_project(String _select){
		setupProjectSettings(validateFileName(_select));
		propagateProjectSettings();
		outlet(OUT_COMMANDS, "saveas");
		modificationMode = MODMODE_DONE;
	}

	/**
	 * called when a current project should be saved
	 */
	public void save_project(){
		if(projectFilePath != null){
			propagateProjectSettings();
			outlet(OUT_COMMANDS, "save");
			modificationMode = MODMODE_DONE;
		}
	}

	/**
	 * called when a current project should be saved
	 */
	public void saveAll_project(){
		//if(projectFilePath != null && ((isStandalone && demoMode == false) || hasDeveloperLicense)){
		if(projectFilePath != null){
			propagateProjectSettings();
			outlet(OUT_COMMANDS, "saveall");
			modificationMode = MODMODE_DONE;
		}
	}

	/**
	 * called when a current project should be autosaved
	 */
	public void autosave_project(int _all){
		//if(projectFilePath != null && ((isStandalone && demoMode == false) || hasDeveloperLicense)){
		// only start autosaving when a proper project was loaded.
		if(projectFilePath != null && modificationMode != MODMODE_NULL){
			propagateProjectSettings();
			if(_all == 0)
				outlet(OUT_COMMANDS, "autosave", 0);
			else
				outlet(OUT_COMMANDS, "autosave", 1);
		}
	}

	/**
	 * called when a project should be loaded
	 * @param _select
	 */
	public void loadproject(String _select){
		setupProjectSettings(_select);
		propagateProjectSettings();
		outlet(OUT_COMMANDS, "load");
	}

	/**
	 * creates all the different project settings
	 * @param _select
	 */
	private void setupProjectSettings(String _select){
		//extract project infos
		projectPath = _select.substring(0, _select.lastIndexOf("/"));
		projectFilePath = _select;
		if(_select.indexOf(".sparck")!= -1){ // there is an extension
			projectName = _select.substring(_select.lastIndexOf("/")+1, _select.lastIndexOf(".sparck"));
			projectExtension = _select.substring(_select.lastIndexOf(".sparck")+1);
		} else {
			projectName = _select.substring(_select.lastIndexOf("/")+1);
			projectExtension = "xml";
		}
		projectSettingsPath = projectPath+"/_settings/"+propertyType+"/_"+projectName;
	}

	/**
	 * Propagates the project settings to the responsible dictionary. should not be called in the
	 * same thread as propagateReadOnlySettings().
	 */
	private void propagateProjectSettings(){
		this.settings("settings", Atom.newAtom(new String[] {"projectPath", projectPath}));
		this.settings("settings", Atom.newAtom(new String[] {"projectName", projectName}));
		this.settings("settings", Atom.newAtom(new String[] {"projectExtension", projectExtension}));
		this.settings("settings", Atom.newAtom(new String[] {"projectFilePath", projectFilePath}));
		this.settings("settings", Atom.newAtom(new String[] {"projectSettingsPath", projectSettingsPath}));

		if(!lastProjectFilePath.equals(projectFilePath)){
			lastProjectFilePath = projectFilePath;
			mkdir(projectPath + "/_settings");
			mkdir(projectPath + "/_settings/" + propertyType);
			mkdir(projectPath + "/_settings/" + propertyType + "/_autosave");
		}

		// make sure the projects settings folder exist
		mkdir(projectPath + "/_settings/" + propertyType + "/_" + projectName);

		if(!lastProjectPath.equals(projectPath)){
			lastProjectPath = projectPath;

			this.settings("filepath", Atom.newAtom(new String[] {"revert"}));
			this.settings("filepath", Atom.newAtom(new String[] {"append", projectPath, "1"}));

			mkdir(projectPath + "/_assets");
			mkdir(projectPath + "/_assets/_scripts");
			mkdir(projectPath + "/_assets/_scripts/_ques");
			mkdir(projectPath + "/_assets/_shaders");
			mkdir(projectPath + "/_assets/_shaders/_raymarch");
			mkdir(projectPath + "/_assets/_textures");
			mkdir(projectPath + "/_assets/_textures/_volumes");
			mkdir(projectPath + "/_assets/_textures/_cubemaps");
			mkdir(projectPath + "/_assets/_videos");
			mkdir(projectPath + "/_assets/_videos/_hap");
			mkdir(projectPath + "/_assets/_materials");
			mkdir(projectPath + "/_assets/_warps");
			mkdir(projectPath + "/_assets/_models");
			mkdir(projectPath + "/_assets/_models/_calib");
			mkdir(projectPath + "/_assets/_models/_warps");
			mkdir(projectPath + "/_assets/_projectors");
			mkdir(projectPath + "/_assets/_projectors/_calib");
			mkdir(projectPath + "/_assets/_projectors");
			mkdir(projectPath + "/_assets/_nodes");
			mkdir(projectPath + "/_assets/_paths");
			mkdir(projectPath + "/_assets/_paths/_ledstrips");
			mkdir(projectPath + "/_assets/_paths/_animations");
			mkdir(projectPath + "/_assets/_patchers");

			mkdir(projectPath + "/_tmp");
			mkdir(projectPath + "/_export");
			mkdir(projectPath + "/_export/_textures");
			mkdir(projectPath + "/_export/_projectors");
			mkdir(projectPath + "/_export/_warps");
		}

	}

	/**
	 * Make dir helper method
	 * @param _foldername
	 */
	private void mkdir(String _foldername){
		if(_foldername.indexOf(":/") > 1){ // it is most probable a mac drive
			_foldername = _foldername.substring(_foldername.indexOf(":/") + 1);
		}
		_foldername = _foldername.replace("\\", "/");

		File newFolder = new File(_foldername);

		try{
			Debug.verbose("Folder creation","check if folder exists: " + _foldername);
			if(!newFolder.exists()){
				Debug.verbose("Folder creation","try creating: " + _foldername + "....");
				if(newFolder.mkdir()){
					Debug.verbose("Folder creation","... successfully created folder.");
				} else {
					Debug.verbose("Folder creation","... unable to create folder: " + _foldername + " Please do it manually.");
				}
				//post("creating folder: '" + _foldername);
			}
		} catch (SecurityException Se){
			Debug.error("Folder creation","Error creating folder: '" + _foldername + "' -> " + Se.toString());
		}
	}

	/**
	 * checks if this folder or file exists.
	 * @param _foldername
	 * @return
	 */
	private boolean hasDir(String _foldername){
		_foldername = _foldername.replace("\\", "/");
		if(_foldername.startsWith("Macintosh"))
			_foldername = _foldername.substring(_foldername.indexOf("/"));

		return new File(_foldername).exists();
	}

	/**
	 * This method populates the dictionary with the access restrictions for the nodes
	 * @param _level
	 */
	private void setLevel(){

//		post("setLicensingLevel... \n");
		// 		store the level info
		int _level = 1;
        makeDictMessage("_level", _level);

		// *** LIMITED ACCESS / LEVEL == 1 ... 9 ***

		String setLevel = "0|1|3|5|7|9";

		// Only node that has a restriction on how many nodes can exist.
		makeDictNode("Window", 1, "bs.node.output.window", setLevel, "$_#", -1, 4, 4, 1);

		// inactive nodes: (nodes that can still be used in projects, but no new nodes can be generated
		makeDictNode("Beamer", 1, "bs.node.beamer", setLevel, "$_#", -1, -1, -1, 0);
		makeDictNode("Calibrator", 1, "bs.node.calibrator", setLevel, "$_#", -1, -1, -1, 0);

		makeDictNode("ViewPort", 1, "bs.node.output.viewport", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("subnode", 1, "bs.vpl.node.util.subnode", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("sub-inlet", 1, "bs.vpl.node.util.subnode.inlets", setLevel, "$_#", 1, -1, 1, 1);
		makeDictNode("sub-outlet", 1, "bs.vpl.node.util.subnode.outlets", setLevel, "$_#", 1, -1, 1, 1);

		makeDictNode("CornerPin", 1, "bs.node.pop.cornerpin", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("QueScript", 1, "bs.node.script.que", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("Video", 1, "bs.node.tex.moovie", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("Texture", 1, "bs.node.tex.texture", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TextureBrCoSa", 1, "bs.node.shader.tex.brcosa", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TextureColorMap", 1, "bs.node.shader.tex.colormap", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TextureBlur", 1, "bs.node.shader.tex.blur", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TextureOP", 1, "bs.node.shader.tex.op", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TextureZoom", 1, "bs.node.shader.tex.zoom", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TextureMask", 1, "bs.node.tex.mask", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TextureAnaglyph", 1, "bs.node.shader.tex.anaglyph", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("CubeMap", 1, "bs.node.tex.cubemap", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("SyphonReceive", 1, "bs.node.tex.syphon.client", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("SpoutReceive", 1, "bs.node.tex.spout.client", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("SyphonSend", 1, "bs.node.tex.syphon.server", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("SpoutSend", 1, "bs.node.tex.spout.server", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("Monitor", 1, "bs.node.output.monitor", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("MeshWarp", 1, "bs.node.pop.meshwarp", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("Model", 1, "bs.node.pop.model", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("Canvas", 1, "bs.node.pop.canvas", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("Shape", 1, "bs.node.pop.shape", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("Material", 1, "bs.node.shader.material", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("Light", 1, "bs.node.light", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("Grid", 1, "bs.node.pop.grid", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("TextureProjection", 2, "bs.node.shader.tex.proj", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("RayMarcher", 1, "bs.node.shader.tex.proj.raymarcher", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("LedStrip", 1, "bs.node.pop.ledstrip", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("LedStripOne", 1, "bs.node.pop.ledstrip.one", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("PixelPusher", 1, "bs.node.output.pixelpusher", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("OculusRift", 1, "bs.node.vr.oculus", setLevel, "$_#", -1, 1, 1, 1);
		makeDictNode("HtcVive", 1, "bs.node.vr.htcvive", setLevel, "$_#", -1, 1, 1, 1);
		makeDictNode("VrHMD", 1, "bs.node.vr.hmd", setLevel, "$_#", -1, 1, 1, 1);

		makeDictNode("SceneCamera", 1, "bs.node.camera.stereo", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("SceneCapture", 1, "bs.node.capture.stereo", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("Projector", 1, "bs.node.projector", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("OscSend", 1, "bs.node.net.osc.send", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("OscMessage", 1, "bs.node.net.osc.message", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("TfmNode", 1, "bs.node.tfm.node", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TfmNodePath", 1, "bs.node.tfm.node.path", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TfmNodeInfo", 1, "bs.node.tfm.node.info", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TfmNodeMerge", 1, "bs.node.tfm.node.merge", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TfmLookAt", 1, "bs.node.tfm.lookat", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("TfmMirror", 1, "bs.node.tfm.mirror", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("LookAtCamera", 1, "bs.node.camera.lookat", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("BoxMapCamera", 1, "bs.node.camera.boxmap", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("BoxMapCapture", 1, "bs.node.capture.boxmap", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("TextureStitcher", 1, "bs.node.shader.tex.stitch", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("BoxMapProjection", 1, "bs.node.shader.tex.proj.boxmap", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("SkyBox", 1, "bs.node.pop.skybox", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("ShaderSelection", 1, "bs.node.shader.selection", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("SpatialSoftedge", 2, "bs.node.shader.spatial.softedge", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("BlendSoftedge", 2, "bs.node.shader.tex.blend.softedge", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("TurnTable", 1, "bs.node.tfm.turntable", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("Optitrack", 1, "bs.node.tfm.optitrack", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("HeadRoom", 1, "bs.node.tfm.headRoom", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("HeadRoom.RS", 1, "bs.node.tfm.headRoom.RS", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("Volume", 1, "bs.node.pop.volume", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("VolumeShader", 1, "bs.node.shader.volume", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("VolumeLoader", 1, "bs.node.tex.volume.loader", setLevel, "$_#", -1, -1, -1, 1);

		makeDictNode("Patcher", 1, "bs.node.script.patcher", setLevel, "$_#", -1, -1, -1, 1);
		makeDictNode("SSAO", 1, "bs.node.shader.tex.ssao", setLevel, "$_#", -1, -1, -1, 1);

		// *** DEVELOPER NODES ***

		makeDictNode("Physics", 1, "bs.node.physics", setLevel, "$_#", -1, -1, -1, 1);

	}

	/**
	 * helper function to create a dict node entry
	 * @param _nodeName
	 * @param _fileName
	 * @param _titlePattrn
	 * @param _branchLevel
	 * @param _maxNodes
	 * @param _branchMaxNodes
	 * @param _active
	 */
	private void makeDictNode(String _nodeName,
								int _version,
								String _fileName,
								String _levels,
								String _titlePattrn,
								int _branchLevel,
								int _maxNodes,
								int _branchMaxNodes,
								int _active){
		makeDictMessage(_nodeName + "::nodeCreation::_version", _version);
		makeDictMessage(_nodeName + "::nodeCreation::_fileName", _fileName);
		makeDictMessage(_nodeName + "::nodeCreation::_titlePattrn", _titlePattrn);
		makeDictMessage(_nodeName + "::nodeCreation::_branchLevel", _branchLevel);
		makeDictMessage(_nodeName + "::nodeCreation::_maxNodes", _maxNodes);
		makeDictMessage(_nodeName + "::nodeCreation::_branchMaxNodes", _branchMaxNodes);
		makeDictMessage(_nodeName + "::nodeCreation::_level", _levels);
		}

	/**
	 * helper method to create a script message
	 * @param _msg
	 * @param _arg
	 */
	private void makeDictMessage(String _msg, String _arg){
		this.settings("dict", new Atom[]{ Atom.newAtom("replace"), Atom.newAtom(_msg), Atom.newAtom(_arg)});
	}

	/**
	 * helper method to create a script message
	 * @param _msg
	 * @param _arg
	 */
	private void makeDictMessage(String _msg, int _arg){
		this.settings("dict", new Atom[]{ Atom.newAtom("replace"), Atom.newAtom(_msg), Atom.newAtom(_arg)});
	}


	/**
	 * loads the registration (if it finds one)
	 *
	 * - if it finds a valid one: set the app free accordingly
	 * - else if it is expired but has a soft expiration flag:
	 * 		- if the app is older than the license expiration day: set it free
	 * - no registration found, give info screen about limitations
	 */

	protected void loadRegistration(){
		Debug.verbose("registration", "checking...");
		try {
			hasValidRegistration = false;
			localized = new Registration();
			localized.read(appDataSupportPath+ REL_PATH_REGISTRATION + FILE_LICENSE_NAME, vector.vectorizer);
			if(localized.isValid()){
				hasValidRegistration = true;
				post("Valid Sparck registration found: " + localized.forPublic());
			} else if(localized.hasExpired()){
				if(localized.canSoftExpire()){
					if(localized.hasSoftExpired()){
						ouch("WARNING: Expired Sparck Donation registration found: " + localized.forPublic());
						post("The good news: you can keep patching without an issue");
						hasValidRegistration = true;
					}
				}
			}
			else {
				ouch("WARNING: No Sparck registration found");
			}
		}catch(IOException e){
			ouch("WARNING: No Sparck registration found. The registration-file 'sparck.reg' is expected to be inside the folder '" + appDataSupportPath+"/registration '");
		}catch (Exception e) {
			ouch("WARNING: The Sparck registration file is corrupt. The Sparck registration-file 'sparck.reg' (" + appDataSupportPath+"/registration/sprack.reg ) is corrupted and needs to be replaced with a valid registration.");
		}
		if(isStandalone && hasValidRegistration == false){
			infoScreen("request", "displayRegistration");
		}
		setLevel();
		Debug.verbose("registration", "...checked");
	}

	/**
	 * called when user calls "menu>donation>manage..."
	 */
	public void manageRegistration(){
		infoScreen("request", "manageRegistration");
	}

	/**
	 * called to display current registration infos
	 */
	public void displayRegistration(){
		infoScreen("request", "displayRegistration");
	}


	/**
	 * Called when user selects an option from the splash screen
	 * @param _command
	 */
	public void splashRequest(int _command){
		outlet(OUT_FILEMENU, "filecommand", new Atom[]{Atom.newAtom(_command)});
	}

	/**
	 * Sends a message to the info screens
	 * @param _type
	 * @param _msg
	 */
	private void infoScreen(String _type, String _msg){
		outlet(OUT_INFOWINDOWS, _type, _msg);
	}

	/**
	 * Sends a message to the info screens
	 * @param _type
	 * @param _msg
	 */
	private void infoScreen(String _type, Atom[] _msg){
		outlet(OUT_INFOWINDOWS, _type, _msg);
	}

	/**
	 * Sends a message to the settings
	 * @param _type
	 * @param _msg
	 */
	private void settings(String _type, Atom[] _msg){
		outlet(OUT_SETTINGS, _type, _msg);
	}

	/**
	 * sends messages into max space
	 * @param _address
	 * @param _list
	 */
	private void send(String _address, Atom[] _list){
		outlet(OUT_SEND, _address, _list);
	}

}
