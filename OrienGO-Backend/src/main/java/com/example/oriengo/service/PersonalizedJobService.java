package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobCreationException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobDeleteException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobGetException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobUpdateException;
import com.example.oriengo.model.dto.PersonalizedJobRequestDto;
import com.example.oriengo.model.entity.PersonalizedJob;
import com.example.oriengo.model.entity.JobRecommendation;
import com.example.oriengo.mapper.PersonalizedJobMapper;
import com.example.oriengo.repository.PersonalizedJobRepository;
import com.example.oriengo.repository.JobRecommendationRepository;
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
public class PersonalizedJobService {
    private final PersonalizedJobRepository repository;
    private final JobRecommendationRepository jobRecommendationRepository;
    private final PersonalizedJobMapper mapper;
    private final MessageSource messageSource; // Injected

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<PersonalizedJob> findAll() {
        try {
            log.info("Fetching personalized job");
            List<PersonalizedJob> personalizedJobs = repository.findAll();
            log.info("Found {} personalized job", personalizedJobs.size());
            return personalizedJobs;
        } catch (Exception e) {
            log.error("Failed to fetch personalized job : {}", e.getMessage(), e);
            throw new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
        }
    }

    public PersonalizedJob findById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch personalized job with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.id.empty"));
        }

        log.info("Fetching personalized job with ID: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Personalized job not found with ID: {}", id);
                    return new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
                });
    }

    public PersonalizedJob create(PersonalizedJobRequestDto requestDto) {
        if (requestDto == null) {
            log.warn("personalized job request cannot be null");
            throw new PersonalizedJobCreationException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.dto.empty"));
        }
        try {
            JobRecommendation jobRecommendation = null;
            if (requestDto.getJobRecommendationId() != null) {
                jobRecommendation = jobRecommendationRepository.findById(requestDto.getJobRecommendationId())
                        .orElseThrow(() -> new PersonalizedJobCreationException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.jobRecommendation.empty")));
            }
            PersonalizedJob personalizedJob = mapper.toEntity(requestDto);
            if (jobRecommendation != null) {
                personalizedJob.setJobRecommendation(jobRecommendation);
            }

            return repository.save(personalizedJob);
        } catch (Exception e) {
            log.error("Unexpected error during personalized job creation : {}", e.getMessage(), e);
            throw new PersonalizedJobCreationException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.create.failed"));
        }
    }

    public PersonalizedJob update(Long id, PersonalizedJobRequestDto requestDto) {
        if (id == null) {
            log.warn("Attempted to update personalized job with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.id.empty"));
        } else if (requestDto == null) {
            log.warn("personalized job request cannot be null");
            throw new PersonalizedJobUpdateException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.dto.empty"));
        }
        try{
            PersonalizedJob existingPersonalizedJob = findById(id);

            if (requestDto.getJobRecommendationId() != null) {
                JobRecommendation jobRecommendation = jobRecommendationRepository.findById(requestDto.getJobRecommendationId())
                        .orElseThrow(() -> new PersonalizedJobUpdateException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.jobRecommendation.not.found")));
                existingPersonalizedJob.setJobRecommendation(jobRecommendation);
            }

            mapper.updateEntityFromDto(requestDto, existingPersonalizedJob);
            PersonalizedJob savedPersonalizedJob = repository.save(existingPersonalizedJob);
            log.info("Personalized job with ID {} successfully updated", savedPersonalizedJob.getId());
            return savedPersonalizedJob;
        } catch (Exception e) {
            log.error("Error updating personalized job with ID {}: {}", id, e.getMessage(), e);
            throw new PersonalizedJobUpdateException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.update.failed"));
        }
    }

    public void deleteById(Long id) {
        if (id == null) {
            log.warn("Attempted delete with null personalized job ID");
            throw new PersonalizedJobDeleteException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.id.empty"));
        }
        try {
            log.info("Attempting delete for personalized job with ID: {}", id);
            PersonalizedJob personalizedJob = findById(id);
            repository.deleteById(personalizedJob.getId());
            log.info("Successfully deleted personalized job with ID: {}", personalizedJob.getId());
        } catch (Exception e) {
            log.error("Error during delete of personalized job with ID {}: {}", id, e.getMessage(), e);
            throw new PersonalizedJobDeleteException(HttpStatus.CONFLICT, getMessage("personalized.delete.failed"));
        }
    }

    public List<PersonalizedJob> findByJobRecommendationId(Long jobRecommendationId) {
        if (jobRecommendationId == null) {
            log.warn("Attempted to fetch personalized job with null job recommendation ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.id.empty"));
        }

        log.info("Fetching personalized job with job recommendation ID: {}", jobRecommendationId);

        return repository.findByJobRecommendationId(jobRecommendationId)
                .orElseThrow(() -> {
                    log.error("Personalized job not found with job recommendation ID: {}", jobRecommendationId);
                    return new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
                });
    }

    public List<PersonalizedJob> findHighlightedJobs() {
        try {
            log.info("Fetching highlighted job");
            List<PersonalizedJob> personalizedJobs = repository.findByHighlightedTrue();
            log.info("Found {} highlighted job", personalizedJobs.size());
            return personalizedJobs;
        } catch (Exception e) {
            log.error("Failed to fetch highlighted jobs : {}", e.getMessage(), e);
            throw new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
        }
    }
} 