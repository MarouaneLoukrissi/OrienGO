package com.example.oriengo.exceptionHandler;

import com.example.oriengo.exception.PathVarException;
import com.example.oriengo.exception.Privilege.PrivilegeCreationException;
import com.example.oriengo.exception.Privilege.PrivilegeDeleteException;
import com.example.oriengo.exception.Privilege.PrivilegeGetException;
import com.example.oriengo.exception.Privilege.PrivilegeUpdateException;
import com.example.oriengo.exception.Role.RoleCreationException;
import com.example.oriengo.exception.Role.RoleDeleteException;
import com.example.oriengo.exception.Role.RoleGetException;
import com.example.oriengo.exception.Role.RoleUpdateException;
import com.example.oriengo.exception.Token.TokenCreationException;
import com.example.oriengo.exception.Token.TokenDeleteException;
import com.example.oriengo.exception.Token.TokenGetException;
import com.example.oriengo.exception.Token.TokenUpdateException;
import com.example.oriengo.exception.user.UserCreationException;
import com.example.oriengo.exception.user.UserDeleteException;
import com.example.oriengo.exception.user.UserGetException;
import com.example.oriengo.exception.user.UserUpdateException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Order(2)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserCreationException(UserCreationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("USER_CREATION_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<?> handleUserUpdateException(UserUpdateException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("USER_UPDATE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(UserDeleteException.class)
    public ResponseEntity<?> handleUserDeleteException(UserDeleteException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("USER_DELETE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(UserGetException.class)
    public ResponseEntity<?> handleUserGetException(UserGetException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse("USER_FETCH_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(RoleCreationException.class)
    public ResponseEntity<?> handleRoleCreationException(RoleCreationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Role_CREATION_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(RoleUpdateException.class)
    public ResponseEntity<?> handleRoleUpdateException(RoleUpdateException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Role_UPDATE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(RoleDeleteException.class)
    public ResponseEntity<?> handleRoleDeleteException(RoleDeleteException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Role_DELETE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(RoleGetException.class)
    public ResponseEntity<?> handleRoleGetException(RoleGetException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse("Role_FETCH_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(PrivilegeCreationException.class)
    public ResponseEntity<?> handlePrivilegeCreationException(PrivilegeCreationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Privilege_CREATION_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(PrivilegeUpdateException.class)
    public ResponseEntity<?> handleRoleUpdateException(PrivilegeUpdateException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Privilege_UPDATE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(PrivilegeDeleteException.class)
    public ResponseEntity<?> handleRoleDeleteException(PrivilegeDeleteException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Privilege_DELETE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(PrivilegeGetException.class)
    public ResponseEntity<?> handlePrivilegeGetException(PrivilegeGetException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse("Privilege_FETCH_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(TokenCreationException.class)
    public ResponseEntity<?> handleTokenCreationException(TokenCreationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("TOKEN_CREATION_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(TokenUpdateException.class)
    public ResponseEntity<?> handleTokenUpdateException(TokenUpdateException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("TOKEN_UPDATE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(TokenDeleteException.class)
    public ResponseEntity<?> handleTokenDeleteException(TokenDeleteException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("TOKEN_DELETE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(TokenGetException.class)
    public ResponseEntity<?> handleTokenGetException(TokenGetException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse("TOKEN_FETCH_FAILED", ex.getMessage()));
    }



    @ExceptionHandler(PathVarException.class)
    public ResponseEntity<?> handlePathVarException(PathVarException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse("PATH_VAR_ERROR", ex.getMessage()));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("ACCESS_DENIED", "You do not have permission to perform this operation.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("GENERAL_ERROR", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}

