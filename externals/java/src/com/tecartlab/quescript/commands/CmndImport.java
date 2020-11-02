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

package com.tecartlab.quescript.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.tecartlab.quescript.expression.RunTimeEnvironment;
import com.tecartlab.quescript.messages.CMsgShuttle;
import com.tecartlab.quescript.messages.ScriptMsgException;
import com.tecartlab.utils.Debug;

public class CmndImport extends Cmnd{
	public static String NODE_NAME = "import";
	
	OutputInterface output;

	private CmndScript myScript;
	
	public CmndImport(Cmnd _parentNode) {
		super(_parentNode);
		super.setCmndName(NODE_NAME);
		
		myScript = new CmndScript();
	}

	public void build(Node _xmlNode) throws ScriptMsgException{
		super.build(_xmlNode);
		// get the instance of the QSManager..
		QSManager sqm = (QSManager)_xmlNode.getParentNode().getParentNode().getUserData("qsmanager");
		// now we are mainly interested in the content
		String parentScriptPath = (String)_xmlNode.getParentNode().getParentNode().getUserData("filepath");
		
		File parentPath = new File(parentScriptPath);
		String dirPath = parentPath.getAbsoluteFile().getParentFile().getAbsolutePath();
		
		Debug.info("CmndImport", "importing: " + dirPath + "/" + content);

		myScript.setOutput(sqm);

		try {
		    SAXSource source = new SAXSource(new InputSource(new java.io.FileInputStream(dirPath + "/" + content)));
		    sqm.validator.validate(source);

		    // Load the Script and make it accessible for building
		    InputStream is = new java.io.FileInputStream(dirPath + "/" + content);
			Document document = PositionalXMLReader.readXML(is);

			document.getDocumentElement().normalize();
			
			// add this info so CmndInput can get the current file path to find its import file
			document.setUserData("filepath", parentScriptPath, null);
			document.setUserData("qsmanager", sqm, null);

			myScript.build(document.getFirstChild());
			
		} catch (SAXParseException e) {
			Debug.error("CmndImport", "Error importing: " + dirPath + "/" + content);
			Debug.error("CmndImport", "Error at line[" + e.getLineNumber() +
					"] col[" + e.getColumnNumber() + "]: " +
					e.getMessage().substring(e.getMessage().indexOf(":")+1));
			sqm.outputInfoMsg(QueMsgFactory.getMsg(QSManager.PARSING).add(QSManager.PARSING_ERROR).add(e.getMessage().substring(e.getMessage().indexOf(":")+1) + " at line(" + e.getLineNumber() + ") col(" + e.getColumnNumber() + ")").done());
			return;
		} catch (ScriptMsgException e) {
			Debug.error("CmndImport", "Error importing: " + dirPath + "/" + content);
			Debug.error("CmndImport", "Error: " + e.getMessage());
			sqm.outputInfoMsg(QueMsgFactory.getMsg(QSManager.PARSING).add(QSManager.PARSING_ERROR).add(e.getMessage()).done());
			return;

		} catch (Exception e) {
			Debug.error("CmndImport", "Error importing: " + dirPath + "/" + content);
			Debug.error("CmndImport", "DocumentBuilder Exceptions:" + e.getMessage());
			sqm.outputInfoMsg(QueMsgFactory.getMsg(QSManager.PARSING).add(QSManager.PARSING_ERROR).add(e.getMessage()).done());
			e.printStackTrace();
			return;
		}
	}

	public void setup(RunTimeEnvironment rt)throws ScriptMsgException{
	}

	public void setOutput(OutputInterface output){
		this.output = output;
	}

	public OutputInterface getOutput(){
		return output;
	}

	/**
	 * gets all this objects ques
	 * @return
	 */
	public List<Cmnd> getQues(){
		return myScript.getQues();
	}

	/**
	 * gets all this objects global vars
	 * @return
	 */
	public List<Cmnd> getVars(){
		return myScript.getVars();
	}

	@Override
	public void bang(CMsgShuttle _msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lockLessBang(CMsgShuttle _msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume(long _timePassed) {
		// TODO Auto-generated method stub

	}

}
