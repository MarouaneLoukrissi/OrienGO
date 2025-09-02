package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.*;
import com.example.oriengo.model.entity.Coach;
import com.example.oriengo.model.entity.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoachMapper {

    Coach toEntity(CoachCreateDTO coachCreateDTO);
    @Mapping(target = "specialization", source = "specialization")
    Coach toEntity(CoachUpdateProfileDTO coachUpdateProfileDTO);
    Coach toEntity(CoachDTO dto);
    Coach toEntity(CoachModifyDTO coachModifyDTO);
    CoachResponseDTO toDTO(Coach coach);

    List<CoachResponseDTO> toDTO(List<Coach> coaches);
    List<CoachReturnDTO> toAdminDTO(List<Coach> coaches);
    CoachReturnDTO toAdminDTO(Coach coaches);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCoachFromDto(CoachUpdateProfileDTO dto, @MappingTarget Coach entity);
}
