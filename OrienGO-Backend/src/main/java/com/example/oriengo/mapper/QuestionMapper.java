package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.FilteredQuestionResponseDTO;
import com.example.oriengo.model.dto.QuestionResponseDTO;
import com.example.oriengo.model.entity.Question;
import com.example.oriengo.model.dto.QuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {AnswerOptionMapper.class})
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
    // Map Question entity to QuestionResponseDTO, MapStruct auto maps answerOptions
    @Mapping(target = "answerOptions", source = "answerOptions")
    QuestionResponseDTO toDTO(Question question);

    List<QuestionResponseDTO> toDTO(List<Question> question);

    // Map Question entity to QuestionResponseDTO, MapStruct auto maps answerOptions
    @Mapping(target = "answerOptions", source = "answerOptions")
    FilteredQuestionResponseDTO toFilteredDTO(Question question);
    List<FilteredQuestionResponseDTO> toFilteredDTO(List<Question> question);
    // Map collections automatically
    Set<QuestionResponseDTO> toDTO(Set<Question> questions);
    Question toEntity(QuestionDTO questionDTO);

    void updateQuestionFromDto(QuestionDTO dto, @MappingTarget Question entity);
}