package com.webshop.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Locale;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class WebUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @NotNull
    @Column(name="username")
    private String username;

    @NotNull
    @Email
    @Column(name="email")
    private String email;

    @NotNull
    @Column(name="password")
    private String password;

    @NotNull
    @Column(name="role")
    private String role;

    @NotNull
    private boolean totpEnabled;

    @Column(name="last_login_time")
    private Date lastLoginTime;

    @Column(name="wrong_tries")
    private int wrongTries;

    @Setter
    @Getter
    private boolean verified;


    public WebUser(String username, String email, String encode, String role, boolean totpEnabled, Date date, int wrongTries) {
        setUsername(username);
        setEmail(email);
        setPassword(encode);
        setRole(role);
        setTotpEnabled(totpEnabled);
        setLastLoginTime(date);
        setWrongTries(wrongTries);
    }
}
