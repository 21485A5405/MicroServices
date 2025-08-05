package com.example.service;

import java.util.List;
import org.springframework.http.ResponseEntity;

import com.example.controller.ApiResponse;
import com.example.dto.LoginDetails;
import com.example.dto.OrderDto;
import com.example.dto.ProductDto;
import com.example.dto.RegisterAdmin;
import com.example.dto.UpdateUser;
import com.example.model.User;

public interface AdminService {
	
	public ResponseEntity<ApiResponse<User>> createAdmin(RegisterAdmin admin) ;

	public ResponseEntity<ApiResponse<User>> getAdminById(Long adminId);

	public ResponseEntity<ApiResponse<User>> updateAdminById(Long adminId, UpdateUser newAdmin);

	public ResponseEntity<ApiResponse<User>> deleteAdminById(Long adminId);

	public ResponseEntity<ApiResponse<List<User>>> getAllAdmins();

	List<Long> getAllUserIds();

	public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders();

	public ResponseEntity<ApiResponse<List<User>>> getAllUsers();

	public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts();
	
	public List<Long> getAllProductIds();

	public ResponseEntity<ApiResponse<?>> loginAdmin(LoginDetails details);

}
