package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.TestType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCreateDTO {

    @NotNull(message = "{test.studentId.notnull}")
    @Positive(message = "{test.studentId.positive}")
    private Long studentId;

    @NotNull(message = "{test.type.notnull}")
    private TestType type;
}
