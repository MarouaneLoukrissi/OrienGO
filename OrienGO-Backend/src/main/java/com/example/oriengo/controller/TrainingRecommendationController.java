package com.example.oriengo.controller;

import com.example.oriengo.mapper.TrainingRecommendationMapper;
import com.example.oriengo.model.dto.TrainingRecommendationRequestDTO;
import com.example.oriengo.model.dto.TrainingRecommendationResponseDTO;
import com.example.oriengo.model.entity.TrainingRecommendation;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.TrainingRecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/trainingRecommendations")
@RequiredArgsConstructor
@Validated
public class TrainingRecommendationController {
    private final TrainingRecommendationService trainingRecommendationService;
    private final TrainingRecommendationMapper trainingRecommendationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TrainingRecommendationResponseDTO>>> getTrainingRecommendations(){
        List<TrainingRecommendation> trainingRecommendations = trainingRecommendationService.getTrainingRecommendations();
        List<TrainingRecommendationResponseDTO> trainingRecommendationResps = trainingRecommendationMapper.toDTO(trainingRecommendations);
        ApiResponse<List<TrainingRecommendationResponseDTO>> response = ApiResponse.<List<TrainingRecommendationResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("TrainingRecommendations fetched successfully")
                .data(trainingRecommendationResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainingRecommendationResponseDTO>> getTrainingRecommendationById(@PathVariable Long id){
        TrainingRecommendation trainingRecommendation = trainingRecommendationService.getTrainingRecommendationById(id);
        TrainingRecommendationResponseDTO trainingRecommendationResp = trainingRecommendationMapper.toDTO(trainingRecommendation);
        ApiResponse<TrainingRecommendationResponseDTO> response = ApiResponse.<TrainingRecommendationResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("TrainingRecommendations fetched successfully")
                .data(trainingRecommendationResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteTrainingRecommendation(@PathVariable Long id){
        trainingRecommendationService.hardDeleteTrainingRecommendation(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("TRAINING_RECOMMENDATION_DELETED")
                .status(204)
                .message("TrainingRecommendation hard deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TrainingRecommendationResponseDTO>> createTrainingRecommendation(@Valid @RequestBody TrainingRecommendationRequestDTO trainingRecommendationInfo){
        TrainingRecommendation trainingRecommendation = trainingRecommendationService.createTrainingRecommendation(trainingRecommendationInfo);
        TrainingRecommendationResponseDTO trainingRecommendationResp = trainingRecommendationMapper.toDTO(trainingRecommendation);
        ApiResponse<TrainingRecommendationResponseDTO> response = ApiResponse.<TrainingRecommendationResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("TrainingRecommendation created successfully")
                .data(trainingRecommendationResp)
                .build();
        URI location = URI.create("/api/trainingRecommendation/" + trainingRecommendation.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainingRecommendationResponseDTO>> updateTrainingRecommendation(@PathVariable Long id, @Valid @RequestBody TrainingRecommendationRequestDTO trainingRecommendationInfo){
        TrainingRecommendation trainingRecommendation = trainingRecommendationService.updateTrainingRecommendation(id, trainingRecommendationInfo);
        TrainingRecommendationResponseDTO trainingRecommendationResp = trainingRecommendationMapper.toDTO(trainingRecommendation);
        ApiResponse<TrainingRecommendationResponseDTO> response = ApiResponse.<TrainingRecommendationResponseDTO>builder()
                .code("TRAINING_RECOMMENDATION_UPDATED")
                .status(200)
                .message("TrainingRecommendation updated successfully")
                .data(trainingRecommendationResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
