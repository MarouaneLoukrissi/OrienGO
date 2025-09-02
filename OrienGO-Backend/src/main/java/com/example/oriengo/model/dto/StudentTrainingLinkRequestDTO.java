package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.LinkType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentTrainingLinkRequestDTO {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Training ID is required")
    private Long trainingId;

    @NotNull(message = "Link type is required")
    private LinkType type;

}
