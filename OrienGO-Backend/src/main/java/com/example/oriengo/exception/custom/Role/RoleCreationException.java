package com.example.oriengo.exception.custom.Role;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class RoleCreationException extends AppException {
    public RoleCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
