package com.example.oriengo.exception.custom.Test;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TestRestoreException extends AppException {
    public TestRestoreException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
