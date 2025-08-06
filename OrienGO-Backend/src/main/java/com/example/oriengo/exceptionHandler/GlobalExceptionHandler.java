//package com.example.oriengo.exceptionHandler;
//
//import jakarta.validation.ConstraintViolationException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    // === Validation @RequestBody @Valid ===
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request) {
//
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors()
//                .forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));
//
//        Map<String,Object> body = Map.of(
//                "message", "Validation failed",
//                "errors", errors
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(body);
//    }
//
//    // === Validation @PathVariable / @RequestParam @Validated ===
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getConstraintViolations()
//                .forEach(cv -> errors.put(cv.getPropertyPath().toString(), cv.getMessage()));
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(Map.of(
//                        "message", "Constraint violation",
//                        "errors", errors
//                ));
//    }
//
//    // === Not found / custom runtime exceptions ===
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Object> handleNotFound(RuntimeException ex) {
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(Map.of("message", ex.getMessage()));
//    }
//
//    // === Fallback toutes exceptions ===
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleAll(Exception ex) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(Map.of(
//                        "message", "Internal error",
//                        "details", ex.getMessage()
//                ));
//    }
//}
