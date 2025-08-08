package com.example.oriengo.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private String code;       // e.g., "SUCCESS" or "USER_NOT_FOUND"
    private int status;        // HTTP Status (e.g., 200, 404)
    private String message;    // Human-readable message
    private T data;            // Actual payload (AdminResponseDTO, list, etc.)

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors; // Changed to Map for validation errors
}