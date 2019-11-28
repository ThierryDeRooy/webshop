package com.webshop.kung.model;

import com.webshop.kung.validator.PasswordConfirmed;
import com.webshop.kung.validator.PasswordPolicy;
import com.webshop.kung.validator.UniqueEmail;
import com.webshop.kung.validator.UniqueUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@PasswordConfirmed
public class UserRegistration {

    @NotEmpty(message="Please enter your firstname")
    private String firstname;
    @NotEmpty(message="Please enter your lastname")
    private String lastname;
    @NotEmpty(message="Please enter a username")
    @UniqueUsername
    private String username;
    @NotEmpty(message="Please enter an email")
    @UniqueEmail
    @Email(message="Email is not valid")
    private String email;
    @NotEmpty(message="Please enter in a password")
    @PasswordPolicy
    private String password;
    @NotEmpty(message="Please confirm your password")
    private String confirmPassword;
}
