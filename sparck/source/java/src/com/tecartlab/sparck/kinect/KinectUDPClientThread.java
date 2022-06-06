package com.tecartlab.sparck.kinect;
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
import java.util.*;

public class KinectUDPClientThread extends Thread {

    protected DatagramSocket socket = null;
    protected boolean isopen = false;
    protected KinectClientListener listener = null;

    public KinectUDPClientThread(KinectClientListener _listener, int _port) throws IOException {
    	super("KinectClientThread");
        socket = new DatagramSocket(_port);
        listener = _listener;
    }

    public void close(){
    	isopen = false;
    	if(this.isInterrupted())
    		socket.close();
    }

    public void run() {
    	isopen = true;
    	int linecounter = 0;
        while (isopen) {
            try {
                byte[] buf = new byte[640];
                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                if(packet.getData()[0] == '@' && packet.getData()[1] == '%'){
                    listener.udpevent("previsously received " + linecounter + " lines.");
                    listener.udpevent("received header with " + packet.getLength() + " bytes.");
                    linecounter = 0;
                }else{
                	linecounter++;
                }
                //int port = packet.getPort();
                //packet.getLength();

                //String data = ">" + packet.getData()[320];
                //listener.udpevent("received: " + data);

            } catch (IOException e) {
                //listener.udpevent("Socket: " + e.getMessage());
                isopen = false;
            }
        }
        socket.close();
    }

    public void delete(){
    	this.interrupt();
    	close();
    }

 }
