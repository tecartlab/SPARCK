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

import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import java.util.*;

import com.cycling74.max.Atom;

import com.tecartlab.utils.Debug;

public class Registration implements Serializable {
	private static int SIZE_VERSION 	= 2;
	private static int SIZE_ESCAPE 		= 2;
	private static int SIZE_REQUEST 	= 2;
	private static int SIZE_LEVEL 		= 2;
	private static int SIZE_YEAR 		= 4;
	private static int SIZE_MONTH 		= 2;
	private static int SIZE_DAY 		= 2;
	private static int SIZE_NAME 		= 30;
	private static int SIZE_EMAIL 		= 40;
	private static int SIZE_NUMBER 		= 27;
	private static int SIZE_ID 			= 80;
	private static int SIZE_ISSUED 		= 8;
	private static int SIZE_EXPIRING 	= 2;

	private static int SIZE_FLAG_A 		= 14;
	private static int SIZE_FLAG_B 		= 14;
	private static int SIZE_FLAG_C 		= 14;

	private static int SIZE 			= 245; //<- this number should not change anymore

	private static int INDEX_VERSION 	= 0; //4
	private static int INDEX_ESCAPE 	= INDEX_VERSION + SIZE_VERSION;
	private static int INDEX_REQUEST 	= INDEX_ESCAPE + SIZE_ESCAPE ;
	private static int INDEX_LEVEL 		= INDEX_REQUEST + SIZE_REQUEST;
	private static int INDEX_YEAR		= INDEX_LEVEL + SIZE_LEVEL ;
	private static int INDEX_MONTH 		= INDEX_YEAR + SIZE_YEAR;
	private static int INDEX_DAY 		= INDEX_MONTH + SIZE_MONTH;
	private static int INDEX_NAME 		= INDEX_DAY + SIZE_DAY;
	private static int INDEX_EMAIL 		= INDEX_NAME + SIZE_NAME;
	private static int INDEX_NUMBER 	= INDEX_EMAIL + SIZE_EMAIL;
	private static int INDEX_ID 		= INDEX_NUMBER + SIZE_NUMBER ;
	private static int INDEX_ISSUED 	= INDEX_ID + SIZE_ID ;
	private static int INDEX_EXPIRING 	= INDEX_ISSUED + SIZE_ISSUED ;

	private static int INDEX_FLAG_A 	= INDEX_EXPIRING + SIZE_EXPIRING ;
	private static int INDEX_FLAG_B 	= INDEX_FLAG_A + SIZE_FLAG_A ;
	private static int INDEX_FLAG_C 	= INDEX_FLAG_B + SIZE_FLAG_B ;

	private static String FLAG_FILL = "00000000000000";

	protected static String[] REGISTRATION_LEVEL = {"Unregistered", "Free", "nan", "Basic", "nan", "Master", "nan", "Pro", "nan", "Developer"};
	protected static String[] REGISTRATION_LEVEL_SHORT = {"UNR", "FRE", "nan", "BAS", "nan", "MAS", "nan", "PRO", "nan", "DEV"};
	protected static String[] REQUEST_LEVEL_SHORT = {"NOREQ", "NEWRQ", "UPDTE", "UPGRD", "DNGRD"};

	private final int RESTRICT_NONE  			= 0;
	private final int RESTRICT_SYSTEM_ONLY	 	= 1;
	private final int RESTRICT_DATE_ONLY	    = 2;
	private final int RESTRICT_ALL				= 3;

	private final int REQUEST_NONE  			= 0;
	private final int REQUEST_NEW	 			= 1;
	private final int REQUEST_UPDATE    		= 2;
	private final int REQUEST_UPGRADE			= 3;
	private final int REQUEST_DOWNGRADE			= 4;

	private final int EXPIRING_HARD				= 0;
	private final int EXPIRING_SOFT				= 1;
	private final int EXPIRING_READONLY			= 2;

	/**
	 * Add here individual license serials that need to be invalidated
	 */
	private static String[] INVALID_LICENSES = {};

	private String 	machineID;
	private String 	issued;
	private String 	name;
	private String 	serialNumber;
	private String 	email;
	private int 	year = 0;
	private int 	month = 0;
	private int 	day = 0;
	private int 	version	= Versioner.REGISTRATION_VERSION_NUMBER;
	private int 	escape	= RESTRICT_ALL;
	private int 	level	= 0;
	private int 	request	= 0;
	private int 	expiring= EXPIRING_HARD;

	private String 	flag_A = FLAG_FILL;
	private String 	flag_B = FLAG_FILL;
	private String 	flag_C = FLAG_FILL;

	private String filePath = null;

	private String biosSN = null;

	private String errorMessage = "";

	/**
	 * Creates a registrationfile request
	 * @param _serial
	 */
	public Registration(){
	}

	/**
	 * Loads a registartionfile and decrypts it
	 * @param path
	 * @throws IOException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public void read(String path, Key _key) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		filePath = path;
		File f = new File(path);
		FileInputStream fis;
		fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[(int) f.length()];
		dis.readFully(keyBytes);
		dis.close();
		parseStream(decrypt(keyBytes, _key));
	}

	/**
	 * Saves a registration. checks first if all the data is valid
	 * @param path
	 * @param _key
	 * @throws Exception
	 */
	public void saveLicense(String path, Key _key) throws Exception{
		Calendar md = Calendar.getInstance();
		issued = String.format("%02d", md.get(Calendar.DAY_OF_MONTH)) + String.format("%02d", (md.get(Calendar.MONTH) + 1)) + String.format("%02d", md.get(Calendar.YEAR));


		if(machineID == null)
			throw new Exception("Cannot save registration: Machine ID is not set");
		if(name == null)
			throw new Exception("Cannot save registration: Name is not set");
		if(email == null)
			throw new Exception("Cannot save registration: email is not set");
		if(serialNumber == null)
			throw new Exception("Cannot save registration: serial number is not set");
		if(year == 0)
			throw new Exception("Cannot save registration: expiry year is not set");
		if(level == 0)
			throw new Exception("Cannot save registration: level = 0 doesnt make sense");

		// by default we only create current registrations...
		version	= Versioner.REGISTRATION_VERSION_NUMBER;

		byte[] enc = encrypt(getStream(), _key);

		BufferedOutputStream oout;
		oout = new BufferedOutputStream(new FileOutputStream(path));
		oout.write(enc);
		oout.close();
	}

	/**
	 * Saves a registration. checks first if all the data is valid
	 * @param path
	 * @param _key
	 * @throws Exception
	 */
	public void saveRequest(String path, Key _key) throws Exception{
		if(machineID == null)
			throw new Exception("Cannot save request: Machine ID is not set");
		if(name == null)
			throw new Exception("Cannot save request: Name is not set");
		if(email == null)
			throw new Exception("Cannot save request: email is not set");
		if(level == 0)
			throw new Exception("Cannot save request: level = 0 doesnt make sense");

		// by default we only create current licenses...
		version	= Versioner.REGISTRATION_VERSION_NUMBER;

		byte[] enc = encrypt(getStream(), _key);

		BufferedOutputStream oout;
		oout = new BufferedOutputStream(new FileOutputStream(path));
		oout.write(enc);
		oout.close();
	}

	/**
	 * prepares this registration to become a request.
	 *  <br><li> It sets the expiry year and month
	 *	<br><li> resets the name and email
	 */
	public void prepareRequest(int _level){
		Calendar md = Calendar.getInstance();
		if(serialNumber == null || level <= 1){
			// if the current registr is FREE or unregistered, the following settings will be set
			name = "<your name>";
			email = "<your email>";
		}
		version = Versioner.REGISTRATION_VERSION_NUMBER;
		issued = String.format("%02d", md.get(Calendar.DAY_OF_MONTH)) + String.format("%02d", (md.get(Calendar.MONTH) + 1)) + String.format("%02d", md.get(Calendar.YEAR));
		if(level < _level){ // its an upgrade
			request = REQUEST_UPGRADE;
		} else if(level == _level){ // its an update
			request = REQUEST_UPDATE;
		} else if(level > _level){ // its a downgrade
			request = REQUEST_DOWNGRADE;
		} else {
			request = REQUEST_NEW;
		}
		if(isValid()){
			// if it is a valid license then take its expiry date and add 1 year.
			year = year + 1;
		} else {
			// otherwise set an expiry date for in a year at the beginning of the next month
			year = md.get(Calendar.YEAR) + 1;
			month = md.get(Calendar.MONTH) + 1;
			day = 1;
		}
		try{
			setSerialNumber(generateSerial(REQUEST_LEVEL_SHORT[request]));
		} catch (Exception e){;}
		level = _level;
		machineID = getSystemID();
		// set it very conservative
		escape = RESTRICT_ALL;
	}

	public boolean removeLicenseFile(){
		if(filePath != null){
			File f = new File(filePath);
			return f.delete();
		}
		return false;
	}

	public void setMachineID(){
		machineID = getSystemID();
	}

	public void setName(String _name){
		if(_name.length() <= SIZE_NAME)
			name = _name;
		else
			name = _name.substring(0, SIZE_NAME);
	}

	public void setEmail(String _email){
		if(_email.length() <= SIZE_EMAIL)
			email = _email;
		else
			email = _email.substring(0, SIZE_EMAIL);
	}

	public void setSerialNumber(String _number){
		serialNumber = _number;
	}

	public void setExpiringYear(int _year){
		year = _year;
	}

	/**
	 * Number of month start with 0 for january and ends with 11 for december
	 * @param _month
	 */
	public void setExpiringMonth(int _month){
		month = _month;
	}

	public void setExpiringDay(int _day){
		day = _day;
	}

	public void setLevel(int _level){
		level = _level;
	}

	public void setEscape(int _escape){
		escape = _escape;
	}

	public void setRequest(int _request){
		request = _request;
	}

	public void setExpiring(int _expiring){
		expiring = _expiring;
	}

	public boolean isValid(){
		boolean identicalSystem = isIdenticalSystem();
		boolean validDate = isDateValid();

		if(version >= Versioner.REGISTRATION_VERSION_NUMBER && checkAgainstInvalidLicenses()){
			switch(escape){
			case RESTRICT_NONE:
				return true;
			case RESTRICT_SYSTEM_ONLY:
				if(identicalSystem){
					return true;
				} else {
					errorMessage = "Registration and System don't match.";
				}
			case RESTRICT_DATE_ONLY:
				if (validDate){
					return true;
				} else {
					errorMessage = "Date has expired.";
				}
			case RESTRICT_ALL:
				if(identicalSystem && validDate){
					if(validDate){
						return true;
					} else {
						errorMessage = "Date has expired.";
					}
				} else {
					errorMessage = "Registration and System don't match.";
				}
			}
		}
		return false;
	}

	// if the validation failed, an error message is generated that can be requested by this method
	public String getErrorMessage(){
		return this.errorMessage;
	}

	private boolean checkAgainstInvalidLicenses(){
		if(serialNumber != null){
			for(int i = 0; i < INVALID_LICENSES.length; i++){
				if(serialNumber.equals(INVALID_LICENSES[i])){
					expiring = EXPIRING_HARD;
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the license is a otherwise valid license that has been expired
	 * @return
	 */
	public boolean hasExpired(){
		boolean identicalSystem = isIdenticalSystem();
		boolean validDate = isDateValid();

		if(version >= Versioner.REGISTRATION_VERSION_NUMBER){
			switch(escape){
			case RESTRICT_NONE:
				return false;
			case RESTRICT_SYSTEM_ONLY:
					return false;
			case RESTRICT_DATE_ONLY:
				if (!validDate)
					return true;
			case RESTRICT_ALL:
				if(identicalSystem && !validDate)
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the license is softexpired:
	 * 		The conditions the need to be true:
	 * 			- The license date is expired
	 * 			- The Build date is older then the license expiry date
	 * 			- the license has set the flag to soft expiry
	 *
	 * @return
	 */
	public boolean hasSoftExpired(){
		if(!isDateValid() && this.expiring == EXPIRING_SOFT){
			return hasNotYetDateExpired(
					Integer.parseInt(Versioner.BUILD_YEAR),
					Integer.parseInt(Versioner.BUILD_MONTH),
					Integer.parseInt(Versioner.BUILD_DAY));
		}
		return false;
	}

	/**
	 * Checks if the license is readonly expired:
	 * 		The conditions the need to be true:
	 * 			- The license date is expired
	 * 			- the license has set the flag to readonly expiry
	 *
	 * @return
	 */
	public boolean hasReadOnlyExpired(){
		if(!isDateValid() && this.expiring == EXPIRING_READONLY){
			return true;
		}
		return false;
	}

	/**
	 * returns true if this license can soft-expire
	 * @return
	 */
	public boolean canSoftExpire(){
		return (this.expiring == EXPIRING_SOFT)? true: false;
	}

	/**
	 * returns true if this license can readonly-expire
	 * @return
	 */
	public boolean canReadOnlyExpire(){
		return (this.expiring == EXPIRING_READONLY)? true: false;
	}

	public boolean isAboutToExpire(){
		Calendar cal = new GregorianCalendar();
		cal.roll(Calendar.MONTH, true);
		return !hasNotYetDateExpired(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),  cal.get(Calendar.DAY_OF_MONTH));
	}

	private boolean isDateValid(){
		Calendar cal = new GregorianCalendar();
		return hasNotYetDateExpired(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),  cal.get(Calendar.DAY_OF_MONTH));
	}

	private boolean hasNotYetDateExpired(int _Year, int _Month, int _Day){
		if(year > _Year)
			return true;
		else if(year == _Year && month > _Month)
			return true;
		else if(year == _Year && month == _Month  && day >= _Day)
			return true;
		return false;
	}

	public boolean isIdenticalSystem(){
		return  (machineID != null && getSystemID().indexOf(machineID) == 0);
	}

	/**
	 * Gets the System ID
	 * @return
	 */
	public String getSystemID() {
		StringBuffer systemID = new StringBuffer();

		systemID.append("COR:"+this.getProcessors() + "|");

		if(getOSName().equals("Mac OS X")){
			systemID.append("SNR:"+this.getMacBIOSSerialNumber() + "|");
			systemID.append("DSK:"+this.getOSXDiskUUID());

		}
		else if(getOSName().indexOf("Windows") != -1){
			systemID.append("BIO:"+this.getWinBIOSSerialNumber() + "|");
			systemID.append("DSK:"+this.getWinDiskUUID());
		}

		if(systemID.length() > SIZE_ID)
			systemID.substring(0, SIZE_ID);

		//System.out.println("systemID " + systemID.toString().trim());

		return systemID.toString().trim();
	}

	public String getMachineInfos() {
		String sum = this.getOSName() + "|" +
				this.getOSVersion() + "|" +
				this.getOSArch() + "|" +
				this.getProcessors() + "|" +
				this.getJavaVersion();
		return sum;
	}

	/**
	 * The result of this method should not be shared with the public -> machineID!!
	 */
	public String toString() {
		StringBuffer myContent = new StringBuffer("Registration: ");
		myContent.append("\n version  : " + version);
		myContent.append("\n escape   : " + escape);
		myContent.append("\n level    : " + level);
		myContent.append("\n year 	  : " + year);
		myContent.append("\n month    : " + month);
		myContent.append("\n day      : " + day);
		myContent.append("\n name     : " + name);
		myContent.append("\n email    : " + email);
		myContent.append("\n number   : " + serialNumber);
		myContent.append("\n machineID: " + machineID);
		myContent.append("\n issued   : " + issued);
		myContent.append("\n expiring : " + expiring);
		return myContent.toString();
	}

	public String forPublic() {
		StringBuffer myContent = new StringBuffer("Registration: ");
		myContent.append("\n v" + String.format("%02d", version));
		if(escape == RESTRICT_ALL)
			myContent.append("\n | type  : system+date");
		if(escape == RESTRICT_SYSTEM_ONLY)
			myContent.append("\n | type  : system");
		if(escape == RESTRICT_DATE_ONLY)
			myContent.append("\n | type  : date");
		if(escape == RESTRICT_DATE_ONLY ||
				escape == RESTRICT_ALL){
			myContent.append("\n | expiry date : " + (day + 1) + ". ");
			if(month == 0)
				myContent.append("january");
			if(month == 1)
				myContent.append("february");
			if(month == 2)
				myContent.append("march");
			if(month == 3)
				myContent.append("april");
			if(month == 4)
				myContent.append("may");
			if(month == 5)
				myContent.append("june");
			if(month == 6)
				myContent.append("july");
			if(month == 7)
				myContent.append("august");
			if(month == 8)
				myContent.append("september");
			if(month == 9)
				myContent.append("october");
			if(month == 10)
				myContent.append("november");
			if(month == 11)
				myContent.append("december");
			myContent.append(" " + year);
		}
		myContent.append("\n | level  : " + REGISTRATION_LEVEL[level]);
		myContent.append("\n | name  : " + name);
		myContent.append("\n | email  : " + email);
		myContent.append("\n | serialNumber  : " + serialNumber);
		return myContent.toString();
	}

	public Atom[] getIDMsg(){
		return Atom.newAtom("id", new Atom[]{Atom.newAtom(machineID)});
	}

	public Atom[] getYearMsg(){
		return Atom.newAtom("year", new Atom[]{Atom.newAtom(year)});
	}

	public Atom[] getMonthMsg(){
		return Atom.newAtom("month", new Atom[]{Atom.newAtom(month)});
	}

	public Atom[] getDayMsg(){
		return Atom.newAtom("day", new Atom[]{Atom.newAtom(day)});
	}

	public Atom[] getIssuedMsg(){
		return Atom.newAtom("issued", new Atom[]{Atom.newAtom(issued)});
	}

	public Atom[] getLevelMsg(){
		return Atom.newAtom("level", new Atom[]{Atom.newAtom(level)});
	}

	public Atom[] getLevelTextMsg(){
		return Atom.newAtom("level", new Atom[]{Atom.newAtom(REGISTRATION_LEVEL[level])});
	}

	public int getlevel(){
		return level;
	}

	public Atom[] getVersionMsg(){
		return Atom.newAtom("version", new Atom[]{Atom.newAtom(version)});
	}

	public Atom[] getExpiringMsg(){
		return Atom.newAtom("expiring", new Atom[]{Atom.newAtom(expiring)});
	}

	public Atom[] getEscapeMsg(){
		return Atom.newAtom("escape", new Atom[]{Atom.newAtom(escape)});
	}

	public Atom[] getRequestMsg(){
		return Atom.newAtom("request", new Atom[]{Atom.newAtom(request)});
	}

	/**
	 * get max list
	 * @return ["name" 'license name']
	 */
	public Atom[] getNameMsg(){
		return Atom.newAtom("name", new Atom[]{Atom.newAtom(name)});
	}

	public String getName(){
		return name;
	}

	/**
	 * get max list
	 * @return ["email" 'license email']
	 */
	public Atom[] getEmailMsg(){
		return Atom.newAtom("email", new Atom[]{Atom.newAtom(email)});
	}

	public String getEmail(){
		return email;
	}

	public Atom[] getSerialNumberMsg(){
		return Atom.newAtom("serial", new Atom[]{Atom.newAtom(serialNumber)});
	}

	public String getSerialNumber(){
		return serialNumber;
	}

	public String getExpiryDate(){
		return year + "-" + month + "-" + day;
	}

	/**
	 * gets the full text of license level
	 * @return
	 */
	public String getLicenseLevel(){
		return REGISTRATION_LEVEL[level];
	}

	public String generateSerial() throws Exception{
		return generateSerial(REGISTRATION_LEVEL_SHORT[level]);
	}

	/**
	 * generates the serial with the provided String
	 * @param _licenseShort (5 char long)
	 * @return
	 */
	private String generateSerial(String _licenseShort) throws Exception{
		if(year < Integer.parseInt(Versioner.BUILD_YEAR)){
			throw new Exception("Error Generating Registration: Year is invalid:" + year);
		}
		String pre = String.format("%02d", year).substring(2) + String.format("%02d", (month + 1)) + String.format("%02d", day);
		UUID uid = new UUID((long)(Math.random()*1000000000.F), (long)(Math.random()*1000000000.F));
		return pre + _licenseShort.substring(0, 3) + "-"+ uid.toString().substring(8, 18) + uid.toString().substring(27, 34);
	}

	private byte[] encrypt(byte[] buffer, Key _key) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		Cipher rsa;
		rsa = Cipher.getInstance("RSA");
		rsa.init(Cipher.ENCRYPT_MODE, _key);
		return rsa.doFinal(buffer);
	}

	private byte[] decrypt(byte[] buffer, Key _key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher rsa;
		rsa = Cipher.getInstance("RSA");
		rsa.init(Cipher.DECRYPT_MODE, _key);
		return rsa.doFinal(buffer);
	}

	private void parseStream(byte[] _stream){
		if(_stream.length >= SIZE){
			String stream = new String(_stream);
			version = Integer.parseInt(stream.substring(INDEX_VERSION, INDEX_VERSION + SIZE_VERSION).trim());
			escape = Integer.parseInt(stream.substring(INDEX_ESCAPE, INDEX_ESCAPE + SIZE_ESCAPE).trim());
			request = Integer.parseInt(stream.substring(INDEX_REQUEST, INDEX_REQUEST + SIZE_REQUEST).trim());
			level = Integer.parseInt(stream.substring(INDEX_LEVEL, INDEX_LEVEL + SIZE_LEVEL).trim());
			year = Integer.parseInt(stream.substring(INDEX_YEAR, INDEX_YEAR + SIZE_YEAR).trim());
			month = Integer.parseInt(stream.substring(INDEX_MONTH, INDEX_MONTH + SIZE_MONTH).trim());
			day = Integer.parseInt(stream.substring(INDEX_DAY, INDEX_DAY + SIZE_DAY).trim());
			name = stream.substring(INDEX_NAME, INDEX_NAME + SIZE_NAME).trim();
			email = stream.substring(INDEX_EMAIL, INDEX_EMAIL + SIZE_EMAIL).trim();
			serialNumber = stream.substring(INDEX_NUMBER, INDEX_NUMBER + SIZE_NUMBER).trim();
			machineID = stream.substring(INDEX_ID, INDEX_ID + SIZE_ID).trim();
			issued = stream.substring(INDEX_ISSUED, INDEX_ISSUED + SIZE_ISSUED).trim();
			expiring = Integer.parseInt(stream.substring(INDEX_EXPIRING, INDEX_EXPIRING + SIZE_EXPIRING).trim());
			flag_A = stream.substring(INDEX_FLAG_A, INDEX_FLAG_A + SIZE_FLAG_A).trim();
			flag_B = stream.substring(INDEX_FLAG_B, INDEX_FLAG_B + SIZE_FLAG_B).trim();
			flag_C = stream.substring(INDEX_FLAG_C, INDEX_FLAG_C + SIZE_FLAG_C).trim();
		} else {
			machineID = "invalid";
			serialNumber = "invalid";
			name = "invalid";
			email = "invalid";
			issued = "01010000";
			escape = RESTRICT_ALL;
			request = REQUEST_NONE;
			version = 0;
			year = 2000;
			month = 0;
			day = 0;
			level = 0;
			expiring = EXPIRING_HARD;
			flag_A = FLAG_FILL;
			flag_B = FLAG_FILL;
			flag_C = FLAG_FILL;
		}
	}

	private byte[] getStream(){
		char[] seq = new char[SIZE];
		for(int i = 0; i < seq.length; i++)
			seq[i] = ' ';
		StringBuffer stream = new StringBuffer(new String(seq));
		stream.replace(INDEX_VERSION, INDEX_VERSION + SIZE_VERSION, String.format("%02d", version));
		stream.replace(INDEX_ESCAPE, INDEX_ESCAPE + SIZE_ESCAPE, String.format("%02d",escape));
		stream.replace(INDEX_REQUEST, INDEX_REQUEST + SIZE_REQUEST, String.format("%02d",request));
		stream.replace(INDEX_LEVEL, INDEX_LEVEL + SIZE_LEVEL, String.format("%02d",level));
		stream.replace(INDEX_YEAR, INDEX_YEAR + SIZE_YEAR, String.format("%04d",year));
		stream.replace(INDEX_MONTH, INDEX_MONTH + SIZE_MONTH,String.format("%02d", month));
		stream.replace(INDEX_DAY, INDEX_DAY + SIZE_DAY, String.format("%02d", day));
		stream.insert(INDEX_NAME, name);
		stream.insert(INDEX_EMAIL, email);
		stream.insert(INDEX_NUMBER, serialNumber);
		stream.insert(INDEX_ID, machineID);
		stream.insert(INDEX_ISSUED, issued);
		stream.insert(INDEX_EXPIRING, expiring);
		stream.insert(INDEX_FLAG_A, flag_A);
		stream.insert(INDEX_FLAG_B, flag_B);
		stream.insert(INDEX_FLAG_C, flag_C);

		return stream.substring(0, SIZE).toString().getBytes();
	}

	private String getMacAddress(){
		// get Mac-Address
		try {
			InetAddress ip = InetAddress.getLocalHost();

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i],
						(i < mac.length - 1) ? "-" : ""));
			}

			return sb.toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "unknown";
	}

	private String getMacBIOSSerialNumber() {
		String result = "unkown";
        ProcessBuilder pb = new ProcessBuilder("bash", "-c",
            "ioreg -l | awk '/IOPlatformSerialNumber/ { print $4;}'");
        pb.redirectErrorStream(true);
        try {
            Process p = pb.start();
            String s;
            // read from the process's combined stdout & stderr
            BufferedReader stdout = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            while ((s = stdout.readLine()) != null) {
                result = s.substring(1, s.length()-1);
            }
            //System.out.println("Exit value: " + p.waitFor());
            p.getInputStream().close();
            p.getOutputStream().close();
            p.getErrorStream().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

	private String getWinBIOSSerialNumber() {

		if (biosSN != null) {
			return biosSN;
		}

		OutputStream os = null;
		InputStream is = null;

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec(new String[] { "wmic", "bios", "get", "serialnumber" });
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		os = process.getOutputStream();
		is = process.getInputStream();

		try {
			os.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Scanner sc = new Scanner(is);
		try {
			while (sc.hasNext()) {
				String next = sc.next();
				if ("SerialNumber".equals(next)) {
					if(sc.hasNext()){
						biosSN = sc.next().trim();
					}
					break;
				}
			}
		} finally {
			try {
				is.close();
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		if (biosSN == null) {
			biosSN = "Cannot find computer SN";
		}

		return biosSN;
	}

	private String getOSXDiskUUID() {
        String result = "unkown";
		try {
			Process p = Runtime.getRuntime().exec("diskutil info /dev/disk0s2");
            BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
            	if(line.contains("UUID:")){
            		int indx = line.indexOf("UUID");
                    String _result = line.substring(indx + 5, line.length());
                    result = _result.toString().trim();
            	}
            }
            input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
    }

    private String getWinDiskUUID() {
        StringBuffer result = new StringBuffer();;
		try {
 	        Process p = Runtime.getRuntime().exec("wmic bios get name");
            BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
            String line;
            String r1 = null;
            while ((line = input.readLine()) != null) {
            	if(line.length() > 2 && !line.contains("Name")){
                    r1 = line;
            	}
            }
            input.close();

 	        p = Runtime.getRuntime().exec("wmic bios get version");
            input = new BufferedReader (new InputStreamReader(p.getInputStream()));
            String r2 = null;
            while ((line = input.readLine()) != null) {
            	if(line.length() > 2 && !line.contains("Name")){
                    r2 = line;
            	}
            }
            input.close();
     		result.append( r1 + "-|-" + r2);
 		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
    }

	private String getStorageSpace(){
	    /* Get a list of all filesystem roots on this system */
	    File[] roots = File.listRoots();

	    /* For each filesystem root, print some info */
	    long sum = 0;
	    for (File root : roots) {
	        sum = sum + root.getTotalSpace();
	    }
	    return "" + sum;
	}


	private String getOSName(){
		return System.getProperty("os.name");
	}

	private String getOSArch(){
		return System.getProperty("os.arch");
	}

	private String getOSVersion(){
		return System.getProperty("os.version");
	}

	private String getJavaVersion(){
		return System.getProperty("java.version");
	}

	private String getProcessors(){
		return "" + Runtime.getRuntime().availableProcessors();
	}

}
