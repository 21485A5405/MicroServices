package com.example.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.dto.ProductDto;

@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductClient {

	@GetMapping("/products/get-all-productsbyid")
	public List<Long> getAllProductIds();
	
	@GetMapping("/products/getall")
	public List<ProductDto> getAllproducts();
}
