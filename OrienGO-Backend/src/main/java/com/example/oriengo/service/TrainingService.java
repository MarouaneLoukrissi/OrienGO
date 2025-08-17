package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.Training.TrainingCreationException;
import com.example.oriengo.exception.custom.Training.TrainingDeleteException;
import com.example.oriengo.exception.custom.Training.TrainingGetException;
import com.example.oriengo.exception.custom.Training.TrainingUpdateException;
import com.example.oriengo.mapper.TrainingMapper;
import com.example.oriengo.model.dto.TrainingRequestDTO;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.model.entity.Training;
import com.example.oriengo.repository.JobRepository;
import com.example.oriengo.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    //    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final MessageSource messageSource; // Injected
    private final JobRepository jobRepository;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<Training> getTrainings() {
        try {
            log.info("Fetching trainings");
            List<Training> trainings = (List<Training>) trainingRepository.findAll();
            log.info("Found {} trainings", trainings.size());
            return trainings;
        } catch (Exception e) {
            log.error("Failed to fetch trainings}: {}", e.getMessage(), e);
            throw new TrainingGetException(HttpStatus.NOT_FOUND, getMessage("training.not.found"));
        }
    }

    public Training getTrainingById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch training with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("training.id.empty"));
        }

        log.info("Fetching training with ID: {}", id);

        return trainingRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Training not found with ID: {}", id);
                    return new TrainingGetException(HttpStatus.NOT_FOUND, getMessage("training.not.found"));
                });
    }

    @Transactional
    public void hardDeleteTraining(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null training ID");
            throw new TrainingDeleteException(HttpStatus.BAD_REQUEST, getMessage("training.id.empty"));
        }

        try {
            log.info("Attempting hard delete for training with ID: {}", id);

            Training training = getTrainingById(id);
            trainingRepository.deleteById(training.getId());

            log.info("Successfully hard deleted training with ID: {}", training.getId());
        } catch (Exception e) {
            log.error("Error during hard delete of training with ID {}: {}", id, e.getMessage(), e);
            throw new TrainingDeleteException(HttpStatus.CONFLICT, getMessage("training.hard.delete.failed"));
        }
    }

    @Transactional
    public Training createTraining(TrainingRequestDTO dto) {
        if (dto == null) {
            log.warn("Training DTO cannot be null");
            throw new TrainingCreationException(HttpStatus.BAD_REQUEST, getMessage("training.dto.empty"));
        }
        try {
            log.info("Starting creation of new training with name: {}", dto.getName());

            // Optional: Check if a training with the same name already exists (if desired)
            boolean exists = trainingRepository.existsByNameAndSoftDeletedFalse(dto.getName());
            if (exists) {
                log.warn("Training already exists with name: {}", dto.getName());
                throw new TrainingCreationException(HttpStatus.CONFLICT, getMessage("training.name.already.exists", dto.getName()));
            }

            // Map DTO to entity
            Training training = trainingMapper.toEntity(dto);

            // Link jobs if jobIds are provided
            if (dto.getJobIds() != null && !dto.getJobIds().isEmpty()) {
                // Fetch all jobs by IDs
                Set<Job> jobs = new HashSet<>(jobRepository.findAllById(dto.getJobIds()));

                if (jobs.size() != dto.getJobIds().size()) {
                    // Some job IDs not found, throw exception or handle as you want
                    log.warn("Some job IDs in the list were not found: {}", dto.getJobIds());
                    throw new TrainingCreationException(HttpStatus.NOT_FOUND, getMessage("training.job.ids.not.found"));
                }

                training.setJobs(jobs);
            }

            // Save entity
            Training savedTraining = trainingRepository.save(training);
            log.info("Training created successfully with ID: {}", savedTraining.getId());

            return savedTraining;

        } catch (TrainingCreationException e) {
            throw e; // Already logged
        } catch (Exception e) {
            log.error("Unexpected error during training creation: {}", e.getMessage(), e);
            throw new TrainingCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("training.create.failed"));
        }
    }



    @Transactional
    public Training updateTraining(Long id, TrainingRequestDTO dto) {
        if (id == null) {
            log.warn("Attempted to update training with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("training.id.empty"));
        }
        if (dto == null) {
            log.warn("Training update request cannot be null");
            throw new TrainingUpdateException(HttpStatus.BAD_REQUEST, getMessage("training.dto.empty"));
        }

        try {
            log.info("Updating training with ID: {}", id);

            Training existingTraining = getTrainingById(id); // assumes this throws if not found

            // No need to check null if getTrainingById throws on not found, but if not:
            if (existingTraining == null) {
                log.warn("Training not found with ID: {}", id);
                throw new TrainingUpdateException(HttpStatus.NOT_FOUND, getMessage("training.not.found", id));
            }

            // Optional: check if another training with the same name exists to avoid duplicates
            if (!existingTraining.getName().equals(dto.getName())) {
                boolean exists = trainingRepository.existsByNameAndSoftDeletedFalse(dto.getName());
                if (exists) {
                    log.warn("Training already exists with name: {}", dto.getName());
                    throw new TrainingUpdateException(HttpStatus.CONFLICT, getMessage("training.name.already.exists", dto.getName()));
                }
            }

            trainingMapper.updateTrainingFromDto(dto, existingTraining);

            // Update linked jobs if jobIds are provided
            if (dto.getJobIds() != null) {
                Set<Job> jobs = new HashSet<>(jobRepository.findAllById(dto.getJobIds()));

                if (jobs.size() != dto.getJobIds().size()) {
                    log.warn("Some job IDs in the list were not found: {}", dto.getJobIds());
                    throw new TrainingUpdateException(HttpStatus.NOT_FOUND, getMessage("training.job.ids.not.found"));
                }

                existingTraining.setJobs(jobs);
            } else {
                // If jobIds is null, optionally clear all jobs or keep existing
                // existingTraining.getJobs().clear();
            }

            Training savedTraining = trainingRepository.save(existingTraining);
            log.info("Training with ID {} successfully updated", savedTraining.getId());

            return savedTraining;

        } catch (TrainingUpdateException e) {
            throw e; // Already logged
        } catch (Exception e) {
            log.error("Error updating training with ID {}: {}", id, e.getMessage(), e);
            throw new TrainingUpdateException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("training.update.failed"));
        }
    }

}
