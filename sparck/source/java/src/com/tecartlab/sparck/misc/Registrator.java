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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import java.util.*;

import com.cycling74.max.*;

import com.tecartlab.utils.Debug;

public class Registrator extends MaxObject{

	protected static int OUT_COMMANDS  = 0;
	protected static int OUT_THISPATCHER  = 1;
	protected static int OUT_MASTER  = 2;

	protected String rootPath;
	protected Vectorizer vector;		// public key

	protected boolean hasMisMatchingRegistration = false;
	protected boolean hasValidRegistration = false;
	protected boolean hasExpiredRegistration = false;
	protected boolean hasUnregisteredRegistration = true;
	protected boolean hasDeveloperLicense = false;

	private Registration localized; 	// license
	private Registration request; 	// license

	String emailVerification;

	boolean isStandalone = false;

	static protected String DOWNLOAD_URL = "http://sparck.tecartlab.com/releaseDB/" + Versioner.SERVER_RELEASE_DB;

	private int 	request_type = 0;
	private String 	request_couponName = "";
	private int 	request_couponPercent = 0;
	private int 	request_priceListing = 0;
	private int 	request_priceReduced = 0;
	private String  request_oldSerial = "";

	public Registrator(Atom args[]) {
		declareInlets(new int[] { DataTypes.ALL });
		declareOutlets(new int[] { DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
		createInfoOutlet(true);

		isStandalone = true;

		try {
			setRootPath();
		} catch (Exception e1) {
			ouch("ERROR: Unable to locate application root" + e1.getMessage());
		}

		try {
			vector = new Vectorizer();
		} catch (Exception e) {
			ouch("ERROR: Unable to authenticate license: " + e.getMessage());
		}
		Debug.setLevelToVerbose();
	}

	protected void setRootPath() throws Exception{
		rootPath = Project.getAppDataSupportPath();
	}

	/**
	 * Starts the Authorization process. Is called by bs.info.request.error.internetaccess and the
	 * Property
	 */
	public void manageRegistration(){
		request_couponName = "";

		outlet(OUT_THISPATCHER, "front"); // open this patcher window
		loadRegistration();
		if(checkInternetAccess("tecartlab.com")){
			if(isCurrentVersion())
				infoScreen("request.tasks", "done");
			else
				infoScreen("request.oldBuild", "done");
		}else{
			infoScreen("request.error", "internetaccess");
			infoScreen("request.error", "done");
		}
	}

	public void close(){
		outlet(OUT_THISPATCHER, "wclose"); // close this patcher window
	}

	/**
	 * Called by bs.info.request.task.
	 */
	public void selectLicense(){
		try {
			infoScreen("request.license.selector", "basicPrice", ""+getLevelPrice(Project.LEVEL_DONATION));
			infoScreen("request.license.selector", "proPrice", ""+getLevelPrice(Project.LEVEL_PRO));
			infoScreen("request.license.selector", "masterPrice", ""+getLevelPrice(Project.LEVEL_MASTER));
			infoScreen("request.license.selector", "done");
		} catch (Exception e) {
			infoScreen("request.error", "internetaccess");
		}
	}

	/**
	 * Called by bs.info.request.license.selector.
	 *
	 * receives the license choice and
	 * 1. creates the license request file
	 * 2. sends the request file to its designated email account
	 * 3. removes the license request file
	 */
	public void licenseChoice(int _level){
		request_type = _level;
		if(_level < 10){
			request = new Registration();
			try {
				request.read(rootPath+ Project.REL_PATH_REGISTRATION + Project.FILE_LICENSE_NAME, vector.vectorizer);
			}
			catch (Exception e) {;}

			if(request.getlevel() > 1){
				// if the current license is BASIC, PRO or DEV
				request_oldSerial = request.getSerialNumber();
			} else {
				request_oldSerial = "no serial on that machine";
			}

			// it creates different kind of request depending on the current license:
			// NEW, UPGRADE, UPDATE, DOWNGRADE.
			request.prepareRequest(_level);

			infoScreen("request.form", "prepare");
			infoScreen("request.form", "done");
		}
	}

	/**
	 * Called by bs.info.request.license.selector.
	 * called when selected Others
	 */
	public void licenseSpecial(){
		request_type = Project.REQUEST_TYPE_SPECIAL;
	}

	/**
	 * Called by bs.info.request.form
	 *
	 * @param _name
	 * @param _email
	 */
	public void validateEmail(String _name, String _email){
		emailVerification =  "cd" + Math.random()*100;
		emailVerification = emailVerification.trim().substring(0, 7);

		if(request != null){
			request.setEmail(_email);
			request.setName(_name);
		}

		requestRegistration();
		try {
			HttpServer.sendEmailVerification(_name, _email, emailVerification, Registration.REGISTRATION_LEVEL[request_type], localized.getMachineInfos());
			//if successfull
		} catch (IOException e) {
			Debug.error("RequestRegistration", "generating email verification: " + e.getMessage());
			if(checkInternetAccess("tecartlab.com")){
				infoScreen("request.error", "emailInvalid");
			} else {
				infoScreen("request.error", "internetaccess");
			}
		}
	}

	public void requestRegistration(){
		try{
			request_priceListing = getLevelPrice(request.getlevel());
			request_priceReduced = (int)((float)request_priceListing * ((100f-(float)request_couponPercent)) / 100f);
			if(request_type == Project.REQUEST_TYPE_FREE){
				infoScreen("request.license.confirm.free", "name", request.getName());
				return;
			} else if(request_type == Project.REQUEST_TYPE_BASIC ||
					request_type == Project.REQUEST_TYPE_MASTER ||
					request_type == Project.REQUEST_TYPE_PRO){
				infoScreen("request.license.confirm.payment", "name", request.getName());
				infoScreen("request.license.confirm.payment", "listprice", ""+request_priceListing);
				infoScreen("request.license.confirm.payment", "reducedprice", ""+request_priceReduced);
				infoScreen("request.license.confirm.payment", "licensename", ""+request.getLicenseLevel().toUpperCase());
				return;
			}
		}
		catch (Exception e) {
			infoScreen("request.error", "internetaccess");
		}
	}

	/**
	 * Called by license.confirm.xxx
	 */
	public void confirmRequest(){
		String filePath = rootPath + Project.REL_PATH_REGISTRATION + Project.FILE_LICENSE_NAME;

		if(request_type == Project.REQUEST_TYPE_FREE){
			try{
				if(downloadLicense(filePath, getFreeActivation())){
					outlet(OUT_MASTER, 1); // tells the master to update its license
					displayRegistration();
				}
			} catch (IOException e){
				infoScreen("request.error", "authorize");
			}
		} else if(request_type == Project.REQUEST_TYPE_BASIC ||
				request_type == Project.REQUEST_TYPE_MASTER ||
				request_type == Project.REQUEST_TYPE_PRO){
			try {
				HttpServer.sendOrderConfirmation(
							request.getEmail(),
							request.getName(),
							Registration.REGISTRATION_LEVEL[request.getlevel()],
							"" + request_priceReduced,
							request.getSerialNumber(),
							request_couponName);

				infoScreen("request.license.finalize.payment", "paypallink",
						createPayPalLink(
							request.getlevel(),
							request_priceReduced,
							request.getSerialNumber() + "(" + request_couponName  + ")"));

				infoScreen("request.license.finalize.payment", "done");

				downloadLicense(filePath, getFreeActivation());

			} catch (IOException e){
				infoScreen("request.error", "authorize");
			}	catch (Exception e) {
				infoScreen("request.error", "genLicenseRequest");
				Debug.error("RequestDonation", "generating sparck donation request: " + e.getMessage());
			}
		}
		return;
	}

	private String createPayPalLink(int _level, int _price, String _serial){
		return "https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business=EEJ6WS6JLJS82&lc=CH"
				+ "&item_name=SPARCK%20%2d%20" + Registration.REGISTRATION_LEVEL[_level]
				+ "&item_number=" + _serial
				+ "&amount="+ _price +"%2e00"
				+ "&currency_code=EUR";
	}

	/**
	 * Called by bs.info.request.tasks. This gets the form to enter the authorization code
	 * and downloads the license
	 */
	public void enterAuthorizationCode(){
		//if successfull
		infoScreen("request.authorization", "done");
	}

	/**
	 * Called by bs.info.request.authorization
	 * @param _code
	 */
	public void authorize(String _code){
		String filePath = rootPath + Project.REL_PATH_REGISTRATION + Project.FILE_LICENSE_NAME;
		if(checkInternetAccess("tecartlab.com")){
			try{
				if(downloadLicense(filePath, _code)){
					outlet(OUT_MASTER, 1); // tells the master to update its license
					displayRegistration();
				}
			} catch (IOException e){
				infoScreen("request.error", "authorize");
			}
		} else {
			infoScreen("request.error", "internetaccess");
		}
	}

	/**
	 * called to display current registration infos
	 */
	public void displayRegistration(){
		outlet(OUT_THISPATCHER, "front"); // open this patcher window
		loadRegistration();
		if(hasMisMatchingRegistration){
			infoScreen("license.invalid", "done");
		} else if(hasUnregisteredRegistration){
			infoScreen("license.unregistered", "done");
		} else if(localized != null){
			infoScreen("license.info", localized.getNameMsg());
			infoScreen("license.info", localized.getEmailMsg());
			infoScreen("license.info", localized.getSerialNumberMsg());
			infoScreen("license.info", localized.getLevelMsg());
			infoScreen("license.info", localized.getEscapeMsg());
			infoScreen("license.info", localized.getYearMsg());
			infoScreen("license.info", localized.getMonthMsg());
			infoScreen("license.info", localized.getDayMsg());
			infoScreen("license.info", localized.getIssuedMsg());
			if(!localized.isValid()){
				if(localized.canSoftExpire()){
					if(localized.hasSoftExpired()){
						infoScreen("license.info",
								new Atom[]{Atom.newAtom("softexpired"),
								Atom.newAtom("WARNING: Your Donation Registration has expired.")});

					} else {
						infoScreen("license.info",
								new Atom[]{Atom.newAtom("hardexpired"),
								Atom.newAtom("WARNING: Your Registration has expired.")});
					}
				} else {
					infoScreen("license.info",
							new Atom[]{Atom.newAtom("hardexpired"),
							Atom.newAtom("WARNING: Your Registration has expired. Please "+
							"register for a new one. ")});
				}
				infoScreen("license.info", new Atom[]{Atom.newAtom("info"), Atom.newAtom(localized.forPublic())});
				infoScreen("license.info", "done");
			} else if(localized.isAboutToExpire()){
				infoScreen("license.info",
						new Atom[]{Atom.newAtom("expiressoon"),
						Atom.newAtom("WARNING: Your Registration is about to expire soon! ")});
			}
		}
	}

	/**
	 * Check if there is an internet connect through a DNS lookup
	 * @param _url
	 * @return
	 */
	protected static boolean checkInternetAccess(String _url)
	{
		try{
			InetAddress.getByName(_url);
		} catch (UnknownHostException e) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the Price for the license from the internet.
	 * @param _level
	 * @return
	 */
	private int getLevelPrice(int _level) throws Exception{
		URL url;
		try {
			url = new URL(DOWNLOAD_URL + "/prices/" + Registration.REGISTRATION_LEVEL_SHORT[_level]+ ".txt");
			Scanner s = new Scanner(url.openStream());
			int price = s.nextInt();
			s.close();
			return price;
		} catch (NoSuchElementException e) {
			throw new IOException("Price lookup failed: " + e.getMessage());
		} catch (MalformedURLException e) {
			throw new IOException("Price lookup failed: " + e.getMessage());
		} catch (IOException e) {
			throw new IOException("Price lookup failed: " + e.getMessage());
		}
	}

	/**
	 * get the current free activation code
	 * @return
	 */
	private String getFreeActivation() throws IOException{
		URL url;
		try {
			url = new URL(DOWNLOAD_URL + "/freeactivation/current.txt");
			Scanner s = new Scanner(url.openStream());
			return s.nextLine();
		} catch (NoSuchElementException e) {
			throw new IOException("Activation code lookup failed: No such Element");
		} catch (MalformedURLException e) {
			throw new IOException("Activation code lookup failed: MalformedURL");
		} catch (IOException e) {
			throw new IOException("Activation code lookup failed: " + e.getMessage());
		}
	}

	/**
	 * Checks on the internet to see if this is the current Version
	 * @return true if this is the case
	 */
	static protected boolean isCurrentVersion(){
		URL url;
		try {
			url = new URL(DOWNLOAD_URL + "/build/current.txt");
			Scanner s = new Scanner(url.openStream());
			int currentBuild = s.nextInt();
			s.close();
			if(currentBuild <= Versioner.BUILD_NUMBER)
				return true;
		} catch (NoSuchElementException e) {
			return true;
		} catch (MalformedURLException e) {
			return true;
		} catch (IOException e) {
			return true;
		}
		return false;
	}

	/**
	 * Downloads the license file
	 *
	 * @param _fileName
	 * @param _serialNumber
	 */
	private boolean downloadLicense(String _fileName, String _serialNumber) throws IOException{

		 String _url = DOWNLOAD_URL + "/registrations/"+_serialNumber+"." + Project.FILE_REGISTRATION_EXT_NAME;

		 try{
			 URL link = new URL(_url); //The file that you want to download

			 InputStream in = new BufferedInputStream(link.openStream());
			 ByteArrayOutputStream out = new ByteArrayOutputStream();
			 byte[] buf = new byte[1024];
			 int n = 0;
			 while (-1!=(n=in.read(buf)))
			 {
			    out.write(buf, 0, n);
			 }
			 out.close();
			 in.close();
			 byte[] response = out.toByteArray();

			 FileOutputStream fos = new FileOutputStream(_fileName);
			 fos.write(response);
			 fos.close();

			 return true;
		 } catch (MalformedURLException e){
			 Debug.error("RequestLicense", "Unable to contact license-server. Please contact support.");
			 throw new IOException("Unable to contact license-server. Please contact support.");
		 }catch (java.io.FileNotFoundException e){
			 if(e.getMessage().startsWith(DOWNLOAD_URL)){
				 Debug.error("RequestLicense", "Unable to contact license-server for ("+_serialNumber+"). Please check activation code and make sure you have internet connection");
				 throw new IOException("Unable to contact license-server for ("+_serialNumber+"). Please check activation code and make sure you have internet connection.");
			 }
		 } catch (java.io.IOException e){
			 Debug.error("RequestLicense","Unable to store license file. (" + e.getMessage() +"). Please contact support.");
			 throw new IOException("Unable to store license file. (" + e.getMessage() +"). Please contact support.");
		 }
		 return false;

	}

	/**
	 * Removes file
	 *
	 * @param _fileName
	 * @return
	 */
	private boolean rmFile(String _fileName){
		_fileName = _fileName.replace("\\", "/");
		if(_fileName.startsWith("Macintosh"))
			_fileName = _fileName.substring(_fileName.indexOf("/"));

		File fileToBeDeleted = new File(_fileName);

		try{
			if(fileToBeDeleted.exists()){
				return fileToBeDeleted.delete();
			} else {
				Debug.error("RequestLicense", "unable to remove request file ("+_fileName+"): -> file doesnt exist.");
			}
		} catch (SecurityException Se){
			Debug.error("RequestLicense", "unable to remove request file ("+_fileName+"): -> " + Se.toString());
		}
		return false;
	}


	/**
	 * loads the registration (if it finds one)
	 */
	private void loadRegistration(){
		hasMisMatchingRegistration = false;
		hasValidRegistration = false;
		hasExpiredRegistration = false;
		hasUnregisteredRegistration = true;
		try {
			localized = new Registration();
			localized.read(rootPath + Project.REL_PATH_REGISTRATION + Project.FILE_LICENSE_NAME, vector.vectorizer);
			if(localized.isValid()){
				hasValidRegistration = true;
				hasUnregisteredRegistration = false;
			} else if(localized.hasExpired()){
				hasExpiredRegistration = true;
				hasUnregisteredRegistration = false;
			} else if(isStandalone){
				hasUnregisteredRegistration = true;
			}
		}catch (Exception e) {
			;
		}
	}

	/**
	 * Sends a message to the info screens
	 * @param _type
	 * @param _msg
	 */
	private void infoScreen(String _type, String _msg){
		outlet(OUT_COMMANDS, _type, _msg);
	}

	/**
	 * Sends a message to the info screens
	 * @param _type
	 * @param _msg
	 * @param _arg
	 */
	private void infoScreen(String _type, String _msg, String _arg){
		outlet(OUT_COMMANDS, _type, new Atom[]{Atom.newAtom(_msg), Atom.newAtom(_arg)});
	}

	/**
	 * Sends a message to the info screens
	 * @param _type
	 * @param _msg
	 */
	private void infoScreen(String _type, Atom[] _msg){
		outlet(OUT_COMMANDS, _type, _msg);
	}

}
