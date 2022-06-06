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
import java.nio.*;
import java.net.*;
import java.util.*;

import com.cycling74.jitter.JitterMatrix;
import com.cycling74.max.Callback;

import com.tecartlab.utils.Debug;

public class KinectMatrixClient extends Thread {

	private static String JIT_MATRIX_PACKET_ID 			="JMTX";
	private static String JIT_MATRIX_LATENCY_PACKET_ID  ="JMLP";
	private static String JIT_MESSAGE_PACKET_ID  		="JMMP";
	private static String JIT_MESSAGE_HANDSHAKE_ID  	="JMHS";
	private static String JIT_MESSAGE_EXIT_ID  			="JMEX";
	private static String JIT_MESSAGE_DISCONNECT_ID  	="JMDC";

	private final int JIT_MATRIX_MAX_DIMCOUNT			= 32;//< maximum dimension count
	private final int JIT_MATRIX_MAX_PLANECOUNT			= 32;//< maximum plane count

	private final int JIT_MATRIX_TYPE_CHAR 		=0;
	private final int JIT_MATRIX_TYPE_SHORT 	=1;
	private final int JIT_MATRIX_TYPE_LONG 		=2;
	private final int JIT_MATRIX_TYPE_FLOAT32 	=3;
	private final int JIT_MATRIX_TYPE_FLOAT64 	=4;

	private final int MSG_UNKNOWN	 	= 0;
	private final int MSG_HANDSHAKE 	= 1;
	private final int MSG_EXIT 			= 2;
	private final int MSG_FRAME 		= 3;
	private final int MSG_DISCONNECT 	= 4;

	public String host = null;
	public int port = 0;
	public String clientid = "MatrixClientThread";
    protected Socket socket = null;
    protected boolean isInitialized = false;
    protected boolean isopen = false;
    protected boolean nextFrame = false;
    protected boolean serverSideExit = false;
    protected Callback listener = null;

    protected BufferedReader bufferedReader;
    protected BufferedInputStream inStream;

    private Debug debug;

	int size;
	int planecount;
	int type;
	int dimcount;
	int[] dim = new int[JIT_MATRIX_MAX_DIMCOUNT];
	int[] dimstride = new int[JIT_MATRIX_MAX_DIMCOUNT];
	int datasize;
	long time;

	JitterMatrix myMatrix;

    public KinectMatrixClient(String _clientid, Callback _listener, String _host, int _port){
    	super("MatrixClientThread");
    	clientid = _clientid;
        listener = _listener;
        host = _host;
        port = _port;

        debug = Debug.getInstance();

        myMatrix = new JitterMatrix();
    }

    private boolean connect(){
    	if(socket == null){
    		debug.infoMessage(clientid, "attempting connection to host:" + host + " at port:"+port + " ...");
    		isInitialized = false;
    		try {
    			//socket = new Socket(host, port, InetAddress.getByName("127.0.0.1"), localport);
    			socket = new Socket(host, port); //, InetAddress.getByName("127.0.0.1"), localport);
    	        socket.setReceiveBufferSize(1024 * 1024);
    	        inStream = new BufferedInputStream(socket.getInputStream(), 640 * 6);
    			//socket.setReuseAddress(true);
    	    	//listener.tcpevent("receiveBufferSize = " + socket.getReceiveBufferSize());
    	        debug.infoMessage(clientid, "... connection established");
    		} catch (IOException e) {
    			debug.errorMessage(clientid, "error connecting: " + e.getMessage());
    		}
    	}
    	return (socket != null)?socket.isConnected(): false;
    }

    private boolean disconnect(){
    	if(socket != null){
    		debug.infoMessage(clientid, "attempting to disconnect from host:" + host + " at port:"+port + " ...");
    		try {
       			//listener.tcpevent("disconnecting...");
    			isInitialized = false;
    			if(!socket.isClosed()){

    	   			socket.shutdownInput();
    	   			socket.shutdownOutput();
    	   			//socket.setSoLinger(true, 50);

    	   			inStream.close();
    				socket.close();
    	   		}
    			socket = null;
    			debug.infoMessage(clientid, "...disconnection succesfull.");
    			return true;
    		} catch (IOException e) {
    			debug.errorMessage(clientid, "error disconnecting: " + e.getMessage());
    		}
    	}
    	return false;
    }

    public void close(){
    	isopen = false;
    }

    public void nextframe(){
    	nextFrame = true;
    }

    public void run() {
		isopen = connect();
		int countFrames = 0;

		while (isopen) {
            if(isopen && !isInitialized){
            	debug.infoMessage(clientid, "requesting handshake...");
            	if(request("handshake[/TCP]") == MSG_HANDSHAKE){
            		debug.infoMessage(clientid, "...handshake accepted. connection initialized.");
       				isInitialized = true;
            	}
        	} else if(isopen) {
        		if(nextFrame){
    	    		//listener.tcpevent("request framenumber: " + countFrames);
        			if(request("nextframe-" + countFrames + "[/TCP]") ==MSG_FRAME)
	        			countFrames++;
        	    		//listener.tcpevent("Frame received");

        			nextFrame = false;
        		}
       		}
		}

		if(!serverSideExit){
			debug.infoMessage(clientid, "requesting disconnection...");
			if(request("disconnect[/TCP]") == MSG_DISCONNECT)
				debug.infoMessage(clientid, "...disconnection accepted.");

			try {
				this.sleep(100);
			} catch (InterruptedException e) {;}
		}

		disconnect();
     }


    private int request(String message){
    	try{
        	sendMessage(message);
        	byte[] chunk = receiveChunkMessage();
    		switch(evaluateChunkMsg(chunk)){
       		case MSG_HANDSHAKE:
    			return MSG_HANDSHAKE;
       		case MSG_DISCONNECT:
    			return MSG_DISCONNECT;
    		case MSG_EXIT:
    			debug.infoMessage(clientid, "...received server exit. connection closing...");
    			serverSideExit = true;
    			close();
    			return MSG_EXIT;
    		case MSG_FRAME:
    			int buffersize = (ByteBuffer.wrap(chunk,4,4)).getInt();
    			//listener.tcpevent("...receiving frameheader. expect " + buffersize);
    			byte[] header = receiveHeaderMessage(buffersize);
    			//listener.tcpevent("got frameheader. with " + header.length);
    			evaluateHeaderMsg(header);
    			if(datasize > 0)
    	    		switch(type){
    	    		case JIT_MATRIX_TYPE_CHAR:
        				if(readIntFrameMessage()){
        					listener.setArgs(myMatrix.getName());
        					listener.execute();
        				}
    		    		break;
    	    		case JIT_MATRIX_TYPE_SHORT:
    	    			//listener.tcpevent("got short frame");
        				if(readRawDepthFrameMessage()){
        					listener.setArgs(myMatrix.getName());
        					listener.execute();
        				}
    		    		break;
    	    		case JIT_MATRIX_TYPE_LONG:
    		    		break;
    	    		case JIT_MATRIX_TYPE_FLOAT32:
    	    			//listener.tcpevent("got float frame");
        				if(readFloatFrameMessage()){
        					listener.setArgs(myMatrix.getName());
        					listener.execute();
        				}
    		    		break;
    	    		}
    			return MSG_FRAME;
    		}
    	} catch (IOException e) {
    		debug.errorMessage(clientid, e.getMessage() + " -> disconnect connection");
			close();
		}
		return MSG_UNKNOWN;

    }

    private byte[] receiveChunkMessage() throws IOException{
    	return receiveHeaderMessage(8);
    }

    private int evaluateChunkMsg(byte[] msg){
 		String nachricht = new String(msg,0,4);
 		//listener.tcpevent("evalChunkMsg: " +nachricht);
 		if(nachricht.equals(JIT_MESSAGE_HANDSHAKE_ID))
 			return MSG_HANDSHAKE;
 		if(nachricht.equals(JIT_MESSAGE_EXIT_ID))
 			return MSG_EXIT;
 		if(nachricht.equals(JIT_MESSAGE_DISCONNECT_ID))
 			return MSG_DISCONNECT;
 		if(nachricht.equals(JIT_MESSAGE_PACKET_ID))
 			return MSG_FRAME;

 		return MSG_UNKNOWN;
    }

    private byte[] receiveHeaderMessage(int _size) throws IOException{
    	byte[] buffer = new byte[_size];
		//listener.tcpevent("receiveChunkMessage....");
    	int anzahlZeichen = inStream.read(buffer, 0, _size); // blockiert bis Nachricht empfangen
    	if(anzahlZeichen == _size)
    		return buffer;
    	throw new IOException("headermessage malformed: expected " + _size + " bytes, received " + anzahlZeichen + " bytes");
    }

    private void evaluateHeaderMsg(byte[] msg){
		String nachricht = new String(msg,0,4);
		//listener.tcpevent("Header: " + nachricht);
		if(nachricht.equals(JIT_MATRIX_PACKET_ID)){
	    	int _offset = 4;
	    	size 		=  (ByteBuffer.wrap(msg,_offset,4)).getInt();
	    	_offset += 4;
	    	planecount 	=  (ByteBuffer.wrap(msg,_offset,4)).getInt();
	    	_offset += 4;
	    	int _type	 	=  (ByteBuffer.wrap(msg,_offset,4)).getInt();
	    	_offset += 4;
	    	dimcount 	=  (ByteBuffer.wrap(msg,_offset,4)).getInt();
	    	_offset += 4;
	    	//int[] _dim = new int[_dimcount];
	    	dim = new int[dimcount];
	    	for(int i = 0; i < JIT_MATRIX_MAX_DIMCOUNT; i++){
	    		if(i < dimcount)
	    			dim[i] 		= (ByteBuffer.wrap(msg,_offset,4)).getInt();
	        	_offset += 4;
	    	}
	    	//int[] _dimstride = new int[_dimcount];
	    	dimstride = new int[dimcount];
	    	for(int i = 0; i < JIT_MATRIX_MAX_DIMCOUNT; i++){
	    		if(i < dimcount)
	    			dimstride[i]= (ByteBuffer.wrap(msg,_offset,4)).getInt();
	        	_offset += 4;
	    	}
	    	int _datasize	= (ByteBuffer.wrap(msg,_offset,4)).getInt();
	    	_offset += 4;

	    	time = (ByteBuffer.wrap(msg,_offset,8)).getLong();

	    	if(_datasize != datasize || _type != type){

	    		datasize = _datasize;
	    		type = _type;

	    		myMatrix.setPlanecount(planecount);
	    		myMatrix.setDim(dim);
	    		switch(type){
	    		case JIT_MATRIX_TYPE_CHAR:
		    		myMatrix.setType("char");
		    		break;
	    		case JIT_MATRIX_TYPE_SHORT:
		    		myMatrix.setType("char");
		    		break;
	    		case JIT_MATRIX_TYPE_LONG:
		    		myMatrix.setType("long");
		    		break;
	    		case JIT_MATRIX_TYPE_FLOAT32:
		    		myMatrix.setType("float32");
		    		break;
	    		case JIT_MATRIX_TYPE_FLOAT64:
		    		myMatrix.setType("float64");
		    		break;
	    		}

	    		debug.infoMessage(clientid, "frameprops: type :" + myMatrix.getType());
	    		debug.infoMessage(clientid, "            planecount: " + planecount);
	    		debug.infoMessage(clientid, "            dimcount :" + dimcount);
	    		String _dims = "";
	    		for(int i = 0; i < dimcount; i++){
	    			_dims += "[" + i + "] = " + dim[i] + " | ";
	    		}
	    		debug.infoMessage(clientid, "            dim[]: " + _dims);
	    		String _dimstrides = "";
	    		for(int i = 0; i < dimcount; i++){
	    			_dimstrides += "[" + i + "] = " + dimstride[i] + " | ";
	    		}
	    		debug.infoMessage(clientid, "            dimstride[]: " + _dimstrides);
	    		debug.infoMessage(clientid, "            datasize : " + datasize);

	    	}
    	}
    }

    private boolean readIntFrameMessage() throws IOException{
       	byte[] buffer = new byte[dimstride[dimcount -1]];
        int[] vector = new int[dimstride[dimcount -1]];
    	int[] offset = new int[2];

    	int index = 0;
    	int height = 0;

    	int ofIn = 0;

    	int waitCounter = 0;
    	try{

        	offset[0] = 0;
    		for (height = 0; height < dim[dimcount - 1]; height++) {
    			index = 0;
    			offset[1] = height;
    			while(index < dimstride[dimcount -1]){
    	    		if((dimstride[dimcount -1] - index) <= inStream.available()){
    					ofIn = inStream.read(buffer, index, dimstride[dimcount -1] - index);
    					for(int i = index; i < (index + ofIn); i++)
    						vector[i] = buffer[i] & 0xff;
    					index += ofIn;
    					myMatrix.copyArrayToVector(0, offset, vector, vector.length,0);
    					waitCounter = 0;
    	    		} else {
    	    			try {
    	    				waitCounter++;
    	    				this.sleep(10);
    	    			} catch (InterruptedException e) {;}
    	    		}
    	   			if(waitCounter > 20){
    	    			//listener.tcpevent("break for frame data at: " + height);
        				break;
    	   			}
        		}
    		}

    	} catch (EOFException e){
    	}
    	return true;
    }

    private boolean readRawDepthFrameMessage() throws IOException{
    	myMatrix.setPlanecount(3);
       	byte[] buffer = new byte[dimstride[dimcount -1]];
        int[] vector = new int[dim[0] * 3];
    	int[] offset = new int[2];

    	int index = 0;
    	int height = 0;

    	int ofIn = 0;

    	int waitCounter = 0;
    	try{

        	offset[0] = 0;
    		for (height = 0; height < dim[dimcount - 1]; height++) {
    			index = 0;
    			offset[1] = height;
    			while(index < dimstride[dimcount -1]){
    	    		if((dimstride[dimcount -1] - index) <= inStream.available()){
    					ofIn = inStream.read(buffer, index, dimstride[dimcount -1] - index);
    					for(int i = index; i < (index + ofIn) - 2; i = i + 2){
    						vector[i / 2 * 3] = buffer[i] & 0xff;
    						vector[i / 2 * 3 + 1] = buffer[i+1] << 5 & 0xff;
    						vector[i / 2 * 3 + 2] = 0;
    					}
    					index += ofIn;
    					myMatrix.copyArrayToVector(0, offset, vector, vector.length,0);
    					waitCounter = 0;
    	    		} else {
    	    			try {
    	    				waitCounter++;
    	    				this.sleep(10);
    	    			} catch (InterruptedException e) {;}
    	    		}
    	   			if(waitCounter > 20){
    	    			//listener.tcpevent("break for frame data at: " + height);
        				break;
    	   			}
        		}
    		}

    	} catch (EOFException e){
    	}
    	return true;
    }

    private boolean readFloatFrameMessage() throws IOException{
       	byte[] buffer = new byte[dimstride[dimcount -1]];
        float[] vector = new float[dimstride[dimcount -1] / 4];
    	int[] offset = new int[2];

    	int index = 0;
    	int height = 0;

    	int ofIn = 0;

    	int waitCounter = 0;
    	try{

        	offset[0] = 0;
    		for (height = 0; height < dim[dimcount - 1]; height++) {
    			index = 0;
    			offset[1] = height;
    			while(index < dimstride[dimcount -1]){
    	    		if((dimstride[dimcount -1] - index) <= inStream.available()){
    					ofIn = inStream.read(buffer, index, dimstride[dimcount -1] - index);
    					for(int i = index; i < (index + ofIn) - 4; i = i + 4)
    						vector[i / 4] = ByteBuffer.wrap(buffer, i, 4).getFloat();
    					index += ofIn;
    					myMatrix.copyArrayToVector(0, offset, vector, vector.length,0);
    					waitCounter = 0;
    	    		} else {
    	    			try {
    	    				waitCounter++;
    	    				this.sleep(10);
    	    			} catch (InterruptedException e) {;}
    	    		}
    	   			if(waitCounter > 20){
    	    			//listener.tcpevent("break for frame data at: " + height);
        				break;
    	   			}
        		}
    		}

    	} catch (EOFException e){
    	}
    	return true;
    }

    private void sendMessage(String nachricht) {
    	nachricht.getBytes();
    	try{
    		socket.getOutputStream().write(nachricht.getBytes());
		} catch (IOException e) {
			debug.errorMessage(clientid, e.getMessage() + " -> closing connection");
			close();
		}
    }

    public void delete(){
    	close();
    }

 }
