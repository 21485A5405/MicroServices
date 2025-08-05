//package com.example.repo;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.example.model.CartItem;
//
//import jakarta.transaction.Transactional;
//
//@Repository
//public interface CartItemRepo extends JpaRepository<CartItem, Long> {
//
//    @Query("SELECT c FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId")
//    Optional<CartItem> findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
//
//    @Transactional
//    @Modifying
//    @Query("DELETE FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId")
//    void deleteByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
//
//    @Query("SELECT c FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId")
//    List<CartItem> findAllByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
//
//    @Query("SELECT c FROM CartItem c WHERE c.userId = :userId")
//    List<CartItem> findByUserId(@Param("userId") Long userId);
//
//    @Transactional
//    @Modifying
//    @Query("DELETE FROM CartItem c WHERE c.userId = :userId")
//    void deleteAllByUser(@Param("userId") Long userId);
//
//    @Query("SELECT c FROM CartItem c WHERE c.productId = :productId")
//    List<CartItem> findAllByProductId(@Param("productId") Long productId);
//}
//

package com.example.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.CartItem;

import jakarta.transaction.Transactional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    // ✅ Find by userId and productId
    @Query("SELECT c FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId")
    Optional<CartItem> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    // ✅ Delete by userId and productId
    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId")
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    // ✅ Find all by userId
    @Query("SELECT c FROM CartItem c WHERE c.userId = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    // ✅ Delete all by userId
    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    // ✅ Find all by productId
    @Query("SELECT c FROM CartItem c WHERE c.productId = :productId")
    List<CartItem> findAllByProductId(@Param("productId") Long productId);
    
  @Query("SELECT c FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId")
  List<CartItem> findAllByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

}

