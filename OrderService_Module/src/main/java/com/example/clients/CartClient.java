package com.example.clients;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.dto.CartDto;

@FeignClient(name = "cart-service", url = "http://localhost:8083")
public interface CartClient {
	
	@GetMapping("/carts/get-by-user-and-product/{userId}/{productId}")
	Optional<CartDto> getUserCart(@PathVariable Long userId, @PathVariable Long productId);

	@DeleteMapping("/carts/delete-cart/{userId}/{productId}")
	void deleteCart(@PathVariable Long userId, @PathVariable Long productId);

	@PutMapping("/carts/update-cart/{userId}/{productId}/{newQuantity}")
	Optional<CartDto> updateCartQuantity(@PathVariable Long userId, @PathVariable Long productId, @PathVariable int newQuantity);

}
