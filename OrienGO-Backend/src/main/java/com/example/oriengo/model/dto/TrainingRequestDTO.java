package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.TrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingRequestDTO {

    @NotBlank(message = "{training.name.notblank}")
    @Size(max = 255, message = "{training.name.size}")
    private String name;

    @Size(max = 1000, message = "{training.description.size}")
    private String description;

    @Size(max = 50, message = "{training.duration.size}")
    private String duration;

    private List<@Size(max = 100, message = "{training.specialization.size}") String> specializations;

    @NotNull(message = "{training.type.notnull}")
    private TrainingType type;

    private boolean highlighted;

    // List of Job IDs to link this training to
    private List<Long> jobIds;
}
