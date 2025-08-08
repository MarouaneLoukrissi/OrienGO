package com.example.oriengo.exception.custom.Role;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class RoleUpdateException extends AppException {
    public RoleUpdateException(HttpStatus httpStatus,  String message) {
        super(httpStatus, message);
    }
}
