package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionWithAnswersDTO {
    @NotBlank(message = "{question.text.notblank}")
    private String text;
    @NotNull(message = "{question.category.notnull}")
    private Category category;
    @Size(min = 5, max = 5, message = "{question.answeroptions.size}")
    private List<AnswerOptionFilteredDTO> answerOptions;
}