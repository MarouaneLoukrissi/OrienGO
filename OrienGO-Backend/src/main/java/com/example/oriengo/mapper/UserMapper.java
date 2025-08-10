package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.UserCreateDTO;
import com.example.oriengo.model.dto.UserResponseDTO;
import com.example.oriengo.model.entity.Role;
import com.example.oriengo.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateDTO userCreateDTO);
    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserResponseDTO toDTO(User user);
    List<UserResponseDTO> toDTO(List<User> user);

    // Custom mapping method for Set<Role> → Set<String>
    default Set<String> mapRoles(Set<Role> roles) {
        return roles == null ? null : roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
