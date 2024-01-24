package com.ra.repository;

import com.ra.model.entity.M5Product;
import com.ra.model.entity.M5ShopCartItem;
import com.ra.model.entity.M5ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<M5ShopCartItem,Long> {
    M5ShopCartItem findByProductAndCart(M5Product product, M5ShoppingCart cart);
    Optional<M5ShopCartItem> findByIdAndCart(Long id, M5ShoppingCart cart);
    void deleteAllByCart(M5ShoppingCart cart);
    void deleteAllByProduct(M5Product product);
}
