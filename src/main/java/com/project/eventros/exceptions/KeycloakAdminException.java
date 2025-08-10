package com.project.eventros.exceptions;

public class KeycloakAdminException extends Exception {
    public KeycloakAdminException(String message) {
        super(message);
    }
    
    public KeycloakAdminException(String message, Throwable cause) {
        super(message, cause);
    }
}
