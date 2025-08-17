package com.example.oriengo.exception.handler;

import com.example.oriengo.exception.custom.AnswerOption.AnswerOptionCreationException;
import com.example.oriengo.exception.custom.AnswerOption.AnswerOptionDeleteException;
import com.example.oriengo.exception.custom.AnswerOption.AnswerOptionGetException;
import com.example.oriengo.exception.custom.AnswerOption.AnswerOptionUpdateException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationCreationException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationDeleteException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationGetException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationUpdateException;
import com.example.oriengo.exception.custom.Jobs.JobCreationException;
import com.example.oriengo.exception.custom.Jobs.JobDeleteException;
import com.example.oriengo.exception.custom.Jobs.JobGetException;
import com.example.oriengo.exception.custom.Jobs.JobUpdateException;
import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.Question.*;
import com.example.oriengo.exception.custom.Test.*;
import com.example.oriengo.exception.custom.TestQuestion.TestQuestionGetException;
import com.example.oriengo.exception.custom.TestResult.TestResultCreationException;
import com.example.oriengo.exception.custom.TestResult.TestResultDeleteException;
import com.example.oriengo.exception.custom.TestResult.TestResultGetException;
import com.example.oriengo.exception.custom.TestResult.TestResultUpdateException;
import com.example.oriengo.exception.custom.Training.TrainingCreationException;
import com.example.oriengo.exception.custom.Training.TrainingDeleteException;
import com.example.oriengo.exception.custom.Training.TrainingGetException;
import com.example.oriengo.exception.custom.Training.TrainingUpdateException;
import com.example.oriengo.exception.custom.TrainingRecommendation.TrainingRecommendationCreationException;
import com.example.oriengo.exception.custom.TrainingRecommendation.TrainingRecommendationDeleteException;
import com.example.oriengo.exception.custom.TrainingRecommendation.TrainingRecommendationGetException;
import com.example.oriengo.exception.custom.TrainingRecommendation.TrainingRecommendationUpdateException;
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

    @ExceptionHandler(TestRestoreException.class)
    public ResponseEntity<?> handleTestRestoreException(TestRestoreException ex) {
        return buildResponse("TEST_RESTORE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

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

    @ExceptionHandler(TestSaveException.class)
    public ResponseEntity<?> handleTestSaveException(TestSaveException ex) {
        return buildResponse("TEST_SAVE_FAILED", ex.getStatusCode(), ex.getReason(), null);
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

    @ExceptionHandler(AnswerOptionCreationException.class)
    public ResponseEntity<?> handleAnswerOptionCreationException(AnswerOptionCreationException ex) {
        return buildResponse("ANSWER_OPTION_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(AnswerOptionUpdateException.class)
    public ResponseEntity<?> handleAnswerOptionUpdateException(AnswerOptionUpdateException ex) {
        return buildResponse("ANSWER_OPTION_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(AnswerOptionDeleteException.class)
    public ResponseEntity<?> handleAnswerOptionDeleteException(AnswerOptionDeleteException ex) {
        return buildResponse("ANSWER_OPTION_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(AnswerOptionGetException.class)
    public ResponseEntity<?> handleAnswerOptionGetException(AnswerOptionGetException ex) {
        return buildResponse("ANSWER_OPTION_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(QuestionCreationException.class)
    public ResponseEntity<?> handleQuestionCreationException(QuestionCreationException ex) {
        return buildResponse("QUESTION_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(QuestionUpdateException.class)
    public ResponseEntity<?> handleQuestionUpdateException(QuestionUpdateException ex) {
        return buildResponse("QUESTION_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(QuestionDeleteException.class)
    public ResponseEntity<?> handleQuestionDeleteException(QuestionDeleteException ex) {
        return buildResponse("QUESTION_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(QuestionGetException.class)
    public ResponseEntity<?> handleQuestionGetException(QuestionGetException ex) {
        return buildResponse("QUESTION_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(QuestionRestoreException.class)
    public ResponseEntity<?> handleQuestionRestoreException(QuestionRestoreException ex) {
        return buildResponse("QUESTION_RESTORE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(QuestionSaveException.class)
    public ResponseEntity<?> handleQuestionSaveException(QuestionSaveException ex) {
        return buildResponse("QUESTION_SAVE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(TestQuestionGetException.class)
    public ResponseEntity<?> handleTestQuestionGetException(TestQuestionGetException ex) {
        return buildResponse("TEST_QUESTION_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(TrainingCreationException.class)
    public ResponseEntity<?> handleTrainingCreationException(TrainingCreationException ex) {
        return buildResponse("TRAINING_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TrainingUpdateException.class)
    public ResponseEntity<?> handleTrainingUpdateException(TrainingUpdateException ex) {
        return buildResponse("TRAINING_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TrainingDeleteException.class)
    public ResponseEntity<?> handleTrainingDeleteException(TrainingDeleteException ex) {
        return buildResponse("TRAINING_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TrainingGetException.class)
    public ResponseEntity<?> handleTrainingGetException(TrainingGetException ex) {
        return buildResponse("TRAINING_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }

    @ExceptionHandler(TrainingRecommendationCreationException.class)
    public ResponseEntity<?> handleTrainingRecommendationCreationException(TrainingRecommendationCreationException ex) {
        return buildResponse("TRAINING_RECOMMENDATION_CREATION_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TrainingRecommendationUpdateException.class)
    public ResponseEntity<?> handleTrainingRecommendationUpdateException(TrainingRecommendationUpdateException ex) {
        return buildResponse("TRAINING_RECOMMENDATION_UPDATE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TrainingRecommendationDeleteException.class)
    public ResponseEntity<?> handleTrainingRecommendationDeleteException(TrainingRecommendationDeleteException ex) {
        return buildResponse("TRAINING_RECOMMENDATION_DELETE_FAILED", ex.getStatusCode(), ex.getReason(), null);
    }
    @ExceptionHandler(TrainingRecommendationGetException.class)
    public ResponseEntity<?> handleTrainingRecommendationGetException(TrainingRecommendationGetException ex) {
        return buildResponse("TRAINING_RECOMMENDATION_FETCH_FAILED", ex.getStatusCode(), ex.getReason(), null);
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
        String message = messageSource.getMessage("error.access.denied", null, LocaleContextHolder.getLocale());
        return buildResponse("ACCESS_DENIED", HttpStatus.UNAUTHORIZED, message, null);
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
