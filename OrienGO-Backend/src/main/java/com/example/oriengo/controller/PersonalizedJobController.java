package com.example.oriengo.controller;

import com.example.oriengo.model.dto.PersonalizedJobRequestDto;
import com.example.oriengo.model.dto.PersonalizedJobResponseDto;
import com.example.oriengo.model.entity.PersonalizedJob;
import com.example.oriengo.model.mapper.PersonalizedJobMapper;
import com.example.oriengo.service.PersonalizedJobService;
import com.example.oriengo.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personalized-jobs")
public class PersonalizedJobController {
    private final PersonalizedJobService service;
    private final PersonalizedJobMapper mapper;

    public PersonalizedJobController(PersonalizedJobService service, PersonalizedJobMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<PersonalizedJobResponseDto> getAll() {
        List<PersonalizedJob> personalizedJobs = service.findAll();
        return personalizedJobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalizedJobResponseDto> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(mapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PersonalizedJobResponseDto create(@Valid @RequestBody PersonalizedJobRequestDto requestDto) {
        PersonalizedJob savedPersonalizedJob = service.create(requestDto);
        return mapper.toResponseDto(savedPersonalizedJob);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalizedJobResponseDto> update(@PathVariable Long id, @Valid @RequestBody PersonalizedJobRequestDto requestDto) {
        try {
            PersonalizedJob updatedPersonalizedJob = service.update(id, requestDto);
            return ResponseEntity.ok(mapper.toResponseDto(updatedPersonalizedJob));
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

    @GetMapping("/job-recommendation/{jobRecommendationId}")
    public List<PersonalizedJobResponseDto> getByJobRecommendationId(@PathVariable Long jobRecommendationId) {
        List<PersonalizedJob> personalizedJobs = service.findByJobRecommendationId(jobRecommendationId);
        return personalizedJobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/highlighted")
    public List<PersonalizedJobResponseDto> getHighlightedJobs() {
        List<PersonalizedJob> highlightedJobs = service.findHighlightedJobs();
        return highlightedJobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
}