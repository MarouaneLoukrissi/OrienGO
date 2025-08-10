package com.example.oriengo.exception.custom.AnswerOption;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class AnswerOptionCreationException extends AppException {
    public AnswerOptionCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
