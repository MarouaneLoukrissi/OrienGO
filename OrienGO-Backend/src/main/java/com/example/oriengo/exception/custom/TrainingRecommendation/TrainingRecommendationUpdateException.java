package com.example.oriengo.exception.custom.TrainingRecommendation;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TrainingRecommendationUpdateException extends AppException {
    public TrainingRecommendationUpdateException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
