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

//PositionalXMLReader.java

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PositionalXMLReader {
	final static String LINE_NUMBER_KEY_NAME = "lineNumber";

	public static Document readXML(final InputStream is) throws IOException, SAXException {
		final Document doc;
		SAXParser parser;
		try {
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			parser = factory.newSAXParser();
			final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
		} catch (final ParserConfigurationException e) {
			throw new RuntimeException("Can't create SAX parser / DOM builder.", e);
		}

		final Stack<Element> elementStack = new Stack<Element>();
		final StringBuilder textBuffer = new StringBuilder();

		final DefaultHandler handler = new DefaultHandler() {
			private Locator locator;

			@Override
			public void setDocumentLocator(final Locator locator) {
				this.locator = locator; // Save the locator, so that it can be used later for line tracking when traversing nodes.
			}

			@Override
			public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
					throws SAXException {
				addTextIfNeeded();
				final Element el = doc.createElement(qName);
				for (int i = 0; i < attributes.getLength(); i++) {
					el.setAttribute(attributes.getQName(i), attributes.getValue(i));
				}
				el.setUserData(LINE_NUMBER_KEY_NAME, String.valueOf(this.locator.getLineNumber()), null);
				elementStack.push(el);
			}

			@Override
			public void endElement(final String uri, final String localName, final String qName) {
				addTextIfNeeded();
				final Element closedEl = elementStack.pop();
				if (elementStack.isEmpty()) { // Is this the root element?
					doc.appendChild(closedEl);
				} else {
					final Element parentEl = elementStack.peek();
					parentEl.appendChild(closedEl);
				}
			}

			@Override
			public void characters(final char ch[], final int start, final int length) throws SAXException {
				textBuffer.append(ch, start, length);
			}

			// Outputs text accumulated under the current node
			private void addTextIfNeeded() {
				if (textBuffer.length() > 0) {
					final Element el = elementStack.peek();
					final Node textNode = doc.createTextNode(textBuffer.toString());
					el.appendChild(textNode);
					textBuffer.delete(0, textBuffer.length());
				}
			}
		};
		parser.parse(is, handler);

		return doc;
	}
}
