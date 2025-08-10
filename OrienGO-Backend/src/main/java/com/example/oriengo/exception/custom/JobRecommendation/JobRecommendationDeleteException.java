package com.example.oriengo.exception.custom.JobRecommendation;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class JobRecommendationDeleteException extends AppException {
    public JobRecommendationDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
