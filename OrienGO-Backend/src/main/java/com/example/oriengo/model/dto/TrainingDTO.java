package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.LinkType;
import com.example.oriengo.model.enumeration.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {

    private Long id;
    private String name;
    private TrainingType type;
    private String description;
    private String duration;
    private List<String> specializations;
    private Integer matchPercentage;
    private List<LinkType> linkType;

}