package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.TestStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jdk.jshell.Snippet;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestSaveDTO {

    @NotNull(message = "{testSave.testId.notnull}")
    @Positive(message = "{testSave.testId.positive}")
    private Long testId;

    @NotNull(message = "{testSave.answers.notnull}")
    @Size(min = 1, message = "{testSave.answers.size}")
    private Map<Long, Integer> answers;

    @NotNull(message = "{testSave.durationMinutes.notnull}")
    @Positive(message = "{testSave.durationMinutes.positive}")
    private Integer durationMinutes;

    private TestStatus status;

    @PastOrPresent(message = "{testSave.completedAt.pastOrPresent}")
    private LocalDateTime completedAt;

    @Positive(message = "{testSave.durationMinutes.positive}")
    private Integer answeredQuestionsCount;
}
