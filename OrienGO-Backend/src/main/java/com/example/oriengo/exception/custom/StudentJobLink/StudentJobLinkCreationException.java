package com.example.oriengo.exception.custom.StudentJobLink;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class StudentJobLinkCreationException extends AppException {
    public StudentJobLinkCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus ,message);
    }
}
