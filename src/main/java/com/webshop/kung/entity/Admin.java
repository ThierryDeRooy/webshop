package com.webshop.kung.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="customer_id")
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



}
