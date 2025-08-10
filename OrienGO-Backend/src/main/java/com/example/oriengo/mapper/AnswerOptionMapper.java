package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.AnswerOptionDTO;
import com.example.oriengo.model.dto.AnswerOptionResponseDTO;
import com.example.oriengo.model.entity.AnswerOption;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface AnswerOptionMapper {
    AnswerOptionMapper INSTANCE = Mappers.getMapper(AnswerOptionMapper.class);

    AnswerOptionResponseDTO toDTO(AnswerOption answerOption);
    Set<AnswerOptionResponseDTO> toDTO(Set<AnswerOption> answerOptions);
    AnswerOption toEntity(AnswerOptionDTO answerOptionDTO);

    void updateAnswerOptionFromDto(AnswerOptionDTO dto,@MappingTarget AnswerOption existingAnswerOption);
}
