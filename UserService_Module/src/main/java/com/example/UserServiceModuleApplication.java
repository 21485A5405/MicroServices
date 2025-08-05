package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.clients")
@EnableDiscoveryClient
public class UserServiceModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceModuleApplication.class, args);
	}

}
