package com.example.oriengo.controller;

import com.example.oriengo.model.entity.TestResult;
import com.example.oriengo.service.TestResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-results")
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;

    @GetMapping
    public ResponseEntity<List<TestResult>> getAll() {
        return ResponseEntity.ok(testResultService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestResult> getById(@PathVariable Long id) {
        return testResultService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<TestResult> getByTestId(@PathVariable Long testId) {
        return testResultService.getByTestId(testId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TestResult>> getByStudentId(@PathVariable Long studentId) {
        List<TestResult> results = testResultService.getByStudentId(studentId);
        return ResponseEntity.ok(results);
    }
}
