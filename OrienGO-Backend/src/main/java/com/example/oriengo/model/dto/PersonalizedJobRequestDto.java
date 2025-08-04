package com.example.oriengo.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedJobRequestDto {
    
    @NotBlank(message = "Job title is required")
    @Size(max = 255, message = "Job title cannot exceed 255 characters")
    private String title;
    
    @Size(max = 255, message = "Company name cannot exceed 255 characters")
    private String companyName;
    
    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String location;
    
    @Size(max = 50, message = "Job type cannot exceed 50 characters")
    private String jobType;
    
    @Size(max = 100, message = "Duration cannot exceed 100 characters")
    private String duration;
    
    @NotNull(message = "Match percentage is required")
    @Min(value = 0, message = "Match percentage must be at least 0")
    @Max(value = 100, message = "Match percentage cannot exceed 100")
    private Integer matchPercentage;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @Size(max = 1000, message = "Apply URL cannot exceed 1000 characters")
    private String applyUrl;
    
    @Size(max = 100, message = "Salary range cannot exceed 100 characters")
    private String salaryRange;
    
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;
    
    private Set<String> requiredSkills;
    private Set<String> advantages;
    
    @Size(max = 100, message = "Source cannot exceed 100 characters")
    private String source;
    
    private LocalDate postedDate;
    private LocalDate expirationDate;
    private boolean highlighted = false;
    private Long jobRecommendationId;
} 