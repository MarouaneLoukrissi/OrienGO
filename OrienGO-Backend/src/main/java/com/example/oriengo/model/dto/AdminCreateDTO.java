package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.AdminLevel;
import com.example.oriengo.model.enumeration.Department;
import com.example.oriengo.model.enumeration.GenderType;
import com.example.oriengo.validations.ValidPhoneNumber;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateDTO {

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

    //    @NotBlank(message = "Confirm password is required")
    //    private String confirmPassword;

    @NotNull(message = "{admin.adminLevel.required}")
    private AdminLevel adminLevel;

    @NotNull(message = "{admin.department.required}")
    private Department department;
}
