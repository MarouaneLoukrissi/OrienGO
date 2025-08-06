package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.StudentDTO;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.dto.StudentResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student toEntity(StudentDTO dto);
    StudentResponseDTO toResponseDto(Student student);
}
