package com.ra.model.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.*;

@NoArgsConstructor@AllArgsConstructor@Getter
@Setter@Builder
public class OrderRequest {
    private Long orderId;
    private Long addressId;
    private String fullAddress;
    private String phone;
    private String receiver;
}
