package com.webshop.kung.validator;

import com.webshop.kung.repository.UserRepository;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return username != null && userRepository.findByUsername(username) == null;
    }
}
