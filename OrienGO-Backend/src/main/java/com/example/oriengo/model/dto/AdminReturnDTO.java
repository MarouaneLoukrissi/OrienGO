package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.AdminLevel;
import com.example.oriengo.model.enumeration.Department;
import com.example.oriengo.model.enumeration.GenderType;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReturnDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Integer age;

    private GenderType gender;

    private String phoneNumber;

    private String email;

    private Boolean enabled;

    private Boolean tokenExpired;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastSeen;

    private Boolean suspended;

    private String suspensionReason;

    private LocalDateTime suspendedUntil;

    private LocalDateTime lastLoginAt;

    private LocalDateTime deletedAt;

    //private Set<String> roles;

    private AdminLevel adminLevel;

    private Long createdById;

    private Department department;

    private Boolean isDeleted;
}
