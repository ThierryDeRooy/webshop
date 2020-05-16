package com.webshop.model;

import com.webshop.validator.*;
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
@Recaptcha
public class UserRegistration {

    @NotEmpty(message="{firstname.missing}")
    private String firstname;
    @NotEmpty(message="{familyname.missing}")
    private String lastname;
    @NotEmpty(message="{username.missing}")
    @UniqueUsername
    private String username;
    @NotEmpty(message="{email.missing}")
    @UniqueEmail
    @Email(message="{email.notValid}")
    private String email;
    @NotEmpty(message="{password.missing}")
    @PasswordPolicy
    private String password;
    @NotEmpty(message="{passwordConfirm.missing}")
    private String confirmPassword;
}
