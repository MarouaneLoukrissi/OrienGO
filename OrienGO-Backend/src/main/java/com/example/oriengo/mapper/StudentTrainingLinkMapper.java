package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.StudentTrainingLinkResponseDTO;
import com.example.oriengo.model.entity.StudentTrainingLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentTrainingLinkMapper {
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "training.id", target = "trainingId")
    StudentTrainingLinkResponseDTO toResponseDto(StudentTrainingLink link);
}
