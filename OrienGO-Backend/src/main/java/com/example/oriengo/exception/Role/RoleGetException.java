package com.example.oriengo.exception.Role;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RoleGetException extends ResponseStatusException {
    public RoleGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
