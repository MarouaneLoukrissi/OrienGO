package com.example.oriengo.exception.handler;

import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationCreationException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationDeleteException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationGetException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationUpdateException;
import com.example.oriengo.exception.custom.Jobs.JobCreationException;
import com.example.oriengo.exception.custom.Jobs.JobDeleteException;
import com.example.oriengo.exception.custom.Jobs.JobGetException;
import com.example.oriengo.exception.custom.Jobs.JobUpdateException;
import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobCreationException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobDeleteException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobGetException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobUpdateException;
import com.example.oriengo.exception.custom.StudentJobLink.StudentJobLinkCreationException;
import com.example.oriengo.exception.custom.StudentJobLink.StudentJobLinkDeleteException;
import com.example.oriengo.exception.custom.StudentJobLink.StudentJobLinkGetException;
import com.example.oriengo.exception.custom.StudentJobLink.StudentJobLinkUpdateException;
import com.example.oriengo.exception.custom.StudentPersonalizedJobLink.StudentPersonalizedJobLinkCreationException;
import com.example.oriengo.exception.custom.StudentPersonalizedJobLink.StudentPersonalizedJobLinkDeleteException;
import com.example.oriengo.exception.custom.StudentPersonalizedJobLink.StudentPersonalizedJobLinkGetException;
import com.example.oriengo.exception.custom.StudentPersonalizedJobLink.StudentPersonalizedJobLinkUpdateException;
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
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@Order(2)
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource; // Injected

    @ExceptionHandler(JobCreationException.class)
    public ResponseEntity<?> handleJobCreationException(JobCreationException ex) {
        return buildResponse("JOB_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(JobDeleteException.class)
    public ResponseEntity<?> handleJobDeleteException(JobDeleteException ex) {
        return buildResponse("JOB_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(JobUpdateException.class)
    public ResponseEntity<?> handleJobUpdateException(JobUpdateException ex) {
        return buildResponse("JOB_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(JobGetException.class)
    public ResponseEntity<?> handleJobGetException(JobGetException ex) {
        return buildResponse("JOB_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(JobRecommendationCreationException.class)
    public ResponseEntity<?> handleJobRecommendationCreationException(JobRecommendationCreationException ex) {
        return buildResponse("JOB_RECOMMENDATION_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(JobRecommendationUpdateException.class)
    public ResponseEntity<?> handleJobRecommendationUpdateException(JobRecommendationUpdateException ex) {
        return buildResponse("JOB_RECOMMENDATION_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(JobRecommendationDeleteException.class)
    public ResponseEntity<?> handleJobRecommendationDeleteException(JobRecommendationDeleteException ex) {
        return buildResponse("JOB_RECOMMENDATION_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(JobRecommendationGetException.class)
    public ResponseEntity<?> handleJobRecommendationGetException(JobRecommendationGetException ex) {
        return buildResponse("JOB_RECOMMENDATION_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(PersonalizedJobCreationException.class)
    public ResponseEntity<?> handlePersonalizedJobCreationException(PersonalizedJobCreationException ex) {
        return buildResponse("PERSONALIZED_JOB_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(PersonalizedJobUpdateException.class)
    public ResponseEntity<?> handleJobRecommendationUpdateException(PersonalizedJobUpdateException ex) {
        return buildResponse("PERSONALIZED_JOB_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(PersonalizedJobDeleteException.class)
    public ResponseEntity<?> handleJobRecommendationDeleteException(PersonalizedJobDeleteException ex) {
        return buildResponse("PERSONALIZED_JOB_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(PersonalizedJobGetException.class)
    public ResponseEntity<?> handlePersonalizedJobGetException(PersonalizedJobGetException ex) {
        return buildResponse("PERSONALIZED_JOB_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(StudentJobLinkCreationException.class)
    public ResponseEntity<?> handleStudentJobLinkCreationException(StudentJobLinkCreationException ex) {
        return buildResponse("STUDENT_JOB_LINK_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(StudentJobLinkUpdateException.class)
    public ResponseEntity<?> handleStudentJobLinkUpdateException(StudentJobLinkUpdateException ex) {
        return buildResponse("STUDENT_JOB_LINK_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(StudentJobLinkDeleteException.class)
    public ResponseEntity<?> handleStudentJobLinkDeleteException(StudentJobLinkDeleteException ex) {
        return buildResponse("STUDENT_JOB_LINK_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(StudentJobLinkGetException.class)
    public ResponseEntity<?> handleStudentJobLinkGetException(StudentJobLinkGetException ex) {
        return buildResponse("STUDENT_JOB_LINK_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(StudentPersonalizedJobLinkCreationException.class)
    public ResponseEntity<?> handleStudentPersonalizedJobLinkCreationException(StudentPersonalizedJobLinkCreationException ex) {
        return buildResponse("STUDENT_PERSONALIZED_JOB_LINK_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(StudentPersonalizedJobLinkUpdateException.class)
    public ResponseEntity<?> handleStudentPersonalizedJobLinkUpdateException(StudentPersonalizedJobLinkUpdateException ex) {
        return buildResponse("STUDENT_PERSONALIZED_JOB_LINK_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(StudentPersonalizedJobLinkDeleteException.class)
    public ResponseEntity<?> handleStudentPersonalizedJobLinkDeleteException(StudentPersonalizedJobLinkDeleteException ex) {
        return buildResponse("STUDENT_PERSONALIZED_LINK_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(StudentPersonalizedJobLinkGetException.class)
    public ResponseEntity<?> handleStudentPersonalizedJobLinkGetException(StudentPersonalizedJobLinkGetException ex) {
        return buildResponse("STUDENT_PERSONALIZED_LINK_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
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

