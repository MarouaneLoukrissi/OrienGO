package com.example.oriengo.service;

import com.example.oriengo.model.dto.JobRequestDto;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.model.enumeration.JobCategory;
import com.example.oriengo.model.mapper.JobMapper;
import com.example.oriengo.repository.JobRepository;
import com.example.oriengo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Job entity = mapper.toEntity(requestDto);
        return repository.save(entity);
    }

    public Job update(Long id, JobRequestDto requestDto) {
        Job existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
        
        mapper.updateEntityFromDto(requestDto, existingEntity);
        return repository.save(existingEntity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Job> findByCategory(String category) {
        try {
            JobCategory jobCategory = JobCategory.valueOf(category.toUpperCase());
            return repository.findByCategory(jobCategory);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid job category: " + category + ". Valid categories are: " + 
                String.join(", ", JobCategory.values().toString()));
        }
    }

    public List<Job> findByTitleContaining(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    public List<Job> findActiveJobs() {
        return repository.findBySoftDeletedFalse();
    }
} 