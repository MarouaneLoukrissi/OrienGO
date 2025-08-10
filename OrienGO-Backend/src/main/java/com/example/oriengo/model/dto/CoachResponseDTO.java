package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.AccountPrivacy;
import com.example.oriengo.model.enumeration.GenderType;
import com.example.oriengo.model.enumeration.MessagePermission;
import com.example.oriengo.model.enumeration.VisibilityStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachResponseDTO {
    private Long id;

    private String firstName;
    private String lastName;
    private Integer age;
    private GenderType gender;
    private String phoneNumber;
    private String email;

    private VisibilityStatus profileVisibility;
    private MessagePermission messagePermission;
    private AccountPrivacy accountPrivacy;
}
