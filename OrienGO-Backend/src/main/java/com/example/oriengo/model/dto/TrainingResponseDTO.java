package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.TrainingType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingResponseDTO {
    private Long id;

    private String name;

    private String description;

    private String duration;

    private List<String> specializations;

    private TrainingType type;

    private boolean highlighted;
}
