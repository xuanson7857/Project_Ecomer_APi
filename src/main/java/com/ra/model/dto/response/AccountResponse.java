package com.ra.model.dto.response;
import lombok.*;
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String avatar;
    private String phone;
}
