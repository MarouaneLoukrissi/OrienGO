package com.example.oriengo.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerOptionFilteredDTO {
    private Integer optionIndex;
    private String text;
}
