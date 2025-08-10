package com.example.oriengo.exception.custom.Media;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class MediaDeleteException extends AppException {
    public MediaDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
