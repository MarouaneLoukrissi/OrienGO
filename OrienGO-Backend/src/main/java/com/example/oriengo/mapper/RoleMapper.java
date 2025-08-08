package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.RoleCreateDTO;
import com.example.oriengo.model.dto.RoleResponseDTO;
import com.example.oriengo.model.entity.Privilege;
import com.example.oriengo.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleCreateDTO roleCreateDTO);
    @Mapping(source = "privileges", target = "privilegeIds", qualifiedByName = "privilegesToIds")
    RoleResponseDTO toDTO(Role role);
    @Mapping(source = "privileges", target = "privilegeIds", qualifiedByName = "privilegesToIds")
    List<RoleResponseDTO> toDTO(List<Role> roles);

    @Named("privilegesToIds")
    default Set<Long> privilegesToIds(Set<Privilege> privileges) {
        if (privileges == null) {
            return null;
        }
        return privileges.stream()
                .map(Privilege::getId)
                .collect(Collectors.toSet());
    }
}
