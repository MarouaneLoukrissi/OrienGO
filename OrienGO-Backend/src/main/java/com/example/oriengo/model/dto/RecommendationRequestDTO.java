package com.example.oriengo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequestDTO {
    
    private RiasecScores riasec;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiasecScores {
        private Double realistic;
        private Double investigative;
        private Double artistic;
        private Double social;
        private Double enterprising;
        private Double conventional;
    }
}
