package com.xmlparse;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Log;

public class MessageXMLHandler extends DefaultHandler {
	
	private String preTAG;
	SplashMessage splashMessage;
	GameMessage gameMessage;
	RegisterReturnMessage registerReturnMessage;
	List<RankInfo> rankInfos;
	RankInfo rankInfo;

	// 记录出现次数
	int findCount = 0;

	// 默认构造方法
	public MessageXMLHandler() {
		super();
	}

	// 构造方法
	public MessageXMLHandler(SplashMessage splashMessage) {
		this.splashMessage = splashMessage;
	}
	
	// 构造方法
	public MessageXMLHandler(GameMessage gameMessage) {
		this.gameMessage = gameMessage;
	}
	

	public MessageXMLHandler(RegisterReturnMessage registerReturnMessage) {
		// TODO Auto-generated constructor stub
		this.registerReturnMessage = registerReturnMessage;
	}
	
	// 构造方法
	public MessageXMLHandler(List<RankInfo> rankInfos) {
		this.rankInfos = rankInfos;
	}

	/*
	 * 文档结束时触发
	 */
	@Override
	public void endDocument() throws SAXException {
		Log.i("yao", "文档解析结束");
		super.endDocument();
	}

	/*
	 * 文档开始时触发
	 */
	@Override
	public void startDocument() throws SAXException {
		Log.i("yao", "文档解析开始");
		super.startDocument();
	}
	/*
	 * 元素开始时触发
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		Log.i("yao", qName);
		preTAG = localName;
		Log.v("localName",localName);
		super.startElement(uri, localName, qName, attributes);
	}

	/*
	 * 元素结束时触发
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		Log.i("yao", "元素解析结束");
		preTAG=null;
		super.endElement(uri, localName, qName);
	}

	/*
	 * 读取元素内容
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		Log.v("test","Test");
		if(preTAG!=null)
		{
			if(preTAG.equals("RankInfo"))
			{
				rankInfo = new RankInfo();
			}
			String data = new String(ch, start, length);
			/*若是splash申请返回的信息，则如下解析*/
			if(preTAG.equals("IsRegister"))
			{
				splashMessage.setIsRegister(data);
				Log.v("hh",data);
			}
			else if(preTAG.equals("EasyTime"))
			{
				splashMessage.setEasyTime(data);
				Log.v("hh",data);
			}
			else if(preTAG.equals("EasyRank"))
			{
				Log.v("hh",data);
				splashMessage.setEasyRank(data);
				Log.v("hh",data);
			}			
			else if(preTAG.equals("NormalTime"))
			{
				splashMessage.setNormalTime(data);
				Log.v("hh",data);
			}
			else if(preTAG.equals("NormalRank"))
			{
				splashMessage.setNormalRank(data);
				Log.v("hh",data);
			}		
			else if(preTAG.equals("HardTime"))
			{
				splashMessage.setHardTime(data);
				Log.v("hh",data);
			}
			else if(preTAG.equals("HardRank"))
			{
				splashMessage.setHardRank(data);
				Log.v("hh",data);
			}
			
			/*若是游戏结束申请返回的信息，则如下解析*/
			if(preTAG.equals("Ranking"))
			{
				gameMessage.setRankThis(data);
				Log.v("rankThis",data);
			}			
			else if(preTAG.equals("Time"))
			{
				Log.v("timeThis",data);
				gameMessage.setTimeThis(data);
			}			
			else if(preTAG.equals("RankingNow"))
			{
				Log.v("game",data);
				gameMessage.setRankBefore(data);
				Log.v("rankBefore",gameMessage.getRankBefore());
			}		
			else if(preTAG.equals("TimeNow"))
			{
				Log.v("game",data);
				gameMessage.setTimeBefore(data);
				Log.v("timeBefore",gameMessage.getTimeBefore());
			}
			
			//如果是返回的是否注册成功的页面，则如下解析
			
			if(preTAG.equals("IsSuccess"))
			{
				registerReturnMessage.setIsSuccess(data);
				Log.v("isSuccess",data);
			}
			
			/*若是排行榜申请，则如下解析*/
			if(preTAG.equals("Rank"))
			{
				rankInfo .setRank(data);
				Log.v("Rank", data);
				rankInfos.add(rankInfo);
				Log.v("rankInfo", rankInfos.get(0).getUsername());
			}
			
			else if(preTAG.equals("UserName"))
			{
				Log.v("username", data+"username");
				rankInfo.setUsername(data);
			}
			
			else if(preTAG.equals("TimeRank"))
			{
				rankInfo.setTime(data);
				Log.v("time", data);
			}
		}
		super.characters(ch, start, length);
	}
}

