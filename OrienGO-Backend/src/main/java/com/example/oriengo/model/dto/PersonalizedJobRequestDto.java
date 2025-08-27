package com.example.oriengo.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedJobRequestDto {

    @NotBlank(message = "{personalizedJob.title.notBlank}")
    @Size(max = 255, message = "{personalizedJob.title.size}")
    private String title;

    @Size(max = 255, message = "{personalizedJob.companyName.size}")
    private String companyName;

    @Size(max = 255, message = "{personalizedJob.location.size}")
    private String location;

    @Size(max = 50, message = "{personalizedJob.jobType.size}")
    private String jobType;

    @Size(max = 1000, message = "{personalizedJob.description.size}")
    private String description;

    @Size(max = 1000, message = "{personalizedJob.applyUrl.size}")
    private String applyUrl;

    @Size(max = 100, message = "{personalizedJob.salaryRange.size}")
    private String salaryRange;

    @Size(max = 100, message = "{personalizedJob.category.size}")
    private String category;

    @Size(max = 100, message = "{personalizedJob.source.size}")
    private String source;

    private LocalDate postedDate;

    // ---------------------------- Extra company/job info ---------------------------- //

    @Size(max = 2000, message = "{personalizedJob.requiredSkills.size}")
    private String requiredSkills; // comma-separated

    @Size(max = 1000, message = "{personalizedJob.companyUrl.size}")
    private String companyUrl;

    @Size(max = 1000, message = "{personalizedJob.companyUrlDirect.size}")
    private String companyUrlDirect;

    @Size(max = 1000, message = "{personalizedJob.companyAddresses.size}")
    private String companyAddresses; // comma-separated

    private Integer companyNumEmployees;

    @Size(max = 255, message = "{personalizedJob.companyRevenue.size}")
    private String companyRevenue;

    private String companyDescription; // TEXT, so no strict @Size

    @Size(max = 255, message = "{personalizedJob.experienceRange.size}")
    private String experienceRange;

    @Size(max = 2000, message = "{personalizedJob.emails.size}")
    private String emails; // comma-separated

    @Size(max = 255, message = "{personalizedJob.companyIndustry.size}")
    private String companyIndustry;

    @Size(max = 1000, message = "{personalizedJob.jobUrlDirect.size}")
    private String jobUrlDirect;

    private Boolean isRemote;

    // ---------------------------- Other fields ---------------------------- //

    private LocalDate expirationDate;

    @Size(max = 100, message = "{personalizedJob.duration.size}")
    private String duration;

    private Set<@Size(max = 50, message = "{personalizedJob.advantage.size}") String> advantages;

    @NotNull(message = "{personalizedJob.matchPercentage.notNull}")
    @Min(value = 0, message = "{personalizedJob.matchPercentage.min}")
    @Max(value = 100, message = "{personalizedJob.matchPercentage.max}")
    private Integer matchPercentage;

    private boolean highlighted = false;

    @NotNull(message = "{personalizedJob.jobRecommendationId.notNull}")
    private Long jobRecommendationId;
}
