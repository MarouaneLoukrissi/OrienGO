package com.example.oriengo.model.mapper;

import com.example.oriengo.model.dto.StudentJobLinkRequestDto;
import com.example.oriengo.model.dto.StudentJobLinkResponseDto;
import com.example.oriengo.model.entity.StudentJobLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudentJobLinkMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    StudentJobLink toEntity(StudentJobLinkRequestDto dto);
    
    @Mapping(target = "studentId", expression = "java(entity.getStudent() != null ? entity.getStudent().getId() : null)")
    @Mapping(target = "jobId", expression = "java(entity.getJob() != null ? entity.getJob().getId() : null)")
    StudentJobLinkResponseDto toResponseDto(StudentJobLink entity);
    
    List<StudentJobLinkResponseDto> toResponseDtoList(List<StudentJobLink> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(StudentJobLinkRequestDto dto, @MappingTarget StudentJobLink entity);
} 