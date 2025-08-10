package com.example.oriengo.controller;

import com.example.oriengo.mapper.QuestionMapper;
import com.example.oriengo.model.dto.QuestionDTO;
import com.example.oriengo.model.dto.QuestionResponseDTO;
import com.example.oriengo.model.entity.Question;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Validated
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionResponseDTO>>> getQuestions(){
        List<Question> questions = questionService.getQuestions(false);
        List<QuestionResponseDTO> questionResps = questionMapper.toDTO(questions);
        ApiResponse<List<QuestionResponseDTO>> response = ApiResponse.<List<QuestionResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Questions fetched successfully")
                .data(questionResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted")
    public ResponseEntity<ApiResponse<List<QuestionResponseDTO>>> getDeletedQuestions(){
        List<Question> questions = questionService.getQuestions(true);
        List<QuestionResponseDTO> questionResps = questionMapper.toDTO(questions);
        ApiResponse<List<QuestionResponseDTO>> response = ApiResponse.<List<QuestionResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Questions fetched successfully")
                .data(questionResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> getQuestionById(@PathVariable Long id){
        Question question = questionService.getQuestionById(id, false);
        QuestionResponseDTO questionResp = questionMapper.toDTO(question);
        ApiResponse<QuestionResponseDTO> response = ApiResponse.<QuestionResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Questions fetched successfully")
                .data(questionResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> getDeletedQuestionById(@PathVariable Long id){
        Question question = questionService.getQuestionById(id, true);
        QuestionResponseDTO questionResp = questionMapper.toDTO(question);
        ApiResponse<QuestionResponseDTO> response = ApiResponse.<QuestionResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Questions fetched successfully")
                .data(questionResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("hard/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteQuestion(@PathVariable Long id){
        questionService.hardDeleteQuestion(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("QUESTION_DELETED")
                .status(204)
                .message("Question hard deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteQuestion(@PathVariable Long id){
        questionService.softDeleteQuestion(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("SUCCESS")
                .status(204)
                .message("Question soft deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> restoreQuestion(@PathVariable Long id){
        Question question = questionService.restoreQuestion(id);
        QuestionResponseDTO questionResp = questionMapper.toDTO(question);
        ApiResponse<QuestionResponseDTO> response = ApiResponse.<QuestionResponseDTO>builder()
                .code("QUESTION_RESTORED")
                .status(200)
                .message("Question restored successfully")
                .data(questionResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> createQuestion(@Valid @RequestBody QuestionDTO questionInfo){
        Question question = questionService.createQuestion(questionInfo);
        QuestionResponseDTO questionResp = questionMapper.toDTO(question);
        ApiResponse<QuestionResponseDTO> response = ApiResponse.<QuestionResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Question created successfully")
                .data(questionResp)
                .build();
        URI location = URI.create("/api/question/" + question.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionDTO questionInfo){
        Question question = questionService.updateQuestion(id, questionInfo);
        QuestionResponseDTO questionResp = questionMapper.toDTO(question);
        ApiResponse<QuestionResponseDTO> response = ApiResponse.<QuestionResponseDTO>builder()
                .code("QUESTION_UPDATED")
                .status(200)
                .message("Question updated successfully")
                .data(questionResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
