package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentReturnDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Integer age;

    private GenderType gender;

    private String phoneNumber;

    private String email;

    private String school;

    private String fieldOfStudy;

    private EducationLevel educationLevel;

    private LocationDTO location;

    // Account status fields
    private boolean enabled;

    private Boolean tokenExpired;

    private LocalDateTime lastSeen;

    private LocalDateTime lastLoginAt;

    private LocalDateTime deletedAt;

    private Boolean isDeleted;

    private boolean suspended;

    private String suspensionReason;

    private LocalDateTime suspendedUntil;

    private VisibilityStatus profileVisibility;

    private MessagePermission messagePermission;

    private AccountPrivacy accountPrivacy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
