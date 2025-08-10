package com.example.oriengo.exception.custom.PersonalizedJob;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class PersonalizedJobCreationException extends AppException {
    public PersonalizedJobCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus ,message);
    }
}
