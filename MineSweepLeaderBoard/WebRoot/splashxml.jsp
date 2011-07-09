<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String isRegister=request.getAttribute("isRegister").toString();
	String easyTime=request.getAttribute("easyTime").toString();
	String easyRank=request.getAttribute("easyRank").toString(); 
	String normalTime=request.getAttribute("normalTime").toString();
	String normalRank=request.getAttribute("normalRank").toString(); 
	String hardTime=request.getAttribute("hardTime").toString();
	String hardRank=request.getAttribute("hardRank").toString(); 
%>
<First>
	<IsRegister><%out.print(isRegister); %></IsRegister>
	<EasyTime><%out.print(easyTime); %></EasyTime>
	<EasyRank><%out.print(easyRank); %></EasyRank>
	<NormalTime><%out.print(normalTime); %></NormalTime>
	<NormalRank><%out.print(normalRank); %></NormalRank>
	<HardTime><%out.print(hardTime); %></HardTime>
	<HardRank><%out.print(hardRank); %></HardRank>	
</First>