package com.webshop.kung.validator;

import com.webshop.kung.repository.UserRepository;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private UserRepository repository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && repository.findByEmail(email) == null ;
    }

}
