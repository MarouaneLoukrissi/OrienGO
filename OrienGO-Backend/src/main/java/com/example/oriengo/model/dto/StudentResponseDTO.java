package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.AccountPrivacy;
import com.example.oriengo.model.enumeration.EducationLevel;
import com.example.oriengo.model.enumeration.GenderType;
import com.example.oriengo.model.enumeration.MessagePermission;
import com.example.oriengo.model.enumeration.VisibilityStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Integer age;

    private GenderType gender;

    private String phoneNumber;

    private String email;

    private String school;

    private String fieldOfStudy;

    private EducationLevel educationLevel;

    private LocationDTO location;

    private VisibilityStatus profileVisibility;

    private MessagePermission messagePermission;

    private AccountPrivacy accountPrivacy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
