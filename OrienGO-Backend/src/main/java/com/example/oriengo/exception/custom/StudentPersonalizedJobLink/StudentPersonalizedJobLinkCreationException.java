package com.example.oriengo.exception.custom.StudentPersonalizedJobLink;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class StudentPersonalizedJobLinkCreationException extends AppException {
    public StudentPersonalizedJobLinkCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus ,message);
    }
}
