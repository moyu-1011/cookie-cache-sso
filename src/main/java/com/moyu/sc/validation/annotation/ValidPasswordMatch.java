package com.moyu.sc.validation.annotation;

import com.moyu.sc.validation.PasswordMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface ValidPasswordMatch {
    String message() default "Password Not Match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
