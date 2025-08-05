package com.example.authentication;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.example.dto.UserDto;


@Component
@RequestScope
public class CurrentUser {

    private UserDto currentUser;
   
	public UserDto getUser() { 
    	
    	return currentUser;
    }
    public void setUser(UserDto currentUser) {
    	this.currentUser = currentUser; 
	}
}
