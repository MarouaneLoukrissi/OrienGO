package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.CoachCreateDTO;
import com.example.oriengo.model.entity.Coach;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoachMapper {
    Coach toEntity(CoachCreateDTO coachCreateDTO);
}
