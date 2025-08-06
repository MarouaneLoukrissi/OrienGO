package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.TestStatus;
import com.example.oriengo.model.enumeration.TestType;
import com.example.oriengo.model.entity.Question;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestDTO {

    // Read-only field - generated automatically
    private Long id;

    @NotNull
    private TestType type;

    @NotNull
    private TestStatus status;

    private String startedAt;
    private String completedAt;

    @NotNull
    private Integer durationMinutes;

    @NotNull
    private Integer questionsCount;

    @NotNull
    private Boolean softDeleted;

    // Read-only field - linked to the student who created the test
    private Long studentId;

    private List<QuestionDTO> questions;

}
