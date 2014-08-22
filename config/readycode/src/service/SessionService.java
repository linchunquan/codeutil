package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.User;
import bean.ValidateText;

public interface SessionService {

	public static String USER_KEY = "USER_KEY";
	
	public static String USER_JSON = "USER_JSON";
	
	boolean userLogin(HttpServletRequest request, String userName, String password, ValidateText validateText);
	
	void userLogout(HttpServletRequest request);
	
	User getCurrentUser(HttpServletRequest request);
}
