package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.UserCreateDTO;
import com.example.oriengo.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateDTO userCreateDTO);
}
