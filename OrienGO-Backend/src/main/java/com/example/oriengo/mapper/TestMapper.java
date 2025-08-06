package com.example.oriengo.mapper;

import com.example.oriengo.model.entity.Test;
import com.example.oriengo.model.dto.TestDTO;
import com.example.oriengo.model.entity.Question;
import com.example.oriengo.model.dto.QuestionDTO;

import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface TestMapper {
    Test toEntity(TestDTO dto);
    
    TestDTO toDto(Test test);
    
    default List<QuestionDTO> map(Set<Question> questions) {
        if (questions == null) {
            return null;
        }
        return questions.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
    
    QuestionDTO map(Question question);
}
