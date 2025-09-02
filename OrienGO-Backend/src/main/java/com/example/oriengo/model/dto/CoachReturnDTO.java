package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachReturnDTO {

    // ==============================
    // User (parent class) fields
    // ==============================
    private Long id;

    private String firstName;

    private String lastName;

    private Integer age;

    private GenderType gender;

    private String phoneNumber;

    private String email;

    private boolean enabled;

    private boolean suspended;

    private String suspensionReason;

    private LocalDateTime suspendedUntil;

    private Boolean tokenExpired;

    // ==============================
    // Coach-specific fields
    // ==============================
    private VisibilityStatus profileVisibility;

    private LocationDTO location;

    private MessagePermission messagePermission;

    private AccountPrivacy accountPrivacy;

    private CoachSpecialization specialization;

    private Double rate;

    private String expertise;

    private String services;

    private String availability;

    // ==============================
    // Audit fields
    // ==============================
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
