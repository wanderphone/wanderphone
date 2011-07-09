package com.xmlparse;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class RankInfoParse {
	
	public static List<RankInfo> parse(String rankInfoUrl)
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	//	RankInfo rankInfo = new RankInfo();
		final List<RankInfo> rankInfos= new  ArrayList<RankInfo>();
		
		try{
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			MessageXMLHandler handler = new MessageXMLHandler(rankInfos);
			xmlReader.setContentHandler(handler);
			
			xmlReader.parse(new InputSource(new StringReader(rankInfoUrl)));
		}catch(SAXException e){
			e.printStackTrace();
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return rankInfos;
	}
}
