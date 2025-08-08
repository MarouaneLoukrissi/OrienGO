package com.example.oriengo.controller;

import com.example.oriengo.mapper.StudentJobLinkMapper;
import com.example.oriengo.model.dto.StudentJobLinkRequestDto;
import com.example.oriengo.model.dto.StudentJobLinkResponseDto;
import com.example.oriengo.model.entity.StudentJobLink;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.StudentJobLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student-job-links")
@RequiredArgsConstructor
@Validated
public class StudentJobLinkController {

    private final StudentJobLinkService service;
    private final StudentJobLinkMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentJobLinkResponseDto>>> getAll() {
        List<StudentJobLink> links = service.findAll();
        List<StudentJobLinkResponseDto> dtos = links.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        ApiResponse<List<StudentJobLinkResponseDto>> response = ApiResponse.<List<StudentJobLinkResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student job links fetched successfully")
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentJobLinkResponseDto>> getById(@PathVariable Long id) {
        StudentJobLink link = service.findById(id);
        StudentJobLinkResponseDto dto = mapper.toResponseDto(link);

        ApiResponse<StudentJobLinkResponseDto> response = ApiResponse.<StudentJobLinkResponseDto>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student job link fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentJobLinkResponseDto>> create(@Valid @RequestBody StudentJobLinkRequestDto requestDto) {
        StudentJobLink created = service.create(requestDto);
        StudentJobLinkResponseDto dto = mapper.toResponseDto(created);

        ApiResponse<StudentJobLinkResponseDto> response = ApiResponse.<StudentJobLinkResponseDto>builder()
                .code("SUCCESS")
                .status(201)
                .message("Student job link created successfully")
                .data(dto)
                .build();

        return ResponseEntity.created(URI.create("/api/student-job-links/" + created.getId())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentJobLinkResponseDto>> update(@PathVariable Long id, @Valid @RequestBody StudentJobLinkRequestDto requestDto) {
        StudentJobLink updated = service.update(id, requestDto);
        StudentJobLinkResponseDto dto = mapper.toResponseDto(updated);

        ApiResponse<StudentJobLinkResponseDto> response = ApiResponse.<StudentJobLinkResponseDto>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student job link updated successfully")
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
                .message("Student job link deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<StudentJobLinkResponseDto>>> getByStudentId(@PathVariable Long studentId) {
        List<StudentJobLink> links = service.findByStudentId(studentId);
        List<StudentJobLinkResponseDto> dtos = links.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        ApiResponse<List<StudentJobLinkResponseDto>> response = ApiResponse.<List<StudentJobLinkResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student job links fetched by student ID")
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<List<StudentJobLinkResponseDto>>> getByJobId(@PathVariable Long jobId) {
        List<StudentJobLink> links = service.findByJobId(jobId);
        List<StudentJobLinkResponseDto> dtos = links.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        ApiResponse<List<StudentJobLinkResponseDto>> response = ApiResponse.<List<StudentJobLinkResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student job links fetched by job ID")
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<StudentJobLinkResponseDto>>> getByType(@PathVariable String type) {
        List<StudentJobLink> links = service.findByType(type);
        List<StudentJobLinkResponseDto> dtos = links.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        ApiResponse<List<StudentJobLinkResponseDto>> response = ApiResponse.<List<StudentJobLinkResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student job links fetched by type")
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }
}
