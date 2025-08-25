package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.StudentCreateDTO;
import com.example.oriengo.model.dto.StudentResponseDTO;
import com.example.oriengo.model.dto.StudentUpdateDTO;
import com.example.oriengo.model.entity.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "educationLevel", source = "educationLevel")
    Student toEntity(StudentCreateDTO studentCreateDTO);
    Student toEntity(StudentUpdateDTO studentCreateDTO);
    StudentResponseDTO toDTO(Student student);
    StudentCreateDTO toCreateDTO(Student student);
    List<StudentResponseDTO> toDTO(List<Student> students);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudentFromDto(StudentCreateDTO dto, @MappingTarget Student entity);
    void updateStudentFromDto(StudentUpdateDTO dto, @MappingTarget Student entity);

}
