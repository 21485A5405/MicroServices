package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.clients")
public class UserServiceModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceModuleApplication.class, args);
	}

}
