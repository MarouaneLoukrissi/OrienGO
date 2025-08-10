package com.example.oriengo.exception.custom.Privilege;

import com.example.oriengo.exception.custom.AppException;
import org.springframework.http.HttpStatus;

public class PrivilegeDeleteException extends AppException {
    public PrivilegeDeleteException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
