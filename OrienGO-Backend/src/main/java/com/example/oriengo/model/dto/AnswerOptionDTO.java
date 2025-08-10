package com.example.oriengo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerOptionDTO {

    @NotNull(message = "{answerOption.questionId.notNull}")
    private Long questionId;

    @NotNull(message = "{answerOption.optionIndex.notNull}")
    private Integer optionIndex;

    @NotBlank(message = "{answerOption.text.notBlank}")
    @Size(max = 500, message = "{answerOption.text.size}")
    private String text;
}
