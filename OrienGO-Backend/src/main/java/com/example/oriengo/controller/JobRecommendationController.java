package com.example.oriengo.controller;

import com.example.oriengo.mapper.JobRecommendationMapper;
import com.example.oriengo.model.dto.JobRecommendationRequestDto;
import com.example.oriengo.model.dto.JobRecommendationResponseDto;
import com.example.oriengo.model.entity.JobRecommendation;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.JobRecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/job-recommendation")
@RequiredArgsConstructor
@Validated
public class JobRecommendationController {

    private final JobRecommendationService service;
    private final JobRecommendationMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobRecommendationResponseDto>>> getAll() {
        List<JobRecommendation> recommendations = service.findAll();
        List<JobRecommendationResponseDto> dtos = recommendations.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        ApiResponse<List<JobRecommendationResponseDto>> response = ApiResponse.<List<JobRecommendationResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Job recommendations fetched successfully")
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobRecommendationResponseDto>> getById(@PathVariable Long id) {
        JobRecommendation entity = service.findById(id);
        JobRecommendationResponseDto dto = mapper.toResponseDto(entity);

        ApiResponse<JobRecommendationResponseDto> response = ApiResponse.<JobRecommendationResponseDto>builder()
                .code("SUCCESS")
                .status(200)
                .message("Job recommendation fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobRecommendationResponseDto>> create(@Valid @RequestBody JobRecommendationRequestDto requestDto) {
        JobRecommendation saved = service.create(requestDto);
        JobRecommendationResponseDto dto = mapper.toResponseDto(saved);

        ApiResponse<JobRecommendationResponseDto> response = ApiResponse.<JobRecommendationResponseDto>builder()
                .code("SUCCESS")
                .status(201)
                .message("Job recommendation created successfully")
                .data(dto)
                .build();

        URI location = URI.create("/api/job-recommendation/" + saved.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobRecommendationResponseDto>> update(@PathVariable Long id, @Valid @RequestBody JobRecommendationRequestDto requestDto) {
        JobRecommendation updated = service.update(id, requestDto);
        JobRecommendationResponseDto dto = mapper.toResponseDto(updated);

        ApiResponse<JobRecommendationResponseDto> response = ApiResponse.<JobRecommendationResponseDto>builder()
                .code("JOB_RECOMMENDATION_UPDATED")
                .status(200)
                .message("Job recommendation updated successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.deleteById(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("JOB_RECOMMENDATION_DELETED")
                .status(204)
                .message("Job recommendation deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
