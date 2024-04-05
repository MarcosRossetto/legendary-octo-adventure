package org.acme.config.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import org.acme.config.PasswordChangeValidator;

@Documented
@Constraint(validatedBy = PasswordChangeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPasswordChange {
    String message() default "Invalid password change request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
