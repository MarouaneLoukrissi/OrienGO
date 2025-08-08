package com.example.oriengo.exception.handler;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.Privilege.PrivilegeCreationException;
import com.example.oriengo.exception.custom.Privilege.PrivilegeDeleteException;
import com.example.oriengo.exception.custom.Privilege.PrivilegeGetException;
import com.example.oriengo.exception.custom.Privilege.PrivilegeUpdateException;
import com.example.oriengo.exception.custom.Role.RoleCreationException;
import com.example.oriengo.exception.custom.Role.RoleDeleteException;
import com.example.oriengo.exception.custom.Role.RoleGetException;
import com.example.oriengo.exception.custom.Role.RoleUpdateException;
import com.example.oriengo.exception.custom.Token.TokenCreationException;
import com.example.oriengo.exception.custom.Token.TokenDeleteException;
import com.example.oriengo.exception.custom.Token.TokenGetException;
import com.example.oriengo.exception.custom.Token.TokenUpdateException;
import com.example.oriengo.exception.custom.user.*;
import com.example.oriengo.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@Order(2)
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource; // Injected

    @ExceptionHandler(UserRestoreException.class)
    public ResponseEntity<?> handleUserRestoreException(UserRestoreException ex) {
        return buildResponse("USER_RESTORE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserCreationException(UserCreationException ex) {
        return buildResponse("USER_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<?> handleUserUpdateException(UserUpdateException ex) {
        return buildResponse("USER_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(UserDeleteException.class)
    public ResponseEntity<?> handleUserDeleteException(UserDeleteException ex) {
        return buildResponse("USER_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(UserGetException.class)
    public ResponseEntity<?> handleUserGetException(UserGetException ex) {
        return buildResponse("USER_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(RoleCreationException.class)
    public ResponseEntity<?> handleRoleCreationException(RoleCreationException ex) {
        return buildResponse("ROLE_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(RoleUpdateException.class)
    public ResponseEntity<?> handleRoleUpdateException(RoleUpdateException ex) {
        return buildResponse("ROLE_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(RoleDeleteException.class)
    public ResponseEntity<?> handleRoleDeleteException(RoleDeleteException ex) {
        return buildResponse("ROLE_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(RoleGetException.class)
    public ResponseEntity<?> handleRoleGetException(RoleGetException ex) {
        return buildResponse("ROLE_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(PrivilegeCreationException.class)
    public ResponseEntity<?> handlePrivilegeCreationException(PrivilegeCreationException ex) {
        return buildResponse("PRIVILEGE_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(PrivilegeUpdateException.class)
    public ResponseEntity<?> handleRoleUpdateException(PrivilegeUpdateException ex) {
        return buildResponse("PRIVILEGE_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(PrivilegeDeleteException.class)
    public ResponseEntity<?> handleRoleDeleteException(PrivilegeDeleteException ex) {
        return buildResponse("PRIVILEGE_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(PrivilegeGetException.class)
    public ResponseEntity<?> handlePrivilegeGetException(PrivilegeGetException ex) {
        return buildResponse("PRIVILEGE_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(TokenCreationException.class)
    public ResponseEntity<?> handleTokenCreationException(TokenCreationException ex) {
        return buildResponse("TOKEN_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TokenUpdateException.class)
    public ResponseEntity<?> handleTokenUpdateException(TokenUpdateException ex) {
        return buildResponse("TOKEN_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TokenDeleteException.class)
    public ResponseEntity<?> handleTokenDeleteException(TokenDeleteException ex) {
        return buildResponse("TOKEN_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TokenGetException.class)
    public ResponseEntity<?> handleTokenGetException(TokenGetException ex) {
        return buildResponse("TOKEN_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        String message = messageSource.getMessage("error.file.too.large", null, LocaleContextHolder.getLocale());
        return buildResponse("FILE_SIZE_LIMIT_EXCEEDED", HttpStatus.PAYLOAD_TOO_LARGE, message, null);
    }
    @ExceptionHandler(PathVarException.class)
    public ResponseEntity<?> handlePathVarException(PathVarException ex) {
        return buildResponse("PATH_VAR_ERROR", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse("ACCESS_DENIED", HttpStatusCode.valueOf(401), "You do not have permission to perform this operation.", null);
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NoHandlerFoundException ex) {
        String message = messageSource.getMessage("error.404", null, LocaleContextHolder.getLocale());
        return buildResponse("NOT_FOUND", HttpStatus.NOT_FOUND, message, null);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = messageSource.getMessage("error.method.not.allowed", null, LocaleContextHolder.getLocale());
        return buildResponse("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED, message, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex); // log the stack trace safely
        String message = messageSource.getMessage(
                "internal.server.error",
                null,
                LocaleContextHolder.getLocale()
        );
        return buildResponse("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }
    private ResponseEntity<ApiResponse<Object>> buildResponse(String code, HttpStatusCode status, String message, Map<String, String> errors) {
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .code(code)
                .status(status.value())
                .message(message)
                .data(null)
                .errors(errors)
                .build();
        return ResponseEntity.status(status).body(response);
    }

}

