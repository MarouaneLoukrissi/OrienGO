package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.TestResponseDTO;
import com.example.oriengo.model.entity.Test;
import com.example.oriengo.model.dto.TestCreateDTO;
import com.example.oriengo.model.entity.Question;
import com.example.oriengo.model.dto.QuestionDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface TestMapper {
    @Mapping(source = "studentId", target = "student.id")
    Test toEntity(TestCreateDTO dto);

    @Mapping(source = "student.id", target = "studentId")
    TestResponseDTO toDTO(Test test);

    List<TestResponseDTO> toDTO(List<Test> tests);
    
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
