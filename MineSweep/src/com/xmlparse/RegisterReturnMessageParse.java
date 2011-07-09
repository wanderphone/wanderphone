package com.xmlparse;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class RegisterReturnMessageParse {

	public static RegisterReturnMessage parse(String registerReturnUrl)
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		RegisterReturnMessage registerReturnMessage = new RegisterReturnMessage();
		try{
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			MessageXMLHandler handler = new MessageXMLHandler(registerReturnMessage);
			xmlReader.setContentHandler(handler);
			
			xmlReader.parse(new InputSource(new StringReader(registerReturnUrl)));
		}catch(SAXException e){
			e.printStackTrace();
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return registerReturnMessage;
	}
}

