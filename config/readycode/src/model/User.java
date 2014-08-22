package model;

import model.annotation.ExtField;

public class User {

	private long id;
	
	@ExtField(fieldName="用户名称")
	private String userName = "";
	
	@ExtField(fieldName="登录密码")
	private String password = "";
	
	@ExtField(fieldName="所属部门")
	private String department = "";
	
	@ExtField(fieldName="用户角色",optValues="[[\"系统管理员\",\"系统管理员\"],[\"系统操作员\",\"系统操作员\"]]")
	private String roles = "";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
}
