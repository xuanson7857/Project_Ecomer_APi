package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class M5Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String description;
    @Positive(message = "product price can't be negative")
    private Double unitPrice;
    private String image;
    private Integer stockQuantity;
    private Date createAt;
    private Date updateAt;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "categoryId")
    @JsonIgnore
    private M5Category category;
}
