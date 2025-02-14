package com.tiktok.project.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AccountValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAccount {
    String message() default "Invalid account format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
