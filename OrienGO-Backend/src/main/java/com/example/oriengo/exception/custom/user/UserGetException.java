package com.example.oriengo.exception.custom.user;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class UserGetException extends AppException {
    public UserGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
