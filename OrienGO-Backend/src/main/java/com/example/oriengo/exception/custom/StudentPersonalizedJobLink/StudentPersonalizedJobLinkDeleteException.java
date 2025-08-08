package com.example.oriengo.exception.custom.StudentPersonalizedJobLink;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class StudentPersonalizedJobLinkDeleteException extends AppException {
    public StudentPersonalizedJobLinkDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
