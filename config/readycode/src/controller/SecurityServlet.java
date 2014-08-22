package controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.SessionService;

import model.User;

public class SecurityServlet extends HttpServlet implements Filter{

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request=(HttpServletRequest)arg0;     
		HttpServletResponse response  =(HttpServletResponse) arg1;      
		HttpSession session = request.getSession(true);       
		
		//String usercode = (String) request.getRemoteUser();// 登录人  
		//String user_role = (String)session.getAttribute("role");//登录人角色  
		String url=request.getRequestURI();     
		Object user = session.getAttribute(SessionService.USER_KEY);
		
		if( user == null || !(user instanceof User) ) {        
			//判断获取的路径不为空且不是访问登录页面或执行登录操作时跳转     
			if(url!=null && !url.equals("") && ( url.indexOf("Login")<0 && url.indexOf("login")<0 )){     
		    	response.sendRedirect(request.getContextPath() + "/frame/login.jsp"); 
				//response.sendRedirect("http://www.baidu.com/");  
		        return ;     
		    }                
		}     
		
		arg2.doFilter(arg0, arg1);     
		return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
