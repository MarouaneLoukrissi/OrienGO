package com.example.oriengo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeCreateDTO {

    @NotBlank(message = "Privilege name is required")
    @Size(max = 100, message = "Privilege name must be at most 100 characters")
    private String name;
}