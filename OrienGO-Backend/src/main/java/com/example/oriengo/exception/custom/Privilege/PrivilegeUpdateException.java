package com.example.oriengo.exception.custom.Privilege;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class PrivilegeUpdateException extends AppException {
    public PrivilegeUpdateException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
