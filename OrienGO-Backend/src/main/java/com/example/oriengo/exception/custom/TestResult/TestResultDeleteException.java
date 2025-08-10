package com.example.oriengo.exception.custom.TestResult;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TestResultDeleteException extends AppException {
    public TestResultDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
