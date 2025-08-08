package com.example.oriengo.exception.custom.PersonalizedJob;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class PersonalizedJobUpdateException extends AppException {
    public PersonalizedJobUpdateException(HttpStatus httpStatus, String message) {
        super(httpStatus ,message);
    }
}
