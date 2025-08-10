package com.example.oriengo.exception.custom.AnswerOption;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class AnswerOptionDeleteException extends AppException {
    public AnswerOptionDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
