package controller;

import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;

import org.springframework.web.servlet.ModelAndView;


import service.SessionService;

import bean.ValidateText;

public class ExtendedCommandHandleController extends CommandHandleController{

	private SessionService sessionService;
	
	@Override
	protected ModelAndView handleRequestInternalForExtention(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		String cmd=request.getParameter("cmd");
		//System.out.println("cmd:"+cmd);
		if(ExtendedCommandType.cmdMap.containsKey(cmd)){
			if(cmd!=null){
				switch(ExtendedCommandType.cmdMap.get(cmd)){
					case ExtendedCommandType.login:login(request,response);break;
					case ExtendedCommandType.logout:logout(request,response);break;
					case ExtendedCommandType.changePassword:changePassword(request,response);break;
				}
			}
		}
		
		return null;
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		
		ValidateText validateText = new ValidateText();
		
		boolean validate = this.sessionService.userLogin(request, userName, password, validateText);

		renderJSON(response,JSONHelper.basicObjectToJson(validateText));
	}

	private void logout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		System.out.println("logout...");
		
		this.sessionService.userLogout(request);
		
		String json = "success";
		renderJSON(response,json);
	}
	
	private void changePassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		System.out.println("changePassword...");
		
		String password = request.getParameter("password");
		User user = this.sessionService.getCurrentUser(request);
		user.setPassword(password);
		this.getCompositeDao().getUserDao().saveOrUpdateUser(user);
		String json = "success";
		renderJSON(response,json);
	}
	
	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
}
