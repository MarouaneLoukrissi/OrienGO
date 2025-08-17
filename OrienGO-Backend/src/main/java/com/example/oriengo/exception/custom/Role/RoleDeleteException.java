package com.example.oriengo.exception.custom.Role;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class RoleDeleteException extends AppException {
    public RoleDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
