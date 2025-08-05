package com.example.clients;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.dto.ProductDto;

@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductClient {
	
	@GetMapping("/products/get-product-by-id/{productId}")
	Optional<ProductDto> getProduct(@PathVariable Long productId);
}
