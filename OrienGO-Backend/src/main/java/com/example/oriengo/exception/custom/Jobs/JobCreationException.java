package com.example.oriengo.exception.custom.Jobs;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class JobCreationException extends AppException {
    public JobCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus ,message);
    }
}
