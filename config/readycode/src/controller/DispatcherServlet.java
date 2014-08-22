package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet{

	public void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		
		//request.setCharacterEncoding("GB2312");
		request.setCharacterEncoding("UTF-8");
		super.service(request, response);
	}
}
