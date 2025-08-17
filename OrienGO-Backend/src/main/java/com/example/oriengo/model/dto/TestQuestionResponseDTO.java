package com.example.oriengo.model.dto;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestionResponseDTO {
    private Long id;
    private FilteredQuestionResponseDTO question;
    private AnswerOptionResponseDTO chosenAnswer;
}
