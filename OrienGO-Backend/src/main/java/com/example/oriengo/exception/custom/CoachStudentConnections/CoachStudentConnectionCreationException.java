package com.example.oriengo.exception.custom.CoachStudentConnections;


import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class CoachStudentConnectionCreationException extends AppException {
    public CoachStudentConnectionCreationException(HttpStatus httpStatus, String message) {
        super(httpStatus ,message);
    }
}
