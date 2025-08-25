package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.MediaType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFilteredResponseDTO {
    private Long id;
    private MediaType type;
    private Long userId;
}
