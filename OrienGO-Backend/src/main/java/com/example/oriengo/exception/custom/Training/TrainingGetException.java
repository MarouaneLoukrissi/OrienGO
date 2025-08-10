package com.example.oriengo.exception.custom.Training;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TrainingGetException extends ResponseStatusException {
    public TrainingGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
