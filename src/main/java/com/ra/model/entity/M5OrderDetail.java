package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class M5OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", referencedColumnName = "orderId")
    private M5Order order;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private M5Product product;
    private Double unitPrice;
    private Integer orderQuantity;
}
