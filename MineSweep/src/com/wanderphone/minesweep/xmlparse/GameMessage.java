package com.wanderphone.minesweep.xmlparse;

import android.util.Log;

public class GameMessage {

	String rankThis;	//本次成绩的排名
	String timeThis;	//本次成绩的时间
	String rankBefore;	//以前最好成绩的排名
	String timeBefore;	//以前最好成绩的时间
	
	public GameMessage()
	{
		rankThis = "no";
		timeThis="no";
		rankBefore="no";
		timeBefore="no";
	}
	
	public String getRankThis()
	{
		Log.v("rankThis",rankThis);
		return rankThis;
	}
	public void setRankThis(String rankThis)
	{
		this.rankThis = rankThis;
	}
	
	public String getTimeThis()
	{
		Log.v("timeThis",timeThis);
		return timeThis;
	}
	public void setTimeThis(String timeThis)
	{
		this.timeThis = timeThis;
	}
	
	public String getRankBefore()
	{
		Log.v("rankbefore",rankBefore);
		return rankBefore;
	}
	public void setRankBefore(String rankBefore)
	{
		this.rankBefore = rankBefore;
	}
	
	public String getTimeBefore()
	{
		Log.v("rankBefore",rankBefore+"njlkkl;ml;");
		return timeBefore;
	}
	public void setTimeBefore(String timeBefore)
	{
		this.timeBefore = timeBefore;
	}
	
}
