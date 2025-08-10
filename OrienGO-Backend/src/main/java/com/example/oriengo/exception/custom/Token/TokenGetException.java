package com.example.oriengo.exception.custom.Token;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TokenGetException extends AppException {
    public TokenGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
