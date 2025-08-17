package com.example.oriengo.exception.custom.Training;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TrainingDeleteException extends AppException {
    public TrainingDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
