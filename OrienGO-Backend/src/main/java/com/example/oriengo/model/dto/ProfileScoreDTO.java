package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.Category;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileScoreDTO {
    private Category category;
    private Double percentage;
}
