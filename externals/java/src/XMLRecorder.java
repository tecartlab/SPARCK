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


import com.cycling74.max.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;


/**
 *
 *	Time string.
 *		There are several ways to write the time:
 *			02d:01h:23:01.001
 *				2 	days
 *				1 	hour
 *				23	minutes
 *				01	seconds
 *				001 milliseconds
 *
 * 			02d:01h:23m:01s
 *				2 	days
 *				1 	hour
 *				23	minutes
 *				01	seconds
 *
 * 			23m:01s
 *				23	minutes
 *				01	seconds
 *
 * 			23:01
 *				23	minutes
 *				01	seconds
 *
 * 			23:01.050
 *				23	minutes
 *				01	seconds
 *				050 milliseconds
 *
 * 			01.050
 *				01	seconds
 *				050 milliseconds

 * @author maybites
 *
 */

/*
<script>
* 		<keyframe time="02d:01h:23:01.001">
* 			<frame time="23:01.001">
* 				<event value="/osc/adress 3 3.5 string" typetag="ifs"/>
*  			<event value="first second third 1 4.5634 56" typetag="sssifs"/>
* 			</frame>
* 		</keyframe>
* 	</script>
*/

public class XMLRecorder extends MaxObject
{

	static final String ELEMENT_SCRIPT = "script";
	static final String ELEMENT_KEYFRAME = "keyframe";
	static final String ELEMENT_FRAME = "frame";
	static final String ELEMENT_EVENT = "event";
	static final String ELEMENT_OSCEVENT = "oscevent";

	static final String ATTR_TIME = "time";
	static final String ATTR_VALUE = "value";
	static final String ATTR_TYPETAG = "typetag";
	static final String ATTR_TYPE = "type";
	static final String ATTR_TAG = "tag";
	static final String ATTR_ADDRESS = "address";

	long pastFrameTime;
	long delayTime = 40;
	long lastFrameTime;
	long startTime;
	long pausedTime;
	long pauseTime;

	Document dasDOMObjekt;
	NodeIterator keyFrames;
	NodeIterator frames;

	String filepath;
	String path;

	MaxClock clock;

	public XMLRecorder(Atom[] args)
	{
		if(args.length == 1){
			delayTime = args[0].toLong();
		}
		pastFrameTime = delayTime * 10;
		path = null;

		clock = new MaxClock(new Callback(this, "time"));

		declareAttribute("path", null, "setpath");
		declareInlets(new int[]{DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.ALL, DataTypes.INT});
		post("XMLRecorder Version 008");
	}

	public void notifyDeleted(){
		clock.release();
	}

	public void setpath(String _path){
		path = _path;
	}

	private String createFilePath(String file){
		if(path != null)
			if(path.endsWith("/"))
				return path + file;
			else
				return path + "/" + file;
		else
			return file;
	}

	public void read(Atom[] file){
		if(file != null && file.length == 1){
			filepath = createFilePath(file[0].toString());
			File dieXMLDatei = new File(filepath);
			DocumentBuilderFactory dasDBFactoryObjekt = DocumentBuilderFactory.newInstance();
			post("Loaded successfull: "+dieXMLDatei.getAbsolutePath());
			try{
				DocumentBuilder dasDBObjekt = dasDBFactoryObjekt.newDocumentBuilder();
				dasDOMObjekt = dasDBObjekt.parse(dieXMLDatei);
			}
			catch (Exception e){
				error("XMLRecorder: DocumentBuilder Exceptions:" + e.getMessage());
				return;
			}
			dasDOMObjekt.getDocumentElement().normalize();
			keyFrames = new NodeIterator(dasDOMObjekt.getElementsByTagName(ELEMENT_KEYFRAME));
			if(!keyFrames.hasCurrent()){
				error("XMLRecorder: no keyframes found in file");
				return;
			}
			fileLoaded();
			clock.unset();
		}
	}

	public void start(Atom[] dasSkript){
		if(dasDOMObjekt != null){
			keyFrames = new NodeIterator(dasDOMObjekt.getElementsByTagName(ELEMENT_KEYFRAME));
			frames = null;
			clock.delay(0);
			pausedTime = 0;
			startTime = (long)clock.getTime();
		}
	}

	public void pause(){
		pauseTime = (long)clock.getTime();
		clock.unset();
	}

	public void play(){
		pausedTime += (long)clock.getTime() - pauseTime;
		clock.delay(0);
	}

	public void time(){
		clock.delay(delayTime);
		long currentTime = getCurrentTime();
		if(keyFrames.hasCurrent()){
			if(frames == null){ // the keyframe has not been triggered yet
				if(checkTrigger(currentTime, keyFrames.getCurrent())){
					frames = new NodeIterator(keyFrames.getCurrent().getChildNodes());
					lastFrameTime = currentTime;
				}
			}
			if(frames != null){
				//iterate through the frames until a correct node is current
				while(frames.hasCurrent() && !frames.getCurrent().getNodeName().equals(ELEMENT_FRAME)){
					frames.iterate();
				}
				if(frames.hasCurrent()){
					long triggerTime = getTriggerTime(frames.getCurrent());
					if((currentTime - lastFrameTime) >= triggerTime){
						// send the events out
						processEvents(new NodeIterator(frames.getCurrent().getChildNodes()));
						frames.iterate(); // and get the next frame
						lastFrameTime = lastFrameTime + triggerTime;
					}
				} else { // if there are no more frames
					frames = null;
					keyFrames.iterate(); // make the next keyframe current
				}
			}
		} else { // there are no more keyframes: the script is over
			scriptDone();
		}
		if(keyFrames.hasNext()){ //check if maybe the next keyframe should kick in
			if(checkTrigger(currentTime, keyFrames.getNext())){
				// if this is the case
				frames = null;
				keyFrames.iterate(); // make the next keyframe current
			}
		}
		printTime(currentTime);
	}

	private void processEvents(NodeIterator events){
		while(events.hasCurrent()){
			Node event = events.getCurrent();
			if(event.getNodeName().equals(ELEMENT_EVENT)){
				processEvent(event);
			}
			events.iterate();
		}
	}

	private void processEvent(Node event){
		char[] typetag = null;
		String tag = null;
		if(event.hasAttributes()){
			Node typetagnode = event.getAttributes().getNamedItem(ATTR_TYPETAG);
			if(typetagnode != null)
				typetag = typetagnode.getNodeValue().toCharArray();
			Node tagnode = event.getAttributes().getNamedItem(ATTR_TAG);
			if(tagnode != null)
				tag = tagnode.getNodeValue();
		}
		String value = event.getTextContent();
		String[] list = value.split(" ");
		Atom[] atoms = new Atom[list.length];
		for(int i = 0; i < list.length; i++){
			if(typetag != null && typetag.length == list.length){
				if(typetag[i] == 's')
					atoms[i] = Atom.newAtom(list[i]);
				if(typetag[i] == 'i')
					atoms[i] = Atom.newAtom(Integer.parseInt(list[i]));
				if(typetag[i] == 'f')
					atoms[i] = Atom.newAtom(Float.parseFloat(list[i]));
			} else {
				if(list[i].matches("[-,+]*(\\d+)")){
					atoms[i] = Atom.newAtom(Integer.parseInt(list[i]));
				}else if(list[i].matches("[-,+]*(\\d+)\\.(\\d+)")){
					atoms[i] = Atom.newAtom(Float.parseFloat(list[i]));
				}else{
					atoms[i] = Atom.newAtom(list[i]);
				}
			}
		}
		if(tag != null)
			eventOut(tag, atoms);
		else
			eventOut(atoms);
	}

	/**
	 * checks if the current time has passed the frametime and if it is not to far
	 * in the future.
	 *
	 * @param current
	 * @param frame
	 * @return
	 */
	private boolean checkTrigger(long current, Node frame){
		long triggerFrameTime = getTriggerTime(frame);
		return (current >= triggerFrameTime)? true: false;
//		return (current - pastFrameTime < triggerFrameTime && current >= triggerFrameTime)? true: false;
	}

	private long getTriggerTime(Node frame){
		if(frame.hasAttributes()){
			NamedNodeMap dieAttribute = frame.getAttributes();
			for (int i = 0; i < dieAttribute.getLength(); i++){
				Node einAttribut = dieAttribute.item(i);
				if (einAttribut.getNodeName().equals(ATTR_TIME)){
					return parseTimeString(einAttribut.getNodeValue());
				}
			}
		}
		return 0;
	}

	private long getCurrentTime(){
		return (long) clock.getTime() - pausedTime - startTime;
	}


	private long parseTimeString(String timestring){
		long time = 0;
		String[] timearray = timestring.split(":");
		for(int j = 0; j < timearray.length; j++){
			if(timearray[j].contains("d")){
				time += Long.parseLong(timearray[j].substring(0, timearray[j].indexOf("d"))) * 24 * 60 * 60 * 1000;
			}else if(timearray[j].contains("h")){
				time += Long.parseLong(timearray[j].substring(0, timearray[j].indexOf("h"))) * 60 * 60 * 1000;
			}else if(timearray[j].contains("m")){
				time += Long.parseLong(timearray[j].substring(0, timearray[j].indexOf("m"))) * 60 * 1000;
			}else if(timearray[j].contains("s")){
				time += Long.parseLong(timearray[j].substring(0, timearray[j].indexOf("s"))) * 1000;
			}else if(timearray[j].contains(".")){
				time += (long)(Float.parseFloat(timearray[j]) * 1000);
			}else{
				if(j < timearray.length - 1){ // it must be minutes
					time += Long.parseLong(timearray[j]) * 60 * 1000;
				} else if(j > 0 && j == timearray.length - 1){ // it must be seconds
					time += Long.parseLong(timearray[j]) * 1000;
				} else if(timearray.length == 1){ // it must be milliseconds
					time += Long.parseLong(timearray[j]);
				}
			}
		}
		return time;
	}

	private void eventOut(String tag, Atom[] args){
		outlet(0, tag, args);
	}

	private void eventOut(Atom[] args){
		outlet(0, args);
	}

	private void printTime(long current){
		outlet(1, current);
	}

	private void scriptDone(){
		clock.unset();
		outlet(this.getInfoIdx(), "done");
	}

	private void fileLoaded(){
		outlet(this.getInfoIdx(), "loaded");
	}


	protected class NodeIterator{
		private int currentIndex;
		private NodeList list;

		protected NodeIterator(NodeList _list){
			list = _list;
			if(list.getLength() > 0)
				currentIndex = 0;
			else
				currentIndex = -1;
		}

		public boolean hasCurrent(){
			return (currentIndex >= 0)?true: false;
		}

		public Node getCurrent(){
			if(hasCurrent())
				return list.item(currentIndex);
			else
				return null;
		}

		public boolean hasNext(){
			return (hasCurrent() && currentIndex + 1 < list.getLength())? true: false;
		}

		public Node getNext(){
			if(hasNext())
				return list.item(currentIndex + 1);
			else
				return null;
		}

		public int size(){
			return list.getLength();
		}

		public void iterate(){
			currentIndex = (currentIndex + 1 < list.getLength())? currentIndex + 1: -1;
		}

	}
}
