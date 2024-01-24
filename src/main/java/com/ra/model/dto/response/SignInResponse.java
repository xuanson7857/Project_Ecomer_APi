package com.ra.model.dto.response;

import lombok.*;

import java.util.Set;
@AllArgsConstructor@NoArgsConstructor@Getter@Setter@Builder
public class SignInResponse {
    private final String type = "Bearer Token";
    private Long id;
    private String fullName;
    private String token;
    private Set<String> roles;

}
