<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Set"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "bootstrap/css/bootstrap.min.css">
<title>Wait a Minute</title>
</head>
<body>
<p class="text-primary" align="center">Hello User. <br>
Please Open the LoginSecure App in your android Device and enter Your correct PIN and Password Image for Authentication.<br>
Thank You!
</p>

<%

String url = "jdbc:mysql://localhost:3306/registerdemo";
String un = "root";
String password = "";
Class.forName ("com.mysql.jdbc.Driver");
Connection conn = DriverManager.getConnection(url, un, password);
String query = "select * from demoregister where name = '"+session.getAttribute("userIdHash")+"'  and isAut = '1' ";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(query);
if(rs.absolute(1))
{
	response.sendRedirect("http://localhost:8080/Web_pages/serviceGet.jsp");
}
else
{
	//response.sendRedirect("http://localhost:8080/myapp/succes.jsp");
}

%>
<p class="text-primary" align="center">
Please Refresh the page after the succesfully Autentication in LoginSecure App.
</p>
</body>
</html>