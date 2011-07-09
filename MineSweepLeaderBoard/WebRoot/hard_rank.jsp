<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%request.setCharacterEncoding("gb2312");%>
<RankEasy>	
<%//连接数据库 
String sql="select user.username,rank_hard.time,rank_hard.ranking from user,rank_hard where user.phone_id=rank_hard.phone_id order by rank_hard.ranking";
String driverClass="com.mysql.jdbc.Driver";
String url="jdbc:mysql://localhost:3306/minesweep";
String usernameSQL="root";
String password="langbbmm";
Class.forName(driverClass).newInstance();
Connection conn=DriverManager.getConnection(url, usernameSQL, password);

Statement stmt=conn.createStatement();
ResultSet rs=stmt.executeQuery(sql);
while(rs.next())
{
%>
	<RankInfo>
		<UserName><%=rs.getString(1)%></UserName>
		<TimeRank><%=rs.getInt(2)%></TimeRank>
		<Rank><%=rs.getInt(3)%></Rank>
	</RankInfo>
<%
}
%>
</RankEasy>