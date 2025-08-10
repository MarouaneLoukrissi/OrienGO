package com.example.oriengo.controller;

import com.example.oriengo.mapper.StudentPersonalizedJobLinkMapper;
import com.example.oriengo.model.dto.StudentPersonalizedJobLinkRequestDto;
import com.example.oriengo.model.dto.StudentPersonalizedJobLinkResponseDto;
import com.example.oriengo.model.entity.StudentPersonalizedJobLink;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.StudentPersonalizedJobLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student-personalized-job-links")
@RequiredArgsConstructor
@Validated
public class StudentPersonalizedJobLinkController {

    private final StudentPersonalizedJobLinkService service;
    private final StudentPersonalizedJobLinkMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentPersonalizedJobLinkResponseDto>>> getAll() {
        List<StudentPersonalizedJobLink> links = service.findAll();
        List<StudentPersonalizedJobLinkResponseDto> dtoList = links.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        ApiResponse<List<StudentPersonalizedJobLinkResponseDto>> response = ApiResponse.<List<StudentPersonalizedJobLinkResponseDto>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student personalized job links fetched successfully")
                .data(dtoList)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentPersonalizedJobLinkResponseDto>> getById(@PathVariable Long id) {
        StudentPersonalizedJobLink link = service.findById(id);
        StudentPersonalizedJobLinkResponseDto dto = mapper.toDto(link);

        ApiResponse<StudentPersonalizedJobLinkResponseDto> response = ApiResponse.<StudentPersonalizedJobLinkResponseDto>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student personalized job link fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentPersonalizedJobLinkResponseDto>> create(@Valid @RequestBody StudentPersonalizedJobLinkRequestDto requestDto) {
        StudentPersonalizedJobLink savedLink = service.save(requestDto);
        StudentPersonalizedJobLinkResponseDto dto = mapper.toDto(savedLink);

        ApiResponse<StudentPersonalizedJobLinkResponseDto> response = ApiResponse.<StudentPersonalizedJobLinkResponseDto>builder()
                .code("SUCCESS")
                .status(201)
                .message("Student personalized job link created successfully")
                .data(dto)
                .build();

        URI location = URI.create("/api/student-personalized-job-links/" + savedLink.getId());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.deleteById(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("SUCCESS")
                .status(204)
                .message("Student personalized job link deleted successfully")
                .build();

        return ResponseEntity.noContent().build();
    }
}
