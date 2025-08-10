package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.TestQuestionResponseDTO;
import com.example.oriengo.model.entity.TestQuestion;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TestQuestionMapper {
    Set<TestQuestionResponseDTO> toDTO(Set<TestQuestion> testQuestions);
}
