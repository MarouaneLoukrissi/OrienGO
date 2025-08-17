//package com.example.oriengo.validations;
//
//import com.google.i18n.phonenumbers.PhoneNumberUtil;
//import com.google.i18n.phonenumbers.Phonenumber;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//
//public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
//
//    @Override
//    public void initialize(ValidPhoneNumber constraintAnnotation) {
//    }
//
//    @Override
//    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
//        if (phoneNumber == null || phoneNumber.isEmpty()) {
//            return false;
//        }
//        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//        try {
//            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, null); // No region code
//            return phoneNumberUtil.isValidNumber(number);
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}

package com.example.oriengo.validations;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            // Parse with a default region (optional)
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "MA");
            return phoneNumberUtil.isValidNumber(number);
        } catch (Exception e) {
            return false;
        }
    }
}

