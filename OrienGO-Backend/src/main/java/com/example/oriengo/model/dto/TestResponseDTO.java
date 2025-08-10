package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.TestStatus;
import com.example.oriengo.model.enumeration.TestType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResponseDTO {

    private Long id;
    private Long studentId;
    private TestType type;
    private TestStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer durationMinutes;
    private Integer questionsCount;
    private boolean softDeleted;
}
