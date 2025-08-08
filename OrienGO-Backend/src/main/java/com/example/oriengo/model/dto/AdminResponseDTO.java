package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.AdminLevel;
import com.example.oriengo.model.enumeration.Department;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;  // We'll map GenderType enum to String
    private String phoneNumber;
    private String email;
    private boolean enabled;
    private AdminLevel adminLevel;
    private Department department;
    private Long createdById;  // just ID of creator to avoid loading full object
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastSeen;
}
