package com.example.oriengo.exception.custom.CoachStudentConnections;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class CoachStudentConnectionUpdateException extends AppException {
    public CoachStudentConnectionUpdateException(HttpStatus httpStatus, String message) {
        super(httpStatus ,message);
    }
}
