package com.example.oriengo.exception.handler.util;

import com.example.oriengo.exception.handler.exceptions.BusinessException;
import com.example.oriengo.exception.handler.exceptions.ValidationException;

/**
 * Utilitaire pour la gestion des exceptions
 */
public class ExceptionUtils {

    /**
     * Lance une BusinessException si la condition est vraie
     */
    public static void throwIfTrue(boolean condition, String message) {
        if (condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * Lance une BusinessException si la condition est fausse
     */
    public static void throwIfFalse(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * Lance une BusinessException si l'objet est null
     */
    public static void throwIfNull(Object object, String message) {
        if (object == null) {
            throw new BusinessException(message);
        }
    }

    /**
     * Lance une BusinessException si l'objet n'est pas null
     */
    public static void throwIfNotNull(Object object, String message) {
        if (object != null) {
            throw new BusinessException(message);
        }
    }

    /**
     * Lance une ValidationException si la condition est vraie
     */
    public static void throwValidationIfTrue(boolean condition, String message) {
        if (condition) {
            throw new ValidationException(message);
        }
    }

    /**
     * Lance une ValidationException si la condition est fausse
     */
    public static void throwValidationIfFalse(boolean condition, String message) {
        if (!condition) {
            throw new ValidationException(message);
        }
    }

    /**
     * Lance une ValidationException si l'objet est null
     */
    public static void throwValidationIfNull(Object object, String message) {
        if (object == null) {
            throw new ValidationException(message);
        }
    }

    /**
     * Lance une ValidationException si l'objet n'est pas null
     */
    public static void throwValidationIfNotNull(Object object, String message) {
        if (object != null) {
            throw new ValidationException(message);
        }
    }
} 