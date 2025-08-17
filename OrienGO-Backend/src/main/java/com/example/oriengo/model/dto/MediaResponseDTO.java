package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.MediaType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponseDTO {
    private Long id;
    private String url;
    private MediaType type;
    private String contentType;
    private String name;
    private Long size;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}
