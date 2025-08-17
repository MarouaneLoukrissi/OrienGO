package com.example.oriengo.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileContentTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileContentType {

    String message() default "{media.file.invalidType}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] allowed(); // list of accepted MIME types
}
