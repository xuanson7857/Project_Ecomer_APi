package com.ra.model.dto.response;

import lombok.*;

@Getter@Setter@Builder@AllArgsConstructor@NoArgsConstructor
public class CartItemResponse {
    private Long id;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
}
