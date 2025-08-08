package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.JobCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDto {
    
    private Long id;
    private String title;
    private String description;
    private JobCategory category;
    private String education;
    private String salaryRange;
    private String jobMarket;
    private Double riasecRealistic;
    private Double riasecInvestigative;
    private Double riasecArtistic;
    private Double riasecSocial;
    private Double riasecEnterprising;
    private Double riasecConventional;
    private Set<String> tags;
    private boolean softDeleted;
    private boolean active;
    private Long version;
} 