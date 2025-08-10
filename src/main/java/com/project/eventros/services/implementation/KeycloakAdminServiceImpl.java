package com.project.eventros.services.implementation;

import com.project.eventros.dtos.SignupRequestDto;
import com.project.eventros.exceptions.KeycloakAdminException;
import com.project.eventros.exceptions.UserRegistrationException;
import com.project.eventros.services.KeycloakAdminService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    private final Keycloak keycloakAdminClient;

    @Value("${keycloak.realm}")
    private String realmName;

    @Override
    public String createUser(SignupRequestDto signupRequest) throws KeycloakAdminException, UserRegistrationException {
        try {
            RealmResource realmResource = keycloakAdminClient.realm(realmName);
            UsersResource usersResource = realmResource.users();

            // Check if user already exists
            if (userExists(signupRequest.getUsername(), signupRequest.getEmail())) {
                throw new UserRegistrationException("User with this username or email already exists");
            }

            // Create user representation
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(signupRequest.getUsername());
            userRepresentation.setEmail(signupRequest.getEmail());
            userRepresentation.setFirstName(signupRequest.getFirstName());
            userRepresentation.setLastName(signupRequest.getLastName());
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(false); // You may want to implement email verification

            // Create the user
            Response response = usersResource.create(userRepresentation);
            
            if (response.getStatus() == 201) {
                // Extract user ID from location header
                String locationHeader = response.getHeaderString("Location");
                String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
                
                // Set password
                setUserPassword(userId, signupRequest.getPassword());
                
                // Assign role
                assignRole(userId, signupRequest.getRole());
                
                log.info("User created successfully with ID: {}", userId);
                return userId;
            } else {
                String errorMessage = "Failed to create user. Status: " + response.getStatus();
                log.error(errorMessage);
                throw new KeycloakAdminException(errorMessage);
            }
        } catch (Exception e) {
            if (e instanceof UserRegistrationException || e instanceof KeycloakAdminException) {
                throw e;
            }
            log.error("Error creating user in Keycloak", e);
            throw new KeycloakAdminException("Failed to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public void assignRole(String userId, String roleName) throws KeycloakAdminException {
        try {
            RealmResource realmResource = keycloakAdminClient.realm(realmName);
            UsersResource usersResource = realmResource.users();
            RolesResource rolesResource = realmResource.roles();

            // Get the role representation
            RoleRepresentation roleRepresentation = rolesResource.get(roleName).toRepresentation();
            
            // Assign the role to the user
            usersResource.get(userId).roles().realmLevel()
                    .add(Collections.singletonList(roleRepresentation));
            
            log.info("Role {} assigned to user {}", roleName, userId);
        } catch (Exception e) {
            log.error("Error assigning role {} to user {}", roleName, userId, e);
            throw new KeycloakAdminException("Failed to assign role: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean userExists(String username, String email) throws KeycloakAdminException {
        try {
            RealmResource realmResource = keycloakAdminClient.realm(realmName);
            UsersResource usersResource = realmResource.users();

            // Check by username
            List<UserRepresentation> usersByUsername = usersResource.search(username, true);
            if (!usersByUsername.isEmpty()) {
                return true;
            }

            // Check by email
            List<UserRepresentation> usersByEmail = usersResource.search(null, email, null, null, 0, 1);
            return !usersByEmail.isEmpty();
        } catch (Exception e) {
            log.error("Error checking if user exists", e);
            throw new KeycloakAdminException("Failed to check user existence: " + e.getMessage(), e);
        }
    }

    private void setUserPassword(String userId, String password) throws KeycloakAdminException {
        try {
            RealmResource realmResource = keycloakAdminClient.realm(realmName);
            UsersResource usersResource = realmResource.users();

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);

            usersResource.get(userId).resetPassword(credential);
            log.info("Password set for user {}", userId);
        } catch (Exception e) {
            log.error("Error setting password for user {}", userId, e);
            throw new KeycloakAdminException("Failed to set password: " + e.getMessage(), e);
        }
    }
}
