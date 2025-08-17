package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.CoachCreateDTO;
import com.example.oriengo.model.dto.CoachResponseDTO;
import com.example.oriengo.model.entity.Coach;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoachMapper {
    Coach toEntity(CoachCreateDTO coachCreateDTO);
    CoachResponseDTO toDTO(Coach coach);
    List<CoachResponseDTO> toDTO(List<Coach> coaches);
}
