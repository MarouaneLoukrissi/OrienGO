package com.example.oriengo.controller;

import com.example.oriengo.model.dto.RecommendationResponseDTO;
import com.example.oriengo.service.RecommendationService;
import com.example.oriengo.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;

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
