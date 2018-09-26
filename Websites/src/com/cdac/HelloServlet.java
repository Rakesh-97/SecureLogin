package com.cdac;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet { 
 
    public HelloServlet() {
        super();
 
    }
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        response.getOutputStream().println("Hurray !! This Servlet Works");
 
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {}
 
}
