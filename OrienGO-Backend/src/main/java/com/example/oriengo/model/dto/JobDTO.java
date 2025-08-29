package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.JobCategory;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {

    private Long id;

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

    // RIASEC attributes
    @DecimalMin(value = "0.00", inclusive = true, message = "{job.riasec.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{job.riasec.max}")
    private Double riasecRealistic;

    @DecimalMin(value = "0.00", inclusive = true, message = "{job.riasec.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{job.riasec.max}")
    private Double riasecInvestigative;

    @DecimalMin(value = "0.00", inclusive = true, message = "{job.riasec.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{job.riasec.max}")
    private Double riasecArtistic;

    @DecimalMin(value = "0.00", inclusive = true, message = "{job.riasec.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{job.riasec.max}")
    private Double riasecSocial;

    @DecimalMin(value = "0.00", inclusive = true, message = "{job.riasec.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{job.riasec.max}")
    private Double riasecEnterprising;

    @DecimalMin(value = "0.00", inclusive = true, message = "{job.riasec.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{job.riasec.max}")
    private Double riasecConventional;

    private Set<String> tags;

    private boolean active;

    private boolean softDeleted;

    private Long version;
}
