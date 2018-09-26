<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Set"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<html>
<head>
<link rel="stylesheet" href = "bootstrap/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Logout Page</title>
</head>
<body>
<form action="./login.jsp" method="post" align="center">
<br>
<p class="text-primary">
Succesfully Log Out!
</p>
<button type="submit" class="btn btn-primary">Login Again</button>
</form>
</body>
</html>

<%

	String url = "jdbc:mysql://localhost:3306/registerdemo";
	String un = "root";
	String password = "";
	Class.forName ("com.mysql.jdbc.Driver");
	Connection conn = DriverManager.getConnection(url, un, password);
	String query = "update demoregister set isAut = '0' , count = '0' where name = '"+ session.getAttribute("userIdHash") +"' and count='1' ";
    Statement stmt = conn.createStatement();
    stmt.executeUpdate(query);

%>