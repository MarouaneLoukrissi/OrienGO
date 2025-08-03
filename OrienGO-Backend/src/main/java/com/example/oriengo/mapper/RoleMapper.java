package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.RoleCreateDTO;
import com.example.oriengo.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleCreateDTO roleCreateDTO);
}
