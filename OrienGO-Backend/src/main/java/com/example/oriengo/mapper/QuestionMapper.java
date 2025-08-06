package com.example.oriengo.mapper;

import com.example.oriengo.model.entity.Question;
import com.example.oriengo.model.dto.QuestionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionDTO toDto(Question question);
} 