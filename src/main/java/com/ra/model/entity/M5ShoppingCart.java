package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class M5ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
//    @ManyToMany
//    @JoinTable(name = "cart_item"
//            , joinColumns = @JoinColumn(name = "cartId")
//            , inverseJoinColumns = @JoinColumn(name = "productId"))
    @OneToMany(mappedBy = "cart")
    private Set<M5ShopCartItem> cartItems;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonIgnore
    private M5User user;
    private Integer order_quantity;
}
