package com.example.oriengo.controller;

import com.example.oriengo.mapper.TrainingMapper;
import com.example.oriengo.model.dto.TrainingRequestDTO;
import com.example.oriengo.model.dto.TrainingResponseDTO;
import com.example.oriengo.model.entity.Training;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
@Validated
public class TrainingController {
    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TrainingResponseDTO>>> getTrainings(){
        List<Training> trainings = trainingService.getTrainings();
        List<TrainingResponseDTO> trainingResps = trainingMapper.toDTO(trainings);
        ApiResponse<List<TrainingResponseDTO>> response = ApiResponse.<List<TrainingResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Trainings fetched successfully")
                .data(trainingResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainingResponseDTO>> getTrainingById(@PathVariable Long id){
        Training training = trainingService.getTrainingById(id);
        TrainingResponseDTO trainingResp = trainingMapper.toDTO(training);
        ApiResponse<TrainingResponseDTO> response = ApiResponse.<TrainingResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Trainings fetched successfully")
                .data(trainingResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteTraining(@PathVariable Long id){
        trainingService.hardDeleteTraining(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("TRAINING_DELETED")
                .status(204)
                .message("Training hard deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TrainingResponseDTO>> createTraining(@Valid @RequestBody TrainingRequestDTO trainingInfo){
        Training training = trainingService.createTraining(trainingInfo);
        TrainingResponseDTO trainingResp = trainingMapper.toDTO(training);
        ApiResponse<TrainingResponseDTO> response = ApiResponse.<TrainingResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Training created successfully")
                .data(trainingResp)
                .build();
        URI location = URI.create("/api/training/" + training.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainingResponseDTO>> updateTraining(@PathVariable Long id, @Valid @RequestBody TrainingRequestDTO trainingInfo){
        Training training = trainingService.updateTraining(id, trainingInfo);
        TrainingResponseDTO trainingResp = trainingMapper.toDTO(training);
        ApiResponse<TrainingResponseDTO> response = ApiResponse.<TrainingResponseDTO>builder()
                .code("TRAINING_UPDATED")
                .status(200)
                .message("Training updated successfully")
                .data(trainingResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
