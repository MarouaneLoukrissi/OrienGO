package com.example.oriengo.exception.custom.AnswerOption;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AnswerOptionGetException extends ResponseStatusException {
    public AnswerOptionGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
