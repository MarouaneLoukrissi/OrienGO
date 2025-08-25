package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.RequestInitiator;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoachStudentConnectionCreateDTO {
    private Long coachId;
    private Long studentId;
    private RequestInitiator requestedBy;
}