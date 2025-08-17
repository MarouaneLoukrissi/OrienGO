package com.example.oriengo.controller;

import com.example.oriengo.mapper.PersonalizedJobMapper;
import com.example.oriengo.model.dto.PersonalizedJobRequestDto;
import com.example.oriengo.model.dto.PersonalizedJobResponseDto;
import com.example.oriengo.model.entity.PersonalizedJob;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.PersonalizedJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personalized-job")
@RequiredArgsConstructor
@Validated
public class PersonalizedJobController {

    private final PersonalizedJobService service;
    private final PersonalizedJobMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PersonalizedJobResponseDto>>> getAll() {
        List<PersonalizedJob> jobs = service.findAll();
        List<PersonalizedJobResponseDto> dtos = jobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        ApiResponse<List<PersonalizedJobResponseDto>> response = ApiResponse.<List<PersonalizedJobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Personalized jobs fetched successfully")
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalizedJobResponseDto>> getById(@PathVariable Long id) {
        PersonalizedJob job = service.findById(id);
        PersonalizedJobResponseDto dto = mapper.toResponseDto(job);

        ApiResponse<PersonalizedJobResponseDto> response = ApiResponse.<PersonalizedJobResponseDto>builder()
                .code("SUCCESS")
                .status(200)
                .message("Personalized job fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PersonalizedJobResponseDto>> create(@Valid @RequestBody PersonalizedJobRequestDto requestDto) {
        PersonalizedJob savedJob = service.create(requestDto);
        PersonalizedJobResponseDto dto = mapper.toResponseDto(savedJob);

        ApiResponse<PersonalizedJobResponseDto> response = ApiResponse.<PersonalizedJobResponseDto>builder()
                .code("SUCCESS")
                .status(201)
                .message("Personalized job created successfully")
                .data(dto)
                .build();

        return ResponseEntity.created(URI.create("/api/personalized-job/" + savedJob.getId())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalizedJobResponseDto>> update(@PathVariable Long id, @Valid @RequestBody PersonalizedJobRequestDto requestDto) {
        PersonalizedJob updatedJob = service.update(id, requestDto);
        PersonalizedJobResponseDto dto = mapper.toResponseDto(updatedJob);

        ApiResponse<PersonalizedJobResponseDto> response = ApiResponse.<PersonalizedJobResponseDto>builder()
                .code("SUCCESS")
                .status(200)
                .message("Personalized job updated successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.deleteById(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("DELETED")
                .status(204)
                .message("Personalized job deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/job-recommendation/{jobRecommendationId}")
    public ResponseEntity<ApiResponse<List<PersonalizedJobResponseDto>>> getByJobRecommendationId(@PathVariable Long jobRecommendationId) {
        List<PersonalizedJob> jobs = service.findByJobRecommendationId(jobRecommendationId);
        List<PersonalizedJobResponseDto> dtos = jobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        ApiResponse<List<PersonalizedJobResponseDto>> response = ApiResponse.<List<PersonalizedJobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Personalized jobs fetched successfully by job recommendation")
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/highlighted")
    public ResponseEntity<ApiResponse<List<PersonalizedJobResponseDto>>> getHighlightedJobs() {
        List<PersonalizedJob> jobs = service.findHighlightedJobs();
        List<PersonalizedJobResponseDto> dtos = jobs.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        ApiResponse<List<PersonalizedJobResponseDto>> response = ApiResponse.<List<PersonalizedJobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Highlighted personalized jobs fetched successfully")
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }
}
