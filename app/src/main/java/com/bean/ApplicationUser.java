package com.bean;

public class ApplicationUser {
	public int user_flag;
	public Long user_id;
	public String user_pwd;
	public String action;
	
	public int getUser_flag(){
		return user_flag;
	}
	
	public void setUser_flag(int user_flag){
		this.user_flag = user_flag;
	}
	
	public Long getUser_id(){
		return user_id;
	}
	
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUser_pwd(){
		return user_pwd;
	}
	
	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public String getAction(){
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
}
