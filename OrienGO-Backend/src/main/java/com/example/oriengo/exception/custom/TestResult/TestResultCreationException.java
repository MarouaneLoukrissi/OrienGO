package com.example.oriengo.exception.custom.TestResult;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TestResultCreationException extends AppException {
    public TestResultCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
