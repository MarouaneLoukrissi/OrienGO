package com.example.oriengo.exception.custom.Privilege;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class PrivilegeCreationException extends AppException {
    public PrivilegeCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
