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

    @NotBlank(message = "{privilege.name.required}")
    @Size(max = 100, message = "{privilege.name.size}")
    private String name;
}