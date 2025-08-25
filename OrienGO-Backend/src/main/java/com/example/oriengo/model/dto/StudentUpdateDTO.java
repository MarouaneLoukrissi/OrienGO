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
public class StudentUpdateDTO {
    @NotBlank(message = "{firstName.notBlank}")
    @Size(min = 2, max = 50, message = "{firstName.size}")
    private String firstName;

    @NotBlank(message = "{lastName.notBlank}")
    @Size(min = 2, max = 50, message = "{lastName.size}")
    private String lastName;

    @NotNull(message = "{age.notNull}")
    @Min(value = 10, message = "{age.min}")
    @Max(value = 120, message = "{age.max}")
    private Integer age;

    @NotNull(message = "{gender.notNull}")
    private GenderType gender;

    @Size(max = 20, message = "{phoneNumber.size}")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,19}$", message = "{phoneNumber.pattern}")
    @ValidPhoneNumber(message = "{phoneNumber.validPhoneNumber}")
    private String phoneNumber;

//    @NotBlank(message = "Confirm password is required")
//    private String confirmPassword;

    @Size(max = 100, message = "{school.size}")
    private String school;

    @Size(max = 100, message = "{fieldOfStudy.size}")
    private String fieldOfStudy;

    @NotNull(message = "{educationLevel.notNull}")
    private EducationLevel educationLevel;

    @Valid
    private LocationDTO location;
}
