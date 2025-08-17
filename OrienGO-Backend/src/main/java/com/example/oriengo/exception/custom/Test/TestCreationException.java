package com.example.oriengo.exception.custom.Test;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TestCreationException extends AppException {
    public TestCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
