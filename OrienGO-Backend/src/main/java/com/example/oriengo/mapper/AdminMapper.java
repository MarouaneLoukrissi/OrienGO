package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.AdminCreateDTO;
import com.example.oriengo.model.entity.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    Admin toEntity(AdminCreateDTO adminCreateDTO);
}
