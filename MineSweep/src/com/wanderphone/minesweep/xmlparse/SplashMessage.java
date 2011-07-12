package com.wanderphone.minesweep.xmlparse;

import android.util.Log;

public class SplashMessage {

	String isRegister;		//标记是否已经注册
	String easyRank;		//简单难度的排名
	String easyTime;		//排名对应的时间
	String normalRank;		//中等难度的排名
	String normalTime;		//排名对应的时间
	String hardRank;		//困难难度的排名
	String hardTime;		//排名对应的时间
	
	public SplashMessage()
	{
		
	}
	public String getIsRegister()
	{
		return isRegister;
	}
	public void setIsRegister(String isRegister)
	{
		this.isRegister = isRegister;
	}
	
	public String getEasyRank()
	{
		Log.v("rankThis","test");
		return easyRank;
	}
	public void setEasyRank(String easyRank)
	{
		this.easyRank = easyRank;
	}
	public String getEasyTime()
	{
		return easyTime;
	}
	public void setEasyTime(String easyTime)
	{
		this.easyTime = easyTime;
	}
	
	public String getNormalRank()
	{
		return normalRank;
	}
	public void setNormalRank(String normalRank)
	{
		this.normalRank = normalRank;
	}
	public String getNormalTime()
	{
		return normalTime;
	}
	public void setNormalTime(String normalTime)
	{
		this.normalTime = normalTime;
	}
	public String getHardRank()
	{
		return hardRank;
	}
	public void setHardRank(String hardRank)
	{
		this.hardRank = hardRank;
	}
	public String getHardTime()
	{
		return hardTime;
	}
	public void setHardTime(String hardTime)
	{
		this.hardTime = hardTime;
	}
}
