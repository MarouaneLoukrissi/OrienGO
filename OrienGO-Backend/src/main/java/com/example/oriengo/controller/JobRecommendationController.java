package com.example.oriengo.controller;

import com.example.oriengo.model.dto.JobRecommendationRequestDto;
import com.example.oriengo.model.dto.JobRecommendationResponseDto;
import com.example.oriengo.model.entity.JobRecommendation;
import com.example.oriengo.model.mapper.JobRecommendationMapper;
import com.example.oriengo.service.JobRecommendationService;
import com.example.oriengo.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/job-recommendations")


public class JobRecommendationController {
    private final JobRecommendationService service;
    private final JobRecommendationMapper mapper;

    public JobRecommendationController(JobRecommendationService service, JobRecommendationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<JobRecommendationResponseDto> getAll() {
        List<JobRecommendation> jobRecommendations = service.findAll();
        return jobRecommendations.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobRecommendationResponseDto> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(mapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public JobRecommendationResponseDto create(@Valid @RequestBody JobRecommendationRequestDto requestDto) {
        JobRecommendation savedJobRecommendation = service.create(requestDto);
        return mapper.toResponseDto(savedJobRecommendation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobRecommendationResponseDto> update(@PathVariable Long id, @Valid @RequestBody JobRecommendationRequestDto requestDto) {
        try {
            JobRecommendation updatedJobRecommendation = service.update(id, requestDto);
            return ResponseEntity.ok(mapper.toResponseDto(updatedJobRecommendation));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}