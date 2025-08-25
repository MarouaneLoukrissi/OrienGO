package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.MediaCreateDTO;
import com.example.oriengo.model.dto.MediaFilteredResponseDTO;
import com.example.oriengo.model.dto.MediaResponseDTO;
import com.example.oriengo.model.entity.Media;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    @Mapping(source = "userId", target = "user.id")
    Media toEntity(MediaCreateDTO dto);

    @Mapping(source = "user.id", target = "userId")
    MediaResponseDTO toDTO(Media media);

    List<MediaResponseDTO> toDTO(List<Media> medias);

    @Mapping(source = "user.id", target = "userId")
    MediaFilteredResponseDTO toFilteredDTO(Media media);

    List<MediaFilteredResponseDTO> toFilteredDTO(List<Media> medias);
}
