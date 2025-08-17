package com.example.oriengo.exception.custom.Question;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class QuestionCreationException extends AppException {
    public QuestionCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
