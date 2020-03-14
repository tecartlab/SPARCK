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
import java.net.URL;
import java.security.*;
import java.security.spec.*;

/**
 * Public Key Manager.
 *
 * It loads the public key from the jar file and makes it available for use.
 *
 * @author maybites
 *
 */
public class Vectorizer {

	public Key vectorizer;

	private byte[] seq = {48, -126, 1, 34, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126,
			1, 15, 0, 48, -126, 1, 10, 2, -126, 1, 1, 0, -120, -12, 75, 14, -45, 36, -11, -48, 2, 60, 127, 127,
			97, -87, 95, -26, -111, 74, 12, 34, -71, -42, 32, -109, 118, 57, -73, -1, -5, 10, -36, 74, 27, -71,
			57, 112, 113, -6, 13, 74, -56, -102, -80, 124, -64, -62, -63, 87, -75, -57, -6, -113, 41, -66, 112,
			19, 64, 47, -24, -107, -23, -84, 86, -1, 49, -29, 82, 4, 91, -4, 6, -96, 49, 105, -102, 102, 10, 95,
			1, -59, 107, -35, 92, 39, 5, 64, 71, -27, -123, 15, 9, 91, 33, -67, 49, -117, 21, 57, 20, 41, 120, 50,
			44, 67, 59, -61, -29, -74, 78, 80, 119, 77, -19, 10, 46, 71, -116, -57, -69, 59, 9, -112, 121, -16, -75,
			-42, -58, 78, -35, -38, 96, 90, -120, -114, 2, -49, -24, 83, 100, -36, 33, -23, 65, 16, 85, -85, -70, 127,
			-96, 38, -111, -29, 8, -37, -24, -74, -61, -29, -98, -55, -20, -114, -55, 115, 87, 9, 119, 110, 68, 108,
			85, 14, 64, 20, -52, 125, 36, -82, -34, 87, 102, -99, -113, 127, 121, 111, -78, -56, -23, -102, -70, 63,
			64, -84, -111, 92, 51, 81, 126, 69, 40, -94, -48, 111, -16, -66, 92, -85, 98, -51, 12, -79, 97, 74, -92,
			51, 90, 43, 5, 35, 42, 19, 111, -24, -35, -16, -95, -93, 11, -35, -24, 28, -68, 116, 45, -81, -79, 45,
			-9, -20, -128, 67, 91, 122, 39, -98, -111, -95, 125, 18, -26, -94, -123, -44, 6, -99, 2, 3, 1, 0, 1};


	public Vectorizer() throws Exception{
		String exception = null;
		try{
			X509EncodedKeySpec spec = new X509EncodedKeySpec(seq);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			// return Public key
			vectorizer = kf.generatePublic(spec);
		} catch (NoSuchAlgorithmException e) {
			exception = e.toString();
		} catch (InvalidKeySpecException e) {
			exception = e.toString();
		}
		if(exception != null)
			throw new Exception(exception);
	}

	public Vectorizer(String keyPath) throws Exception{
		URL locator = this.getClass().getResource(keyPath);
		String exception = null;

		if (locator != null) {
			try {
				InputStream stream = locator.openStream();
				try{
					DataInputStream dis = new DataInputStream(stream);
					byte[] keyBytes = new byte[0];
					byte[] buf = new byte[8192];
					int nread;
					while ((nread = dis.read(buf)) >= 0) {
						byte[] store = keyBytes;
						keyBytes = new byte[keyBytes.length + nread];
						for (int i = 0; i < store.length; i++) {
							keyBytes[i] = store[i];
						}
						for (int i = store.length; i < keyBytes.length; i++) {
							keyBytes[i] = buf[i - store.length];
						}
					}
					dis.close();
					X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
					KeyFactory kf = KeyFactory.getInstance("RSA");
					// return Public key
					vectorizer = kf.generatePublic(spec);
				} catch (NoSuchAlgorithmException e) {
					exception = e.toString();
				} catch (InvalidKeySpecException e) {
					exception = e.toString();
				} catch (IOException e) {
					exception = e.toString();
				}

				stream.close();
			} catch (IOException e) {
				exception = e.toString();
			}
		} else {
			exception = "Library is corrupted";
		}
		if(exception != null)
			throw new Exception(exception);
	}

}
