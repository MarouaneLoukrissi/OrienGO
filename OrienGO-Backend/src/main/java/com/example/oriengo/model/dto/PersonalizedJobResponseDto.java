package com.example.oriengo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedJobResponseDto {
    
    private Long id;
    private String title;
    private String companyName;
    private String location;
    private String jobType;
    private String duration;
    private Integer matchPercentage;
    private String description;
    private String applyUrl;
    private String salaryRange;
    private String category;
    private Set<String> requiredSkills;
    private Set<String> advantages;
    private String source;
    private LocalDate postedDate;
    private LocalDateTime createdAt;
    private LocalDate expirationDate;
    private boolean highlighted;
    private Long jobRecommendationId;
    private boolean softDeleted;
} 