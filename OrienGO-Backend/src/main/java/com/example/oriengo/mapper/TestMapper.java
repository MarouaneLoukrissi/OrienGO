package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.TestResponseDTO;
import com.example.oriengo.model.entity.Test;
import com.example.oriengo.model.dto.TestCreateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface TestMapper {
    @Mapping(source = "studentId", target = "student.id")
    Test toEntity(TestCreateDTO dto);

    @Mapping(source = "student.id", target = "studentId")
    TestResponseDTO toDTO(Test test);
    List<TestResponseDTO> toDTO(List<Test> tests);
}
