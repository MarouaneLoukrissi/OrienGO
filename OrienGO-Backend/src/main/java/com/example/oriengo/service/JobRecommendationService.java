package com.example.oriengo.service;

import com.example.oriengo.model.dto.JobRecommendationRequestDto;
import com.example.oriengo.model.entity.JobRecommendation;
import com.example.oriengo.model.entity.TestResult;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.model.mapper.JobRecommendationMapper;
import com.example.oriengo.repository.JobRecommendationRepository;
import com.example.oriengo.repository.TestResultRepository;
import com.example.oriengo.repository.JobRepository;
import com.example.oriengo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobRecommendationService {
    private final JobRecommendationRepository repository;
    private final TestResultRepository testResultRepository;
    private final JobRepository jobRepository;
    private final JobRecommendationMapper mapper;

    public JobRecommendationService(JobRecommendationRepository repository, 
                                   TestResultRepository testResultRepository,
                                   JobRepository jobRepository,
                                   JobRecommendationMapper mapper) {
        this.repository = repository;
        this.testResultRepository = testResultRepository;
        this.jobRepository = jobRepository;
        this.mapper = mapper;
    }

    public List<JobRecommendation> findAll() {
        return repository.findAll();
    }

    public Optional<JobRecommendation> findById(Long id) {
        return repository.findById(id);
    }

    public JobRecommendation create(JobRecommendationRequestDto requestDto) {
        TestResult testResult = testResultRepository.findById(requestDto.getTestResultId())
                .orElseThrow(() -> new ResourceNotFoundException("TestResult", "id", requestDto.getTestResultId()));
        
        Job job = jobRepository.findById(requestDto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", requestDto.getJobId()));
        
        JobRecommendation entity = mapper.toEntity(requestDto);
        entity.setTestResult(testResult);
        entity.setJob(job);
        
        return repository.save(entity);
    }

    public JobRecommendation update(Long id, JobRecommendationRequestDto requestDto) {
        JobRecommendation existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobRecommendation", "id", id));
        
        if (requestDto.getTestResultId() != null) {
            TestResult testResult = testResultRepository.findById(requestDto.getTestResultId())
                    .orElseThrow(() -> new ResourceNotFoundException("TestResult", "id", requestDto.getTestResultId()));
            existingEntity.setTestResult(testResult);
        }
        
        if (requestDto.getJobId() != null) {
            Job job = jobRepository.findById(requestDto.getJobId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job", "id", requestDto.getJobId()));
            existingEntity.setJob(job);
        }
        
        mapper.updateEntityFromDto(requestDto, existingEntity);
        return repository.save(existingEntity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}