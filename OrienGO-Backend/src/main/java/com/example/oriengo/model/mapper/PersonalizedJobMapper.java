package com.example.oriengo.model.mapper;

import com.example.oriengo.model.dto.PersonalizedJobRequestDto;
import com.example.oriengo.model.dto.PersonalizedJobResponseDto;
import com.example.oriengo.model.entity.PersonalizedJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonalizedJobMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobRecommendation", ignore = true)
    PersonalizedJob toEntity(PersonalizedJobRequestDto dto);
    
    @Mapping(target = "jobRecommendationId", expression = "java(entity.getJobRecommendation() != null ? entity.getJobRecommendation().getId() : null)")
    PersonalizedJobResponseDto toResponseDto(PersonalizedJob entity);
    
    List<PersonalizedJobResponseDto> toResponseDtoList(List<PersonalizedJob> entities);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobRecommendation", ignore = true)
    void updateEntityFromDto(PersonalizedJobRequestDto dto, @MappingTarget PersonalizedJob entity);
} 