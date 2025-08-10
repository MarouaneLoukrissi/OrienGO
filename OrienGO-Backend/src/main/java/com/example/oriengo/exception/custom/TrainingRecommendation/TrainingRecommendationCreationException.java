package com.example.oriengo.exception.custom.TrainingRecommendation;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TrainingRecommendationCreationException extends AppException {
    public TrainingRecommendationCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
