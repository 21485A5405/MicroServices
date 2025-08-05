package com.example.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.enums.OrderStatus;
import com.example.enums.PaymentStatus;
import com.example.model.OrderProduct;

import jakarta.transaction.Transactional;
@Repository
public interface OrderRepo extends JpaRepository<OrderProduct, Long> {

    // Finding orders by product ID, without referencing the Product entity directly
    @Query("SELECT o FROM OrderProduct o JOIN o.orderItems oi WHERE oi.productId = :productId")
    List<OrderProduct> findOrdersByProduct(@Param("productId") Long productId);

    // Finding orders for a specific user and product
    @Query("SELECT o FROM OrderProduct o JOIN o.orderItems oi WHERE o.userId = :userId AND oi.productId = :productId")
    List<OrderProduct> findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    // Finding all orders by user ID (with no join on the User entity)
    @Query("SELECT o FROM OrderProduct o WHERE o.userId = :userId")
    List<OrderProduct> findByUser(@Param("userId") Long userId);

    // Finding orders by user ID and product ID (same as above but more specific)
    @Query("SELECT o FROM OrderProduct o JOIN o.orderItems oi WHERE o.userId = :userId AND oi.productId = :productId")
    List<OrderProduct> findAllByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    // Delete all orders by user ID
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderProduct o WHERE o.userId = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    // Delete order by user ID and product ID, also considering quantity
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderProduct o WHERE o.userId = :userId AND o IN (SELECT oi.order FROM OrderItem oi WHERE oi.productId = :productId AND oi.quantity = :quantity)")
    void deleteByQuantity(@Param("userId") Long userId, 
                          @Param("productId") Long productId, 
                          @Param("quantity") int quantity);

    // Finding orders by order status
    @Query("SELECT o FROM OrderProduct o WHERE o.orderStatus = :status")
    List<OrderProduct> findAllByOrderStatus(@Param("status") OrderStatus status);

    // Finding orders by payment status
    @Query("SELECT o FROM OrderProduct o WHERE o.paymentStatus = :paymentStatus")
    List<OrderProduct> findAllByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);

    // Finding an order by user ID, product ID, and quantity
    @Query("SELECT o FROM OrderProduct o JOIN o.orderItems oi WHERE o.userId = :userId AND oi.productId = :productId AND oi.quantity = :quantity")
    Optional<OrderProduct> findByUserAndProductAndQuantity(@Param("userId") Long userId,
                                                           @Param("productId") Long productId,
                                                           @Param("quantity") int quantity);
}
