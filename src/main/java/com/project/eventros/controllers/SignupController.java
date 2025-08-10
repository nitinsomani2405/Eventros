package com.project.eventros.controllers;

import com.project.eventros.dtos.SignupRequestDto;
import com.project.eventros.dtos.SignupResponseDto;
import com.project.eventros.exceptions.KeycloakAdminException;
import com.project.eventros.exceptions.UserRegistrationException;
import com.project.eventros.services.KeycloakAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Configure this properly for production
public class SignupController {

    private final KeycloakAdminService keycloakAdminService;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realmName;

    @Value("${keycloak.public.client-id}")
    private String publicClientId;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequest) 
            throws KeycloakAdminException, UserRegistrationException {
        
        log.info("Signup request received for username: {}", signupRequest.getUsername());
        
        try {
            // Create user in Keycloak
            String userId = keycloakAdminService.createUser(signupRequest);
            
            // Build login URL for frontend redirect
            String loginUrl = String.format("%s/realms/%s/protocol/openid-connect/auth?client_id=%s&response_type=code&scope=openid&redirect_uri=",
                    authServerUrl, realmName, publicClientId);
            
            SignupResponseDto response = SignupResponseDto.builder()
                    .message("User registered successfully")
                    .userId(userId)
                    .username(signupRequest.getUsername())
                    .email(signupRequest.getEmail())
                    .role(signupRequest.getRole())
                    .emailVerificationRequired(true) // You can customize this based on your Keycloak setup
                    .loginUrl(loginUrl)
                    .build();
            
            log.info("User {} registered successfully with ID: {}", signupRequest.getUsername(), userId);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (UserRegistrationException e) {
            log.warn("User registration failed: {}", e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        } catch (KeycloakAdminException e) {
            log.error("Keycloak admin operation failed", e);
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    @GetMapping("/signup/roles")
    public ResponseEntity<String[]> getAvailableRoles() {
        String[] roles = {"ROLE_ATTENDEE", "ROLE_STAFF", "ROLE_ORGANIZER"};
        return ResponseEntity.ok(roles);
    }
}
