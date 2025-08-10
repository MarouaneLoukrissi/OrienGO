package com.example.oriengo.exception.custom.Test;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class TestUpdateException extends AppException {
    public TestUpdateException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
