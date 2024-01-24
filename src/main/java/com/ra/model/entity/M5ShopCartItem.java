package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class M5ShopCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "cartId")
    @JsonIgnore
    private M5ShoppingCart cart;
    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "productId")
    private M5Product product;
    private Integer quantity;
}
