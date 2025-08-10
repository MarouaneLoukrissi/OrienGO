package com.example.oriengo.exception.custom.StudentJobLink;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class StudentJobLinkDeleteException extends AppException {
    public StudentJobLinkDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
