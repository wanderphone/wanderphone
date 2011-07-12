package com.wanderphone.minesweep.xmlparse;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SplashMessageParse {

	// 解析天气预报字符串成一个天气信息对象
	public static SplashMessage parse(String splashReturnMessage) {

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

		SplashMessage splashMessage = new SplashMessage();

		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			MessageXMLHandler handler = new MessageXMLHandler(splashMessage);
			xmlReader.setContentHandler(handler);

			xmlReader.parse(new InputSource(new StringReader(splashReturnMessage)));

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return splashMessage;

	}
}