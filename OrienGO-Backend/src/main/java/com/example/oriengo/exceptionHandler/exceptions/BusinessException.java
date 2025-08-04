package com.example.oriengo.exceptionHandler.exceptions;

/**
 * Exception pour les erreurs de logique métier
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 