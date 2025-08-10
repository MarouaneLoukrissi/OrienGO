package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
    @NotNull(message = "Category must not be null")
    private Category category;

    @NotBlank(message = "Question text must not be blank")
    @Size(max = 1000, message = "Question text must not exceed 1000 characters")
    private String text;
}