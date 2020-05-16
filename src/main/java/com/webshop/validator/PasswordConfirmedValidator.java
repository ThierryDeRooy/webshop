package com.webshop.validator;

import com.webshop.model.PasswordChange;
import com.webshop.model.UserRegistration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmedValidator implements ConstraintValidator<PasswordConfirmed, Object> {

    @Override
    public boolean isValid(Object user, ConstraintValidatorContext context) {
        String password = null;
        String confirmedPassword = null;
        if (user instanceof UserRegistration) {
            password = ((UserRegistration) user).getPassword();
            confirmedPassword = ((UserRegistration) user).getConfirmPassword();
        } else if (user instanceof PasswordChange) {
            password = ((PasswordChange) user).getPassword();
            confirmedPassword = ((PasswordChange) user).getConfirmPassword();
        }
        if (password == null)
            return false;
        return password.equals(confirmedPassword);
    }

}
