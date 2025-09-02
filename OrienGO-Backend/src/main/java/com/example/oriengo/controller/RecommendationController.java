package com.example.oriengo.controller;

import com.example.oriengo.mapper.StudentJobLinkMapper;
import com.example.oriengo.mapper.StudentTrainingLinkMapper;
import com.example.oriengo.model.dto.*;
import com.example.oriengo.model.entity.StudentJobLink;
import com.example.oriengo.model.entity.StudentTrainingLink;
import com.example.oriengo.model.enumeration.LinkType;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Slf4j
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final StudentTrainingLinkMapper studentTrainingLinkMapper;
    private final StudentJobLinkMapper studentJobLinkMapper;

    @GetMapping("/by-link-type")
    public ResponseEntity<ApiResponse<RecommendationResponseDTO>> getRecommendationsByType(
            @RequestParam LinkType type,
            @RequestParam(required = false) Long studentId) {

        RecommendationResponseDTO response = recommendationService.fetchRecommendationsByType(type, studentId);

        return ResponseEntity.ok(ApiResponse.<RecommendationResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Recommendations fetched successfully")
                .data(response)
                .build());
    }

    @PostMapping("/link-job")
    public ResponseEntity<ApiResponse<StudentJobLinkResponseDto>> linkJob(@RequestBody StudentJobLinkRequestDto request) {
        StudentJobLink link = recommendationService.linkJob(request.getStudentId(), request.getJobId(), request.getType());
        StudentJobLinkResponseDto responseDto = studentJobLinkMapper.toResponseDto(link);

        return ResponseEntity.ok(ApiResponse.<StudentJobLinkResponseDto>builder()
                .code("SUCCESS")
                .status(200)
                .message("Job linked successfully")
                .data(responseDto)
                .build());
    }


    @DeleteMapping("/unlink-job")
    public ResponseEntity<ApiResponse<String>> unlinkJob(@RequestBody StudentJobLinkRequestDto request) {
        recommendationService.unlinkJob(request.getStudentId(), request.getJobId(), request.getType());
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code("SUCCESS")
                .status(200)
                .message("Job unlinked successfully")
                .data(null)
                .build());
    }

    @PostMapping("/link-training")
    public ResponseEntity<ApiResponse<StudentTrainingLinkResponseDTO>> linkTraining(
            @RequestBody StudentTrainingLinkRequestDTO request) {

        StudentTrainingLink link = recommendationService.linkTraining(
                request.getStudentId(),
                request.getTrainingId(),
                request.getType()
        );

        StudentTrainingLinkResponseDTO responseDto = studentTrainingLinkMapper.toResponseDto(link);

        return ResponseEntity.ok(ApiResponse.<StudentTrainingLinkResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Training linked successfully")
                .data(responseDto)
                .build());
    }

    @DeleteMapping("/unlink-training")
    public ResponseEntity<ApiResponse<String>> unlinkTraining(@RequestBody StudentTrainingLinkRequestDTO request) {
        recommendationService.unlinkTraining(request.getStudentId(), request.getTrainingId(), request.getType());
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code("SUCCESS")
                .status(200)
                .message("Training unlinked successfully")
                .data(null)
                .build());
    }

    @GetMapping("/fetch-existing")
    public ResponseEntity<ApiResponse<RecommendationResponseDTO>> fetchExistingRecommendations(
            @RequestParam Long testResultId,
            @RequestParam(required = false) Long studentId) {

        try {
            RecommendationResponseDTO response = recommendationService.fetchExistingRecommendations(testResultId, studentId);

            if (response == null || (response.getJobs().isEmpty() && response.getTrainings().isEmpty())) {
                return ResponseEntity.ok(ApiResponse.<RecommendationResponseDTO>builder()
                        .code("NO_RECOMMENDATIONS")
                        .status(200)
                        .message("No recommendations found")
                        .data(null)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.<RecommendationResponseDTO>builder()
                    .code("SUCCESS")
                    .status(200)
                    .message("Recommendations fetched successfully")
                    .data(response)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<RecommendationResponseDTO>builder()
                            .code("INTERNAL_SERVER_ERROR")
                            .status(500)
                            .message("Error fetching recommendations: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }



    @PostMapping("/process")
    public ResponseEntity<ApiResponse<RecommendationResponseDTO>> processRecommendations(
            @RequestParam Long studentId,
            @RequestParam Long testResultId) {

        log.info("Received recommendation request for student {} and test result {}", studentId, testResultId);

        try {
            // Validation des paramètres
            if (studentId == null || testResultId == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.<RecommendationResponseDTO>builder()
                                .code("BAD_REQUEST")
                                .status(400)
                                .message("Both studentId and testResultId are required")
                                .build());
            }

            // Traiter les recommandations
            RecommendationResponseDTO response = recommendationService.processRecommendations(studentId, testResultId);

            log.info("Successfully processed recommendations for student {} and test result {}", studentId, testResultId);

            return ResponseEntity.ok(ApiResponse.<RecommendationResponseDTO>builder()
                    .code("SUCCESS")
                    .status(200)
                    .data(response)
                    .message("Recommendations processed successfully")
                    .build());

        } catch (Exception e) {
            log.error("Error processing recommendations: {}", e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<RecommendationResponseDTO>builder()
                            .code("INTERNAL_SERVER_ERROR")
                            .status(500)
                            .message("Failed to process recommendations: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Recommendation service is running");
    }
}
