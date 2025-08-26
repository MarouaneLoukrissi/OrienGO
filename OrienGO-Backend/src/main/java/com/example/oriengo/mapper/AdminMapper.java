package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.*;
import com.example.oriengo.model.entity.Admin;
import com.example.oriengo.model.entity.Role;
import com.example.oriengo.repository.AdminRepository;
import com.example.oriengo.repository.RoleRepository;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminRepository adminRepository = null;
    RoleRepository roleRepository = null;

    Admin toEntity(AdminCreateDTO adminCreateDTO);


//    @Mapping(target = "createdBy", source = "createdById", qualifiedByName = "idToAdmin")
//    @Mapping(target = "roles", source = "roles", qualifiedByName = "namesToRoles")
@Mapping(target = "roles", ignore = true)        // handled manually in service
@Mapping(target = "createdBy", ignore = true)   // handled manually in service
    Admin toEntity(AdminDTO adminDTO);
    Admin toEntity(AdminUpdateDTO adminUpdateDTO);
    List<Admin> toEntity(List<AdminCreateDTO> adminCreateDTOs);
    AdminResponseDTO toDTO(Admin admin);
    List<AdminResponseDTO> toDTO(List<Admin> admins);

    //@Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToNames")
    @Mapping(target = "createdById", source = "createdBy.id")
    AdminReturnDTO toReturnDTO(Admin admin);

    List<AdminReturnDTO> toReturnDTO(List<Admin> admins);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdminFromDto(AdminUpdateDTO dto, @MappingTarget Admin admin);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "roles", source = "roles", qualifiedByName = "namesToRoles")
//    @Mapping(target = "createdBy", source = "createdById", qualifiedByName = "idToAdmin")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", ignore = true)        // handled in service
    @Mapping(target = "createdBy", ignore = true)   // handled in service
    void updateAdminFromDto(AdminModifyDTO dto, @MappingTarget Admin admin);

    @Named("rolesToNames")
    default Set<String> mapRolesToNames(Set<Role> roles) {
        return roles == null ? Set.of() : roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
    @Named("namesToRoles")
    default Set<Role> mapNamesToRoles(Set<String> roleNames) {
        return roleNames == null ? Set.of() : roleNames.stream()
                .map(name -> Role.builder().name(name).build())
                .collect(Collectors.toSet());
    }
    @Named("idToAdmin")
    default Admin mapIdToAdmin(Long id) {
        if (id == null) return null;
        Admin admin = new Admin();
        admin.setId(id);
        return admin;
    }

    @Named("idToAdmin")
    default Admin mapAdmin(Long adminId) {
        if (adminId == null) return null;
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + adminId));
    }
    @Named("namesToRoles")
    default Set<Role> mapRoles(Set<String> roleNames) {
        if (roleNames == null) return Collections.emptySet();
        return roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                .collect(Collectors.toSet());
    }
}
