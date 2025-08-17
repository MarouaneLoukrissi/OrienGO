package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.GenderType;
//import com.example.oriengo.validations.PasswordMatches;
import com.example.oriengo.validations.ValidPhoneNumber;
import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@PasswordMatches
public class UserCreateDTO {

    @NotBlank(message = "{user.firstname.required}")
    @Size(min = 2, max = 50, message = "{user.firstname.size}")
    private String firstName;

    @NotBlank(message = "{user.lastname.required}")
    @Size(min = 2, max = 50, message = "{user.lastname.size}")
    private String lastName;

    @NotNull(message = "{user.age.required}")
    @Min(value = 10, message = "{user.age.min}")
    @Max(value = 120, message = "{user.age.max}")
    private Integer age;

    @Size(max = 10, message = "{user.gender.size}")
    private GenderType gender;

    @Size(max = 20, message = "{user.phone.size}")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,19}$", message = "{user.phone.pattern}")
    @ValidPhoneNumber(message = "{user.phone.invalid}")
    private String phoneNumber;

    @NotBlank(message = "{user.email.required}")
    @Email(message = "{user.email.invalid}")
    @Size(max = 365, message = "{user.email.size}")
    private String email;

    @NotBlank(message = "{user.password.required}")
    @Size(min = 8, max = 255, message = "{user.password.size}")
    private String password;

//    @NotBlank(message = "Confirm password is required")
//    private String confirmPassword;

}

