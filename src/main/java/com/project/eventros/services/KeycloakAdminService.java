package com.project.eventros.services;

import com.project.eventros.dtos.SignupRequestDto;
import com.project.eventros.exceptions.KeycloakAdminException;
import com.project.eventros.exceptions.UserRegistrationException;

public interface KeycloakAdminService {
    String createUser(SignupRequestDto signupRequest) throws KeycloakAdminException, UserRegistrationException;
    void assignRole(String userId, String roleName) throws KeycloakAdminException;
    boolean userExists(String username, String email) throws KeycloakAdminException;
}
