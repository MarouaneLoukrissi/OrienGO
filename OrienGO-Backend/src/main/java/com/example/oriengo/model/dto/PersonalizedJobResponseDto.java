package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.LinkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    private String description;
    private String applyUrl;
    private String salaryRange;
    private String category;
    private String source;
    private LocalDate postedDate;

    // ---------------------------- Extra company/job info ---------------------------- //

    private String requiredSkills; // comma-separated
    private String companyUrl;
    private String companyUrlDirect;
    private String companyAddresses; // comma-separated
    private Integer companyNumEmployees;
    private String companyRevenue;
    private String companyDescription;
    private String experienceRange;
    private String emails; // comma-separated
    private String companyIndustry;
    private String jobUrlDirect;
    private Boolean isRemote;

    // ---------------------------- Other fields ---------------------------- //

    private LocalDateTime createdAt;
    private LocalDate expirationDate;
    private String duration;
    private Set<String> advantages;
    private Integer matchPercentage;
    private boolean highlighted;

    private Long jobRecommendationId;
    private List<LinkType> linkTypes; // <-- new field

    private boolean softDeleted;
} 