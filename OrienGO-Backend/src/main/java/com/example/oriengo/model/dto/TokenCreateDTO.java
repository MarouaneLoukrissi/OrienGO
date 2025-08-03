package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.TokenType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenCreateDTO {

    @NotBlank(message = "Token value is required")
    @Size(max = 512, message = "Token value must be at most 512 characters")
    private String tokenValue;

    @NotNull(message = "Token type is required")
    private TokenType tokenType;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDateTime expiredAt;

    @NotNull(message = "User ID is required")
    private Long userId;
}

