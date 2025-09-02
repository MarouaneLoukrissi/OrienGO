package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.*;
import com.example.oriengo.validations.ValidPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachModifyDTO {
    // ==============================
    // User fields
    // ==============================
    @NotBlank(message = "{firstName.notBlank}")
    @Size(min = 2, max = 50, message = "{firstName.size}")
    private String firstName;

    @NotBlank(message = "{lastName.notBlank}")
    @Size(min = 2, max = 50, message = "{lastName.size}")
    private String lastName;

    @NotNull(message = "{age.notNull}")
    @Min(value = 18, message = "{age.min}")
    @Max(value = 120, message = "{age.max}")
    private Integer age;

    @NotNull(message = "{gender.notNull}")
    private GenderType gender;

    @Size(max = 20, message = "{phoneNumber.size}")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,19}$", message = "{phoneNumber.pattern}")
    @ValidPhoneNumber(message = "{phoneNumber.validPhoneNumber}")
    private String phoneNumber;

    @Email(message = "{email.valid}")
    @NotBlank(message = "{email.notBlank}")
    private String email;

    @NotBlank(message = "{password.notBlank}")
    @Size(min = 8, max = 255, message = "{password.size}")
    private String password;

    private boolean enabled;

    private boolean suspended;

    @Size(max = 255, message = "{suspensionReason.size}")
    private String suspensionReason;

    private LocalDateTime suspendedUntil;

    // ==============================
    // Coach-specific fields
    // ==============================
    @NotNull(message = "{profileVisibility.notNull}")
    private VisibilityStatus profileVisibility;

    @Valid
    private LocationDTO location;

    @NotNull(message = "{messagePermission.notNull}")
    private MessagePermission messagePermission;

    @NotNull(message = "{accountPrivacy.notNull}")
    private AccountPrivacy accountPrivacy;

    @NotNull(message = "{specialization.notNull}")
    private CoachSpecialization specialization;

    @DecimalMin(value = "0.0", inclusive = true, message = "{rate.min}")
    private Double rate;

    @Size(max = 500, message = "{expertise.size}")
    private String expertise;

    @Size(max = 500, message = "{services.size}")
    private String services;

    @Size(max = 255, message = "{availability.size}")
    private String availability;
}
