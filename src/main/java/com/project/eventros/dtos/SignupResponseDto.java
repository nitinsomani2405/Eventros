package com.project.eventros.dtos;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class SignupResponseDto {
    private String message;
    private String userId;
    private String username;
    private String email;
    private String role;
    private boolean emailVerificationRequired;
    private String loginUrl;
}
