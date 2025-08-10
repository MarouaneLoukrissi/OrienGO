package com.example.oriengo.exception.custom.JobRecommendation;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class JobRecommendationGetException extends AppException {
    public JobRecommendationGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
