package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.TrainingRecommendation.TrainingRecommendationCreationException;
import com.example.oriengo.exception.custom.TrainingRecommendation.TrainingRecommendationDeleteException;
import com.example.oriengo.exception.custom.TrainingRecommendation.TrainingRecommendationGetException;
import com.example.oriengo.exception.custom.TrainingRecommendation.TrainingRecommendationUpdateException;
import com.example.oriengo.model.dto.TrainingRecommendationRequestDTO;
import com.example.oriengo.model.entity.TestResult;
import com.example.oriengo.model.entity.Training;
import com.example.oriengo.model.entity.TrainingRecommendation;
import com.example.oriengo.repository.TestResultRepository;
import com.example.oriengo.repository.TrainingRecommendationRepository;
import com.example.oriengo.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class TrainingRecommendationService {
    private final TrainingRecommendationRepository trainingRecommendationRepository;
    private final MessageSource messageSource; // Injected
    private final TestResultRepository testResultRepository;
    private final TrainingRepository trainingRepository;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<TrainingRecommendation> getTrainingRecommendations() {
        try {
            log.info("Fetching trainingRecommendations");
            List<TrainingRecommendation> trainingRecommendations = (List<TrainingRecommendation>) trainingRecommendationRepository.findAll();
            log.info("Found {} trainingRecommendations", trainingRecommendations.size());
            return trainingRecommendations;
        } catch (Exception e) {
            log.error("Failed to fetch trainingRecommendations}: {}", e.getMessage(), e);
            throw new TrainingRecommendationGetException(HttpStatus.NOT_FOUND, getMessage("trainingRecommendation.not.found"));
        }
    }

    public TrainingRecommendation getTrainingRecommendationById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch trainingRecommendation with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("trainingRecommendation.id.empty"));
        }

        log.info("Fetching trainingRecommendation with ID: {}", id);

        return trainingRecommendationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("TrainingRecommendation not found with ID: {}", id);
                    return new TrainingRecommendationGetException(HttpStatus.NOT_FOUND, getMessage("trainingRecommendation.not.found"));
                });
    }

    @Transactional
    public void hardDeleteTrainingRecommendation(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null trainingRecommendation ID");
            throw new TrainingRecommendationDeleteException(HttpStatus.BAD_REQUEST, getMessage("trainingRecommendation.id.empty"));
        }

        try {
            log.info("Attempting hard delete for trainingRecommendation with ID: {}", id);

            TrainingRecommendation trainingRecommendation = getTrainingRecommendationById(id);
            trainingRecommendationRepository.deleteById(trainingRecommendation.getId());

            log.info("Successfully hard deleted trainingRecommendation with ID: {}", trainingRecommendation.getId());
        } catch (Exception e) {
            log.error("Error during hard delete of trainingRecommendation with ID {}: {}", id, e.getMessage(), e);
            throw new TrainingRecommendationDeleteException(HttpStatus.CONFLICT, getMessage("trainingRecommendation.hard.delete.failed"));
        }
    }

    @Transactional
    public TrainingRecommendation createTrainingRecommendation(TrainingRecommendationRequestDTO dto) {
        if (dto == null) {
            log.warn("TrainingRecommendation DTO cannot be null");
            throw new TrainingRecommendationCreationException(
                    HttpStatus.BAD_REQUEST, getMessage("trainingRecommendation.dto.empty")
            );
        }

        try {
            log.info("Starting creation of new TrainingRecommendation for TestResult ID: {} and Training ID: {}",
                    dto.getTestResultId(), dto.getTrainingId());

            // Validate TestResult exists
            TestResult testResult = testResultRepository.findById(dto.getTestResultId())
                    .orElseThrow(() -> {
                        log.warn("TestResult not found with ID: {}", dto.getTestResultId());
                        return new TrainingRecommendationCreationException(
                                HttpStatus.NOT_FOUND, getMessage("trainingRecommendation.testResult.not.found", dto.getTestResultId())
                        );
                    });

            // Validate Training exists
            Training training = trainingRepository.findById(dto.getTrainingId())
                    .orElseThrow(() -> {
                        log.warn("Training not found with ID: {}", dto.getTrainingId());
                        return new TrainingRecommendationCreationException(
                                HttpStatus.NOT_FOUND, getMessage("trainingRecommendation.training.not.found", dto.getTrainingId())
                        );
                    });

            // Create entity
            TrainingRecommendation trainingRecommendation = TrainingRecommendation.builder()
                    .testResult(testResult)
                    .training(training)
                    .matchPercentage(dto.getMatchPercentage())
                    .highlighted(dto.isHighlighted())
                    .build();

            // Save entity
            TrainingRecommendation savedTrainingRecommendation =
                    trainingRecommendationRepository.save(trainingRecommendation);

            log.info("TrainingRecommendation created successfully with ID: {}",
                    savedTrainingRecommendation.getId());

            return savedTrainingRecommendation;

        } catch (TrainingRecommendationCreationException e) {
            throw e; // already logged
        } catch (Exception e) {
            log.error("Unexpected error during TrainingRecommendation creation: {}", e.getMessage(), e);
            throw new TrainingRecommendationCreationException(
                    HttpStatus.INTERNAL_SERVER_ERROR, getMessage("trainingRecommendation.create.failed")
            );
        }
    }



    @Transactional
    public TrainingRecommendation updateTrainingRecommendation(Long id, TrainingRecommendationRequestDTO dto) {
        if (id == null) {
            log.warn("Attempted to update TrainingRecommendation with null ID");
            throw new PathVarException(
                    HttpStatus.BAD_REQUEST, getMessage("trainingRecommendation.id.empty"));
        }
        if (dto == null) {
            log.warn("TrainingRecommendation update request cannot be null");
            throw new TrainingRecommendationUpdateException(HttpStatus.BAD_REQUEST, getMessage("trainingRecommendation.dto.empty")
            );
        }

        try {
            log.info("Updating TrainingRecommendation with ID: {}", id);

            TrainingRecommendation existing = trainingRecommendationRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("TrainingRecommendation not found with ID: {}", id);
                        return new TrainingRecommendationUpdateException(
                                HttpStatus.NOT_FOUND,
                                getMessage("trainingRecommendation.not.found", id)
                        );
                    });

            // Update TestResult if changed
            if (dto.getTestResultId() != null &&
                    (existing.getTestResult() == null ||
                            !existing.getTestResult().getId().equals(dto.getTestResultId()))) {

                TestResult testResult = testResultRepository.findById(dto.getTestResultId())
                        .orElseThrow(() -> {
                            log.warn("TestResult not found with ID: {}", dto.getTestResultId());
                            return new TrainingRecommendationUpdateException(
                                    HttpStatus.NOT_FOUND,
                                    getMessage("trainingRecommendation.testResult.not.found", dto.getTestResultId())
                            );
                        });
                existing.setTestResult(testResult);
            }

            // Update Training if changed
            if (dto.getTrainingId() != null &&
                    (existing.getTraining() == null ||
                            !existing.getTraining().getId().equals(dto.getTrainingId()))) {

                Training training = trainingRepository.findById(dto.getTrainingId())
                        .orElseThrow(() -> {
                            log.warn("Training not found with ID: {}", dto.getTrainingId());
                            return new TrainingRecommendationUpdateException(
                                    HttpStatus.NOT_FOUND,
                                    getMessage("trainingRecommendation.training.not.found", dto.getTrainingId())
                            );
                        });
                existing.setTraining(training);
            }

            // Update match percentage
            if (dto.getMatchPercentage() != null) {
                existing.setMatchPercentage(dto.getMatchPercentage());
            }

            // Update highlighted flag
            existing.setHighlighted(dto.isHighlighted());

            TrainingRecommendation saved = trainingRecommendationRepository.save(existing);

            log.info("TrainingRecommendation with ID {} successfully updated", saved.getId());
            return saved;

        } catch (TrainingRecommendationUpdateException e) {
            throw e; // Already logged
        } catch (Exception e) {
            log.error("Error updating TrainingRecommendation with ID {}: {}", id, e.getMessage(), e);
            throw new TrainingRecommendationUpdateException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    getMessage("trainingRecommendation.update.failed")
            );
        }
    }

}
