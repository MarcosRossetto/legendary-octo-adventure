package org.acme.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.acme.config.interfaces.ValidPassword;
import org.acme.utils.PasswordValidationUtil;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        StringBuilder message = new StringBuilder();
        boolean valid = PasswordValidationUtil.isValidPassword(password, message);
        
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message.toString())
                   .addConstraintViolation();
        }
        
        return valid;
    }
}