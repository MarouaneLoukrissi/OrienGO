package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.TokenCreateDTO;
import com.example.oriengo.model.dto.TokenResponseDTO;
import com.example.oriengo.model.entity.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    Token toEntity(TokenCreateDTO dto);
    TokenResponseDTO toDTO(Token token);
    List<TokenResponseDTO> toDTO(List<Token> token);
}
