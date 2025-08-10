package com.example.oriengo.controller;

import com.example.oriengo.mapper.AnswerOptionMapper;
import com.example.oriengo.model.dto.AnswerOptionDTO;
import com.example.oriengo.model.dto.AnswerOptionResponseDTO;
import com.example.oriengo.model.entity.AnswerOption;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.AnswerOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/api/answer-options")
@RequiredArgsConstructor
@Validated
public class AnswerOptionController {
    private final AnswerOptionService answerOptionService;
    private final AnswerOptionMapper answerOptionMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Set<AnswerOptionResponseDTO>>> getAnswerOptions(){
        Set<AnswerOption> answerOptions = answerOptionService.getAnswerOptions();
        Set<AnswerOptionResponseDTO> answerOptionResps = answerOptionMapper.toDTO(answerOptions);
        ApiResponse<Set<AnswerOptionResponseDTO>> response = ApiResponse.<Set<AnswerOptionResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("AnswerOptions fetched successfully")
                .data(answerOptionResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AnswerOptionResponseDTO>> getAnswerOptionById(@PathVariable Long id){
        AnswerOption answerOption = answerOptionService.getAnswerOptionById(id);
        AnswerOptionResponseDTO answerOptionResp = answerOptionMapper.toDTO(answerOption);
        ApiResponse<AnswerOptionResponseDTO> response = ApiResponse.<AnswerOptionResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("AnswerOptions fetched successfully")
                .data(answerOptionResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteAnswerOption(@PathVariable Long id){
        answerOptionService.hardDeleteAnswerOption(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("ANSWER_OPTION_DELETED")
                .status(204)
                .message("AnswerOption hard deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AnswerOptionResponseDTO>> createAnswerOption(@Valid @RequestBody AnswerOptionDTO answerOptionInfo){
        AnswerOption answerOption = answerOptionService.createAnswerOption(answerOptionInfo);
        AnswerOptionResponseDTO answerOptionResp = answerOptionMapper.toDTO(answerOption);
        ApiResponse<AnswerOptionResponseDTO> response = ApiResponse.<AnswerOptionResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("AnswerOption created successfully")
                .data(answerOptionResp)
                .build();
        URI location = URI.create("/api/answerOption/" + answerOption.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AnswerOptionResponseDTO>> updateAnswerOption(@PathVariable Long id, @Valid @RequestBody AnswerOptionDTO answerOptionInfo){
        AnswerOption answerOption = answerOptionService.updateAnswerOption(id, answerOptionInfo);
        AnswerOptionResponseDTO answerOptionResp = answerOptionMapper.toDTO(answerOption);
        ApiResponse<AnswerOptionResponseDTO> response = ApiResponse.<AnswerOptionResponseDTO>builder()
                .code("ANSWER_OPTION_UPDATED")
                .status(200)
                .message("AnswerOption updated successfully")
                .data(answerOptionResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
