package service.impl;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.User;

import bean.ValidateText;
import service.SessionService;
import dao.CompositeDao;

public class SessionServiceImpl implements SessionService{

	private CompositeDao compositeDao;
	
	public CompositeDao getCompositeDao() {
		return compositeDao;
	}

	public void setCompositeDao(CompositeDao compositeDao) {
		this.compositeDao = compositeDao;
	}

	@Override
	public boolean userLogin(HttpServletRequest request, String userName,
			String password, ValidateText validateText) {
		// TODO Auto-generated method stub
		validateText.setUserNameValidateText("");
		validateText.setPasswordValidateText("");
		
		User user = compositeDao.getComplexQuerier().getUserByName(userName);
		if(user==null){
			validateText.setUserNameValidateText("用户名不存在");
			return false;
		}
		else if(!user.getPassword().equals(password)){
			validateText.setPasswordValidateText("登录密码不正确");
			return false;
		}
		
		user.setPassword("");
		request.getSession().setAttribute(SessionService.USER_KEY, user);
		
		String userJson = "{id:"+user.getId()+", userName:'"+user.getUserName()+"', department:'"+user.getDepartment()+"', roles:'"+user.getRoles()+"',loginTime:"+(new Date()).getTime()+"}";
		request.getSession().setAttribute(SessionService.USER_JSON, userJson);
		
		return true;
	}

	@Override
	public void userLogout(HttpServletRequest request) {
		// TODO Auto-generated method stub
		request.getSession().removeAttribute(SessionService.USER_KEY);
	}
	
	public User getCurrentUser(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return (User)request.getSession().getAttribute(SessionService.USER_KEY);
	}
}
