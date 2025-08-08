package com.example.oriengo.exception.custom.user;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class UserCreationException extends AppException {
    public UserCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus ,message);
    }
}
