package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.GenderType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Integer age;

    private GenderType gender;

    private String phoneNumber;

    private String email;

    private boolean enabled;

    private boolean tokenExpired;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastSeen;

    private boolean suspended;

    private String suspensionReason;

    private LocalDateTime suspendedUntil;

    private LocalDateTime lastLoginAt;

    private Set<String> roles;  // Just role names for simplicity

    private LocalDateTime deletedAt;

    private boolean isDeleted;

    private boolean online;

}
