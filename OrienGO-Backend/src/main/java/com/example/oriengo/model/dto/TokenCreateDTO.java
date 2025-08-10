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

    @NotBlank(message = "{token.tokenValue.notBlank}")
    @Size(max = 512, message = "{token.tokenValue.size}")
    private String tokenValue;

    @NotNull(message = "{token.tokenType.notNull}")
    private TokenType tokenType;

    @NotNull(message = "{token.expiredAt.notNull}")
    @Future(message = "{token.expiredAt.future}")
    private LocalDateTime expiredAt;

    @NotNull(message = "{token.userId.notNull}")
    private Long userId;

}

