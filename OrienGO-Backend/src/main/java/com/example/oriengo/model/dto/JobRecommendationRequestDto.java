package com.example.oriengo.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendationRequestDto {

    @NotNull(message = "{jobRecommendation.testResultId.notNull}")
    @Positive(message = "{jobRecommendation.testResultId.positive}")
    private Long testResultId;

    @NotNull(message = "{jobRecommendation.jobId.notNull}")
    @Positive(message = "{jobRecommendation.jobId.positive}")
    private Long jobId;

    @NotNull(message = "{jobRecommendation.matchPercentage.notNull}")
    @Min(value = 0, message = "{jobRecommendation.matchPercentage.min}")
    @Max(value = 100, message = "{jobRecommendation.matchPercentage.max}")
    private Integer matchPercentage;

    private boolean highlighted = false;
}
