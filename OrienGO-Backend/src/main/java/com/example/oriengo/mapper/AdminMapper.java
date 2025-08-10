package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.AdminCreateDTO;
import com.example.oriengo.model.dto.AdminResponseDTO;
import com.example.oriengo.model.dto.AdminUpdateDTO;
import com.example.oriengo.model.entity.Admin;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    Admin toEntity(AdminCreateDTO adminCreateDTO);
    Admin toEntity(AdminUpdateDTO adminUpdateDTO);
    List<Admin> toEntity(List<AdminCreateDTO> adminCreateDTOs);
    AdminResponseDTO toDTO(Admin admin);
    List<AdminResponseDTO> toDTO(List<Admin> admins);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdminFromDto(AdminUpdateDTO dto, @MappingTarget Admin admin);
}
