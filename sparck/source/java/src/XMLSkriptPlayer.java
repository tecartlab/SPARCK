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

public class XMLSkriptPlayer extends MaxObject
{
	long naechsterZeitpunkt = 0;
	int derEventCounter;
	Document dasDOMObjekt;
	NodeList dieEventListe;

	String filepath;

	public XMLSkriptPlayer(Atom[] args)
	{
		declareInlets(new int[]{DataTypes.INT, DataTypes.LIST});
		declareOutlets(new int[]{DataTypes.ALL,DataTypes.ALL});
		filepath = "/OSCScripts/";
		post("XMLScriptPlayer Version 003");
	}

	public void inlet(int aktuelleZeit)
	{
		if (aktuelleZeit >= naechsterZeitpunkt)
		{
			messagesAusgeben(derEventCounter);
			derEventCounter++;
			if (derEventCounter == dieEventListe.getLength())
			{
				outlet(1, 0);
			}
			else
			{
				naechstenZeitpunktFinden(derEventCounter);
			}
		}
	}

	public void path(Atom[] path){
		filepath = path[0].toString();
	}

	public void start(Atom[] dasSkript)
	{
		outlet(1, 0);
		derEventCounter = 0;
		naechsterZeitpunkt = 0;
		File dieXMLDatei = new File(filepath + dasSkript[0].toString());
		DocumentBuilderFactory dasDBFactoryObjekt = DocumentBuilderFactory.newInstance();
		post("Loaded successfull: "+dieXMLDatei.getAbsolutePath());
		try
		{
			DocumentBuilder dasDBObjekt = dasDBFactoryObjekt.newDocumentBuilder();
			dasDOMObjekt = dasDBObjekt.parse(dieXMLDatei);
		}
		catch (Exception e)
		{
			post("DocumentBuilder Exceptions!!");
			//nicht beachten
		}
		dasDOMObjekt.getDocumentElement().normalize();
		dieEventListe = dasDOMObjekt.getElementsByTagName("event");
		if (dieEventListe.getLength() == 0)
		{
			return;
		}
		post("Found "+dieEventListe.getLength()+" events.");
		naechstenZeitpunktFinden(derEventCounter);
		outlet(1, 1);
	}

	void naechstenZeitpunktFinden(int dieEventNummer)
	{
		Node zuBearbeitenderEvent = dieEventListe.item(dieEventNummer);
		NamedNodeMap dieAttribute = zuBearbeitenderEvent.getAttributes();
		long neueZeit = 0;
		boolean relativeZeit = true;
		for (int i = 0; i < dieAttribute.getLength(); i++)
		{
			Node einAttribut = dieAttribute.item(i);
			if (einAttribut.getNodeName().compareTo("hour") == 0)
			{
				neueZeit += Integer.parseInt(einAttribut.getNodeValue()) * 3600000;
				continue;
			}
			if (einAttribut.getNodeName().compareTo("min") == 0)
			{
				neueZeit += Integer.parseInt(einAttribut.getNodeValue()) * 60000;
				continue;
			}
			if (einAttribut.getNodeName().compareTo("sec") == 0)
			{
				neueZeit += Integer.parseInt(einAttribut.getNodeValue()) * 1000;
				continue;
			}
			if (einAttribut.getNodeName().compareTo("millisec") == 0)
			{
				neueZeit += Integer.parseInt(einAttribut.getNodeValue());
				continue;
			}
			if (einAttribut.getNodeName().compareTo("relation") == 0)
			{
				if (einAttribut.getNodeValue().compareTo("relative") == 0)
				{
					relativeZeit = true;
				}
				else if (einAttribut.getNodeValue().compareTo("absolute") == 0)
				{
					relativeZeit = false;
				}
				continue;
			}
		}
		if (relativeZeit)
		{
			naechsterZeitpunkt += neueZeit;
		}
		else
		{
			naechsterZeitpunkt = neueZeit;
		}
	}

	void messagesAusgeben(int dieEventNummer)
	{
		Node zuBearbeitenderEvent = dieEventListe.item(dieEventNummer);
		NodeList dieMessages = zuBearbeitenderEvent.getChildNodes();
		for (int i = 0; i < dieMessages.getLength(); i++)
		{
			if (dieMessages.item(i).getNodeName().compareTo("message") == 0)
			{
				Node eineMessage = dieMessages.item(i);
				eineMessage.normalize();
				NamedNodeMap messageAttribute = eineMessage.getAttributes();
				Node dasAdressAttribut = messageAttribute.getNamedItem("adress");
				NodeList dieArgumente = eineMessage.getChildNodes();
				int anzahlArgumente = 0;
				for (int j = 0; j < dieArgumente.getLength(); j++)
				{
					if (dieArgumente.item(j).getNodeName().compareTo("argument") == 0)
					{
						anzahlArgumente++;
					}
				}
				Atom[] ausgabeMessage = new Atom[anzahlArgumente + 1];
				ausgabeMessage[0] = Atom.newAtom(dasAdressAttribut.getNodeValue());
				int dieArgumentNummer = 1;
				for (int j = 0; j < dieArgumente.getLength(); j++)
				{
					if (dieArgumente.item(j).getNodeName().compareTo("argument") == 0)
					{

						Node einArgument = dieArgumente.item(j);
						NamedNodeMap argumentAttribute = einArgument.getAttributes();
						Node derAttributTyp = argumentAttribute.getNamedItem("type");
						Node derAttributWert = argumentAttribute.getNamedItem("value");
						if (derAttributTyp.getNodeValue().compareTo("int") == 0)
						{
							ausgabeMessage[dieArgumentNummer] = Atom.newAtom(Integer.parseInt(derAttributWert.getNodeValue()));
							dieArgumentNummer++;
						}
						else if (derAttributTyp.getNodeValue().compareTo("float") == 0)
						{
							ausgabeMessage[dieArgumentNummer] = Atom.newAtom(Float.parseFloat(derAttributWert.getNodeValue()));
							dieArgumentNummer++;
						}
						else if (derAttributTyp.getNodeValue().compareTo("string") == 0)
						{
							ausgabeMessage[dieArgumentNummer] = Atom.newAtom(derAttributWert.getNodeValue());
							dieArgumentNummer++;
						}
					}
				}
				outlet(0, ausgabeMessage);
			}
		}
	}
}
