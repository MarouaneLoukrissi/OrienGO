package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.MediaType;
import com.example.oriengo.validations.FileContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaRequestDTO {

    @NotNull(message = "{media.file.required}")
    @FileContentType(
            allowed = {"image/png", "image/jpeg", "application/pdf"},
            message = "{media.file.invalidType}"
    )
    private MultipartFile media;

    @NotBlank(message = "{media.userId.required}")
    @NotEmpty(message = "{media.userId.required}")
    private Long userId;

    @NotNull(message = "{media.type.notNull}")
    private MediaType type;
}
