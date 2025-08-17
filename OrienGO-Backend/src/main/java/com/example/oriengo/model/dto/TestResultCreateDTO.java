package com.example.oriengo.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultCreateDTO {
    @NotNull(message = "{testResult.testId.notnull}")
    @Positive(message = "{testResult.testId.positive}")
    private Long testId;

//    @NotNull(message = "{testResult.answers.notnull}")
    @Size(min = 1, message = "{testResult.answers.size}")
    private Map<Long, Integer> answers;

//    @NotNull(message = "{testResult.durationMinutes.notnull}")
    @Positive(message = "{testResult.durationMinutes.positive}")
    private Integer durationMinutes;
}
