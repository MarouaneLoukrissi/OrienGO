package com.example.oriengo.exception.custom.Question;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class QuestionDeleteException extends AppException {
    public QuestionDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
