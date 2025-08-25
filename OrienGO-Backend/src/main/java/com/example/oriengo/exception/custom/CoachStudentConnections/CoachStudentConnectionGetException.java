package com.example.oriengo.exception.custom.CoachStudentConnections;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class CoachStudentConnectionGetException extends AppException {
    public CoachStudentConnectionGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
