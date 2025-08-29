package com.example.oriengo.model.dto;

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
    
    private String name;
    private String type;
    private String description;
    private String duration;
    private List<String> specializations;
}
