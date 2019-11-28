package com.webshop.kung.validator;

import com.webshop.kung.model.UserRegistration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmedValidator implements ConstraintValidator<PasswordConfirmed, Object> {

    @Override
    public boolean isValid(Object user, ConstraintValidatorContext context) {
        String password = ((UserRegistration)user).getPassword();
        String confirmedPassword = ((UserRegistration)user).getConfirmPassword();
        return password.equals(confirmedPassword);
    }

}
