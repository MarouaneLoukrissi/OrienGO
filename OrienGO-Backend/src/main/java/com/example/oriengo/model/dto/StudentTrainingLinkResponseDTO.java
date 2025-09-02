package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.LinkType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentTrainingLinkResponseDTO {
    private Long id;
    private Long studentId;
    private Long trainingId;
    private LinkType type;
    private LocalDateTime createdAt;
}
