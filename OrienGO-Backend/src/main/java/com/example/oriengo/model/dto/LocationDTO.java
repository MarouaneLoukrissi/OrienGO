package com.example.oriengo.model.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String region;

    @Size(max = 100)
    private String country;

    @Size(max = 255)
    private String address;
}
