package com.example.oriengo.controller;

import com.example.oriengo.model.dto.JobRequestDto;
import com.example.oriengo.model.dto.JobResponseDto;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.model.mapper.JobMapper;
import com.example.oriengo.service.JobService;
import com.example.oriengo.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService service;
    private final JobMapper mapper;

    public JobController(JobService service, JobMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<JobResponseDto> getAll() {
        List<Job> jobs = service.findAll();
        return jobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDto> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(mapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public JobResponseDto create(@Valid @RequestBody JobRequestDto requestDto) {
        Job savedJob = service.create(requestDto);
        return mapper.toResponseDto(savedJob);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDto> update(@PathVariable Long id, @Valid @RequestBody JobRequestDto requestDto) {
        try {
            Job updatedJob = service.update(id, requestDto);
            return ResponseEntity.ok(mapper.toResponseDto(updatedJob));
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

    @GetMapping("/category/{category}")
    public List<JobResponseDto> getByCategory(@PathVariable String category) {
        List<Job> jobs = service.findByCategory(category);
        return jobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<JobResponseDto> searchByTitle(@RequestParam String title) {
        List<Job> jobs = service.findByTitleContaining(title);
        return jobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/active")
    public List<JobResponseDto> getActiveJobs() {
        List<Job> jobs = service.findActiveJobs();
        return jobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
} 