package com.tiktok.project.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DifferentUsersValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentUsers {
    String message() default "User IDs must be different";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}