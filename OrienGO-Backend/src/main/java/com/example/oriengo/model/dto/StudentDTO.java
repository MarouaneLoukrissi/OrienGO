package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.AccountPrivacy;
import com.example.oriengo.model.enumeration.EducationLevel;
import com.example.oriengo.model.enumeration.VisibilityStatus;
import com.example.oriengo.model.enumeration.MessagePermission;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.example.oriengo.model.entity.Location;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    private String phoneNumber;
    private String age;
    private String gender;

    private String school;
    private String fieldOfStudy;
    @NotNull
    private EducationLevel educationLevel;
    private Location location;
    @NotNull
    private VisibilityStatus profileVisibility;
    @NotNull
    private AccountPrivacy accountPrivacy;
    @NotNull
    private MessagePermission messagePermission; // si pertinent


}
