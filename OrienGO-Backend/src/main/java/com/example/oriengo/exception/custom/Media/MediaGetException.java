package com.example.oriengo.exception.custom.Media;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MediaGetException extends ResponseStatusException {
    public MediaGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
