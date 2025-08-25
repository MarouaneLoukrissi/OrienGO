package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.TestType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCountDTO {
    private TestType testType;
    private Long count;
}
