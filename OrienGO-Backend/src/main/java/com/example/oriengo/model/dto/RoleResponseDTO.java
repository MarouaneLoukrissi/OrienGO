package com.example.oriengo.model.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {
    private Long id;
    private String name;
    private Set<Long> privilegeIds;  // Just IDs for simplicity, or create a PrivilegeResponseDTO if needed
}
