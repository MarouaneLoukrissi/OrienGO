package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.StudentCreateDTO;
import com.example.oriengo.model.entity.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toEntity(StudentCreateDTO userCreateDTO);
}
