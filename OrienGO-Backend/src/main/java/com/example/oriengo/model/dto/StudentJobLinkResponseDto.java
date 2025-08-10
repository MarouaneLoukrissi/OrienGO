package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.LinkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentJobLinkResponseDto {
    
    private Long id;
    private Long studentId;
    private String studentName;
    private Long jobId;
    private String jobTitle;
    private String jobCompany;
    private LinkType type;
    private LocalDateTime createdAt;
} 