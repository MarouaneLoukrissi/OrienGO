package com.example.oriengo.controller;

import com.example.oriengo.mapper.PersonalizedJobMapper;
import com.example.oriengo.model.dto.PersonalizedJobRequestDto;
import com.example.oriengo.model.dto.PersonalizedJobResponseDto;
import com.example.oriengo.model.dto.StudentPersonalizedJobLinkRequestDto;
import com.example.oriengo.model.entity.PersonalizedJob;
import com.example.oriengo.model.enumeration.LinkType;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.PersonalizedJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/personalized-job")
@RequiredArgsConstructor
@Validated
public class PersonalizedJobController {

    private final PersonalizedJobService service;
    private final PersonalizedJobMapper personalizedJobMapper;

    @GetMapping("/by-link-type")
    public ResponseEntity<ApiResponse<List<PersonalizedJobResponseDto>>> getByLinkType(
            @RequestParam LinkType type,
            @RequestParam(required = false) Long studentId) {

        List<PersonalizedJobResponseDto> jobs = service.findByLinkTypeAndStudent(type, studentId);

        ApiResponse<List<PersonalizedJobResponseDto>> response = ApiResponse.<List<PersonalizedJobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Personalized jobs fetched successfully by link type")
                .data(jobs)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PersonalizedJobResponseDto>>> getAll() {
        List<PersonalizedJob> jobs = service.findAll();
        List<PersonalizedJobResponseDto> dtos = jobs.stream()
                .map(personalizedJobMapper::toResponseDto)
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
        PersonalizedJobResponseDto dto = personalizedJobMapper.toResponseDto(job);

        ApiResponse<PersonalizedJobResponseDto> response = ApiResponse.<PersonalizedJobResponseDto>builder()
                .code("SUCCESS")
                .status(200)
                .message("Personalized job fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/by-job-recommendations/{studentId}")
    public ResponseEntity<ApiResponse<List<PersonalizedJobResponseDto>>> getByJobRecommendationIds(
            @PathVariable Long studentId,
            @RequestBody List<Long> jobRecommendationIds) {

        List<PersonalizedJobResponseDto> jobs = service.findByJobRecommendationIds(jobRecommendationIds, studentId);
//        List<PersonalizedJobResponseDto> dtos = jobs.stream()
//                .map(personalizedJobMapper::toResponseDto)
//                .collect(Collectors.toList());

        ApiResponse<List<PersonalizedJobResponseDto>> response = ApiResponse.<List<PersonalizedJobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Personalized jobs fetched successfully by job recommendation IDs")
                .data(jobs)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/by-job-recommendations/coach")
    public ResponseEntity<ApiResponse<List<PersonalizedJobResponseDto>>> getByJobRecommendationIdsForCoach(
            @RequestBody List<Long> jobRecommendationIds) {

        // Pass null as studentId → no student-specific links
        List<PersonalizedJobResponseDto> jobs = service.findByJobRecommendationIds(jobRecommendationIds, null);

        ApiResponse<List<PersonalizedJobResponseDto>> response = ApiResponse.<List<PersonalizedJobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Personalized jobs fetched successfully by job recommendation IDs for coach")
                .data(jobs)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<PersonalizedJobResponseDto>>> getByStudentId(@PathVariable Long studentId) {
        List<PersonalizedJobResponseDto> jobs = service.findByStudentId(studentId);

        ApiResponse<List<PersonalizedJobResponseDto>> response = ApiResponse.<List<PersonalizedJobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Personalized jobs fetched successfully for student")
                .data(jobs)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PersonalizedJobResponseDto>> create(@Valid @RequestBody PersonalizedJobRequestDto requestDto) {
        PersonalizedJob savedJob = service.create(requestDto);
        PersonalizedJobResponseDto dto = personalizedJobMapper.toResponseDto(savedJob);

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
        PersonalizedJobResponseDto dto = personalizedJobMapper.toResponseDto(updatedJob);

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

    @PostMapping("/link")
    public ResponseEntity<ApiResponse<String>> linkJobToStudent(@Valid @RequestBody StudentPersonalizedJobLinkRequestDto request) {
        service.linkJobToStudent(request.getStudentId(), request.getPersonalizedJobId(), request.getType());

        ApiResponse<String> response = ApiResponse.<String>builder()
                .code("SUCCESS")
                .status(200)
                .message("Job linked to student as " + request.getType())
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/unlink")
    public ResponseEntity<ApiResponse<String>> unlinkJobFromStudent(@Valid @RequestBody StudentPersonalizedJobLinkRequestDto request) {
        service.unlinkJobFromStudent(request.getStudentId(), request.getPersonalizedJobId(), request.getType());

        ApiResponse<String> response = ApiResponse.<String>builder()
                .code("SUCCESS")
                .status(200)
                .message("Job unlinked from student for type " + request.getType())
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/job-recommendation/{jobRecommendationId}")
    public ResponseEntity<ApiResponse<List<PersonalizedJobResponseDto>>> getByJobRecommendationId(@PathVariable Long jobRecommendationId) {
        List<PersonalizedJob> jobs = service.findByJobRecommendationId(jobRecommendationId);
        List<PersonalizedJobResponseDto> dtos = jobs.stream()
                .map(personalizedJobMapper::toResponseDto)
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
                .map(personalizedJobMapper::toResponseDto)
                .collect(Collectors.toList());

        ApiResponse<List<PersonalizedJobResponseDto>> response = ApiResponse.<List<PersonalizedJobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Highlighted personalized jobs fetched successfully")
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/scrape")
    public ResponseEntity<ApiResponse<Map<Long, List<PersonalizedJobResponseDto>>>> scrapeAndCreate(
            @RequestParam List<Long> jobRecommendationIds,
            @RequestParam Long studentId) {
        Map<Long, List<PersonalizedJobResponseDto>> data = service.scrapeAndCreatePersonalizedJobs(jobRecommendationIds, studentId);
        ApiResponse<Map<Long, List<PersonalizedJobResponseDto>>> response = ApiResponse.<Map<Long, List<PersonalizedJobResponseDto>>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Scraping executed and personalized jobs created")
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }
}
