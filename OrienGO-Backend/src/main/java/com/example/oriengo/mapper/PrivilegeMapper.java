package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.PrivilegeCreateDTO;
import com.example.oriengo.model.dto.PrivilegeResponseDTO;
import com.example.oriengo.model.entity.Privilege;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {
    Privilege toEntity(PrivilegeCreateDTO privilegeCreateDTO);
    PrivilegeResponseDTO toDTO(Privilege privilege);
    List<PrivilegeResponseDTO> toDTO(List<Privilege> privilege);
}
