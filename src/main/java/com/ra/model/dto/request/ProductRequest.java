package com.ra.model.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor@NoArgsConstructor
@Getter@Setter
public class ProductRequest {
    private Long productId;
    private String productName;
    private String description;
    @Positive(message = "product price can't be negative")
    private Double unitPrice;
    private MultipartFile image;
    private Integer stockQuantity;
    private String categoryName;
}
