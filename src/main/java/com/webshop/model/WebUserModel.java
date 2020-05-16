package com.webshop.model;

import com.webshop.validator.UniqueEmail;
import com.webshop.validator.UniqueUsername;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@RequiredArgsConstructor
public class WebUserModel {
    @NotNull
    @UniqueUsername
    @Column(name="username")
    private String username;

    @NotNull
    @UniqueEmail
    @Email(message="{email.notValid}")
    @Column(name="email")
    private String email;

    @NotNull
    @Column(name="role")
    private String role;

}
