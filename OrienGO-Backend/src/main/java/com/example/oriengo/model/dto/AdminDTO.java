package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.AdminLevel;
import com.example.oriengo.model.enumeration.Department;
import com.example.oriengo.model.enumeration.GenderType;
import com.example.oriengo.validations.ValidPhoneNumber;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {

    @NotBlank(message = "{admin.firstName.required}")
    @Size(min = 2, max = 50, message = "{admin.firstName.size}")
    private String firstName;

    @NotBlank(message = "{admin.lastName.required}")
    @Size(min = 2, max = 50, message = "{admin.lastName.size}")
    private String lastName;

    @NotNull(message = "{admin.age.required}")
    @Min(value = 10, message = "{admin.age.min}")
    @Max(value = 120, message = "{admin.age.max}")
    private Integer age;

    @NotNull(message = "{admin.gender.required}")
    private GenderType gender;

    @Size(max = 20, message = "{admin.phoneNumber.size}")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,19}$", message = "{admin.phoneNumber.pattern}")
    @ValidPhoneNumber(message = "{admin.phoneNumber.invalid}")
    private String phoneNumber;

    @NotBlank(message = "{admin.email.required}")
    @Email(message = "{admin.email.invalid}")
    @Size(max = 365, message = "{admin.email.size}")
    private String email;

    @NotBlank(message = "{admin.password.required}")
    @Size(min = 8, max = 255, message = "{admin.password.size}")
    private String password;

    @NotNull(message = "{admin.enabled.required}")
    private Boolean enabled;

    @NotNull(message = "{admin.suspended.required}")
    private Boolean suspended;

    private String suspensionReason;

    private LocalDateTime suspendedUntil;

    @NotNull(message = "{admin.adminLevel.required}")
    private AdminLevel adminLevel;

    @NotNull(message = "{admin.createdBy.required}")
    private Long createdById;

    @NotNull(message = "{admin.department.required}")
    private Department department;
//    @NotBlank(message = "Confirm password is required")
//    private String confirmPassword;

//    @NotNull(message = "{admin.tokenExpired.required}")
//    private Boolean tokenExpired;

//    private LocalDateTime createdAt;

//    private LocalDateTime updatedAt;

//    private LocalDateTime lastSeen;

//    private LocalDateTime lastLoginAt;

//    private LocalDateTime deletedAt;

//    @Builder.Default
//    @NotNull(message = "{admin.roles.required}")
//    private Set<String> roles = new HashSet<>();

//    @NotNull(message = "{admin.isDeleted.required}")
//    private Boolean isDeleted;

}
