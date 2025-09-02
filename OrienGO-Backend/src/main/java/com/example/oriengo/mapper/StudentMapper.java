package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.*;
import com.example.oriengo.model.entity.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "educationLevel", source = "educationLevel")
    Student toEntity(StudentCreateDTO studentCreateDTO);
    Student toEntity(StudentDTO studentDTO);
    Student toEntity(StudentUpdateDTO studentCreateDTO);
    StudentResponseDTO toDTO(Student student);
    StudentReturnDTO toAdminDTO(Student student);
    List<StudentReturnDTO> toAdminDTO(List<Student> students);
    List<StudentResponseDTO> toDTO(List<Student> students);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudentFromDto(StudentCreateDTO dto, @MappingTarget Student entity);
    void updateStudentFromDto(StudentUpdateDTO dto, @MappingTarget Student entity);
    void updateStudentFromDto(StudentModifyDTO dto, @MappingTarget Student entity);
}
