package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.ConnectionStatus;
import com.example.oriengo.model.enumeration.RequestInitiator;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoachStudentConnectionUpdateDTO {
    private Long coachId;
    private Long studentId;
    private ConnectionStatus status;
    private LocalDateTime respondedAt;
    private RequestInitiator requestedBy;
}
