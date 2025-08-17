package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.StudentPersonalizedJobLinkRequestDto;
import com.example.oriengo.model.dto.StudentPersonalizedJobLinkResponseDto;
import com.example.oriengo.model.entity.StudentPersonalizedJobLink;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentPersonalizedJobLinkMapper {
    StudentPersonalizedJobLink toEntity(StudentPersonalizedJobLinkRequestDto dto);
    StudentPersonalizedJobLink toEntity(StudentPersonalizedJobLinkResponseDto dto);
    StudentPersonalizedJobLinkResponseDto toDto(StudentPersonalizedJobLink dto);
}
