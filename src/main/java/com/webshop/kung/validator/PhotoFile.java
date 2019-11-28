package com.webshop.kung.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhotoFileValidator.class)
@Documented
public @interface PhotoFile {
    String message() default "Please enter valid image (jsp or png)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
