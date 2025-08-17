package com.example.oriengo.exception.custom.Training;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TrainingCreationException extends AppException {
    public TrainingCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
