package com.webshop.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InStockValidator.class)
public @interface InStock {
    String message() default "DEMAND EXCEEDS STOCK";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
