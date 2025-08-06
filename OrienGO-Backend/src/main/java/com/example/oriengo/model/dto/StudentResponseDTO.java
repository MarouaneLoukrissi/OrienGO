package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.AccountPrivacy;
import com.example.oriengo.model.enumeration.EducationLevel;
import com.example.oriengo.model.enumeration.VisibilityStatus;
import com.example.oriengo.model.enumeration.MessagePermission;
import com.example.oriengo.model.entity.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String age;
    private String gender;
    private String school;
    private String fieldOfStudy;
    private EducationLevel educationLevel;
    private Location location;
    private VisibilityStatus profileVisibility;
    private AccountPrivacy accountPrivacy;
    private MessagePermission messagePermission;
}
