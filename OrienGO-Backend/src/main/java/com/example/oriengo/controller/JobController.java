package com.example.oriengo.controller;

import com.example.oriengo.mapper.JobMapper;
import com.example.oriengo.model.dto.JobRequestDto;
import com.example.oriengo.model.dto.JobResponseDto;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
@Validated
public class JobController {

    private final JobService jobService;
    private final JobMapper jobMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> getAllJobs() {
        List<Job> jobs = jobService.findAll();
        List<JobResponseDto> jobDtos = jobs.stream().map(jobMapper::toResponseDto).collect(Collectors.toList());

        ApiResponse<List<JobResponseDto>> response = ApiResponse.<List<JobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Jobs fetched successfully")
                .data(jobDtos)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponseDto>> getJobById(@PathVariable Long id) {
        Job job = jobService.findById(id);
        JobResponseDto jobDto = jobMapper.toResponseDto(job);

        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .code("SUCCESS")
                .status(200)
                .message("Job fetched successfully")
                .data(jobDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse<JobResponseDto>> createJob(@Valid @RequestBody JobRequestDto requestDto) {
        Job job = jobService.create(requestDto);
        JobResponseDto jobDto = jobMapper.toResponseDto(job);

        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .code("SUCCESS")
                .status(201)
                .message("Job created successfully")
                .data(jobDto)
                .build();
        URI location = URI.create("/api/job/" + job.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<JobResponseDto>> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequestDto requestDto) {
        Job updatedJob = jobService.update(id, requestDto);
        JobResponseDto jobDto = jobMapper.toResponseDto(updatedJob);

        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .code("JOB_UPDATED")
                .status(200)
                .message("Job updated successfully")
                .data(jobDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        jobService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("JOB_DELETED")
                .status(204)
                .message("Job deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> getByCategory(@PathVariable String category) {
        List<Job> jobs = jobService.findByCategory(category);
        List<JobResponseDto> jobDtos = jobs.stream().map(jobMapper::toResponseDto).collect(Collectors.toList());

        ApiResponse<List<JobResponseDto>> response = ApiResponse.<List<JobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Jobs by category fetched successfully")
                .data(jobDtos)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> searchByTitle(@RequestParam String title) {
        List<Job> jobs = jobService.findByTitleContaining(title);
        List<JobResponseDto> jobDtos = jobs.stream().map(jobMapper::toResponseDto).collect(Collectors.toList());

        ApiResponse<List<JobResponseDto>> response = ApiResponse.<List<JobResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Jobs matching title fetched successfully")
                .data(jobDtos)
                .build();
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/active")
//    public ResponseEntity<ApiResponse<List<JobResponseDto>>> getActiveJobs() {
//        List<Job> jobs = jobService.findActiveJobs();
//        List<JobResponseDto> jobDtos = jobs.stream().map(jobMapper::toResponseDto).collect(Collectors.toList());
//
//        ApiResponse<List<JobResponseDto>> response = ApiResponse.<List<JobResponseDto>>builder()
//                .code("SUCCESS")
//                .status(200)
//                .message("Active jobs fetched successfully")
//                .data(jobDtos)
//                .build();
//        return ResponseEntity.ok(response);
//    }
}
