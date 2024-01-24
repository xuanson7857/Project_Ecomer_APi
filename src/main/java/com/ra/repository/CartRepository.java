package com.ra.repository;

import com.ra.model.entity.M5ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<M5ShoppingCart,Long> {
    Optional<M5ShoppingCart> getByUserUserId(Long id);
//    List<Long> cartItems();

}
