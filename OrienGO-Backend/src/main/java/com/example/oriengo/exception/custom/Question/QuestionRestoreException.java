package com.example.oriengo.exception.custom.Question;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class QuestionRestoreException extends AppException {
    public QuestionRestoreException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
