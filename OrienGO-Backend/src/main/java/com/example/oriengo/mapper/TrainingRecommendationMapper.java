package com.example.oriengo.mapper;


import com.example.oriengo.model.dto.TrainingRecommendationRequestDTO;
import com.example.oriengo.model.dto.TrainingRecommendationResponseDTO;
import com.example.oriengo.model.entity.TrainingRecommendation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingRecommendationMapper {
    TrainingRecommendation toEntity(TrainingRecommendationRequestDTO trainingRecommendationRequestDTO);
    TrainingRecommendationResponseDTO toDTO(TrainingRecommendation trainingRecommendation);
    List<TrainingRecommendationResponseDTO> toDTO(List<TrainingRecommendation> trainingRecommendations);

}
