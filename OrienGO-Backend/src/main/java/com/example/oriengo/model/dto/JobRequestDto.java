package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.JobCategory;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "{job.title.notBlank}")
    @Size(max = 100, message = "{job.title.size}")
    private String title;

    @NotBlank(message = "{job.description.notBlank}")
    @Size(max = 1000, message = "{job.description.size}")
    private String description;

    @NotNull(message = "{job.category.notNull}")
    private JobCategory category;

    @Size(max = 100, message = "{job.education.size}")
    private String education;

    @Size(max = 100, message = "{job.salaryRange.size}")
    private String salaryRange;

    @Size(max = 100, message = "{job.jobMarket.size}")
    private String jobMarket;

    @Min(value = 0, message = "{job.riasecRealistic.min}")
    @Max(value = 100, message = "{job.riasecRealistic.max}")
    private Double riasecRealistic;

    @Min(value = 0, message = "{job.riasecInvestigative.min}")
    @Max(value = 100, message = "{job.riasecInvestigative.max}")
    private Double riasecInvestigative;

    @Min(value = 0, message = "{job.riasecArtistic.min}")
    @Max(value = 100, message = "{job.riasecArtistic.max}")
    private Double riasecArtistic;

    @Min(value = 0, message = "{job.riasecSocial.min}")
    @Max(value = 100, message = "{job.riasecSocial.max}")
    private Double riasecSocial;

    @Min(value = 0, message = "{job.riasecEnterprising.min}")
    @Max(value = 100, message = "{job.riasecEnterprising.max}")
    private Double riasecEnterprising;

    @Min(value = 0, message = "{job.riasecConventional.min}")
    @Max(value = 100, message = "{job.riasecConventional.max}")
    private Double riasecConventional;

    private Set<@Size(max = 50, message = "{job.tags.size}") String> tags;

    //private boolean softDeleted = false;
}
