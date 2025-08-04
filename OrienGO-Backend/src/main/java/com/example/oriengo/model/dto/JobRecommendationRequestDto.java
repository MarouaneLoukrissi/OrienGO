package com.example.oriengo.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendationRequestDto {
    
    @NotNull(message = "Test result ID is required")
    private Long testResultId;
    
    @NotNull(message = "Job ID is required")
    private Long jobId;
    
    @NotNull(message = "Match percentage is required")
    @Min(value = 0, message = "Match percentage must be at least 0")
    @Max(value = 100, message = "Match percentage cannot exceed 100")
    private Integer matchPercentage;
    
    private boolean highlighted = false;
} 