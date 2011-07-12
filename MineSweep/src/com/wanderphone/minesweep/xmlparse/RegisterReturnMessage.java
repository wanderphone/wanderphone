package com.wanderphone.minesweep.xmlparse;

public class RegisterReturnMessage {
	String isSuccess;
	
	public RegisterReturnMessage()
	{
		isSuccess = "no";
	}
	public String getIsSuccess()
	{
		return this.isSuccess;
	}
	public void setIsSuccess(String isSuccess)
	{
		this.isSuccess = isSuccess;
	}
}
