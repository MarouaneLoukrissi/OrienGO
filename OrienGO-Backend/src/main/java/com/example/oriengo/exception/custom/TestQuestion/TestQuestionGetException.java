package com.example.oriengo.exception.custom.TestQuestion;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TestQuestionGetException extends AppException {
    public TestQuestionGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}