package com.ra.model.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {
    private Long productId;
    private String productName;
    private String description;
    private Double unitPrice;
    private String image;
    private Integer stockQuantity;
    private String categoryName;
}
