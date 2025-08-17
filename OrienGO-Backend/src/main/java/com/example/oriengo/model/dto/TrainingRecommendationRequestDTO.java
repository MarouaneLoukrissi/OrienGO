package com.example.oriengo.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingRecommendationRequestDTO {

    @NotNull(message = "{trainingRecommendation.testResultId.notnull}")
    private Long testResultId;

    @NotNull(message = "{trainingRecommendation.trainingId.notnull}")
    private Long trainingId;

    @NotNull(message = "{trainingRecommendation.matchPercentage.notnull}")
    @Min(value = 0, message = "{trainingRecommendation.matchPercentage.min}")
    @Max(value = 100, message = "{trainingRecommendation.matchPercentage.max}")
    private Integer matchPercentage;

    private boolean highlighted;
}
