package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.CoachSpecialization;
import com.example.oriengo.model.enumeration.GenderType;
import com.example.oriengo.validations.ValidPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachCreateDTO {
    @NotBlank(message = "{coach.firstname.notblank}")
    @Size(min = 2, max = 50, message = "{coach.firstname.size}")
    private String firstName;

    @NotBlank(message = "{coach.lastname.notblank}")
    @Size(min = 2, max = 50, message = "{coach.lastname.size}")
    private String lastName;

    @NotNull(message = "{coach.age.notnull}")
    @Min(value = 10, message = "{coach.age.min}")
    @Max(value = 120, message = "{coach.age.max}")
    private Integer age;

    @NotNull(message = "{gender.notNull}")
    private GenderType gender;

    @Size(max = 20, message = "{coach.phoneNumber.size}")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,19}$", message = "{coach.phoneNumber.pattern}")
    @ValidPhoneNumber(message = "{coach.phoneNumber.invalid}")
    private String phoneNumber;

    @NotBlank(message = "{coach.email.notblank}")
    @Email(message = "{coach.email.email}")
    @Size(max = 365, message = "{coach.email.size}")
    private String email;

    @NotBlank(message = "{coach.password.notblank}")
    @Size(min = 8, max = 255, message = "{coach.password.size}")
    private String password;

    @Valid
    private LocationDTO location;
//    @NotBlank(message = "Confirm password is required")
//    private String confirmPassword;


//    @NotNull(message = "{coach.specialization.notnull}")
//    private CoachSpecialization specialization;
//    @Size(max = 500, message = "{coach.expertise.size}")
//    private String expertise; // e.g., "Career Development, Personal Growth, Goal Setting"
//
//    @Size(max = 200, message = "{coach.services.size}")
//    private String services; // e.g., "Available for 1-on-1 sessions"
//
//    @Size(max = 500, message = "{coach.availability.size}")
//    private String availability; // e.g., "Monday - Friday: 9 AM - 6 PM; Weekend: By appointment"

}
