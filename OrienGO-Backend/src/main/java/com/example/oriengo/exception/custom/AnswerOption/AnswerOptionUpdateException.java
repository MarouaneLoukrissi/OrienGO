package com.example.oriengo.exception.custom.AnswerOption;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class AnswerOptionUpdateException extends AppException {
    public AnswerOptionUpdateException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
