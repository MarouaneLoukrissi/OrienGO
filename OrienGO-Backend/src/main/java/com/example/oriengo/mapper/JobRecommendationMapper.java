package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.JobRecommendationRequestDto;
import com.example.oriengo.model.dto.JobRecommendationResponseDto;
import com.example.oriengo.model.entity.JobRecommendation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JobRecommendationMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "testResult", ignore = true)
    @Mapping(target = "job", ignore = true)
    JobRecommendation toEntity(JobRecommendationRequestDto dto);
    
    @Mapping(target = "testResultId", expression = "java(entity.getTestResult() != null ? entity.getTestResult().getId() : null)")
    @Mapping(target = "jobId", expression = "java(entity.getJob() != null ? entity.getJob().getId() : null)")
    JobRecommendationResponseDto toResponseDto(JobRecommendation entity);
    
    List<JobRecommendationResponseDto> toResponseDtoList(List<JobRecommendation> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "testResult", ignore = true)
    @Mapping(target = "job", ignore = true)
    void updateEntityFromDto(JobRecommendationRequestDto dto, @MappingTarget JobRecommendation entity);
} 