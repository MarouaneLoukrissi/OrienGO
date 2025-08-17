package com.example.oriengo.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingRecommendationResponseDTO {
    private Long id;

    private Long testResultId;
    private Long trainingId;

    // Optional extra fields for display purposes
    private String trainingName;
    private String trainingDescription;

    private Integer matchPercentage;

    private boolean highlighted;
}
