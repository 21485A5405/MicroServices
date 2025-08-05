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

import com.example.dto.RegisterAdmin;
import com.example.dto.LoginDetails;
import com.example.dto.OrderDto;
import com.example.dto.ProductDto;
import com.example.dto.UpdateUser;
import com.example.model.User;
import com.example.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admins")
public class AdminController {
	

	private AdminService adminService;
	
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@PostMapping("/register-admin")
	public ResponseEntity<ApiResponse<User>> RegisterAdmin(@Valid @RequestBody RegisterAdmin admin) {
		return adminService.createAdmin(admin);
	}
	
	@GetMapping("/get-adminbyid/{adminId}")
	public ResponseEntity<ApiResponse<User>> getAdmin(@PathVariable Long adminId) {
		return adminService.getAdminById(adminId);
	}
	
	@GetMapping("/get-all-admins")
	public ResponseEntity<ApiResponse<List<User>>> getAdmin() {
		return adminService.getAllAdmins();
	}
	
	@GetMapping("/get-all-usersbyid")
	public List<Long> getUsers() {
		return adminService.getAllUserIds();
	}
	
	@GetMapping("/get-all-products")
	public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
		return adminService.getAllProducts();
	}
	
	@GetMapping("/get-all-users")
	public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
		return adminService.getAllUsers();
	}
	
	@GetMapping("/get-all-productsbyid")
	public List<Long> getProducts() {
		return adminService.getAllProductIds();
	}
	
	@PutMapping("/update-admin/{adminId}")
	public ResponseEntity<ApiResponse<User>> updateAdmin(@PathVariable Long adminId, @RequestBody UpdateUser newAdmin) {
		return adminService.updateAdminById(adminId, newAdmin);
	}

	@DeleteMapping("/delete-adminbyid/{adminId}")
	public ResponseEntity<ApiResponse<User>> deleteAdmin(@PathVariable Long adminId) {
		return adminService.deleteAdminById(adminId);
	}
	
	@GetMapping("/get-all-orders")
	public ResponseEntity<ApiResponse<List<OrderDto>>> getAll() {
		return adminService.getAllOrders();
	}
	
	@PostMapping("/login-admin")
	public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginDetails details) {
		return adminService.loginAdmin(details);
	}
}