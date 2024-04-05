package org.acme.config;

import org.acme.config.interfaces.ValidPasswordChange;
import org.acme.dtos.input.UserChangePasswordInputDTO;
import org.acme.utils.PasswordValidationUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordChangeValidator implements ConstraintValidator<ValidPasswordChange, UserChangePasswordInputDTO> {

    @Override
    public boolean isValid(UserChangePasswordInputDTO userChangePasswordInputDTO, ConstraintValidatorContext context) {
        
        if (userChangePasswordInputDTO.getCurrentPassword() == null && userChangePasswordInputDTO.getNewPassword() == null) {
            return true;
        }

        boolean valid = true;

        StringBuilder messageCurrentPassword = new StringBuilder();
        if (!PasswordValidationUtil.isValidPassword(userChangePasswordInputDTO.getCurrentPassword(), messageCurrentPassword)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageCurrentPassword.toString())
                   .addPropertyNode("currentPassword")
                   .addConstraintViolation();
            valid = false;
        }

        StringBuilder messageNewPassword = new StringBuilder();
        if (!PasswordValidationUtil.isValidPassword(userChangePasswordInputDTO.getNewPassword(), messageNewPassword)) {
            if (valid) {
                context.disableDefaultConstraintViolation();
            }
            context.buildConstraintViolationWithTemplate(messageNewPassword.toString())
                   .addPropertyNode("newPassword")
                   .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
