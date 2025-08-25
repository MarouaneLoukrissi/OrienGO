package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private LocationDTO location;
    private GenderType gender;
    private String phoneNumber;
    private String email;
    private CoachSpecialization specialization;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double rate;
    private VisibilityStatus profileVisibility;
    private MessagePermission messagePermission;
    private AccountPrivacy accountPrivacy;
    private String expertise;
    private String services;
    private String availability;
    private LocalDateTime lastSeen;
}
