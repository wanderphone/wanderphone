<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
String ranking = request.getAttribute("ranking").toString();
String ranking_now = request.getAttribute("ranking_now").toString();
String time = request.getAttribute("time").toString();
String time_now = request.getAttribute("time_now").toString();
%>

<GameOver>
	<Ranking><%out.print(ranking);%></Ranking>
	<Time><%out.print(time);%></Time>
	<RankingNow><%out.print(ranking_now);%></RankingNow>
	<TimeNow><%out.print(time_now);%></TimeNow>
</GameOver>