package com.example.oriengo.model.entity;

public enum EducationLevel {
    MIDDLE_SCHOOL,
    HIGH_SCHOOL,
    POST_SECONDARY,
    UNIVERSITY,
    GRADUATE,
    OTHER;

    public String getLabel() {
        return switch (this) {
            case MIDDLE_SCHOOL -> "Middle School";
            case HIGH_SCHOOL -> "High School";
            case POST_SECONDARY -> "Post-Secondary (e.g., BTS/DUT/Preparatory)";
            case UNIVERSITY -> "University";
            case GRADUATE -> "Graduate (Master's, Doctorate)";
            case OTHER -> "Other";
        };
    }
}
