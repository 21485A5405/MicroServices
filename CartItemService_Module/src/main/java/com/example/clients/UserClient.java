package com.example.clients;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.dto.UserDto;

@FeignClient(name = "user-service", url = "http://localhost:8084")
public interface UserClient {
	
	@GetMapping("/users/get-user-by-id/{userId}")
	Optional<UserDto> getUser(@PathVariable Long userId);
	
    @GetMapping("/users/get-user-token/{userToken}")
    UserDto validateToken(@PathVariable String userToken);


	
}
