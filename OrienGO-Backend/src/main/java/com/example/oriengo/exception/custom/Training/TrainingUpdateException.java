package com.example.oriengo.exception.custom.Training;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TrainingUpdateException extends AppException {
    public TrainingUpdateException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
