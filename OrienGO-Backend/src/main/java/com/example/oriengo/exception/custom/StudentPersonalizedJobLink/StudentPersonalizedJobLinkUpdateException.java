package com.example.oriengo.exception.custom.StudentPersonalizedJobLink;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class StudentPersonalizedJobLinkUpdateException extends AppException {
    public StudentPersonalizedJobLinkUpdateException(HttpStatus httpStatus, String message) {
        super(httpStatus ,message);
    }
}
