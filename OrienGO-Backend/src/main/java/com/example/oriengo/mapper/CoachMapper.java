package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.CoachCreateDTO;
import com.example.oriengo.model.dto.CoachUpdateProfileDTO;
import com.example.oriengo.model.dto.CoachResponseDTO;
import com.example.oriengo.model.dto.StudentUpdateDTO;
import com.example.oriengo.model.entity.Coach;
import com.example.oriengo.model.entity.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoachMapper {
    Coach toEntity(CoachCreateDTO coachCreateDTO);
    @Mapping(target = "specialization", source = "specialization")
    Coach toEntity(CoachUpdateProfileDTO coachUpdateProfileDTO);
    CoachResponseDTO toDTO(Coach coach);
    List<CoachResponseDTO> toDTO(List<Coach> coaches);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCoachFromDto(CoachUpdateProfileDTO dto, @MappingTarget Coach entity);
}
