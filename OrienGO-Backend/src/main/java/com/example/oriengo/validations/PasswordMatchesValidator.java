//package com.example.oriengo.validations;
//
//import com.example.oriengo.model.dto.UserCreateDTO;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserCreateDTO> {
//
//    @Override
//    public boolean isValid(UserCreateDTO dto, ConstraintValidatorContext context) {
//        if (dto.getPassword() == null || dto.getConfirmPassword() == null) {
//            return false;
//        }
//        return dto.getPassword().equals(dto.getConfirmPassword());
//    }
//}
