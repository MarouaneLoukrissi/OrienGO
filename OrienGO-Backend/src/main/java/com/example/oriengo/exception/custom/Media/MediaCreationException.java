package com.example.oriengo.exception.custom.Media;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class MediaCreationException extends AppException {
    public MediaCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
