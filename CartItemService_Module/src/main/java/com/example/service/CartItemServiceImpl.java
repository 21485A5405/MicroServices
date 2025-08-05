//package com.example.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.example.authentication.CurrentUser;
//import com.example.clients.ProductClient;
//import com.example.clients.UserClient;
//import com.example.controller.ApiResponse;
//import com.example.dto.ProductDto;
//import com.example.dto.UserDto;
//import com.example.exception.CustomException;
//import com.example.exception.ProductNotFoundException;
//import com.example.exception.UnAuthorizedException;
//import com.example.exception.UserNotFoundException;
//import com.example.model.CartItem;
//import com.example.repo.CartItemRepo;
//
//import jakarta.transaction.Transactional;
//
//@Service
//public class CartItemServiceImpl implements CartItemService{
//
//	private CartItemRepo cartItemRepo;
//	private UserClient userClinet;
//	private ProductClient productClient;
//	private CurrentUser currentUser;
//	
//	public CartItemServiceImpl(CartItemRepo cartItemRepo, CurrentUser currentUser, 
//									ProductClient productClient, UserClient userClient) {
//		this.cartItemRepo = cartItemRepo;
//		this.userClinet = userClient;
//		this.productClient = productClient;
//		this.currentUser = currentUser;
//		
//	}  
//	
//	public ResponseEntity<ApiResponse<CartItem>> addProductToCart(Long userId,Long productId, int quantity) {
//
//		Optional<CartItem> exists = cartItemRepo.findByUserAndProduct(userId, productId);
//		Optional<ProductDto> productExists = productClient.getProduct(productId);
//		Optional<UserDto> userExists = userClinet.getUser(userId);
//		
//		UserDto currUser = currentUser.getUser();
//		if(currUser == null) {
//			throw new UnAuthorizedException("Please Login");
//		}if(!userExists.isPresent()) {
//			throw new UserNotFoundException("User Not Found");
//		}
//		if(currUser.getUserId()!= userId) {
//			throw new UnAuthorizedException("User Not Authorized to Add Product Into Another Account");
//		}
//		if(!productExists.isPresent()) {
//			throw new ProductNotFoundException("Product Not Found to Add Into Cart");
//		}
//		ProductDto product =productExists.get();
//		UserDto user = userExists.get();	
//		if(product.getProductQuantity() == 0) {
//			throw new CustomException("Product Out Of Stock");
//		}
//		if(quantity <=0) {
//			throw new CustomException("Quantity Cannot be Less than Zero");
//		}
//		
//		if(product.getProductQuantity()<quantity) {
//			
//			throw new CustomException("Enough Quantity Selected , We have "
//					+product.getProductQuantity()+" Items available. Please Selcct Under "
//							+product.getProductQuantity()+" as Quantity");
//		}
//		CartItem cartItem = null;
//		if(exists.isPresent()) { // only increase the existing quantity
//			cartItem = exists.get();
//			cartItem.setProductQuantity(cartItem.getProductQuantity()+quantity);
//			
//		}else {
//			cartItem = new CartItem();
//			cartItem.setUser(user);
//			cartItem.setProduct(product);
//			cartItem.setProductQuantity(quantity);
//			cartItem.setTotalPrice(quantity*product.getProductPrice());
//		}
//		cartItemRepo.save(cartItem);
//		ApiResponse<CartItem> response = new ApiResponse<>();
//		response.setData(cartItem);
//		response.setMessage("Item Added Into Cart Successfully");
//		return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<CartItem>> getCartItems(Long userId, Long productId) {
//		
//		UserDto currUser = currentUser.getUser();
//		if(currUser == null) {
//			throw new UnAuthorizedException("Please Login");
//		}
//		if(currUser.getUserId()!= userId) {
//			throw new UnAuthorizedException("Not Authorized To See Another User Cart Details");
//		}
//		Optional<CartItem> c = cartItemRepo.findByUserAndProduct(userId, productId);
//		if(!c.isPresent()) {
//			throw new UserNotFoundException("User with respective Product Not Found In Cart");
//		}
//		
//			CartItem cartItem = c.get();
//			ApiResponse<CartItem> response = new ApiResponse<>();
//			response.setData(cartItem);
//			response.setMessage("CartItem Details");
//			return ResponseEntity.ok(response);
//	}
//
//	@Transactional
//	public ResponseEntity<ApiResponse<CartItem>> deleteUserAndProduct(Long userId, Long productId) {
//		
//		UserDto currUser = currentUser.getUser();
//		if(currUser == null) {
//			throw new UnAuthorizedException("Please Login");
//		}
//		if(currUser.getUserId()!= userId) {
//			throw new UnAuthorizedException("Not Authorized to Delete Another User Cart Details");
//		}
//		Optional<CartItem> exists = cartItemRepo.findByUserAndProduct(userId, productId);
//		if (!exists.isPresent()) {
//		    throw new ProductNotFoundException("No Items Found For That Product ID and User ID to Delete");
//		}
//		
//		cartItemRepo.deleteByUserAndProduct(userId, productId);
//		CartItem cartItem = exists.get();
//		ApiResponse<CartItem> response = new ApiResponse<>();
//		response.setData(cartItem);
//		response.setMessage("Item Deleted From the cart");
//		return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<List<CartItem>>> getItemsByUserId(Long userId) {
//
//
//		Optional<UserDto> userExists = userClinet.getUser(userId);
//		if(!userExists.isPresent()) {
//			throw new UserNotFoundException("User Not Found");
//		}
//		UserDto currUser = currentUser.getUser();
//		if(currUser == null) {
//			throw new UnAuthorizedException("Please Login");
//		}
//		if(currUser.getUserId()!= userId) {
//			throw new UnAuthorizedException("Not Authorized To See Another User Cart Details");
//		}
//		List<CartItem> cartItems = cartItemRepo.findByUserId(userId);
//		if(cartItems.isEmpty()) {
//			throw new UserNotFoundException("User Cart Is empty");
//		}
//		
//		ApiResponse<List<CartItem>> response = new ApiResponse<>();
//		response.setData(cartItems);
//		response.setMessage("CartItem of User"+userId);
//		return ResponseEntity.ok(response);
//	}
//
//	@Transactional
//	public ResponseEntity<ApiResponse<List<CartItem>>> deleteAllbyUserId(Long userId) {
//		
//		Optional<UserDto> userExists = userClinet.getUser(userId);
//		if(!userExists.isPresent()) {
//			throw new UserNotFoundException("User Not Found");
//		}
//		UserDto currUser = currentUser.getUser();
//		if(currUser == null) {
//			throw new UnAuthorizedException("Please Login");
//		}
//		if(currUser.getUserId()!= userId) {
//			throw new UnAuthorizedException("Not Authorized To Delete Another User Cart Details");
//		}
//		List<CartItem> c= cartItemRepo.findByUserId(userId);
//		
//		if(c.isEmpty()) {
//			throw new UserNotFoundException("User Cart Is empty");
//		}
//		
//		cartItemRepo.deleteAllByUser(userId);
//		ApiResponse<List<CartItem>> response = new ApiResponse<>();
//		response.setData(c);
//		response.setMessage("User "+userId+" Related Items Deleted From The Cart Successfully");
//		return ResponseEntity.ok(response);
//	}
//
//	@Transactional
//	public ResponseEntity<ApiResponse<CartItem>> updateCart(Long userId, Long productId, int newQuantity) {
//		
//		Optional<CartItem> exists = cartItemRepo.findByUserAndProduct(userId, productId);
//		Optional<ProductDto> productExists = productClient.getProduct(productId);
//		Optional<UserDto> userExists = userClinet.getUser(userId);
//		
//		UserDto currUser = currentUser.getUser();
//		if(currUser == null) {
//			throw new UnAuthorizedException("Please Login");
//		}if(!userExists.isPresent()) {
//			throw new UserNotFoundException("User Not Found");
//		}
//		if(currUser.getUserId()!= userId) {
//			throw new UnAuthorizedException("User Not Authorized to Add Product Into Another Account");
//		}
//		if(!productExists.isPresent()) {
//			throw new ProductNotFoundException("Product Not Found to Add to Cart");
//		}
//		ProductDto product =productExists.get();
//		if(product.getProductQuantity() == 0) {
//			throw new CustomException("Product Out Of Stock");
//		}
//		if(newQuantity <=0) {
//			throw new CustomException("Quantity Cannot be Less than Zero");
//		}
//		
//		if(product.getProductQuantity()<newQuantity) {
//			
//			throw new CustomException("Enough Quantity Selected , We have "
//					+product.getProductQuantity()+" Items available. Please Selcct Under "
//							+product.getProductQuantity()+" as Quantity");
//		}
//		CartItem cartItem = null;
//		if(exists.isPresent()) { // update Existing quantity 
//			cartItem = exists.get();
//			cartItem.setProductQuantity(newQuantity);
//			cartItem.setTotalPrice(newQuantity*product.getProductPrice());
//		}
//		ApiResponse<CartItem> response = new ApiResponse<>();
//		response.setData(cartItem);
//		response.setMessage("CartItems Updated Successfully");
//		return ResponseEntity.ok(response);
//	}
//
//	@Transactional
//	public void deleteCart(Long userId, Long productId) {
//		cartItemRepo.deleteByUserAndProduct(userId, productId);
//		
//	}
//}
//
package com.example.service;

import com.example.authentication.CurrentUser;
import com.example.clients.ProductClient;
import com.example.clients.UserClient;
import com.example.controller.ApiResponse;
import com.example.dto.ProductDto;
import com.example.dto.UserDto;
import com.example.exception.CustomException;
import com.example.exception.ProductNotFoundException;
import com.example.exception.UnAuthorizedException;
import com.example.model.CartItem;
import com.example.repo.CartItemRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepo cartItemRepo;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final CurrentUser currentUser;

    public CartItemServiceImpl(CartItemRepo cartItemRepo, CurrentUser currentUser,
                               ProductClient productClient, UserClient userClient) {
        this.cartItemRepo = cartItemRepo;
        this.userClient = userClient;
        this.productClient = productClient;
        this.currentUser = currentUser;
    }

    private UserDto validateCurrentUser(Long userId) {
        UserDto currUser = currentUser.getUser();
        if (currUser == null) {
            throw new UnAuthorizedException("Please login first.");
        }
        if (!currUser.getUserId().equals(userId)) {
            throw new UnAuthorizedException("Access denied to another user's cart.");
        }
        return currUser;
    }

    private ProductDto validateProduct(Long productId) {
        ProductDto product = productClient.getProduct(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        if (product.getProductQuantity() == 0) {
            throw new CustomException("Product out of stock.");
        }
        return product;
    }

    private void validateQuantity(int quantity, int availableStock) {
        if (quantity <= 0) {
            throw new CustomException("Quantity must be greater than zero.");
        }
        if (quantity > availableStock) {
            throw new CustomException("Only " + availableStock + " items available in stock.");
        }
    }

    @Override
    public ResponseEntity<ApiResponse<CartItem>> addProductToCart(Long userId, Long productId, int quantity) {
        validateCurrentUser(userId);
        ProductDto product = validateProduct(productId);
        validateQuantity(quantity, product.getProductQuantity());

        CartItem cartItem = cartItemRepo.findByUserIdAndProductId(userId, productId)
                .orElse(new CartItem());

        if (cartItem.getCartItemId() != null) {
            cartItem.setProductQuantity(cartItem.getProductQuantity() + quantity);
        } else {
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setProductQuantity(quantity);
        }

        cartItem.setTotalPrice(cartItem.getProductQuantity() * product.getProductPrice());
        cartItemRepo.save(cartItem);

        ApiResponse<CartItem> response = new ApiResponse<>(cartItem, "Item added to cart successfully.");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<CartItem>> getCartItems(Long userId, Long productId) {
        validateCurrentUser(userId);
        CartItem cartItem = cartItemRepo.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ProductNotFoundException("No cart item found for user and product."));
        ApiResponse<CartItem> response = new ApiResponse<>(cartItem, "Cart item details.");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<ApiResponse<CartItem>> deleteUserAndProduct(Long userId, Long productId) {
        validateCurrentUser(userId);

        CartItem cartItem = cartItemRepo.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ProductNotFoundException("No cart item found to delete."));

        cartItemRepo.deleteByUserIdAndProductId(userId, productId);

        ApiResponse<CartItem> response = new ApiResponse<>(cartItem, "Item deleted from the cart.");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<CartItem>>> getItemsByUserId(Long userId) {
        validateCurrentUser(userId);

        List<CartItem> cartItems = cartItemRepo.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new CustomException("Cart is empty.");
        }

        ApiResponse<List<CartItem>> response = new ApiResponse<>(cartItems, "Cart items for user " + userId);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<ApiResponse<List<CartItem>>> deleteAllbyUserId(Long userId) {
        validateCurrentUser(userId);

        List<CartItem> cartItems = cartItemRepo.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new CustomException("Cart is already empty.");
        }

        cartItemRepo.deleteAllByUserId(userId);

        ApiResponse<List<CartItem>> response = new ApiResponse<>(cartItems, "All items deleted from cart.");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<ApiResponse<CartItem>> updateCart(Long userId, Long productId, int newQuantity) {
        validateCurrentUser(userId);
        ProductDto product = validateProduct(productId);
        validateQuantity(newQuantity, product.getProductQuantity());

        CartItem cartItem = cartItemRepo.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ProductNotFoundException("Cart item not found to update."));

        cartItem.setProductQuantity(newQuantity);
        cartItem.setTotalPrice(newQuantity * product.getProductPrice());
        cartItemRepo.save(cartItem);

        ApiResponse<CartItem> response = new ApiResponse<>(cartItem, "Cart item updated successfully.");
        return ResponseEntity.ok(response);
    }

}

