package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.Category;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class QuestionResponseDTO {

    private Long id;

    private Category category;

    private String text;

    // Include answer options in the response DTO, but without circular references or lazy-loading issues
    private Set<AnswerOptionResponseDTO> answerOptions;

}
