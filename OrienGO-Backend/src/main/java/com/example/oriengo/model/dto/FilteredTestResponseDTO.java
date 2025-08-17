package com.example.oriengo.model.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilteredTestResponseDTO {
    private LocalDateTime startedAt;
}
