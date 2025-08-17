package com.example.oriengo.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultMapMediaDTO {
    @NotNull(message = "{testResult.testId.notnull}")
    @Positive(message = "{testResult.testId.positive}")
    private Long id;

    @NotNull(message = "{testResult.mediaId.notnull}")
    @Positive(message = "{testResult.mediaId.positive}")
    private Long mediaId;
}
