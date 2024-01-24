package com.ra.model.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class CartItemRequest {
    private Long id;
    private Long productId;
    @Positive(message = "quantity can't be negative")
    private Integer quantity;
}
