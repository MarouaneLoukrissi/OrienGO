package com.example.oriengo.exceptionHandler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * Timestamp de l'erreur
     */
    private LocalDateTime timestamp;
    
    /**
     * Code de statut HTTP
     */
    private int status;
    
    /**
     * Type d'erreur (ex: "Not Found", "Bad Request")
     */
    private String error;
    
    /**
     * Message d'erreur détaillé
     */
    private String message;
    
    /**
     * Chemin de la requête qui a causé l'erreur
     */
    private String path;
    
    /**
     * Erreurs de validation (optionnel)
     */
    private Map<String, String> validationErrors;
    
    /**
     * Trace de l'erreur (optionnel, pour le développement)
     */
    private String trace;
} 