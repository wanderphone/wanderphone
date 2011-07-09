<div align="left"><%@page language="java" contenttype="text/html; charset=gb2312" pageencoding="gb2312"%> 
<%@page import="java.sql.*"%> 
<%request.setCharacterEncoding("gb2312");%> 
 
<%
String phone_id = null;	//用户的电话Id，即其唯一标示符
String username=null;	//用户名
int which_use;			// 客户端提交申请的标示：1为splash界面提交的申请， 2则是 用户注册的用户名的申请， 3为游戏结束后提交的申请
String level="null";				//游戏难度的标示：easy为简单，normal为中等，hard为困难
int time = 100;		//用户的游戏时间 
int ranking = 0;		//用户的排行，默认为0，表示没有排行 
int ranking_now=0;	//保存当前用户的排名情况 
int time_now=0;		//保存当前用户的游戏时间 
String IsRegister;

//连接数据库 
String driverClass="com.mysql.jdbc.Driver";
String url="jdbc:mysql://localhost:3306/minesweep";
String usernameSQL="root";
String password="langbbmm";
Class.forName(driverClass).newInstance();
Connection conn=DriverManager.getConnection(url, usernameSQL, password);

// 申请两个类对象，用于对数据库的操作 
//Statement stmt = conn.createStatement();
PreparedStatement pStmt = conn.prepareStatement("insert into rank (phone_id,username,time) values(?,?,?)");

which_use = Integer.valueOf(request.getParameter("which_use"));

if(which_use == 1)
{	
	
	phone_id = request.getParameter("phone_id");
	out.println(phone_id);
	pStmt = conn.prepareStatement("select * from user where phone_id=?");
	pStmt.setString(1,phone_id);
	ResultSet rs = pStmt.executeQuery();
	if(rs.next()==false) 		//如果rs为空，说明数据库中还没有改用户注册，返回一个特殊值，让客户端进行注册 
	{
		IsRegister = "no";%> 
<jsp:forward page="noregister.jsp"></jsp:forward> 
<% 
	} 
	else		//如果不为空，则返回该用户三个难度的排名情况  
	{ 
		IsRegister = "yes"; 
		//获取简单难度的排行  
		pStmt = conn.prepareStatement("select * from rank_easy where phone_id=?"); 
		pStmt.setString(1,phone_id); 
		ResultSet rsEasy = pStmt.executeQuery(); 
		if(rsEasy.isBeforeFirst() == rsEasy.isAfterLast()) 
		{ 
			request.setAttribute("easyTime",0); 
			request.setAttribute("easyRank",0); 
		} 
		else 
		{ 
			while(rsEasy.next()) 
			{ 
				request.setAttribute("easyTime",rsEasy.getInt(2)); 
				request.setAttribute("easyRank",rsEasy.getInt(3)); 
			} 
		} 
		rsEasy.close(); 
		//获取中等难度的排行   
		pStmt = conn.prepareStatement("select * from rank_normal where phone_id=?"); 
		pStmt.setString(1,phone_id); 
		ResultSet rsNormal = pStmt.executeQuery(); 
		if(rsNormal.isBeforeFirst() == rsNormal.isAfterLast()) 
		{ 
			request.setAttribute("normalTime",0); 
			request.setAttribute("normalRank",0); 
		} 
		else 
		{ 
		while(rsNormal.next()) 
			{ 
				request.setAttribute("normalTime",rsNormal.getInt(2)); 
				request.setAttribute("normalRank",rsNormal.getInt(3)); 
			} 
		} 
		rsNormal.close(); 
		//获取困难难度的排行   
		pStmt = conn.prepareStatement("select * from rank_hard where phone_id=?"); 
		pStmt.setString(1,phone_id); 
		ResultSet rsHard = pStmt.executeQuery(); 
		if(rsHard.isBeforeFirst() == rsHard.isAfterLast()) 
		{ 
			request.setAttribute("hardTime",0); 
			request.setAttribute("hardRank",0); 
		} 
		else 
		{ 
			while(rsHard.next()) 
			{ 
				request.setAttribute("hardTime",rsHard.getInt(2)); 
				request.setAttribute("hardRank",rsHard.getInt(3)); 
			} 
		} 
	rsHard.close();} 
	request.setAttribute("phone_id",phone_id); 
	request.setAttribute("isRegister",IsRegister); 
 
 	rs.close(); 
 	%> 
<jsp:forward page="splashxml.jsp"></jsp:forward> 
<%	 
} 
 
else if(which_use == 2)		//保存用户的账号申请  
{ 
	//将用户信息的账号信息保存到数据库中  
	phone_id = request.getParameter("phone_id"); 
	username = request.getParameter("username"); 
	pStmt = conn.prepareStatement("insert into user(phone_id,username) values(?,?)"); 
	pStmt.setString(1,phone_id); 
	pStmt.setString(2,username); 
	int rtn = pStmt.executeUpdate(); 
	//如果注册成功，则返回想客户端返回success，否则返回fail 
	if(rtn > 0) 
	{ 
%> 
<jsp:forward page="registerReturn.jsp"></jsp:forward> 
<%	} 
	else 
	{%> 
<jsp:forward page="registerFailed.jsp"></jsp:forward> 
<%	} 
} 
 
else if(which_use == 3)			//记录用户成绩的申请  
{ 
	phone_id = request.getParameter("phone_id"); 
	time = Integer.valueOf(request.getParameter("time")); 
	level = request.getParameter("level"); 
	 
	//简单难度游戏排行记录更新  
	if(level.equals("easy")) 
	{ 
		//查询用户当前的排名情况  
		pStmt = conn.prepareStatement("select * from rank_easy where phone_id=?"); 
		pStmt.setString(1,phone_id); 
		ResultSet rs_now = pStmt.executeQuery(); 
		if(rs_now.isAfterLast() == rs_now.isBeforeFirst()) 
		{ 
			ranking_now = 0; 
			time_now = -1; 
		} 
		else 
		{ 
			while(rs_now.next()) 
			{ 
				ranking_now = rs_now.getInt(3); 
				time_now = rs_now.getInt(2); 
			} 
		} 
		if(ranking_now == 0) 
			pStmt = conn.prepareStatement("select * from rank_easy order by ranking desc");  
		else 
		{  
			pStmt = conn.prepareStatement("select * from rank_easy where time<=? order by ranking desc"); 
			pStmt.setInt(1,time); 
		}	 
		ResultSet rs = pStmt.executeQuery(); 
		 
		 
			if(rs.isAfterLast()==rs.isBeforeFirst()) 
			{ 
				System.out.println("test"); 
				ranking = 1; 
				if(ranking_now == 0) 
				{ 
					pStmt = conn.prepareStatement("insert into rank_easy(phone_id,time,ranking) values (?,?,?)"); 
					pStmt.setString(1,phone_id); 
					pStmt.setInt(2,time); 
					pStmt.setInt(3,ranking); 
					int r = pStmt.executeUpdate(); 
				} 
				else 
				{ 
					pStmt = conn.prepareStatement("update rank_easy set ranking=?,time=? where phone_id=?"); 
					pStmt.setInt(1,ranking); 
					pStmt.setInt(2,time); 
					pStmt.setString(3,phone_id); 
					int rtn = pStmt.executeUpdate(); 
				} 
				ranking_now = -1; 
			} 
		 
			else 
			{ 
				int userAll=0;			//记录表中一个有多少条记录  
				int ranking_before=0;	//上一个比当前用户排行差的排名  
				pStmt= conn.prepareStatement("select count(*) from rank_easy where time<=?"); 
				pStmt.setInt(1,time); 
				ResultSet rs2 = pStmt.executeQuery(); 
				while(rs2.next()) 
				{ 
					userAll = rs2.getInt(1); 
				} 
				rs2.close(); 
				while(rs.next()) 
				{ 
					if(time < rs.getInt(2)) 
					{ 
						ranking = rs.getInt(3); 
						ranking_before=rs.getInt(3); 
						pStmt = conn.prepareStatement("update rank_easy set ranking=? where phone_id=?"); 
						pStmt.setInt(1,rs.getInt(3)+1); 
						pStmt.setString(2,rs.getString(1)); 
						int rtn = pStmt.executeUpdate(); 
					} 
					else if(time == rs.getInt(2)) 
					{ 
						ranking = rs.getInt(3); 
						break; 
					} 
					else if(time > rs.getInt(2)) 
					{ 
						if(ranking_before==0) 
							ranking = userAll+1; 
						else 
							ranking = ranking_before; 
						break; 
					} 
				} 
			} 
		 
		 
			//更新用户排名数据库，将新用户的成绩插入数据库 ,若用户的新成绩排行小于之前的记录，才更新其成绩  
		if(ranking <= ranking_now) 
		{ 
			pStmt = conn.prepareStatement("update rank_easy set ranking=?,time=? where phone_id=?"); 
			pStmt.setInt(1,ranking); 
			pStmt.setInt(2,time); 
			pStmt.setString(3,phone_id); 
			int rtn = pStmt.executeUpdate(); 
		} 
		else if(ranking_now==0) 
		{ 
			pStmt = conn.prepareStatement("insert into rank_easy(phone_id,time,ranking) values(?,?,?)"); 
			pStmt.setString(1,phone_id); 
			pStmt.setInt(2,time); 
			pStmt.setInt(3,ranking); 
			int i = pStmt.executeUpdate(); 
		} 
		rs.close(); 
		rs_now.close(); 
		 
		//提取排行数据，向客户端发送 
		request.setAttribute("ranking", ranking); 
		request.setAttribute("time", time); 
		request.setAttribute("ranking_now",ranking_now); 
		request.setAttribute("time_now", time_now); 
		%> 
	<jsp:forward page="gamexml.jsp"></jsp:forward> 
	<%} 
	//中等难度排行记录更新  
	else if(level.equals("normal"))		 
	{ 
		//查询用户当前的排名情况  
		pStmt = conn.prepareStatement("select * from rank_normal where phone_id=?"); 
		pStmt.setString(1,phone_id); 
		ResultSet rs_now = pStmt.executeQuery(); 
		while(rs_now.next()) 
		{ 
			ranking_now = rs_now.getInt(3); 
			time_now = rs_now.getInt(2); 
		} 
		if(ranking_now == 0) 
		{ 
			pStmt = conn.prepareStatement("select * from rank_normal order by ranking desc");  
		} 
		else 
		{  
			pStmt = conn.prepareStatement("select * from rank_normal where time<=? order by ranking desc"); 
			pStmt.setInt(1,time_now); 
		}	 
		ResultSet rs = pStmt.executeQuery(); 
		 
		 
			if(rs.isAfterLast()==rs.isBeforeFirst()) 
			{ 
				out.print("test"); 
				ranking = 1; 
				if(ranking_now == 0) 
				{ 
					pStmt = conn.prepareStatement("insert into rank_normal(phone_id,time,ranking) values (?,?,?)"); 
					pStmt.setString(1,phone_id); 
					pStmt.setInt(2,time); 
					pStmt.setInt(3,ranking); 
					int r = pStmt.executeUpdate(); 
				} 
				else 
				{ 
					pStmt = conn.prepareStatement("update rank_normal set ranking=?,time=? where phone_id=?"); 
					pStmt.setInt(1,ranking); 
					pStmt.setInt(2,time); 
					pStmt.setString(3,phone_id); 
					int rtn = pStmt.executeUpdate(); 
				} 
				ranking_now = -1; 
			} 
		 
			else 
			{ 
				int userAll=0;			//记录表中一个有多少条记录  
				int ranking_before=0;	//上一个比当前用户排行差的排名  
				pStmt= conn.prepareStatement("select count(*) from rank_normal where time<=?"); 
				pStmt.setInt(1,time); 
				ResultSet rs2 = pStmt.executeQuery(); 
				while(rs2.next()) 
				{ 
					userAll = rs2.getInt(1); 
				} 
				rs2.close(); 
				while(rs.next()) 
				{ 
					if(time < rs.getInt(2)) 
					{ 
						out.print(rs.getString(1)); 
						out.print(rs.getInt(2)); 
						out.print(ranking_now); 
						ranking = rs.getInt(3); 
						ranking_before=rs.getInt(3); 
						pStmt = conn.prepareStatement("update rank_normal set ranking=? where phone_id=?"); 
						pStmt.setInt(1,rs.getInt(3)+1); 
						pStmt.setString(2,rs.getString(1)); 
						int rtn = pStmt.executeUpdate(); 
					} 
					else if(time == rs.getInt(2)) 
					{ 
						ranking = rs.getInt(3); 
						break; 
					} 
					else if(time > rs.getInt(2)) 
					{ 
						if(ranking_before==0) 
						{ 
						out.print("test10+   "); 
							ranking = userAll+1; 
						} 
						else 
						{ 
							ranking = ranking_before; 
						} 
						break; 
					} 
				} 
			} 
		 
		 
			//更新用户排名数据库，将新用户的成绩插入数据库 ,若用户的新成绩排行小于之前的记录，才更新其成绩  
		if(ranking <= ranking_now) 
		{ 
			pStmt = conn.prepareStatement("update rank_normal set ranking=?,time=? where phone_id=?"); 
			pStmt.setInt(1,ranking); 
			pStmt.setInt(2,time); 
			pStmt.setString(3,phone_id); 
			int rtn = pStmt.executeUpdate(); 
		} 
		else if(ranking_now==0) 
		{ 
			pStmt = conn.prepareStatement("insert into rank_normal(phone_id,time,ranking) values(?,?,?)"); 
			pStmt.setString(1,phone_id); 
			pStmt.setInt(2,time); 
			pStmt.setInt(3,ranking); 
			int i = pStmt.executeUpdate(); 
		} 
		rs.close(); 
		rs_now.close(); 
				//提取排行数据，向客户端发送 
		request.setAttribute("ranking", ranking); 
		request.setAttribute("time", time); 
		request.setAttribute("ranking_now",ranking_now); 
		request.setAttribute("time_now", time_now); 
	%> 
<jsp:forward page="gamexml.jsp"></jsp:forward> 
<% 
	} 
	//困难难度排行记录更新  
	else if(level.equals("hard"))	 
	{ 
		//查询用户当前的排名情况  
		pStmt = conn.prepareStatement("select * from rank_hard where phone_id=?"); 
		pStmt.setString(1,phone_id); 
		ResultSet rs_now = pStmt.executeQuery(); 
		while(rs_now.next()) 
		{ 
			ranking_now = rs_now.getInt(3); 
			time_now = rs_now.getInt(2); 
		} 
		if(ranking_now == 0) 
			pStmt = conn.prepareStatement("select * from rank_hard order by ranking desc");  
		else 
		{  
			pStmt = conn.prepareStatement("select * from rank_hard where time<=? order by ranking desc"); 
			pStmt.setInt(1,time_now); 
		}	 
		ResultSet rs = pStmt.executeQuery(); 
		 
		 
			if(rs.isAfterLast()==rs.isBeforeFirst()) 
			{ 
				ranking = 1; 
				if(ranking_now == 0) 
				{ 
					pStmt = conn.prepareStatement("insert into rank_hard(phone_id,time,ranking) values (?,?,?)"); 
					pStmt.setString(1,phone_id); 
					pStmt.setInt(2,time); 
					pStmt.setInt(3,ranking); 
					int r = pStmt.executeUpdate(); 
				} 
				else 
				{ 
					pStmt = conn.prepareStatement("update rank_hard set ranking=?,time=? where phone_id=?"); 
					pStmt.setInt(1,ranking); 
					pStmt.setInt(2,time); 
					pStmt.setString(3,phone_id); 
					int rtn = pStmt.executeUpdate(); 
				} 
				ranking_now = -1; 
			} 
		 
			else 
			{ 
				int userAll=0;			//记录表中一个有多少条记录  
				int ranking_before=0;	//上一个比当前用户排行差的排名  
				pStmt= conn.prepareStatement("select count(*) from rank_hard where time<=?"); 
				pStmt.setInt(1,time); 
				ResultSet rs2 = pStmt.executeQuery(); 
				while(rs2.next()) 
				{ 
					userAll = rs2.getInt(1); 
				} 
				rs2.close(); 
				while(rs.next()) 
				{ 
					System.out.print("sha"); 
					System.out.print("time:"+rs.getInt(2)); 
					if(time < rs.getInt(2)) 
					{ 
						ranking = rs.getInt(3); 
						ranking_before=rs.getInt(3); 
						pStmt = conn.prepareStatement("update rank_hard set ranking=? where phone_id=?"); 
						pStmt.setInt(1,rs.getInt(3)+1); 
						pStmt.setString(2,rs.getString(1)); 
						int rtn = pStmt.executeUpdate(); 
					} 
					else if(time == rs.getInt(2)) 
					{ 
						ranking = rs.getInt(3); 
						break; 
					} 
					else if(time > rs.getInt(2)) 
					{ 
						if(ranking_before==0) 
							ranking = userAll+1; 
						else 
							ranking = ranking_before; 
						break; 
					} 
				} 
			} 
		 
		 
			//更新用户排名数据库，将新用户的成绩插入数据库 ,若用户的新成绩排行小于之前的记录，才更新其成绩  
		if(ranking <= ranking_now) 
		{ 
			pStmt = conn.prepareStatement("update rank_hard set ranking=?,time=? where phone_id=?"); 
			pStmt.setInt(1,ranking); 
			pStmt.setInt(2,time); 
			pStmt.setString(3,phone_id); 
			int rtn = pStmt.executeUpdate(); 
		} 
		else if(ranking_now==0) 
		{ 
			pStmt = conn.prepareStatement("insert into rank_hard(phone_id,time,ranking) values(?,?,?)"); 
			pStmt.setString(1,phone_id); 
			pStmt.setInt(2,time); 
			pStmt.setInt(3,ranking); 
			int i = pStmt.executeUpdate(); 
		} 
		rs.close(); 
		rs_now.close(); 
				//提取排行数据，向客户端发送 
		request.setAttribute("ranking", ranking); 
		request.setAttribute("time", time); 
		request.setAttribute("ranking_now",ranking_now); 
		request.setAttribute("time_now", time_now); 
	%> 
<jsp:forward page="gamexml.jsp"></jsp:forward> 
<% 
	}	 
	//返回用户当前的排名 	 
} 
 
else if(which_use==4)		//客户端需要获取网络排行榜  
{ 
	level = request.getParameter("level"); 
	if(level.equals("easy")) 
	{ 
%> 
<jsp:forward page="easy_rank.jsp"></jsp:forward> 
<%	} 
	else if(level.equals("normal")) 
	{ 
%> 
<jsp:forward page="normal_rank.jsp"></jsp:forward> 
<%  } 
	else if(level.equals("hard")) 
	{ 
%> 
<jsp:forward page="hard_rank.jsp"></jsp:forward> 
</div><%	}
}
pStmt.close();
conn.close();
%>

