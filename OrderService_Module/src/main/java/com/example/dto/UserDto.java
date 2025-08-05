package com.example.dto;

import java.util.List;
import java.util.Set;

import com.example.enums.AdminPermissions;
import com.example.enums.Role;
import com.example.model.Address;
import com.example.model.PaymentInfo;

public class UserDto {

	private Long userId;
	private String userEmail;
	private String userPassword;
	private Role userRole;
	private List<Address> shippingAddress;
	
	public List<Address> getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(List<Address> shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public UserDto(Long userId, String userPassword, Role userRole) {
		super();
		this.userId = userId;
		this.userPassword = userPassword;
		this.userRole = userRole;
	}
	
	
	private Set<AdminPermissions> userPermissions;
	
	public Set<AdminPermissions> getUserPermissions() {
		return userPermissions;
	}
	public void setUserPermissions(Set<AdminPermissions> userPermissions) {
		this.userPermissions = userPermissions;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Role getUserRole() {
		return userRole;
	}
	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}
	List<PaymentInfo> paymentDetails;
	
	public List<PaymentInfo> getPaymentDetails() {
		return paymentDetails;
	}
	public void setPaymentDetails(List<PaymentInfo> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String displayPayments() {
	    StringBuilder builder = new StringBuilder();
	    if (paymentDetails != null) {
	        for (PaymentInfo info : paymentDetails) {
	            builder.append(info.getPaymentMethod()).append(" - ")
	                   .append(info.getAccountDetails()).append(", ");   
	        }
	    }
	    return builder.toString();
	}

}
