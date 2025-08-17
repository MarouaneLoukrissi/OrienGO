package com.example.oriengo.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AnswerOptionResponseDTO {

    private Long id;

    private Integer optionIndex;

    private String text;
}
