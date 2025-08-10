package com.example.oriengo.service;

import com.example.oriengo.exception.custom.Jobs.JobCreationException;
import com.example.oriengo.exception.custom.Jobs.JobDeleteException;
import com.example.oriengo.exception.custom.Jobs.JobGetException;
import com.example.oriengo.exception.custom.Jobs.JobUpdateException;
import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.model.dto.JobRequestDto;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.model.enumeration.JobCategory;
import com.example.oriengo.mapper.JobMapper;
import com.example.oriengo.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private final JobRepository repository;
    private final JobMapper jobMapper;
    private final MessageSource messageSource; // Injected

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<Job> findAll() {
        try {
            log.info("Fetching jobs");
            List<Job> jobs = repository.findAll();
            log.info("Found {} jobs", jobs.size());
            return jobs;
        } catch (Exception e) {
            log.error("Failed to fetch jobs : {}", e.getMessage(), e);
            throw new JobGetException(HttpStatus.NOT_FOUND, getMessage("job.not.found"));
        }
    }

    public Job findById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch job with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("job.id.empty"));
        }

        log.info("Fetching job with ID: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Job not found with ID: {}", id);
                    return new JobGetException(HttpStatus.NOT_FOUND, getMessage("job.not.found"));
                });
    }

    public Job create(JobRequestDto requestDto) {
        if (requestDto == null) {
            log.warn("job request cannot be null");
            throw new JobCreationException(HttpStatus.BAD_REQUEST, getMessage("job.dto.empty"));
        }
        try {
            log.info("Starting creation of new job");
            Job job = jobMapper.toEntity(requestDto);
            Job savedJob = repository.save(job);
            log.info("Job created successfully with ID: {}", savedJob.getId());
            return savedJob;
        } catch (Exception e) {
            log.error("Unexpected error during job creation : {}", e.getMessage(), e);
            throw new RuntimeException("Error creating job: " + e.getMessage(), e);
        }
    }

    public Job update(Long id, JobRequestDto requestDto) {
        if (id == null) {
            log.warn("Attempted to update job with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("job.id.empty"));
        } else if (requestDto == null) {
            log.warn("job request cannot be null");
            throw new JobUpdateException(HttpStatus.BAD_REQUEST, getMessage("job.dto.empty"));
        }
        try {
            Job existingEntity = findById(id);
            jobMapper.updateEntityFromDto(requestDto, existingEntity);
            Job savedJob = repository.save(existingEntity);
            log.info("Job updated successfully with ID: {}", savedJob.getId());
            return savedJob;
        } catch (Exception e) {
            throw new RuntimeException("Error updating job: " + e.getMessage(), e);
        }
    }

    public void deleteById(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null job ID");
            throw new JobDeleteException(HttpStatus.BAD_REQUEST, getMessage("job.id.empty"));
        }
        try {
            log.info("Attempting delete for job with ID: {}", id);
            Job job = findById(id);
            repository.deleteById(job.getId());
            log.info("Successfully deleted job with ID: {}", job.getId());
        } catch (Exception e) {
            log.error("Error during delete of job with ID {}: {}", id, e.getMessage(), e);
            throw new JobDeleteException(HttpStatus.CONFLICT, getMessage("job.delete.failed"));
        }
    }

    public List<Job> findByCategory(String category) {
        if (category == null) {
            log.warn("Attempted to fetch job with null category");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("job.category.null"));
        } else if(category.isEmpty()){
            log.warn("Attempted to fetch job with empty category");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("job.category.empty"));
        }
        try {
            JobCategory jobCategory = JobCategory.valueOf(category.toUpperCase());
            return repository.findByCategory(jobCategory);
        } catch (IllegalArgumentException e) {
            String message = getMessage("job.category.invalid", category, String.join(", ", JobCategory.values().toString()));
            throw new JobGetException(HttpStatus.BAD_REQUEST, message);
        } catch (Exception e) {
            log.error("Failed to fetch job : {}", e.getMessage(), e);
            throw new JobGetException(HttpStatus.NOT_FOUND, getMessage("job.not.found"));
        }
    }

    public List<Job> findByTitleContaining(String title) {
        if (title == null) {
            log.warn("Attempted to fetch job with null title");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("job.title.null"));
        } else if(title.isEmpty()){
            log.warn("Attempted to fetch job with empty title");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("job.title.empty"));
        }
        try {
            log.info("Fetching jobs with title = {}", title);
            List<Job> jobs = repository.findByTitleContainingIgnoreCase(title);
            log.info("Found {} jobs", jobs.size());
            return jobs;
        } catch (Exception e) {
            log.error("Failed to fetch jobs with title = {}: {}", title, e.getMessage(), e);
            throw new JobGetException(HttpStatus.NOT_FOUND, getMessage("job.not.found"));
        }
    }

//    public List<Job> findActiveJobs() {
//        try {
//            return repository.findBySoftDeletedFalse();
//        } catch (Exception e) {
//            throw new RuntimeException("Error finding active jobs: " + e.getMessage(), e);
//        }
//    }
} 