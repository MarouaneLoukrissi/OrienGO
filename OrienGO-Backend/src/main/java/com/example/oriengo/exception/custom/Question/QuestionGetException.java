package com.example.oriengo.exception.custom.Question;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class QuestionGetException extends AppException {
    public QuestionGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
