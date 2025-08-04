package com.example.oriengo.controller;

import com.example.oriengo.model.dto.StudentJobLinkRequestDto;
import com.example.oriengo.model.dto.StudentJobLinkResponseDto;
import com.example.oriengo.model.entity.StudentJobLink;
import com.example.oriengo.model.mapper.StudentJobLinkMapper;
import com.example.oriengo.service.StudentJobLinkService;
import com.example.oriengo.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student-job-links")
public class StudentJobLinkController {
    private final StudentJobLinkService service;
    private final StudentJobLinkMapper mapper;

    public StudentJobLinkController(StudentJobLinkService service, StudentJobLinkMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<StudentJobLinkResponseDto> getAll() {
        List<StudentJobLink> studentJobLinks = service.findAll();
        return studentJobLinks.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentJobLinkResponseDto> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(mapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public StudentJobLinkResponseDto create(@Valid @RequestBody StudentJobLinkRequestDto requestDto) {
        StudentJobLink savedStudentJobLink = service.create(requestDto);
        return mapper.toResponseDto(savedStudentJobLink);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentJobLinkResponseDto> update(@PathVariable Long id, @Valid @RequestBody StudentJobLinkRequestDto requestDto) {
        try {
            StudentJobLink updatedStudentJobLink = service.update(id, requestDto);
            return ResponseEntity.ok(mapper.toResponseDto(updatedStudentJobLink));
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

    @GetMapping("/student/{studentId}")
    public List<StudentJobLinkResponseDto> getByStudentId(@PathVariable Long studentId) {
        List<StudentJobLink> studentJobLinks = service.findByStudentId(studentId);
        return studentJobLinks.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/job/{jobId}")
    public List<StudentJobLinkResponseDto> getByJobId(@PathVariable Long jobId) {
        List<StudentJobLink> studentJobLinks = service.findByJobId(jobId);
        return studentJobLinks.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/type/{type}")
    public List<StudentJobLinkResponseDto> getByType(@PathVariable String type) {
        List<StudentJobLink> studentJobLinks = service.findByType(type);
        return studentJobLinks.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
} 