package com.example.oriengo.service;

import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationCreationException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationDeleteException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationGetException;
import com.example.oriengo.exception.custom.JobRecommendation.JobRecommendationUpdateException;
import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.model.dto.JobRecommendationRequestDto;
import com.example.oriengo.model.entity.JobRecommendation;
import com.example.oriengo.model.entity.TestResult;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.mapper.JobRecommendationMapper;
import com.example.oriengo.repository.JobRecommendationRepository;
import com.example.oriengo.repository.TestResultRepository;
import com.example.oriengo.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class JobRecommendationService {
    private final JobRecommendationRepository repository;
    private final TestResultRepository testResultRepository;
    private final JobRepository jobRepository;
    private final JobRecommendationMapper mapper;
    private final MessageSource messageSource; // Injected

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<JobRecommendation> findAll() {
        try {
            log.info("Fetching job recommendations");
            List<JobRecommendation> jobRecommendations = repository.findAll();
            log.info("Found {} job recommendation", jobRecommendations.size());
            return jobRecommendations;
        } catch (Exception e) {
            log.error("Failed to fetch job recommendations : {}", e.getMessage(), e);
            throw new JobRecommendationGetException(HttpStatus.NOT_FOUND, getMessage("jobRecommendation.not.found"));
        }
    }

    public JobRecommendation findById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch job recommendation with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("jobRecommendation.id.empty"));
        }

        log.info("Fetching job recommendation with ID: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Job recommendation not found with ID: {}", id);
                    return new JobRecommendationGetException(HttpStatus.NOT_FOUND, getMessage("jobRecommendation.not.found"));
                });
    }

    public JobRecommendation create(JobRecommendationRequestDto requestDto) {
        if (requestDto == null) {
            log.warn("job recommendation request cannot be null");
            throw new JobRecommendationCreationException(HttpStatus.BAD_REQUEST, getMessage("jobRecommendation.dto.empty"));
        }
        try {
            log.info("Starting creation of new job recommendation");
            TestResult testResult = testResultRepository.findById(requestDto.getTestResultId())
                    .orElseThrow(() -> new JobRecommendationCreationException(HttpStatus.NOT_FOUND, getMessage("jobRecommendation.testResult.not.found")));
            Job job = jobRepository.findById(requestDto.getJobId())
                    .orElseThrow(() -> new JobRecommendationCreationException(HttpStatus.NOT_FOUND, getMessage("jobRecommendation.job.not.found")));
            JobRecommendation jobRecommendation = mapper.toEntity(requestDto);
            jobRecommendation.setTestResult(testResult);
            jobRecommendation.setJob(job);
            JobRecommendation savedJobRecommendation = repository.save(jobRecommendation);
            log.info("Job recommendation created successfully with ID: {}", savedJobRecommendation.getId());
            return savedJobRecommendation;
        } catch (Exception e) {
            log.error("Unexpected error during job recommendation creation : {}", e.getMessage(), e);
            throw new JobRecommendationCreationException(HttpStatus.BAD_REQUEST, getMessage("jobRecommendation.create.failed"));
        }
    }

    public JobRecommendation update(Long id, JobRecommendationRequestDto requestDto) {
        if(id == null){
            log.warn("Attempted to fetch job recommendation with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("jobRecommendation.id.empty"));
        }
        else if (requestDto == null) {
            log.warn("job recommendation request cannot be null");
            throw new JobRecommendationUpdateException(HttpStatus.BAD_REQUEST, getMessage("jobRecommendation.dto.empty"));
        }
        try {
            JobRecommendation existingJobRecommendation = findById(id);
            log.info("Updating admin with ID: {}", existingJobRecommendation.getId());
            if (requestDto.getTestResultId() != null) {
                TestResult testResult = testResultRepository.findById(requestDto.getTestResultId())
                        .orElseThrow(() -> new JobRecommendationUpdateException(HttpStatus.NOT_FOUND, getMessage("jobRecommendation.testResult.not.found")));
                existingJobRecommendation.setTestResult(testResult);
            }
            if (requestDto.getJobId() != null) {
                Job job = jobRepository.findById(requestDto.getJobId())
                        .orElseThrow(() -> new JobRecommendationUpdateException(HttpStatus.NOT_FOUND, getMessage("jobRecommendation.job.not.found")));
                existingJobRecommendation.setJob(job);
            }
            mapper.updateEntityFromDto(requestDto, existingJobRecommendation);
            JobRecommendation savedJobRecommendation = repository.save(existingJobRecommendation);
            log.info("Job recommendation with ID {} successfully updated", existingJobRecommendation.getId());
            return savedJobRecommendation;
        } catch (Exception e) {
            log.error("Error updating job recommendation with ID {}: {}", id, e.getMessage(), e);
            throw new JobRecommendationUpdateException(HttpStatus.BAD_REQUEST, getMessage("jobRecommendation.update.failed"));
        }
    }

    public void deleteById(Long id) {
        if (id == null) {
            log.warn("Attempted delete with null job recommendation ID");
            throw new JobRecommendationDeleteException(HttpStatus.BAD_REQUEST, getMessage("jobRecommendation.id.empty"));
        }
        try {
            log.info("Attempting delete for job recommendation with ID: {}", id);
            JobRecommendation jobRecommendation = findById(id);
            repository.deleteById(jobRecommendation.getId());
            log.info("Successfully deleted job recommendation with ID: {}", jobRecommendation.getId());
        } catch (Exception e) {
            log.error("Error during delete of job recommendation with ID {}: {}", id, e.getMessage(), e);
            throw new JobRecommendationDeleteException(HttpStatus.CONFLICT, getMessage("jobRecommendation.delete.failed"));
        }
    }
}