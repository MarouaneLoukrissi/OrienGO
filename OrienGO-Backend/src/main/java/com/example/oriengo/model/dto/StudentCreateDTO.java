package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.EducationLevel;
import com.example.oriengo.model.enumeration.GenderType;
//import com.example.oriengo.validations.PasswordMatches;
import com.example.oriengo.validations.ValidPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@PasswordMatches
public class StudentCreateDTO {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotNull(message = "Age is required")
    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 120, message = "Age must be at most 120")
    private Integer age;

    @Size(max = 10, message = "Gender must be at most 10 characters")
    private GenderType gender;

    @Size(max = 20, message = "Phone number must be at most 20 characters")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,19}$", message = "Phone number must contain 8 to 20 digits and start with + or non-zero")
    @ValidPhoneNumber(message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 365, message = "Email must be at most 365 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;

//    @NotBlank(message = "Confirm password is required")
//    private String confirmPassword;

    @Size(max = 100)
    private String school;

    @Size(max = 100)
    private String fieldOfStudy;

    @NotNull
    private EducationLevel educationLevel;

    @Valid
    private LocationDTO location;

}
