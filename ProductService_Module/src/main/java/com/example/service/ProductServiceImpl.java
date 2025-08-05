//package com.example.service;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.example.advicemethods.IsAuthorized;
//import com.example.authentication.CurrentUser;
//import com.example.clients.UserClient;
//import com.example.controller.ApiResponse;
//import com.example.dto.UserDto;
//import com.example.exception.CustomException;
//import com.example.exception.ProductNotFoundException;
//import com.example.exception.UnAuthorizedException;
//import com.example.model.Product;
//import com.example.repo.ProductRepo;
//
//import jakarta.transaction.Transactional;
//
//@Service
//public class ProductServiceImpl implements ProductService{
//	
//
//	private ProductRepo productRepo;
//	private UserClient userClient;
//	private CurrentUser currentUser;
//	
//	
//	public ProductServiceImpl(ProductRepo productRepo, UserClient userClient, CurrentUser currentUser) {
//			this.productRepo = productRepo;
//			this.userClient = userClient;
//			this.currentUser = currentUser;
//	}
//	 
//	public ResponseEntity<ApiResponse<Product>> saveProduct(Product product) {
//		
//		if(product == null) {
//			throw new ProductNotFoundException("Product Cannot be Empty");
//		}else if(product.getProductName() == null || product.getProductName().isBlank()) {
//			throw new ProductNotFoundException("Product Name Cannot be Null");
//		}else if(product.getProductCategory() == null || product.getProductCategory().isBlank()) {
//			throw new ProductNotFoundException("Product Category Cannot Null");
//		}else if(product.getProductImageURL() == null  || product.getProductImageURL().isBlank()) {
//			throw new ProductNotFoundException("Product ImageURL Cannot be Null");
//		}else if(product.getProductPrice() <= 0.0) {
//			throw new ProductNotFoundException("Product Price Canot be Less than Zero");
//		}else if(product.getProductDescription() == null || product.getProductDescription().isBlank()) {
//			throw new ProductNotFoundException("Product Description Cannot be Empty");
//		}else if(product.getProductQuantity() <= 0) {
//			throw new ProductNotFoundException("Product Quantity cannot be less than 0");
//		}
//		productRepo.save(product);
//		ApiResponse<Product> response = new ApiResponse<>();
//		response.setData(product);
//		response.setMessage("New Product Added Successfully");
//	
//		return ResponseEntity.ok(response);
//	}
//
//	@Transactional
//	public ResponseEntity<ApiResponse<Product>> productUpdate(Long productId, Product newProduct) {
//		
//		Optional<Product> exists= productRepo.findById(productId);
//		
//		if(!exists.isPresent()) {
//			throw new ProductNotFoundException("Product Not Found");
//		}
//		
//			Product product = exists.get();
//			product.setProductName(newProduct.getProductName());
//			product.setProductPrice(newProduct.getProductPrice());
//			product.setProductQuantity(newProduct.getProductQuantity());
//			product.setProductCategory(newProduct.getProductCategory());
//			
//			productRepo.save(product);
//			ApiResponse<Product> response = new ApiResponse<>();
//			response.setData(product);
//			response.setMessage("Product Updated Successfully");
//		
//		return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<Product>> getProductById(Long productId) {
//		
//		Optional<Product> exists = productRepo.findById(productId);
//		if(!exists.isPresent()) {
//			throw new ProductNotFoundException("Product Not Found");
//		}
//		ApiResponse<Product> response = new ApiResponse<>();
//		Product product = exists.get();
//		response.setData(product);
//		response.setMessage("Product "+productId+" Details");
//		return ResponseEntity.ok(response);
//	}
//
//	@Transactional
//	public ResponseEntity<ApiResponse<Product>> deleteById(Long productId) {
//		
//		UserDto currUser = currentUser.getUser();
//		boolean isManager = IsAuthorized.isManager(currUser.getAdminPermissions());
//		boolean isProductManager = IsAuthorized.isProductManager(currUser.getAdminPermissions());
//		if(!isManager || !isProductManager) {
//			throw new UnAuthorizedException("You Dont Have Rights to Delete The Product");
//		}
//		Optional<Product> exists = productRepo.findById(productId);
//		if(!exists.isPresent()) {
//			throw new ProductNotFoundException("Product Not Found");
//			
//		}
//		
//			productRepo.deleteById(productId);
//			ApiResponse<Product> response = new ApiResponse<>();
//			response.setMessage("Product Deleted Successfully");
//			return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<List<Product>>> getProductByCategory(String category) {
//		
//		List<Product> exists = productRepo.findByProductCategory(category);	
//		if(exists.isEmpty()) {
//			throw new ProductNotFoundException("No Product Found Under the Category "+category);
//		}
//		ApiResponse<List<Product>> response = new ApiResponse<>();
//		response.setData(exists);
//		response.setMessage("Product Details");
//		return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<List<Product>>> displayAllProducts() {	
//		List<Product> products = productRepo.findAll();
//		
//		ApiResponse<List<Product>> response = new ApiResponse<>();
//		response.setData(products);
//		response.setMessage("All Product Details");
//		return ResponseEntity.ok(response);
//	}
//
//	public ResponseEntity<ApiResponse<List<Product>>> getProductBetweenPrice(String category, double minPrice, double maxPrice) {
//		
//		List<Product> products = productRepo.findProductsByPriceRange(category, minPrice, maxPrice);
//		
//		if(products.isEmpty()) {
//			throw new CustomException("No Items Found Between That PriceRange");
//		}
//		ApiResponse<List<Product>> response = new ApiResponse<>();
//		response.setData(products);
//		response.setMessage("Products Between "+minPrice+" And "+maxPrice);
//		return ResponseEntity.ok(response);
//	}
//
//	public void decreaseQuantity(Long productId, int newQuantity) {
//		
//		Optional<Product> findProduct = productRepo.findById(productId);
//		if(findProduct.isPresent()) {
//			throw new ProductNotFoundException("Product Not Found");
//		}
//		
//		findProduct.get().setProductQuantity(findProduct.get().getProductQuantity()-newQuantity);
//		productRepo.save(findProduct.get());
//		
//	}
//	
//	public void increaseQuantity(Long productId, int newQuantity) {
//		
//		Optional<Product> findProduct = productRepo.findById(productId);
//		if(findProduct.isPresent()) {
//			throw new ProductNotFoundException("Product Not Found");
//		}
//		
//		findProduct.get().setProductQuantity(findProduct.get().getProductQuantity()+newQuantity);
//		productRepo.save(findProduct.get());
//		
//	}
//
//	public List<Long> getAllProductIds() {
//		
//		return productRepo.getAllProductIds();
//	}
//
//}

package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.advicemethods.IsAuthorized;
import com.example.authentication.CurrentUser;
import com.example.clients.UserClient;
import com.example.controller.ApiResponse;
import com.example.dto.UserDto;
import com.example.exception.CustomException;
import com.example.exception.ProductNotFoundException;
import com.example.exception.UnAuthorizedException;
import com.example.model.Product;
import com.example.repo.ProductRepo;

import feign.FeignException;
import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepo productRepo;
    private UserClient userClient;
    private CurrentUser currentUser;

    // Constructor Injection
    public ProductServiceImpl(ProductRepo productRepo, UserClient userClient, CurrentUser currentUser) {
        this.productRepo = productRepo;
        this.userClient = userClient;
        this.currentUser = currentUser;
    }

    // Token Validation Method (can be used for all methods that need authorization)
    private void validateUserToken(String token) {
        try {
            UserDto userDto = userClient.validateToken(token);  // Validates token via UserClient
            currentUser.setUser(userDto);  // Sets current user after token validation
        } catch (FeignException.Unauthorized ex) {
            throw new UnAuthorizedException("Token Not Found or Token is Expired");
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Product>> saveProduct(Product product) {
        // Get the token from current user context (or from request header)
        String token = currentUser.getUser().getUserToken();

        // Validate token before performing any action
        validateUserToken(token);

        // Validate Product fields before saving
        if (product == null) {
            throw new ProductNotFoundException("Product Cannot be Empty");
        } else if (product.getProductName() == null || product.getProductName().isBlank()) {
            throw new ProductNotFoundException("Product Name Cannot be Null");
        } else if (product.getProductCategory() == null || product.getProductCategory().isBlank()) {
            throw new ProductNotFoundException("Product Category Cannot Null");
        } else if (product.getProductImageURL() == null || product.getProductImageURL().isBlank()) {
            throw new ProductNotFoundException("Product ImageURL Cannot be Null");
        } else if (product.getProductPrice() <= 0.0) {
            throw new ProductNotFoundException("Product Price Cannot be Less than Zero");
        } else if (product.getProductDescription() == null || product.getProductDescription().isBlank()) {
            throw new ProductNotFoundException("Product Description Cannot be Empty");
        } else if (product.getProductQuantity() <= 0) {
            throw new ProductNotFoundException("Product Quantity Cannot be Less than 0");
        }

        // Save the product
        productRepo.save(product);
        ApiResponse<Product> response = new ApiResponse<>();
        response.setData(product);
        response.setMessage("New Product Added Successfully");
        return ResponseEntity.ok(response);
    }

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<Product>> productUpdate(Long productId, Product newProduct) {
        // Get the token from current user context (or from request header)
        String token = currentUser.getUser().getUserToken();

        // Validate token before performing any action
        validateUserToken(token);

        // Check if the product exists before updating
        Optional<Product> exists = productRepo.findById(productId);
        if (!exists.isPresent()) {
            throw new ProductNotFoundException("Product Not Found");
        }

        // Update product details
        Product product = exists.get();
        product.setProductName(newProduct.getProductName());
        product.setProductPrice(newProduct.getProductPrice());
        product.setProductQuantity(newProduct.getProductQuantity());
        product.setProductCategory(newProduct.getProductCategory());

        productRepo.save(product);

        ApiResponse<Product> response = new ApiResponse<>();
        response.setData(product);
        response.setMessage("Product Updated Successfully");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponse<Product>> getProductById(Long productId) {
        // Get the token from current user context (or from request header)
        String token = currentUser.getUser().getUserToken();

        // Validate token before performing any action
        validateUserToken(token);

        // Check if product exists
        Optional<Product> exists = productRepo.findById(productId);
        if (!exists.isPresent()) {
            throw new ProductNotFoundException("Product Not Found");
        }

        ApiResponse<Product> response = new ApiResponse<>();
        Product product = exists.get();
        response.setData(product);
        response.setMessage("Product " + productId + " Details");
        return ResponseEntity.ok(response);
    }

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<Product>> deleteById(Long productId) {
        // Get the token from current user context (or from request header)
        String token = currentUser.getUser().getUserToken();

        // Validate token before performing any action
        validateUserToken(token);

        // Authorization check: if user has permissions to delete
        UserDto currUser = currentUser.getUser();
        boolean isManager = IsAuthorized.isManager(currUser.getAdminPermissions());
        boolean isProductManager = IsAuthorized.isProductManager(currUser.getAdminPermissions());
        if (!isManager || !isProductManager) {
            throw new UnAuthorizedException("You Don't Have Rights to Delete The Product");
        }

        // Check if product exists
        Optional<Product> exists = productRepo.findById(productId);
        if (!exists.isPresent()) {
            throw new ProductNotFoundException("Product Not Found");
        }

        productRepo.deleteById(productId);
        ApiResponse<Product> response = new ApiResponse<>();
        response.setMessage("Product Deleted Successfully");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponse<List<Product>>> getProductByCategory(String category) {
        // Get the token from current user context (or from request header)
        String token = currentUser.getUser().getUserToken();

        // Validate token before performing any action
        validateUserToken(token);

        // Fetch products by category
        List<Product> exists = productRepo.findByProductCategory(category);
        if (exists.isEmpty()) {
            throw new ProductNotFoundException("No Product Found Under the Category " + category);
        }

        ApiResponse<List<Product>> response = new ApiResponse<>();
        response.setData(exists);
        response.setMessage("Product Details");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponse<List<Product>>> displayAllProducts() {
        // Get the token from current user context (or from request header)
        String token = currentUser.getUser().getUserToken();

        // Validate token before performing any action
        validateUserToken(token);

        // Fetch all products
        List<Product> products = productRepo.findAll();

        ApiResponse<List<Product>> response = new ApiResponse<>();
        response.setData(products);
        response.setMessage("All Product Details");
        return ResponseEntity.ok(response);
    }
    
	public ResponseEntity<ApiResponse<List<Product>>> getProductBetweenPrice(String category, double minPrice, double maxPrice) {
		
		List<Product> products = productRepo.findProductsByPriceRange(category, minPrice, maxPrice);
		
		if(products.isEmpty()) {
			throw new CustomException("No Items Found Between That PriceRange");
		}
		ApiResponse<List<Product>> response = new ApiResponse<>();
		response.setData(products);
		response.setMessage("Products Between "+minPrice+" And "+maxPrice);
		return ResponseEntity.ok(response);
	}

	public void decreaseQuantity(Long productId, int newQuantity) {
		
		Optional<Product> findProduct = productRepo.findById(productId);
		if(findProduct.isPresent()) {
			throw new ProductNotFoundException("Product Not Found");
		}
		
		findProduct.get().setProductQuantity(findProduct.get().getProductQuantity()-newQuantity);
		productRepo.save(findProduct.get());
		
	}
	
	public void increaseQuantity(Long productId, int newQuantity) {
		
		Optional<Product> findProduct = productRepo.findById(productId);
		if(findProduct.isPresent()) {
			throw new ProductNotFoundException("Product Not Found");
		}
		
		findProduct.get().setProductQuantity(findProduct.get().getProductQuantity()+newQuantity);
		productRepo.save(findProduct.get());
		
	}

	public List<Long> getAllProductIds() {
		
		return productRepo.getAllProductIds();
	}
}

