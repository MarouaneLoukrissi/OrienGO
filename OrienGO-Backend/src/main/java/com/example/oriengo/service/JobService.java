package com.example.oriengo.service;

import com.example.oriengo.model.dto.JobRequestDto;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.model.enumeration.JobCategory;
import com.example.oriengo.model.mapper.JobMapper;
import com.example.oriengo.repository.JobRepository;
import com.example.oriengo.exception.ResourceNotFoundException;
import com.example.oriengo.exceptionHandler.exceptions.BusinessException;
import com.example.oriengo.exceptionHandler.util.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobService {
    private final JobRepository repository;
    private final JobMapper mapper;

    public JobService(JobRepository repository, JobMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Job> findAll() {
        return repository.findAll();
    }

    public Optional<Job> findById(Long id) {
        return repository.findById(id);
    }

    public Job create(JobRequestDto requestDto) {
        try {
            // Validations métier simplifiées
            if (requestDto == null) {
                throw new BusinessException("Job request cannot be null");
            }
            
            if (!StringUtils.hasText(requestDto.getTitle())) {
                throw new BusinessException("Job title is required");
            }
            
            if (!StringUtils.hasText(requestDto.getDescription())) {
                throw new BusinessException("Job description is required");
            }
            
            // Validation des scores RIASEC
            validateRiasScore(requestDto.getRiasecRealistic(), "Realistic");
            validateRiasScore(requestDto.getRiasecInvestigative(), "Investigative");
            validateRiasScore(requestDto.getRiasecArtistic(), "Artistic");
            validateRiasScore(requestDto.getRiasecSocial(), "Social");
            validateRiasScore(requestDto.getRiasecEnterprising(), "Enterprising");
            validateRiasScore(requestDto.getRiasecConventional(), "Conventional");
            
            Job entity = mapper.toEntity(requestDto);
            return repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating job: " + e.getMessage(), e);
        }
    }

    public Job update(Long id, JobRequestDto requestDto) {
        try {
            // Validations métier simplifiées
            if (requestDto == null) {
                throw new BusinessException("Job request cannot be null");
            }
            
            if (!StringUtils.hasText(requestDto.getTitle())) {
                throw new BusinessException("Job title is required");
            }
            
            if (!StringUtils.hasText(requestDto.getDescription())) {
                throw new BusinessException("Job description is required");
            }
            
            Job existingEntity = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
            
            mapper.updateEntityFromDto(requestDto, existingEntity);
            return repository.save(existingEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error updating job: " + e.getMessage(), e);
        }
    }

    public void deleteById(Long id) {
        try {
            Job job = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
            
            // Validation métier : empêcher la suppression d'un job actif
            if (job.isActive()) {
                throw new BusinessException("Cannot delete an active job");
            }
            
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting job: " + e.getMessage(), e);
        }
    }

    public List<Job> findByCategory(String category) {
        try {
            if (category == null) {
                throw new BusinessException("Category cannot be null");
            }
            
            if (!StringUtils.hasText(category)) {
                throw new BusinessException("Category cannot be empty");
            }
            
            JobCategory jobCategory = JobCategory.valueOf(category.toUpperCase());
            return repository.findByCategory(jobCategory);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid job category: " + category + ". Valid categories are: " +
                String.join(", ", JobCategory.values().toString()));
        } catch (Exception e) {
            throw new RuntimeException("Error finding jobs by category: " + e.getMessage(), e);
        }
    }

    public List<Job> findByTitleContaining(String title) {
        try {
            if (title == null) {
                throw new BusinessException("Title cannot be null");
            }
            
            if (!StringUtils.hasText(title)) {
                throw new BusinessException("Title cannot be empty");
            }
            
            return repository.findByTitleContainingIgnoreCase(title);
        } catch (Exception e) {
            throw new RuntimeException("Error finding jobs by title: " + e.getMessage(), e);
        }
    }

    public List<Job> findActiveJobs() {
        try {
            return repository.findBySoftDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Error finding active jobs: " + e.getMessage(), e);
        }
    }

    /**
     * Validation d'un score RIASEC
     */
    private void validateRiasScore(Double score, String type) {
        if (score != null) {
            if (score < 0 || score > 100) {
                throw new BusinessException(type + " RIASEC score must be between 0 and 100");
            }
        }
    }
} 