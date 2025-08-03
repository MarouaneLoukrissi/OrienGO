package com.example.oriengo.exception.Privilege;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PrivilegeGetException extends ResponseStatusException {
    public PrivilegeGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
