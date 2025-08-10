package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.StudentCreateDTO;
import com.example.oriengo.model.dto.StudentResponseDTO;
import com.example.oriengo.model.entity.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toEntity(StudentCreateDTO studentCreateDTO);
    StudentResponseDTO toDTO(Student student);
    StudentCreateDTO toCreateDTO(Student student);
    List<StudentResponseDTO> toDTO(List<Student> students);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudentFromDto(StudentCreateDTO dto, @MappingTarget Student entity);
}
