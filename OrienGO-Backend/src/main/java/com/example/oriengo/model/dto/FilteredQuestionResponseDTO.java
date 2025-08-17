package com.example.oriengo.model.dto;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FilteredQuestionResponseDTO {
    private Long id;
    private String text;
    private Set<AnswerOptionResponseDTO> answerOptions;
}