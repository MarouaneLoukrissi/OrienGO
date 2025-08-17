package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.TokenType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {

    private Long id;

    private String tokenValue;

    private TokenType tokenType;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private LocalDateTime revokedAt;

    private boolean expired;

    private boolean revoked;

    private Long userId; // To keep DTO flat and avoid nested User
}
