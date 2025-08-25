package com.example.oriengo.exception.custom.CoachStudentConnections;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class CoachStudentConnectionDeleteException extends AppException {
    public CoachStudentConnectionDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
