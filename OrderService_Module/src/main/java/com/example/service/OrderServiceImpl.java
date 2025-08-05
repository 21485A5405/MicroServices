//package com.example.service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.example.advicemethods.IsAuthorized;
//import com.example.authentication.CurrentUser;
//import com.example.clients.CartItemClient;
//import com.example.clients.ProductClient;
//import com.example.clients.UserClient;
//import com.example.controller.ApiResponse;
//import com.example.dto.CartDto;
//import com.example.dto.PlaceOrder;
//import com.example.dto.ProductDto;
//import com.example.dto.UserDto;
//import com.example.enums.OrderStatus;
//import com.example.enums.PaymentStatus;
//import com.example.enums.Role;
//import com.example.exception.CustomException;
//import com.example.exception.ProductNotFoundException;
//import com.example.exception.UnAuthorizedException;
//import com.example.exception.UserNotFoundException;
//import com.example.model.*;
//import com.example.repo.AddressRepo;
//import com.example.repo.OrderRepo;
//
//import jakarta.transaction.Transactional;
//
//@Service
//public class OrderServiceImpl implements OrderService{
//
//	private OrderRepo orderRepo;
//	private AddressRepo addressRepo;
//	private CurrentUser currentUser;
//
//	private UserClient userClient;
//	private CartItemClient cartClient;
//	private ProductClient productClient;
//	
//	public OrderServiceImpl(UserClient userClient, ProductClient productClient, CartItemClient cartClient) {
//		this.userClient = userClient;
//		this.cartClient = cartClient;
//		this.productClient = productClient;
//	}
//
//	@Transactional
//	public ResponseEntity<ApiResponse<OrderProduct>> placeOrder(PlaceOrder orderDetails) {
//
//	    Optional<UserDto> findUser = userClient.getUser(orderDetails.getUserId());
//	    Optional<CartDto> cart = cartClient.getUserCart(orderDetails.getUserId(), orderDetails.getProductId());
//	    Optional<Address> addressExists = addressRepo.findById(orderDetails.getAddressId());
//	    Optional<ProductDto> findProduct = productClient.getProduct(orderDetails.getProductId());
//
//	    
//	    if (!userClient.isValid(orderDetails.getUserId())) {
//	        throw new UnAuthorizedException("Please Login");
//	    }
//	    if (!findUser.isPresent()) {
//	        throw new UserNotFoundException("User Not Found");
//	    }
//	    if (!currUser.getUserId().equals(orderDetails.getUserId())) {
//	        throw new UnAuthorizedException("Not Authorized to Place Order with Another User ID");
//	    }
//	    
//	    if (!cart.isPresent()) {
//	        throw new ProductNotFoundException("Please Add Product into Cart to place Order");
//	    }
//	    if(!addressExists.get().getUser().getUserId().equals(orderDetails.getUserId())) {
//	    	throw new CustomException("Address Not Matched");
//	    }
//
//	    CartDto cartItem = cart.get();
//	    if (cartItem.getProductQuantity() < orderDetails.getQuantity()) {
//	        throw new CustomException("Selected Quantity is Greater Than Your Cart Quantity");
//	    }
//
//	    List<PaymentInfo> payment = findUser.get().getPaymentDetails();
//	    if (payment == null || payment.isEmpty()) {
//	        throw new CustomException("Payment Method Cannot be Empty");
//	    }
//	    boolean isValid = false; 
//	    for (PaymentInfo info : payment) {
//	    	if (info.getPaymentMethod() == orderDetails.getPaymentType()) { 
//	    		isValid = true; 
//	    		break; 
//	    		} 
//	    	} 
//	    if (!isValid) { 
//	    	throw new UnAuthorizedException("Selected Payment Method Not Available. Available: " 
//	    					+ findUser.get().displayPayments()); 
//	    	}
//	    // Create order
//	    OrderProduct order = new OrderProduct();
//	    OrderItem orderItem = new OrderItem();
//	    UserDto user = findUser.get();
//	    ProductDto product = findProduct.get();
//	    Address address = addressExists.get();
//
//	    order.setUser(user);
//	    order.setOrderDate(LocalDateTime.now());
//	    order.setShippingAddress(address.getFullAddress());
//	    order.setOrderStatus(OrderStatus.PROCESSING);
//	    order.setPaymentStatus(PaymentStatus.PENDING);
//	    order.setTotalPrice(orderDetails.getQuantity() * product.getProductPrice());
//	    
//	    // Stock Checking
//	    int newStock = product.getProductQuantity() - orderDetails.getQuantity();
//	    if (newStock < 0) {
//	        throw new CustomException("Out Of Stock.");
//	    }
//	    productClient.decreaseQuantity(product.getProductId(), orderDetails.getQuantity());
//
//	    // Update current user's cart
//	    int remainingQty = cartItem.getProductQuantity() - orderDetails.getQuantity();
//	    if (remainingQty <= 0) {
//	        cartClient.deleteCart(cart.get().getUserId(), cart.get().getProductId());
//	    } else {
//	        cartItem.setProductQuantity(remainingQty);
//	        cartItem.setTotalPrice(remainingQty * product.getProductPrice());
//	    }
//
//	    orderItem.setOrder(order);
//	    orderItem.setProduct(product);
//	    orderItem.setQuantity(orderDetails.getQuantity());
//	    order.setItems(List.of(orderItem));
//	    orderRepo.save(order);
//
//	    ApiResponse<OrderProduct> response = new ApiResponse<>();
//	    response.setData(order);
//	    response.setMessage("Order Placed Successfully");
//	    return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<List<OrderProduct>>> getOrderByUser(Long userId) {
//		
//		List<OrderProduct> orders = orderRepo.findByUser(userId);
//		
//		UserDto currUser = currentUser.getUser();
//		if(currUser == null) {
//			throw new UnAuthorizedException("Please Login");
//		}
//		if(!userClient.getUser(userId).isPresent()) {
//			throw new UserNotFoundException("User Not Found");
//		}
//		
//		boolean isSelf = currUser.getUserId().equals(userId);
//		boolean isManager = IsAuthorized.isManager(currUser.getUserPermissions());
//		boolean isOrderManager = IsAuthorized.isOrderManager(currUser.getUserPermissions());
//		ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
//		if(isSelf || (isManager || isOrderManager)) {
//			response.setData(orders);
//			response.setMessage("User "+userId+" Orders Details");
//		}
//		else if (!isSelf && !(isManager || isOrderManager)) {
//		    throw new UnAuthorizedException("Not Authorized to View This User's Order Details");
//		}
//		else if (currUser.getUserRole() == Role.ADMIN && !(isManager || isOrderManager)) {
//		    throw new UnAuthorizedException("You don't have rights to view order details");
//		}
//		if(orders.isEmpty()) {
//			throw new UserNotFoundException("No Order Details Found with Given User ID");
//		}
//		
//		return ResponseEntity.ok(response);
//	}
//
//	@Transactional
//	public ResponseEntity<ApiResponse<OrderProduct>> cancelOrder(Long userId, Long productId, int quantity) {
//
//	    UserDto currUser = currentUser.getUser();
//		if(currUser == null) {
//			throw new UnAuthorizedException("Please Login");
//		}
//		if(currUser.getUserId()!= userId) {
//			throw new UnAuthorizedException("Not Authorized to Cancel Order With Another Account");
//		}
//		
//	    Optional<OrderProduct> orderExists = orderRepo.findByUserAndProductAndQuantity(userId, productId, quantity);
//	    if (!orderExists.isPresent()) {
//	        throw new ProductNotFoundException("No matching order found for user " + userId + " with product " 
//	        								+ productId + " and quantity " + quantity);
//	    }
//	    OrderProduct order = orderExists.get();
//
//	    OrderItem items = null;
//	    for (OrderItem item : order.getItems()) {
//	        if (item.getProduct().getProductId().equals(productId) && item.getQuantity() == quantity) {
//	            items = item;
//	            break;
//	        }
//	    }
//
//	    if (items == null) {
//	        throw new ProductNotFoundException("No matching item found in order for product " + productId);
//	    }
//	    productClient.increaseQuantity(productId, quantity);
//	    orderRepo.delete(order);
//
//	    ApiResponse<OrderProduct> response = new ApiResponse<>();
//	    response.setMessage("Order cancelled successfully");
//	    return ResponseEntity.ok(response);
//	}
//
//
//	public ResponseEntity<ApiResponse<List<OrderProduct>>> getByUserIdAndProductId(Long userId, Long productId) {
//		
//		UserDto currUser = currentUser.getUser();
//		if(currUser == null) {
//			throw new UnAuthorizedException("Please Login");
//		}
//		boolean isSelf = currUser.getUserId().equals(userId);
//		boolean isManager = IsAuthorized.isManager(currUser.getUserPermissions());
//		boolean isOrderManager = IsAuthorized.isOrderManager(currUser.getUserPermissions());
//		ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
//		List<OrderProduct> orders = orderRepo.findAllByUserAndProduct(userId, productId);
//		if(isSelf || (isManager || isOrderManager)) {
//			response.setData(orders);
//			response.setMessage("User "+userId+" Orders Details");
//		}
//		else if (!isSelf && !(isManager || isOrderManager)) {
//		    throw new UnAuthorizedException("Not Authorized to View This User's Order Details");
//		}
//		else if (currUser.getUserRole() == Role.ADMIN && !(isManager || isOrderManager)) {
//		    throw new UnAuthorizedException("You don't have rights to view order details");
//		}
//		if(orders.isEmpty()) {
//			throw new ProductNotFoundException("Orders Not Found With This UserID "+userId+" and ProductID "+productId);
//		}
//		return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<List<OrderProduct>>> getAllOrders() {
//		
//		List<OrderProduct> orderList = orderRepo.findAll();
//		
//		if(orderList.isEmpty()) {
//			throw new CustomException("No Order Found");
//		}
//		
//		ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
//		response.setData(orderList);
//		response.setMessage("All Orders Details");
//		return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<List<OrderProduct>>> getOrderStatus(OrderStatus status) {
//		
//		List<OrderProduct> orders = orderRepo.findAllByOrderStatus(status);
//		if(orders.isEmpty()) {
//			throw new CustomException("No Order Found with Order Status "+status);
//		}
//		
//		
//		ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
//		response.setData(orders);
//		response.setMessage("Order Details with Order Status "+status);
//		return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<List<OrderProduct>>> getOrderByPayment(PaymentStatus paymentStatus) {
//		
//		List<OrderProduct> orders = orderRepo.findAllByPaymentStatus(paymentStatus);
//		if(orders.isEmpty()) {
//			throw new CustomException("No Orders Found With Payment Status "+paymentStatus);
//		}
//		ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
//		response.setData(orders);
//		response.setMessage("Order Details with Payment Status "+paymentStatus);
//		return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<OrderProduct>> updateOrderStatus(Long orderId, OrderStatus status) {
//		
//		Optional<OrderProduct> orderExists = orderRepo.findById(orderId);
//		
//		orderExists.get().setOrderStatus(status);
//		ApiResponse<OrderProduct> response = new ApiResponse<>();
//		response.setData(orderExists.get());
//		response.setMessage(" Order Status for"+" Order ID "+orderExists.get().getOrderId() +" Updated Sucessfully");
//		return ResponseEntity.ok(response);
//	}
//
//}

package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.advicemethods.IsAuthorized;
import com.example.authentication.CurrentUser;
import com.example.clients.CartClient;
import com.example.clients.ProductClient;
import com.example.clients.UserClient;
import com.example.controller.ApiResponse;
import com.example.dto.CartDto;
import com.example.dto.PlaceOrder;
import com.example.dto.ProductDto;
import com.example.dto.UserDto;
import com.example.enums.OrderStatus;
import com.example.enums.PaymentStatus;
import com.example.enums.Role;
import com.example.exception.CustomException;
import com.example.exception.ProductNotFoundException;
import com.example.exception.UnAuthorizedException;
import com.example.exception.UserNotFoundException;
import com.example.model.Address;
import com.example.model.OrderItem;
import com.example.model.OrderProduct;
import com.example.repo.AddressRepo;
import com.example.repo.OrderRepo;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepo orderRepo;
    private CurrentUser currentUser;
    private AddressRepo addressRepo;
    private UserClient userClient;
    private CartClient cartClient;
    private ProductClient productClient;

    public OrderServiceImpl(OrderRepo orderRepo, CurrentUser currentUser,
                            UserClient userClient, AddressRepo addressRepo, ProductClient productClient, CartClient cartClient) {
        this.orderRepo = orderRepo;
        this.currentUser = currentUser;
        this.userClient = userClient;
        this.productClient = productClient;
        this.cartClient = cartClient;
        this.addressRepo = addressRepo;
    }

    @Transactional
    public ResponseEntity<ApiResponse<OrderProduct>> placeOrder(PlaceOrder orderDetails) {

        // Fetch user
        UserDto user = userClient.getUserById(orderDetails.getUserId())
                                 .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        if (!userClient.isValid(orderDetails.getUserId())) {
            throw new UnAuthorizedException("Please Login");
        }

        // Fetch product
        ProductDto product = productClient.getProductById(orderDetails.getProductId())
                                          .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        // Fetch cart item
        CartDto cartItem = cartClient.getUserCart(orderDetails.getUserId(), orderDetails.getProductId())
                                     .orElseThrow(() -> new ProductNotFoundException("Product not in cart"));

        Address address = addressRepo.findById(orderDetails.getAddressId())
                                     .orElseThrow(() -> new CustomException("Address Not Found"));

        if (!address.getUserId().equals(orderDetails.getUserId())) {
            throw new UnAuthorizedException("Address does not belong to this user");
        }

        if (product.getProductQuantity() < orderDetails.getQuantity()) {
            throw new CustomException("Insufficient product stock.");
        }

        if (!user.getPaymentDetails().contains(orderDetails.getPaymentType())) {
            throw new CustomException("Invalid payment method. Available: " + user.getPaymentDetails());
        }

        OrderProduct order = new OrderProduct();
        order.setUser(user.getUserId());
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(address.getFullAddress()); 
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setTotalPrice(orderDetails.getQuantity() * product.getProductPrice());

        productClient.decreaseQuantity(product.getProductId(), orderDetails.getQuantity());

        int remainingQty = cartItem.getProductQuantity() - orderDetails.getQuantity();
        if (remainingQty <= 0) {
            cartClient.deleteCart(orderDetails.getUserId(), orderDetails.getProductId());
        } else {
            cartClient.updateCartQuantity(orderDetails.getUserId(), orderDetails.getProductId(), remainingQty);
        }

        // Create order item
        OrderItem item = new OrderItem();
        item.setProductId(product.getProductId());
        item.setQuantity(orderDetails.getQuantity());
        order.setItems(List.of(item));

        // Save order
        orderRepo.save(order);

        ApiResponse<OrderProduct> response = new ApiResponse<>();
        response.setData(order);
        response.setMessage("Order placed successfully");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<OrderProduct>>> getOrderStatus(OrderStatus status) {
		
		List<OrderProduct> orders = orderRepo.findAllByOrderStatus(status);
		if(orders.isEmpty()) {
			throw new CustomException("No Order Found with Order Status "+status);
		}
		
		
		ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
		response.setData(orders);
		response.setMessage("Order Details with Order Status "+status);
		return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<OrderProduct>>> getOrderByUser(Long userId) {
    	
        UserDto currUser = currentUser.getUser();

        if (!currUser.getUserId().equals(userId) && currUser.getUserRole() != Role.ADMIN) {
            throw new UnAuthorizedException("Not authorized to view orders for this user");
        }

        List<OrderProduct> orders = orderRepo.findByUser(userId);
        if (orders.isEmpty()) {
            throw new CustomException("No orders found for user " + userId);
        }

        ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
        response.setData(orders);
        response.setMessage("Orders for user " + userId);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<OrderProduct>>> getByUserIdAndProductId(Long userId, Long productId) {
		
		UserDto currUser = currentUser.getUser();
		if(currUser == null) {
			throw new UnAuthorizedException("Please Login");
		}
		boolean isSelf = currUser.getUserId().equals(userId);
		boolean isManager = IsAuthorized.isManager(currUser.getUserPermissions());
		boolean isOrderManager = IsAuthorized.isOrderManager(currUser.getUserPermissions());
		ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
		List<OrderProduct> orders = orderRepo.findAllByUserAndProduct(userId, productId);
		if(isSelf || (isManager || isOrderManager)) {
			response.setData(orders);
			response.setMessage("User "+userId+" Orders Details");
		}
		else if (!isSelf && !(isManager || isOrderManager)) {
		    throw new UnAuthorizedException("Not Authorized to View This User's Order Details");
		}
		else if (currUser.getUserRole() == Role.ADMIN && !(isManager || isOrderManager)) {
		    throw new UnAuthorizedException("You don't have rights to view order details");
		}
		if(orders.isEmpty()) {
			throw new ProductNotFoundException("Orders Not Found With This UserID "+userId+" and ProductID "+productId);
		}
		return ResponseEntity.ok(response);
	}

    public ResponseEntity<ApiResponse<List<OrderProduct>>> getAllOrders() {
        List<OrderProduct> orders = orderRepo.findAll();
        if (orders.isEmpty()) {
            throw new CustomException("No orders found");
        }

        ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
        response.setData(orders);
        response.setMessage("All orders");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<OrderProduct>>> getOrderByPayment(PaymentStatus paymentStatus) {
		
		List<OrderProduct> orders = orderRepo.findAllByPaymentStatus(paymentStatus);
		if(orders.isEmpty()) {
			throw new CustomException("No Orders Found With Payment Status "+paymentStatus);
		}
		ApiResponse<List<OrderProduct>> response = new ApiResponse<>();
		response.setData(orders);
		response.setMessage("Order Details with Payment Status "+paymentStatus);
		return ResponseEntity.ok(response);
	}
    
    public ResponseEntity<ApiResponse<OrderProduct>> updateOrderStatus(Long orderId, OrderStatus status) {
        OrderProduct order = orderRepo.findById(orderId)
                                      .orElseThrow(() -> new CustomException("Order not found"));
        order.setOrderStatus(status);
        orderRepo.save(order);

        ApiResponse<OrderProduct> response = new ApiResponse<>();
        response.setData(order);
        response.setMessage("Order status updated successfully");
        return ResponseEntity.ok(response);
    }

	@Transactional
	public ResponseEntity<ApiResponse<OrderProduct>> cancelOrder(Long userId, Long productId, int quantity) {

	    UserDto currUser = currentUser.getUser();
		if(currUser == null) {
			throw new UnAuthorizedException("Please Login");
		}
		if(currUser.getUserId()!= userId) {
			throw new UnAuthorizedException("Not Authorized to Cancel Order With Another Account");
		}
		
	    Optional<OrderProduct> orderExists = orderRepo.findByUserAndProductAndQuantity(userId, productId, quantity);
	    if (!orderExists.isPresent()) {
	        throw new ProductNotFoundException("No matching order found for user " + userId + " with product " 
	        								+ productId + " and quantity " + quantity);
	    }
	    OrderProduct order = orderExists.get();

	    OrderItem items = null;
	    for (OrderItem item : order.getItems()) {
	        if (item.getProductId().equals(productId) && item.getQuantity() == quantity) {
	            items = item;
	            break;
	        }
	    }

	    if (items == null) {
	        throw new ProductNotFoundException("No matching item found in order for product " + productId);
	    }
	    productClient.increaseQuantity(productId, quantity);
	    orderRepo.delete(order);

	    ApiResponse<OrderProduct> response = new ApiResponse<>();
	    response.setMessage("Order cancelled successfully");
	    return ResponseEntity.ok(response);
	}
}
