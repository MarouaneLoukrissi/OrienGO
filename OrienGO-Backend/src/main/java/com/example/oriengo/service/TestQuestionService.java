package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.TestQuestion.TestQuestionGetException;
import com.example.oriengo.model.entity.Test;
import com.example.oriengo.model.entity.TestQuestion;
import com.example.oriengo.repository.TestQuestionRepository;
import com.example.oriengo.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class TestQuestionService {
    private final TestQuestionRepository testQuestionRepository;
    private final TestRepository testRepository;
    private final MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Transactional(readOnly = true)
    public Set<TestQuestion> getTestQuestionsWithResponsesByTestId(Long testId) {
        if (testId == null) {
            log.warn("Attempted to fetch test questions with null test ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("test.questions.id.empty"));
        }

        try {
            log.info("Fetching test questions with responses for test ID: {}", testId);
            // ✅ 1. Check if test exists & is not soft deleted
            Test test = testRepository.findByIdWithRelations(testId)
                    .orElseThrow(() -> new TestQuestionGetException(
                            HttpStatus.NOT_FOUND,
                            getMessage("test.not.found", testId)
                    ));

            if (test.isSoftDeleted()) { // or check test.getDeletedAt() != null
                log.warn("Test ID {} is soft deleted. Returning empty set.", testId);
                return Collections.emptySet();
            }
            Set<TestQuestion> testQuestions =
                    testQuestionRepository.findAllByTestIdWithQuestionAndChosenAnswer(testId);

            if (testQuestions.isEmpty()) {
                log.warn("No test questions found for test ID: {}", testId);
                throw new TestQuestionGetException(HttpStatus.NOT_FOUND, getMessage("test.questions.not.found", testId));
            }

            log.info("Found {} test questions for test ID: {}", testQuestions.size(), testId);
            return testQuestions;
        } catch (PathVarException | TestQuestionGetException e) {
            throw e; // Already logged
        } catch (Exception e) {
            log.error("Unexpected error fetching test questions for test ID {}: {}", testId, e.getMessage(), e);
            throw new TestQuestionGetException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("test.questions.fetch.failed"));
        }
    }
}
