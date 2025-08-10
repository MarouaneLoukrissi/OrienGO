package com.example.oriengo.controller;

import com.example.oriengo.mapper.TestMapper;
import com.example.oriengo.model.dto.TestCreateDTO;
import com.example.oriengo.model.dto.TestResponseDTO;
import com.example.oriengo.model.dto.TestSaveDTO;
import com.example.oriengo.model.entity.Test;

import com.example.oriengo.model.enumeration.TestStatus;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
@Validated
public class TestController {

    private final TestService testService;
    private final TestMapper testMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TestResponseDTO>>> getTests() {
        List<Test> tests = testService.getAll(false);
        List<TestResponseDTO> testResps = testMapper.toDTO(tests);
        ApiResponse<List<TestResponseDTO>> response = ApiResponse.<List<TestResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Tests fetched successfully")
                .data(testResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted")
    public ResponseEntity<ApiResponse<List<TestResponseDTO>>> getDeletedTests() {
        List<Test> tests = testService.getAll(true);
        List<TestResponseDTO> testResps = testMapper.toDTO(tests);
        ApiResponse<List<TestResponseDTO>> response = ApiResponse.<List<TestResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted tests fetched successfully")
                .data(testResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TestResponseDTO>> getTestById(@PathVariable Long id) {
        Test test = testService.getById(id, false);
        TestResponseDTO testResp = testMapper.toDTO(test);
        ApiResponse<TestResponseDTO> response = ApiResponse.<TestResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Test fetched successfully")
                .data(testResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ApiResponse<TestResponseDTO>> getDeletedTestById(@PathVariable Long id) {
        Test test = testService.getById(id, true);
        TestResponseDTO testResp = testMapper.toDTO(test);
        ApiResponse<TestResponseDTO> response = ApiResponse.<TestResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted test fetched successfully")
                .data(testResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<ApiResponse<List<TestResponseDTO>>> getTestByStudentId(@PathVariable Long id) {
        List<Test> tests = testService.getByStudentId(id, false);
        List<TestResponseDTO> testResps = testMapper.toDTO(tests);
        ApiResponse<List<TestResponseDTO>> response = ApiResponse.<List<TestResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("tests fetched successfully")
                .data(testResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/student/{id}")
    public ResponseEntity<ApiResponse<List<TestResponseDTO>>> getDeletedTestByStudentId(@PathVariable Long id) {
        List<Test> tests = testService.getByStudentId(id, true);
        List<TestResponseDTO> testResps = testMapper.toDTO(tests);
        ApiResponse<List<TestResponseDTO>> response = ApiResponse.<List<TestResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted tests fetched successfully")
                .data(testResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<ApiResponse<List<TestResponseDTO>>> getTestsByStudentIdAndStatus(@PathVariable Long studentId, @PathVariable String status) {
        List<Test> tests = testService.getTestsByStudentIdAndStatus(studentId, status, false);
        List<TestResponseDTO> dtos = testMapper.toDTO(tests);
        ApiResponse<List<TestResponseDTO>> response = ApiResponse.<List<TestResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Tests fetched successfully for student ID " + studentId + " with status " + status)
                .data(dtos)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted/student/{studentId}/status/{status}")
    public ResponseEntity<ApiResponse<List<TestResponseDTO>>> getDeletedTestsByStudentIdAndStatus(@PathVariable Long studentId, @PathVariable String status) {
        List<Test> tests = testService.getTestsByStudentIdAndStatus(studentId, status, true);
        List<TestResponseDTO> dtos = testMapper.toDTO(tests);
        ApiResponse<List<TestResponseDTO>> response = ApiResponse.<List<TestResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Tests fetched successfully for student ID " + studentId + " with status " + status)
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TestResponseDTO>> createTest(@Valid @RequestBody TestCreateDTO dto) {
        Test test = testService.createTest(dto);
        TestResponseDTO testResp = testMapper.toDTO(test);
        ApiResponse<TestResponseDTO> response = ApiResponse.<TestResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Test created successfully")
                .data(testResp)
                .build();
        URI location = URI.create("/api/tests/" + test.getId());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("hard/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteTest(@PathVariable Long id) {
        testService.hardDeleteTest(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("TEST_DELETED")
                .status(204)
                .message("Test hard deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteTest(@PathVariable Long id) {
        testService.softDeleteTest(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("SUCCESS")
                .status(204)
                .message("Test soft deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<TestResponseDTO>> restoreTest(@PathVariable Long id) {
        Test test = testService.restoreTest(id);
        TestResponseDTO testResp = testMapper.toDTO(test);
        ApiResponse<TestResponseDTO> response = ApiResponse.<TestResponseDTO>builder()
                .code("TEST_RESTORED")
                .status(200)
                .message("Test restored successfully")
                .data(testResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<TestResponseDTO>> saveUncompletedTest(@Valid @RequestBody TestSaveDTO dto) {
        Test savedTest = testService.saveUncompletedTest(dto);
        TestResponseDTO testResp = testMapper.toDTO(savedTest);
        ApiResponse<TestResponseDTO> response = ApiResponse.<TestResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Uncompleted test saved successfully")
                .data(testResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
