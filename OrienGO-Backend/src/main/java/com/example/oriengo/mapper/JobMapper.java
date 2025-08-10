package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.JobRequestDto;
import com.example.oriengo.model.dto.JobResponseDto;
import com.example.oriengo.model.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JobMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobRecommendations", ignore = true)
    @Mapping(target = "trainings", ignore = true)
    @Mapping(target = "studentLinks", ignore = true)
    @Mapping(target = "version", ignore = true)
    Job toEntity(JobRequestDto dto);
    
    @Mapping(target = "tags", ignore = true)
    JobResponseDto toResponseDto(Job entity);
    
    List<JobResponseDto> toResponseDtoList(List<Job> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobRecommendations", ignore = true)
    @Mapping(target = "trainings", ignore = true)
    @Mapping(target = "studentLinks", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(JobRequestDto dto, @MappingTarget Job entity);
} 