package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.JobCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequestDto {
    
    @NotBlank(message = "Job title is required")
    @Size(max = 100, message = "Job title cannot exceed 100 characters")
    private String title;
    
    @NotBlank(message = "Job description is required")
    @Size(max = 1000, message = "Job description cannot exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Job category is required")
    private JobCategory category;
    
    @Size(max = 100, message = "Education requirement cannot exceed 100 characters")
    private String education;
    
    @Size(max = 100, message = "Salary range cannot exceed 100 characters")
    private String salaryRange;
    
    @Size(max = 100, message = "Job market cannot exceed 100 characters")
    private String jobMarket;
    
    // RIASEC attributes
    private Double riasecRealistic;
    private Double riasecInvestigative;
    private Double riasecArtistic;
    private Double riasecSocial;
    private Double riasecEnterprising;
    private Double riasecConventional;
    
    private Set<String> tags;
    
    private boolean softDeleted = false;
} 