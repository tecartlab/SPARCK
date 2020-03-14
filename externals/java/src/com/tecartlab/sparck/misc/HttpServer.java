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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class HttpServer {

	public HttpServer(){
	}

	// sends data to server for startup log
	public static void sendStartupLog(
			String _machineID,
			String _license,
			String _serial,
			int _pref_startupCounter,
			int _pref_shutdownCounter,
			int _pref_totalRunningTime) throws Exception {

		String url = "http://sparck.tecartlab.com/logs/startup/logger.php";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String serialNumber = (_serial == null)? "UNREGISTERED-COPY----------": _serial;

		//String urlParameters  = "channel0Title=title&channel0Gain=license&channel0Offset=serial";
		String urlParameters  = "machineID="+_machineID+
				"&license="+_license+
				"&serial="+serialNumber+
				"&build="+Versioner.BUILD_NUMBER+
				"&version="+Versioner.VERSION+
				"&release="+Versioner.RELEASE+
				"&startupCounter="+_pref_startupCounter+
				"&crashCounter="+(_pref_startupCounter - _pref_shutdownCounter)+
				"&totalRunningTime="+_pref_totalRunningTime;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		con.getResponseCode();
	}

	// sends data to server for email confirmation
	public static void sendEmailVerification(
			String _Name,
			String _Email,
			String _Code,
			String _LicenseType,
			String _machineID) throws IOException {

		String url = "http://sparck.tecartlab.com/logs/verify/email.php";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		//String urlParameters  = "channel0Title=title&channel0Gain=license&channel0Offset=serial";
		String urlParameters  = "machineID="+_machineID+
				"&version="+Versioner.VERSION+
				"&name="+_Name+
				"&email="+_Email+
				"&licensetype="+_LicenseType+
				"&code="+_Code;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		con.getResponseCode();
	}

	public static void sendOrderConfirmation(
			String _mail,
			String _name,
			String _license,
			String _price,
			String _serial,
			String _coupon) throws IOException {

		String url = "http://sparck.tecartlab.com/logs/order/confirmation.php";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String coupontext = (_coupon.length() > 0)?"Promo Code: " +_coupon:"";

		Calendar cal = new GregorianCalendar();
		String date = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " " + cal.get(Calendar.DAY_OF_MONTH) + " " +  cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()	) + "  " + cal.get(Calendar.YEAR);

		//String urlParameters  = "channel0Title=title&channel0Gain=license&channel0Offset=serial";
		String urlParameters  =
				"&name="+_name+
				"&email="+_mail+
				"&license="+_license+
				"&serial="+_serial+
				"&coupon="+coupontext+
				"&price="+_price+
				"&date="+date;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		con.getResponseCode();
	}

}
