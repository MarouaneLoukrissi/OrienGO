package com.example.oriengo.exceptionHandler.exceptions;

/**
 * Exception pour les erreurs de validation personnalisées
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
} 