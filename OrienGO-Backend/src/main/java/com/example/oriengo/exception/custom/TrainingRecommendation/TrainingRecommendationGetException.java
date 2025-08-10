package com.example.oriengo.exception.custom.TrainingRecommendation;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TrainingRecommendationGetException extends ResponseStatusException {
    public TrainingRecommendationGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
