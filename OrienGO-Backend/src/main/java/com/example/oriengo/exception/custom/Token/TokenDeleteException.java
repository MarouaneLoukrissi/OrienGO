package com.example.oriengo.exception.custom.Token;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TokenDeleteException extends AppException {
    public TokenDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
