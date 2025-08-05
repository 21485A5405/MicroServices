package com.example.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.dto.OrderDto;

@FeignClient(name = "order-service", url = "http://localhost:8082")
public interface OrderClient {
	
	@GetMapping("/orders/get-all-orders")
	public List<OrderDto> getAllOrders();

}
