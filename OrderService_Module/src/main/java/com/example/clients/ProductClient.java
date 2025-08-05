package com.example.clients;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.dto.ProductDto;

@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductClient {
	
	@GetMapping("/products/get-product-by-id/{productId}")
	Optional<ProductDto> getProductById(@PathVariable Long productId);
	
	@PutMapping("/products/decrease-quantity/{productId}/{newQuantity}")
	void decreaseQuantity(@PathVariable Long productId, @PathVariable int newQuantity);
	
	@PutMapping("/products/increase-quantity/{productId}/{newQuantity}")
	void increaseQuantity(@PathVariable Long productId, @PathVariable int newQuantity);


	
}
