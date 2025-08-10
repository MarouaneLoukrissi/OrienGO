package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.LinkType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentPersonalizedJobLinkRequestDto {

    @NotNull(message = "{studentPersonalizedJobLink.studentId.notNull}")
    @Positive(message = "{studentPersonalizedJobLink.studentId.positive}")
    private Long studentId;

    @NotNull(message = "{studentPersonalizedJobLink.personalizedJobId.notNull}")
    @Positive(message = "{studentPersonalizedJobLink.personalizedJobId.positive}")
    private Long personalizedJobId;

    @NotNull(message = "{studentPersonalizedJobLink.type.notNull}")
    private LinkType type;
}
