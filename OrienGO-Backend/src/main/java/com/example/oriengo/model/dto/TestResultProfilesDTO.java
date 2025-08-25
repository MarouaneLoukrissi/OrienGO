package com.example.oriengo.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestResultProfilesDTO {
    private List<ProfileScoreDTO> profiles; // sorted list
}
