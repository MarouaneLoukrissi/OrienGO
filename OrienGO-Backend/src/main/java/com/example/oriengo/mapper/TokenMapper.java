package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.TokenCreateDTO;
import com.example.oriengo.model.entity.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    Token toEntity(TokenCreateDTO dto);
}
