package com.example.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.example.validation.InvalidMobileConstraintValidator;

@Documented
@Constraint(validatedBy = {InvalidMobileConstraintValidator.class})
@Target(FIELD)
@Retention(RUNTIME)
public @interface InvalidMobile {
    String message() default "{javax.validation.constraints.InvalidMobile.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
