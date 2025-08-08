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

    @NotBlank(message = "{role.name.notblank}")
    @Size(min = 2, max = 50, message = "{role.name.size}")
    private String name;
    private Set<Long> privilegeIds;
}
