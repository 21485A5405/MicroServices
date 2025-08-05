package com.example.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.customannotations.ForProduct;
import com.example.enums.AdminPermissions;
import com.example.enums.Role;
import com.example.model.Product;
import com.example.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	
	private ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/add-product")
	@ForProduct(validPermissions = { AdminPermissions.Manager, AdminPermissions.Product_Manager},requiredRole = Role.ADMIN)
	public ResponseEntity<ApiResponse<Product>> addProduct(@RequestBody Product product) {		
		return productService.saveProduct(product);
	}
	
	@PutMapping("/update/{productId}")
	@ForProduct(validPermissions = {AdminPermissions.Manager,AdminPermissions.Product_Manager},requiredRole = Role.ADMIN)
	public ResponseEntity<ApiResponse<Product>> updateProductById(@PathVariable Long productId, @RequestBody Product product) {
		return productService.productUpdate(productId, product);
		
	}
	
	@GetMapping("/get-product-by-id/{productId}")
	public ResponseEntity<ApiResponse<Product>> getById(@PathVariable Long productId) {		
		return productService.getProductById(productId);
	}
	
	@DeleteMapping("/delete-by-id/{productId}")
	@ForProduct(validPermissions = {AdminPermissions.Manager, AdminPermissions.Product_Manager},requiredRole = Role.ADMIN)
	public ResponseEntity<ApiResponse<Product>> deleteById(@PathVariable Long productId) {
		return productService.deleteById(productId);
	}
	
	@GetMapping("/get-product-by-category/{category}")
	public ResponseEntity<ApiResponse<List<Product>>> getProductByCategory(@PathVariable String category) {
		return productService.getProductByCategory(category);
	}
	
	@GetMapping("/getall")
	public ResponseEntity<ApiResponse<List<Product>>> displayAllProducts() {	 
		return productService.displayAllProducts();
	}
	
	@GetMapping("/get-all-productsbyid")
	public List<Long> getProducts() {
		return productService.getAllProductIds();
	}
	
	@GetMapping("/get-product-by-range/{category}/{minPrice}/{maxPrice}")
	public ResponseEntity<ApiResponse<List<Product>>> getProductByPrice(@PathVariable String category, @PathVariable double minPrice, @PathVariable double maxPrice) {
		return productService.getProductBetweenPrice(category, minPrice, maxPrice);
	}
	
	@PutMapping("decrease-quantity/{productId}/{newQuantity}")
	public void decreaseQuantity(@PathVariable Long productId, int newQuantity) {
		productService.decreaseQuantity(productId, newQuantity);
	}
	
	@PutMapping("increase-quantity/{productId}/{newQuantity}")
	public void increaseQuantity(@PathVariable Long productId, int newQuantity) {
		productService.increaseQuantity(productId, newQuantity);
	}
	
	
}
