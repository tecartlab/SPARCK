/* MIT License
 * Copyright (c) 2013 maybites.ch
 *
 * Initialy based on
 * Alias .obj loader for processing
 * programmed by Tatsuya SAITO / UCLA Design | Media Arts
 * Created on 2005/04/17
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is furnished
 *  to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.tecartlab.sparck.faceAR;

import com.tecartlab.tools.math.la.*;
import com.tecartlab.utils.Debug;
import com.tecartlab.jay3dee.CallBackInterface;
import com.tecartlab.jay3dee.model.Constants;
import com.tecartlab.jay3dee.model.Face;
import com.tecartlab.jay3dee.model.Group;
import com.tecartlab.jay3dee.model.Segment;
import com.tecartlab.jay3dee.model.Vertice;
import com.tecartlab.tools.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

/**
 * @author martin froehlich et. al.
 */
public class ModelData implements Constants{

	// global variables

	private ArrayList<Vertice> modelVertices; // vertexes
	private ArrayList<Vertice> textureVertices; // texture coordinates
	private ArrayList<Vector3f> normalVertices; // normals
	public ArrayList<Segment> segments;

	private Hashtable<String, Group> groups;

	String name = "default";

	private Group defaultGroup = new Group("default");
	private Segment defaultSegment = new Segment();


	private CallBackInterface myContainer;


	/**
	 * Class Constructor to setup an empty obj model
	 */
	public ModelData(CallBackInterface _myContainer) {
		myContainer = _myContainer;
		setup();
	}


	/****************************************************************
	 * 					Selection Methods
	 ****************************************************************/


	private void setup() {
		modelVertices = new ArrayList<Vertice>();
		segments = new ArrayList<Segment>();
		textureVertices = new ArrayList<Vertice>();
		normalVertices = new ArrayList<Vector3f>();

		groups = new Hashtable<String, Group>();
	}

	// -------------------------------------------------------------------------
	// ------------------------------------------------------------------- Utils
	// -------------------------------------------------------------------------

	/**
	 * A Debug method that prints information about the loaded model<br>
	 * </br> This method only prints information if the debugMode is true.<br>
	 * </br> V Size = The number of vertex positions<br>
	 * </br> Vt Size = The number of UV positions<br>
	 * </br> Vn Size = The number of normals <br>
	 * </br> G Size = the number of Groups in the model <br>
	 * </br> S Size = the number of segments in the model, this should directly
	 * equate to the number of unique materials in the mtl file<br>
	 * </br>
	 */
	public void printModelInfo() {
		Debug.info("Jay3dModel",
				"Obj Name: \t\t" + name + "\n" +
				"V  Size: \t\t" + modelVertices.size() + "\n" +
				"Vt Size: \t\t" + textureVertices.size() + "\n" +
				"Vn Size: \t\t" + normalVertices.size() + "\n" +
				"G  Size: \t\t" + groups.size() + "\n" +
				"S  Size: \t\t" + getSegmentCount() + "\n" + 
				"F  Size: \t\t" + ((segments.size()>0)?segments.get(0).faces.size():0) + "\n");
	}

	/**
	 * Clears all Vectors ready for loading in a new model.<br>
	 * </br> Doing something like this<br>
	 * </br> <br>
	 * </br> OBJModel model = new OBJModel(this, "myobj.obj");<br>
	 * </br> // do something with model<br>
	 * </br> model.clear();<br>
	 * </br> model.load("myobj.obj");<br>
	 * </br> <br>
	 * </br> is vastly better for memory use than doing something like this<br>
	 * </br> <br>
	 * </br> OBJModel model = new OBJModel(this, "myobj.obj");<br>
	 * </br> // do something with model<br>
	 * </br> model = new OBJModel(this, "myOtherObj.obj");<br>
	 * </br> <br>
	 * </br> The second example is really bad because the original model is
	 * still in memory but nothing is pointing to it.<br>
	 * </br> We would have to wait for the Garbage Collector to do the clean up
	 * before freeing the memory. <br>
	 * </br> If loading in lots of stuff is important then using the
	 * model.clear() could help a lot.<br>
	 * </br>
	 */
	public void reset() {
		modelVertices.clear();
		textureVertices.clear();
		normalVertices.clear();
		groups.clear();
		segments.clear();
		Debug.info("Jay3dModel","OBJModel is empty");
	}


	/**
	 * The manual load method for obj files. This method is automaticly called
	 * when using Constructors that include the file name<br>
	 * </br> The method uses the Processing createReader() function to get a
	 * BufferedReader object in order to read the file one line at a time.<br>
	 * </br> This is slightly better method than loadStrings() as it's less
	 * memory intensive for large obj files. <br>
	 * </br>
	 */
	public void load(String filename) {
		setup();
		parseOBJ(getBufferedReader(filename));
	}


	// -------------------------------------------------------------------------
	// -------------------------- Obj Parsing ----------------------------------
	// -------------------------------------------------------------------------

	/**
	 * The method that does all the grunt work in reading and processing the obj
	 * file.<br>
	 * </br>
	 */
	private void parseOBJ(final BufferedReader bread) {
		//create a new thread to fill out darray
		Thread t = new Thread(){
			public void run()
			{
				try {
					defaultGroup = new Group("default");
					defaultSegment = new Segment();

					groups.clear();
					segments.clear();
					// adding default variables to the global data table creating the
					// default group
					groups.put("default", defaultGroup);

					Group currentGroup = defaultGroup;

					// creating the default model segment
					segments.add(defaultSegment);

					currentGroup.segments.add(defaultSegment);

					Segment currentModelSegment = defaultSegment;

					String line;

					while ((line = bread.readLine()) != null) {
						// debug.println(line);
						// parse the line

						// The below patch/hack comes from Carles Tom�s Mart� and is a
						// fix for single backslashes in Rhino obj files

						// BEGINNING OF RHINO OBJ FILES HACK
						// Statements can be broken in multiple lines using '\' at the
						// end of a line.
						// In regular expressions, the backslash is also an escape
						// character.
						// The regular expression \\ matches a single backslash. This
						// regular expression as a Java string, becomes "\\\\".
						// That's right: 4 backslashes to match a single one.
						while (line.contains("\\")) {
							line = line.split("\\\\")[0];
							final String s = bread.readLine();
							if (s != null)
								line += s;
						}
						// END OF RHINO OBJ FILES HACK

						String[] elements = line.split("\\s+");

						// if not a blank line, process the line.
						if (elements.length > 0) {
							if (elements[0].equals("v")) {
								// vertex
								Vertice tmpv = new Vertice(Float.valueOf(elements[1]).floatValue(), Float.valueOf(elements[2]).floatValue(), Float.valueOf(elements[3]).floatValue());
								modelVertices.add(tmpv);
								tmpv.index = modelVertices.indexOf(tmpv);
							} else if (elements[0].equals("vn")) {
								// normal
								Vector3f tmpn = new Vector3f(Float.valueOf(elements[1]).floatValue(), Float.valueOf(elements[2]).floatValue(), Float.valueOf(elements[3]).floatValue());
								normalVertices.add(tmpn);
							} else if (elements[0].equals("vt")) {
								// uv
								Vertice tmpv = new Vertice(Float.valueOf(elements[1]).floatValue(), Float.valueOf(elements[2]).floatValue(), 0.0f);
								textureVertices.add(tmpv);
								tmpv.index = textureVertices.indexOf(tmpv);
							} else if (elements[0].equals("o")) {
								if (elements[1] != null)
									name = elements[1];
							} else if (elements[0].equals("mtllib")) {
								; // material parsing removed from original
							} else if (elements[0].equals("g")) {
								// grouping setting
								Segment newModelSegment = new Segment();

								segments.add(newModelSegment);

								currentModelSegment = newModelSegment;

								for (int e = 1; e < elements.length; e++) {
									if (groups.get(elements[e]) == null) {
										// debug.println("group '" + elements[e] +"'");
										Group newGroup = new Group(elements[e]);

										groups.put(elements[e], newGroup);
									}
								}
							} else if (elements[0].equals("usemtl")) {
								; // dont care about materials
							} else if (elements[0].equals("f")) {
								// face setting
								Face f = new Face();

								if (elements.length < 3)
									Debug.warning("Jay3dModel", "Potential model data error");

								for (int i = 1; i < elements.length; i++) {
									String seg = elements[i];

									if (seg.indexOf("/") > 0) {
										String[] forder = seg.split("/");

										if (forder.length > 2) {
											if (forder[0].length() > 0) {
												f.vertexIndices.add(Integer.valueOf(forder[0]));
												// f.vertices.add(getVertex(Integer.valueOf(forder[0])));
											}

											if (forder[1].length() > 0) {
												f.uvIndices.add(Integer.valueOf(forder[1]));
												// f.uvs.add(getVertex(Integer.valueOf(forder[1])));
											}

											if (forder[2].length() > 0) {
												f.normalIndices.add(Integer.valueOf(forder[2]));
												// f.normals.add(getVertex(Integer.valueOf(forder[2])));
											}
										} else if (forder.length > 1) {
											if (forder[0].length() > 0) {
												f.vertexIndices.add(Integer.valueOf(forder[0]));
												// f.vertices.add(getVertex(Integer.valueOf(forder[0])));
											}

											if (forder[1].length() > 0) {
												f.uvIndices.add(Integer.valueOf(forder[1]));
												// f.uvs.add(getVertex(Integer.valueOf(forder[1])));
											}
										} else if (forder.length > 0) {
											if (forder[0].length() > 0) {
												f.vertexIndices.add(Integer.valueOf(forder[0]));
												// f.vertices.add(getVertex(Integer.valueOf(forder[0])));
											}
										}
									} else {
										if (seg.length() > 0) {
											f.vertexIndices.add(Integer.valueOf(seg));
											// f.vertices.add(getVertex(Integer.valueOf(seg));
										}
									}
								}

								currentModelSegment.faces.add(f);

							} else if (elements[0].equals("l")) {
								// line
								Face f = new Face();
								f.indexType = LINE;

								if (elements.length < 2)
									Debug.warning("Jay3dModel", "potential model data error");

								for (int i = 1; i < elements.length; i++)
									f.vertexIndices.add(Integer.valueOf(elements[i]));

								currentModelSegment.faces.add(f);
							}
						}
					}

					if(textureVertices.size() == 0){
						Debug.warning("Jay3dModel","Carefull: model doesnt contain any texture vertices!");
					}

					updateSegments();
					refreshGeometry();

				} catch (Exception e) {
					Debug.error("Jay3dModel", "Error while trying to read file");
				}

				// Depending on the layout of the obj file, extra empty modeSegments can
				// be created.
				// Here I check each segment to ensure it contains indexes.
				// If there aren't any then the Segment will draw no faces so it's fine
				// to kill it off.
				for (int i = getSegmentCount() - 1; i >= 0; i--) {
					if (getIndexCountInSegment(i) == 0) { // again with the empty model
						// segments WTF?
						segments.remove(i);
					}
				}

				printModelInfo();

				myContainer.dataEvent("fileparsed");
			}
		};
		t.start();
	}

	public void setFaceSubDivision(int subDivision){
		for (int i = 0; i < getSegmentCount(); i++) {
			Segment s = segments.get(i);

			for (int j = 0; j < s.getFaceCount(); j++) {
				Face f = s.getFace(j);
				f.makeSubFaces(subDivision);
			}
		}
	}

	/**
	 * This method refreshes the complete model structure. it should only be called if the
	 * model vertices have been manipulated
	 */
	public void refreshGeometry(){
		for (int i = 0; i < getSegmentCount(); i++) {
			segments.get(i).refreshGeometry();
		}
	}

	/**
	 * This method updates the complete datastructure. It should only be necessary to be called after reading
	 * the modeldata from a file or after a clone.
	 */
	private void updateSegments(ArrayList<Vertice> newList){
		for (int i = 0; i < getSegmentCount(); i++) {
			Segment s = segments.get(i);

			for (int j = 0; j < s.getFaceCount(); j++) {
				Face f = s.getFace(j);

				int[] vtIndex = f.getVertexIndices();
				int[] nmIndex = f.getNormalIndices();
				int[] uvIndex = f.getTextureIndices();

				f.vertices.clear();
				f.normals.clear();
				f.uvs.clear();
				// three for loops for safety. if there are no normals or
				// uv's then nothing will break
				for (int k = 0; k < vtIndex.length; k++)
					f.vertices.add(newList.get(vtIndex[k]));

				for (int k = 0; k < nmIndex.length; k++)
					f.normals.add(normalVertices.get(nmIndex[k]));

				if(textureVertices.size() > 0){
					for (int k = 0; k < uvIndex.length; k++)
						f.uvs.add(textureVertices.get(uvIndex[k]));
				}
			}
		}
	}

	private void updateSegments(){
		updateSegments(modelVertices);
	}


	public boolean saveAs(String filename){
		try
		{
			FileWriter filewriter = new FileWriter(filename);
			BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
			PrintWriter printwriter = new PrintWriter(bufferedwriter, true);
			printwriter.println("# MeshWarpEditor Object File:");
			printwriter.println("# maybites.ch");
			Vector3f tmpn;

			for(int i = 0; i < modelVertices.size(); i++){
				tmpn = modelVertices.get(i);
				printwriter.println("v " + tmpn.x() + " " + tmpn.y() + " " + tmpn.z());
			}
			for(int i = 0; i < textureVertices.size(); i++){
				tmpn = textureVertices.get(i);
				printwriter.println("vt " + tmpn.x() + " " + tmpn.y());
			}
			for(int i = 0; i < normalVertices.size(); i++){
				tmpn = normalVertices.get(i);
				printwriter.println("vn " + tmpn.x() + " " + tmpn.y() + " " + tmpn.z());
			}

			for (int i = 0; i < getSegmentCount(); i++) {
				Segment s = segments.get(i);

				for (int j = 0; j < s.getFaceCount(); j++) {
					Face f = s.getFace(j);
					printwriter.print("f");
					for(int k = 0; k < f.getVertexIndexCount(); k++){
						printwriter.print(" " + f.vertexIndices.get(k));
						if(f.uvIndices.size() > 0)
							printwriter.print("/" + f.uvIndices.get(k));
						if(f.normalIndices.size() > 0)
							printwriter.print("/" + f.normalIndices.get(k));
					}
					printwriter.println();
				}
			}

			printwriter.close();
		}
		catch(Exception exception)
		{
			System.err.println("OBJ saveas: Error while trying to save file as: "+filename);
		}
		return true;

	}


	/**
	 * Gets the number of segments in the model.<br>
	 * </br> A segment is a unique material and an array of indexes into the
	 * vert, norm and uv Vectors<br>
	 * </br>
	 *
	 * @return int
	 */
	public int getSegmentCount() {
		return this.segments.size();
	}


	/**
	 * Gets the number of Index count in the Segment. <br>
	 * </br> In effect this is the number of faces in the Segment. <br>
	 * </br> As each Index is an Array of ints to the vert, normal, or uv Array <br>
	 * </br>
	 *
	 * @param a
	 *            number between 0 and the number of segments <br>
	 *            </br>
	 * @return int <br>
	 *         </br>
	 */
	public int getIndexCountInSegment(int i) {
		return (segments.get(i)).getFaceCount();
	}

	/**
	 * Used in the loading of obj files and mtl files that come from mtl files.<br>
	 * </br>
	 *
	 * @param The
	 *            filename. A String containing the location of the obj file.
	 *            The createReader function should take care of finding the file
	 *            in all the usual Processing places.<br>
	 *            </br>
	 * @return a BufferedReader<br>
	 *         </br>
	 */
	private BufferedReader getBufferedReader(String filename) {
		BufferedReader retval;
		try{
			retval = createReader(filename);
			if (retval != null) {
				return retval;
			} else {
				Debug.error("Jay3DModel", "Unable to load file: " + filename);
			}
		}catch(NullPointerException e){
			Debug.error("Jay3DModel", "Unable to load file: " + filename);
		}
		return null;
	}

	private BufferedReader createReader(String filename){
		return createReader(openInputStream(filename));
	}

	private BufferedReader createReader(InputStream input) {
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(input, "UTF-8");
		} catch (UnsupportedEncodingException e) { }  // not gonna happen
		return new BufferedReader(isr);
	}

	private InputStream openInputStream(String s){
		Object obj = null;
		try
		{
			File file = new File(s);
			FileInputStream fileinputstream = new FileInputStream(file);
			if(s.endsWith(".gz"))
			{
				obj = new GZIPInputStream(fileinputstream);
				s = s.substring(0, s.indexOf(".gz"));
			} else
				if(s.endsWith(".zip"))
				{
					obj = new ZipInputStream(fileinputstream);
					s = s.substring(0, s.indexOf(".zip"));
				} else
				{
					obj = new BufferedInputStream(fileinputstream);
				}
		}
		catch(Exception exception)
		{
			Debug.error("Jay3dModel", "Error while opening inputstream: "+ exception.getMessage());
			obj = null;
		}
		return ((InputStream) (obj));
	}


}
