package com.example.oriengo.exception.custom.PersonalizedJob;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class PersonalizedJobGetException extends AppException {
    public PersonalizedJobGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
