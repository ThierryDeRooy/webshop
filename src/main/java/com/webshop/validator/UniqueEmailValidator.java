package com.webshop.validator;

import com.webshop.repository.WebUserRepository;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private WebUserRepository repository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && repository.findByEmail(email.toLowerCase()) == null ;
    }

}
