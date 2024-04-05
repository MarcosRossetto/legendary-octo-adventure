package org.acme.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class PasswordValidationUtil {

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 20;
    private static final String SPECIAL_CHARACTERS_PATTERN = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]";

    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public static boolean isValidPassword(String password, StringBuilder message) {
        
        if (password == null) {
            message.append(messages.getString("VALIDATION.PASSWORD.NOT_NULL"));
            return false;
        }

        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            message.append(MessageFormat.format(messages.getString("VALIDATION.PASSWORD.LENGTH"), PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH));
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            message.append(messages.getString("VALIDATION.PASSWORD.LOWERCASE"));
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            message.append(messages.getString("VALIDATION.PASSWORD.UPPERCASE"));
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            message.append(messages.getString("VALIDATION.PASSWORD.DIGIT"));
            return false;
        }

        if (!password.matches(".*" + SPECIAL_CHARACTERS_PATTERN + ".*")) {
            message.append(MessageFormat.format(messages.getString("VALIDATION.PASSWORD.SPECIAL_CHARACTER"), SPECIAL_CHARACTERS_PATTERN));
            return false;
        }

        return true;
    }
}
