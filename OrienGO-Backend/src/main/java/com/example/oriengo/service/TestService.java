package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.Test.*;
import com.example.oriengo.exception.custom.TestResult.TestResultCreationException;
import com.example.oriengo.mapper.TestMapper;
import com.example.oriengo.model.dto.TestCreateDTO;

import com.example.oriengo.model.dto.TestSaveDTO;
import com.example.oriengo.model.entity.*;
import com.example.oriengo.model.enumeration.TestStatus;
import com.example.oriengo.repository.StudentRepository;
import com.example.oriengo.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import com.example.oriengo.model.enumeration.TestType;
import com.example.oriengo.repository.QuestionRepository;
import com.example.oriengo.model.enumeration.Category;
import com.example.oriengo.repository.TestResultRepository;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class TestService {

    private final TestRepository testRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final MessageSource messageSource;
    private final TestMapper testMapper;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Transactional(readOnly = true)
    public List<Test> getAll(boolean deleted) {
        try {
            log.info("Fetching tests with isDeleted = {}", deleted);
            List<Test> tests = testRepository.findAllWithRelations(deleted);
            log.info("Found {} tests", tests.size());
            return tests;
        } catch (Exception e) {
            log.error("Failed to fetch tests with isDeleted = {}: {}", deleted, e.getMessage(), e);
            throw new TestGetException(HttpStatus.NOT_FOUND, getMessage("test.not.found"));
        }
    }

    @Transactional(readOnly = true)
    public Test getById(Long id, boolean deleted) {
        if (id == null) {
            log.warn("Attempted to fetch test with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("test.id.empty"));
        }

        log.info("Fetching test with ID: {} and isDeleted = {}", id, deleted);

        return testRepository.findByIdWithRelations(id, deleted)
                .orElseThrow(() -> {
                    log.error("Test not found with ID: {} and isDeleted = {}", id, deleted);
                    return new TestGetException(HttpStatus.NOT_FOUND, getMessage("test.not.found"));
                });
    }

    @Transactional(readOnly = true)
    public Test getById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch test with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("test.id.empty"));
        }

        log.info("Fetching test with ID: {}", id);

        return testRepository.findByIdWithRelations(id)
                .orElseThrow(() -> {
                    log.error("Test not found with ID: {}", id);
                    return new TestGetException(HttpStatus.NOT_FOUND, getMessage("test.not.found"));
                });
    }

    @Transactional(readOnly = true)
    public List<Test> getByStudentId(Long id, boolean deleted) {
        if (id == null) {
            log.warn("Attempted to fetch test with null student ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("test.id.empty"));
        }
        try {
            log.info("Fetching tests with student ID: {} and isDeleted = {}", id, deleted);
            List<Test> tests = testRepository.findAllByStudentIdWithRelations(id, deleted);
            log.info("Found {} tests", tests.size());
            return tests;
        } catch (Exception e) {
            log.error("Failed to fetch tests with student id = {} and isDeleted = {}: {}", id, deleted, e.getMessage(), e);
            throw new TestGetException(HttpStatus.NOT_FOUND, getMessage("test.not.found"));
        }
    }

    @Transactional(readOnly = true)
    public List<Test> getTestsByStudentIdAndStatus(Long studentId, String status, boolean softDeleted) {
        if (studentId == null) {
            log.warn("Attempted to fetch tests with null student ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("test.id.empty"));
        }
        TestStatus testStatus;
        try {
            testStatus = TestStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid test status provided: {}", status);
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Invalid test status: " + status);
        }
        try {
            log.info("Fetching tests for student ID: {}, with status: {}, softDeleted: {}", studentId, testStatus, softDeleted);
            List<Test> tests = testRepository.findAllByStudentIdAndStatusWithRelations(studentId, testStatus, softDeleted);
            log.info("Found {} tests for student ID {} with status {} and softDeleted = {}", tests.size(), studentId, testStatus, softDeleted);
            return tests;
        } catch (Exception e) {
            log.error("Failed to fetch tests for student ID {}, status {}, softDeleted {}: {}", studentId, testStatus, softDeleted, e.getMessage(), e);
            throw new TestGetException(HttpStatus.NOT_FOUND, getMessage("test.not.found"));
        }
    }

    @Transactional
    public Test createTest(TestCreateDTO dto) {
        if (dto == null) {
            log.warn("test request cannot be null");
            throw new TestCreationException(HttpStatus.BAD_REQUEST, getMessage("test.dto.empty"));
        }
        try {
            log.info("Starting creation of new test for student with id: {}", dto.getStudentId());

            if (dto.getStudentId() == null) {
                log.warn("Student ID cannot be null for test creation");
                throw new TestCreationException(HttpStatus.BAD_REQUEST, getMessage("test.student.id.empty"));
            }

            if (dto.getType() == null) {
                log.warn("Test type cannot be null");
                throw new TestCreationException(HttpStatus.BAD_REQUEST, getMessage("test.type.empty"));
            }

            Test test = testMapper.toEntity(dto);
            TestType type = test.getType();
            Long studentId = test.getStudent().getId();

            Student student = studentRepository.findByIdAndIsDeletedFalse(studentId)
                    .orElseThrow(() -> {
                        log.warn("Student with Id '{}' not found in database", studentId);
                        return new TestCreationException(HttpStatus.NOT_FOUND, getMessage("test.student.not.found"));
                    });

            // 2. Tirage aléatoire des questions
            List<Question> randomQuestions = new ArrayList<>();
            try {
                int questionCount = (type == TestType.FAST) ? 3 : 10;
                for (Category category : Category.values()) {
                    List<Question> categoryQuestions = questionRepository.findRandomQuestionsByCategory(category.name(), questionCount);
                    if (categoryQuestions == null || categoryQuestions.isEmpty()) {
                        log.warn("No questions found for category {}", category.name());
                        throw new TestCreationException(HttpStatus.NOT_FOUND, getMessage("test.cat.questions.not.found", category.name()));
                    }
                    randomQuestions.addAll(categoryQuestions);
                }
            } catch (TestCreationException e) {
                throw e; // Already logged
            } catch (Exception e) {
                log.error("Error fetching random questions: {}", e.getMessage(), e);
                throw new TestCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("test.questions.retrieve.error"));
            }

            // 3. Créer le Test et lier à l'étudiant
            try {
                test.setStudent(student);
                Set<TestQuestion> testQuestions = new HashSet<>();
                for (Question question : randomQuestions) {
                    TestQuestion tq = TestQuestion.builder()
                            .test(test)
                            .question(question)
                            // chosenAnswer is null at creation time, student hasn't answered yet
                            .chosenAnswer(null)
                            .build();
                    testQuestions.add(tq);
                }
                test.setTestQuestions(testQuestions);
                test.setQuestionsCount(randomQuestions.size());
                test.setDurationMinutes(0);

                test = testRepository.save(test);

                // 6. Link test to student
                student.getTests().add(test);
                studentRepository.save(student);
            } catch (Exception e) {
                log.error("Error saving test for student ID {}: {}", studentId, e.getMessage(), e);
                throw new TestCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("test.create.failed"));
            }

            log.info("Test created successfully with ID {} for student ID {}", test.getId(), studentId);

            return test;
        } catch (TestCreationException e) {
            // Already logged; just rethrow
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during test creation: {}", e.getMessage(), e);
            throw new TestCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("test.create.failed.unexpected"));
        }
    }
    @Transactional
    public void hardDeleteTest(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null test ID");
            throw new TestDeleteException(HttpStatus.BAD_REQUEST, getMessage("test.id.empty"));
        }

        try {
            log.info("Attempting hard delete for test with ID: {}", id);

            Test test = getById(id);
            testRepository.deleteById(test.getId());

            log.info("Successfully hard deleted test with ID: {}", test.getId());
        } catch (Exception e) {
            log.error("Error during hard delete of test with ID {}: {}", id, e.getMessage(), e);
            throw new TestDeleteException(HttpStatus.CONFLICT, getMessage("test.hard.delete.failed"));
        }
    }

    @Transactional
    public void softDeleteTest(Long id) {
        if (id == null) {
            log.warn("Attempted soft delete with null test ID");
            throw new TestDeleteException(HttpStatus.BAD_REQUEST, getMessage("test.id.empty"));
        }

        try {
            log.info("Attempting soft delete for test with ID: {}", id);

            Test test = getById(id, false);
            test.setSoftDeleted(true);
            testRepository.save(test);

            log.info("Successfully soft deleted test with ID: {}", test.getId());
        } catch (Exception e) {
            log.error("Error during soft delete of test with ID {}: {}", id, e.getMessage(), e);
            throw new TestDeleteException(HttpStatus.CONFLICT, getMessage("test.soft.delete.failed"));
        }
    }

    @Transactional
    public Test restoreTest(Long id) {
        if (id == null) {
            log.warn("Attempted restore with null test ID");
            throw new TestRestoreException(HttpStatus.BAD_REQUEST, getMessage("test.id.empty"));
        }

        try {
            log.info("Attempting restore for test with ID: {}", id);
            Test test = getById(id, true);
            test.setSoftDeleted(false);
            testRepository.save(test);
            log.info("Successfully restore test with ID: {}", test.getId());
            return test;
        } catch (Exception e) {
            log.error("Error during restore of test with ID {}: {}", id, e.getMessage(), e);
            throw new TestRestoreException(HttpStatus.BAD_REQUEST, getMessage("test.restore.failed"));
        }
    }

    @Transactional
    public Test saveTest(TestSaveDTO dto) {
        if (dto == null) {
            log.warn("test request is null");
            throw new TestCreationException(HttpStatus.BAD_REQUEST, getMessage("test.dto.empty"));
        }

        Long testId = dto.getTestId();
        Map<Long, Integer> answers = dto.getAnswers();
        Integer durationMinutes = dto.getDurationMinutes();

        try {
            log.info("Saving uncompleted test with ID: {}", testId);

            if (testId == null) {
                log.warn("test ID cannot be null for test saving");
                throw new TestCreationException(HttpStatus.BAD_REQUEST, getMessage("test.id.empty"));
            }

            if (answers == null) {
                log.warn("Test answers can't be null");
                throw new TestCreationException(HttpStatus.BAD_REQUEST, getMessage("test.answers.empty"));
            }

            if (durationMinutes == null) {
                log.warn("Test duration can't be null");
                throw new TestCreationException(HttpStatus.BAD_REQUEST, getMessage("test.duration.empty"));
            }

            Test test = testRepository.findByIdWithRelations(testId, false)
                    .orElseThrow(() -> {
                        log.error("Test not found with ID: {}", testId);
                        return new TestResultCreationException(HttpStatus.NOT_FOUND, getMessage("test.not.found", testId));
                    });

            // Update chosen answers in TestQuestion entities
            for (TestQuestion tq : test.getTestQuestions()) {
                Long questionId = tq.getQuestion().getId();
                Integer answerIndex = answers.get(questionId);

                if (answerIndex != null) {
                    AnswerOption chosenAnswerOption = tq.getQuestion().getAnswerOptions().stream()
                            .filter(ao -> ao.getOptionIndex().equals(answerIndex)) // assuming answers are 1-based, optionIndex 0-based
                            .findFirst()
                            .orElseThrow(() -> new TestCreationException(HttpStatus.BAD_REQUEST,
                                    getMessage("test.answer.option.not.found.for.question", answerIndex, questionId)));

                    tq.setChosenAnswer(chosenAnswerOption);
                } else {
                    tq.setChosenAnswer(null);
                }
            }

            // Update durationMinutes if provided
            test.setDurationMinutes(durationMinutes);

            // Mark test status as PENDING (or IN_PROGRESS if you prefer)
            if(dto.getStatus() != null) {
                test.setStatus(dto.getStatus());
            } else{
                test.setStatus(TestStatus.PENDING);
            }

            if(dto.getCompletedAt() != null) {
                test.setCompletedAt(dto.getCompletedAt());
            }

            test.setAnsweredQuestionsCount(dto.getAnsweredQuestionsCount());

            // Save the test and cascade save the updated TestQuestions
            Test savedTest = testRepository.save(test);

            log.info("Uncompleted test saved successfully with ID: {}", savedTest.getId());
            return savedTest;

        } catch (Exception e) {
            log.error("Error while saving uncompleted test with ID: {}", testId, e);
            throw new TestSaveException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messageSource.getMessage("test.save.uncompleted.error", new Object[]{testId}, LocaleContextHolder.getLocale())
            );
        }
    }
}
