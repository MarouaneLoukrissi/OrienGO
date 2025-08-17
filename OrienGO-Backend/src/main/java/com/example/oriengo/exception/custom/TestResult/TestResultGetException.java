package com.example.oriengo.exception.custom.TestResult;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TestResultGetException extends ResponseStatusException {
    public TestResultGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
