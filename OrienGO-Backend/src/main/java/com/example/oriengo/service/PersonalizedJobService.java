package com.example.oriengo.service;

import com.example.oriengo.model.dto.PersonalizedJobRequestDto;
import com.example.oriengo.model.entity.PersonalizedJob;
import com.example.oriengo.model.entity.JobRecommendation;
import com.example.oriengo.mapper.PersonalizedJobMapper;
import com.example.oriengo.repository.PersonalizedJobRepository;
import com.example.oriengo.repository.JobRecommendationRepository;
import com.example.oriengo.exception.custom.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonalizedJobService {
    private final PersonalizedJobRepository repository;
    private final JobRecommendationRepository jobRecommendationRepository;
    private final PersonalizedJobMapper mapper;

    public PersonalizedJobService(PersonalizedJobRepository repository,
                                 JobRecommendationRepository jobRecommendationRepository,
                                 PersonalizedJobMapper mapper) {
        this.repository = repository;
        this.jobRecommendationRepository = jobRecommendationRepository;
        this.mapper = mapper;
    }

    public List<PersonalizedJob> findAll() {
        return repository.findAll();
    }

    public Optional<PersonalizedJob> findById(Long id) {
        return repository.findById(id);
    }

    public PersonalizedJob create(PersonalizedJobRequestDto requestDto) {
        JobRecommendation jobRecommendation = null;
        if (requestDto.getJobRecommendationId() != null) {
            jobRecommendation = jobRecommendationRepository.findById(requestDto.getJobRecommendationId())
                    .orElseThrow(() -> new ResourceNotFoundException("JobRecommendation", "id", requestDto.getJobRecommendationId()));
        }
        
        PersonalizedJob entity = mapper.toEntity(requestDto);
        if (jobRecommendation != null) {
            entity.setJobRecommendation(jobRecommendation);
        }
        
        return repository.save(entity);
    }

    public PersonalizedJob update(Long id, PersonalizedJobRequestDto requestDto) {
        PersonalizedJob existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PersonalizedJob", "id", id));
        
        if (requestDto.getJobRecommendationId() != null) {
            JobRecommendation jobRecommendation = jobRecommendationRepository.findById(requestDto.getJobRecommendationId())
                    .orElseThrow(() -> new ResourceNotFoundException("JobRecommendation", "id", requestDto.getJobRecommendationId()));
            existingEntity.setJobRecommendation(jobRecommendation);
        }
        
        mapper.updateEntityFromDto(requestDto, existingEntity);
        return repository.save(existingEntity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<PersonalizedJob> findByJobRecommendationId(Long jobRecommendationId) {
        return repository.findByJobRecommendationId(jobRecommendationId);
    }

    public List<PersonalizedJob> findHighlightedJobs() {
        return repository.findByHighlightedTrue();
    }
} 