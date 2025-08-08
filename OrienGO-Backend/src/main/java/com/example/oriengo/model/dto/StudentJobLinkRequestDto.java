package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.LinkType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentJobLinkRequestDto {

    @NotNull(message = "{studentJobLink.studentId.notNull}")
    @Positive(message = "{studentJobLink.studentId.positive}")
    private Long studentId;

    @NotNull(message = "{studentJobLink.jobId.notNull}")
    @Positive(message = "{studentJobLink.jobId.positive}")
    private Long jobId;

    @NotNull(message = "{studentJobLink.type.notNull}")
    private LinkType type;
}
