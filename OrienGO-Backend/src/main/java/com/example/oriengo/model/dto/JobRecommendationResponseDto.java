package com.example.oriengo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendationResponseDto {
    
    private Long id;
    private Long testResultId;
    private Long jobId;
    private String jobTitle;
    private String jobCompany;
    private String jobLocation;
    private Integer matchPercentagen;
    private boolean highlighted;
    private Set<PersonalizedJobResponseDto> personalizedJobs;
} 