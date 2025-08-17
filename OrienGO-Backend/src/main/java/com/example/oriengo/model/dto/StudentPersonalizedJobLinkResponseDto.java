package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.LinkType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentPersonalizedJobLinkResponseDto {

    private Long id;

    private Long studentId;

    private Long personalizedJobId;

    private LinkType type;

    private LocalDateTime createdAt;
}
