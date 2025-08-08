package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.MediaType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaCreateDTO {

    @NotBlank(message = "{media.url.notBlank}")
    @Size(max = 500, message = "{media.url.size}")
    private String url;

    @NotNull(message = "{media.type.notNull}")
    private MediaType type;

    @NotBlank(message = "{media.contentType.notBlank}")
    @Size(max = 100, message = "{media.contentType.size}")
    private String contentType;

    @NotBlank(message = "{media.name.notBlank}")
    @Size(max = 255, message = "{media.name.size}")
    private String name;

    @NotNull(message = "{media.size.notNull}")
    @Positive(message = "{media.size.positive}")
    private Long size;

    @NotNull(message = "{media.userId.notNull}")
    private Long userId;
}
