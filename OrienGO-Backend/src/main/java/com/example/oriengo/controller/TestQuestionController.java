package com.example.oriengo.controller;

import com.example.oriengo.mapper.TestQuestionMapper;
import com.example.oriengo.model.dto.TestQuestionResponseDTO;
import com.example.oriengo.model.entity.TestQuestion;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.TestQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/test-questions")
@RequiredArgsConstructor
@Validated
public class TestQuestionController {

    private final TestQuestionService testQuestionService;
    private final TestQuestionMapper testQuestionMapper;

    @GetMapping("/{testId}/with-responses")
    public ResponseEntity<ApiResponse<Set<TestQuestionResponseDTO>>> getTestQuestionsWithResponses(@PathVariable Long testId) {
        Set<TestQuestion> testQuestions = testQuestionService.getTestQuestionsWithResponsesByTestId(testId);
        Set<TestQuestionResponseDTO> dtos = testQuestionMapper.toDTO(testQuestions);

        ApiResponse<Set<TestQuestionResponseDTO>> response = ApiResponse.<Set<TestQuestionResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Test questions with responses fetched successfully for test ID " + testId)
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

}
