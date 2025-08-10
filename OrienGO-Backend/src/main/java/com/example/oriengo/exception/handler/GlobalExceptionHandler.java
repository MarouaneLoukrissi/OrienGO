package com.example.oriengo.exception.handler;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.Test.TestCreationException;
import com.example.oriengo.exception.custom.Test.TestDeleteException;
import com.example.oriengo.exception.custom.Test.TestGetException;
import com.example.oriengo.exception.custom.Test.TestUpdateException;
import com.example.oriengo.exception.custom.TestResult.TestResultCreationException;
import com.example.oriengo.exception.custom.TestResult.TestResultDeleteException;
import com.example.oriengo.exception.custom.TestResult.TestResultGetException;
import com.example.oriengo.exception.custom.TestResult.TestResultUpdateException;
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
import java.util.Map;

@Order(2)
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource; // Injected

//    @ExceptionHandler(QuestionRestoreException.class)
//    public ResponseEntity<?> handleTestRestoreException(QuestionRestoreException ex) {
//        return buildResponse("TEST_RESTORE_FAILED", ex.getStatusCode(), ex.getReason(), null);
//    }

    @ExceptionHandler(TestCreationException.class)
    public ResponseEntity<?> handleTestCreationException(TestCreationException ex) {
        return buildResponse("TEST_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TestUpdateException.class)
    public ResponseEntity<?> handleTestUpdateException(TestUpdateException ex) {
        return buildResponse("TEST_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TestDeleteException.class)
    public ResponseEntity<?> handleTestDeleteException(TestDeleteException ex) {
        return buildResponse("TEST_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TestGetException.class)
    public ResponseEntity<?> handleTestGetException(TestGetException ex) {
        return buildResponse("TEST_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(TestResultCreationException.class)
    public ResponseEntity<?> handleTestResultCreationException(TestResultCreationException ex) {
        return buildResponse("TEST_RESULT_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TestResultUpdateException.class)
    public ResponseEntity<?> handleTestResultUpdateException(TestResultUpdateException ex) {
        return buildResponse("TEST_RESULT_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TestResultDeleteException.class)
    public ResponseEntity<?> handleTestResultDeleteException(TestResultDeleteException ex) {
        return buildResponse("TEST_RESULT_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TestResultGetException.class)
    public ResponseEntity<?> handleTestResultGetException(TestResultGetException ex) {
        return buildResponse("TEST_RESULT_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
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

