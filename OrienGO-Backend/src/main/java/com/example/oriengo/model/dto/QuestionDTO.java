package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDTO {
    private Long id;
    private Category category;
    private String text;
} 