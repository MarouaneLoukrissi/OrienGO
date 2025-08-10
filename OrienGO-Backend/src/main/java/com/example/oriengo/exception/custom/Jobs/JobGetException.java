package com.example.oriengo.exception.custom.Jobs;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class JobGetException extends AppException {
    public JobGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
