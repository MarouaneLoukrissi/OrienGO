package com.example.oriengo.controller;

import com.example.oriengo.mapper.TestResultMapper;
import com.example.oriengo.model.dto.TestResultCreateDTO;
import com.example.oriengo.model.dto.TestResultMapMediaDTO;
import com.example.oriengo.model.dto.TestResultResponseDTO;
import com.example.oriengo.model.entity.TestResult;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.TestResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/test-results")
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;
    private final TestResultMapper testResultMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TestResultResponseDTO>>> getAll() {
        List<TestResult> results = testResultService.getAll();
        List<TestResultResponseDTO> dtoList = testResultMapper.toDTO(results);

        ApiResponse<List<TestResultResponseDTO>> response = ApiResponse.<List<TestResultResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Test results fetched successfully")
                .data(dtoList)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TestResultResponseDTO>> getById(@PathVariable Long id) {
        TestResult result = testResultService.getById(id)
                .orElseThrow(() -> new RuntimeException("TestResult not found with ID " + id)); // Or your custom exception

        TestResultResponseDTO dto = testResultMapper.toDTO(result);
        ApiResponse<TestResultResponseDTO> response = ApiResponse.<TestResultResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Test result fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<ApiResponse<TestResultResponseDTO>> getByTestId(@PathVariable Long testId) {
        TestResult result = testResultService.getByTestId(testId)
                .orElseThrow(() -> new RuntimeException("TestResult not found for test ID " + testId));

        TestResultResponseDTO dto = testResultMapper.toDTO(result);
        ApiResponse<TestResultResponseDTO> response = ApiResponse.<TestResultResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Test result fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<TestResultResponseDTO>>> getByStudentId(@PathVariable Long studentId) {
        List<TestResult> results = testResultService.getByStudentId(studentId);
        List<TestResultResponseDTO> dtoList = testResultMapper.toDTO(results);

        ApiResponse<List<TestResultResponseDTO>> response = ApiResponse.<List<TestResultResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Test results fetched successfully")
                .data(dtoList)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{testId}/unsaved-test/submit")
    public ResponseEntity<ApiResponse<TestResultResponseDTO>> createUnsavedTestResult(@Valid @RequestBody TestResultCreateDTO dto) {
        TestResult result = testResultService.createUnsavedTestResult(dto);
        TestResultResponseDTO responseDTO = testResultMapper.toDTO(result);
        ApiResponse<TestResultResponseDTO> response = ApiResponse.<TestResultResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Test result created successfully")
                .data(responseDTO)
                .build();

        URI location = URI.create("/api/test-results/" + result.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{testId}/submit")
    public ResponseEntity<ApiResponse<TestResultResponseDTO>> createSavedTestResult(@PathVariable Long testId) {
        TestResult result = testResultService.createSavedTestResult(testId);
        TestResultResponseDTO responseDTO = testResultMapper.toDTO(result);
        ApiResponse<TestResultResponseDTO> response = ApiResponse.<TestResultResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Test result created successfully")
                .data(responseDTO)
                .build();

        URI location = URI.create("/api/test-results/" + result.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/map-to-media")
    public ResponseEntity<ApiResponse<TestResultResponseDTO>> createMapToMedia(@RequestBody TestResultMapMediaDTO testResultMapMedia) {
        TestResult result = testResultService.mapTestResultToMedia(testResultMapMedia.getId(), testResultMapMedia.getMediaId());
        TestResultResponseDTO responseDTO = testResultMapper.toDTO(result);
        ApiResponse<TestResultResponseDTO> response = ApiResponse.<TestResultResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Test result mapped successfully")
                .data(responseDTO)
                .build();

        URI location = URI.create("/api/test-results/" + result.getId());
        return ResponseEntity.created(location).body(response);
    }
}
