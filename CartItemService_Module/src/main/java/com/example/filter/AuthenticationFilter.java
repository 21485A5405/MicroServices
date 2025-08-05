package com.example.filter;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.authentication.CurrentUser;
import com.example.clients.UserClient;
import com.example.dto.UserDto;
import com.example.exception.UnAuthorizedException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {


    private UserClient userClient;
    private CurrentUser currentUser;
    
    public AuthenticationFilter(UserClient userClient, CurrentUser currentUser) {
    	this.userClient = userClient;
    	this.currentUser = currentUser;
    }

    public AuthenticationFilter() {
    	
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

    	try {
	        String token = request.getHeader("Authorization");
	        System.out.println("Token received: " + token);
	        if (token != null && !token.isBlank()) {
	        	UserDto user = userClient.validateToken(token);
	            currentUser.setUser(user);
	        }
	        filterChain.doFilter(request, response);
    	}catch (UnAuthorizedException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.getWriter().write("{ \"Message\": \"" + ex.getMessage() + "\" }");
        }
    }
}

