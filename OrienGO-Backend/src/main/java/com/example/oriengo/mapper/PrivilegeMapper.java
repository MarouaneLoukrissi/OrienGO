package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.PrivilegeCreateDTO;
import com.example.oriengo.model.entity.Privilege;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {
    Privilege toEntity(PrivilegeCreateDTO roleCreateDTO);
}
