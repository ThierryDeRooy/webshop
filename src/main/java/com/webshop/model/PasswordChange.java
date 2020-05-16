package com.webshop.model;

import com.webshop.validator.PasswordConfirmed;
import com.webshop.validator.PasswordPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@PasswordConfirmed
public class PasswordChange {
    private String oldPassword;
    @NotEmpty(message="{password.missing}")
    @PasswordPolicy
    private String password;
    @NotEmpty(message="{passwordConfirm.missing}")
    private String confirmPassword;

}
