package com.webshop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class TOTPDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String username;

    @Column
    private String secret;

    public TOTPDetails() {}

    public TOTPDetails(String username, String secret) {
        setUsername(username);
        setSecret(secret);
    }

}
