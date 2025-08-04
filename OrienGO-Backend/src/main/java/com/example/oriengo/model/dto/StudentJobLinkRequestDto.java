package com.example.oriengo.model.dto;

import com.example.oriengo.model.enumeration.LinkType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentJobLinkRequestDto {
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Job ID is required")
    private Long jobId;
    
    @NotNull(message = "Link type is required")
    private LinkType type;
} 