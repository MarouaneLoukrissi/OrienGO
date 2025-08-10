package com.example.oriengo.exception.custom.StudentJobLink;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class StudentJobLinkGetException extends AppException {
    public StudentJobLinkGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
