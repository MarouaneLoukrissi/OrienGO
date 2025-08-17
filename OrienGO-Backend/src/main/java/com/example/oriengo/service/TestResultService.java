package com.example.oriengo.service;

import com.example.oriengo.exception.custom.Test.TestCreationException;
import com.example.oriengo.exception.custom.TestResult.TestResultCreationException;
import com.example.oriengo.model.dto.MediaResponseDTO;
import com.example.oriengo.model.dto.TestResultCreateDTO;
import com.example.oriengo.model.entity.*;
import com.example.oriengo.model.enumeration.Category;
import com.example.oriengo.model.enumeration.TestStatus;
import com.example.oriengo.repository.MediaRepository;
import com.example.oriengo.repository.TestRepository;
import com.example.oriengo.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class TestResultService {

    private final TestResultRepository testResultRepository;
    private final TestRepository testRepository;
    private final MessageSource messageSource;
    private final MediaRepository mediaRepository;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Transactional(readOnly = true)
    public List<TestResult> getAll() {
        try {
            log.info("Fetching all test results (not soft deleted)");
            List<TestResult> results = testResultRepository.findBySoftDeletedFalse();
            log.info("Found {} test results", results.size());
            return results;
        } catch (Exception e) {
            log.error("Failed to fetch test results: {}", e.getMessage(), e);
            throw new TestResultCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("testresult.fetch.failed"));
        }
    }

    @Transactional(readOnly = true)
    public Optional<TestResult> getById(Long id) {
        try {
            log.info("Fetching test result by ID: {}", id);
            return testResultRepository.findById(id).filter(result -> !result.isSoftDeleted());
        } catch (Exception e) {
            log.error("Error fetching test result with ID {}: {}", id, e.getMessage(), e);
            throw new TestResultCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("testresult.fetch.failed"));
        }
    }

    @Transactional(readOnly = true)
    public Optional<TestResult> getByTestId(Long testId) {
        try {
            log.info("Fetching test result by test ID: {}", testId);
            return testResultRepository.findByTestIdAndSoftDeletedFalse(testId);
        } catch (Exception e) {
            log.error("Error fetching test result for test ID {}: {}", testId, e.getMessage(), e);
            throw new TestResultCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("testresult.fetch.failed"));
        }
    }

    @Transactional(readOnly = true)
    public List<TestResult> getByStudentId(Long studentId) {
        try {
            log.info("Fetching test results by student ID: {}", studentId);
            return testResultRepository.findByStudentIdAndSoftDeletedFalse(studentId);
        } catch (Exception e) {
            log.error("Error fetching test results for student ID {}: {}", studentId, e.getMessage(), e);
            throw new TestResultCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("testresult.fetch.failed"));
        }
    }

    @Transactional
    public TestResult createUnsavedTestResult(TestResultCreateDTO dto) {
        if (dto == null) {
            log.warn("test request is null");
            throw new TestResultCreationException(HttpStatus.BAD_REQUEST, getMessage("testresult.dto.empty"));
        }

        Long testId = dto.getTestId();
        Map<Long, Integer> answers = dto.getAnswers();
        Integer durationMinutes = dto.getDurationMinutes();

        try {
            log.info("Creating TestResult for test ID {}", testId);

            if (testId == null) {
                log.warn("test ID cannot be null for test saving");
                throw new TestCreationException(HttpStatus.BAD_REQUEST, getMessage("testresult.test.id.empty"));
            }

            // Récupérer le test avec relations (questions)
            Test test = testRepository.findByIdWithRelations(testId, false)
                    .orElseThrow(() -> {
                        log.error("Test not found with ID: {}", testId);
                        return new TestResultCreationException(HttpStatus.NOT_FOUND, getMessage("testresult.test.not.found", testId));
                    });

            Set<TestQuestion> testQuestions = test.getTestQuestions();

            if (testQuestions.isEmpty()) {
                log.warn("No test questions found for test ID: {}", testId);
                throw new TestResultCreationException(HttpStatus.BAD_REQUEST, getMessage("testresult.test.no.questions"));
            }

            // Validation des réponses
            validateTestCompletion(testQuestions, answers);

            // Update TestQuestion entities with chosen answers
            for (TestQuestion tq : testQuestions) {
                Long questionId = tq.getQuestion().getId();
                Integer answerIndex = answers.get(questionId);

                if (answerIndex != null) {
                    // Find the corresponding AnswerOption by question and answer index (1-based)
                    AnswerOption chosenAnswerOption = tq.getQuestion().getAnswerOptions().stream()
                            .filter(ao -> ao.getOptionIndex().equals(answerIndex - 1)) // assuming answers map is 1-based, optionIndex is 0-based
                            .findFirst()
                            .orElseThrow(() -> new TestResultCreationException(HttpStatus.BAD_REQUEST,
                                    getMessage("testresult.answer.option.not.found.for.question", answerIndex, questionId)));

                    tq.setChosenAnswer(chosenAnswerOption);
                } else {
                    tq.setChosenAnswer(null);
                }
            }

            // Update durationMinutes if provided
            test.setDurationMinutes(durationMinutes);
            test.setCompletedAt(LocalDateTime.now());
            test.setStatus(TestStatus.COMPLETED);

            // Save updated testQuestions via cascade
            testRepository.save(test);

            // Calcul des scores
            Map<Category, Double> scores = calculateRIASECScores(testQuestions);

            // Déterminer le type dominant
            Category dominantType = determineDominantType(scores);

            TestResult testResult = TestResult.builder()
                    .test(test)
                    .dominantType(dominantType)
                    .dominantTypeDescription(dominantType.toString())
                    .scores(scores)
                    .keyPoints(getMessage("testresult.keypoints", dominantType.toString()))
                    .shared(false)
                    .downloaded(false)
                    .softDeleted(false)
                    .build();

            // Sauvegarde du résultat
            testResult = testResultRepository.save(testResult);

            // Mise à jour du test (status + date)
            test.setStatus(TestStatus.COMPLETED);
            test.setCompletedAt(LocalDateTime.now());
            testRepository.save(test);

            log.info("TestResult created successfully with ID {} for test ID {}", testResult.getId(), testId);
            return testResult;

        } catch (TestResultCreationException e) {
            // déjà loggé, on rethrow
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during TestResult creation for test ID {}: {}", testId, e.getMessage(), e);
            throw new TestResultCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("testresult.create.failed"));
        }
    }

    @Transactional
    public TestResult createSavedTestResult(Long testId) {
        if (testId == null) {
            log.warn("testId is null");
            throw new TestResultCreationException(HttpStatus.BAD_REQUEST, getMessage("testresult.test.id.empty"));
        }

        try {
            log.info("Creating TestResult for test ID {}", testId);

            // Récupérer le test avec relations (questions)
            Test test = testRepository.findByIdWithRelations(testId, false)
                    .orElseThrow(() -> {
                        log.error("Test not found with ID: {}", testId);
                        return new TestResultCreationException(HttpStatus.NOT_FOUND, getMessage("testresult.test.not.found", testId));
                    });

            Set<TestQuestion> testQuestions = test.getTestQuestions();

            if (testQuestions.isEmpty()) {
                log.warn("No test questions found for test ID: {}", testId);
                throw new TestResultCreationException(HttpStatus.BAD_REQUEST, getMessage("testresult.test.no.questions"));
            }

            // 2. Validate that each question has a chosenAnswer (optional, based on your logic)
            boolean allAnswered = testQuestions.stream()
                    .allMatch(tq -> tq.getChosenAnswer() != null);

            if (!allAnswered) {
                log.warn("Not all questions have answers for test ID: {}", testId);
                throw new TestResultCreationException(HttpStatus.BAD_REQUEST, getMessage("testresult.test.incomplete"));
            }

            // 3. Calculate scores based on chosen answers in testQuestions
            Map<Category, Double> scores = calculateRIASECScores(testQuestions);

            // 4. Determine dominant type
            Category dominantType = determineDominantType(scores);

            // 5. Update test entity
            // testRepository.save(test);

            // 6. Build and save TestResult
            TestResult testResult = TestResult.builder()
                    .test(test)
                    .dominantType(dominantType)
                    .dominantTypeDescription(dominantType.toString())
                    .scores(scores)
                    .keyPoints(getMessage("testresult.keypoints", dominantType.toString()))
                    .shared(false)
                    .downloaded(false)
                    .softDeleted(false)
                    .build();

            testResult = testResultRepository.save(testResult);

            log.info("TestResult created successfully with ID {} for test ID {}", testResult.getId(), testId);

            return testResult;

        } catch (TestResultCreationException e) {
            throw e; // already logged
        } catch (Exception e) {
            log.error("Unexpected error during TestResult creation for test ID {}: {}", testId, e.getMessage(), e);
            throw new TestResultCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("testresult.create.failed"));
        }
    }

    @Transactional
    public TestResult mapTestResultToMedia(Long id, Long mediaId) {
        if (id == null) {
            log.error("testResult Id is null");
            throw new TestResultCreationException(HttpStatus.BAD_REQUEST, getMessage("testresult.test.id.empty"));
        }

        if (mediaId == null) {
            log.error("media Id is null");
            throw new TestResultCreationException(HttpStatus.BAD_REQUEST, getMessage("media.id.empty"));
        }

        try {
            log.info("Map test result with ID {} to media with ID {}", id, mediaId);

            // Récupérer le test avec relations (questions)
            TestResult testResult = testResultRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Test result not found with ID: {}", id);
                        return new TestResultCreationException(HttpStatus.NOT_FOUND, getMessage("testresult.test.not.found", id));
                    });
            Media media = mediaRepository.findById(mediaId)
                    .orElseThrow(() -> {
                        log.error("Media not found with ID: {}", mediaId);
                        return new TestResultCreationException(HttpStatus.NOT_FOUND, getMessage("testresult.media.not.found", mediaId));
                    });

            // 6. Build and save TestResult
            testResult.setPdf(media);

            testResultRepository.save(testResult);

            log.info("Test result with ID {} successfully mapped with media ID {}", testResult.getId(), testResult.getPdf().getId());

            return testResult;

        } catch (TestResultCreationException e) {
            throw e; // already logged
        } catch (Exception e) {
            log.error("Unexpected error during test result with Id {} mapping to media with Id {}: {}", id, mediaId, e.getMessage(), e);
            throw new TestResultCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("testresult.create.failed"));
        }
    }


    private void validateTestCompletion(Set<TestQuestion> testQuestions, Map<Long, Integer> answers) {
        for (TestQuestion tq : testQuestions) {
            Long questionId = tq.getQuestion().getId();
            Integer answer = answers.get(questionId);
            if (answer == null || answer < 1 || answer > 5) {
                String msg = getMessage("testresult.incomplete.question", questionId);
                log.warn(msg);
                throw new TestResultCreationException(HttpStatus.BAD_REQUEST, msg);
            }
        }
        if (answers.size() > testQuestions.size()) {
            String msg = getMessage("testresult.too.many.answers", testQuestions.size(), answers.size());
            log.warn(msg);
            throw new TestResultCreationException(HttpStatus.BAD_REQUEST, msg);
        }
    }

    private Map<Category, Double> calculateRIASECScores(Set<TestQuestion> testQuestions) {
        Map<Category, Double> scores = new EnumMap<>(Category.class);
        Map<Category, Integer> questionCounts = new EnumMap<>(Category.class);

        for (Category category : Category.values()) {
            scores.put(category, 0.0);
            questionCounts.put(category, 0);
        }

        for (TestQuestion tq : testQuestions) {
            Question question = tq.getQuestion();
            Category category = question.getCategory();
            questionCounts.put(category, questionCounts.get(category) + 1);

            AnswerOption chosen = tq.getChosenAnswer();
            if (chosen != null) {
                // Convert optionIndex (0-based) to score (1-5)
                int score = chosen.getOptionIndex();
                scores.put(category, scores.get(category) + score);
            }
        }

        for (Category category : Category.values()) {
            double score = scores.get(category);
            int questionCount = questionCounts.get(category);

            if (questionCount > 0) {
                int maxPossibleScore = questionCount * 5;
                double percentage = (score * 100) / maxPossibleScore;
                scores.put(category, percentage);
            } else {
                scores.put(category, 0.0);
            }
        }

        return scores;
    }

    private Category determineDominantType(Map<Category, Double> scores) {
        double maxScore = scores.values().stream().mapToDouble(Double::doubleValue).max().orElse(0);

        List<Category> dominantCategories = scores.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), maxScore))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (dominantCategories.size() == 1) {
            return dominantCategories.get(0);
        }

        Category[] priorityOrder = {
                Category.REALISTIC,
                Category.INVESTIGATIVE,
                Category.ARTISTIC,
                Category.SOCIAL,
                Category.ENTERPRISING,
                Category.CONVENTIONAL
        };

        for (Category priorityCategory : priorityOrder) {
            if (dominantCategories.contains(priorityCategory)) {
                return priorityCategory;
            }
        }

        return Category.REALISTIC;
    }
}
