package com.example.oriengo.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

    private String[] allowedTypes;

    @Override
    public void initialize(FileContentType constraintAnnotation) {
        this.allowedTypes = constraintAnnotation.allowed();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Let @NotNull handle empty file
        }

        String contentType = file.getContentType();
        return Arrays.asList(allowedTypes).contains(contentType);
    }
}
