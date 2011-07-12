package com.wanderphone.minesweep.xmlparse;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class GameMessageParse {

	public static GameMessage parse(String gameMessageUrl)
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		GameMessage gameMessage = new GameMessage();
		
		try{
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			MessageXMLHandler handler = new MessageXMLHandler(gameMessage);
			xmlReader.setContentHandler(handler);
			
			xmlReader.parse(new InputSource(new StringReader(gameMessageUrl)));
		}catch(SAXException e){
			e.printStackTrace();
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return gameMessage;
	}
}
