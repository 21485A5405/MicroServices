package com.example.dto;

import java.util.List;

import com.example.enums.Role;

public class UserDto {
	
	private Long userId;
	private String userEmail;
	private String userPassword;
	private Role userRole;
	
	public UserDto(Long userId, String userPassword, Role userRole) {
		super();
		this.userId = userId;
		this.userPassword = userPassword;
		this.userRole = userRole;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Role getUserRole() {
		return userRole;
	}
	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
