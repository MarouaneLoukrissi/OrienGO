package com.example.oriengo.exception.custom.Test;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TestGetException extends AppException {
    public TestGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
