package com.example.oriengo.controller;

import com.example.oriengo.model.dto.StudentDTO;
import com.example.oriengo.model.dto.TestDTO;
import com.example.oriengo.model.enumeration.TestType;
import com.example.oriengo.model.entity.TestResult;

import com.example.oriengo.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping
    public ResponseEntity<List<TestDTO>> list() {
        return ResponseEntity.ok(testService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestDTO> get(@PathVariable Long id) {
        return testService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/start")
    public ResponseEntity<TestDTO> startTest(@RequestBody StudentDTO studentDto, @RequestParam TestType type) {
        TestDTO test = testService.createStudentAndTestWithRandomQuestions(studentDto, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(test);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TestDTO> complete(@PathVariable Long id, @Valid @RequestBody TestDTO dto) {
        return testService.completeTest(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<TestDTO> cancel(@PathVariable Long id) {
        return testService.cancelTest(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{testId}/submit")
    public ResponseEntity<TestResult> submitTest(@PathVariable Long testId, @RequestBody Map<Long, Integer> answers) {
        TestResult result = testService.submitTestAndCalculateResults(testId, answers);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = testService.softDelete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
