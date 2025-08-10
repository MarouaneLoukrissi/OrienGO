package com.example.oriengo.exception.custom.TrainingRecommendation;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TrainingRecommendationDeleteException extends AppException {
    public TrainingRecommendationDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
