package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.TrainingRequestDTO;
import com.example.oriengo.model.dto.TrainingResponseDTO;
import com.example.oriengo.model.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    Training toEntity(TrainingRequestDTO dto);
    TrainingResponseDTO toDTO(Training training);
    List<TrainingResponseDTO> toDTO(List<Training> training);

    void updateTrainingFromDto(TrainingRequestDTO dto, @MappingTarget Training existingTraining);
}
