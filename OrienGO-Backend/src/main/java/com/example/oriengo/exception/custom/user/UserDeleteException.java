package com.example.oriengo.exception.custom.user;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class UserDeleteException extends AppException {
    public UserDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
