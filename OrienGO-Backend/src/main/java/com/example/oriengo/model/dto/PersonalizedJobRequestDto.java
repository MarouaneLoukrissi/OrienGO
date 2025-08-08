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

    @Size(max = 100, message = "{personalizedJob.duration.size}")
    private String duration;

    @NotNull(message = "{personalizedJob.matchPercentage.notNull}")
    @Min(value = 0, message = "{personalizedJob.matchPercentage.min}")
    @Max(value = 100, message = "{personalizedJob.matchPercentage.max}")
    private Integer matchPercentage;

    @Size(max = 1000, message = "{personalizedJob.description.size}")
    private String description;

    @Size(max = 1000, message = "{personalizedJob.applyUrl.size}")
    private String applyUrl;

    @Size(max = 100, message = "{personalizedJob.salaryRange.size}")
    private String salaryRange;

    @Size(max = 100, message = "{personalizedJob.category.size}")
    private String category;

    private Set<@Size(max = 50, message = "{personalizedJob.requiredSkill.size}") String> requiredSkills;
    private Set<@Size(max = 50, message = "{personalizedJob.advantage.size}") String> advantages;

    @Size(max = 100, message = "{personalizedJob.source.size}")
    private String source;

    private LocalDate postedDate;
    private LocalDate expirationDate;

    private boolean highlighted = false;

    @NotNull(message = "{personalizedJob.jobRecommendationId.notNull}")
    private Long jobRecommendationId;
}
