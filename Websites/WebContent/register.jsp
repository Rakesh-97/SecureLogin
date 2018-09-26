<%@page import="java.sql.ResultSet"%>
<%@page import="com.sun.java.swing.plaf.windows.resources.windows"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Statement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
String fName=request.getParameter("fname");

String url = "jdbc:mysql://localhost:3306/registerdemo";
String un = "root";
String password = "";
Class.forName ("com.mysql.jdbc.Driver");
Connection conn = DriverManager.getConnection(url, un, password);
String passwordToHash=fName;
String salt="india";
String generatedPassword = null;
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++){
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        generatedPassword = sb.toString();
    }
    catch (NoSuchAlgorithmException e){
        e.printStackTrace();
    }
session.setAttribute("userIdHash", generatedPassword);
String query = "select * from demoregister where name = '"+generatedPassword+"' " ;
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(query);
if(rs.absolute(1)) {
	String queryy = "update demoregister set count='1'where name = '"+generatedPassword+"' " ;
	Statement stmtt = conn.createStatement();
	stmtt.executeUpdate(queryy);
	response.sendRedirect("http://localhost:8080/Web_pages/succes.jsp");
}
else
{
	response.sendRedirect("http://localhost:8080/myapp/unsucces.html");	
}
%>

</body>
</html>