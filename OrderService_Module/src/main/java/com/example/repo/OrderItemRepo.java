package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.model.OrderItem;

import jakarta.transaction.Transactional;
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    
    // Delete all order items for a given product ID
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem oi WHERE oi.productId = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);

    // Delete all order items for a given order ID
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem o WHERE o.order.orderId = :orderId")
    void deleteAllByOrderId(@Param("orderId") Long orderId);
}
