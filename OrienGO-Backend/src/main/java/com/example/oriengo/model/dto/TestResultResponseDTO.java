package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.Category;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestResultResponseDTO {

    private Long id;

    private Long testId; // on expose juste l'id du Test, pas l'objet complet

    private Category dominantType;

    private String dominantTypeDescription;

    private Map<Category, Double> scores;

    private String keyPoints;

    private Long pdfId; // id du media PDF, s'il existe

    private boolean shared;

    private boolean downloaded;

    private boolean softDeleted;
}
