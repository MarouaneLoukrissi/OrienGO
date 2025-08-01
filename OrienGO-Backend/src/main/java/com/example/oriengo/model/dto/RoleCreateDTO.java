package com.example.oriengo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCreateDTO {

    @NotBlank(message = "Role name must not be blank")
    @Size(min = 2, max = 50, message = "Role name must be between 2 and 50 characters")
    private String name;
    private Set<Long> privilegeIds;
}
