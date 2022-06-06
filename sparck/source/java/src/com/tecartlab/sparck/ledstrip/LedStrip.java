package com.tecartlab.sparck.ledstrip;
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

import com.tecartlab.mxj.utils.JitterObjectArray;
import com.tecartlab.jay3dee.model.Fast3DModelling;
import com.tecartlab.utils.Debug;

import com.cycling74.max.*;
import com.cycling74.jitter.*;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author maybites
 *
 *
 */

public class LedStrip extends MaxObject {

	public static final String VERSION = "1";

	public static final String 	ELEMENT_LEDSTRIP 		= "ledstrip";
	private static final String		ATTR_VERSION 		= "version";

	private static int OUTLET_SEGMENTS = 0;
	private static int OUTLET_STATS = 1;
	private static int OUTLET_UPDATE = 2;
	private static int OUTLET_DUMP = 3;

	private static final int GUI_MAX_SEGMENTS = 16;

	public String filepath;
	public String context;
	public String parentAnimName;

	private JitterObjectArray drawable;
	private Document document;

	private float[] color = new float[]{.5f, .5f, .5f, 1.f};

	private SegmentsMngr segmntMngr;

	private int stripID;

	private boolean showUnusedSegments;

	public LedStrip(Atom args[]){
		if (args.length != 1)
			Debug.warning("LedStrip", "needs a drawing context as argument.");
		else{
			segmntMngr = new SegmentsMngr();
			setcontext(args[0].toString());
		}

		declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});

		Debug.info("LedStrip", "version 0");

		stripID = -1;
		showUnusedSegments = false;
	}

	/**
	 * Loads the Led Strip file
	 * @param _filepath
	 */
	public void read(String _filepath){
		segmntMngr.read(_filepath);
	}

	public void setcontext(String _context){
		context = _context;
		segmntMngr.setupContext(_context);
	}

	public void animname(String _animname){
		parentAnimName = _animname;
		segmntMngr.setupTransform(_animname);
	}

	public void show_unused_segmts(int _unused){
		showUnusedSegments	= (_unused == 1)? true: false;
		segmntMngr.refresh();
	}

	public void transformation_bang(){
		segmntMngr.transformation_bang();
	}

	public void drawto_render(Atom[] _drawto){
		segmntMngr.drawto_render(_drawto);
	}

	public void drawto_stageview(Atom[] _drawto){
		segmntMngr.drawto_stageview(_drawto);
	}

	public void ledsize(float _size){
		segmntMngr.ledsize(_size);
	}

	public void texture(Atom[] _textures){
		segmntMngr.texture(_textures);
	}

	public void shader(String _shaderName){
		segmntMngr.shader(_shaderName);
	}

	public void setall_ledspermeter(float _ledsPerMeter){
		segmntMngr.setall_ledspermeter(_ledsPerMeter);
	}

	public void segments(int _sID, String _method){
//		Debugger.verbose("Led Strip", "segments ID = " + _sID + " | method =  " + _method );
		if(_method.equals("swapup")){
			segmntMngr.swap(_sID, _sID - 1);
		} else if(_method.equals("swapdown")){
			segmntMngr.swap(_sID, _sID + 1);
		}
	}

	public void segments(int _sID, String _segmentName, long _ledstepdistance, long _ledinset, long _ledoffset, long _direction){
		segmntMngr.set(_sID, _segmentName, (int)_ledstepdistance, (int)_ledinset, (int)_ledoffset, (int)_direction);
	}

	public void color(float _red, float _green, float _blue, float _alpha){
		color[0] = _red;
		color[1] = _green;
		color[2] = _blue;
		color[3] = _alpha;

		segmntMngr.setColor(color);
	}

	public void id(int _stripID){
		stripID = _stripID;
	}

	public void layer(int _layer){
		segmntMngr.setLayer(_layer);
	}

	public void tempFilePath(String _path){
		segmntMngr.tempFilePath(_path);
	}

	protected void setSegment(int _segmentID, String _mesg, int _mesg1){
		Atom[] list = {Atom.newAtom(_segmentID), Atom.newAtom(_mesg), Atom.newAtom(_mesg1)};
		outlet(OUTLET_SEGMENTS, list);
	}

	protected void setSegment(int _segmentID, String _mesg, float _mesg1){
		Atom[] list = {Atom.newAtom(_segmentID), Atom.newAtom(_mesg), Atom.newAtom(_mesg1)};
		outlet(OUTLET_SEGMENTS, list);
	}

	protected void setSegment(int _segmentID, String _mesg, String _mesg1){
		Atom[] list = {Atom.newAtom(_segmentID), Atom.newAtom(_mesg), Atom.newAtom(_mesg1)};
		outlet(OUTLET_SEGMENTS, list);
	}

	protected void setSegment(int _segmentID, String _mesg, String _mesg1, String _mesg2){
		Atom[] list = {Atom.newAtom(_segmentID), Atom.newAtom(_mesg), Atom.newAtom(_mesg1), Atom.newAtom(_mesg2)};
		outlet(OUTLET_SEGMENTS, list);
	}

	protected void setSegment(int _segmentID, String _mesg, String _mesg1, String _mesg2, String _mesg3){
		Atom[] list = {Atom.newAtom(_segmentID), Atom.newAtom(_mesg), Atom.newAtom(_mesg1), Atom.newAtom(_mesg2), Atom.newAtom(_mesg3)};
		outlet(OUTLET_SEGMENTS, list);
	}

	protected void setStat(String _mesg){
		outlet(OUTLET_STATS, "info", Atom.newAtom(new String[]{"set", _mesg}));
	}

	protected void outputLedCount(int _length){
		outlet(OUTLET_STATS, "store", new Atom[]{Atom.newAtom("ledcount"), Atom.newAtom(_length)});
	}

	protected void sendUpdateBang(){
		outlet(OUTLET_UPDATE, "bang");
	}


	public class SegmentsMngr {
		private GUISegment[] gui_segments;
		public ArrayList<ModelSegment> model_segments;

		private JitterObject objectToStage;
		private JitterObject objectToRender;

		private Fast3DModelling objectToStage2;

		private JitterObject transformToRender;
		private JitterObject transformToStage;
		private JitterObject transformToStage2;

		float ledsize;

		boolean render4Shader = false;

		String tempFilePath;

		public SegmentsMngr(){
			gui_segments = new GUISegment[GUI_MAX_SEGMENTS];
			model_segments = new ArrayList<ModelSegment>();
		}

		private void setupContext(String _context){
			if(objectToStage == null){
				objectToStage = new JitterObject("jit.gl.sketch");
				objectToRender = new JitterObject("jit.gl.sketch");
				objectToStage2 = new Fast3DModelling(_context);

				objectToStage2.setAttr("auto_material", 0);
				objectToStage2.setAttr("gen_normals", 0);
				objectToStage2.setAttr("gen_tangents", 0);
				objectToStage2.setAttr("material_mode", 0);
				objectToStage2.setAttr("normalize", 0);

				objectToStage.setAttr("drawto", _context);
				objectToRender.setAttr("drawto", _context);

				objectToStage.setAttr("enable", 1);
				objectToRender.setAttr("enable", 1);
			}
		}

		private void setupTransform(String _parentAnimNode){
			if(objectToStage != null && transformToRender == null){
				transformToRender = new JitterObject("jit.anim.node");
				transformToStage = new JitterObject("jit.anim.node");
				transformToStage2 = new JitterObject("jit.anim.node");

				transformToStage.setAttr("anim", _parentAnimNode);
				transformToStage2.setAttr("anim", _parentAnimNode);
				transformToRender.setAttr("anim", _parentAnimNode);

				transformToStage2.setAttr("automatic", 0);
				transformToStage.setAttr("automatic", 0);
				transformToRender.setAttr("automatic", 0);

				objectToStage2.setAttr("anim", transformToStage2.getAttr("name"));
				objectToStage.setAttr("anim", transformToStage.getAttr("name"));
				objectToRender.setAttr("anim", transformToRender.getAttr("name"));
			}
		}

		/**
		 * Loads the Led Strip file
		 * @param _filepath
		 */
		public void read(String _filepath){
			File dieXMLDatei = new File(_filepath);

			Debug.verbose("loading ledstrip ", "'"+_filepath+"'");

			try {
				DocumentBuilderFactory dasDBFactoryObjekt = DocumentBuilderFactory.newInstance();
				DocumentBuilder dasDBObjekt = dasDBFactoryObjekt.newDocumentBuilder();
				document = dasDBObjekt.parse(dieXMLDatei);
			} catch (Exception e) {
				Debug.error(this.getClass(), "DocumentBuilder Exceptions:" + e.getMessage());
				return;
			}
			filepath = _filepath;

			document.getDocumentElement().normalize();

			Element root = document.getDocumentElement();
			NamedNodeMap values = root.getAttributes();
			Node version = values.getNamedItem(ATTR_VERSION);
			if(version != null && version.getTextContent().equals(VERSION)){

				NodeList segmentList = document.getElementsByTagName(ModelSegment.ELEMENT_SEGMENT);

				clear();

				/*
				 <ledstrip>
					<segment name="SegmentName">
						...
					</segment>
					<segment name="SegmentName.001">
						...
					</segment>
				</ledstrip>
				*/

				for(int i = 0; i < segmentList.getLength(); i++){
					Node segment = segmentList.item(i);
					ModelSegment newSegment = new ModelSegment(segment, color, objectToStage2, objectToStage, objectToRender);
					newSegment.setPointSize(ledsize);
					add(newSegment);
				}

				Debug.verbose("Led Strip", "loaded " + model_segments.size() + " segments");

				refresh();

				// after the strip has been loaded, get the stored pattr parameters
				for(int i = 0; i < GUI_MAX_SEGMENTS; i++){
					setSegment(i, "reload", "bang");
				}
			} else {
				Debug.warning("Led Strip", "loaded script (" + _filepath + ") has an invalid version. Script requires Version=" + VERSION);
			}
		}

		public void texture(Atom[] _textures){
			objectToStage2.texture(_textures);
		}

		public void shader(String _shaderName){
			if(_shaderName.equals("")){
				objectToStage2.shader();
				render4Shader = false;
			} else {
				objectToStage2.shader(_shaderName);
				render4Shader = true;
			}
			refresh();
		}

		public void tempFilePath(String _path){
			tempFilePath = _path;
		}

		public void ledsize(float _size){
			ledsize = _size;
			for(int i = 0; i < model_segments.size(); i++)
				model_segments.get(i).setPointSize(_size);
			refresh();
		}

		public void setall_ledspermeter(float _ledsPerMeter){
			if(_ledsPerMeter >= 1){
				for(int i = 0; i < gui_segments.length; i++){
					gui_segments[i].ledsPerMeter = _ledsPerMeter;
					gui_segments[i].print(LedStrip.this, i);
				}
			}
			refresh();
		}

		public void set(int _pos, String _name, float _ledsPerMeter, int _ledinset, int _ledoffset, int _direction){
			if(_ledsPerMeter >= 1){
				if(gui_segments[_pos] != null){
					gui_segments[_pos].name = _name;
					gui_segments[_pos].ledsPerMeter = _ledsPerMeter;
					gui_segments[_pos].ledinset = _ledinset;
					gui_segments[_pos].ledoffset = _ledoffset;
					gui_segments[_pos].direction = _direction;
				} else {
					gui_segments[_pos] = new GUISegment(_name, _ledsPerMeter, _ledinset, _ledoffset, _direction);
				}
				refresh();
			}else{
				Debug.warning("LedStrip", "set arguments invalid");
			}
		}

		public void swap(int _from, int _to){
			if(_from >= 0 && _from <= GUI_MAX_SEGMENTS && _to >= 0 && _to <= GUI_MAX_SEGMENTS){
				GUISegment to = gui_segments[_to];
				gui_segments[_to] = gui_segments[_from];
				gui_segments[_from] =  to;
				gui_segments[_to].print(LedStrip.this, _to);
				gui_segments[_from].print(LedStrip.this, _from);
			}
			refresh();
		}

		public void add(ModelSegment _seg){
			model_segments.add(_seg);
		}

		public void transformation_bang(){
			transformToStage2.call("update_node");
			transformToStage.call("update_node");
			transformToRender.call("update_node");
		}

		public void drawto_render(Atom[] _drawto){
			objectToRender.setAttr("drawto", _drawto);
		}

		public void drawto_stageview(Atom[] _drawto){
			objectToStage.setAttr("drawto", _drawto);
			objectToStage2.drawto(_drawto);
		}

		public void setColor(float[] _color){
			for(int i = 0; i < model_segments.size(); i++)
				model_segments.get(i).setColor(_color);
			refresh();
		}

		public void setLayer(int _layer){
			objectToStage.setAttr("layer", _layer);
			objectToRender.setAttr("layer", _layer);
		}

		void refresh(){
			objectToStage.call("reset");
			objectToRender.call("reset");
			objectToStage2.clear();

			ArrayList<String> modelNames = new ArrayList<String>();
			for(int i = 0; i < model_segments.size(); i++){
//				System.out.println("model_segment found = " + model_segments.get(i).name);
				modelNames.add(model_segments.get(i).name);
			}

			int ledIndex = 0;
			int totalLeds = 0;
			float totalLength = 0;

			for(int i = 0; i < GUI_MAX_SEGMENTS; i++){
				GUISegment sgmnt = gui_segments[i];
				if(sgmnt != null && modelNames.contains(sgmnt.name)){
//					System.out.println("segment to assign = " + sgmnt.name);
					for(int j = 0; j < model_segments.size(); j++){
						if(sgmnt.name.equals(model_segments.get(j).name)){
//							System.out.println("model assigned = " + sgmnt.name);
							sgmnt.model = model_segments.get(j);
							ledIndex = model_segments.get(j).draw(render4Shader, stripID, ledIndex, sgmnt.ledsPerMeter, sgmnt.ledinset, sgmnt.ledoffset, sgmnt.direction);
							totalLength += model_segments.get(j).getSegmentLength();
							totalLeds += model_segments.get(j).getSegmentLedCount();
							modelNames.remove(sgmnt.name);
						}
					}
				}
			}

			if(showUnusedSegments){
				for(int j = 0; j < model_segments.size(); j++){
					if(modelNames.contains((model_segments.get(j).name))){
						model_segments.get(j).drawAsUnused();
//						System.out.println("model_segment left = " + model_segments.get(j).name);
					}
				}
			}

			for(int i = 0; i < GUI_MAX_SEGMENTS; i++){
				setSegment(i, "segmentmenu", "clear");
				for(int t = 0; t < modelNames.size(); t++){
					setSegment(i, "segmentmenu", "hint", "stats: no data available");
					setSegment(i, "segmentmenu", "insert", "0", modelNames.get(t));
//					System.out.println("model_segment left = " + modelNames.get(t));
				}
				GUISegment sgmnt = gui_segments[i];
				if(gui_segments[i] != null && !sgmnt.name.equals("off")){
					setSegment(i, "segmentmenu", "insert", "0", sgmnt.name);
					setSegment(i, "segmentmenu", "hint", "stats: length = " + getStringVal(sgmnt.getModel().getSegmentLength(), 3) + "m | led count = " + sgmnt.getModel().getSegmentLedCount());
					setSegment(i, "segmentmenu", "insert", "0", "off");
					setSegment(i, "segmentname", sgmnt.name);
				} else {
					setSegment(i, "segmentmenu", "insert", "0", "off");
				}
			}

			setStat("stats: length = " + getStringVal(totalLength, 3) + "m | led count = " + totalLeds);

			if(tempFilePath != null)
				objectToStage2.update(tempFilePath);

			sendUpdateBang();
			outputLedCount(totalLeds);
		}

		private String getStringVal(float _val, int _digits){
		    String format = "%." + _digits + "f";
		    return String.format(format, _val);
		}

		public void clear(){
			for(int i = 0; i < model_segments.size(); i++)
				model_segments.get(i).clear();
			model_segments.clear();
		}

		public void notifyDeleted(){
			if(objectToStage != null){
				objectToStage.freePeer();
				objectToRender.freePeer();
			}
			if(transformToStage != null){
				transformToStage2.freePeer();
				transformToStage.freePeer();
				transformToRender.freePeer();
			}
			if(objectToStage2 != null){
				objectToStage2.clear();
				objectToStage2.notifyDeleted();
			}

			clear();
		}

	}

	public void notifyDeleted(){
		segmntMngr.notifyDeleted();
	}

}
